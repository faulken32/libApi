/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.infinity.dto;

import java.util.HashMap;

/**
 *
 * @author Utilisateur
 */
public class CandidatEnum {

    
    
    private static final String NO_SEARCH = "nosearch";
    private static final String LISTEN = "listen";
    private static final String ACTIVE = "active";
    private final HashMap<String , String> statusList;
    
    
    public CandidatEnum() {
        
        statusList = new HashMap<>();
        statusList.put(ACTIVE, "Recherche active");
        statusList.put(NO_SEARCH, "Pas en recherche");
        statusList.put(LISTEN, "à l'écoute du marché");
        
        
    }

    public HashMap<String, String> getStatusList() {
        return statusList;
    }
    
     
    
    
    
    

}
