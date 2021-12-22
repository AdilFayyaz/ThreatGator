import logo from './logo.svg';
// import './App.css';
import React, { Component } from "react";
// import "bootstrap/dist/css/bootstrap.min.css";
import "./App.css";
import 'bootstrap/dist/css/bootstrap.min.css'
import {  Table } from "semantic-ui-react";
// npm install semantic-ui-react semantic-ui-css
// import { MDBScrollbar, MDBSmoothScroll } from 'mdbreact';
import {Modal} from "react-bootstrap";
import { useEffect, useState } from "react";
import {navigate} from "@reach/router"
import {Navigate} from 'react-router-dom';
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
import SearchResults from './searchResults';


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

// let x;
function getNotificationCards(x){
  return(<Button 
    style={{ width: '40rem' , height: '3rem',backgroundColor:'#1c2b45',borderRadius:'3rem'} } 
   value={x} >{x}</Button>
  );
}


const UserDashboard =()=> {
  const [DoughnutData, setDoughnutData] = useState({})
  const [WeekData, setWeekData] = useState({})
  const [NotificationsData, setNotificationsData] = useState([])
  const [SearchKeyword,SetSearchKeyword] = useState('')
  const [KeywordResult, SetKeywordResult] = useState({})
  const [HashNotif, SetHashNotif] = useState([])
  const [FieldsData, SetFieldsData] = useState({})
  const [vulnerabilitiesData, SetVulnerabilitiesData] = useState({})
  const [reportsData, SetReportsData] = useState({})
  const [exchangeData, SetExchangeData] = useState({})
  const [isOpen, SetIsOpen] = useState()
  const [isOpenSearch, SetIsOpenSearch] = useState()
  const [isOpenVuln, SetIsOpenVuln] = useState()
  const [isOpenReport, SetIsOpenReport] = useState()
  const [isOpenExchange, SetIsOpenExchange] = useState()

  const openModal = () => SetIsOpen(true);
  const closeModal = () => SetIsOpen(false);

  const openModalSearch = () => SetIsOpenSearch(true);
  const closeModalSearch = () => SetIsOpenSearch(false);

  const openModalVuln = () => SetIsOpenVuln(true);
  const closeModalVuln = () => SetIsOpenVuln(false);

  const openModalReport = () => SetIsOpenReport(true);
  const closeModalReport = () => SetIsOpenReport(false);

  const openModalExchange = () => SetIsOpenExchange(true);
  const closeModalExchange = () => SetIsOpenExchange(false);


  let arr = []
  const doughnut = () =>{
    let topMalwares = [];
    let topMalwaresData = [];
    
    fetch("http://127.0.0.1:8082/dataAnalysis/getTopMalwares/3")
    .then(res => res.json())
    .then( data => {
      topMalwares = Object.keys(data)
      topMalwaresData = Object.values(data)
      
    setDoughnutData({
      labels: topMalwares,
      datasets: [{
        label: 'statistics',
        data: topMalwaresData,
        backgroundColor: [
          'rgb(255, 99, 132)',
          'rgb(54, 162, 235)',
          'rgb(255, 205, 86)',
          'rgb(34,139,34)'
        ],
        hoverOffset: 4
      }]
    })
  }
    )};

    const hitsGraph = () =>{
      let hits = [];
      
      fetch("http://127.0.0.1:8082/dataAnalysis/getWeekHits")
      .then(res => res.json())
      .then( data => {
        hits = (data)
      setWeekData({
        labels: ["Monday", "Tuesday",
          "Wednesday", "Thursday", "Friday", "Saturday", "Sunday"],
        datasets: [
          {
            label: "Number of Hits",
            data: hits,
            fill: true,
            backgroundColor: "rgba(6, 156,51, .3)",
            borderColor: "#02b844",
          }
        ]
      })
    }
      )};

  const notifications = () =>{
      let alerts = [];
      let keys = [];
      fetch("http://127.0.0.1:8082/dataAnalysis/getNotifications/5")
      .then(res => res.json())
      .then( data => {
        alerts = Object.values(data)
        keys = Object.keys(data)
      SetHashNotif(keys)
      setNotificationsData(alerts)
      
      // SetHashNotif(Object.keys(data))
    }
    )};
  const getVulnerabilities = event =>{
    event.preventDefault()
    fetch("http://127.0.0.1:8082/dataAnalysis/getVulnerabilities")
          .then(res => res.json())
          .then( data => {
            SetVulnerabilitiesData(data)
            openModalVuln()
          })
  }
  const getReports = event =>{
    event.preventDefault()
    fetch("http://127.0.0.1:8082/dataAnalysis/getReports")
          .then(res => res.json())
          .then( data => {
            SetReportsData(data)
            openModalReport()
          })
  }

  const getTEPReports = event =>{
    event.preventDefault()
    fetch("http://127.0.0.1:8082/dataAnalysis/getThreatExchangeData")
          .then(res => res.json())
          .then( data => {
            console.log(data)
            SetExchangeData(data)
            // console.log(Object.keys(data))
            openModalExchange()
          })
  }

  const getSearchResults = event =>{
    event.preventDefault()
    let reports = [];
          const requestOptions = {
            method: 'POST',
            headers: { 'Content-Type': 'application/json' },
            body:SearchKeyword
        };
          fetch("http://127.0.0.1:8082/dataAnalysis/getSearchResults", requestOptions)
          .then(res => res.json())
          .then( data => {
            SetKeywordResult(data)
            openModalSearch()
          })
    // navigate(`/searchResults/${SearchKeyword}`, {replace: false}, { state: { data: SearchKeyword } })
  };

  const handleSearchChange = event =>{
    event.preventDefault();
    SetSearchKeyword(event.target.value);
    console.log(SearchKeyword);
  };
  const handleNotifClick = event =>{
    event.preventDefault(); 
    // console.log(event.target.value)
    var count = 0 ;
    var preHash;
    NotificationsData.forEach((m) => {
      count += 1;
      if (m==event.target.value){
        preHash =count;
      }
    })
    var count2= 0;
    var _hash="";
    HashNotif.forEach((x)=>{
      count2+=1;
      if(preHash == count2){
        _hash = x;
      }
    })
    // Now that we have the hash
    // we can call the fetch req to get all values
    let alerts2 = []
    let keys2 =[]
    const requestOptions = {
      method: 'POST',
      headers: { 'Content-Type': 'application/json' },
      body:_hash
    };
      fetch("http://127.0.0.1:8082/dataAnalysis/getResultOnHash",requestOptions)
      .then(res => res.json())
      .then( data => {
        SetFieldsData(data)
        openModal();
    })
  };
  const openVisualizer = event => {
    navigate("/visualizer", {replace: true})
    window.location.reload(false);
  }

    useEffect(()=>{
      doughnut()
      notifications()
      hitsGraph()
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

                <Form className="d-flex" onSubmit={getSearchResults}>
                  <FormControl
                      style={{borderRadius:20,width:"8.5rem",height:"2rem",marginLeft:"0rem"}}
                      type="search"
                      placeholder="Keyword "
                      className="me-2"
                      aria-label="Search"
                      onChange={handleSearchChange}
                  />
                  <Button variant="outline-success" style={{ marginLeft:0 ,borderRadius:"50px",width:"1.5rem",height:"1.5rem",paddingRight:"0px",backgroundColor:"transparent",
                  margin:0,borderColor:"transparent"}}>   <Image src={"./search.png"} height={25} width={25} roundedCircle  /></Button>
                </Form>


              </Row>
              <Row style={{margin:20}}> </Row>
              <Row>
                <ButtonGroup vertical>
                  <Button href="#" style={{backgroundColor:'#162237',borderRight:"transparent",borderLeft:"transparent",borderTop:"transparent"}}
                    onClick={getReports}>Latest Reports
                  </Button>
                  <Button href="#" style={{backgroundColor: '#162237',borderRight:"transparent",borderLeft:"transparent"}}
                    onClick={getTEPReports}>TEP Reports</Button>
                  <Button href="/demo" style={{backgroundColor: '#162237',borderRight:"transparent",borderLeft:"transparent"}}>Demo</Button>
                  <Button href="#" style={{backgroundColor: '#162237',borderRight:"transparent",borderLeft:"transparent"}}>Manage Assets</Button>
                  <Button href="/"style={{backgroundColor: '#162237',borderRight:"transparent",borderLeft:"transparent",borderBottom:"transparent"}} >Log Out</Button>
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
                    NotificationsData.map( (notif) => {
                      
                      return <Button onClick={handleNotifClick}
                          style={{ width: '40rem' , height: '3rem',backgroundColor:'#1c2b45',borderRadius:'3rem'} } 
                          value={notif}
                  
                        >{notif}
                      </Button>
                      // return getNotificationCards(notif).bind(this);
                    }) 
                  }
                 
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
                {
                  <div onClick={openVisualizer}>
                    {getcard('Visualizer','#ffe6e1','./icons.png')}
                  </div>
                }
                { 
                  <div onClick={getVulnerabilities}>
                    {getcard('Active Vulnerabilities','#fef7db',"./Active vulnerabilities.png")}
                  </div>
                }
                {
                  <div onClick={getReports}>
                    {getcard('Reports','#f0dcff',"./reports.png")}
                  </div>
                }
                {getcard('Forum','#def1ff')}
                {getcard('Actor Activity','#ffe6e1')}

              </Stack>

            </Card>


            {/*third row here*/}
            <Stack direction="horizontal" gap={3} style={{paddingRight:0,paddingLeft:0,paddingTop:10}}>

              <Card style={{ width: '25rem' , height: '14rem'}}>
                {/*line graph starts here*/}
                <Card.Body>


                  <Line data={WeekData} />

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
          </Stack>
        </Stack>


        {/* Top Notifications */}
        <Modal show={isOpen} onHide={closeModal}
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
               <div>
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
              
               
              <Table.Row >
                <Table.Cell>{FieldsData.source}</Table.Cell>
                <Table.Cell>{FieldsData.rawText}</Table.Cell>
                <Table.Cell>{FieldsData.malwares}</Table.Cell>
                <Table.Cell>{FieldsData.vulnerabilities}</Table.Cell>
                <Table.Cell>{FieldsData.locations}</Table.Cell>
                <Table.Cell>{FieldsData.threatActors}</Table.Cell>
                <Table.Cell>{FieldsData.identities}</Table.Cell>
                <Table.Cell>{FieldsData.tools}</Table.Cell>
                <Table.Cell>{FieldsData.infrastructure}</Table.Cell>
                <Table.Cell>{FieldsData.campaigns}</Table.Cell>
              </Table.Row>
              
              
            </Table.Body>
          </Table>
          </div>
              </Modal.Body>
              <Modal.Footer>
                <Button onClick={closeModal}>Close</Button>
              </Modal.Footer>
            </Modal>

        {/* Search Results */}
        <Modal show={isOpenSearch} onHide={closeModalSearch}
          fullscreen={true}
          aria-labelledby="contained-modal-title-vcenter"
          centered
        >
          <Modal.Header>
            <Modal.Title id="contained-modal-title-vcenter">
              Search Results
            </Modal.Title>
          </Modal.Header>
          <Modal.Body>
          <Table singleLine>
            <Table.Header>
              <Table.Row>
                <Table.HeaderCell>Searches</Table.HeaderCell>
              </Table.Row>
            </Table.Header>

              <Table.Body>
              {Object.keys(KeywordResult).map((item,i) => {
              
                return (
                  <Table.Row>
                    <Table.Cell>{KeywordResult[item]}</Table.Cell>
                  </Table.Row>
                );
              })}
            </Table.Body>
          </Table>
          </Modal.Body>
          <Modal.Footer>
            <Button onClick={closeModalSearch}>Close</Button>
          </Modal.Footer>
        </Modal>


        {/* Vulnerabilities */}
        <Modal show={isOpenVuln} onHide={closeModalVuln}
          size="lg"
          aria-labelledby="contained-modal-title-vcenter"
          centered
        >
          <Modal.Header>
            <Modal.Title id="contained-modal-title-vcenter">
              Vulnerabilities Result
            </Modal.Title>
          </Modal.Header>
          <Modal.Body>
          <Table singleLine>
            <Table.Header>
              <Table.Row>
                <Table.HeaderCell>Vulnerabilities</Table.HeaderCell>
              </Table.Row>
            </Table.Header>

              <Table.Body>
              {Object.keys(vulnerabilitiesData).map((item,i) => {
              
                return (
                  <Table.Row>
                    <Table.Cell>{item}</Table.Cell>
                  </Table.Row>
                );
              })}
            </Table.Body>
          </Table>

          </Modal.Body>
          <Modal.Footer>
            <Button onClick={closeModalVuln}>Close</Button>
          </Modal.Footer>
        </Modal>

        {/* Reports */}
        <Modal show={isOpenReport} onHide={closeModalReport}
          fullscreen={true}
          
          aria-labelledby="contained-modal-title-vcenter"
          centered
        >
          <Modal.Header>
            <Modal.Title id="contained-modal-title-vcenter">
              Reports
            </Modal.Title>
          </Modal.Header>
          <Modal.Body>
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
              {Object.values(reportsData).map(el => {
                
                return (
                  <Table.Row>
                    <Table.Cell>{el.source}</Table.Cell>
                    <Table.Cell>{el.rawText}</Table.Cell>
                    <Table.Cell>{el.malwares}</Table.Cell>
                    <Table.Cell>{el.vulnerabilities}</Table.Cell>
                    <Table.Cell>{el.locations}</Table.Cell>
                    <Table.Cell>{el.threatActors}</Table.Cell>
                    <Table.Cell>{el.identities}</Table.Cell>
                    <Table.Cell>{el.tools}</Table.Cell>
                    <Table.Cell>{el.infrastructure}</Table.Cell>
                    <Table.Cell>{el.campaigns}</Table.Cell>
                  </Table.Row>
                );
              })}
            </Table.Body>
          </Table>
          </Modal.Body>
          <Modal.Footer>
            <Button onClick={closeModalReport}>Close</Button>
          </Modal.Footer>
        </Modal>

        {/* Exchange Reports */}
        <Modal show={isOpenExchange} onHide={closeModalExchange}
          fullscreen={true}
          
          aria-labelledby="contained-modal-title-vcenter"
          centered
        >
          <Modal.Header>
            <Modal.Title id="contained-modal-title-vcenter">
              Reports
            </Modal.Title>
          </Modal.Header>
          <Modal.Body>
          <Table singleLine>
            <Table.Header>
              <Table.Row>
                <Table.HeaderCell>ID</Table.HeaderCell>
                <Table.HeaderCell>Title</Table.HeaderCell>
                <Table.HeaderCell>Type</Table.HeaderCell>
                <Table.HeaderCell>Threat Score</Table.HeaderCell>
                <Table.HeaderCell>Confidence</Table.HeaderCell>
                <Table.HeaderCell>Risk Factor</Table.HeaderCell>
                <Table.HeaderCell>Data Sources</Table.HeaderCell>
                <Table.HeaderCell>iPv4 Details</Table.HeaderCell>
              
                
              </Table.Row>
            </Table.Header>

              <Table.Body>
              {Object.values(exchangeData).map(el => {
                
                return (
                  <Table.Row>
                    <Table.Cell>{el.id}</Table.Cell>
                    <Table.Cell>{el.title}</Table.Cell>
                    <Table.Cell>{el.type}</Table.Cell>
                    <Table.Cell>{el.threatScore}</Table.Cell>
                    <Table.Cell>{el.confidence}</Table.Cell>
                    <Table.Cell>{el.riskFactor}</Table.Cell>
                    <Table.Cell>{el.dataSources}</Table.Cell>
                    <Table.Cell>{el.iPv4Details}</Table.Cell>
                    
                  </Table.Row>
                );
              })}
            </Table.Body>
          </Table>
          </Modal.Body>
          <Modal.Footer>
            <Button onClick={closeModalExchange}>Close</Button>
          </Modal.Footer>
        </Modal>

      </Container>

  );
}

export default UserDashboard;

