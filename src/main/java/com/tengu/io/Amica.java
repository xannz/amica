/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.tengu.io;

import backtype.storm.Config;
import backtype.storm.LocalCluster;
import backtype.storm.StormSubmitter;
import backtype.storm.topology.TopologyBuilder;

/**
 *
 * @author sander
 *
 * https://github.com/Azure-Samples/hdinsight-python-storm-wordcount/tree/master/JavaTopology
 * https://azure.microsoft.com/en-us/documentation/articles/hdinsight-storm-develop-python-topology/
 * 
 * processingbolt:
 * http://stackoverflow.com/questions/19807395/how-would-i-split-a-stream-in-apache-storm
 */
public class Amica {

    public static void main(String[] args) throws Exception {

        TopologyBuilder builder = new TopologyBuilder();
        builder.setSpout("ImageSpout", new ImageSpout(), 1);
        builder.setBolt("preprocessingBolt", new PreProcessingBolt(), 1).shuffleGrouping("ImageSpout");
        builder.setBolt("downloadBolt", new DownloadImageBolt(), 1).shuffleGrouping( "preprocessingBolt", "imageStream");
        builder.setBolt("nudityBolt", new NudityBolt(), 1).allGrouping("downloadBolt");
        //builder.setBolt("mutilationBolt", new MutilationBolt(), 1).allGrouping("downloadBolt");

        Config conf = new Config();
        conf.put("path", "/tmp/images/");
        conf.put("exePathNudity", "/home/sander/amica/misc/Components/Visics/Nudity/ImageInterpretation/src/ImageInterpretation");
        conf.put("exePathMutilation", "/home/sander/amica/misc/Components/Visics/Mutilation/ImageInterpretation2/src/ImageInterpretation2");
        conf.setDebug(true);

        if (args != null && args.length > 0) {
            //parallelism hint to set the number of workers
            conf.setNumWorkers(5);
            //submit the topology
            StormSubmitter.submitTopology(args[0], conf, builder.createTopology());
        } //Otherwise, we are running locally
        else {
            //Cap the maximum number of executors that can be spawned
            //for a component to 3
            conf.setMaxTaskParallelism(1);
            //LocalCluster is used to run locally
            LocalCluster cluster = new LocalCluster();
            //submit the topology
            cluster.submitTopology("Amica", conf, builder.createTopology());
            //sleep
            Thread.sleep(20000);
            //shut down the cluster
            cluster.shutdown();
        }
    }
}
