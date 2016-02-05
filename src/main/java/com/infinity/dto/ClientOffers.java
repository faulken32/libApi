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
public class ClientOffers {
    
    
    private String id;
    private String title;
    private String desc;
    private int expTotalMin;
    private int expTotalMax;
    private String dep;
    private String profileName;
    private String profiType;
    private boolean publish;
    private String lastUpdateDate;
    private boolean adminValidate;
    
    
    private PartialsClients partialsClients;
    private ArrayList<TechnoCriteria> technoCriterias;

    public boolean isPublish() {
        return publish;
    }

    public void setPublish(boolean publish) {
        this.publish = publish;
    }

    public boolean isAdminValidate() {
        return adminValidate;
    }

    public void setAdminValidate(boolean AdminValidate) {
        this.adminValidate = AdminValidate;
    }

    
    
    
    public String getDep() {
        return dep;
    }

    public void setDep(String dep) {
        this.dep = dep;
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

    public int getExpTotalMin() {
        return expTotalMin;
    }

    public void setExpTotalMin(int expTotalMin) {
        this.expTotalMin = expTotalMin;
    }

    public int getExpTotalMax() {
        return expTotalMax;
    }

    public void setExpTotalMax(int expTotalMax) {
        this.expTotalMax = expTotalMax;
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

    public ArrayList<TechnoCriteria> getTechnoCriterias() {
        return technoCriterias;
    }

    public void setTechnoCriterias(ArrayList<TechnoCriteria> technoCriterias) {
        this.technoCriterias = technoCriterias;
    }

   

    public String getLastUpdateDate() {
        return lastUpdateDate;
    }

    public void setLastUpdateDate(String date) {
        this.lastUpdateDate = date;
    }

    
    
    
    
    
}
