import './style.css';
import React from 'react'
import './App.css';
import './table.css';

import {  Table } from "semantic-ui-react";
import { Tab, Tabs, TabList, TabPanel } from 'react-tabs';
import {Card, Stack, Row, Button, Form, Image, Dropdown} from 'react-bootstrap'


class ViewConsumedData extends React.Component{


    constructor(){
        super();
        this.state ={
            tweets:[],
            threads:[],
            comments:[],
            tagged:[],
            inferencedSentence:"",
            sentence:"",
            data: '',
            Url:"",

        }


    }

    async getTweets(event){
        event.preventDefault();

        fetch('http://127.0.0.1:8080/viewRawTweets').then(function(response){
            console.log(response)
            return response.json();
        })
            .then((myJson) =>{
                console.log(myJson)
                this.setState({tweets:myJson})
            });
    }

    async getThreads(event){
        event.preventDefault();

        fetch('http://127.0.0.1:8080/viewRawThreads').then(function(response){
            console.log(response)
            return response.json();
        })
            .then((myJson) =>{
                console.log(myJson)
                this.setState({threads:myJson})
            });
    }

    async getComments(event){
        event.preventDefault();

        fetch('http://127.0.0.1:8080/viewRawComments').then(function(response){
            console.log(response)
            return response.json();
        })
            .then((myJson) =>{
                console.log(myJson)
                this.setState({comments:myJson})
            });
    }

    async getTaggedData(event){
        event.preventDefault();

        fetch('http://127.0.0.1:8080/viewTaggedData').then(function(response){
            console.log(response)
            return response.json();
        })
            .then((myJson) =>{
                console.log(myJson)
                this.setState({tagged:myJson})
            });
    }

    async getInference(event){
        event.preventDefault();

        fetch('http://127.0.0.1:8080/getInference?'+ new URLSearchParams({
            sentence:this.state.sentence
        })).then(function(response){
            console.log(response)
            return response.json();
        })
            .then((myJson) =>{
                console.log(myJson)
                this.setState({inferencedSentence:myJson})
            });
    }

    async handleSentence(event){
        this.state.sentence=event.target.value
    }


