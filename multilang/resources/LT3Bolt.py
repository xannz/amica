import storm
import sys
import json as simplejson
import time
sys.path.append('/tmp/Amica/LT3/amica_demo/suicide')
from pipeline import suicidality_pipeline as sp

#Decorator to catch printed output from sp.get_result(text)
#get_result() should always print the json message before return statement
def capture(f):
    def captured(*args, **kwargs):
        import sys
        from cStringIO import StringIO

        backup = sys.stdout       

        try:
            sys.stdout = StringIO()  
            f(*args, **kwargs)
            out = sys.stdout.getvalue()
        finally:
            sys.stdout.close()  
            sys.stdout = backup   

        return out 

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
        id_tweet, text = tuple.values
        storm.logInfo("LT3BOLTINFO")
        storm.logInfo(text)
        
        json = get_res(text.encode('utf-8'))
        
        ''.join(json)
        json = json.split('\n')[-2]        
        json_string = json.replace("'", '"')
        
        data = simplejson.loads(json_string)
        data['id'] = str(id_tweet)
        data['source'] = "LT3"
        data['info'] = text

        if(data['relevance_boolean'] == 1 and data['severity_boolean'] == 1):
            data['flag'] = "LT3"           
        else:
            data['flag'] = "none"
            
        del data['relevance_boolean'] 
        del data['severity_boolean']

        json_string = simplejson.dumps(data)        

        storm.emit([json_string])   


LT3Bolt().run()