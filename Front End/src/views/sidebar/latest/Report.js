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

const Reports = () => {
  const [reportsData, SetReportsData] = useState({})
  // fetching data from data analysis service for reports
  const location = useLocation()
  const test = 'yahoo'
  return (
    <>
      <CCard className="mb-4">
        <CCardHeader>
          Report Details
          <CButton href="/latestReports" style={{ float: 'right' }}>
            Return
          </CButton>
        </CCardHeader>
        <CCardBody>
          {/*{<b>{location.state.rawText}</b>}*/}
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
                {location.state.attackPattern ? (
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
                {location.state.attackPattern ? (
                  <CTableDataCell className="text-center">
                    {location.state.attackPattern}
                  </CTableDataCell>
                ) : (
                  console.log('-')
                )}
              </CTableRow>
            </CTableBody>
          </CTable>
          <div>
            STIX Visualizer
            <Visualizer name={test} />
          </div>
        </CCardBody>
      </CCard>
    </>
  )
}

export default Reports
