import logo from './logo.svg';
// import './App.css';
import React, { Component } from "react";
// import "bootstrap/dist/css/bootstrap.min.css";
import "./App.css";
import 'bootstrap/dist/css/bootstrap.min.css'
// import { MDBScrollbar, MDBSmoothScroll } from 'mdbreact';
import { useEffect, useState } from "react";
import {navigate} from "@reach/router"
import './scrollbar.css';
import './horizontalscrollbar.css';
import {
  Button,
  Alert,
  Breadcrumb,
  Card,
  Container,
  Row,
  Col,
  Navbar,
  Form,
  Nav,
  FormControl,
  NavDropdown,
  Carousel,
  Stack, Image, ButtonGroup, DropdownButton
} from "react-bootstrap";
import Chart from 'chart.js/auto'
import { MDBContainer } from "mdbreact";
import { Line ,Doughnut} from "react-chartjs-2";

import {MDBCol, MDBFormInline, MDBIcon, MDBInput} from "mdbreact";


const data = {
  labels: ["Sunday", "Monday", "Tuesday",
    "Wednesday", "Thursday", "Friday", "Saturday"],
  datasets: [
    {
      label: "Hours",
      data: [2, 5, 7, 9, 7, 6, 4],
      fill: true,
      backgroundColor: "rgba(6, 156,51, .3)",
      borderColor: "#02b844",
    }
  ]
}


let img='./icons.png';
function getcard(string,color,imag){

  return(<Button   size="lg" style={{ width: '17rem' , height: '7rem',backgroundColor:color,
        border:"transparent",borderRadius:'1rem',paddingRight:'6rem',paddingLeft:'2rem', fonstSize:'3rem',
        color:"black", fontSize:"20px",fontWeight:"bold"}}  >

        <Stack direction={"horizontal"} gap={4} style={{marginRight:'3rem'}}>

          <img src={imag} height={60} width={55}  />

          {string}

        </Stack>
      </Button>
  );
}
function navigateTo(){
  // navigate("/", {replace: true})
  // window.location.reload(false);
}
// let x;
function getNotificationCards(x){
  console.log(x)
  return(<Button style={{ width: '40rem' , height: '3rem',backgroundColor:'#1c2b45',borderRadius:'3rem'}} value={x} >{x}</Button>
  );
}


