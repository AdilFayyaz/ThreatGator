import './style.css';
import React from 'react'
import './App.css';
import { Tab, Tabs, TabList, TabPanel } from 'react-tabs';
import Wave from 'react-wavify'
import {Card, Stack, Row, Button, Form, Image, Dropdown} from 'react-bootstrap'
import { navigate } from "@reach/router"//npm install @reach/router
import { useState, useEffect } from 'react'
import Dashboard from "./dashboard";
import {useNavigate} from "react-router-dom";

// class addSource is a component which will be called only when an Admin signs in
class addSource extends React.Component{


    constructor(){
        super();
        //all the state variables required in managing the add source component
        this.state ={
            accountName:'',
            account:'',
            prompt:'Enter',
            username:'',
            password:'',
            data: '',
            Url:"",
            SourceType:"",
            DataFormat:"",
            Name:"",
            numOfSources:0,
            sourcesNames: [ ],
            msg:"hii",
            selectedSource:"",

            handle:"0",
            user:"",
            isHandle:"",
            Date:""


        }

    }

    getPrompt(){
        if (this.accountName=="Twitter"){
            this.prompt="Enter Twitter Handle"
        }
        else if (this.accountName=="Reddit"){
            this.prompt="Enter Reddit Subreddit"
        }
        else if (this.accountName=="Discord"){
            this.prompt="Enter Channel ID"
        }
        else {
            this.prompt="Enter Account Name"
        }
        console.log(this.prompt)
    }

   // function to create drop down items from sources
   createDropDownItem(name){

        return(  <Dropdown.Item eventKey={name} >{name}</Dropdown.Item>);
    }
    // called when navigating to dashboard
    async navigateTo(){

        navigate("/dashboard", {replace: true})
        window.location.reload(false);
    }
    // the function fetches all the sources from the source management service
    async getSources(event){

        event.preventDefault();

        const requestOptions = {
            method: 'GET',
            headers: { 'Content-Type': 'application/json'}

        };
        //fething sources from source management service
        fetch('http://127.0.0.1:8081/sources/getAll', requestOptions)
            .then(response => response.json())
            .then((data) => {
                //push all sources in array
                this.state.numOfSources=data.length
                for (var i=0;i<this.state.numOfSources;i++){
                    this.state.sourcesNames.push(data[i])
                }



            });

    }
    // function that lets the user add sources via source management service
    async addSource(event){
        //await navigate("/dashboard", {replace: true})
        event.preventDefault();

        const requestOptions = {
            method: 'POST',
            headers: { 'Content-Type': 'application/json'},
            body: JSON.stringify({ name: this.state.Name,
                dataFormat: this.state.DataFormat,sourceType: this.state.SourceType,
                url: this.state.Url
            })

        };
        //connection to source management
        fetch('http://127.0.0.1:8081/sources/addSource', requestOptions)
            .then(response => response.text())
            .then((data) => {

                this.state.data=data

                console.log(data)
                if (this.state.data === "New source added"){
                    alert("New source added!");

                }
                else{
                    alert('Enter the required Information')
                }
            });
    }
    //function that adds accounts associated with the sources via source management service
    async addAccount(event){

        event.preventDefault();

        const requestOptions = {
            method: 'POST',
            headers: { 'Content-Type': 'application/json'},
            body: JSON.stringify({ source_id: this.state.selectedSource,
                handle: this.state.handle,username: this.state.user,isHandle:this.state.isHandle,
                Date: this.state.date
            })

        };
        //connection to source management
        fetch('http://127.0.0.1:8081/sources/addSource/account', requestOptions)
            .then(response => response.text())
            .then((data) => {

                this.state.data=data

                console.log(data)
                if (this.state.data === "New account added"){
                    alert("New account added");

                }
                else{
                    alert('Enter the correct information')
                }
            });
    }
    // handlers for different input fields
    async handleAccount(event){
        let items = [];
        for (let i=0; i<this.state.numOfSources; i++){
            items.push(this.createDropDownItem(this.sourcesNames[i]))
        }
        return items;
    }
    handleAccountName(event){
        // this.setState({username: event.target.value});
        this.state.accountName=event.target.value
        console.log(this.state.accountName)

    }
    handleName(event){
        // this.setState({username: event.target.value});
        this.state.Name=event.target.value
        console.log(this.state.Name)

    }
    getSourceWith(name){
        for (let i=0; i<this.state.numOfSources; i++){
            if(this.state.sourcesNames[i]["name"]==name){
                return (this.state.sourcesNames[i]["id"])
            }
        }
    }
    handleSelect=(e)=>{
        // console.log(e);
        // setValue(e)
        if (e=="Twitter") {
            this.state.selectedSource = this.getSourceWith("Twitter")
        }
        else if (e=="Reddit") {
            this.state.selectedSource = this.getSourceWith("Reddit")
        }
        else if (e=="Reddit") {
            this.state.selectedSource = this.getSourceWith("Reddit")
        }
        console.log(this.state.selectedSource);
    }
    handleDataFormat(event){
        this.state.DataFormat=event.target.value
        console.log(this.state.DataFormat)

    }
    handleSourceType(event) {
        this.state.SourceType = event.target.value
        console.log(this.state.SourceType)

    }

