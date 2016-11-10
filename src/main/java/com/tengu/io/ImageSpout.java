/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.tengu.io;

import backtype.storm.spout.SpoutOutputCollector;
import backtype.storm.task.TopologyContext;
import backtype.storm.topology.OutputFieldsDeclarer;
import backtype.storm.topology.base.BaseRichSpout;
import backtype.storm.tuple.Fields;
import backtype.storm.tuple.Values;
import backtype.storm.utils.Utils;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author sander
 *
 * Sends Images to AMICA-Vision
 */
public class ImageSpout extends BaseRichSpout {

    SpoutOutputCollector _collector;
    String imagePath;
    Integer counter;

    public void open(Map map, TopologyContext tc, SpoutOutputCollector soc) {
        _collector = soc;
        imagePath = map.get("path").toString();
        counter = 1;
    }

    public void declareOutputFields(OutputFieldsDeclarer ofd) {
        //ofd.declare(new Fields("image", "id"));
        ofd.declareStream("images", new Fields("image", "id"));
    }

    public void nextTuple() {
        Utils.sleep(3000); //Add artificial delay
        try {
            System.out.println("Reading image");
            File fi = new File(imagePath + counter + ".jpg");
            
            if (fi.exists() && !fi.isDirectory()) {
                byte[] fileContent = Files.readAllBytes(fi.toPath());
                counter++;
                System.out.println("Emitting byte array(" + counter + ")");
                _collector.emit("images" ,new Values(fileContent, "0000001"));
            }

        } catch (IOException ex) {
            Logger.getLogger(ImageSpout.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

}
