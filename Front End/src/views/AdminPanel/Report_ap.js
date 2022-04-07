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
  const [isedit, SetIsedit] = useState(true)
  // fetching data from data analysis service for reports
  const location = useLocation()
  const edit = useRef(null)
  const blah = useRef(null)
  const arr = [null, null, null, null, null, null, null, null, null, null]
  const refs = useRef(arr.map(() => React.createRef()))

  const test = 'yahoo'

  function saveRow() {
    console.log('save')
    //
    // var len = edit.current.cells.length
    // console.log(arr.length)
    // for (var j = 0; j < len - 1; j++) {
    //   let te = blah.current
    //   console.log('ttt' + te)
    //   edit.current.cells[j].innerHTML = te
    // }
    SetIsedit(true)
  }

  const editField = () => {
    console.log('edit')
    //
    // var len = edit.current.cells.length

    // for (var j = 0; j < len - 1; j++) {
    //   var text = edit.current.cells[j].innerHTML
    //   console.log(text)
    //
    //   edit.current.cells[j].innerHTML =
    //     '<input ref=' + blah + 'type="text" value="' + text + '" id=' + j + '>'
    //   console.log('ref ' + blah.current)
    // }
    SetIsedit(false)
  }
  const sourceChangedHandler = (event) => {
    event.preventDefault()
    location.state.source = event.target.value
  }
  const malwareChangedHandler = (event) => {
    event.preventDefault()
    location.state.malware = event.target.value
  }
  const vulnerabilitiesChangedHandler = (event) => {
    event.preventDefault()
    location.state.vulnerabilities = event.target.value
  }
  const locationChangedHandler = (event) => {
    event.preventDefault()
    location.state.location = event.target.value
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
    // const requestOptions = {
    //   method: 'POST',
    //   headers: { 'Content-Type': 'application/json' },
    //   body: _hash,
    // }
    // fetch('http://127.0.0.1:8082/dataAnalysis/updateElasticDocument', location.state.requestOptions)
    //   .then((res) => res.json())
    //   .then((data) => {
    //     console.log('sending...' + data)
    //   })
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
                {/*<CTableHeaderCell className="text-center">Edit</CTableHeaderCell>*/}
              </CTableRow>
            </CTableHead>
            <CTableBody>
              <CTableRow ref={edit}>
                {location.state.source ? (
                  isedit ? (
                    <CTableDataCell className="text-center">{location.state.source}</CTableDataCell>
                  ) : (
                    <CTableDataCell className="text-center">
                      <CFormInput
                        ref={blah}
                        type="text"
                        placeholder={location.state.source}
                        id="a"
                        onChange={sourceChangedHandler}
                      />
                    </CTableDataCell>
                  )
                ) : (
                  console.log('src')
                )}
                {/*malware*/}
                {location.state.malware ? (
                  isedit ? (
                    <CTableDataCell className="text-center">
                      {location.state.malware}
                    </CTableDataCell>
                  ) : (
                    <CTableDataCell className="text-center">
                      <CFormInput
                        ref={blah}
                        type="text"
                        placeholder={location.state.malware}
                        id="a"
                        onChange={malwareChangedHandler}
                      />
                    </CTableDataCell>
                  )
                ) : (
                  console.log('mal')
                )}
                {/*vulner*/}
                {location.state.vulnerabilities ? (
                  isedit ? (
                    <CTableDataCell className="text-center">
                      {location.state.vulnerabilities}
                    </CTableDataCell>
                  ) : (
                    <CTableDataCell className="text-center">
                      <CFormInput
                        ref={blah}
                        type="text"
                        placeholder={location.state.vulnerabilities}
                        id="a"
                        onChange={vulnerabilitiesChangedHandler}
                      />
                    </CTableDataCell>
                  )
                ) : (
                  console.log('vul')
                )}
                {/*location*/}
                {location.state.location ? (
                  isedit ? (
                    <CTableDataCell className="text-center">
                      {location.state.location}
                    </CTableDataCell>
                  ) : (
                    <CTableDataCell className="text-center">
                      <CFormInput
                        ref={blah}
                        type="text"
                        placeholder={location.state.location}
                        id="a"
                        onChange={locationChangedHandler}
                      />
                    </CTableDataCell>
                  )
                ) : (
                  console.log('loc')
                )}
                {/*threat actors*/}
                {location.state.threatActors ? (
                  isedit ? (
                    <CTableDataCell className="text-center">
                      {location.state.threatActors}
                    </CTableDataCell>
                  ) : (
                    <CTableDataCell className="text-center">
                      <CFormInput
                        ref={blah}
                        type="text"
                        placeholder={location.state.threatActors}
                        id="a"
                        onChange={threatActorsChangedHandler}
                      />
                    </CTableDataCell>
                  )
                ) : (
                  console.log('loc')
                )}
                {/*idnetities*/}
                {location.state.identities ? (
                  isedit ? (
                    <CTableDataCell className="text-center">
                      {location.state.identities}
                    </CTableDataCell>
                  ) : (
                    <CTableDataCell className="text-center">
                      <CFormInput
                        ref={blah}
                        type="text"
                        placeholder={location.state.identities}
                        id="a"
                        onChange={identitiesChangedHandler}
                      />
                    </CTableDataCell>
                  )
                ) : (
                  console.log('-')
                )}
                {/*tools*/}
                {location.state.tools ? (
                  isedit ? (
                    <CTableDataCell className="text-center">{location.state.tools}</CTableDataCell>
                  ) : (
                    <CTableDataCell className="text-center">
                      <CFormInput
                        ref={blah}
                        type="text"
                        placeholder={location.state.tools}
                        id="a"
                        onChange={toolsChangedHandler}
                      />
                    </CTableDataCell>
                  )
                ) : (
                  console.log('-')
                )}
                {/*infra*/}
                {location.state.infrastructure ? (
                  isedit ? (
                    <CTableDataCell className="text-center">
                      {location.state.infrastructure}
                    </CTableDataCell>
                  ) : (
                    <CTableDataCell className="text-center">
                      <CFormInput
                        ref={blah}
                        type="text"
                        placeholder={location.state.infrastructure}
                        id="a"
                        onChange={infrastructureChangedHandler}
                      />
                    </CTableDataCell>
                  )
                ) : (
                  console.log('-')
                )}
                {/*campaign*/}
                {location.state.campaigns ? (
                  isedit ? (
                    <CTableDataCell className="text-center">
                      {location.state.campaigns}
                    </CTableDataCell>
                  ) : (
                    <CTableDataCell className="text-center">
                      <CFormInput
                        ref={blah}
                        type="text"
                        placeholder={location.state.campaigns}
                        id="a"
                        onChange={campaignsChangedHandler}
                      />
                    </CTableDataCell>
                  )
                ) : (
                  console.log('-')
                )}

                <CTableDataCell className="text-center">
                  <CButton
                    id={isedit ? 'editButton' : 'saveButton'}
                    value={isedit ? 'Edit' : 'Save'}
                    onClick={() => (isedit ? editField() : saveRow())}
                  >
                    {isedit ? 'Edit' : 'Save'}
                  </CButton>
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

export default Report_ap
