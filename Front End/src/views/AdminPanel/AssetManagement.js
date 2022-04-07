import PropTypes from 'prop-types'
import React, { useEffect, useState, createRef, useRef } from 'react'
import classNames from 'classnames'
import Visualizer from '/home/hurriya/Desktop/8semester/Fyp/20marchupdate/Front End/src/views/visualizer/visualizer.js'
import {
  CRow,
  CCol,
  CCard,
  CCardHeader,
  CCardBody,
  CTableHead,
  CTableRow,
  CTableHeaderCell,
  CTableBody,
  CTableDataCell,
  CAvatar,
  CProgress,
  CTable,
  CButton,
  CBadge,
  CFormInput,
} from '@coreui/react'
import { rgbToHex } from '@coreui/utils'
import { DocsLink } from 'src/components'
import CIcon from '@coreui/icons-react'
import '/home/hurriya/Desktop/8semester/Fyp/20marchupdate/Front End/src/scss/Report_ap.css'
import {
  cibCcAmex,
  cibCcApplePay,
  cibCcMastercard,
  cibCcPaypal,
  cibCcStripe,
  cibCcVisa,
  cifBr,
  cifEs,
  cifFr,
  cifIn,
  cifPl,
  cifUs,
  cilPeople,
} from '@coreui/icons'
import { useLocation } from 'react-router-dom'

const Report_ap = () => {
  const [reportsData, SetReportsData] = useState({})
  const [hash1, SetHash] = useState(0)
  const [isedit, SetIsedit] = useState(true)
  const [badge, SetBadge] = useState('hidden')
  // fetching data from data analysis service for reports
  const location = useLocation()
  const edit = useRef(null)
  const blah = useRef(null)

  const hash = location.state.hash
  function getStix() {
    console.log('getting stix')
    fetch('http://127.0.0.1:8082/dataAnalysis/getStixBundle/' + hash)
      .then((res) => res.json())
      .then((data) => {
        SetHash(data)
      })
    console.log(hash1.id)
  }
  function saveRow() {
    console.log('save')

    SetIsedit(true)
    SetBadge('hidden')
    updateElastic()
  }

  const editField = () => {
    console.log('edit')

    SetIsedit(false)
    SetBadge('visible')
  }
  const sourceChangedHandler = (event) => {
    event.preventDefault()
    location.state.source = event.target.value
  }
  const malwaresChangedHandler = (event) => {
    event.preventDefault()
    location.state.malwares = event.target.value
  }
  const vulnerabilitiesChangedHandler = (event) => {
    event.preventDefault()
    location.state.vulnerabilities = event.target.value
  }
  const locationChangedHandler = (event) => {
    event.preventDefault()
    location.state.locations = event.target.value
  }
  const threatActorsChangedHandler = (event) => {
    event.preventDefault()
    location.state.threatActors = event.target.value
  }
  const identitiesChangedHandler = (event) => {
    event.preventDefault()
    location.state.identities = event.target.value
  }
  const toolsChangedHandler = (event) => {
    event.preventDefault()
    location.state.tools = event.target.value
  }
  const infrastructureChangedHandler = (event) => {
    event.preventDefault()
    location.state.infrastructure = event.target.value
  }
  const campaignsChangedHandler = (event) => {
    event.preventDefault()
    location.state.campaigns = event.target.value
  }
  function updateElastic() {
    var req = {
      hash: location.state.hash,
      malwares: location.state.malwares,
      threatActors: location.state.threatActors,
      identities: location.state.identities,
      locations: location.state.locations,
      tools: location.state.tools,
      vulnerabilities: location.state.vulnerabilities,
      infrastructures: location.state.infrastructure,
      indicators: location.state.indicators,
      campaigns: location.state.campaigns,
    }
    const requestOptions = {
      method: 'POST',
      headers: { 'Content-Type': 'application/json' },
      body: req,
    }
    fetch('http://127.0.0.1:8082/dataAnalysis/updateElasticDocument', requestOptions)
      .then((res) => res.json())
      .then((data) => {
        console.log('sending...' + data)
      })
  }
  useEffect(() => {
    getStix()
    return () => {
      console.log('returning ')
    }
  }, [])

  return (
    <>
      <CCard className="mb-4">
        <CCardHeader>
          Asset Management
          <CButton href="/latestReports_admin" style={{ float: 'right' }}>
            Latest Reports
          </CButton>
        </CCardHeader>
        <CCardBody id="card">
          {/*{<b>{location.state.rawText}</b>}*/}
          {<br></br>}

        </CCardBody>
      </CCard>
    </>
  )
}

export default Report_ap
