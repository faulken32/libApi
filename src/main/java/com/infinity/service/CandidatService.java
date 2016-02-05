/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.infinity.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.infinity.dto.Candidat;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.concurrent.ExecutionException;
import javax.annotation.PostConstruct;
import org.elasticsearch.ElasticsearchException;
import org.elasticsearch.action.get.GetResponse;
import org.elasticsearch.action.index.IndexResponse;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.action.update.UpdateRequest;
import org.elasticsearch.action.update.UpdateResponse;
import org.elasticsearch.client.transport.TransportClient;
import org.elasticsearch.index.query.BoolQueryBuilder;
import org.elasticsearch.index.query.FilterBuilders;
import org.elasticsearch.index.query.QueryBuilder;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.index.query.RangeFilterBuilder;
import org.elasticsearch.search.SearchHit;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 *
 * @author Utilisateur
 */
@Service
public class CandidatService {

    private static final org.slf4j.Logger LOG = LoggerFactory.getLogger(CandidatService.class);
    @Autowired
    private ElasticClientConf elasticClientConf;
    private TransportClient client;

    @PostConstruct
    private void init() {
        this.client = elasticClientConf.getClient();
    }

    public String addCandidat(Candidat candidat) {

        String id = "";

        try {
            client = elasticClientConf.getClient();

            ObjectMapper mapper = new ObjectMapper();

            byte[] json = mapper.writeValueAsBytes(candidat);

            IndexResponse response = client.prepareIndex(elasticClientConf.getINDEX_NAME(), "candidat")
                    .setSource(json)
                    .execute()
                    .actionGet();

            id = response.getId();
            client.admin().indices().prepareRefresh().execute().actionGet();

        } catch (JsonProcessingException ex) {

            LOG.error("add candidat error  : " + ex.getMessage());

        }

        return id;
    }

    public Candidat getById(String id) throws IOException {

       
        Candidat readValue = null;
      

            GetResponse response = client.
                    prepareGet(elasticClientConf.getINDEX_NAME(), "candidat", id)
                    .execute()
                    .actionGet();

            ObjectMapper mapper = new ObjectMapper();
            readValue = mapper.readValue(response.getSourceAsString(), Candidat.class);
        
        return readValue;
    }

    public long updateOneById(Candidat candidat) {

     
        ObjectMapper mapper = new ObjectMapper();
        long version = 0;
        try {

            byte[] json = mapper.writeValueAsBytes(candidat);

            UpdateRequest updateRequest = new UpdateRequest();
            updateRequest.index(elasticClientConf.getINDEX_NAME());
            updateRequest.type("candidat");
            updateRequest.id(candidat.getId());
            updateRequest.doc(json);

            UpdateResponse get = client.update(updateRequest).get();
            version = get.getVersion();
            client.admin().indices().prepareRefresh().execute().actionGet();

        } catch (JsonProcessingException | InterruptedException | ExecutionException | ElasticsearchException e) {

            LOG.error(e.getMessage());
        }
        return version;
    }

    public ArrayList<Candidat> getAll() throws IOException {

       
        QueryBuilder qb = QueryBuilders.matchAllQuery();
        SearchResponse response = client.prepareSearch(elasticClientConf.getINDEX_NAME())
                .setTypes("candidat")
                .setQuery(qb) // Query
                .setFrom(0).setSize(100).setExplain(true)
                .execute()
                .actionGet();

        SearchHit[] hits = response.getHits().getHits();
        ObjectMapper mapper = new ObjectMapper();
        ArrayList<Candidat> candidatList = new ArrayList<>();

        if (hits.length > 0) {
            for (SearchHit hit : hits) {
                Candidat readValue = mapper.readValue(hit.getSourceAsString(), Candidat.class);
                readValue.setId(hit.getId());
                candidatList.add(readValue);
            }
        }
        return candidatList;

    }

