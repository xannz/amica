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
    private String source;
    private String info;
    
    public Data(){
        timestamp = new Date().getTime();
    }
    
    public Data(String id, String flag){
        this.id = id;
        this.flag = flag;
        this.timestamp = new Date().getTime();
    }
    
    public Data(String id, String flag, Long timestamp, String source, String info){
        this.id = id;
        this.flag = flag;
        this.timestamp = timestamp;
        this.source = source;
        this.info = info;
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

    /**
     * @return the source
     */
    public String getSource() {
        return source;
    }

    /**
     * @param source the source to set
     */
    public void setSource(String source) {
        this.source = source;
    }

    /**
     * @return the info
     */
    public String getInfo() {
        return info;
    }

    /**
     * @param info the info to set
     */
    public void setInfo(String info) {
        this.info = info;
    }
}
