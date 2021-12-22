import './style.css';
import React, { useState } from 'react'
import './App.css';
import './table.css';

import {  Table } from "semantic-ui-react";
import { Tab, Tabs, TabList, TabPanel } from 'react-tabs';
import {Card, Stack, Row, Button, Form, Image, Dropdown} from 'react-bootstrap'
import {Modal} from "react-bootstrap";
var scraperApiURL = "http://127.0.0.1:8000/"
//UI to show data being consumed from kafka
const ViewConsumedData = () =>{
   
    const [tweets,SetTweets] = useState([])
    const [threads,SetThreads] = useState([])
    const [comments, SetComments] = useState([])
    const [tagged, SetTagged] = useState([])
    const [inferencedSentence, SetinferencedSentence] = useState('')
    const [sentence, SetSentence] = useState('');

    const [isOpenTweets, SetIsOpenTweets] = useState()
    // Note this Threads var is being used for tagged data
    // Sorry for the wrong naming lol
    const [isOpenThreads, SetIsOpenThreads] = useState()
    const [isOpenRawThreads, SetIsOpenRawThreads] = useState()
    const [isOpenComments, SetIsOpenComments] = useState()
    const [isOpenSentence, SetIsOpenSentence] = useState()
    const [isOpenScrapeTwt, SetIsOpenScrapeTwt] = useState()
    const [isOpenScrapeReddit, SetIsOpenScrapeReddit] = useState()

    const openModalTweets = () => SetIsOpenTweets(true);
    const closeModalTweets = () => SetIsOpenTweets(false);

    const openModalThreads = () => SetIsOpenThreads (true);
    const closeModalThreads  = () => SetIsOpenThreads(false);

    const openModalRawThreads = () => SetIsOpenRawThreads (true);
    const closeModalRawThreads  = () => SetIsOpenRawThreads(false);

    const openModalComments = () => SetIsOpenComments (true);
    const closeModalComments = () => SetIsOpenComments(false);

    const openModalSentence = () => SetIsOpenSentence(true);
    const closeModalSentence = () => SetIsOpenSentence(false);

    const openModalScrapeTwt = () => SetIsOpenScrapeTwt(true);
    const closeModalScrapeTwt = () => SetIsOpenScrapeTwt(false);

    const openModalScrapeReddit = () => SetIsOpenScrapeReddit(true);
    const closeModalScrapeReddit = () => SetIsOpenScrapeReddit(false);

    //connection with data consumer service to get tweets
    const getTweets = (event) =>{
        event.preventDefault();
        fetch('http://127.0.0.1:8080/viewRawTweets')
        .then(res => res.json())
            .then((myJson) =>{
                SetTweets(myJson)
                console.log(myJson)
                openModalTweets()
            });
    }
    //connection with data consumer service to get reddit threads
    const getThreads = (event) =>{
        event.preventDefault();
        fetch('http://127.0.0.1:8080/viewRawThreads')
        .then(res => res.json())
        .then((data) =>{
                SetThreads(data)
                console.log(threads)
                openModalRawThreads()
            });
    }
    //connection with data consumer service to get comments
    const getComments = (event) =>{
        event.preventDefault();
        fetch('http://127.0.0.1:8080/viewRawComments')
        .then(res => res.json())
        .then((myJson) =>{
                console.log(myJson)
                SetComments(myJson)
                openModalComments()
        });
    }
    //connection with data consumer service to get tagged data
    const getTaggedData = (event) => {
        event.preventDefault();

        fetch('http://127.0.0.1:8080/viewTaggedData')
        .then(res => res.json())
        .then((myJson) =>{
                console.log(myJson)
                openModalThreads()
                SetTagged(myJson)
        });
    }

    //connection with data consumer service to get inference for a particular sentence
    const getInference = (event) =>{
        event.preventDefault();

        fetch('http://127.0.0.1:8080/getInference?'+ new URLSearchParams({
            sentence:sentence
        }))
        .then(res => res.json())
        .then((myJson) =>{
            SetinferencedSentence(myJson);
            openModalSentence()
        });
    }

    const handleSentence = (event) =>{
        event.preventDefault();
        SetSentence(event.target.value)
        // this.state.sentence=event.target.value
    }
    // social media scrapers
    const scrapeTwitter = (event) =>{
        event.preventDefault()
        openModalScrapeTwt()
    }
    function getAllTweets(name,value,dateValue) {
        fetch(scraperApiURL + 'source/twitter', {
            method: 'POST',
            headers: {
              'Accept': 'application/json',
              'Content-Type': 'application/json',
            },
            body: JSON.stringify({
              handle: name,
              Date: dateValue,
              isHandle: value
            })
          })
          .then(res => res.json())
          .then((data) => {
            
          })
    }

    const fetchAllTweets = (event) => {
           
        if(event.target.twtHandle.value && event.target.tillDate.value){
          console.log(event.target.twtHandle.value);
          getAllTweets(event.target.twtHandle.value, 1, event.target.tillDate.value);
        }
        else if (event.target.twtHashtag.value && event.target.tillDate.value){
          console.log(event.target.twtHashtag.value);
          getAllTweets(event.target.twtHashtag.value, 0, event.target.tillDate.value)
        }
        else{
            alert("Enter Handle/Hashtag and Date");
        }
        event.preventDefault();
    }

    const scrapeReddit = (event) => {
        event.preventDefault()
        fetch(scraperApiURL + 'source/reddit/mostRecentData', {
            method: 'POST',
            headers: {
              'Accept': 'application/json',
              'Content-Type': 'application/json', 
            },
            body: JSON.stringify({
              username: event.target.username.value
            })
          })
          .then(res => res.json())
          .then((data) => {
           
          })
    }
    const scrapeRedditOpen = (event) =>{
        event.preventDefault()
        openModalScrapeReddit()
    }
    // call this to push on elastic
    const pushToElastic = (event) => {
        event.preventDefault()
        fetch("http://127.0.0.1:8080/pushToElastic")
          .then(res => res.json())
          .then((data) => {
           
          })
    }
    return(

        <div className="App-header" >
            <div>
            <Button href="/dashboard"style={{backgroundColor: '#162237',borderRight:"transparent",borderLeft:"transparent",borderBottom:"transparent"}} >Return</Button>
            </div>
                <div className="bg-card" style={{borderRadius:"3rem"}}>
                    {/*threatgator image*/}
                    <img className="image" src="./threatgator.png" style={{borderRadius: '0rem',width:"20rem",height:"20rem"}}/>
                    {/*</div>*/}
                    {/*tab list starts here*/}
                    <Tabs className="tabs">
                        <TabList  style={{}}>
                            <Tab style={{color:"silver"}} onClick={scrapeTwitter}>Scrape Twitter</Tab>
                            <Tab style={{color:"silver"}} onClick={scrapeRedditOpen}>Scrape Reddit</Tab>
                            <Tab style={{color:"silver"}} onClick={ getTweets }>Get Raw Tweets</Tab>
                            <Tab style={{color:"silver"}} onClick={ getThreads }>Get Raw Threads</Tab>
                            <Tab style={{color:"silver"}} onClick={ getComments }>Get Raw Comments</Tab>
                            <Tab style={{color:"silver"}} onClick={ getTaggedData }>Get Tagged Data</Tab>
                            <Tab style={{color:"silver"}} >Tag Sentence</Tab>
                            <Tab style={{color:"silver"}} onClick={ pushToElastic }>Push To Elastic</Tab>
                        </TabList>
                        <TabPanel>
                            Scrape Twitter
                        </TabPanel>
                        <TabPanel>
                            Scrape Reddit
                        </TabPanel>
                        <TabPanel>
                            Tweets Body
                        </TabPanel>
                        <TabPanel>
                            Thread Body
                        </TabPanel>
                        <TabPanel>
                           Comments Body
                        </TabPanel>
                        <TabPanel>
                            View Tagged Data       
                        </TabPanel>
                        <TabPanel>
                        <Form style={{alignContent:"center"}}
                            onSubmit={getInference}>

                                <Form.Group className="mb-3" controlId="formBasicName" onChange={handleSentence}>
                                    <Form.Control style={{marginLeft:"33%",paddingTop:"2%",width:"35%",height:"5%",borderRadius:"50px",backgroundColor:"transparent",color:"white"}} type="name" placeholder="Sentence" />

                                </Form.Group>
                                <Row style={{position: "absolute", margin: "5%"}}/>

                                <button style={{marginLeft:"12.5rem",borderRadius:"10px",backgroundColor:"silver"}}  type="submit">Inference</button>
                            </Form>
                        </TabPanel>
                        <TabPanel>
                            Pushed To ElasticSearch      
                        </TabPanel>
                    </Tabs>
                    {/* Display the Tagged Result */}
                    <Modal show={isOpenThreads} onHide={closeModalThreads}
                                        // size = "lg"
                            fullscreen = {true}
                            aria-labelledby="contained-modal-title-vcenter"
                            centered
                            >
                                <Modal.Header>
                                <Modal.Title id="contained-modal-title-vcenter">
                                Indicators
                                </Modal.Title>
                            </Modal.Header>
                            <Modal.Body>
                            <p align={"center"} style={{color:"silver"}}>Tagged Data:</p>
                            <div className="container__table">
                                <Table singleLine>
                                    <Table.Header>
                                        <Table.Row>
                                            <Table.HeaderCell>Source</Table.HeaderCell>
                                            <Table.HeaderCell>Raw Text</Table.HeaderCell>
                                            <Table.HeaderCell>Malwares</Table.HeaderCell>
                                            <Table.HeaderCell>Vulnerabilities</Table.HeaderCell>
                                            <Table.HeaderCell>Locations</Table.HeaderCell>
                                            <Table.HeaderCell>Threat Actors</Table.HeaderCell>
                                            <Table.HeaderCell>Identities</Table.HeaderCell>
                                            <Table.HeaderCell>Tools</Table.HeaderCell>
                                            <Table.HeaderCell>Infrastructures</Table.HeaderCell>
                                            <Table.HeaderCell>Indicators</Table.HeaderCell>
                                            <Table.HeaderCell>Campaigns</Table.HeaderCell>
                                        </Table.Row>
                                    </Table.Header>
                                    <Table.Body>
                                        {tagged.map(el => {
                                            return (
                                                <Table.Row>
                                                    <Table.Cell>{el.source}</Table.Cell>
                                                    <Table.Cell>{el.rawText}</Table.Cell>
                                                    <Table.Cell>{el.malwares.map(tweet => <div>{tweet.name}</div>)}</Table.Cell>
                                                    <Table.Cell>{el.vulnerabilities.map(tweet => <div>{tweet.name}</div>)}</Table.Cell>
                                                    <Table.Cell>{el.locations.map(tweet => <div>{tweet.name}</div>)}</Table.Cell>
                                                    <Table.Cell>{el.threatActors.map(tweet => <div>{tweet.name}</div>)}</Table.Cell>
                                                    <Table.Cell>{el.identities.map(tweet => <div>{tweet.name}</div>)}</Table.Cell>
                                                    <Table.Cell>{el.tools.map(tweet => <div>{tweet.name}</div>)}</Table.Cell>
                                                    <Table.Cell>{el.infrastructures.map(tweet => <div>{tweet.name}</div>)}</Table.Cell>
                                                    <Table.Cell>{el.campaigns.map(tweet => <div>{tweet.name}</div>)}</Table.Cell>
                                                </Table.Row>
                                            );
                                        })}
                                    </Table.Body>

                                </Table>
                            </div>
                            </Modal.Body>
                            <Modal.Footer>
                                <Button onClick={closeModalThreads}>Close</Button>
                            </Modal.Footer>
                    </Modal>

                    {/* Get Tweets Body */}
                    <Modal show={isOpenTweets} onHide={closeModalTweets}
                                        // size = "lg"
                            fullscreen = {true}
                            aria-labelledby="contained-modal-title-vcenter"
                            centered
                            >
                                <Modal.Header>
                                <Modal.Title id="contained-modal-title-vcenter">
                                Twitter
                                </Modal.Title>
                            </Modal.Header>
                            <Modal.Body>
                            <p align={"center"} style={{color:"silver"}}>Tweets:</p>
                            <p>{tweets.map(tweet => <div>{tweet.body}</div>)}</p>
                            </Modal.Body>
                            <Modal.Footer>
                                <Button onClick={closeModalTweets}>Close</Button>
                            </Modal.Footer>
                    </Modal>

                    {/*  Get Raw Reddit Threads*/}
                    <Modal show={isOpenRawThreads} onHide={closeModalRawThreads}
                                        // size = "lg"
                            fullscreen = {true}
                            aria-labelledby="contained-modal-title-vcenter"
                            centered
                            >
                                <Modal.Header>
                                <Modal.Title id="contained-modal-title-vcenter">
                                Raw Threads
                                </Modal.Title>
                            </Modal.Header>
                            <Modal.Body>
                            <p align={"center"} style={{color:"silver"}}>Threads:</p>
                            <div className="container__table">
                                <p>{threads.map(thread => <div>
                                    <p>Title:</p>
                                    {thread.title}
                                    <p>Text: </p>
                                    {thread.selftext}
                                </div>)}</p>
                            </div>
                            </Modal.Body>
                            <Modal.Footer>
                                <Button onClick={closeModalRawThreads}>Close</Button>
                            </Modal.Footer>
                    </Modal>

                    {/*  Get Raw Reddit Comments*/}
                    <Modal show={isOpenComments} onHide={closeModalComments}
                                        // size = "lg"
                            fullscreen = {true}
                            aria-labelledby="contained-modal-title-vcenter"
                            centered
                            >
                                <Modal.Header>
                                <Modal.Title id="contained-modal-title-vcenter">
                                Comments
                                </Modal.Title>
                            </Modal.Header>
                            <Modal.Body>
                            <p align={"center"} style={{color:"silver"}}>Comments:</p>
                            <p>{comments.map(comment => <div>{comment.body}</div>)}</p>
                            </Modal.Body>
                            <Modal.Footer>
                                <Button onClick={closeModalComments}>Close</Button>
                            </Modal.Footer>
                    </Modal>

                     {/*  Get Sentence Inference*/}
                     <Modal show={isOpenSentence} onHide={closeModalSentence}
                            size = "lg"
                            // fullscreen = {true}
                            aria-labelledby="contained-modal-title-vcenter"
                            centered
                            >
                                <Modal.Header>
                                <Modal.Title id="contained-modal-title-vcenter">
                                Comments
                                </Modal.Title>
                            </Modal.Header>
                            <Modal.Body>
      
                            {inferencedSentence?(
                                <Table singleLine>
                                    <Table.Header>
                                        <Table.Row>
                                            {inferencedSentence[0].map(el=>{
                                                return(
                                                    <Table.HeaderCell>{el}</Table.HeaderCell>                                                )
                                            })}
                                        </Table.Row>
                                    </Table.Header>
                                    <Table.Row>
                                        {inferencedSentence[1].map(el => {
                                            return(
                                                <Table.Cell>{el}</Table.Cell>
                                            )
                                        })}
                                    </Table.Row>

                                </Table>
                            ):(
                                <p></p>
                            )}
                            </Modal.Body>
                            <Modal.Footer>
                                <Button onClick={closeModalSentence}>Close</Button>
                            </Modal.Footer>
                    </Modal>


                     {/*  Get Scrape Twitter*/}
                     <Modal show={isOpenScrapeTwt} onHide={closeModalScrapeTwt}
                            size = "lg"
                            // fullscreen = {true}
                            aria-labelledby="contained-modal-title-vcenter"
                            centered
                            >
                                <Modal.Header>
                                <Modal.Title id="contained-modal-title-vcenter">
                                Comments
                                </Modal.Title>
                            </Modal.Header>
                            <Modal.Body>
                            <form onSubmit={fetchAllTweets}>
                                <Form.Group role="form" controlId="twtHandle">
                                    <Form.Control type = "twt" placeholder="Add User Handle" />
                                    
                                </Form.Group>
                                <Form.Group role="form" controlId="twtHashtag">
                                    <Form.Label style={{fontSize:"20px"}}>OR</Form.Label>
                                    <Form.Control type = "twt" placeholder="Add Hashtag" />
                                
                                </Form.Group>
                                <Form.Group role="form" controlId="tillDate">
                                    <Form.Label style={{fontSize:"20px"}}>Enter the date</Form.Label>
                                    <Form.Control type = "twt" placeholder="Enter Date (Oct 23).. " />
                                
                                    <Button type="submit"> Get Tweets! </Button>
                                </Form.Group>
                            </form>

                            </Modal.Body>
                            <Modal.Footer>
                                <Button onClick={closeModalScrapeTwt}>Close</Button>
                            </Modal.Footer>
                    </Modal>

                    {/*  Get Scrape Reddit*/}
                    <Modal show={isOpenScrapeReddit} onHide={closeModalScrapeReddit}
                            size = "lg"
                            // fullscreen = {true}
                            aria-labelledby="contained-modal-title-vcenter"
                            centered
                            >
                                <Modal.Header>
                                <Modal.Title id="contained-modal-title-vcenter">
                                Most Recent Reddit Data
                                </Modal.Title>
                            </Modal.Header>
                            <Modal.Body>
                            <form onSubmit={scrapeReddit}>
                                <Form.Group role="form" controlId="username" classname="mb-3">
                                <Form.Control type = "reddit" placeholder="Add Username" />
                                </Form.Group>
                                <Form.Group className="mb-3" controlId = "reddit_option">
                                <Button type="submit"> Get Reddit Data! </Button>
                                </Form.Group>  
                            </form>

                            </Modal.Body>
                            <Modal.Footer>
                                <Button onClick={closeModalScrapeReddit}>Close</Button>
                            </Modal.Footer>
                    </Modal>
                </div>
            </div>
        

    );
    
}

export default ViewConsumedData;
