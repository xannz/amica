/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.tengu.io;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import java.util.Date;

/**
 *
 * @author sander
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class Data {
    private String id;
    private String flag;
    private Long timestamp;
    private String source;
    private String info;
    private String age;
    private String gnd;
    private double age_acc;
    private double gnd_acc;
    
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
    
    public Data(String id, String flag, Long timestamp, String source, String info,
            String gnd, double gnd_acc, String age, double age_acc){
        this.id = id;
        this.flag = flag;
        this.timestamp = timestamp;
        this.source = source;
        this.info = info;
        this.gnd = gnd;
        this.gnd_acc = gnd_acc;
        this.age = age;
        this.age_acc = age_acc;
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

    /**
     * @return the age
     */
    public String getAge() {
        return age;
    }

    /**
     * @param age the age to set
     */
    public void setAge(String age) {
        this.age = age;
    }

    /**
     * @return the gnd
     */
    public String getGnd() {
        return gnd;
    }

    /**
     * @param gnd the gnd to set
     */
    public void setGnd(String gnd) {
        this.gnd = gnd;
    }

    /**
     * @return the age_acc
     */
    public double getAge_acc() {
        return age_acc;
    }

    /**
     * @param age_acc the age_acc to set
     */
    public void setAge_acc(double age_acc) {
        this.age_acc = age_acc;
    }

    /**
     * @return the gnd_acc
     */
    public double getGnd_acc() {
        return gnd_acc;
    }

    /**
     * @param gnd_acc the gnd_acc to set
     */
    public void setGnd_acc(double gnd_acc) {
        this.gnd_acc = gnd_acc;
    }
}
