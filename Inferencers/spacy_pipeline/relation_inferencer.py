from unicodedata import name
from requests.api import request
from pandas.io.json import json_normalize
from fastapi import FastAPI, Query, Request, Response
from typing import List
from relationship_extraction import *
from fastapi.middleware.cors import CORSMiddleware
import pandas as pd
from stix2 import *
import requests, json, os
from elasticsearch import Elasticsearch
# pip install elasticsearch==5.5.3
# pip install nltk
# pip install pycountry

import re
import nltk
import pycountry

countries = [country.name for country in pycountry.countries]
countries = [x.lower() for x in countries]
countries.append('russia') # Russia is properly known as russian frederation.


desired_width = 320

pd.set_option('display.width', desired_width)
pd.set_option('display.max_columns', 20)


# Init app 
app = FastAPI()

# Allow CORS headers
origins = ["*"]
app.add_middleware(
    CORSMiddleware,
    allow_origins=origins,
    allow_credentials=True,
    allow_methods=["*"],
    allow_headers=["*"],
)

def exists(entities_list, entity_name):
  for e in entities_list:
    if (e["name"]==entity_name):
      print("EXISTS="+e["name"])
      return True
  return False


def makeStixBundle(prediction):

  entities = prediction["entity tags"]
  relations = prediction["relationships"]
  
  entities_list = []


  for e in entities[0]:
    if exists(entities_list, e[1])==False:
      if(e[2] == "TA"):
        entities_list.append(ThreatActor(name=e[1]))
      elif(e[2] == "M"):
        entities_list.append(Malware(name=e[1], is_family = False))
      elif(e[2] == "IND"):
        entities_list.append(Indicator(name=e[1],pattern="[user-account:value = 'none']", pattern_type="stix"))
      elif(e[2] == "ID"):
        entities_list.append(Identity(name=e[1]))
      elif(e[2] == "T"):
        entities_list.append(Tool(name=e[1]))
      elif(e[2] == "V"):
        entities_list.append(Vulnerability(name=e[1]))
      elif(e[2] == "L"):
        entities_list.append(Location(name=e[1], longitude=0.0, latitude=0.0,region="", country=""))
      elif(e[2]=="INF"):
        entities_list.append(Infrastructure(name=e[1]))
      elif(e[2]=="C"):
        entities_list.append(Campaign(name=e[1]))
      elif(e[2]=="AP"):
        entities_list.append(AttackPattern(name=e[1]))

  a_list = []
  global a
  a= None 
  global b
  b= None 
  for e in entities_list:
    a_list.append(e)

  print(a_list)

  for val in relations:
    for ent in entities_list:
        # Find the entity match
        if (val["entities"][0] == ent["name"]):
          a = ent
        if (val["entities"][1] == ent["name"]):
          b = ent
          
        # Found both entities
        if a and b:
          print(val["predicted_relations"]+" "+ a["name"]+" "+ b["name"])
          relationship = Relationship(relationship_type=val["predicted_relations"],
          source_ref=a,
          target_ref=b)
          
          a_list.append(relationship)
          a= None
          b=None
          
  bundle = Bundle(a_list)

  return bundle.serialize(pretty=True)

  

def convertLocation(word):
  # list of regex things to check
  patterns = ['ese', 'ian', 'an', 'ean', 'n', 'ic', 'ern', 'i']
	#list of suffixes for appending to country names that get damaged when they are split.
  suffixes = ['a', 'o']

	# for every potential way of forming a nationality adjective, test them.
  for pattern in patterns:
    tup = re.findall(r'^(.*)(' + pattern + ')', word)
    if tup:
      country = tup[0][0]

      # check to see if the country is in the list of countries returned by pycountry. If it is, return it.
      
      if country in countries:
        return country

      # if the stem is not a country, try adding suffixes to it to see if you can pull out a real country.
      else:
        for suffix in suffixes:
          new_country = country + suffix
          if new_country in countries:
            return new_country


