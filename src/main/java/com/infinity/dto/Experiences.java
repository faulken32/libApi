/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.infinity.dto;

import java.io.Serializable;
import java.util.List;


/**
 * 
 * 
 * 
 
  {
    "properties": {
          "end": {
            "type": "date",
            "format": "dateOptionalTime"
          },
          "partialCandidat": {
            "properties": {
              "id": {
                "type": "string",
            "index" : "not_analyzed"
              },
              "name": {
                "type": "string",
            "index" : "not_analyzed"
              }
            }
          },
          "start": {
            "type": "date",
            "format": "dateOptionalTime"
          },
          "tecnoList": {
            "type": "string",
            "index" : "not_analyzed"
          },
          "title": {
            "type": "string"
          }
        }
    }
 */


/**
 *
 * @author Utilisateur
 */
public class Experiences implements Serializable{
          
    private String id;
    private String title;
    private String compagny;
    private String start; 
    private String end;
    private String expContend;
    private float duration;
    private PartialCandidat partialCandidat;
    private List<String> tecnoList;
       
    
    
    public Experiences() {
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

    public String getCompagny() {
        return compagny;
    }

    public void setCompagny(String compagny) {
        this.compagny = compagny;
    }

    public String getStart() {
        return start;
    }

    public void setStart(String start) {
        this.start = start;
    }

    public String getEnd() {
        return end;
    }

    public void setEnd(String end) {
        this.end = end;
    }

    public String getExpContend() {
        return expContend;
    }

    public void setExpContend(String expContend) {
        this.expContend = expContend;
    }
 
    public List<String> getTecnoList() {
        return tecnoList;
    }

    public void setTecnoList(List<String> tecnoList) {
        this.tecnoList = tecnoList;
    }

    public PartialCandidat getPartialCandidat() {
        return partialCandidat;
    }

    public void setPartialCandidat(PartialCandidat partialCandidat) {
        this.partialCandidat = partialCandidat;
    }

    public float getDuration() {
        return duration;
    }

    public void setDuration(float duration) {
        this.duration = duration;
    }

  
   
    
}
