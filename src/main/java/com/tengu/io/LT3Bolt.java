/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.tengu.io;

import backtype.storm.task.ShellBolt;
import backtype.storm.topology.IRichBolt;
import backtype.storm.topology.OutputFieldsDeclarer;
import backtype.storm.tuple.Fields;
import java.util.Map;

/**
 *
 * @author sander
 */
public class LT3Bolt extends ShellBolt implements IRichBolt{
    
    public LT3Bolt(){
        super("python", "LT3Bolt.py");
    }

    public void declareOutputFields(OutputFieldsDeclarer ofd) {
        ofd.declare(new Fields("result"));
    }

    public Map<String, Object> getComponentConfiguration() {
        return null;
    }
    
}
