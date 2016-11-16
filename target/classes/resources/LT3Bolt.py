import storm
import sys
import json as simplejson
sys.path.append('/tmp/Amica/LT3/amica_demo/suicide')
from pipeline import suicidality_pipeline as sp

class LT3Bolt(storm.BasicBolt):

	def initialize(self, conf, context):
		self._conf = conf;
		self._context = context;
		storm.logInfo("LT3Bolt instance starting ...")

	def process(self, tuple):
		id_tweet, text = tuple.values
		json = sp.get_result(text);

		#change id to tweet id
		json_string = str(json_string).replace("'", '"')
		data = simplejson.loads(json_string)
		data['id'] = str(id_tweet)


		if(data['relevance_boolean'] == 1 and data['severity_boolean'] == 1):
			data['flag'] = "LT3"
			print "if"
		else:
			data['flag'] = "none"
			print "else"

		del data['relevance_boolean'] 
		del data['severity_boolean']

		json_string = simplejson.dumps(data)

		storm.emit([json])



LT3Bolt().run()