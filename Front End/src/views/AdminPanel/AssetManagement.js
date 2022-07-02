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

import { useLocation } from 'react-router-dom'

const AssetManagement = () => {
  var location = useLocation()
  var assetName
  var vendor
  var version
  const [assets, SetAssets] = useState([])
  const [toast, addToast] = useState(false)
  const [edit, setEdit] = useState(false)
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
  var vendorEdit
  const vendorEditHandler = (index) => (e) => {
    console.log('index: ' + index)
    console.log('property vendor: ' + e.target.value)
    let newArr = [...assets]
    // console.log('new arr', newArr)
    newArr[index].vendor = e.target.value
    SetAssets(newArr)
  }
  var response

  function addAsset() {
    console.log('in function')
    if (version == undefined || vendor == undefined || assetName == undefined) {
      return
    }

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
        req.id = data.id
        console.log('sending...' + JSON.stringify(req))
        response = JSON.parse(JSON.stringify(data))
        setOrgName(response.organization.name)
        SetAssets((assets) => [...assets, req])
        if (!edit) {
          addToast(true)
        }
      })
  }
  function getAssets() {
    console.log('org id ', location.state.org_id)
    fetch('http://127.0.0.1:8084/assets/assetsByOrganization/' + location.state.org_id)
      .then((res) => res.json())
      .then((data) => {
        console.log('assets' + JSON.stringify(data))
        SetAssets(data)
        // console.log('id of org' + orgName)
        // addToast(exampleToast(orgName))
      })
  }
  async function deleteRow(el) {
    if (!edit) {
      console.log('delete', el.id)
      SetAssets(assets.filter((item) => item !== el))
      console.log('Asset', assets)
    }
    var myHeaders = new Headers()
    myHeaders.append('Content-Type', 'application/json')

    var raw = JSON.stringify({
      id: el.id,
    })

    var requestOptions = {
      method: 'POST',
      headers: myHeaders,
      body: raw,
      redirect: 'follow',
    }

    await fetch('http://127.0.0.1:8084/assets/deleteAsset/' + location.state.org_id, requestOptions)
      .then((response) => response.text())
      .then((result) => {
        console.log('........', result)
      })
      .catch((error) => console.log('error', error))
  }
  function editRow() {
    console.log('edit')
    setEdit(true)
  }
  async function saveRow() {
    console.log('saveRow')
    setEdit(false)
    let assetsTemp
    assetsTemp = assets

    // for (let i = 0; i < assetsTemp.length; i++) {
    //   await deleteRow(assets[i])
    //   console.log('Asset check', assetsTemp.length, assets)
    // }
    SetAssets([])

    for (let i = 0; i < assetsTemp.length; i++) {
      deleteRow(assets[i])
      assetName = assetsTemp[i].name
      vendor = assetsTemp[i].vendor
      version = assetsTemp[i].version

      addAsset()
    }
    console.log('assets update', assets)
  }
  useEffect(() => {
    // getStix()
    getAssets()
    return () => {
      console.log('returning ')
    }
  }, [])
  let versionEdit
  const versionEditHandler = (index) => (e) => {
    console.log('index: ' + index)
    console.log('version: ' + e.target.value)
    let newArr = [...assets]
    // console.log('new arr', newArr)
    newArr[index].version = e.target.value
    SetAssets(newArr)
  }
  let nameEdit

  const nameEditHandler2 = (index) => (e) => {
    console.log('index: ' + index)
    console.log('property name: ' + e.target.value)
    let newArr = [...assets]
    // console.log('new arr', newArr)
    newArr[index].name = e.target.value
    SetAssets(newArr)
  }
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
                  <CFormLabel>
                    <h5>Vendor</h5>
                  </CFormLabel>
                  <CFormInput
                    type="text"
                    id="exampleInputName1"
                    aria-describedby="nameHelp"
                    required={true}
                    onChange={vendorChangedHandler}
                  />
                </div>
                <div className="mb-3">
                  <CFormLabel htmlFor="exampleName1">
                    <h5>Asset Name</h5>
                  </CFormLabel>
                  <CFormInput
                    type="name"
                    id="exampleInputName1"
                    aria-describedby="nameHelp"
                    required={true}
                    onChange={assetNameChangedHandler}
                  />
                </div>
                <div className="mb-3">
                  <CFormLabel>
                    <h5>Version</h5>
                  </CFormLabel>
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
          {/*display this as organization's assets*/}
          {console.log('assets update', assets)}
          <CRow>
            {/*<div style={{ visibility: tableVisibilty }}>*/}
            <h3>
              Your Organization&apos;s Assets{' '}
              <CButton
                style={{ marginLeft: '4px' }}
                color={!edit ? 'secondary' : 'warning'}
                id={!edit ? 'editButton' : 'saveButton'}
                value={edit ? 'Edit' : 'Save'}
                onClick={() => (!edit ? editRow() : saveRow())}
              >
                {!edit ? 'Edit' : 'Save'}
              </CButton>
            </h3>

            <CTable align="middle" className="mb-0 border" hover responsive>
              <CTableHead color="light">
                <CTableRow>
                  <CTableHeaderCell className="text-center">S.no</CTableHeaderCell>
                  <CTableHeaderCell className="text-center">Vendor</CTableHeaderCell>
                  <CTableHeaderCell className="text-center">Asset Name</CTableHeaderCell>
                  <CTableHeaderCell className="text-center">Version</CTableHeaderCell>
                  <CTableHeaderCell className="text-center"></CTableHeaderCell>
                </CTableRow>
              </CTableHead>
              <CTableBody>
                {Object.values(assets).map((el, index) => (
                  <CTableRow key={index}>
                    <CTableDataCell>
                      <div style={{ display: 'flex', justifyContent: 'center' }}>{index}</div>
                    </CTableDataCell>
                    <CTableDataCell>
                      {edit === true ? (
                        <div className="assets">
                          <CFormInput
                            type="text"
                            defaultValue={el.vendor}
                            id="a"
                            onChange={vendorEditHandler(index)}
                          />
                        </div>
                      ) : (
                        <div className="assets">{el.vendor}</div>
                      )}
                    </CTableDataCell>
                    <CTableDataCell>
                      {edit === true ? (
                        <div className="assets">
                          <CFormInput
                            type="text"
                            defaultValue={el.name}
                            id="a"
                            onChange={nameEditHandler2(index)}
                          />
                        </div>
                      ) : (
                        <div className="assets">{el.name}</div>
                      )}
                    </CTableDataCell>
                    <CTableDataCell>
                      {edit === true ? (
                        <div className="assets">
                          <CFormInput
                            type="text"
                            defaultValue={el.version}
                            id="a"
                            onChange={versionEditHandler(index)}
                          />
                        </div>
                      ) : (
                        <div className="assets">{el.version}</div>
                      )}
                    </CTableDataCell>
                    <CTableDataCell>
                      <CButton
                        style={{ margin: '10px' }}
                        color="primary"
                        onClick={() => deleteRow(el)}
                      >
                        Delete
                      </CButton>
                    </CTableDataCell>
                  </CTableRow>
                ))}
              </CTableBody>
            </CTable>
            {/*</div>*/}
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
