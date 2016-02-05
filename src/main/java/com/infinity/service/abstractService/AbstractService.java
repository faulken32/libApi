/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.infinity.service.abstractService;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.infinity.service.ElasticClientConf;
import java.io.IOException;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;

import javax.annotation.PostConstruct;
import org.elasticsearch.action.get.GetResponse;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.action.update.UpdateRequest;
import org.elasticsearch.client.transport.TransportClient;
import org.elasticsearch.index.query.QueryBuilder;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.search.SearchHit;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

/**
 *
 * @author t311372
 */
abstract public class AbstractService {

    @Autowired
    protected ElasticClientConf elasticClientConf;
    protected TransportClient client;
    private static final Logger LOG = LoggerFactory.getLogger(AbstractService.class);

    @PostConstruct
    protected void init() {

        this.client = elasticClientConf.getClient();

    }

    /**
     *
     * @param <T>
     * @param id
     * @param type
     * @param classType
     * @return
     * @throws java.io.IOException
     */
    public <T extends Object> T getById(String id, String type, Class<T> classType) throws IOException {

        client = elasticClientConf.getClient();
        GetResponse response = client.
                prepareGet(elasticClientConf.getINDEX_NAME(), type, id)
                .execute()
                .actionGet();

        ObjectMapper mapper = new ObjectMapper();
        Class<T> readValue = mapper.readValue(response.getSourceAsString(), classType.getClass());
        client.admin().indices().prepareRefresh().execute().actionGet();
        return (T) readValue;

    }

    /**
     * generic add object into ES
     *
     * @param o
     * @param type
     * @throws JsonProcessingException
     * @throws NoSuchFieldException
     * @throws IllegalArgumentException
     * @throws IllegalAccessException
     */
    public void add(Object o, String type) throws JsonProcessingException, NoSuchFieldException, IllegalArgumentException, IllegalAccessException {

    

        ObjectMapper mapper = new ObjectMapper();

        byte[] json = mapper.writeValueAsBytes(o);

        Class<? extends Object> aClass = o.getClass();
        Field declaredField = aClass.getDeclaredField("id");
        declaredField.setAccessible(true);
        Object get = declaredField.get(o);

        client.prepareIndex(elasticClientConf.getINDEX_NAME(), type, get.toString())
                .setSource(json)
                .execute()
                .actionGet();

        client.admin().indices().prepareRefresh().execute().actionGet();

    }

    public List<?> getAll(String type, Class<?> classType) throws IOException {

    
        QueryBuilder qb = QueryBuilders.matchAllQuery();
        SearchResponse response = client.prepareSearch(elasticClientConf.getINDEX_NAME())
                .setTypes(type)
                .setQuery(qb) // Query
                .execute()
                .actionGet();

        SearchHit[] hits = response.getHits().getHits();
        ObjectMapper mapper = new ObjectMapper();
        List<Object> resList = new ArrayList<>();

        if (hits.length > 0) {
            for (SearchHit hit : hits) {
                Object readValue = mapper.readValue(hit.getSourceAsString(), classType);
                resList.add(readValue);
            }
        }
//        return resList;
        return resList;

    }

    public void updateOneById(Object o , String type) throws IllegalArgumentException, NoSuchFieldException, IllegalAccessException, JsonProcessingException, InterruptedException, ExecutionException {

        ObjectMapper mapper = new ObjectMapper();
        Class<? extends Object> aClass = o.getClass();
        Field declaredField = aClass.getDeclaredField("id");
        declaredField.setAccessible(true);
        Object get = declaredField.get(o);
        byte[] json = mapper.writeValueAsBytes(o);

        UpdateRequest updateRequest = new UpdateRequest();
        updateRequest.index(elasticClientConf.getINDEX_NAME());
        updateRequest.type(type);
        updateRequest.id(get.toString());
        updateRequest.doc(json);

        client.update(updateRequest).get();

        client.admin().indices().prepareRefresh().execute().actionGet();

    }

}
