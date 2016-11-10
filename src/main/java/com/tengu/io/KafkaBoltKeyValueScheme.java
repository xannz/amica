/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.tengu.io;

import backtype.storm.tuple.Fields;
import storm.kafka.StringKeyValueScheme;

/**
 *
 * @author sander
 */
public class KafkaBoltKeyValueScheme extends StringKeyValueScheme{
    
    @Override
    public Fields getOutputFields(){
        return new Fields("message");
    }
}