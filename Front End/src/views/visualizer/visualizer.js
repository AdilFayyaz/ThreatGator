import Graph from 'react-graph-vis'
import React, { useEffect, useState } from 'react'
import { TransformWrapper, TransformComponent } from 'react-zoom-pan-pinch'
import PropTypes from 'prop-types'
import ReactDOM from 'react-dom'
import * as url from 'url'
// import { Button } from 'react-bootstrap'
import SlidingPane from 'react-sliding-pane'
import 'react-sliding-pane/dist/react-sliding-pane.css'
import {
  CButton,
  CCard,
  CCardBody,
  CCardHeader,
  CDropdown,
  CDropdownItem,
  CDropdownMenu,
  CDropdownToggle,
  CImage,
} from '@coreui/react'
import { DocsLink } from '../../components'
//STIX relations
export class sro {
  constructor(type, id, relationship_type, source, target) {
    this.type = type
    this.id = id
    this.relationship_type = relationship_type
    this.source = source
    this.target = target
  }
}
//STIX Domain Object (sdo)
export class sdo {
  constructor(type, id, name) {
    this.type = type
    this.id = id
    this.name = name
  }
}
// customization of graph
const options = {
  layout: {
    hierarchical: {
      enabled: false,
      direction: 'UD',
      sortMethod: 'hubsize',
      shakeTowards: 'roots',
      levelSeparation: 150,
      nodeSpacing: 150,
      treeSpacing: 200,
    },
  },

  groups: {
    malware: {
      color: { border: '#ea8e8e', highlight: { border: '#B66565' } },
      image: { unselected: 'malware1.png', selected: 'malware2.png' },
      shape: 'circularImage',
    },
    indicator: {
      color: { border: '#8AA4C0', highlight: { border: '#54697E' } },
      image: { unselected: 'indicator1.png', selected: 'indicator2.png' },
      shape: 'circularImage',
    },
    attack: {
      color: { border: '#BF749B', highlight: { border: '#72475D' } },
      image: { unselected: 'attack1.png', selected: 'attack2.png' },
      shape: 'circularImage',
    },
    threat: {
      color: { border: '#FF6E4E', highlight: { border: '#C8593B' } },
      image: { unselected: 'threat1.png', selected: 'threat2.png' },
      shape: 'circularImage',
    },
    intrusion: {
      color: { border: '#7EC5AC', highlight: { border: '#50806F' } },
      image: { unselected: 'intrusion1.png', selected: 'intrusion2.png' },
      shape: 'circularImage',
    },
    email: {
      color: { border: '#37EE5F', highlight: { border: '#4EA962' } },
      image: { unselected: 'email1.png', selected: 'email2.png' },
      shape: 'circularImage',
    },
    campaign: {
      color: { border: '#F39E29', highlight: { border: '#C58022' } },
      image: { unselected: 'campaign1.png', selected: 'campaign2.png' },
      shape: 'circularImage',
    },
    infrastructure: {
      color: { border: '#E1EE8F', highlight: { border: '#B6BF7A' } },
      image: { unselected: 'infrastructure1.png', selected: 'infrastructure2.png' },
      shape: 'circularImage',
    },
    identity: {
      color: { border: '#6AC267', highlight: { border: '#4C8D4A' } },
      image: { unselected: 'identity1.png', selected: 'identity2.png' },
      shape: 'circularImage',
    },
    location: {
      color: { border: '#9A8BF3', highlight: { border: '#675DA6' } },
      image: { unselected: 'location1.png', selected: 'location2.png' },
      shape: 'circularImage',
    },
    tool: {
      color: { border: '#D2B6B2', highlight: { border: '#937D7A' } },
      image: { unselected: 'tool1.png', selected: 'tool2.png' },
      shape: 'circularImage',
    },
    vulnerability: {
      color: { border: '#85BFC5', highlight: { border: '#679499' } },
      image: { unselected: 'vulnerability1.png', selected: 'vulnerability2.png' },
      shape: 'circularImage',
    },
    sighting: {
      color: { border: '#FF8D07', highlight: { border: '#CF7103' } },
      image: { unselected: 'sighting1.png', selected: 'sighting2.png' },
      shape: 'circularImage',
    },
  },
  nodes: {
    fixed: {
      x: false,
      y: false,
    },
    shape: 'dot',
    size: 25,
    scaling: {
      type: 'incomingAndOutgoingConnections',
      min: 10,
      max: 60,
      label: {
        enabled: true,
        min: 20,
        max: 32,
      },
    },
  },
  interaction: {
    hover: true,
    multiselect: true,
    hoverConnectedEdges: false,
    zoomView: false,
    // keyboard: {
    //   enabled: true,
    //
    //   bindToWindow: false,
    // },
    navigationButtons: true,
    // tooltipDelay: 1000000,
    // hideEdgesOnDrag: true,
    //
    // zoomView: false,
  },
  physics: {
    solver: 'repulsion',
    repulsion: {
      nodeDistance: 600, // Put more distance between the nodes.
    },
    stabilization: true,
    forceAtlas2Based: {
      gravitationalConstant: -26,
      centralGravity: 0.005,
      springLength: 230,
      springConstant: 0.18,
      avoidOverlap: 1.5,
    },
  },
  edges: {
    color: '#000000',
  },
  height: '900px',
  width: '100%',
}

