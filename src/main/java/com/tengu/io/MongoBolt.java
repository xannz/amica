/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.tengu.io;

import backtype.storm.task.TopologyContext;
import backtype.storm.topology.BasicOutputCollector;
import backtype.storm.topology.OutputFieldsDeclarer;
import backtype.storm.topology.base.BaseBasicBolt;
import backtype.storm.tuple.Fields;
import backtype.storm.tuple.Tuple;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.mongodb.MongoClient;
import com.mongodb.client.MongoDatabase;
import java.io.IOException;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.bson.Document;

/**
 *
 * @author sander
 */
public class MongoBolt extends BaseBasicBolt{
    
    private MongoDatabase db;
    
    @Override
    public void prepare(Map stormConf, TopologyContext context){
        String mongo_ip = stormConf.get("mongo.ip").toString();
        int mongo_port = Integer.parseInt(stormConf.get("mongo.port").toString());
        System.out.format("Mongo connect: %s:%d", mongo_ip, mongo_port);
        MongoClient mongoClient = new MongoClient(mongo_ip, mongo_port);
        db = mongoClient.getDatabase("amica-results");
        System.out.println("Database found");
    }

    public void declareOutputFields(OutputFieldsDeclarer ofd) {
        ofd.declare(new Fields(""));
    }

    public void execute(Tuple tuple, BasicOutputCollector boc) {            
        try { 
            String json = tuple.getStringByField("result");            
            ObjectMapper mapper = new ObjectMapper();             
            Data d = mapper.readValue(json, Data.class);            
            String final_json = mapper.writeValueAsString(d);            
            String collection = "data";
            db.getCollection(collection).insertOne(Document.parse(final_json));
        } catch (IOException ex) {
            Logger.getLogger(MongoBolt.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
}
