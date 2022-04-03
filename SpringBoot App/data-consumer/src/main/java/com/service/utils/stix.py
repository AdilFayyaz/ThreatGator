from unicodedata import name
from requests.api import request
from pandas.io.json import json_normalize
from fastapi import FastAPI, Query, Request, Response
from typing import List
from fastapi.middleware.cors import CORSMiddleware
import pandas as pd
from stix2 import *
import requests, json, os
# from elasticsearch import Elasticsearch
# pip install elasticsearch==5.5.3

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

def makeStixBundle(finalBundle):
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

  a_list = []
  global a
  a= None 
  global b
  b= None 
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
          if a not in a_list:
            a_list.append(a)
          if b not in a_list:
            a_list.append(b)
          a_list.append(relationship)
          a= None
          b=None
          
  bundle = Bundle(a_list)
 

  return bundle.serialize(pretty=True)
  
@app.post("/makeStix")
async def make_stix_bundle(req: Request):
	finalBundle = await req.json()
	return makeStixBundle(finalBundle)