const UserDashboard =()=> {
  const [DoughnutData, setDoughnutData] = useState({})
  const [NotificationsData, setNotificationsData] = useState([])
  const doughnut = () =>{
    let topMalwares = [];
    let topMalwaresData = [];
    
    fetch("http://127.0.0.1:8082/dataAnalysis/getTopMalwares/3")
    .then(res => res.json())
    .then( data => {
      console.log(data)
     
      topMalwares = Object.keys(data)
      topMalwaresData = Object.values(data)
      console.log(topMalwares)
      console.log(topMalwaresData)
    setDoughnutData({
      labels: topMalwares,
      datasets: [{
        label: 'statistics',
        data: topMalwaresData,
        backgroundColor: [
          'rgb(255, 99, 132)',
          'rgb(54, 162, 235)',
          'rgb(255, 205, 86)'
        ],
        hoverOffset: 4
      }]
    })
  }
    )};

    const notifications = () =>{
      let alerts = [];
      fetch("http://127.0.0.1:8082/dataAnalysis/getNotifications/5")
      .then(res => res.json())
      .then( data => {
        console.log(data)
        alerts = Object.values(data)
        
        console.log(alerts)
      setNotificationsData(alerts)
      
      // console.log(NotificationsData.notifications)
    }
    )};
    useEffect(()=>{
      doughnut()
      notifications()
      return () => {
        console.log("returning")
      };
    }, []);
  return (

      <Container fluid  style={{position:"absolute",width:'100%',height:'100%', backgroundColor:'#b2c1d3'}}>
        <Stack direction="horizontal" gap={0} style={{position:"absolute",padding:0,margin:0}}>
          {/*drawer here*/}
          <Card style={{position:"absolute", width: '18%' , height: '100%', borderRadius:'1rem',padding:"0rem",paddingRight:"1rem",margin:0,backgroundColor:'#162237'}} >

            <Container style={{padding:"10%"}}>
              {/*<Row style={{margin:"10%"}}> </Row>*/}
              <Row>
                <Col xs={6} md={4}>
                  <Image src={"display.jpg"} rounded height={140} width={150} roundedCircle />
                </Col>
              </Row>
              <Row style={{margin:30}}>Name </Row>
              <Row style={{backgroundColor:"transparent"}}>

                <Form className="d-flex" >
                  <FormControl
                      style={{borderRadius:20,backgroundColor:"transparent",width:"7.5rem",height:"2rem",marginLeft:"0rem"}}
                      type="search"
                      placeholder="Search--"
                      className="me-2"
                      aria-label="Search"
                  />
                  <Button variant="outline-success" style={{ marginLeft:0 ,borderRadius:"50px",width:"1.5rem",height:"1.5rem",paddingRight:"0px",backgroundColor:"transparent",margin:0,borderColor:"transparent"}}>   <Image src={"./search.png"} height={20} width={25}  rounded  /></Button>
                </Form>


              </Row>
              <Row style={{margin:20}}> </Row>
              <Row>
                <ButtonGroup vertical>
                  <Button href="#" style={{backgroundColor:'#162237',borderRight:"transparent",borderLeft:"transparent",borderTop:"transparent"}}>Latest Reports</Button>
                  <Button href="#" style={{backgroundColor: '#162237',borderRight:"transparent",borderLeft:"transparent"}}>Manage Alerts</Button>
                  <Button href="#" style={{backgroundColor: '#162237',borderRight:"transparent",borderLeft:"transparent"}}>Manage Assets</Button>
                  <Button href="/"style={{backgroundColor: '#162237',borderRight:"transparent",borderLeft:"transparent",borderBottom:"transparent"}} onClick={navigateTo()} >Log Out</Button>






                </ButtonGroup>
              </Row>
            </Container>


          </Card>
          {/*2nd half of page(after drawer)*/}
          <Stack gap={0} style={{marginLeft:"20%",paddingRight:10,paddingLeft:15}}>
            {/*1st row starts here*/}
            <Stack direction="horizontal" gap={3} style={{paddingRight:0,paddingLeft:0,paddingTop:'0rem',paddingBottom:1 , marginTop:'0rem',marginBottom:0}}>
              <Card  className="scrollbar scrollbar-morpheus-den" style={{paddingTop:'1rem'}}>
                {/*<Card.Body>*/}
                {/*  <Card.Title>Card Title</Card.Title>*/}
                <Card.Subtitle className="mb-2 text-muted">Notifications</Card.Subtitle>
                <Stack direction={"vertical"} gap={3} style={{paddingRight:10,paddingLeft:30}}>
                  {
                    NotificationsData.map( notif => {
                      return getNotificationCards(notif);
                    }) 
                  }
                  {/* {getNotificationCard('notification1')}
                  {getNotificationCard('notification2')}
                  {getNotificationCard('notification3')}
                  {getNotificationCard('notification4')}
                  {getNotificationCard('notification5')}
                  {getNotificationCard('notification6')} */}


                </Stack>
                {/*</Card.Body>*/}
              </Card>
              <Card style={{ width: '25rem' , height: '14rem',paddingLeft:'90px',marginTop:'0rem'}}>
                {/*donughnut graph*/}
                <Card.Body style={{ width: '15rem' , height: '9rem',paddingTop:'0px',paddingRight:'15px',paddingLeft:'15px',marginTop:'0rem'}}>
                  <Doughnut data={DoughnutData}  />

                </Card.Body>
              </Card>{/* first row*/}
            </Stack>
            {/* Carousel starts here*/}

            <Card  className="horizontalscrollbar horizontalscrollbar-morpheus-den"  style={{ padding:'0px',marginTop:'0rem'}}>

              <Stack direction={"horizontal"} gap={3}>

                {getcard('Actor Activity','#def1ff','actor activity.png')}
                {getcard('Visualizer','#ffe6e1','./icons.png')}
                {getcard('Active Vulnerabilities','#fef7db',"./Active vulnerabilities.png")}
                {getcard('Reports','#f0dcff',"./reports.png")}
                {getcard('Forum','#def1ff')}
                {getcard('Actor Activity','#ffe6e1')}

              </Stack>

            </Card>


            {/*third row here*/}
            <Stack direction="horizontal" gap={3} style={{paddingRight:0,paddingLeft:0,paddingTop:10}}>

              <Card style={{ width: '25rem' , height: '14rem'}}>
                {/*line graph starts here*/}
                <Card.Body>


                  <Line data={data} />

                </Card.Body>
              </Card>
              <Card style={{ width: '45rem' , height: '14rem'}}>
                <Card.Body>
                  <Card.Title>Card Title</Card.Title>
                  <Card.Subtitle className="mb-2 text-muted">Card x</Card.Subtitle>
                  <Card.Text >
                    Some quick example text to build on the card title and make up the bulk of
                    the card's content.
                  </Card.Text>

                </Card.Body>
              </Card>
              {/*last row*/}
            </Stack>

            {/*<>*/}
            {/*    <style type="text/css">*/}
            {/*        {`*/}
            {/*              .btn-flat {*/}
            {/*                background-color: purple;*/}
            {/*                color: white;*/}
            {/*              }*/}
            {/*          */}
            {/*              .btn-xxl {*/}
            {/*                padding: 1rem 1.5rem;*/}
            {/*                font-size: 1.5rem;*/}
            {/*              }*/}
            {/*              `}*/}
            {/*    </style>*/}

            {/*    <Button variant="flat" size="xxl">*/}
            {/*        flat button*/}
            {/*    </Button>*/}
            {/*</>*/}

          </Stack>
        </Stack>

      </Container>

  );
}

export default UserDashboard;
