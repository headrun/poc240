from aylienapiclient import textapi
import json
import re
from collections import Counter
import operator
import csv
import xlwt,xlrd
from random import choice
import time

class PDFscoringSheet():

	def __init__(self):
		self.excel_file = 'pdf_scoring_sheet.xls'
		access_keys = [{'36ea06f52762762d6c73086cc121f0ee':'433156c5'},{'46d2cc32900194fcd285818613d8dd94':'74b57e0f'}]
                keys = choice(access_keys)
		self.key = keys.keys()[0]
		self.app_id = keys.values()[0]	
		
	def unicode_csv_reader(self,utf8_data, dialect=csv.excel,**kwargs):
            csv_reader = csv.reader(utf8_data, dialect=dialect, **kwargs)
            for row in csv_reader:
                yield [unicode(cell, 'utf-8') for cell in row]

	def headers(self,file_name):
		headers = ['Link','Entities-Mentioned','Theme','Prominence','Sentiment 1','Sentiment 2','PR-Influence','Proactive_v/s _Reactive','Press-Release','Employee-speak','Article-Type','Story','Summary(30-40_words)','ArticleCode']
		self.scoringsheet_excel_file = xlwt.Workbook(encoding="utf-8")
 		self.scoring_sheet_hdlr = self.scoringsheet_excel_file.add_sheet("sheet1")
 		for i, row in enumerate(headers):
 			self.scoring_sheet_hdlr.write(0, i, row)
		self.prog_row_count = 1

	def main(self):
		client = textapi.Client(self.app_id, self.key)
		self.headers(self.excel_file)		
		input_filename = 'new.csv'
                input_type = 'pdf'
		print "starting to process data"
		print client.RateLimits()
                reader = self.unicode_csv_reader(open(input_filename))
                for row in reader:
                        if row!=['Title', 'Date', 'Description', 'Filename']:
                                title, publishDate,description,filename = row
				time.sleep(30)
				print "processing %s having title %s"%(filename,title)
                                article_text = self.normalize(description)
                                title = self.normalize(title)
				input_type = self.normalize(filename.split(',')[-1])
				aux_info ={}
				keyword_sentences=[]
				rulebased_sentiment_final = {}
				summary_sentiment_final = {}

				
				print "Extracting Entities"
				#ENTITY EXTRACTION
				entities = client.Entities({"text": article_text})
				language = entities.get('language','')
				other_entities = entities.get('entities')
				if other_entities:
					product = other_entities.get('product','')
					keyword = other_entities.get('keyword','')
					person = other_entities.get('person','')
					location = other_entities.get('location','')
					email = other_entities.get('email','')
					organization = other_entities.get('organization','')			
				print "Extracted Entities"					


				keyword = keyword or tags_for_sentiment
				if keyword:
					aux_info.update({'keywords':keyword})


				print "Extracting Sentences from Summary using Keywords"
				#Extracting sentences based on Keywords
				article_sentences = article_text.split('.')
				for sentence in article_sentences:
					for kw in keyword:
						kw = self.normalize(kw)
						if kw in sentence:
							keyword_sentences.append(sentence)
				print "Extracted Sentences from Summary using Keywords"

				final_keyword_sentences = set(keyword_sentences)
				total_polarity_confidence = []
				total_subjectivity_confidence = []
				total_polarity = []
				total_subjectivity =[]
				time.sleep(30)
	
				print "Extracting RuleBasedSentiment"
				#***********RULEBASED SENTIMENT****************
				for keyword_sentence in final_keyword_sentences:
						
					rulebased_sentiment = client.Sentiment({'text':keyword_sentence})
					rulebased_sentiment_polarity =  self.normalize(rulebased_sentiment['polarity'])
					total_polarity.append(rulebased_sentiment_polarity)
					rulebased_sentiment_text = self.normalize(rulebased_sentiment['text'])
					rulebased_sentiment_polarityconfidence = self.normalize(rulebased_sentiment['polarity_confidence'])
					total_polarity_confidence.append(rulebased_sentiment_polarityconfidence)
					rulebased_sentiment_subjectivity_confidence = self.normalize(rulebased_sentiment['subjectivity_confidence'])
					total_subjectivity_confidence.append(rulebased_sentiment_subjectivity_confidence)
					rulebased_sentiment_subjectivity = self.normalize(rulebased_sentiment['subjectivity'])	
					total_subjectivity.append(rulebased_sentiment_subjectivity)

				time.sleep(30)
			
				if total_polarity:
					rulebased_sentiment_polarity =  max(Counter(total_polarity).iteritems(), key=operator.itemgetter(1))[0]
					rulebased_sentiment_final.update({'polarity':rulebased_sentiment_polarity})

				if total_polarity_confidence:
					rulebased_sentiment_polarityconfidence = sum(float(i) for i in total_polarity_confidence)/len(total_polarity_confidence)
					rulebased_sentiment_final.update({'polarity_confidence':rulebased_sentiment_polarityconfidence})

				if total_subjectivity_confidence:
					rulebased_sentiment_subjectivity_confidence = sum(float(i) for i in total_subjectivity_confidence) / len(total_subjectivity_confidence)
					rulebased_sentiment_final.update({'subjectivity_confidence':rulebased_sentiment_subjectivity_confidence})
		
				if total_subjectivity:
					rulebased_sentiment_subjectivity = max(Counter(total_subjectivity).iteritems(), key=operator.itemgetter(1))[0]
					rulebased_sentiment_final.update({'sentiment_subjectivity':rulebased_sentiment_subjectivity})

				print "Extracted RuleBasedSentiment"

				print "Extracting Summary and Sentiment"


				time.sleep(30)
				#****************SUMMARY******************

				summary = client.Summarize({'title':title,'text':article_text,'sentences_number': 3})
				summary_text = self.normalize(summary['text'])
				summary_sentences = self.normalize(self.textify(summary['sentences']))
				
				time.sleep(30)

				#****************OVERALL SENTIMENT******************#
				summary_sentiment = client.Sentiment({'text':summary_sentences})
				summary_sentiment_polarity =  self.normalize(summary_sentiment['polarity'])
				summary_sentiment_text = self.normalize(summary_sentiment['text'])
				summary_sentiment_polarityconfidence = self.normalize(summary_sentiment['polarity_confidence'])
				summary_subjectivity_confidence = self.normalize(summary_sentiment['subjectivity_confidence'])
				summary_subjectivity = self.normalize(summary_sentiment['subjectivity'])


				summary_sentiment_final.update({'polarity': summary_sentiment_polarity,'polarity_confidence': summary_sentiment_polarityconfidence,'subjectivity_confidence':summary_subjectivity_confidence,'sentiment_subjectivity': summary_subjectivity})
				
				print "Extracted Summary and Sentiment"
				
				link = ''
				theme = ','.join(keyword)
				prominence = ''
				sentiment_1 = summary_sentiment_final['polarity']
				sentiment_2 = rulebased_sentiment_final['polarity']
				entities_mentioned = ''
				PR_influence = ''
				proactive_reactive = ''
				press_release=''
				employee_speak = ''
				article_type = ''
				story = article_text
				summary = summary_sentences

				print "Writing data into Excel Sheet"
                        	values = (link,entities_mentioned, theme,prominence,sentiment_1, sentiment_2,  PR_influence, proactive_reactive, press_release, employee_speak,article_type,story, summary,filename)
				value = [self.Y(self.new_str(i)) if i is not None else '' for i in values]
				for col_count,data in enumerate(value):
					self.scoring_sheet_hdlr.write(self.prog_row_count,col_count,data)
				self.prog_row_count += 1
				print "Written data into Excel sheet"

				print client.RateLimits()
				time.sleep(30)
			self.scoringsheet_excel_file.save(self.excel_file)

	def new_str(self,data):
   		try:
       			return str(data)
   		except:
       			try:
           			return ''.join([chr(ord(x)) for x in data]).decode('utf8').encode('utf8')
       			except ValueError:
           			return data.encode('utf8')

	def Y(self,data):
		return data.replace('\n', '').replace('\t', '').replace('\r', '').replace('&amp;', '&').replace('&quot;', '"')

	def compact(self,text, level=0):
    		if text is None: return ''
		if level == 0:
			if type(text) is not float: 
				text = text.replace("\n", ".")
				text = text.replace("\r", ".")
				text = text.replace("\t"," ")
		text = str(text).replace('..','. ')
		compacted = re.sub("\s\s(?m)", " ", text)
		if compacted != text:
			compacted = self.compact(compacted, level+1)
		return compacted.strip()

	def clean(self,text):
		if not text: return text
		value = str(text)
		if type(value) is not float:
			value = re.sub("&amp;", "&", value)
			value = re.sub("&lt;", "<", value)
			value = re.sub("&gt;", ">", value)
			value = re.sub("&quot;", '"', value)
			value = re.sub("&apos;", "'", value)
		return value

	def normalize(self,text):
	    	return self.clean(self.compact(self.xcode(text)))

	def textify(self, nodes, sep=' '):
	    	if isinstance(nodes, (list, tuple)):
			nodes = sep.join(nodes)
		return nodes

	def concatenating_with_tags(self,nodes):
		if isinstance(nodes, (list, tuple)):
                        nodes = '<>'.join(nodes)
		return nodes

	def xcode(self,text, encoding='utf8', mode='strict'):
	    	return text.encode(encoding, mode) if isinstance(text, unicode) else text
			
PDFscoringSheet().main()
