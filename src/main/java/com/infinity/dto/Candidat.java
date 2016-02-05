/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.infinity.dto;


import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import org.elasticsearch.common.Nullable;


/**
 *
 * @author ncanicatti
 */
public class Candidat  implements Serializable{
    
    
    
    
    private String _id;
    private String name;
    private int age;
    private String surname;
    private String email;
    private String phone;
    private String cvContends;
    private String status;

    // en mois
    
    
    private float preavis;
    private List mobilite;
    private String enterDate;
    private String updateDate;
    private String lastEmailDate;
    private boolean profiled;
    private boolean autoMaticProfiled;
    private float nbYearExp;
    @Nullable
    private ArrayList<String> language;
    private ArrayList<SendTo> sendTo;
    
   

    public boolean isAutoMaticProfiled() {
        return autoMaticProfiled;
    }

    public void setAutoMaticProfiled(boolean autoMaticProfiled) {
        this.autoMaticProfiled = autoMaticProfiled;
    }
           
    
    public boolean isProfiled() {
        return profiled;
    }

    public void setProfiled(boolean profiled) {
        this.profiled = profiled;
    }
  
    
    public float getPreavis() {
        return preavis;
    }

    public void setPreavis(float preavis) {
        this.preavis = preavis;
    }
       

    public List getMobilite() {
        return mobilite;
    }

    public void setMobilite(List mobilite) {
        this.mobilite = mobilite;
    }

    public String getEnterDate() {
        return enterDate;
    }

    public void setEnterDate(String enterDate) {
        this.enterDate = enterDate;
    }

    public String getUpdateDate() {
        return updateDate;
    }

    public void setUpdateDate(String updateDate) {
        this.updateDate = updateDate;
    }

    public String getLastEmailDate() {
        return lastEmailDate;
    }

    public void setLastEmailDate(String lastEmailDate) {
        this.lastEmailDate = lastEmailDate;
    }        
    
    public String getId() {
        return _id;
    }

    public void setId(String id) {
        this._id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getAge() {
        return age;
    }

    public void setAge(int age) {
        this.age = age;
    }

    public String getSurname() {
        return surname;
    }

    public void setSurname(String surname) {
        this.surname = surname;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getCvContends() {
        return cvContends;
    }

    public void setCvContends(String cvContends) {
        this.cvContends = cvContends;
    }

    public float getNbYearExp() {
        return nbYearExp;
    }

    public void setNbYearExp(float nbYearExp) {
        this.nbYearExp = nbYearExp;
    }

   
    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public ArrayList<String> getLanguage() {
        return language;
    }

    public void setLanguage(ArrayList<String> language) {
        this.language = language;
    }

    public ArrayList<SendTo> getSendTo() {
        return sendTo;
    }

    public void setSendTo(ArrayList<SendTo> sendTo) {
        this.sendTo = sendTo;
    }

    
    
}
