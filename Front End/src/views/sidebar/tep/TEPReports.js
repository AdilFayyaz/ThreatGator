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
const TEPReports = () => {
  const [exchangeData, SetExchangeData] = useState({})
  // fetching data from data analysis service for reports

  // fetching data from data analysis service for threat exchange platform's data

  const getTEPReports = (event) => {
    event.preventDefault()
    fetch('http://127.0.0.1:8082/dataAnalysis/getThreatExchangeData')
      .then((res) => res.json())
      .then((data) => {
        console.log(data)
        SetExchangeData(data)
        // console.log(Object.keys(data))
        // openModalExchange()
      })
  }

  return (
    <>
      {getTEPReports}
      <CCard className="mb-4">
        <CCardHeader>ThreatGator&apos;s Latest Threat Exchange Platform Reports</CCardHeader>
        <CCardBody>
          <CTable align="middle" className="mb-0 border" hover responsive>
            <CTableHead color="light">
              <CTableRow>
                <CTableHeaderCell className="text-center">ID</CTableHeaderCell>
                <CTableHeaderCell className="text-center">Title</CTableHeaderCell>
                <CTableHeaderCell className="text-center">Type</CTableHeaderCell>
                <CTableHeaderCell className="text-center">Threat Score</CTableHeaderCell>
                <CTableHeaderCell className="text-center">Confidence</CTableHeaderCell>
                <CTableHeaderCell className="text-center">Risk Factor</CTableHeaderCell>
                <CTableHeaderCell className="text-center">Data Sources</CTableHeaderCell>
                <CTableHeaderCell className="text-center">iPv4 Details</CTableHeaderCell>
              </CTableRow>
            </CTableHead>
            <CTableBody>
              {Object.values(exchangeData).map((el) => (
                <CTableRow key={el}>
                  <CTableDataCell>{el.id}</CTableDataCell>
                  <CTableDataCell>{el.title}</CTableDataCell>
                  <CTableDataCell>{el.type}</CTableDataCell>
                  <CTableDataCell>{el.threatScore}</CTableDataCell>
                  <CTableDataCell>{el.confidence}</CTableDataCell>
                  <CTableDataCell>{el.riskFactor}</CTableDataCell>
                  <CTableDataCell>{el.dataSources}</CTableDataCell>
                  <CTableDataCell>{el.iPv4Details}</CTableDataCell>
                </CTableRow>
              ))}
            </CTableBody>
          </CTable>
        </CCardBody>
      </CCard>
    </>
  )
}

export default TEPReports
