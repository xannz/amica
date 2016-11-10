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
        ofd.declareStream("imageStream", new Fields("id", "imageURL"));
        ofd.declareStream("textStream", new Fields("id", "text"));
    }

    public void execute(Tuple tuple, BasicOutputCollector boc) {
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
                
                
            }
            /**
             * Test if tweet has text to analyze
             */
            if(! t.getText().equals("")){
                //if yes, emit text and id
                boc.emit(new Values( Long.toString(t.getId()),t.getText()));
            }
        } catch (IOException ex) {
            Logger.getLogger(PreProcessingBolt.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
}
