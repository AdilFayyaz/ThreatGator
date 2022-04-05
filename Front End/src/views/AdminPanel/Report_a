import PropTypes from 'prop-types'
import React, { useEffect, useState, createRef } from 'react'
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
} from '@coreui/react'
import { rgbToHex } from '@coreui/utils'
import { DocsLink } from 'src/components'
import CIcon from '@coreui/icons-react'
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

const Report_a = () => {
  const [reportsData, SetReportsData] = useState({})
  // fetching data from data analysis service for reports
  const location = useLocation()
  const test = 'yahoo'

  function editField(row) {
    var len = row.parentNode.parentNode.cells.length
    console.log(len)
    for (var j = 0; j < len - 1; j++) {
      row.parentNode.parentNode.cells[j].innerHTML =
        '<input type="text" value=row.parentNode.parentNode.cells[j].value id=' + j + '>'
    }
    var node = document.createElement('input')
    var attr = document.createAttribute('type')
    attr.value = 'button'
    node.setAttributeNode(attr)
    var attr2 = document.createAttribute('value')
    attr2.value = 'Save'
    node.setAttributeNode(attr2)

    var attr3 = document.createAttribute('onclick')
    attr3.value = 'saveRow(this)'
    node.setAttributeNode(attr3)
    row.replaceWith(node)
  }

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
                <CTableHeaderCell className="text-center">Source</CTableHeaderCell>

                <CTableHeaderCell className="text-center">Malware</CTableHeaderCell>

                <CTableHeaderCell className="text-center">Vulnerabilities</CTableHeaderCell>

                <CTableHeaderCell className="text-center">Location</CTableHeaderCell>

                <CTableHeaderCell className="text-center">ThreatActors</CTableHeaderCell>

                <CTableHeaderCell className="text-center">Identities</CTableHeaderCell>

                <CTableHeaderCell className="text-center">Tools</CTableHeaderCell>

                <CTableHeaderCell className="text-center">Infrastructure</CTableHeaderCell>

                <CTableHeaderCell className="text-center">Campaigns</CTableHeaderCell>
                <CTableHeaderCell className="text-center">Edit</CTableHeaderCell>
              </CTableRow>
            </CTableHead>
            <CTableBody>
              <CTableRow>
                <CTableDataCell className="text-center">{location.state.source}</CTableDataCell>

                <CTableDataCell className="text-center">{location.state.malware}</CTableDataCell>

                <CTableDataCell className="text-center">
                  {location.state.vulnerabilities}
                </CTableDataCell>

                <CTableDataCell className="text-center">{location.state.locations}</CTableDataCell>

                <CTableDataCell className="text-center">
                  {location.state.threatActors}
                </CTableDataCell>

                <CTableDataCell className="text-center">{location.state.identities}</CTableDataCell>

                <CTableDataCell className="text-center">{location.state.tools}</CTableDataCell>

                <CTableDataCell className="text-center">
                  {location.state.infrastructure}
                </CTableDataCell>

                <CTableDataCell className="text-center">{location.state.campaigns}</CTableDataCell>
                <CTableDataCell className="text-center">
                  {/*<CButton value="Edit" onClick={() => editField(this)}>*/}
                  {/*  Edit*/}
                  {/*</CButton>*/}
                </CTableDataCell>
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

export default Report_a
