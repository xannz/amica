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

        self.p.stdin.write(text + '\n')
        output = self.p.stdout.readline()
        result = output.splitlines()[-1]
        storm.logInfo(result)    
        self.p.stdout.flush()

        result = result.replace("'", '"')
        data = simplejson.loads(result)
        data['id'] = id_tweet

        json_string = simplejson.dumps(data)
        storm.logInfo("CLIPS RESULT")
        storm.logInfo(json_string)
        storm.emit(json_string)


ClipsBolt().run()