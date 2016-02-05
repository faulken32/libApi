/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.infinity.service;


import com.infinity.dto.Comments;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.IOException;
import java.util.ArrayList;
import java.util.concurrent.ExecutionException;
import org.elasticsearch.action.get.GetResponse;
import org.elasticsearch.action.index.IndexResponse;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.action.update.UpdateRequest;
import org.elasticsearch.action.update.UpdateResponse;
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
public class CommentsService {

    private static final Logger LOG = LoggerFactory.getLogger(CommentsService.class);

    @Autowired
    private ElasticClientConf elasticClientConf;
    private TransportClient client;

    public ArrayList<Comments> getByCandidatId(String id) throws IOException {

        LOG.info("id du candidat {} ", id);
        client = elasticClientConf.getClient();

        QueryBuilder qb = QueryBuilders.matchQuery("partialCandidat.id", id);
        SearchResponse response = client.prepareSearch(elasticClientConf.getINDEX_NAME())
                .setTypes("comments")
                .setQuery(qb) // Query
                .execute()
                .actionGet();

        SearchHit[] hits = response.getHits().getHits();
        ObjectMapper mapper = new ObjectMapper();
        ArrayList<Comments> comments = new ArrayList<>();

        if (hits.length > 0) {
            for (SearchHit hit : hits) {
                Comments readValue = mapper.readValue(hit.getSourceAsString(), Comments.class);
                readValue.setId(hit.getId());
                comments.add(readValue);
            }
        }
        return comments;

    }

    public long updateOneById(Comments comments) throws IOException, InterruptedException, ExecutionException {

        client = elasticClientConf.getClient();
        ObjectMapper mapper = new ObjectMapper();

        byte[] json = mapper.writeValueAsBytes(comments);

        UpdateRequest updateRequest = new UpdateRequest();
        updateRequest.index(elasticClientConf.getINDEX_NAME());
        updateRequest.type("comments");
        updateRequest.id(comments.getId());
        updateRequest.doc(json);

        UpdateResponse get = client.update(updateRequest).get();
        long version = get.getVersion();

        return version;
    }

    public Comments getById(String id) throws IOException {

        client = elasticClientConf.getClient();

        GetResponse response = client.
                prepareGet(elasticClientConf.getINDEX_NAME(), "comments", id)
                .execute()
                .actionGet();

        ObjectMapper mapper = new ObjectMapper();
        Comments readValue = mapper.readValue(response.getSourceAsString(), Comments.class);

        return readValue;
    }

    public long addComments(Comments comments) throws JsonProcessingException {

        client = elasticClientConf.getClient();

        ObjectMapper mapper = new ObjectMapper();

        byte[] json = mapper.writeValueAsBytes(comments);

        IndexResponse response = client.prepareIndex(elasticClientConf.getINDEX_NAME(), "comments")
                .setSource(json)
                .execute()
                .actionGet();

        long version = response.getVersion();
        return version;
    }

}
