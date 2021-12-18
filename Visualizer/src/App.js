import Graph from "react-graph-vis";
import React, {useEffect, useState} from "react";
import ReactDOM from "react-dom";

class sro {
    constructor(type, id, relationship_type, source, target) {
        this.type=type;
        this.id=id;
        this.relationship_type=relationship_type;
        this.source=source;
        this.target=target;
    }
}
export class sdo{
    constructor(type, id, name) {
        this.type=type;
        this.id=id;
        this.name=name;
    }
}

const options = {
  layout: {
    hierarchical: false
  },
  edges: {
    color: "#000000"
  },
    height: "500px"
};

function App(){
    const [data,setData]=useState([]);
    const getData=()=>{
        fetch('bundle--97b40f76.json'
            ,{
                headers : {
                    'Content-Type': 'application/json',
                    'Accept': 'application/json'
                }
            }
        )
            .then(function(response){
                console.log(response)
                return response.json();
            })
            .then(function(myJson) {
                console.log(myJson);
                setData(myJson)
            });
    }
    useEffect(()=>{
        getData()
    },[])
    let relationships=[]
    let objects=[]
    let graphE=[]
    let graphN=[]
    console.log(data["id"])
    console.log(data["objects"])
    for (let i in data.objects) {
        if (data.objects[i]["type"] === "relationship") {
            relationships.push(new sro("relationship", data.objects[i]["relationship_type"], data.objects[i]["source_ref"], data.objects[i]["target_ref"]))
            graphE.push({from: data.objects[i]["source_ref"], to: data.objects[i]["target_ref"],  label:data.objects[i]["relationship_type"]})
        } else {
            objects.push(new sdo(data.objects[i]["type"], data.objects[i]["id"], data.objects[i]["name"]))
            if (!graphN.includes({id: data.objects[i]["id"], label: data.objects[i]["type"], color: "#e04141"})) {
                graphN.push({id: data.objects[i]["id"], label: data.objects[i]["name"], color: "#359b77"})
            }
        }
    }
    console.log(relationships)
    console.log(objects)
    console.log(graphN)


    let graph={
        nodes:graphN,
        edges: graphE
    };
    console.log(graph)


  return (
      <div>
        <h1>React graph vis</h1>
        <Graph graph={graph} options={options} style={{ height: "640px" }} />
      </div>
  );

}
export default App;