const Visualizer = (props) => {
  const [data, setData] = useState([])
  const [state, setState] = useState({
    isPaneOpen: false,
  })
  const [reportData, setReportData] = useState([])

  //data for graph
  let bundle = {}
  let mergedReports = {}
  let reports = {}
  bundle = props.graph1

  // reports = props.graph2
  async function getData() {
    // console.log('heree ' + props.graph2.merged)
    // setReportData(reports.merged)
    setReportData(props.graph2.merged)
    console.log(bundle)
    setData(bundle)
  }
  const dashboardNavigation = (event) => {
    console.log('here')
    event.preventDefault()
    // navigate("/dashboard", {replace: true})
    window.location.reload(false)
  }
  Visualizer.propTypes = {
    graph1: PropTypes.object,
    graph2: PropTypes.array,
    // mergedReports: PropTypes.object,
    //... other props you will use in this component
  }
  useEffect(async () => {
    // console.log(props)

    // mergedReports = props.mergedReports
    // console.log('bundle called' + JSON.stringify(bundle))
    // console.log('reports merged called' + JSON.stringify(reports.merged))
    await getData()
  }, [props.graph2])
  let relationships = []
  let objects = []
  let graphE = []
  let graphN = []
  let uniqueObjects = []
  // console.log(data.id)
  console.log(data)
  for (let i in data.objects) {
    if (data.objects[i]['type'] === 'relationship') {
      relationships.push(
        new sro(
          'relationship',
          data.objects[i]['relationship_type'],
          data.objects[i]['source_ref'],
          data.objects[i]['target_ref'],
        ),
      )
      graphE.push({
        from: data.objects[i]['source_ref'],
        to: data.objects[i]['target_ref'],
        label: data.objects[i]['relationship_type'],
      })
    } else {
      objects.push(new sdo(data.objects[i]['type'], data.objects[i]['id'], data.objects[i]['name']))
      let group = data.objects[i]['type']
      if (!uniqueObjects.includes(group)) {
        uniqueObjects.push(group)
      }
      if (group === 'attack-pattern') {
        group = 'attack'
      }
      if (group === 'threat-actor') {
        group = 'threat'
      }
      if (group === 'intrusion-set') {
        group = 'intrusion'
      }
      if (group === 'email-addr') {
        group = 'email'
      }
      graphN.push({ id: data.objects[i]['id'], label: data.objects[i]['name'], group: group })
    }
  }
  console.log(relationships)
  console.log(objects)
  console.log(graphN)

  let graph = {
    nodes: graphN,
    edges: graphE,
  }
  console.log(graph)

  // dropp down selection handler
  const handleSelect = (e) => {
    console.log('in handle select')
    console.log(e)
    setData(e)
  }
  return (
    <div>
      {/* eslint-disable-next-line react/prop-types */}
      {/*<div>Hi there {bundle.id}</div>*/}
      {/*<CCard className="mb-4">*/}
      {/*<CCardHeader>STIX Visualizer</CCardHeader>*/}
      {/*<CCardBody>*/}
      {/*<div>*/}
      {/*    nav back to dashboard*/}
      {/*<CButton*/}
      {/*  href="/dashboard"*/}
      {/*  style={{*/}
      {/*    backgroundColor: '#162237',*/}
      {/*    borderRight: 'transparent',*/}
      {/*    borderLeft: 'transparent',*/}
      {/*    borderBottom: 'transparent',*/}
      {/*  }}*/}
      {/*>*/}
      {/*  Return*/}
      {/*</CButton>*/}
      {/*</div>*/}

      <TransformWrapper
        initialScale={1}
        // initialPositionX={200}
        // initialPositionY={100}
      >
        {({ zoomIn, zoomOut, resetTransform, ...rest }) => (
          <React.Fragment>
            {/* tool bar*/}
            <div className="tools" style={{ backgroundColor: '#3C4B64', borderRadius: '5px' }}>
              <CButton
                onClick={() => zoomIn()}
                style={{
                  border: 'transparent',
                  backgroundColor: 'transparent',
                  width: '10%',
                  borderRight: 'black',
                }}
              >
                <CImage src="zoomIn.png" width="30%" height="15%"></CImage>
              </CButton>
              <CButton
                onClick={() => zoomOut()}
                style={{
                  border: 'transparent',
                  backgroundColor: 'transparent',
                  width: '10%',
                  borderRight: 'black',
                }}
              >
                <CImage src="zoomOut.png" style={{ width: '30%', height: '15%' }}></CImage>
              </CButton>
              <CButton
                style={{
                  border: 'transparent',
                  backgroundColor: 'transparent',
                  width: '10%',
                  height: '15%',
                  color: 'black',
                  borderLeft: 'black 1px solid',
                  borderRight: 'black 1px solid',
                  borderRadius: '1px',
                }}
                onClick={() => resetTransform()}
              >
                Reset
              </CButton>
              <CButton
                style={{
                  border: 'transparent',
                  backgroundColor: 'transparent',
                  width: '10%',
                }}
                onClick={() => setState({ isPaneOpen: true })}
              >
                View Legend
              </CButton>

              <CDropdown title="dropDown" id="dropDown">
                <CDropdownToggle href="#" color="secondary">
                  View graph
                </CDropdownToggle>
                {reportData ? (
                  <CDropdownMenu>
                    <CDropdownItem onClick={() => handleSelect(bundle)} key={bundle}>
                      Filtered Report
                    </CDropdownItem>

                    {Object.values(reportData).map((el, i) => (
                      <CDropdownItem onClick={() => handleSelect(el)} key={i}>
                        Report: {i + 1}
                      </CDropdownItem>
                    ))}
                    {/*<CDropdownItem href="#">Action</CDropdownItem>*/}
                    {/*<CDropdownItem href="#">Another action</CDropdownItem>*/}
                    {/*<CDropdownItem href="#">Something else here</CDropdownItem>*/}
                  </CDropdownMenu>
                ) : (
                  console.log('drop down error')
                )}
              </CDropdown>
            </div>
            <TransformComponent>
              <Graph
                // align="middle"
                graph={graph}
                options={options}
              />
            </TransformComponent>
          </React.Fragment>
        )}
      </TransformWrapper>

      <SlidingPane
        // className="glass"
        isOpen={state.isPaneOpen}
        title="Legend"
        from="right"
        width="250px"
        onRequestClose={() => setState({ isPaneOpen: false })}
      >
        <div style={{ paddingTop: '5rem' }}>
          {uniqueObjects.map((el) => (
            <div key={el}>
              {el} <img src={el + '1.png'} width="30px" height="30px" />
            </div>
          ))}
        </div>
      </SlidingPane>
      {/*</CCardBody>*/}
      {/*</CCard>*/}
    </div>
  )
}
export default Visualizer
