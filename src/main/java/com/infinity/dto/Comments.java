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
public class Comments {

    
    private String id;
    private PartialCandidat partialCandidat;
    private String commentDate;
    private String comment;

    public Comments() {
    }

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

    public String getCommentDate() {
        return commentDate;
    }

    public void setCommentDate(String commentDate) {
        this.commentDate = commentDate;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }
    
    
    
    
}
