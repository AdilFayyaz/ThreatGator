# pip install beautifulsoup4
# pip install selenium
# sudo apt install uvicorn
# pip install webdriver-manager
# pip install OTXv2
# pip install pydantic
# pip install "fastapi[all]" --user
# or
# pip install "uvicorn[standard]" --user
# uvicorn main:app --reload
# on windows => python -m uvicorn main:app --reload

# pip install kafka-python
# pip install pandas
####################################################################
import json
from json import dumps

from requests.api import request
from reddit_crawler import *
from discord_crawler import *
from twitter_crawler import *
from alienVault import *
from pandas.io.json import json_normalize
from fastapi import FastAPI, Query, Request, Response
from typing import List
from fastapi.middleware.cors import CORSMiddleware
import pandas as pd
from kafka import KafkaProducer

desired_width = 320

pd.set_option('display.width', desired_width)
pd.set_option('display.max_columns', 20)

# Init app 
app = FastAPI()

# create kafka producer
producer = KafkaProducer(bootstrap_servers=['localhost:9092'],
                        value_serializer=lambda v: json.dumps(v).encode('utf-8'))

# Allow CORS headers
origins = ["*"]
app.add_middleware(
    CORSMiddleware,
    allow_origins=origins,
    allow_credentials=True,
    allow_methods=["*"],
    allow_headers=["*"],
)


@app.get("/")
async def root():
    return {"message": "Threat Gator"}


@app.post("/source/twitter")
async def get_tweets(req: Request):
    requestData = await req.json()
    tCrawler = TwitterCrawler()
    tweets = tCrawler.init(requestData["handle"], requestData["Date"], requestData["isHandle"])
    tweets["tweets"] = tweets[requestData["handle"]]
    del tweets[requestData["handle"]]
    # tweets['source'] = "twitter"

    json_tweets = json.dumps(tweets)
    producer.send('tweets', value=json_tweets)
    return tweets


@app.post("/source/reddit/fetch_mp_mr")
async def get_reddit_mp_mr(req: Request):
    requestData = await req.json()
    rCrawler = RedditCrawler()
    rCrawler.authenticate()
    if requestData["type"] == "mp":
        _, data = rCrawler.fetch_most_popular_data(requestData["subreddit"], rCrawler.headers, {'limit': 50}, 1)
    elif requestData["type"] == "mr":
        _, data = rCrawler.fetch_most_recent_data(requestData["subreddit"], rCrawler.headers, {'limit': 50}, 1)
    d2 = data.to_json(orient="records");
    producer.send('reddit-threads', value=d2)  # sending the fetched threads to the topic called reddit-threads
    returnDic = {}
    returnDic["title"] = data["title"]
    returnDic["selftext"] = data["selftext"]

    # returnDic["source"] = "reddit"
    return returnDic


@app.post("/source/reddit/fetch_user")
async def get_reddit_user(req: Request):
    requestData = await req.json()
    rCrawler = RedditCrawler()
    rCrawler.authenticate()

    if requestData["type"] == 1:
        _, data = rCrawler.fetch_comments_by_user(requestData["username"], rCrawler.headers, {'limit': 50}, 1)
    d2 = data.to_json(orient="records");
    producer.send('reddit-comments', value=d2)
    returnDic = {}
    returnDic['body'] = data['body']
    # data["source"] = "reddit_user"
    return returnDic


@app.post("/source/reddit/fetch_user_submit")
async def get_reddit_user_submit(req: Request):
    requestData = await req.json()
    rCrawler = RedditCrawler()
    rCrawler.authenticate()
    # if requestData["type"] == 2:
    # if(requestData["username"]=="cybersecurity"):
    #     import webbrowser
    #     webbrowser.open('https://stackoverflow.com/questions/4302027/how-to-open-a-url-in-python')
    _, data = rCrawler.fetch_submitted_by_user(requestData["username"], rCrawler.headers, {'limit': 50}, 1)
    d2 = data.to_json(orient="records");
    producer.send('reddit-threads', value=d2)  # sending the fetched threads to the topic called reddit-threads
    returnDic = {}
    returnDic["title"] = data["title"]
    returnDic["selftext"] = data["selftext"]
    return returnDic


@app.post('/source/reddit/mostRecentData')
async def recent_reddit_data(req: Request):
    requestData = await req.json()
    rCrawler = RedditCrawler()
    rCrawler.authenticate()
    _, data = rCrawler.fetch_most_recent_data(requestData["username"], rCrawler.headers, {'limit': 1000}, 1)
    #data["selftext"] = data["selftext"].encode().decode('utf-8', 'replace')
    #data["title"] = data["title"].encode().decode('utf-8', 'replace')
    d2 = data.to_json(orient="records");
    producer.send('reddit-threads', value=d2)  # sending the fetched threads to the topic called reddit-threads
    returnDic = {}
    returnDic["title"] = data["title"]
    returnDic["selftext"] = data["selftext"]
    return returnDic


@app.post("/source/discord")
async def get_discord_discussion(req: Request):
    requestData = await req.json()
    dCrawler = DiscordCrawler()
    data = dCrawler.get_discord_messages(requestData["channel_id"])
    print(type(data))
    data_json = json.dumps(data)
    producer.send('discord', value=data_json)
    return data


@app.post("/source/alien_vault")
async def get_alien_vault(req: Request):
    requestData = await req.json()
    aCrawler = AlienVault()
    pulses = aCrawler.get_pulse_by_keyword(requestData['keyword'])
    json_normalize(pulses["results"])
    pulse_id = pulses["results"][1]["id"]
    aCrawler.get_indicators_by_pulse(pulse_id)
    returnDict = {}
    threats = []

    for i, val in enumerate(pulses["results"]):
        threats.append("Name: " + val["name"] + " Description: " +
                       val["description"])
        returnDict[i] = threats
        threats = []

    return returnDict
