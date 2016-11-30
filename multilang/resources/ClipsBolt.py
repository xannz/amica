import storm
import sys
import subprocess
import json as simplejson


class ClipsBolt(storm.BasicBolt):

    def __init__(self):
        self.p = subprocess.Popen(["python3", "/tmp/Amica/Clips/profl.py"], stdout=subprocess.PIPE, stdin=subprocess.PIPE)

    def initialize(self, conf, context):
        self._conf = conf
        self._context = context
        storm.logInfo("ClipsBolt instance starting ...")

    def process(self, tuple):
        storm.logInfo("CLIPS INCOMING TUPLE")
        id_tweet, text = tuple.values
        
        self.p.stdin.write(text.encode('ascii', 'ignore') + '\n')
        result = ""
        while True:
            result = self.p.stdout.readline()
            if not result:
                storm.logInfo("EOF EXCEPTION")
                #break ?
            if result[0:-1].startswith("{"):
            	storm.logInfo("GEVONDEN")
                break


        self.p.stdout.flush()

        if result.startswith("{"):
            result = result.replace("'", '"')
            result = result.replace("(", '[')
            result = result.replace(")", ']')
            data = simplejson.loads(result)
            data['id'] = id_tweet
            gender = data['gnd'][0]
            gender_acc = data['gnd'][1]
            age = data['age'][0]
            age_acc = data['age'][1]
            data['gnd'] = gender
            data['gnd_acc'] = gender_acc
            data['age'] = age
            data['age_acc'] = age_acc
            data['info'] = text
            data['source'] = "Clips"
            data['flag'] = "none"

            json_string = simplejson.dumps(data)
            storm.emit([json_string])       	


ClipsBolt().run()