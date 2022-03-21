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

def makeStixBundle(prediction):
  entities = prediction["entity tags"]
  relations = prediction["relationships"]
  
  entities_list = []


  for e in entities[0]:
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

  a_list = []
  global a
  a= None 
  global b
  b= None 
  for val in relations:
    for ent in entities_list:
        # Find the entity match
        if (val["entities"][0] == ent["name"]):
          a = ent
        if (val["entities"][1] == ent["name"]):
          b = ent
          
        # Found both entities
        if a and b:
          relationship = Relationship(relationship_type=val["predicted_relations"],
          source_ref=a,
          target_ref=b)
          if a not in a_list:
            a_list.append(a)
          if b not in a_list:
            a_list.append(b)
          a_list.append(relationship)
          a= None
          b=None
          
  bundle = Bundle(a_list)
  # Add to Elastic Search
  es = Elasticsearch("http://localhost:9200")
  # Send the data into es
  es.index(index='bundle', ignore=400, doc_type='doc', body=json.loads(bundle.serialize(pretty=True)))

  return bundle.serialize(pretty=True)
  

# bertModel = BertTokenClassification()
relationExtracter=RelationExtracter()
@app.post("/getInference")
async def get_model_inference(req: Request):
    #return [["ok"]["ok"]]
    
    requestData = await req.json()
    # sentence = requestData['sentence'].encode().decode('utf-8', 'replace')
    text=[]
    sentence = requestData['sentence'].replace('\n','')
    sentence = sentence.replace(',','')
    
    
    if(len(sentence.split()) > 510):
      return [[],[]]
    else:
      x= sentence.lower()
      text.append(x)
    
    prediction = relationExtracter.getInference(text)
    return prediction
    
@app.post("/getBundle")
async def get_stix_bundle(req: Request):
  prediction = await req.json()
  return makeStixBundle(prediction)