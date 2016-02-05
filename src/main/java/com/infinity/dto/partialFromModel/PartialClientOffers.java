/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.infinity.dto.partialFromModel;


import com.infinity.dto.PartialsClients;
import java.util.ArrayList;



/**
 *
 * @author t311372
 */
public class PartialClientOffers {
    
    
    private String id;
    private String title;
    private String desc;
    private int expTotal;
    private String profileName;
    private String profiType;
    private PartialsClients partialsClients;
    private ArrayList<String> expCriteria;

    public ArrayList<String> getExpCriteria() {
        return expCriteria;
    }

    public void setExpCriteria(ArrayList<String> expCriteria) {
        this.expCriteria = expCriteria;
    }
    

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }

    public int getExpTotal() {
        return expTotal;
    }

    public void setExpTotal(int expTotal) {
        this.expTotal = expTotal;
    }

    public String getProfileName() {
        return profileName;
    }

    public void setProfileName(String profileName) {
        this.profileName = profileName;
    }

    public String getProfiType() {
        return profiType;
    }

    public void setProfiType(String profiType) {
        this.profiType = profiType;
    }

    public PartialsClients getPartialsClients() {
        return partialsClients;
    }

    public void setPartialsClients(PartialsClients partialsClients) {
        this.partialsClients = partialsClients;
    }

  

   
 
    
    
}
