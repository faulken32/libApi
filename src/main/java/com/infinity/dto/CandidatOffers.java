/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.infinity.dto;

import com.fasterxml.jackson.annotation.JsonProperty;



/**
 *
 * @author t311372
 */
public class CandidatOffers {
    
    @JsonProperty("_id")
    private String id;
    
    private PartialCandidat partialCandidat;
    private String offerId;
    private String date;

    public String getId() {
        return id;
    }
    
    public void setId(String id) {
        this.id = id;
    }

    public PartialCandidat getPartialCandidat() {
        return partialCandidat;
    }

    public void setPartialCandidat(PartialCandidat partialCandidat) {
        this.partialCandidat = partialCandidat;
    }

    public String getOfferId() {
        return offerId;
    }

    public void setOfferId(String offerId) {
        this.offerId = offerId;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }
    
    
    
}