def fixLocationAdjectives(prediction):
  entities = prediction["entity tags"]
  relations = prediction["relationships"]
  locs = []
  loc_found = 0
  for i,e in enumerate(entities[0]):
    if e[2]=="L":
      if e[1] not in countries:
        loc_found = 1
        locs.append(e[1])
        new_country = convertLocation(e[1])
        if new_country!=e[1] and new_country:
          add_list = [e[0],new_country,e[2]]
          prediction["entity tags"][0][i] = add_list
  
  # Fix relationships
  if loc_found:
    for i, val in enumerate(relations):
      if val["entities"][0] in locs:
        new_country = convertLocation(val["entities"][0])
        if new_country!=val["entities"][0] and new_country:     
          add_list = [new_country,val["entities"][1]]
          prediction["relationships"][i]["entities"] = add_list
      
      if val["entities"][1] in locs:
        new_country = convertLocation(val["entities"][1])
        if new_country!=val["entities"][1] and new_country:     
          add_list = [val["entities"][0], new_country]
          prediction["relationships"][i]["entities"] = add_list

  return prediction

	
# bertModel = BertTokenClassification()
relationExtracter=RelationExtracter()
@app.post("/getInference")
async def get_model_inference(req: Request):
    #return [["ok"]["ok"]]
    
    requestData = await req.json()
    
    # sentence = requestData['sentence'].encode().decode('utf-8', 'replace')
    text=[]
    sentence = str(requestData['sentence']).replace('\n','')
    sentence = sentence.replace(',','')
    # sentence = sentence.encode('utf-8').strip()
    
    if(len(sentence.split()) > 510):
      return [[],[]]
    else:
      x= sentence.lower()
      
      x =  re.sub(r'[^a-zA-Z0-9_ !@#$%^&*()-<>:;.~=]+', '', str(x))
      
      text.append(x)

    
    prediction = relationExtracter.getInference(text)
    
    # Fix location adjectives to nouns
    prediction = fixLocationAdjectives(prediction)

    return prediction
    
@app.post("/getBundle")
async def get_stix_bundle(req: Request):
  prediction = await req.json()
  return makeStixBundle(prediction)


def makeStixBundle2(finalBundle):
  entities = finalBundle["entities"]
  relations = finalBundle["relationships"]
  
  entities_list = []


  for e in entities:
    if(e["type"] == "threat-actor"):
      entities_list.append(ThreatActor(name=e["name"]))
    elif(e["type"] == "malware"):
      entities_list.append(Malware(name=e["name"], is_family = False))
    elif(e["type"] == "indicator"):
      entities_list.append(Indicator(name=e["name"],pattern="[user-account:value = 'none']", pattern_type="stix"))
    elif(e["type"] == "identity"):
      entities_list.append(Identity(name=e["name"]))
    elif(e["type"] == "tool"):
      entities_list.append(Tool(name=e["name"]))
    elif(e["type"] == "vulnerability"):
      entities_list.append(Vulnerability(name=e["name"]))
    elif(e["type"] == "location"):
      entities_list.append(Location(name=e["name"], longitude=0.0, latitude=0.0,region="", country=""))
    elif(e["type"]=="infrastructure"):
      entities_list.append(Infrastructure(name=e["name"]))
    elif(e["type"]=="campaign"):
      entities_list.append(Campaign(name=e["name"]))
    elif(e["type"]=="attack-pattern"):
      entities_list.append(AttackPattern(name=e[1]))

  a_list = []
  global a
  a= None 
  global b
  b= None 

  #  ADDRESS DUPLICATION!!
  for e in entities_list:
    a_list.append(e)


  for val in relations:
    for ent in entities_list:
        # Find the entity match
        if (val["source"] == ent["name"]):
          a = ent
        if (val["target"] == ent["name"]):
          b = ent

        # Found both entities
        if a and b:
          relationship = Relationship(relationship_type=val["name"],
          source_ref=a,
          target_ref=b)
          a_list.append(relationship)
          a= None
          b=None
          
  bundle = Bundle(a_list)
 

  return bundle.serialize(pretty=True)
  
@app.post("/makeStix")
async def make_stix_bundle(req: Request):
	finalBundle = await req.json()
	return makeStixBundle2(finalBundle)
