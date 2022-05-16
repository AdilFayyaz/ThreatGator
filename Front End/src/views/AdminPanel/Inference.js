// import './style.css'
import React, { useEffect, useState } from 'react'
// import './App.css'
// import './table.css'

// import { Table } from 'semantic-ui-react'
import { Tab, Tabs, TabList, TabPanel } from 'react-tabs'
import { Card, Stack, Row, Button, Form, Image, Dropdown } from 'react-bootstrap'
import { Modal } from 'react-bootstrap'
import {
  CButton,
  CForm,
  CFormTextarea,
  CNav,
  CNavItem,
  CNavLink,
  CTable,
  CTableBody,
  CTableDataCell,
  CTableHead,
  CTableHeaderCell,
  CTableRow,
  CTabPane,
} from '@coreui/react'
import Visualizer from '../visualizer/visualizer'
var scraperApiURL = 'http://127.0.0.1:8000/'
//UI to show data being consumed from kafka
const ViewConsumedData = () => {
  const [inferencedSentence, SetinferencedSentence] = useState('')
  const [sentence, SetSentence] = useState('')

  //connection with data consumer service to get inference for a particular sentence
  const getInference = (event) => {
    event.preventDefault()

    var myHeaders = new Headers()
    myHeaders.append('Content-Type', 'application/json')

    var raw = JSON.stringify({
      sentence: sentence,
    })

    var requestOptions = {
      method: 'POST',
      headers: myHeaders,
      body: raw,
      redirect: 'follow',
    }

    var x = fetch('http://127.0.0.1:5000/getInference', requestOptions)
      .then((res) => res.json())
      .then((myJson) => {
        console.log('got results' + JSON.stringify(myJson))
        SetinferencedSentence(myJson)
        getbundle()
      })
      .catch((error) => console.log('error', error))
  }

  const handleSentence = (event) => {
    // event.preventDefault()
    SetSentence(event.target.value)
    // this.state.sentence=event.target.value
  }
  // social media scrapers
  const getbundle = () => {
    var myHeaders = new Headers()
    myHeaders.append('Content-Type', 'application/json')

    var raw = JSON.stringify({
      sentence: sentence,
    })

    var requestOptions = {
      method: 'POST',
      headers: myHeaders,
      body: raw,
      redirect: 'follow',
    }

    let x = fetch('http://127.0.0.1:5000/getLiveInference', requestOptions)
      .then((response) => response.json())
      .then((result) => {
        console.log('result', JSON.parse(JSON.stringify(result)))
        setBundle(JSON.parse(JSON.stringify(result)))
      })
      .catch((error) => console.log('eror', error))
  }
  const [bundle, setBundle] = useState({})
  useEffect(() => {
    return () => {
      // getbundle()
      console.log('returning ')
    }
  }, [])
  return (
    <>
      <div style={{ borderRadius: '3rem' }}>
        <CForm style={{ alignContent: 'center' }} onSubmit={getInference}>
          {/*<CForm className="mb-3" controlId="formBasicName" >*/}
          <CFormTextarea
            onChange={handleSentence}
            style={{
              // marginLeft: '33%',
              // paddingTop: '2%',
              // width: '90%',
              height: '100%',
              borderRadius: '50px',
              backgroundColor: 'transparent',
              color: 'black',
            }}
            type="name"
            placeholder="Sentence"
          />
          {/*</CForm>*/}
          <Row style={{ position: 'absolute' }} />

          <CButton style={{ marginTop: '2%', borderRadius: '10px' }} type="Submit">
            Inference
          </CButton>
        </CForm>

        {inferencedSentence ? (
          <CTable>
            <CTableBody>
              {Object.values(inferencedSentence['entity tags']).map((el, i) => (
                <CTableRow key={i}>
                  {Object.values(el).map((x) => (
                    <CTableDataCell key={x}>
                      <div>word: {x[1]}</div>
                      <div>tag: {x[2]}</div>
                    </CTableDataCell>
                  ))}
                </CTableRow>
              ))}
            </CTableBody>
          </CTable>
        ) : (
          <p></p>
        )}
        {console.log('here', JSON.stringify(bundle))}
        {bundle ? <Visualizer graph1={bundle} graph2={{}} /> : console.log('+-')}
      </div>
    </>
  )
}

export default ViewConsumedData
