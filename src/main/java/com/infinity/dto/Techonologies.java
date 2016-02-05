/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.infinity.dto;

import java.io.Serializable;

/**
 *
 * @author Utilisateur
 */
public class Techonologies implements Serializable {

    private String technoName;
    private int expDurationStart;
    private int expDurationEnd;

    public Techonologies() {

    }

    public String getTechnoName() {
        return technoName;
    }

    public void setTechnoName(String technoName) {
        this.technoName = technoName;
    }

    public int getExpDurationStart() {
        return expDurationStart;
    }

    public void setExpDurationStart(int expDurationStart) {
        this.expDurationStart = expDurationStart;
    }

    public int getExpDurationEnd() {
        return expDurationEnd;
    }

    public void setExpDurationEnd(int expDurationEnd) {
        this.expDurationEnd = expDurationEnd;
    }

}