    render(){

        return(

            <div className="App-header" >

                <div className="bg-card" style={{borderRadius:"3rem"}}>

                    {/*<div className="image">*/}
                    <img className="image" src="./threatgator.png" style={{borderRadius: '0rem',width:"20rem",height:"20rem"}}/>
                    {/*</div>*/}
                    {/*<div className="tabs">*/}
                    <Tabs className="tabs">
                        <TabList  style={{}}>
                            <Tab style={{color:"silver"}} onClick={ this.getTweets.bind(this) }>Get Raw Tweets</Tab>
                            <Tab style={{color:"silver"}} onClick={ this.getThreads.bind(this) }>Get Raw Threads</Tab>
                            <Tab style={{color:"silver"}} onClick={ this.getComments.bind(this) }>Get Raw Comments</Tab>
                            <Tab style={{color:"silver"}} onClick={ this.getTaggedData.bind(this) }>Get Tagged Data</Tab>
                            <Tab style={{color:"silver"}} >Tag Sentence</Tab>
                        </TabList>
                        <TabPanel>
                            <p align={"center"} style={{color:"silver"}}>Tweets:</p>
                            <p>{this.state.tweets.map(tweet => <div>{tweet.body}</div>)}</p>

                        </TabPanel>
                        <TabPanel>
                            <p align={"center"} style={{color:"silver"}}>Threads:</p>
                            <div className="container__table">
                                <p>{this.state.threads.map(thread => <div>
                                    <p>Title:</p>
                                    {thread.title}
                                    <p>Text: </p>
                                    {thread.selftext}


                                </div>)}</p>
                            {/*<Table singleLine>*/}
                            {/*    <Table.Header>*/}
                            {/*        <Table.Row>*/}
                            {/*            <Table.HeaderCell>Title</Table.HeaderCell>*/}
                            {/*            <Table.HeaderCell>Self Text</Table.HeaderCell>*/}
                            {/*        </Table.Row>*/}
                            {/*    </Table.Header>*/}
                            {/*    <Table.Body>*/}
                            {/*        {this.state.threads.map(thread => {*/}
                            {/*            return (*/}
                            {/*                <Table.Row>*/}
                            {/*                    <Table.Cell>{thread.title}</Table.Cell>*/}
                            {/*                    <Table.Cell>{thread.selftext}</Table.Cell>*/}
                            {/*                </Table.Row>*/}
                            {/*            );*/}
                            {/*        })}*/}
                            {/*    </Table.Body>*/}
                            {/*</Table>*/}
                            </div>


                        </TabPanel>
                        <TabPanel>
                            <p align={"center"} style={{color:"silver"}}>Comments:</p>
                            <p>{this.state.comments.map(comment => <div>{comment.body}</div>)}</p>

                        </TabPanel>
                        <TabPanel>
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
                                        {this.state.tagged.map(el => {
                                            return (
                                                <Table.Row>
                                                    <Table.Cell>{el.source}</Table.Cell>
                                                    <Table.Cell>{el.rawText}</Table.Cell>
                                                    <Table.Cell>{el.malwares.map(tweet => <div>{tweet.name}</div>)}</Table.Cell>
                                                    <Table.Cell>{el.vulnerabilities.map(tweet => <div>{tweet.name}</div>)}}</Table.Cell>
                                                    <Table.Cell>{el.locations.map(tweet => <div>{tweet.name}</div>)}}</Table.Cell>
                                                    <Table.Cell>{el.threatActors.map(tweet => <div>{tweet.name}</div>)}}</Table.Cell>
                                                    <Table.Cell>{el.identities.map(tweet => <div>{tweet.name}</div>)}}</Table.Cell>
                                                    <Table.Cell>{el.tools.map(tweet => <div>{tweet.name}</div>)}}</Table.Cell>
                                                    <Table.Cell>{el.infrastructures.map(tweet => <div>{tweet.name}</div>)}}</Table.Cell>
                                                    <Table.Cell>{el.campaigns.map(tweet => <div>{tweet.name}</div>)}}</Table.Cell>
                                                </Table.Row>
                                            );
                                        })}
                                    </Table.Body>

                                </Table>

                            </div>


                        </TabPanel>

                        <TabPanel>
                            <Form style={{alignContent:"center"}} onSubmit={(e)=>{
                                this.getInference(e).then(r => console.log(r) )

                            }}>

                                <Form.Group className="mb-3" controlId="formBasicName" onChange={this.handleSentence.bind(this)}>
                                    <Form.Control style={{marginLeft:"33%",paddingTop:"2%",width:"35%",height:"5%",borderRadius:"50px",backgroundColor:"transparent",color:"white"}} type="name" placeholder="Sentence" />

                                </Form.Group>
                                <Row style={{position: "absolute", margin: "5%"}}/>

                                <button style={{marginLeft:"12.5rem",borderRadius:"10px",backgroundColor:"silver"}}  type="submit">Inference</button>
                            </Form>
                            {this.state.inferencedSentence?(
                                <Table singleLine>
                                    <Table.Header>
                                        <Table.Row>
                                            {this.state.inferencedSentence[0].map(el=>{
                                                return(
                                                    <Table.HeaderCell>{el}</Table.HeaderCell>                                                )
                                            })}
                                        </Table.Row>
                                    </Table.Header>
                                    <Table.Row>
                                        {this.state.inferencedSentence[1].map(el => {
                                            return(
                                                <Table.Cell>{el}</Table.Cell>
                                            )

                                        })}
                                    </Table.Row>

                                </Table>
                            ):(
                                <p></p>
                            )}
                        </TabPanel>

                    </Tabs>

                </div>
            </div>


        );
    }
}

export default ViewConsumedData;
