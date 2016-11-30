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
import java.awt.Image;
import java.awt.image.BufferedImage;
import java.awt.image.DataBufferByte;
import java.awt.image.WritableRaster;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.imageio.ImageIO;

/**
 *
 * @author sander
 */
public class DownloadImageBolt extends BaseBasicBolt {

    ByteArrayOutputStream outputStream;

    @Override
    public void prepare(Map stormConf, TopologyContext context) {
        outputStream = new ByteArrayOutputStream();
    }

    public void declareOutputFields(OutputFieldsDeclarer ofd) {
        ofd.declare(new Fields("id", "image", "imageExtension", "url"));
    }

    public void execute(Tuple tuple, BasicOutputCollector boc) {
        //extension of image might need to be emitted 
        String id = tuple.getStringByField("id");
        String extension = tuple.getStringByField("imageExtension");
        String imageURL = tuple.getStringByField("imageURL");

        if (imageURL.equals("nudity") || imageURL.equals("mutilation")) {
            
            boc.emit(new Values(id, new byte[0], extension, imageURL));
                                
             
        } else {
            try {
                byte[] imgBytes = downloadUrl(new URL(imageURL));
                if (imgBytes.length != 0) {
                    boc.emit(new Values(id, imgBytes, extension, imageURL));
                } else {
                    System.out.println("ERROR DOWNLOADING IMAGE");
                }

            } catch (MalformedURLException ex) {
                Logger.getLogger(DownloadImageBolt.class.getName()).log(Level.SEVERE, null, ex);
            }
        }

    }

    private byte[] downloadUrl(URL toDownload) {

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
/*
    private byte[] extractBytes(String ImageName) throws IOException {
        File imgPath;
        if(ImageName.equals("nudity")){
            imgPath = new File("/tmp/Amica/Nudity/ImageInterpretation/data/1.jpg");
        }else{
            imgPath = new File("/tmp/Amica/Mutilation/ImageInterpretation2/data/1.jpg");
        }
        BufferedImage bufferedImage = ImageIO.read(imgPath);

        WritableRaster raster = bufferedImage.getRaster();
        DataBufferByte data = (DataBufferByte) raster.getDataBuffer();

        return (data.getData());
    }
*/
}
