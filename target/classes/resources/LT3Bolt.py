import storm
import sys
import json as simplejson
sys.path.append('/tmp/Amica/LT3/amica_demo/suicide')
from pipeline import suicidality_pipeline as sp

def capture(f):
    def captured(*args, **kwargs):
        import sys
        from cStringIO import StringIO

        backup = sys.stdout

        try:
            sys.stdout = StringIO()  
            f(*args, **kwargs)
            out = sys.stdout.getvalue() # release output
        finally:
            sys.stdout.close()  # close the stream 
            sys.stdout = backup # restore original stdout

        return out # captured output wrapped in a string

    return captured

@capture
def get_res(text):
    return sp.get_result(text)

class LT3Bolt(storm.BasicBolt):

	

    def initialize(self, conf, context):
        self._conf = conf;
        self._context = context;
        storm.logInfo("LT3Bolt instance starting ...")

    def process(self, tuple):
        storm.logInfo("LT3Bolt tuple incomming")
        id_tweet, text = tuple.values
        storm.logInfo(text)
        #json = sp.get_result(text)
        json = get_res(text)
        ''.join(json)
        #print '[%s]' % ', '.join(map(str, json_string))
        json = json.split('\n')[-2]        
        json_string = json.replace("'", '"')
        storm.logInfo("JSON")
        storm.logInfo(json_string)
        
        data = simplejson.loads(json_string)
        #storm.logInfo(data)
        storm.logInfo("JSON OBJECT SUCCES")
        data['id'] = str(id_tweet)
        #storm.logInfo(data)
        data['source'] = "LT3"
        #storm.logInfo(data)

        if(data['relevance_boolean'] == 1 and data['severity_boolean'] == 1):
            data['flag'] = "LT3"
            storm.logInfo("JSON 1 and 1")            
        else:
            data['flag'] = "none"
            storm.logInfo("JSON OBJECT none")
            
        del data['relevance_boolean'] 
        del data['severity_boolean']
        storm.logInfo("JSON OBJECT DEL")

        json_string = simplejson.dumps(data)
        
        storm.logInfo("LT3Bolt emitting tuple")
        storm.emit([json_string])


         


LT3Bolt().run()