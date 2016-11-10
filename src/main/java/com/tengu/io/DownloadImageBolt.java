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
import java.awt.Image;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.imageio.ImageIO;

/**
 *
 * @author sander
 */
public class DownloadImageBolt extends BaseBasicBolt {

    public void declareOutputFields(OutputFieldsDeclarer ofd) {
        ofd.declare(new Fields("id", "image"));
    }

    public void execute(Tuple tuple, BasicOutputCollector boc) {
        //extension of image might need to be emitted 
        String id = tuple.getStringByField("id");
        try {
            boc.emit(new Values(id, downloadUrl(new URL(tuple.getStringByField("imageURL")))));
        } catch (MalformedURLException ex) {
            Logger.getLogger(DownloadImageBolt.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    private byte[] downloadUrl(URL toDownload) {
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();

        try {
            byte[] chunk = new byte[4096];
            int bytesRead;
            InputStream stream = toDownload.openStream();

            while ((bytesRead = stream.read(chunk)) > 0) {
                outputStream.write(chunk, 0, bytesRead);
            }
            
            stream.close();

        } catch (IOException e) {
            e.printStackTrace();
        }

        return outputStream.toByteArray();
    }

}
