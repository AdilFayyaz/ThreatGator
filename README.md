# ThreatGator
## Description
ThreatGator is a Threat Intelligence Platform (TIP). <br/>
It includes curation of threat intelligence data from different sources particularly social media, prioritization of threats, correlation of data based on Indicators of Compromise (IOCs) and effective visualization of extracted information on an interative dashboard. <br/>
## Required Tools & Technologies
1. Java 8 and 11
2. Linux Based OS
3. Springboot Framework
4. Hibernate
5. Kafka Server (3.0.0)
6. ElasticSearch (7.13.3)
7. MySQL
8. Python
9. React JS

## Steps to Run Code
### Start Kafka Server
1. bin/zookeeper-server-start.sh config/zookeeper.properties (to start zookeeper)
2. bin/kafka-server-start.sh config/server.properties (to start kafka broker)
### Start ElasticSearch
./bin/elasticsearch
### Start Python Crawler
1. Navigate to Crawler Director
2. Run the following command: uvicorn main:app --reload (running on port 8000)
### Start BERT Inference
1. Download Model from https://drive.google.com/drive/folders/1eGOOkR304ssgh_Bh0-6ZWEsoGEGvmveJ?usp=sharing *
2. Place Model in bert_inference directory
3. Run the following command: uvicorn relation_inferencer:app --reload --port 5000
### Start SpringBoot Services
1. Open SpringBoot App as a Maven Project on IntelliJ
2. Download all Maven Dependencies
3. Run all services from the 'Services' window
### Start React Server
1. Navigate to Front End
2. Run npm install to download required dependencies
3. Run npm start to start React Server
4. Navigate to https://localhost:3000 to access sign in page for ThreatGator

## Hardware Requirements
Minimum 12 GB RAM <br/>
Preferably 16 GB RAM

<br/>
*Model has been trained using the following notebook by Niels Rogge: https://colab.research.google.com/github/NielsRogge/Transformers-Tutorials/blob/master/BERT/Custom_Named_Entity_Recognition_with_BERT_only_first_wordpiece.ipynb#scrollTo=4Gz-wHAw3xMk
