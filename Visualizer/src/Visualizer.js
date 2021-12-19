// import Graph from "react-graph-vis";
// import React, {Component, useEffect, useState} from "react";
// import ReactDOM from "react-dom";
// export class sro {
//     constructor(type, id, relationship_type, source, target) {
//         this.type=type;
//         this.id=id;
//         this.relationship_type=relationship_type;
//         this.source=source;
//         this.target=target;
//     }
// }
// export class sdo{
//     constructor(type, id, name) {
//         this.type=type;
//         this.id=id;
//         this.name=name;
//     }
// }
// class Visualizer extends Component {
//     setData(obj){
//         this.setState({data:obj})
//     }
//     constructor() {
//         super();
//         this.state = {data: null, options:null, graph:null}
//         fetch('bundle--97b40f76.json'
//             ,{
//                 headers : {
//                     'Content-Type': 'application/json',
//                     'Accept': 'application/json'
//                 }
//             }
//         )
//             .then(function(response){
//                 console.log(response)
//                 return response.json();
//             })
//             .then(function(myJson) {
//                 console.log(myJson);
//                 setData(myJson)
//             });
//
//
//     }
// }