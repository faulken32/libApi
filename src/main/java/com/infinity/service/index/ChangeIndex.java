/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.infinity.service.index;

import org.elasticsearch.action.bulk.BulkProcessor;
import org.elasticsearch.action.bulk.BulkRequest;
import org.elasticsearch.action.bulk.BulkResponse;
import org.elasticsearch.action.index.IndexRequest;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.action.search.SearchType;
import org.elasticsearch.client.Client;
import org.elasticsearch.client.transport.TransportClient;
import org.elasticsearch.common.transport.InetSocketTransportAddress;
import org.elasticsearch.common.unit.ByteSizeUnit;
import org.elasticsearch.common.unit.ByteSizeValue;
import org.elasticsearch.common.unit.TimeValue;
import static org.elasticsearch.index.query.QueryBuilders.matchAllQuery;
import org.elasticsearch.search.SearchHit;

/**
 *
 * @author t311372
 */
public class ChangeIndex {

    public ChangeIndex() {

        Client client = new TransportClient()
                .addTransportAddress(new InetSocketTransportAddress("127.0.0.1", 9300));
               

        SearchResponse searchResponse = client.prepareSearch("cvdb")
                             .setQuery(matchAllQuery())
                .setSearchType(SearchType.SCAN)
                .setScroll(new TimeValue(60000))
                .setSize(50).execute().actionGet();

        BulkProcessor bulkProcessor = BulkProcessor.builder(
                client,
                new BulkProcessor.Listener() {

                    @Override
                    public void beforeBulk(long executionId, BulkRequest request) {

                    }

                    @Override
                    public void afterBulk(long executionId, BulkRequest request, BulkResponse response) {

                    }

                    @Override
                    public void afterBulk(long executionId, BulkRequest request, Throwable failure) {

                    }

                })
                .setBulkActions(10000)
                .setBulkSize(new ByteSizeValue(1, ByteSizeUnit.GB))
                .setFlushInterval(TimeValue.timeValueSeconds(5))
                .setConcurrentRequests(1)
                .build();

        while (true) {
            searchResponse = client.prepareSearchScroll(searchResponse.getScrollId())
                    .setScroll((new TimeValue(60000))).execute().actionGet();

            if (searchResponse.getHits().getHits().length == 0) {

                bulkProcessor.close();
                break; //Break condition: No hits are returned
            }

            for (SearchHit hit : searchResponse.getHits()) {
                IndexRequest request = new IndexRequest("cvdb2", hit.type(), hit.id());
                request.source(hit.sourceRef());
                bulkProcessor.add(request);
            }
        }
    }
}
