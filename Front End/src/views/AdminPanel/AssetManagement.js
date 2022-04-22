import React, { useEffect, useState, createRef, useRef } from 'react'
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
  CForm,
  CFormLabel,
  CFormText,
  CToast,
  CToastHeader,
  CToastBody,
  CToaster,
} from '@coreui/react'

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

const AssetManagement = () => {
  var location = useLocation()
  var assetName
  var vendor
  var version
  const [assets, SetAssets] = useState({})
  const [toast, addToast] = useState(false)
  let [orgName, setOrgName] = useState()
  let [tableVisibilty, setTableVisibility] = useState('hidden')
  const [validated, setValidated] = useState(false)
  const validate = (event) => {
    event.preventDefault()
    const form = event.currentTarget
    if (form.checkValidity() === false) {
      event.preventDefault()
      event.stopPropagation()
    }
    setValidated(true)
    addAsset()
  }

  const orgNameChangedHandler = (event) => {
    event.preventDefault()
    setOrgName(event.target.value)
  }
  const vendorChangedHandler = (event) => {
    event.preventDefault()
    vendor = event.target.value
  }
  const versionChangedHandler = (event) => {
    event.preventDefault()
    version = event.target.value
  }
  const assetNameChangedHandler = (event) => {
    event.preventDefault()
    assetName = event.target.value
  }
  var response

  function addAsset() {
    console.log('in function')
    if (vendor == undefined || assetName == undefined) {
      return
    }
    addToast(true)
    setTableVisibility('visible')
    var req = {
      vendor: vendor,
      name: assetName,
      version: version,
    }
    const requestOptions = {
      method: 'POST',
      headers: { 'Content-Type': 'application/json' },
      body: JSON.stringify(req),
    }

    fetch('http://127.0.0.1:8084/assets/addAsset/' + location.state.org_id, requestOptions)
      .then((res) => res.json())
      .then((data) => {
        console.log('got...' + JSON.stringify(data))
        console.log('sending...' + JSON.stringify(req))
        response = JSON.parse(JSON.stringify(data))
        getAssets()
      })
  }
  function getAssets() {
    fetch('http://127.0.0.1:8084/assets/assetsByOrganization/' + response.organization.id)
      .then((res) => res.json())
      .then((data) => {
        setOrgName(response.organization.name)
        console.log('assets' + JSON.stringify(data))
        SetAssets(data)
        console.log('id of org' + orgName)
        // addToast(exampleToast(orgName))
      })
  }
  useEffect(() => {
    // getStix()
    return () => {
      console.log('returning ')
    }
  }, [])

  return (
    <>
      <CCard className="mb-4">
        <CCardHeader>
          <h3>Asset Management</h3>
        </CCardHeader>
        <CCardBody id="card">
          <h5>Add Your Organizations Assets Below</h5>
          {<br></br>}
          <CRow>
            <div style={{ paddingLeft: '15%', paddingRight: '15%' }}>
              <CForm validated={validated} onSubmit={validate}>
                {/*<div className="mb-3">*/}
                {/*  <CFormLabel>Organization Name</CFormLabel>*/}
                {/*  <CFormInput*/}
                {/*    type="text"*/}
                {/*    id="exampleInputName"*/}
                {/*    aria-describedby="nameHelp"*/}
                {/*    required={true}*/}
                {/*    onChange={orgNameChangedHandler}*/}
                {/*  />*/}
                {/*</div>*/}
                <div className="mb-3">
                  <CFormLabel>Vendor</CFormLabel>
                  <CFormInput
                    type="text"
                    id="exampleInputName1"
                    aria-describedby="nameHelp"
                    required={true}
                    onChange={vendorChangedHandler}
                  />
                </div>
                <div className="mb-3">
                  <CFormLabel htmlFor="exampleName1">Asset Name</CFormLabel>
                  <CFormInput
                    type="name"
                    id="exampleInputName1"
                    aria-describedby="nameHelp"
                    required={true}
                    onChange={assetNameChangedHandler}
                  />
                </div>
                <div className="mb-3">
                  <CFormLabel>Version</CFormLabel>
                  <CFormInput
                    type="name"
                    id="exampleInputName1"
                    aria-describedby="emailHelp"
                    onChange={versionChangedHandler}
                  />
                </div>

                <CButton type="Submit" color="primary">
                  Submit
                </CButton>
              </CForm>
            </div>
          </CRow>
          <br />
          {/*display this later when submitted*/}
          <CRow>
            <div style={{ visibility: tableVisibilty }}>
              <h3>Assets of {orgName}</h3>
              <CTable align="middle" className="mb-0 border" hover responsive>
                <CTableHead color="light">
                  <CTableRow>
                    <CTableHeaderCell className="text-center">Vendor</CTableHeaderCell>
                    <CTableHeaderCell className="text-center">Asset Name</CTableHeaderCell>
                    <CTableHeaderCell className="text-center">Version</CTableHeaderCell>
                  </CTableRow>
                </CTableHead>
                <CTableBody>
                  {Object.values(assets).map((el) => (
                    <CTableRow key={el}>
                      <CTableDataCell>
                        <div className="rawText">{el.vendor}</div>
                      </CTableDataCell>
                      <CTableDataCell>
                        <div className="rawText">{el.name}</div>
                      </CTableDataCell>
                      <CTableDataCell>
                        <div className="rawText">{el.version}</div>
                      </CTableDataCell>
                    </CTableRow>
                  ))}
                </CTableBody>
              </CTable>
            </div>
          </CRow>

          {/*  TOAST */}
          <CToaster placement="top-end">
            <CToast title="ThreatGator" autohide={true} visible={toast}>
              <CToastHeader close>
                <strong className="me-auto">ThreatGator</strong>
                <small>Latest</small>
              </CToastHeader>
              <CToastBody>New Asset Added to {orgName}</CToastBody>
            </CToast>
          </CToaster>
        </CCardBody>
      </CCard>
    </>
  )
}

export default AssetManagement
