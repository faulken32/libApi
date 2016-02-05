package com.infinity.service;

import com.infinity.dto.Experiences;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;
import org.elasticsearch.action.delete.DeleteResponse;
import org.elasticsearch.action.get.GetResponse;
import org.elasticsearch.action.index.IndexResponse;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.action.update.UpdateRequest;
import org.elasticsearch.action.update.UpdateResponse;
import org.elasticsearch.client.transport.TransportClient;
import org.elasticsearch.index.query.QueryBuilder;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.search.SearchHit;
import org.elasticsearch.search.aggregations.AggregationBuilders;
import org.elasticsearch.search.aggregations.bucket.terms.Terms;
import org.elasticsearch.search.aggregations.bucket.terms.TermsBuilder;
import static org.elasticsearch.search.sort.SortBuilders.fieldSort;
import static org.elasticsearch.search.sort.SortOrder.DESC;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 *
 * @author Utilisateur
 */
@Service
public class ExpService {

    private static final Logger LOG = LoggerFactory.getLogger(ExpService.class);
    private static final String TYPE = "exp";
    @Autowired
    private ElasticClientConf elasticClientConf;
    private TransportClient client;

    /**
     * return a list of exp from candidat id
     *
     * @param id candidate id
     * @return ArrayList<Experiences>
     * @throws IOException
     */
    public ArrayList<Experiences> getByIdSearhText(String id) throws IOException {

        LOG.debug("id du candidat {} ", id);
        client = elasticClientConf.getClient();
//        QueryBuilder qb = QueryBuilders.queryStringQuery(id);
        QueryBuilder qb = QueryBuilders.matchQuery("partialCandidat.id", id);
        SearchResponse response = client.prepareSearch(elasticClientConf.getINDEX_NAME())
                .setTypes("exp")
                .setQuery(qb)
                .setFrom(0).setSize(100).setExplain(true)
                .addSort(fieldSort("end").order(DESC).missing("_last"))// Query
                .execute()
                .actionGet();

        SearchHit[] hits = response.getHits().getHits();
        ObjectMapper mapper = new ObjectMapper();
        ArrayList<Experiences> candidateExp = new ArrayList<>();

        if (hits.length > 0) {
            for (SearchHit hit : hits) {
                Experiences readValue = mapper.readValue(hit.getSourceAsString(), Experiences.class);
                readValue.setId(hit.getId());
                candidateExp.add(readValue);
            }
        }
        return candidateExp;

    }

    /**
     *
     * @param id
     * @return
     */
    public Experiences getById(String id) {

        client = elasticClientConf.getClient();
        GetResponse response = client.
                prepareGet(elasticClientConf.getINDEX_NAME(), "exp", id)
                .execute()
                .actionGet();

        ObjectMapper mapper = new ObjectMapper();
        Experiences readValue = null;
        try {

            readValue = mapper.readValue(response.getSourceAsString(), Experiences.class);
            readValue.setId(id);
        } catch (IOException e) {

            LOG.error(e.getMessage());
        }

        return readValue;

    }

    public long updateById(Experiences exp) throws InterruptedException, JsonProcessingException, ExecutionException, UnsupportedEncodingException {

        client = elasticClientConf.getClient();
        ObjectMapper mapper = new ObjectMapper();

        final Charset utf8 = Charset.forName("UTF-8");
        byte[] json = mapper.writeValueAsBytes(exp);

        String convert = new String(json, utf8);
        byte[] ptext = convert.getBytes("UTF-8");

        UpdateRequest updateRequest = new UpdateRequest();
        updateRequest.index(elasticClientConf.getINDEX_NAME());
        updateRequest.type("exp");
        updateRequest.id(exp.getId());
        updateRequest.doc(ptext);

        UpdateResponse get = client.update(updateRequest).get();
        long version = get.getVersion();
        client.admin().indices().prepareRefresh().execute().actionGet();
        return version;

    }

    public String addExp(Experiences exp) throws JsonProcessingException {

        client = elasticClientConf.getClient();

        ObjectMapper mapper = new ObjectMapper();

        byte[] json = mapper.writeValueAsBytes(exp);

        IndexResponse response = client.prepareIndex(elasticClientConf.getINDEX_NAME(), "exp")
                .setSource(json)
                .execute()
                .actionGet();

        String version = response.getId();
        client.admin().indices().prepareRefresh().execute().actionGet();
        return version;
    }

    public void deleteById(String id) {

        DeleteResponse response = client.prepareDelete(elasticClientConf.getINDEX_NAME(), "exp", id)
                .execute()
                .actionGet();

        long version = response.getVersion();

        client.admin().indices().prepareRefresh().execute().actionGet();

    }

    /**
     *
     * @param id
     * @return
     */
    public List<Terms.Bucket> getAgregatedDurationExp(String id) {

        QueryBuilder qb = QueryBuilders.matchQuery("partialCandidat.id", id);
        TermsBuilder subAggregation = AggregationBuilders.terms("by_country").field("tecnoList")
                .subAggregation(AggregationBuilders.sum("by_year")
                        .field("duration")
                );

        SearchResponse actionGet = client.prepareSearch(elasticClientConf.getINDEX_NAME())
                .setTypes("exp")
                .setQuery(qb)
                .addAggregation(subAggregation)
                .execute()
                .actionGet();

        Terms terms = actionGet.getAggregations().get("by_country");
        List<Terms.Bucket> buckets = terms.getBuckets();

        return buckets;
    }

}
