import Graph from "react-graph-vis";
import React, {useEffect, useState} from "react";
import ReactDOM from "react-dom";
import * as url from "url";
import {Button} from "react-bootstrap";
import SlidingPane from "react-sliding-pane";
import "react-sliding-pane/dist/react-sliding-pane.css";
//STIX relations
export class sro {
    constructor(type, id, relationship_type, source, target) {
        this.type=type;
        this.id=id;
        this.relationship_type=relationship_type;
        this.source=source;
        this.target=target;
    }
}
//STIX Domain Object (sdo)
export class sdo{
    constructor(type, id, name) {
        this.type=type;
        this.id=id;
        this.name=name;
    }
}
// customization of graph
const options = {
    layout: {
        hierarchical: {
            enabled: false,
            direction: "UD",
            sortMethod: "hubsize",
            shakeTowards: "roots",
            levelSeparation: 150,
            nodeSpacing: 150,
            treeSpacing: 200
        }
    },
    groups: {
        malware: {
            color: {border: "#ea8e8e", highlight:{border:"#B66565"}},
            image:{unselected:'malware1.png', selected:'malware2.png'},
            shape:'circularImage'
        },
        indicator:{
            color: {border: "#8AA4C0", highlight:{border:"#54697E"}},
            image:{unselected:'indicator1.png', selected:'indicator2.png'},
            shape:'circularImage'
        },
        attack:{
            color: {border: "#BF749B", highlight:{border:"#72475D"}},
            image:{unselected:'attack1.png', selected:'attack2.png'},
            shape:'circularImage'
        },
        threat:{
            color: {border: "#FF6E4E", highlight:{border:"#C8593B"}},
            image:{unselected:'threat1.png', selected:'threat2.png'},
            shape:'circularImage'
        },
        intrusion:{
            color: {border: "#7EC5AC", highlight:{border:"#50806F"}},
            image:{unselected:'intrusion1.png', selected:'intrusion2.png'},
            shape:'circularImage'
        },
        email:{
            color: {border: "#37EE5F", highlight:{border:"#4EA962"}},
            image:{unselected:'email1.png', selected:'email2.png'},
            shape:'circularImage'
        },
        campaign:{
            color: {border: "#F39E29", highlight:{border:"#C58022"}},
            image:{unselected:'campaign1.png', selected:'campaign2.png'},
            shape:'circularImage'
        },
        infrastructure:{
            color: {border: "#E1EE8F", highlight:{border:"#B6BF7A"}},
            image:{unselected:'infrastructure1.png', selected:'infrastructure2.png'},
            shape:'circularImage'
        },
        identity:{
            color: {border: "#6AC267", highlight:{border:"#4C8D4A"}},
            image:{unselected:'identity1.png', selected:'identity2.png'},
            shape:'circularImage'
        },
        location:{
            color: {border: "#9A8BF3", highlight:{border:"#675DA6"}},
            image:{unselected:'location1.png', selected:'location2.png'},
            shape:'circularImage'
        },
        tool:{
            color: {border: "#D2B6B2", highlight:{border:"#937D7A"}},
            image:{unselected:'tool1.png', selected:'tool2.png'},
            shape:'circularImage'
        },
        vulnerability:{
            color: {border: "#85BFC5", highlight:{border:"#679499"}},
            image:{unselected:'vulnerability1.png', selected:'vulnerability2.png'},
            shape:'circularImage'
        },
        sighting:{
            color: {border: "#FF8D07", highlight:{border:"#CF7103"}},
            image:{unselected:'sighting1.png', selected:'sighting2.png'},
            shape:'circularImage'
        }



    },
    nodes: {
        fixed: {
            x: false,
            y: false
        },
        shape: "dot",
        size: 25,
        scaling: {
            type: "incomingAndOutgoingConnections",
            min: 10,
            max: 60,
            label: {
                enabled: true,
                min: 20,
                max: 32
            }
        },
    },
    interaction: {
        hover: true,
        multiselect: true,
        hoverConnectedEdges: false
    },
    physics: {
        solver:"repulsion",
        repulsion: {
            nodeDistance: 600 // Put more distance between the nodes.
        },
        forceAtlas2Based: {
            gravitationalConstant: -26,
            centralGravity: 0.005,
            springLength: 230,
            springConstant: 0.18,
            avoidOverlap: 1.5
        },

    },
  edges: {
    color: "#000000"
  },
    height: "700px"
};

const Visualizer=() =>{
    const [data,setData]=useState([]);
    const [state, setState] = useState({
        isPaneOpen: false,
    });

    //data for graph
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
    const dashboardNavigation = event =>{
        console.log("here")
        event.preventDefault();
        // navigate("/dashboard", {replace: true})
        window.location.reload(false)
    }
    useEffect(()=>{
        getData()
    },[])
    let relationships=[]
    let objects=[]
    let graphE=[]
    let graphN=[]
    let uniqueObjects=[]
    console.log(data["id"])
    console.log(data["objects"])
    for (let i in data.objects) {
        if (data.objects[i]["type"] === "relationship") {
            relationships.push(new sro("relationship", data.objects[i]["relationship_type"], data.objects[i]["source_ref"], data.objects[i]["target_ref"]))
            graphE.push({from: data.objects[i]["source_ref"], to: data.objects[i]["target_ref"],  label:data.objects[i]["relationship_type"]})
        } else {
            objects.push(new sdo(data.objects[i]["type"], data.objects[i]["id"], data.objects[i]["name"]))
            let group=data.objects[i]["type"]
            if (!uniqueObjects.includes(group)){
                uniqueObjects.push(group)
            }
            if (group==="attack-pattern"){
                group="attack"
            }
            if (group==="threat-actor"){
                group="threat"
            }
            if (group==="intrusion-set"){
                group="intrusion"
            }
            if (group==="email-addr"){
                group="email"
            }
            graphN.push({id: data.objects[i]["id"], label: data.objects[i]["name"], group:group})


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
        <div>
            <h1>STIX Visualizer</h1>
            <div>
            {/*    nav back to dashboard*/}
            <Button href="/dashboard"style={{backgroundColor: '#162237',borderRight:"transparent",borderLeft:"transparent",borderBottom:"transparent"}} >Return</Button>
            </div>
            <button onClick={() => setState({ isPaneOpen: true })}>
                View Legend
            </button>
            {/*legend for graph*/}
            <SlidingPane
                isOpen={state.isPaneOpen}
                title="Legend"
                from="right"
                width="250px"
                onRequestClose={() => setState({ isPaneOpen: false })}
            >
                <div>{uniqueObjects.map(el=>{
                    return(
                        <p>{el} <img src={el+"1.png"} width="30px" height="30px" /></p>
                    )
                })}</div>
            </SlidingPane>
            <Graph graph={graph} options={options} style={{ height: "640px" }} />
        </div>
      </div>
  );

}
export default Visualizer;

