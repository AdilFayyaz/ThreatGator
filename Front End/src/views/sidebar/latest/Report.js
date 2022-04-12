import React, { useEffect, useState, createRef } from 'react'
import Visualizer from '../../visualizer/visualizer'
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
} from '@coreui/react'
import { useLocation } from 'react-router-dom'

const Reports = (props) => {
  const [reportsData, SetReportsData] = useState({})
  const [hash1, SetHash] = useState(0)
  const [threatScore, SetThreatScore] = useState('')
  const [mergedReports, setMergedReports] = useState([])
  // fetching data from data analysis service for reports
  const location = useLocation()
  const hash = location.state.hash
  // const org_id=location.state.org_id
  const mergedReportsBundles = []
  console.log('hash' + hash)
  const data1 = { org_id: location.state.org_id, reportId: hash, index: 'tagged_bundle_data' }

  function getScore() {
    // return null
    // const data1 = { org_id: location.state.org_id, reportId: hash, index: 'tagged_bundle_data' }

    var myHeaders = new Headers()
    myHeaders.append('Content-Type', 'text/plain')

    // var raw = '{   "org_id":2\n    "report_id":-1138549715\n    "index":"tagged_data_bundle"}'

    var requestOptions = {
      method: 'GET',
      headers: myHeaders,
      // body: data1,
      redirect: 'follow',
    }

    fetch(
      'http://127.0.0.1:8086/threatScore/getThreatScoreByOrganizationReport?org_id=' +
        encodeURIComponent(data1.org_id) +
        '&report_id=' +
        encodeURIComponent(data1.reportId) +
        '&index=' +
        encodeURIComponent(data1.index),
      requestOptions,
    )
      .then((response) => response.text())
      .then((result) => SetThreatScore(result))
      .catch((error) => console.log('error', error))
  }

  function getStix() {
    console.log('getting stix')
    // fetch('http://127.0.0.1:8082/threatScore/filterStixBundle/' + hash + '/tagged_bundle_data')
    //   .then((res) => res.json())
    //   .then((data) => {
    //     SetHash(data)
    //   })
    // console.log('abc' + hash1.id)
    var requestOptions = {
      method: 'GET',
      redirect: 'follow',
    }

    fetch(
      'http://127.0.0.1:8086/threatScore/filterStixBundle/?org_id=' +
        encodeURIComponent(data1.org_id) +
        '&report_id=' +
        encodeURIComponent(data1.reportId) +
        '&index=' +
        encodeURIComponent(data1.index),
      requestOptions,
    )
      .then((response) => response.text())
      .then((result) => SetHash(result))
      .catch((error) => console.log('error', error))

    //  get merged reports
    var myHeaders = new Headers()
    myHeaders.append('Content-Type', 'application/json')

    var raw = JSON.stringify({
      hash: hash,
    })

    var requestOptions = {
      method: 'POST',
      headers: myHeaders,
      body: raw,
      redirect: 'follow',
    }

    fetch('http://127.0.0.1:8082/dataAnalysis/getRelatedReports', requestOptions)
      .then((response) => response.text())
      .then((result) => setMergedReports(result))
      .catch((error) => console.log('error', error))

    if (mergedReports != []) {
      for (var i = 0; i < mergedReports.length; i++) {
        fetch(
          'http://127.0.0.1:8082/threatScore/filterStixBundle/' +
            mergedReports[i].id +
            mergedReports[i].index,
        )
          .then((res) => res.json())
          .then((data) => {
            mergedReportsBundles.push(data)
          })
      }
      console.log('merged reports!!!! ' + mergedReportsBundles)
    }
  }
  useEffect(() => {
    getStix()
    getScore()
    return () => {
      console.log('returning ')
    }
  }, [])

  return (
    <>
      <CCard className="mb-4">
        <CCardHeader>
          Report Details {location.state.org_id}
          <CButton href="/latestReports" style={{ float: 'right' }}>
            Return
          </CButton>
        </CCardHeader>
        <CCardBody>
          {<b>{location.state.rawText}</b>}
          {<br></br>}
          <CTable align="middle" className="mb-0 border" hover responsive>
            <CTableHead color="light">
              <CTableRow>
                {location.state.source ? (
                  <CTableHeaderCell className="text-center">Source</CTableHeaderCell>
                ) : (
                  console.log('-')
                )}
                {location.state.malware ? (
                  <CTableHeaderCell className="text-center">Malware</CTableHeaderCell>
                ) : (
                  console.log('-')
                )}
                {location.state.vulnerabilities ? (
                  <CTableHeaderCell className="text-center">Vulnerabilities</CTableHeaderCell>
                ) : (
                  console.log('-')
                )}
                {location.state.location ? (
                  <CTableHeaderCell className="text-center">Location</CTableHeaderCell>
                ) : (
                  console.log('-')
                )}
                {location.state.threatActors ? (
                  <CTableHeaderCell className="text-center">ThreatActors</CTableHeaderCell>
                ) : (
                  console.log('-')
                )}
                {location.state.identities ? (
                  <CTableHeaderCell className="text-center">Identities</CTableHeaderCell>
                ) : (
                  console.log('-')
                )}
                {location.state.tools ? (
                  <CTableHeaderCell className="text-center">Tools</CTableHeaderCell>
                ) : (
                  console.log('-')
                )}
                {location.state.infrastructure ? (
                  <CTableHeaderCell className="text-center">Infrastructure</CTableHeaderCell>
                ) : (
                  console.log('-')
                )}
                {location.state.campaigns ? (
                  <CTableHeaderCell className="text-center">Campaigns</CTableHeaderCell>
                ) : (
                  console.log('-')
                )}
                {location.state.attackPatterns ? (
                  <CTableHeaderCell className="text-center">Attack-Pattern</CTableHeaderCell>
                ) : (
                  console.log('-')
                )}
              </CTableRow>
            </CTableHead>
            <CTableBody>
              <CTableRow>
                {location.state.source ? (
                  <CTableDataCell className="text-center">{location.state.source}</CTableDataCell>
                ) : (
                  console.log('-')
                )}
                {location.state.malware ? (
                  <CTableDataCell className="text-center">{location.state.malware}</CTableDataCell>
                ) : (
                  console.log('-')
                )}
                {location.state.vulnerabilities ? (
                  <CTableDataCell className="text-center">
                    {location.state.vulnerabilities}
                  </CTableDataCell>
                ) : (
                  console.log('-')
                )}
                {location.state.location ? (
                  <CTableDataCell className="text-center">
                    {location.state.locations}
                  </CTableDataCell>
                ) : (
                  console.log('-')
                )}
                {location.state.threatActors ? (
                  <CTableDataCell className="text-center">
                    {location.state.threatActors}
                  </CTableDataCell>
                ) : (
                  console.log('-')
                )}
                {location.state.identities ? (
                  <CTableDataCell className="text-center">
                    {location.state.identities}
                  </CTableDataCell>
                ) : (
                  console.log('-')
                )}
                {location.state.tools ? (
                  <CTableDataCell className="text-center">{location.state.tools}</CTableDataCell>
                ) : (
                  console.log('-')
                )}
                {location.state.infrastructure ? (
                  <CTableDataCell className="text-center">
                    {location.state.infrastructure}
                  </CTableDataCell>
                ) : (
                  console.log('-')
                )}
                {location.state.campaigns ? (
                  <CTableDataCell className="text-center">
                    {location.state.campaigns}
                  </CTableDataCell>
                ) : (
                  console.log('-')
                )}
                {location.state.attackPatterns ? (
                  <CTableDataCell className="text-center">
                    {location.state.attackPatterns}
                  </CTableDataCell>
                ) : (
                  console.log('-')
                )}
              </CTableRow>
            </CTableBody>
          </CTable>
          <div>
            <p>Threat Score: {threatScore}</p>
          </div>
          <div>
            STIX Visualizer
            {/*<Visualizer graph1={hash1} />*/}
          </div>
        </CCardBody>
      </CCard>
    </>
  )
}

export default Reports
