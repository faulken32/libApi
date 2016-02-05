/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.infinity.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.infinity.dto.Candidat;
import com.infinity.dto.ClientOffers;
import com.infinity.dto.Experiences;
import com.infinity.dto.TechnoCriteria;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.client.transport.TransportClient;
import org.elasticsearch.index.query.BoolQueryBuilder;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.search.SearchHit;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 *
 * @author t311372
 */
@Service
public class SearchEngineServices {

    @Autowired
    private ElasticClientConf elasticClientConf;
    private TransportClient client;
    @Autowired
    private ClientsJobsService clientsJobsService;
    private ClientOffers criteriaFromDb;
    private ArrayList<Candidat> candidatList;
    private ArrayList<Experiences> exp;

    private ArrayList<Candidat> selectCandidatCriteria(final String jobOfferId) throws IOException {

        client = elasticClientConf.getClient();
        criteriaFromDb = clientsJobsService.getById(jobOfferId);

        candidatList = new ArrayList<>();

        BoolQueryBuilder qb = QueryBuilders.boolQuery();
        if (criteriaFromDb != null && criteriaFromDb.getExpTotalMin() != 0) {

            qb.must(QueryBuilders.rangeQuery("nbYearExp").from(criteriaFromDb.getExpTotalMin()).to(criteriaFromDb.getExpTotalMax()));
            qb.must(QueryBuilders.termQuery("mobilite", criteriaFromDb.getDep()));
            qb.mustNot(QueryBuilders.termQuery("status", "nosearch"));

            SearchResponse response = client.prepareSearch(elasticClientConf.getINDEX_NAME())
                    .setTypes("candidat")
                    .setQuery(qb)
                    .setFrom(0).setSize(100).setExplain(true)
                    //                 .addSort(fieldSort("end").order(DESC).missing("_last"))// Query
                    .execute()
                    .actionGet();

            SearchHit[] hits = response.getHits().getHits();
            ObjectMapper mapper = new ObjectMapper();

            if (hits.length > 0) {
                for (SearchHit hit : hits) {
                    Candidat readValue = mapper.readValue(hit.getSourceAsString(), Candidat.class);
                    candidatList.add(readValue);
                }
            }

        }

        return candidatList;

    }

    /**
     *
     * @return hasmap client job id => array of candidat or null
     * @throws IOException
     */
    public HashMap<ClientOffers, ArrayList<Candidat>> searchEngine() throws IOException {

        HashMap<ClientOffers, ArrayList<Candidat>> resByClientOffer = new HashMap<>();

        ArrayList<ClientOffers> all = clientsJobsService.getAll();

        for (ClientOffers clientOffer : all) {

            candidatList = this.selectCandidatCriteria(clientOffer.getId());
            exp = new ArrayList<>();
            exp = this.selectExpCriteria();

         
          
                
                ArrayList<String> finalRes = this.finalRes();
                if (finalRes != null & !finalRes.isEmpty()) {
                    resByClientOffer.put(clientOffer, candidatList);
                } else {

                    return null;
                }
            }
        
        return resByClientOffer;
    }

    private ArrayList<String> finalRes() {

        ArrayList<String> candidatIdRes = new ArrayList<>();

        if (!candidatList.isEmpty()
                & !exp.isEmpty()) {
            for (Candidat candidat : candidatList) {

                candidatIdRes.add(candidat.getId());
            }
        }

        return candidatIdRes;
    }

    private ArrayList<Experiences> selectExpCriteria() throws IOException {

        if (!candidatList.isEmpty() && criteriaFromDb != null) {
            if (criteriaFromDb.getTechnoCriterias() != null && !criteriaFromDb.getTechnoCriterias().isEmpty()) {
                BoolQueryBuilder qbExpCriteria = QueryBuilders.boolQuery();

                for (TechnoCriteria technoCriteria : criteriaFromDb.getTechnoCriterias()) {

                    qbExpCriteria.should(QueryBuilders.queryStringQuery(technoCriteria.getTechnoName()));
                    qbExpCriteria.must(QueryBuilders.rangeQuery("duration")
                            .from(technoCriteria.getExpDurationStart())
                            .to(technoCriteria.getExpDurationEnd()));

                }
                SearchResponse responseCritetia = client.prepareSearch(elasticClientConf.getINDEX_NAME())
                        .setTypes("exp")
                        .setQuery(qbExpCriteria)
                        .setFrom(0).setSize(100).setExplain(true)
                        //                 .addSort(fieldSort("end").order(DESC).missing("_last"))// Query
                        .execute()
                        .actionGet();

                SearchHit[] hits1 = responseCritetia.getHits().getHits();
                ObjectMapper mapper = new ObjectMapper();
                if (hits1.length > 0) {
                    for (SearchHit hits11 : hits1) {
                        Experiences readValue = mapper.readValue(hits11.getSourceAsString(), Experiences.class);
                        exp.add(readValue);
                    }
                }
            }
        }
        return exp;
    }
}
