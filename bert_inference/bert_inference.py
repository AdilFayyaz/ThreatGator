from requests.api import request
from pandas.io.json import json_normalize
from fastapi import FastAPI, Query, Request, Response
from typing import List
from bert_token_classification import *
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


@app.post("/getInference")
async def get_model_inference(req: Request):
    requestData = await req.json()
    bertModel = BertTokenClassification()
    prediction = bertModel.getInference(requestData['sentence'])
    return prediction

