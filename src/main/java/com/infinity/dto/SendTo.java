/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.infinity.dto;

import java.util.ArrayList;

/**
 *
 * @author t311372
 */
public class SendTo {
    
    
    private ArrayList<String> email;
    private String clientId;
    private String date;
    private String candidatId;

    public String getCandidatId() {
        return candidatId;
    }

    public void setCandidatId(String candidatId) {
        this.candidatId = candidatId;
    }

    
    public ArrayList<String> getEmail() {
        return email;
    }

    public void setEmail(ArrayList<String> email) {
        this.email = email;
    }  
    
    public String getClientId() {
        return clientId;
    }

    public void setClientId(String clientId) {
        this.clientId = clientId;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }
    
    
    
}
