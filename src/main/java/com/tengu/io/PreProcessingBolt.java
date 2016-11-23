/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.tengu.io;

import backtype.storm.topology.BasicOutputCollector;
import backtype.storm.topology.OutputFieldsDeclarer;
import backtype.storm.topology.base.BaseBasicBolt;
import backtype.storm.tuple.Fields;
import backtype.storm.tuple.Tuple;
import backtype.storm.tuple.Values;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author sander
 */
public class PreProcessingBolt extends BaseBasicBolt{

    public void declareOutputFields(OutputFieldsDeclarer ofd) {
        ofd.declareStream("imageStream", new Fields("id", "imageURL", "imageExtension"));
        ofd.declareStream("textStream", new Fields("id", "text"));
    }

    public void execute(Tuple tuple, BasicOutputCollector boc) {
        //System.out.println("TUPLE in PreProcessingBolt");
        
        try {
            String json_tweet = tuple.getStringByField("message");            
            Tweet t = new Tweet();
            ObjectMapper mapper = new ObjectMapper();            
            t = mapper.readValue(json_tweet, Tweet.class);
            
            /**
            * Test if tweet has an image to analyze
            */
            if(t.mediaCount() > 0){
                //Loop and check to see if really img
                //if yes, emit url and id       
                //System.out.println("MEDIA FOUND");         
                for(String s : t.getMedia()){
                    //if(s.matches(".*jpg$") || s.matches(".*png$") || s.matches(".*bmp$") || s.matches(".*jpeg$")){
                    if(s.matches(".*jpg$") || s.matches(".*JPG$")){
                        //System.out.println("Sending tuple to downloadbolt");
                        //System.out.println("jpg FOUND");
                        boc.emit("imageStream", new Values(Long.toString(t.getId()), s, "jpg"));
                    }else if(s.matches(".*png") || s.matches(".*PNG")){
                        //System.out.println("png FOUND");
                        boc.emit("imageStream", new Values(Long.toString(t.getId()), s, "png"));
                    }else if(s.matches(".*bmp$") || s.matches(".*BMP$")){
                        //System.out.println("bmp FOUND");
                        boc.emit("imageStream", new Values(Long.toString(t.getId()), s, "bmp"));
                    }else if(s.matches(".*jpeg$") || s.matches(".*JPEG$")){
                        //System.out.println("jpeg FOUND");
                        boc.emit("imageStream", new Values(Long.toString(t.getId()), s, "jpeg"));
                    }
                }                
            }
            /**
             * Test if tweet has text to analyze
             */
            if(! t.getText().equals("")){
                //if yes, emit text and id
                //System.out.println("text FOUND");
                boc.emit("textStream", new Values( Long.toString(t.getId()),t.getText()));
            }
        } catch (IOException ex) {
            Logger.getLogger(PreProcessingBolt.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
}
