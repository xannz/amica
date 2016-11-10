/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.tengu.io;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 *
 * @author sander
 */
public class Tweet {
    private String user;
    private String text;
    private long id;
    private String language;
    private List<String> media;
    private Date createdAt;
    
    public Tweet(){
        media = new ArrayList<String>();
    }
    
    public void AddMediaURL(String url){
        this.getMedia().add(url);
    }

    /**
     * @return the user
     */
    public String getUser() {
        return user;
    }

    /**
     * @param user the user to set
     */
    public void setUser(String user) {
        this.user = user;
    }

    /**
     * @return the text
     */
    public String getText() {
        return text;
    }

    /**
     * @param text the text to set
     */
    public void setText(String text) {
        this.text = text;
    }
   
    /**
     * @return the id
     */
    public long getId() {
        return id;
    }

    /**
     * @param id the id to set
     */
    public void setId(long id) {
        this.id = id;
    }
    
    /**
     * @return the language
     */
    public String getLanguage() {
        return language;
    }

    /**
     * @param language the language to set
     */
    public void setLanguage(String language) {
        this.language = language;
    }

    /**
     * @return the media
     */
    public List<String> getMedia() {
        return media;
    }

    /**
     * @param media the media to set
     */
    public void setMedia(List<String> media) {
        this.media = media;
    }

    /**
     * @return the createdAt
     */
    public Date getCreatedAt() {
        return createdAt;
    }

    /**
     * @param createdAt the createdAt to set
     */
    public void setCreatedAt(Date createdAt) {
        this.createdAt = createdAt;
    }
    
    public int mediaCount(){
        return media.size();
    }
}
