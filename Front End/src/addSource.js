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
// import {Tab, TabList, TabPanel, Tabs} from "react-tabs";


class addSource extends React.Component{


    constructor(){
        super();
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

   createDropDownItem(name){

        return(  <Dropdown.Item eventKey={name} >{name}</Dropdown.Item>);
    }
    async navigateTo(){

        navigate("/dashboard", {replace: true})
        window.location.reload(false);
    }
    async getSources(event){

        //await navigate("/dashboard", {replace: true})
        event.preventDefault();

        const requestOptions = {
            method: 'GET',
            headers: { 'Content-Type': 'application/json'}

        };
        fetch('http://127.0.0.1:8081/sources/getAll', requestOptions)
            .then(response => response.json())
            .then((data) => {

                this.state.numOfSources=data.length
                for (var i=0;i<this.state.numOfSources;i++){
                    // this.setState({ sourcesNames: [this.state.sourcesNames, new String(data[i]["name"])] })
                    this.state.sourcesNames.push(data[i])
                }
                this.state.msg="hello"
                this.msg2=this.state.sourcesNames[0]
                console.log("hererere")


            });

    }
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
        fetch('http://127.0.0.1:8081/sources/addSource', requestOptions)
            .then(response => response.text())
            .then((data) => {

                this.state.data=data

                console.log(data)
                if (this.state.data === "New source added"){
                    alert("New source added!");
                    // this.navigateTo();
                    // await navigate("/dashboard")
                }
                else{
                    alert('Enter the required Information')
                }
            });
    }
    async addAccount(event){
     console.log("innn")
        event.preventDefault();

        const requestOptions = {
            method: 'POST',
            headers: { 'Content-Type': 'application/json'},
            body: JSON.stringify({ source_id: this.state.selectedSource,
                handle: this.state.handle,username: this.state.user,isHandle:this.state.isHandle,
                Date: this.state.date
            })

        };
        fetch('http://127.0.0.1:8081/sources/addSource/account', requestOptions)
            .then(response => response.text())
            .then((data) => {

                this.state.data=data

                console.log("hereeeeeeeeeeeeeeeeeeeeeee")
                console.log(data)
                if (this.state.data === "New account added"){
                    alert("New account added");
                    // this.navigateTo();
                    // await navigate("/dashboard")
                }
                else{
                    alert('Enter the correct credentials')
                }
            });
    }
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
        // this.setState({username: event.target.value});
        this.state.DataFormat=event.target.value
        console.log(this.state.DataFormat)

    }
    handleSourceType(event) {
        // this.setState({username: event.target.value});
        this.state.SourceType = event.target.value
        console.log(this.state.SourceType)

    }

    handleURL(event){
            // this.setState({username: event.target.value});
            this.state.Url=event.target.value
            console.log(this.state.Url)

    }
    // handleAccount=(e)=>{
    //     console.log(e);
    //     // setValue(e)
    // }
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

                <div className="App-header" >

                        <div className="bg-card" style={{borderRadius:"3rem"}}>

                        {/*<div className="image">*/}
                        <img className="image" src="./threatgator.png" style={{borderRadius: '0rem',width:"20rem",height:"20rem"}}/>
                        {/*</div>*/}
                        {/*<div className="tabs">*/}
                        <Tabs className="tabs">
                            <TabList  style={{}}>
                                <Tab style={{color:"silver"}}>Add Source</Tab>
                                <Tab style={{color:"silver"}} onClick={ this.getSources.bind(this) }>Add Account</Tab>

                            </TabList>
                            <TabPanel>
                                <p align={"center"} style={{color:"silver"}}>Add Source</p>
                                <Form style={{alignContent:"center"}} onSubmit={(e)=>{

                                    this.addSource(e);
                                }}>



                                    <Form.Group className="mb-3" controlId="formBasicName" onChange={this.handleName.bind(this)}>
                                        <Form.Control style={{marginLeft:"33%",paddingTop:"2%",width:"35%",height:"5%",borderRadius:"50px",backgroundColor:"transparent",color:"white"}} type="name" placeholder="Source Name" />

                                    </Form.Group>
                                    <Row style={{position:"absolute",margin:"5%"}}></Row>


                                    <Form.Group className="mb-3" controlId="formBasicName" onChange={this.handleDataFormat.bind(this)}>
                                        <Form.Control style={{marginLeft:"33%",paddingTop:"2%",width:"35%",height:"5%",borderRadius:"50px",backgroundColor:"transparent",color:"white"}} type="name" placeholder="Data format" />

                                    </Form.Group>

                                    <Form.Group className="mb-3" controlId="formBasicName" onChange={this.handleSourceType.bind(this)}>
                                        <Form.Control style={{marginLeft:"33%",paddingTop:"2%",width:"35%",height:"5%",borderRadius:"50px",backgroundColor:"transparent",color:"white"}} type="name" placeholder="Source Type" />
                                    </Form.Group>

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


                                <Form.Group className="mb-3" controlId="formBasicName" onChange={this.handleHandle.bind(this)}>
                                    <Form.Control style={{marginLeft:"33%",paddingTop:"2%",width:"35%",height:"5%",borderRadius:"50px",backgroundColor:"transparent",color:"white"}} type="name" placeholder="Enter handle(twitter)" />

                                </Form.Group>
                            <Form.Group className="mb-3" controlId="formBasicName" onChange={this.handleUser.bind(this)}>
                                <Form.Control style={{marginLeft:"33%",paddingTop:"2%",width:"35%",height:"5%",borderRadius:"50px",backgroundColor:"transparent",color:"white"}} type="name" placeholder="Enter username(reddit)" />

                            </Form.Group>
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
