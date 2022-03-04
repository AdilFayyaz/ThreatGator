from requests.api import request
from pandas.io.json import json_normalize
from fastapi import FastAPI, Query, Request, Response
from typing import List
from roberta_tokenizer import *
from fastapi.middleware.cors import CORSMiddleware
import pandas as pd
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

# bertModel = BertTokenClassification()
robertaModel=SpacyRobertaTokenizer()
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
      text.append(sentence)
    
    prediction = robertaModel.getInference(text)
    return prediction
	  
 
    

