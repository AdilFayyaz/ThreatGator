import PropTypes from 'prop-types'
import React, { useEffect, useState, createRef } from 'react'
import classNames from 'classnames'
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

const ThemeView = () => {
  const [color, setColor] = useState('rgb(255, 255, 255)')
  const ref = createRef()

  useEffect(() => {
    const el = ref.current.parentNode.firstChild
    const varColor = window.getComputedStyle(el).getPropertyValue('background-color')
    setColor(varColor)
  }, [ref])

  return (
    <table className="table w-100" ref={ref}>
      <tbody>
        <tr>
          <td className="text-medium-emphasis">HEX:</td>
          <td className="font-weight-bold">{rgbToHex(color)}</td>
        </tr>
        <tr>
          <td className="text-medium-emphasis">RGB:</td>
          <td className="font-weight-bold">{color}</td>
        </tr>
      </tbody>
    </table>
  )
}

const ThemeColor = ({ className, children }) => {
  const classes = classNames(className, 'theme-color w-75 rounded mb-3')
  return (
    <CCol xs={12} sm={6} md={4} xl={2} className="mb-4">
      <div className={classes} style={{ paddingTop: '75%' }}></div>
      {children}
      <ThemeView />
    </CCol>
  )
}

ThemeColor.propTypes = {
  children: PropTypes.node,
  className: PropTypes.string,
}

const Reports = () => {
  const [reportsData, SetReportsData] = useState({})
  // fetching data from data analysis service for reports
  const location = useLocation()
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
          {<b>{location.state.rawText}</b>}
          {<br></br>}
          <CTable align="middle" className="mb-0 border" hover responsive>
            <CTableHead color="light">
              <CTableRow>
                {/*<CTableHeaderCell className="text-center">*/}
                {/*<CIcon icon={cilPeople} />*/}
                {/*</CTableHeaderCell>*/}
                <CTableHeaderCell className="text-center">Source</CTableHeaderCell>
                <CTableHeaderCell className="text-center">Malware</CTableHeaderCell>
                <CTableHeaderCell className="text-center">Vulnerabilities</CTableHeaderCell>
                <CTableHeaderCell className="text-center">Location</CTableHeaderCell>
                <CTableHeaderCell className="text-center">ThreatActors</CTableHeaderCell>
                <CTableHeaderCell className="text-center">Identities</CTableHeaderCell>
                <CTableHeaderCell className="text-center">Tools</CTableHeaderCell>
                <CTableHeaderCell className="text-center">Infrastructure</CTableHeaderCell>
                <CTableHeaderCell className="text-center">Campaigns</CTableHeaderCell>
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
              </CTableRow>
              {/*  {Object.values(reportsData).map((el) => (*/}
              {/*    <CTableRow key={el}>*/}
              {/*      <CTableDataCell>{el.source}</CTableDataCell>*/}
              {/*      <CTableDataCell>{el.malwares}</CTableDataCell>*/}
              {/*      <CTableDataCell>{el.vulnerabilities}</CTableDataCell>*/}
              {/*      <CTableDataCell>{el.locations}</CTableDataCell>*/}
              {/*      <CTableDataCell>{el.threatActors}</CTableDataCell>*/}

              {/*      /!*<CTableDataCell>{el.identities}</CTableDataCell>*!/*/}
              {/*      /!*<CTableDataCell>{el.tools}</CTableDataCell>*!/*/}
              {/*      /!*<CTableDataCell>{el.infrastructure}</CTableDataCell>*!/*/}
              {/*      /!*<CTableDataCell>{el.campaigns}</CTableDataCell>*!/*/}
              {/*    </CTableRow>*/}
              {/*  ))}*/}
            </CTableBody>
          </CTable>
        </CCardBody>
      </CCard>
    </>
  )
}

export default Reports
