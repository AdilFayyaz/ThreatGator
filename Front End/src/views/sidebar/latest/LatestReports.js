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

const LatestReports = () => {
  const [reportsData, SetReportsData] = useState({})
  // fetching data from data analysis service for reports
  const getReports = () => {
    // event.preventDefault()

    fetch('http://127.0.0.1:8082/dataAnalysis/getReports')
      .then((res) => res.json())
      .then((data) => {
        SetReportsData(data)
        // openModalReport()
      })
    console.log('in function')
  }

  return (
    <>
      {getReports()}
      <CCard className="mb-4">
        <CCardHeader>
          ThreatGator&apos;s Latest Reports
          <DocsLink href="https://coreui.io/docs/utilities/colors/" />
        </CCardHeader>
        <CCardBody>
          <CTable align="middle" className="mb-0 border" hover responsive>
            <CTableHead color="light">
              <CTableRow>
                {/*<CTableHeaderCell className="text-center">*/}
                {/*<CIcon icon={cilPeople} />*/}
                {/*</CTableHeaderCell>*/}
                <CTableHeaderCell className="text-center">Source</CTableHeaderCell>
                {/*<CTableHeaderCell className="text-center">Raw Text</CTableHeaderCell>*/}
                <CTableHeaderCell className="text-center">Malwares</CTableHeaderCell>
                <CTableHeaderCell className="text-center">Vulnerabilities</CTableHeaderCell>
                <CTableHeaderCell className="text-center">Locations</CTableHeaderCell>
                <CTableHeaderCell className="text-center">Threat Actors</CTableHeaderCell>
                <CTableHeaderCell className="text-center">Identities</CTableHeaderCell>
                <CTableHeaderCell className="text-center">Tools</CTableHeaderCell>
                <CTableHeaderCell className="text-center">Infrastructures</CTableHeaderCell>
                <CTableHeaderCell className="text-center">Indicators</CTableHeaderCell>
                <CTableHeaderCell className="text-center">Campaigns</CTableHeaderCell>

                {/*<CTableHeaderCell>User</CTableHeaderCell>*/}
                {/*<CTableHeaderCell className="text-center">Country</CTableHeaderCell>*/}
                {/*<CTableHeaderCell>Usage</CTableHeaderCell>*/}
                {/*<CTableHeaderCell className="text-center">Payment Method</CTableHeaderCell>*/}
                {/*<CTableHeaderCell>Activity</CTableHeaderCell>*/}
              </CTableRow>
            </CTableHead>
            <CTableBody>
              {Object.values(reportsData).map((el) => (
                <CTableRow key={el}>
                  <CTableDataCell>{el.source}</CTableDataCell>
                  <CTableDataCell>{el.rawText}</CTableDataCell>
                  <CTableDataCell>{el.malwares}</CTableDataCell>
                  <CTableDataCell>{el.vulnerabilities}</CTableDataCell>
                  <CTableDataCell>{el.locations}</CTableDataCell>
                  <CTableDataCell>{el.threatActors}</CTableDataCell>
                  <CTableDataCell>{el.identities}</CTableDataCell>
                  <CTableDataCell>{el.tools}</CTableDataCell>
                  <CTableDataCell>{el.infrastructure}</CTableDataCell>
                  <CTableDataCell>{el.campaigns}</CTableDataCell>
                </CTableRow>
              ))}
            </CTableBody>
          </CTable>
        </CCardBody>
      </CCard>
    </>
  )
}

export default LatestReports
