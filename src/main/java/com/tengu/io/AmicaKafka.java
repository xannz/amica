/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.tengu.io;

import backtype.storm.Config;
import backtype.storm.StormSubmitter;
import backtype.storm.spout.SchemeAsMultiScheme;
import backtype.storm.topology.TopologyBuilder;
import java.util.UUID;
import storm.kafka.BrokerHosts;
import storm.kafka.KafkaSpout;
import storm.kafka.SpoutConfig;
import storm.kafka.ZkHosts;

/**
 *
 * @author sander
 */
public class AmicaKafka {

    public static void main(String[] args) throws Exception {
        String zookeeper_connect = args[0];
        String mongo_connect = args[1];
        String mongo_ip = mongo_connect.split(":")[0];
        String mongo_port = mongo_connect.split(":")[1];

        String kafka_topic = "amica";
        String consumer_home = "/kafkaStorm";

        System.out.format("Zookeeper_connect: %s", zookeeper_connect);
        BrokerHosts hosts = new ZkHosts(zookeeper_connect);
        SpoutConfig spoutConf = new SpoutConfig(hosts, kafka_topic, consumer_home, UUID.randomUUID().toString());
        spoutConf.scheme = new SchemeAsMultiScheme(new KafkaBoltKeyValueScheme());
        /**
         * Comment to allow kafka real-time use
         */
        spoutConf.forceFromStart = true;
        spoutConf.startOffsetTime = kafka.api.OffsetRequest.EarliestTime();
        KafkaSpout kafkaSpout = new KafkaSpout(spoutConf);

        TopologyBuilder builder = new TopologyBuilder();
        builder.setSpout("kafkaSpout", kafkaSpout, 1);
        builder.setBolt("preprocessingBolt", new PreProcessingBolt(), 1)
                .shuffleGrouping("kafkaSpout");
        builder.setBolt("downloadBolt", new DownloadImageBolt(), 1)
                .shuffleGrouping("preprocessingBolt", "imageStream");
        builder.setBolt("nudityBolt", new NudityBolt(), 1)
                .allGrouping("downloadBolt");
        builder.setBolt("mutilationBolt", new MutilationBolt(), 1)
                .allGrouping("downloadBolt");
        //builder.setBolt("LT3Bolt", new LT3Bolt(), 3)
        //        .shuffleGrouping("preprocessingBolt", "textStream");
        //builder.setBolt("ClipsBolt", new ClipsBolt(), 1)
        //        .shuffleGrouping("preprocessingBolt", "textStream");
        builder.setBolt("mongoBolt", new MongoBolt(), 1)
                .shuffleGrouping("nudityBolt")
                .shuffleGrouping("mutilationBolt");
                //.shuffleGrouping("LT3Bolt")
                //.shuffleGrouping("ClipsBolt");

        Config conf = new Config();
        conf.setDebug(true);
        conf.setMaxSpoutPending(100);
        conf.put("mongo.ip", mongo_ip);
        conf.put("mongo.port", mongo_port);
        conf.put("exePathMutilation", "/tmp/Amica/Mutilation/ImageInterpretation2/src/ImageInterpretation2");
        conf.put("exePathNudity", "/tmp/Amica/Nudity/ImageInterpretation/src/ImageInterpretation");
        conf.setNumWorkers(10);

        StormSubmitter.submitTopology("Amica", conf, builder.createTopology());
    }
}
