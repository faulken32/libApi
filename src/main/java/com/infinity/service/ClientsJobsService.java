/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.infinity.service;

import com.infinity.dto.ClientOffers;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;
import javax.annotation.PostConstruct;
import org.elasticsearch.action.get.GetResponse;
import org.elasticsearch.action.index.IndexResponse;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.action.update.UpdateRequest;
import org.elasticsearch.action.update.UpdateResponse;
import org.elasticsearch.client.transport.TransportClient;
import org.elasticsearch.index.query.BoolQueryBuilder;
import org.elasticsearch.index.query.QueryBuilder;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.search.SearchHit;
import org.elasticsearch.search.sort.ScoreSortBuilder;
import org.elasticsearch.search.sort.SortBuilders;
import org.elasticsearch.search.sort.SortOrder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 *
 * @author t311372
 */
@Service
public class ClientsJobsService {

    private static final Logger LOG = LoggerFactory.getLogger(CandidatOffersService.class);
    @Autowired
    private ElasticClientConf elasticClientConf;
    private TransportClient client;
    private static final String TYPE = "jobs";

    @PostConstruct
    private void init() {
        this.client = elasticClientConf.getClient();
    }

    /**
     *
     * @param clientOffers
     * @return
     * @throws JsonProcessingException
     */
    public String addJobs(ClientOffers clientOffers) throws JsonProcessingException {

    

        ObjectMapper mapper = new ObjectMapper();

        byte[] json = mapper.writeValueAsBytes(clientOffers);

        IndexResponse response = client.prepareIndex(elasticClientConf.getINDEX_NAME(), "jobs")
                .setSource(json)
                .execute()
                .actionGet();

        String id = response.getId();
        client.admin().indices().prepareRefresh().execute().actionGet();
        return id;
    }

    /**
     *
     * @param jobs
     * @return
     * @throws IOException
     * @throws InterruptedException
     * @throws ExecutionException
     */
    public long updateOneById(ClientOffers jobs) throws IOException, InterruptedException, ExecutionException {

     
        ObjectMapper mapper = new ObjectMapper();

        byte[] json = mapper.writeValueAsBytes(jobs);

        UpdateRequest updateRequest = new UpdateRequest();
        updateRequest.index(elasticClientConf.getINDEX_NAME());
        updateRequest.type("jobs");
        updateRequest.id(jobs.getId());
        updateRequest.doc(json);

        UpdateResponse get = client.update(updateRequest).get();
        long version = get.getVersion();
        client.admin().indices().prepareRefresh().execute().actionGet();

        return version;
    }

    /**
     *
     * @param id
     * @return ClientOffers
     * @throws IOException
     */
    public ClientOffers getById(String id) throws IOException {

     
        ClientOffers readValue = null;
        try {

            GetResponse response = client.
                    prepareGet(elasticClientConf.getINDEX_NAME(), "jobs", id)
                    .execute()
                    .actionGet();

            ObjectMapper mapper = new ObjectMapper();
            readValue = mapper.readValue(response.getSourceAsString(), ClientOffers.class);
        } catch (NullPointerException e) {

            LOG.error(e.getMessage(), e);
            return null;
        }
        return readValue;
    }

    public ArrayList<ClientOffers> getAll() throws IOException {

      

        QueryBuilder qb = QueryBuilders.matchAllQuery();
        SearchResponse response = client.prepareSearch(elasticClientConf.getINDEX_NAME())
                .setTypes("jobs")
                .setQuery(qb) // Query
                .execute()
                .actionGet();

        SearchHit[] hits = response.getHits().getHits();
        ObjectMapper mapper = new ObjectMapper();
        ArrayList<ClientOffers> ClientOffersList = new ArrayList<>();

        if (hits.length > 0) {
            for (SearchHit hit : hits) {
                ClientOffers readValue = mapper.readValue(hit.getSourceAsString(), ClientOffers.class);
                readValue.setId(hit.getId());
                ClientOffersList.add(readValue);
            }
        }
        return ClientOffersList;

    }

    public ArrayList<ClientOffers> getAllByClientId(String id) throws IOException {

       

        QueryBuilder qb = QueryBuilders.matchQuery("partialsClients.id", id);
        SearchResponse response = client.prepareSearch(elasticClientConf.getINDEX_NAME())
                .setTypes("jobs")
                .setQuery(qb) // Query
                .execute()
                .actionGet();

        SearchHit[] hits = response.getHits().getHits();
        ObjectMapper mapper = new ObjectMapper();
        ArrayList<ClientOffers> ClientOffersList = new ArrayList<>();

        if (hits.length > 0) {
            for (SearchHit hit : hits) {
                ClientOffers readValue = mapper.readValue(hit.getSourceAsString(), ClientOffers.class);
                readValue.setId(hit.getId());
                ClientOffersList.add(readValue);
            }
        }
        return ClientOffersList;

    }

    /**
     *
     * @param departement
     * @param text
     * @return
     * @throws IOException
     */
    public List<ClientOffers> findByTerms(String departement, String text) throws IOException {

       

        List<ClientOffers> clientOffersList = new ArrayList<>();
        BoolQueryBuilder qb = QueryBuilders.boolQuery();

        qb.must(QueryBuilders.queryStringQuery(text));
        qb.must(QueryBuilders.termQuery("dep", departement));
        qb.must(QueryBuilders.termQuery("publish", "true"));
        qb.must(QueryBuilders.termQuery("adminValidate", "true"));
        ScoreSortBuilder scoreSort = SortBuilders.scoreSort();
        SearchResponse response = client.prepareSearch(elasticClientConf.getINDEX_NAME())
                .setTypes("jobs")
                .setQuery(qb)
                .setFrom(0).setSize(50).setExplain(true)
                .addSort(scoreSort.order(SortOrder.ASC))
                .execute()
                .actionGet();
        SearchHit[] hits = response.getHits().getHits();
        ObjectMapper mapper = new ObjectMapper();

        if (hits.length > 0) {
            for (SearchHit hit : hits) {
                ClientOffers readValue = mapper.readValue(hit.getSourceAsString(), ClientOffers.class);
                clientOffersList.add(readValue);
            }
        }

        return clientOffersList;
    }

    /**
     *
     * @return @throws IOException
     */
    public List<ClientOffers> findNonValidClientOffers() throws IOException {

     
        List<ClientOffers> clientOffersList = new ArrayList<>();

        QueryBuilder qb = QueryBuilders.matchQuery("adminValidate", Boolean.FALSE);
        SearchResponse response = client.prepareSearch(elasticClientConf.getINDEX_NAME())
                .setTypes("jobs")
                .setQuery(qb)
                .execute()
                .actionGet();

        SearchHit[] hits = response.getHits().getHits();
        ObjectMapper mapper = new ObjectMapper();

        if (hits.length > 0) {
            for (SearchHit hit : hits) {
                ClientOffers readValue = mapper.readValue(hit.getSourceAsString(), ClientOffers.class);
                readValue.setId(hit.getId());
                clientOffersList.add(readValue);
            }
        }

        return clientOffersList;

    }

    /**
     *
     * @param id
     */
    public void deleteById(String id) {

        client.prepareDelete(elasticClientConf.getINDEX_NAME(), ClientsJobsService.TYPE, id)
                .execute()
                .actionGet();

        client.admin().indices().prepareRefresh().execute().actionGet();

    }
}
