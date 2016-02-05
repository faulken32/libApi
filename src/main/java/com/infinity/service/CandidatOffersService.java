/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.infinity.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.infinity.dto.CandidatOffers;

import java.io.IOException;
import java.util.ArrayList;
import org.elasticsearch.action.get.GetResponse;
import org.elasticsearch.action.index.IndexResponse;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.client.transport.TransportClient;
import org.elasticsearch.index.query.QueryBuilder;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.search.SearchHit;
import org.slf4j.Logger;

import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 *
 * @author t311372
 */
@Service
public class CandidatOffersService {

    private static final Logger LOG = LoggerFactory.getLogger(CandidatOffersService.class);
    @Autowired
    private ElasticClientConf elasticClientConf;
    private TransportClient client;

    /**
     *
     * @param candidatOffers
     * @return
     */
    public String addCandidatOffers(CandidatOffers candidatOffers) {

        String id = null;

        try {

            client = elasticClientConf.getClient();

            ObjectMapper mapper = new ObjectMapper();

            byte[] json = mapper.writeValueAsBytes(candidatOffers);

            IndexResponse response = client.prepareIndex(elasticClientConf.getINDEX_NAME(), "candidatOffer" , candidatOffers.getId())
                    .setSource(json)
                    .execute()
                    .actionGet();

            id = response.getId();
            client.admin().indices().prepareRefresh().execute().actionGet();

        } catch (JsonProcessingException ex) {

            LOG.error("add candidatOffer error  : " + ex.getMessage());

        }

        return id;
    }

    public CandidatOffers getById(String id) {

        client = elasticClientConf.getClient();
        CandidatOffers readValue = null;
        try {

            GetResponse response = client.
                    prepareGet(elasticClientConf.getINDEX_NAME(), "candidatOffer", id)
                    .execute()
                    .actionGet();

            ObjectMapper mapper = new ObjectMapper();

            readValue = mapper.readValue(response.getSourceAsString(), CandidatOffers.class);
        } catch (NullPointerException | IOException e) {
            LOG.error(e.getMessage());
        }
        return readValue;
    }

    /**
     *
     * @param id
     * @return
     * @throws java.io.IOException
     */
    public ArrayList<CandidatOffers> getByOfferId(String id) throws IOException {

        client = elasticClientConf.getClient();
        CandidatOffers readValue = null;
        ArrayList<CandidatOffers> candidatOffersList = new ArrayList<>();
        try {

            QueryBuilder qb = QueryBuilders.matchQuery("offerId", id);
            SearchResponse response = client.prepareSearch(elasticClientConf.getINDEX_NAME())
                    .setTypes("candidatOffer")
                    .setQuery(qb) // Query
                    .execute()
                    .actionGet();

            SearchHit[] hits = response.getHits().getHits();
            ObjectMapper mapper = new ObjectMapper();

            if (hits.length > 0) {
                for (SearchHit hit : hits) {
                    readValue = mapper.readValue(hit.getSourceAsString(), CandidatOffers.class);
                    candidatOffersList.add(readValue);
                }
            }

        } catch (NullPointerException e) {
            LOG.error(e.getMessage());
        }
        return candidatOffersList;
    }
    
     public ArrayList<CandidatOffers> getByIdSearhText(String id) throws IOException {

        LOG.debug("id du candidat {} ", id);
        client = elasticClientConf.getClient();
//        QueryBuilder qb = QueryBuilders.queryStringQuery(id);
        QueryBuilder qb = QueryBuilders.matchQuery("partialCandidat.id", id);
        SearchResponse response = client.prepareSearch(elasticClientConf.getINDEX_NAME())
                .setTypes("candidatOffer")
                .setQuery(qb)
                .setFrom(0).setSize(100).setExplain(true)
                .execute()
                .actionGet();

        SearchHit[] hits = response.getHits().getHits();
        ObjectMapper mapper = new ObjectMapper();
        ArrayList<CandidatOffers> candidateOffersList = new ArrayList<>();

        if (hits.length > 0) {
            for (SearchHit hit : hits) {
                CandidatOffers readValue = mapper.readValue(hit.getSourceAsString(), CandidatOffers.class);
                readValue.setId(hit.getId());
                candidateOffersList.add(readValue);
            }
        }
        return candidateOffersList;

    }

}
