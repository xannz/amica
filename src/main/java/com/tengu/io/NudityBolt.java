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
import java.io.PrintStream;
import java.util.ArrayList;
import java.util.Map;
import java.util.Scanner;
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
                     "/tmp/Amica/Nudity/ImageInterpretation/data/GlobalParams.h5",                       
                     "/tmp/Amica/Nudity/ImageInterpretation/data/LoadParamsLog",                      
                     "/tmp/Amica/Nudity/ImageInterpretation/results").start();
            br = new BufferedReader(new InputStreamReader(process.getInputStream()));
            bw = new BufferedWriter(new OutputStreamWriter(process.getOutputStream()));
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
            String writeFilePath;
            String extension = tuple.getStringByField("imageExtension");
            String id = tuple.getStringByField("id");
            if (tuple.getStringByField("url").equals("mutilation")) {
                writeFilePath = "/tmp/Amica/Mutilation/ImageInterpretation2/data/1.jpg";
            } else if (tuple.getStringByField("url").equals("nudity")) {
                writeFilePath = "/tmp/Amica/Nudity/ImageInterpretation/data/1.jpg";
            } else {
                BufferedImage img = ImageIO.read(new ByteArrayInputStream(tuple.getBinaryByField("image")));
                writeFilePath = "/tmp/Amica/Nudity/ImageInterpretation/results/tempNudity." + extension;
                File writeFile = new File(writeFilePath);
                ImageIO.write(img, extension, writeFile);
            }

            bw.write(writeFilePath + '\n');
            bw.flush();

            String response = br.readLine();
            //System.out.println("RESPONSE: " + response);
            if (!tuple.getStringByField("url").equals("mutilation") && !tuple.getStringByField("url").equals("nudity")) {
                File deleteFile = new File("/tmp/Amica/Nudity/ImageInterpretation/results/tempNudity." + extension);
                deleteFile.delete();
            }

            /**
             * Class 2 == nudity Class 1 == no flag
             */
            String json;
            if (response.equals("2")) {
                json = "{ \"id\": \"" + id + "\", \"flag\": \"nudity\", \"source\": \"nudity\", \"info\": \""+ tuple.getStringByField("url") +"\" }";
            } else {
                json = "{ \"id\": \"" + id + "\", \"flag\": \"none\", \"source\": \"nudity\", \"info\": \""+ tuple.getStringByField("url") +"\" }";
            }
            boc.emit(new Values(json));
        } catch (IOException ex) {
            Logger.getLogger(NudityBolt.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

}