    public ArrayList<Candidat> getByName(final String name) throws IOException {

     
//        QueryBuilder qb = QueryBuilders.queryStringQuery(name);
        QueryBuilder qb = QueryBuilders.prefixQuery("name", name);
        SearchResponse response = client.prepareSearch(elasticClientConf.getINDEX_NAME())
                .setTypes("candidat")
                .setQuery(qb)
                .setFrom(0).setSize(100).setExplain(true)
                //                 .addSort(fieldSort("end").order(DESC).missing("_last"))// Query
                .execute()
                .actionGet();

        SearchHit[] hits = response.getHits().getHits();
        ObjectMapper mapper = new ObjectMapper();
        ArrayList<Candidat> candidat = new ArrayList<>();

        if (hits.length > 0) {
            for (SearchHit hit : hits) {
                Candidat readValue = mapper.readValue(hit.getSourceAsString(), Candidat.class);
                readValue.setId(hit.getId());
                candidat.add(readValue);
            }
        }
        return candidat;

    }

    public ArrayList<Candidat> getByEmail(final String email) throws IOException {

      
//        QueryBuilder qb = QueryBuilders.queryStringQuery(name);
        QueryBuilder qb = QueryBuilders.prefixQuery("email", email);
        SearchResponse response = client.prepareSearch(elasticClientConf.getINDEX_NAME())
                .setTypes("candidat")
                .setQuery(qb)
                .setFrom(0).setSize(100).setExplain(true)
                //                 .addSort(fieldSort("end").order(DESC).missing("_last"))// Query
                .execute()
                .actionGet();

        SearchHit[] hits = response.getHits().getHits();
        ObjectMapper mapper = new ObjectMapper();
        ArrayList<Candidat> candidat = new ArrayList<>();

        if (hits.length > 0) {
            for (SearchHit hit : hits) {
                Candidat readValue = mapper.readValue(hit.getSourceAsString(), Candidat.class);
                readValue.setId(hit.getId());
                candidat.add(readValue);
            }
        }
        return candidat;

    }

    public ArrayList<Candidat> powerSearch(final String mobilite, final String terms) throws IOException {

     
//        criteriaFromDb = clientsJobsService.getById(jobOfferId);
        ArrayList<Candidat> candidatList = new ArrayList<>();

        BoolQueryBuilder qb = QueryBuilders.boolQuery();
        //QueryBuilders.multiMatchQuery(terms , "cvContends^6","mobilite")

        qb.must(QueryBuilders.matchQuery("cvContends", terms).boost(20));
        qb.should(QueryBuilders.termQuery("mobilite", mobilite));
        qb.mustNot(QueryBuilders.termQuery("status", "nosearch"));

        SearchResponse response = client.prepareSearch(elasticClientConf.getINDEX_NAME())
                .setTypes("candidat")
                .setQuery(qb)
                .setFrom(0).setSize(100).setExplain(true)
                //                 .addSort(fieldSort("end").order(DESC).missing("_last"))// Query
                .execute()
                .actionGet();

        SearchHit[] hits = response.getHits().getHits();
        float maxScore = response.getHits().getMaxScore();

        LOG.debug("SCORE : {}", maxScore);
        ObjectMapper mapper = new ObjectMapper();

        if (hits.length > 0) {
            for (SearchHit hit : hits) {
                Candidat readValue = mapper.readValue(hit.getSourceAsString(), Candidat.class);
                candidatList.add(readValue);
            }
        }

        return candidatList;

    }

    public List<Candidat> findLastMonthOfInavtivity() throws IOException {

     
        List<Candidat> matchingCandidat = new ArrayList<>();
        Calendar c = Calendar.getInstance();
     
        c.add(Calendar.MONTH, -1);

        SimpleDateFormat sp = new SimpleDateFormat("yyyy-MM-dd");
        LOG.debug(sp.format(c.getTime()));

        
        
        RangeFilterBuilder to = FilterBuilders.rangeFilter("updateDate").lte(sp.format(c.getTime()));
        QueryBuilder qb = QueryBuilders.filteredQuery(QueryBuilders.matchAllQuery(), to);

        SearchResponse response = client.prepareSearch(elasticClientConf.getINDEX_NAME())
                .setTypes("candidat")
                .setQuery(qb)
                .setFrom(0).setSize(10000).setExplain(true)
                .execute()
                .actionGet();

        SearchHit[] hits = response.getHits().getHits();

        ObjectMapper mapper = new ObjectMapper();

        if (hits.length > 0) {
            for (SearchHit hit : hits) {
                Candidat readValue = mapper.readValue(hit.getSourceAsString(), Candidat.class);
                matchingCandidat.add(readValue);
            }
        }

        return matchingCandidat;

    }

}
