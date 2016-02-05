/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.infinity.service;

import org.elasticsearch.client.transport.TransportClient;
import org.elasticsearch.common.transport.InetSocketTransportAddress;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.DisposableBean;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

/**
 *
 * @author Utilisateur
 */
@Service
public class ElasticClientConf implements InitializingBean, DisposableBean {

    private TransportClient client;
    private static final Logger LOG = LoggerFactory
            .getLogger(ElasticClientConf.class);

    @Value("${ES_IP1}")
    private String ES_IP1;

    @Value("${INDEX_NAME}")
    private String INDEX_NAME;

    public String getINDEX_NAME() {
        return INDEX_NAME;
    }

    public TransportClient getClient() {
        return client;
    }

    public void setClient(TransportClient client) {
        this.client = client;
    }

//      @Override
//    public void afterPropertiesSet() throws Exception {
////           Settings settings = ImmutableSettings.settingsBuilder()
////                .put("client.transport.sniff", true)
////                .put("cluster.name", "recetteES")
////                .build();
//        
//       
//                
////        client =   new TransportClient(settings)
////                .addTransportAddress(new InetSocketTransportAddress("10.60.12.44", 9300));
//        
//        client = new TransportClient()
//                .addTransportAddress(new InetSocketTransportAddress("10.60.198.75", 9300))
//                .addTransportAddress(new InetSocketTransportAddress("10.60.198.75", 9301));
//        
//        
//        LOG.info("client created");
//    }
    @Override
    public void destroy() {

        if (client != null) {
            client.close();
        }

    }

    @Override
    public void afterPropertiesSet() {
        client = new TransportClient()
                .addTransportAddress(new InetSocketTransportAddress(this.ES_IP1, 9300));
//                .
//                addTransportAddress(new InetSocketTransportAddress(this.ES_IP1, 9301))
//                .
//                addTransportAddress(new InetSocketTransportAddress(this.ES_IP1, 9302));

        LOG.info("client créated");
    }
}
