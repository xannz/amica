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
import backtype.storm.tuple.Values;
import java.awt.image.BufferedImage;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.util.ArrayList;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.imageio.ImageIO;

/**
 *
 * @author sander
 *
 * Takes images and runs them through a SVM, result is emitted
 */
public class NudityBolt extends BaseBasicBolt {

    String exePath;
    Process process;
    BufferedReader br;
    BufferedWriter bw;

    @Override
    public void prepare(Map stormConf, TopologyContext context) {
        try {
            exePath = stormConf.get("exePathNudity").toString();
            process = new ProcessBuilder(exePath,
                    "/tmp/images/GlobalParams.h5",
                    "/tmp/images/LoadParamsLog",
                    "/tmp/results").start();
            br = new BufferedReader(new InputStreamReader(process.getInputStream()));
            bw = new BufferedWriter(new OutputStreamWriter(process.getOutputStream()));
        } catch (IOException ex) {
            Logger.getLogger(NudityBolt.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    /**
    * Only called when topology is deployed on local cluster 
    */
    @Override
    public void cleanup(){
        try {
            br.close();
            bw.close();
            process.destroy();
        } catch (IOException ex) {
            Logger.getLogger(NudityBolt.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public void declareOutputFields(OutputFieldsDeclarer ofd) {
        ofd.declare(new Fields("result"));
    }

    public void execute(Tuple tuple, BasicOutputCollector boc) {
        System.out.println("Incoming tuple (NudityBolt)");
        try {
            BufferedImage img = ImageIO.read(new ByteArrayInputStream(tuple.getBinaryByField("image")));
            String id = tuple.getStringByField("id");

            String writeFilePath = "/tmp/results/tempNudity.jpg";
            File writeFile = new File(writeFilePath);
            ImageIO.write(img, "jpg", writeFile);
            
            bw.write(writeFilePath + '\n');
            bw.flush();            
            String response = br.readLine();            
            writeFile.delete();
            
            /**
            * Class 2 == nudity
            * Class 1 == no flag
            */
            String json;
            if(response.equals("2")){
                json = "{ \"id\": \""+ id +"\", \"flag\": \"nudity\" }";
            }else{
                json = "{ \"id\": \""+ id +"\", \"flag\": \"none\" }";
            }
            System.out.println(json);
            boc.emit(new Values(json));
        } catch (IOException ex) {
            Logger.getLogger(NudityBolt.class.getName()).log(Level.SEVERE, null, ex);
        }
    }   

}