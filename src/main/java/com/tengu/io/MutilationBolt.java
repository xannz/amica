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
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.util.ArrayList;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.imageio.ImageIO;

/**
 *
 * @author sander
 */
public class MutilationBolt extends BaseBasicBolt {

    String exePath;
    Process process;
    BufferedReader br;
    BufferedWriter bw;

    @Override
    public void prepare(Map stormConf, TopologyContext context) {
        try {
            exePath = stormConf.get("exePathMutilation").toString();
            process = new ProcessBuilder(exePath,
                    "/tmp/Amica/Mutilation/ImageInterpretation2/data/GlobalParams_000-201_GMM000.h5",
                    "/tmp/Amica/Mutilation/ImageInterpretation2/data/GlobalParams_000-201_GMM201.h5",
                    "/tmp/Amica/Mutilation/ImageInterpretation2/data/LoadParamsLog.txt",
                    "/tmp/Amica/Mutilation/ImageInterpretation2/results").start();
            br = new BufferedReader(new InputStreamReader(process.getInputStream()));
            bw = new BufferedWriter(new OutputStreamWriter(process.getOutputStream()));
        } catch (IOException ex) {
            Logger.getLogger(MutilationBolt.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    /**
     * Only called when topology is deployed on local cluster
     */
    @Override
    public void cleanup() {
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
        System.out.println("Incoming tuple (MutilationBolt)");
        try {
            BufferedImage img = ImageIO.read(new ByteArrayInputStream(tuple.getBinaryByField("image")));
            String id = tuple.getStringByField("id");
            String extension = tuple.getStringByField("imageExtension");

            String writeFilePath = "/tmp/Amica/Mutilation/ImageInterpretation2/results/tempMutilation."+ extension;
            File writeFile = new File(writeFilePath);
            System.out.println(tuple.getStringByField("url"));
            //ImageIO.write(img, extension, writeFile);
            ImageIO.write(img, extension, writeFile);
            bw.write(writeFilePath + '\n');
            bw.flush();
            
            String response = br.readLine();
            System.out.println("RESPONSE: "  + response);

            writeFile.delete();
            

            /**
             * Class 201 == mutilation Class 0 == no flag
             */
            String json;
            if (response.equals("201")) {
                json = "{ \"id\": \"" + id + "\", \"flag\": \"mutilation\", \"source\": \"mutilation\", \"info\": \""+ tuple.getStringByField("url") +"\" }";
            }else{
                json = "{ \"id\": \""+ id +"\", \"flag\": \"none\", \"source\": \"mutilation\", \"info\": \""+ tuple.getStringByField("url") +"\" }";
            }
            System.out.println(json);
            boc.emit(new Values(json));
        } catch (IOException ex) {
            Logger.getLogger(NudityBolt.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

}