    handleURL(event){
            this.state.Url=event.target.value
            console.log(this.state.Url)

    }

    msg2;

    handleHandle(event) {
        // this.setState({username: event.target.value});
        this.state.handle = event.target.value
        this.state.isHandle="1"
        console.log(this.state.handle)

    }
    handleDate(event) {
        // this.setState({username: event.target.value});
        this.state.Date = event.target.value
        console.log(this.state.Date)

    }
    handleUser(event) {
        // this.setState({username: event.target.value});
        this.state.user = event.target.value
        console.log(this.state.user)

    }
    async logOutNavigation(){
        console.log("logging out")
        navigate("/", {replace: true})
        window.location.reload(false)
    }
    

    render(){

        return(
                //main container
                <div className="App-header" >
                        {/*back ground card display*/}
                        <div className="bg-card" style={{borderRadius:"3rem"}}>

                        {/*threat gator logo image */}
                        <img className="image" src="./threatgator.png" style={{borderRadius: '0rem',width:"20rem",height:"20rem"}}/>
                        {/*tabs list */}
                        <Tabs className="tabs">
                            <TabList  style={{}}>
                                <Tab style={{color:"silver"}}>Add Source</Tab>
                                <Tab style={{color:"silver"}} onClick={ this.getSources.bind(this) }>Add Account</Tab>

                            </TabList>
                            <TabPanel>
                                <p align={"center"} style={{color:"silver"}}>Add Source</p>
                                <Form style={{alignContent:"center"}} onSubmit={(e)=>{
                                    // add source to source manager when all information has been provided
                                    this.addSource(e);
                                }}>


                                    {/*name of the source eg Twitter*/}
                                    <Form.Group className="mb-3" controlId="formBasicName" onChange={this.handleName.bind(this)}>
                                        <Form.Control style={{marginLeft:"33%",paddingTop:"2%",width:"35%",height:"5%",borderRadius:"50px",backgroundColor:"transparent",color:"white"}} type="name" placeholder="Source Name" />

                                    </Form.Group>
                                    <Row style={{position:"absolute",margin:"5%"}}></Row>

                                    {/*data format from the source eg txt,xml,json*/}
                                    <Form.Group className="mb-3" controlId="formBasicName" onChange={this.handleDataFormat.bind(this)}>
                                        <Form.Control style={{marginLeft:"33%",paddingTop:"2%",width:"35%",height:"5%",borderRadius:"50px",backgroundColor:"transparent",color:"white"}} type="name" placeholder="Data format" />

                                    </Form.Group>
                                    {/*source type e.g  social media*/}
                                    <Form.Group className="mb-3" controlId="formBasicName" onChange={this.handleSourceType.bind(this)}>
                                        <Form.Control style={{marginLeft:"33%",paddingTop:"2%",width:"35%",height:"5%",borderRadius:"50px",backgroundColor:"transparent",color:"white"}} type="name" placeholder="Source Type" />
                                    </Form.Group>
                                    {/*link of the source eg https://www.twitter.com*/}
                                    <Form.Group className="mb-3" controlId="formBasicURL" onChange={this.handleURL.bind(this)}>
                                        <Form.Control style={{marginLeft:"33%",paddingTop:"2%",width:"35%",height:"5%",borderRadius:"50px",backgroundColor:"transparent",color:"white"}} type="url" placeholder="Url" />
                                    </Form.Group>
                                    <button style={{marginLeft:"12.5rem",borderRadius:"10px",backgroundColor:"silver"}}  type="submit">Add Source</button>
                                 </Form>
                            </TabPanel>
                            <TabPanel style={{}}>
                        <div>
                        <p style={{color:"silver"}}>Add Account</p>
                        <Form style={{alignContent:"center"}}
                            onSubmit={(event) => { this.addAccount(event)}}>
                                <Dropdown onSelect={this.handleSelect} title="Select Source">
                                    <Dropdown.Toggle  id="dropdown-button-dark-example1" variant="secondary" style={{backgroundColor:"#0000ff"}}title="Select Source">
                                        Select Source
                                    </Dropdown.Toggle>

                                    <Dropdown.Menu variant="dark"   >

                                        {this.createDropDownItem("Twitter")
                                            //this.handleAccount()
                                        }
                                        {this.createDropDownItem("Reddit")
                                            //this.handleAccount()
                                        }

                                    </Dropdown.Menu>
                                </Dropdown>

                                {/*handle incase of  twitter account*/}
                                <Form.Group className="mb-3" controlId="formBasicName" onChange={this.handleHandle.bind(this)}>
                                    <Form.Control style={{marginLeft:"33%",paddingTop:"2%",width:"35%",height:"5%",borderRadius:"50px",backgroundColor:"transparent",color:"white"}} type="name" placeholder="Enter handle(twitter)" />

                                </Form.Group>
                            {/*user incase of reddit account*/}
                            <Form.Group className="mb-3" controlId="formBasicName" onChange={this.handleUser.bind(this)}>
                                <Form.Control style={{marginLeft:"33%",paddingTop:"2%",width:"35%",height:"5%",borderRadius:"50px",backgroundColor:"transparent",color:"white"}} type="name" placeholder="Enter username(reddit)" />

                            </Form.Group>
                            {/*date of tweet or reddit subthread if applicable*/}
                            <Form.Group className="mb-3" controlId="formBasicDate" onChange={this.handleDate.bind(this)}>
                                <Form.Control style={{marginLeft:"33%",paddingTop:"2%",width:"35%",height:"5%",borderRadius:"50px",backgroundColor:"transparent",color:"white"}} type="date" placeholder="Enter Date" />

                            </Form.Group>
                                <Row style={{position:"absolute",margin:"5%"}}></Row>

                                {/*<Row style={{margin:"1%"}}></Row>*/}
                                {/* <Row style={{margin:"20px"}}></Row> */}
                                <button style={{borderRadius:"10px",backgroundColor:"silver",marginLeft:"12.5rem"}}  type="submit">
                                    Add Account
                                </button>

                            </Form>
                            </div>
                            </TabPanel>

                        </Tabs>
                        <div>
                        {/*    log out and navigation to sign in page*/}
                        <button style={{borderRadius:"10px",backgroundColor:"silver",marginLeft:"12.5rem"}}
                            onClick={
                                this.logOutNavigation.bind(this)
                            }>
                            Log Out
                        </button>
                        </div>

                    </div>
                </div>


        );
    }
}

export default addSource;
