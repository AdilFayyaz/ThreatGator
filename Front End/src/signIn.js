import './style.css';
import React from 'react'
import './App.css';
import Wave from 'react-wavify'
import {Card,Stack,Row,Button,Form,Image} from 'react-bootstrap'
import {navigate} from "@reach/router"//npm install @reach/router

import { useState, useEffect } from 'react'
import Dashboard from "./dashboard";

//co,ponent that renders the Sign in page
class signIn extends React.Component{
    constructor(props){
        super(props);
        this.state ={
            username:'',
            password:'',
            data: ''
        }
      
    }
    
    componentDidMount(){
        
        console.log("Component Mounted")
    }
    componentWillMount(){
        // this.setState({username: '',password:'', data: ''})
    }
    //a function that navigates to dashboard page if the credentials match with users's
    async navigateTo(){
        navigate("/dashboard", {replace: true})
        window.location.reload(false);
    }
    //a function that navigates to admin's add source page if the credentials match with admin's
    async navigateToAdmin(){
        navigate("/addSource", {replace: true})
        window.location.reload(false);
    }
    //function decides whether the credetials are correct w.e.t the database(both admin's and user's)
    //navigates accordingly
    async authenticateUser(event){
       
        event.preventDefault();
        const requestOptions = {
            method: 'POST',
            headers: { 'Content-Type': 'application/json' },
            body: JSON.stringify({ username: this.state.username,
                password: this.state.password })
        };
        //uses the user management system
        fetch('http://127.0.0.1:8084/users/validateCredentials', requestOptions)
            .then(response => response.json())
            .then((data) => {
                this.state.data = data;
               
                console.log(this.state.data);  
                if (this.state.data === true){
                    this.navigateTo()

                }
                else{
                    //Admin checking
                    fetch('http://127.0.0.1:8084/Admin/validateAdminCredentials', requestOptions)
                        .then(response => response.json())
                        .then((data) => {
                            this.state.data = data;

                            console.log(this.state.data);
                            if (this.state.data === true){
                                this.navigateToAdmin()
                                // alert("Add Navigation here");

                                // history.push("/dashboard")
                            }
                            else{
                                alert('Enter the correct credentials')
                            }
                        });
                }
            });

    }
    // handler functions for email and password
    async handleEmailChange(event){
        this.state.username = event.target.value;

    }
    async handlePasswordChange(event){
        // this.setState({password: event.target.value});
        this.state.password = event.target.value;
    }





render(){

    return(
        <div className="App"style={{height:'100%',width:"100%"}}>
       <Stack  direction="vertical" gap={0} style={{position:"absolute",backgroundColor:'#162237' ,margin:'0',height:'100%',width:"100%",paddingLeft:"33%",paddingTop:"4%",paddingBottom:"0%"}}>
            <Card style={{position:"absolute", width: '27%',height:'75%',borderRadius:'15px' ,background:"#353B47",opacity:1}}>
                <Row style={{position:"absolute",margin:"10%"}}></Row>
                <Row style={{paddingTop:"1%",marginLeft:"35%",marginRight:"35%"}}><img src={"./threatgator.png"}  /></Row>

                <div style={{backgroundColor:"transparent"}}>
                    <div style={{alignContent:"center",fontSize:"200%",fontWeight:"bold",color:"#ffffff"}}>SIGN IN</div>
                    <div style={{height:"5%"}}></div>
                    <Form style={{alignContent:"center"}} onSubmit={(e)=>{
                            //check  email and password before any nabvigation
                            this.authenticateUser(e);

                        }}>
                        <Form.Group className="mb-3" controlId="formBasicEmail" onChange={this.handleEmailChange.bind(this)}>
                            <Form.Control style={{marginLeft:"33%",paddingTop:"2%",width:"35%",height:"5%",borderRadius:"50px",backgroundColor:"transparent"}} type="email" placeholder="Username" />

                        </Form.Group>
                        <Row style={{position:"absolute",margin:"5%"}}></Row>
                        <Form.Group className="mb-3" controlId="formBasicPassword" onChange={this.handlePasswordChange.bind(this)}>
                            <Form.Control  style={{marginLeft:"33%",paddingTop:"2%",width:"35%",height:"5%",borderRadius:"50px",backgroundColor:"transparent"}} type="password" placeholder="Password" />
                        </Form.Group>

                        <button style={{borderRadius:"10px",backgroundColor:"transparent"}}  type="submit">
                            <Image src={"./arrow.png"} height={22} width={20}/>
                        </button>

                    </Form>

                </div>

            </Card>
                        {/*wave decor starts here*/}
                        <Wave  className="box" fill="url(#gradient)" style={{ position:"absolute", opacity: 0.3 ,backgroundColor:'transparent'}} options={{
                            height: 70,
                            amplitude: 30,
                            speed: 0.6,
                            points: 4,

                        }}>
                        <defs>
                            <linearGradient id="gradient" gradientTransform="rotate(90)">
                                <stop offset="10%"  stopColor="#1277b0" />
                                <stop offset="90%" stopColor="##62237" />
                            </linearGradient>
                        </defs>

                        </Wave>


                        <Wave  className="box" fill="url(#gradient)" style={{  opacity: 0.2,backgroundColor:'transparent' }} options={{height: 50,amplitude: 35,speed: 0.8,points: 5,}}>
                            <defs>
                                <linearGradient id="gradient" gradientTransform="rotate(90)">
                                    <stop offset="10%"  stopColor="#1277b0" />
                                    <stop offset="90%" stopColor="#162237" />
                                </linearGradient>
                            </defs>

                        </Wave>

            <Wave  className="box" fill="url(#gradient)" style={{  opacity: 0.2 ,backgroundColor:'transparent'}} options={{height: 60,amplitude: 45,speed: 0.5,points: 3,}}>
                <defs>
                    <linearGradient id="gradient" gradientTransform="rotate(90)">
                        <stop offset="10%"  stopColor="#1277b0" />
                        <stop offset="90%" stopColor="##162237" />
                    </linearGradient>
                </defs>

            </Wave>

            </Stack>
        <div>
            Returned values: {this.state.data}
        </div>

        </div>
    );
    }
}

export default signIn;
