import './style.css';
import React from 'react'
import './App.css';
import Wave from 'react-wavify'
import {Card,Stack,Row,Button,Form,Image} from 'react-bootstrap'
import {navigate} from "@reach/router"//npm install @reach/router

import { useState, useEffect } from 'react'
import Dashboard from "./dashboard";

// export default function(props) {
//     const navigation = useNavigate();
//
//     return <div{...props} navigation={navigation} />;
// }
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
    async navigateTo(){
        navigate("/dashboard", {replace: true})
        window.location.reload(false);
    }
    async navigateToAdmin(){
        navigate("/addSource", {replace: true})
        window.location.reload(false);
    }
    async authenticateUser(event){
       
        event.preventDefault();
        const requestOptions = {
            method: 'POST',
            headers: { 'Content-Type': 'application/json' },
            body: JSON.stringify({ username: this.state.username,
                password: this.state.password })
        };
        fetch('http://127.0.0.1:8084/users/validateCredentials', requestOptions)
            .then(response => response.json())
            .then((data) => {
                this.state.data = data;
               
                console.log(this.state.data);  
                if (this.state.data === true){
                    this.navigateTo()
                    // alert("Add Navigation here");
                    
                    // history.push("/dashboard")
                }
                else{
                    // alert('Enter the correct credentials')
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
    async handleEmailChange(event){
        this.state.username = event.target.value;
        // this.setState({username: event.target.value});
       
    }
    async handlePasswordChange(event){
        // this.setState({password: event.target.value});
        this.state.password = event.target.value;
    }





render(){

    return(
        <div className="App"style={{height:'100%',width:"100%"}}>
        {/*<header className="App-header">*/}
        {/*  <Stack direction="horizontal" gap={3} style={{paddingRight:0,paddingLeft:0,paddingTop:'0rem',paddingBottom:1 , marginTop:'0rem',marginBottom:0}}>*/}
            <Stack  direction="vertical" gap={0} style={{position:"absolute",backgroundColor:'#162237' ,margin:'0',height:'100%',width:"100%",paddingLeft:"33%",paddingTop:"4%",paddingBottom:"0%"}}>
            <Card style={{position:"absolute", width: '27%',height:'75%',borderRadius:'15px' ,background:"#353B47",opacity:1}}>
                <Row style={{position:"absolute",margin:"10%"}}></Row>
                <Row style={{paddingTop:"1%",marginLeft:"35%",marginRight:"35%"}}><img src={"./threatgator.png"}  /></Row>

                <div style={{backgroundColor:"transparent"}}>
                    <div style={{alignContent:"center",fontSize:"200%",fontWeight:"bold",color:"#ffffff"}}>SIGN IN</div>
                    <div style={{height:"5%"}}></div>
                    <Form style={{alignContent:"center"}} onSubmit={(e)=>{
                            // e.preventDefault();
                            this.authenticateUser(e);
                            // navigate("/dashboard", {replace: true})
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
        {/*</header>*/}
        </div>
    );
    }
}

export default signIn;
