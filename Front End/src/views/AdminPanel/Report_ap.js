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
  const attackPatternChangedHandler = (event) => {
    event.preventDefault()
    location.state.attackPattern = event.target.value
  }
  function updateElastic() {
    if (!location.state.malwares) {
      location.state.malwares = ''
    }
    if (!location.state.threatActors) {
      location.state.threatActors = ''
    }
    if (!location.state.identities) {
      location.state.identities = ''
    }
    if (!location.state.locations) {
      location.state.locations = ''
    }
    if (!location.state.tools) {
      location.state.tools = ''
    }
    if (!location.state.vulnerabilities) {
      location.state.vulnerabilities = ''
    }
    if (!location.state.infrastructure) {
      location.state.infrastructure = ''
    }
    if (!location.state.indicators) {
      location.state.indicators = ''
    }
    if (!location.state.campaigns) {
      location.state.campaigns = ''
    }
    if (!location.state.attackPattern) {
      location.state.attackPattern = ''
    }
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
      attackPatterns: location.state.attackPattern,
    }
    const requestOptions = {
      method: 'POST',
      headers: { 'Content-Type': 'application/json' },
      body: JSON.parse(JSON.stringify(req)),
    }
    fetch('http://127.0.0.1:8082/dataAnalysis/updateElasticDocument', requestOptions)
      .then((res) => res.json())
      .then((data) => {
        console.log('got...' + JSON.stringify(data))
        console.log('sending...' + JSON.stringify(req))
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
          Report Details
          <CButton href="/latestReports_admin" style={{ float: 'right' }}>
            Return
          </CButton>
        </CCardHeader>
        <CCardBody id="card">
          {/*{<b>{location.state.rawText}</b>}*/}
          {<br></br>}
          <CTable align="middle" className="mb-0 border" hover responsive>
            <CTableHead color="light">
              <CTableRow>
                {location.state.source || !isedit ? (
                  <CTableHeaderCell className="text-center">Source</CTableHeaderCell>
                ) : (
                  console.log('-')
                )}
                {location.state.malwares || !isedit ? (
                  <CTableHeaderCell className="text-center">Malwares</CTableHeaderCell>
                ) : (
                  console.log('-')
                )}
                {location.state.vulnerabilities || !isedit ? (
                  <CTableHeaderCell className="text-center">Vulnerabilities</CTableHeaderCell>
                ) : (
                  console.log('-')
                )}
                {location.state.locations || !isedit ? (
                  <CTableHeaderCell className="text-center">Location</CTableHeaderCell>
                ) : (
                  console.log('-')
                )}
                {location.state.threatActors || !isedit ? (
                  <CTableHeaderCell className="text-center">ThreatActors</CTableHeaderCell>
                ) : (
                  console.log('-')
                )}
                {location.state.identities || !isedit ? (
                  <CTableHeaderCell className="text-center">Identities</CTableHeaderCell>
                ) : (
                  console.log('-')
                )}
                {location.state.tools || !isedit ? (
                  <CTableHeaderCell className="text-center">Tools</CTableHeaderCell>
                ) : (
                  console.log('=')
                )}
                {location.state.infrastructure || !isedit ? (
                  <CTableHeaderCell className="text-center">Infrastructure</CTableHeaderCell>
                ) : (
                  console.log('-')
                )}
                {location.state.campaigns || !isedit ? (
                  <CTableHeaderCell className="text-center">Campaigns</CTableHeaderCell>
                ) : (
                  console.log('-')
                )}
                {location.state.attackPattern || !isedit ? (
                  <CTableHeaderCell className="text-center">AttackPattern</CTableHeaderCell>
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
                ) : isedit ? (
                  console.log(isedit)
                ) : (
                  <CTableDataCell className="text-center">
                    <CFormInput
                      type="text"
                      placeholder={location.state.source}
                      id="a"
                      onChange={sourceChangedHandler}
                    />
                  </CTableDataCell>
                )}
                {/*malwares*/}
                {location.state.malwares ? (
                  isedit ? (
                    <CTableDataCell className="text-center">
                      {location.state.malwares}
                    </CTableDataCell>
                  ) : (
                    <CTableDataCell className="text-center">
                      <CFormInput
                        ref={blah}
                        type="text"
                        placeholder={location.state.malwares}
                        id="a"
                        onChange={malwaresChangedHandler}
                      />
                    </CTableDataCell>
                  )
                ) : isedit ? (
                  console.log(isedit)
                ) : (
                  <CTableDataCell className="text-center">
                    <CFormInput
                      type="text"
                      placeholder={location.state.malwares}
                      id="a"
                      onChange={malwaresChangedHandler}
                    />
                  </CTableDataCell>
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
                ) : isedit ? (
                  console.log(isedit)
                ) : (
                  <CTableDataCell className="text-center">
                    <CFormInput
                      type="text"
                      placeholder={location.state.vulnerabilities}
                      id="a"
                      onChange={vulnerabilitiesChangedHandler}
                    />
                  </CTableDataCell>
                )}
                {/*location*/}
                {location.state.locations ? (
                  isedit ? (
                    <CTableDataCell className="text-center">
                      {location.state.locations}
                    </CTableDataCell>
                  ) : (
                    <CTableDataCell className="text-center">
                      <CFormInput
                        ref={blah}
                        type="text"
                        placeholder={location.state.locations}
                        id="a"
                        onChange={locationChangedHandler}
                      />
                    </CTableDataCell>
                  )
                ) : isedit ? (
                  console.log(isedit)
                ) : (
                  <CTableDataCell className="text-center">
                    <CFormInput
                      type="text"
                      placeholder={location.state.locations}
                      id="a"
                      onChange={locationChangedHandler}
                    />
                  </CTableDataCell>
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
                ) : isedit ? (
                  console.log(isedit)
                ) : (
                  <CTableDataCell className="text-center">
                    <CFormInput
                      type="text"
                      placeholder={location.state.threatActors}
                      id="a"
                      onChange={threatActorsChangedHandler}
                    />
                  </CTableDataCell>
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
                ) : isedit ? (
                  console.log(isedit)
                ) : (
                  <CTableDataCell className="text-center">
                    <CFormInput
                      type="text"
                      placeholder={location.state.identities}
                      id="a"
                      onChange={identitiesChangedHandler}
                    />
                  </CTableDataCell>
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
                ) : isedit ? (
                  console.log(isedit)
                ) : (
                  <CTableDataCell className="text-center">
                    <CFormInput
                      type="text"
                      placeholder={location.state.tools}
                      id="a"
                      onChange={toolsChangedHandler}
                    />
                  </CTableDataCell>
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
                ) : isedit ? (
                  console.log(isedit)
                ) : (
                  <CTableDataCell className="text-center">
                    <CFormInput
                      type="text"
                      placeholder={location.state.infrastructure}
                      id="a"
                      onChange={infrastructureChangedHandler}
                    />
                  </CTableDataCell>
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
                ) : isedit ? (
                  console.log(isedit)
                ) : (
                  <CTableDataCell className="text-center">
                    <CFormInput
                      type="text"
                      placeholder={location.state.campaigns}
                      id="a"
                      onChange={campaignsChangedHandler}
                    />
                  </CTableDataCell>
                )}
                {/*Attack Pattern*/}
                {location.state.attackPattern ? (
                  isedit ? (
                    <CTableDataCell className="text-center">
                      {location.state.attackPattern}
                    </CTableDataCell>
                  ) : (
                    <CTableDataCell className="text-center">
                      <CFormInput
                        ref={blah}
                        type="text"
                        placeholder={location.state.attackPattern}
                        id="a"
                        onChange={attackPatternChangedHandler}
                      />
                    </CTableDataCell>
                  )
                ) : isedit ? (
                  console.log(isedit)
                ) : (
                  <CTableDataCell className="text-center">
                    <CFormInput
                      type="text"
                      placeholder={location.state.attackPattern}
                      id="a"
                      onChange={attackPatternChangedHandler}
                    />
                  </CTableDataCell>
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
              <CTableRow>
                <CBadge style={{ backgroundColor: '#F7F7FF', visibility: badge }}>
                  Scroll Right to Save
                </CBadge>
              </CTableRow>
            </CTableBody>
          </CTable>
          <div>
            STIX Visualizer
            <Visualizer graph1={hash1} />
          </div>
        </CCardBody>
      </CCard>
    </>
  )
}

export default Report_ap
