import React, { Component } from "react";
import "./App.css";
import 'bootstrap/dist/css/bootstrap.min.css'
import { useEffect, useState } from "react";
import {navigate, useParams} from "@reach/router"
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


class SearchResults extends React.Component{
    constructor(props){
        super(props);
        let search = window.location.search;
        let params = new URLSearchParams(search);
        console.log(params)
        let id = params.get('searchKeyword');
        console.log(id)
        
    }
    // fetch search data against a particular keywords from data analysis service
    async getSearchResults(){
        
        /// SearchKeyword = props.SearchKeyword   
        console.log(this.state.SearchKeyword)
        console.log("Getting searches")
          let reports = [];
          const requestOptions = {
            method: 'POST',
            headers: { 'Content-Type': 'application/json' },
            body:this.state.SearchKeyword
        };
          fetch("http://127.0.0.1:8082/dataAnalysis/getSearchResults", requestOptions)
          .then(res => res.json())
          .then( data => {
            reports = Object.values(data)
            console.log(reports)
               
          })
      };
      getParams(){
          let params = useParams()
          console.log(params)
      }
      componentDidMount(){
        // this.getParams()
        
        
        //   this.getSearchResults();
      }
      
    render(){
        // let params = useParams();
        // const state  = this.props.location
        // console.log(state)
    return(
        <div>
            {/* {params} */}
        </div>
    )
    }
    
}
export default SearchResults;