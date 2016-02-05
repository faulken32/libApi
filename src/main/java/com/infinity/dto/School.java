/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.infinity.dto;

/**
 *
 * @author t311372
 */
public class School {

    private String _id;
    private String title;
    private String school;
    private String start;
    private String end;
    private String sContend;
    private String date;
    private PartialCandidat partialCandidat;

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    
    
    public String getId() {
        return _id;
    }

    public void setId(String _id) {
        this._id = _id;
    }

    
    
    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getSchool() {
        return school;
    }

    public void setSchool(String school) {
        this.school = school;
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

    public String getsContend() {
        return sContend;
    }

    public void setsContend(String sContend) {
        this.sContend = sContend;
    }

    public PartialCandidat getPartialCandidat() {
        return partialCandidat;
    }

    public void setPartialCandidat(PartialCandidat partialCandidat) {
        this.partialCandidat = partialCandidat;
    }

}
