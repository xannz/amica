/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.tengu.io;

import java.util.Date;

/**
 *
 * @author sander
 */
public class Data {
    private String id;
    private String flag;
    private Long timestamp;
    
    public Data(){
        timestamp = new Date().getTime();
    }
    
    public Data(String id, String flag){
        this.id = id;
        this.flag = flag;
        this.timestamp = new Date().getTime();
    }
    
    public Data(String id, String flag, Long timestamp){
        this.id = id;
        this.flag = flag;
        this.timestamp = timestamp;
    }

    /**
     * @return the id
     */
    public String getId() {
        return id;
    }

    /**
     * @param id the id to set
     */
    public void setId(String id) {
        this.id = id;
    }

    /**
     * @return the flag
     */
    public String getFlag() {
        return flag;
    }

    /**
     * @param flag the flag to set
     */
    public void setFlag(String flag) {
        this.flag = flag;
    }

    /**
     * @return the timestamp
     */
    public long getTimestamp() {
        return timestamp;
    }

    /**
     * @param timestamp the timestamp to set
     */
    public void setTimestamp(Long timestamp) {
        this.timestamp = timestamp;
    }
}
