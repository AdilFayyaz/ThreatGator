import React, { useEffect, useState, createRef, useRef } from 'react'
import Visualizer from '.././visualizer/visualizer'
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
import '../../scss/Report_ap.css'
import { useHistory, useLocation } from 'react-router-dom'

const Report_ap = (props) => {
  // const [reportsData, SetReportsData] = useState({})
  // const [hash1, SetHash] = useState(0)
  const [isedit, SetIsedit] = useState(true)
  const [badge, SetBadge] = useState('hidden')
  // fetching data from data analysis service for reports
  // const location = useLocation()
  const edit = useRef(null)
  const blah = useRef(null)
  var history = useHistory()
  const [reportsData, SetReportsData] = useState({})
  var [hash1, SetHash] = useState({})
  // var hash1 = {}
  const [threatScore, SetThreatScore] = useState('')
  var mergedReports = []
  var [relatedReportData, setRelatedReportData] = useState([])
  // var [mergedReports, SetMergedReports] = useState([])
  // fetching data from data analysis service for reports
  const location = useLocation()
  const hash = location.state.hash
  // const org_id=location.state.org_id
  // const mergedReportsBundles = []
  var [mergedReportsBundles, setMergedReportsBundles] = useState([])
  var [objectMergedReport, setObjectMergedReport] = useState({})
  const relatedLinks = []
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

  function goToDetails(
    hash,
    source,
    rawtext,
    malwares,
    vulnerabilities,
    locations,
    threatActors,
    identities,
    tools,
    infrastructure,
    campaigns,
    attackPatterns,
  ) {
    history.push('/Report', {
      hash: hash,
      source: source,
      rawText: rawtext,
      malware: malwares,
      vulnerabilities: vulnerabilities,
      locations: locations,
      threatActors: threatActors,
      identities: identities,
      tools: tools,
      infrastructure: infrastructure,
      campaigns: campaigns,
      attackPatterns: attackPatterns,
      org_id: data1.org_id,
    })
    window.location.reload(true)
  }

  async function getStix() {
    console.log('getting stix')

    var requestOptions = {
      method: 'GET',
      redirect: 'follow',
    }

    var x = await fetch(
      'http://127.0.0.1:8086/threatScore/filterStixBundle/?org_id=' +
        encodeURIComponent(data1.org_id) +
        '&report_id=' +
        encodeURIComponent(data1.reportId) +
        '&index=' +
        encodeURIComponent(data1.index),
      requestOptions,
    )
      .then((response) => response.json())
      .then((result) => {
        SetHash(result)
        // hash1 = result
        getMergedReports()
      })
      .catch((error) => console.log('error', error))

    // SetHash(JSON.parse(JSON.stringify(hash1)))
    // getMergedReports()
  }
  async function getMergedReports() {
    console.log('Merged called')
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
    }

    var x = await fetch('http://127.0.0.1:8082/dataAnalysis/getRelatedReports', requestOptions)
      .then((response) => response.json())
      .then((result) => {
        mergedReports = result
        console.log('Merged', mergedReports)
        split()
      })
      .catch((error) => console.log('error', error))
  }
  async function split() {
    console.log('Split called')
    if (mergedReports != []) {
      var temp = new Object()
      temp.merged = []
      var temp2 = []
      for (var i = 0; i < mergedReports.length; i++) {
        if (mergedReports[i].index === 'stix') {
          console.log(mergedReports[i])
          var y = await fetch(
            'http://127.0.0.1:8082/dataAnalysis/getStixBundle/' +
              mergedReports[i].id +
              '/' +
              mergedReports[i].index,
          )
            .then((res) => res.json())
            .then((data) => {
              setMergedReportsBundles(data)
              temp.merged.push(data)
              console.log(temp)

              // Object.assign(objectMergedReport, mergedReportsBundles)
              // mergedReportsBundles.push(data)
              // console.log(mergedReportsBundles)
            })
        } else if (mergedReports[i].index == 'tagged_bundle_data') {
          //   console.log('tagged bundle data')
          //   fetch(
          //     'http://127.0.0.1:8082/dataAnalysis/getStixBundle/' +
          //       mergedReports[i].id +
          //       '/' +
          //       mergedReports[i].index,
          //   )
          //     .then((res) => res.json())
          //     .then((data) => {
          //       relatedLinks.push(data)
          //       console.log(relatedLinks)
          //     })
          // }
          var myHeaders = new Headers()
          myHeaders.append('Content-Type', 'application/json')
          var raw = JSON.stringify({
            hash: mergedReports[i].id,
          })
          var requestOptions = {
            method: 'POST',
            headers: myHeaders,
            body: raw,
            redirect: 'follow',
          }
          var x = await fetch('http://127.0.0.1:8082/dataAnalysis/getResultOnHash', requestOptions)
            .then((response) => response.text())
            .then((data) => {
              temp2.push(JSON.parse(data))

              console.log(temp2)
            })
            .catch((error) => console.log('error', error))
        }
      }
      setObjectMergedReport(temp)
      setRelatedReportData(temp2)
    }
    console.log('merged reports!!!! ' + mergedReportsBundles)
    console.log('Related links ' + relatedLinks)
  }

  // const hash = location.state.hash
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
  const attackPatternsChangedHandler = (event) => {
    event.preventDefault()
    location.state.attackPatterns = event.target.value
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
    if (!location.state.attackPatterns) {
      location.state.attackPatterns = ''
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
      attackPatterns: location.state.attackPatterns,
    }
    const requestOptions = {
      method: 'POST',
      headers: { 'Content-Type': 'application/json' },
      body: JSON.stringify(req),
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
    getScore()
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
          {<b>{location.state.rawText}</b>}
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
                {location.state.attackPatterns || !isedit ? (
                  <CTableHeaderCell className="text-center">AttackPatterns</CTableHeaderCell>
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
                        defaultValue={location.state.source}
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
                      defaultValue={location.state.source}
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
                        defaultValue={location.state.malwares}
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
                      defaultValue={location.state.malwares}
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
                        defaultValue={location.state.vulnerabilities}
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
                      defaultValue={location.state.vulnerabilities}
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
                        defaultValue={location.state.locations}
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
                      defaultValue={location.state.locations}
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
                        defaultValue={location.state.threatActors}
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
                      defaultValue={location.state.threatActors}
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
                        defaultValue={location.state.identities}
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
                      defaultValue={location.state.identities}
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
                        defaultValue={location.state.tools}
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
                      defaultValue={location.state.tools}
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
                        defaultValue={location.state.infrastructure}
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
                      defaultValue={location.state.infrastructure}
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
                        defaultValue={location.state.campaigns}
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
                      defaultValue={location.state.campaigns}
                      id="a"
                      onChange={campaignsChangedHandler}
                    />
                  </CTableDataCell>
                )}
                {/*Attack Pattern*/}
                {location.state.attackPatterns ? (
                  isedit ? (
                    <CTableDataCell className="text-center">
                      {location.state.attackPatterns}
                    </CTableDataCell>
                  ) : (
                    <CTableDataCell className="text-center">
                      <CFormInput
                        ref={blah}
                        type="text"
                        defaultValue={location.state.attackPatterns}
                        id="a"
                        onChange={attackPatternsChangedHandler}
                      />
                    </CTableDataCell>
                  )
                ) : isedit ? (
                  console.log(isedit)
                ) : (
                  <CTableDataCell className="text-center">
                    <CFormInput
                      type="text"
                      defaultValue={location.state.attackPatterns}
                      id="a"
                      onChange={attackPatternsChangedHandler}
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
            <p>Threat Score: {threatScore}</p>
          </div>
          <div>
            STIX Visualizer
            {hash1 && mergedReportsBundles ? (
              <Visualizer graph1={hash1} graph2={objectMergedReport} />
            ) : (
              //   console.log('Hash1 is: ' + JSON.parse(JSON.stringify(hash1)))
              console.log('Visualizer not called')
            )}
          </div>
          <div>
            Related Links
            {/*  Adding related links */}
            {console.log(relatedReportData)}
            <CTable align="middle" className="mb-0 border" hover responsive>
              <CTableBody>
                {Object.values(relatedReportData).map((el) => (
                  <CTableRow key={el}>
                    <CTableDataCell>
                      <div>{el.rawText.slice(0, 100) + '...'}</div>
                    </CTableDataCell>

                    <CTableDataCell className="text-center">
                      <CButton
                        style={{ backgroundColor: 'blue', margin: '1%' }}
                        onClick={() =>
                          goToDetails(
                            el.hash,
                            el.source,
                            el.rawText,
                            el.malwares,
                            el.vulnerabilities,
                            el.locations,
                            el.threatActors,
                            el.identities,
                            el.tools,
                            el.infrastructure,
                            el.campaigns,
                            el.attackPatterns,
                          )
                        }
                      >
                        Details
                      </CButton>
                    </CTableDataCell>
                  </CTableRow>

                  // <CDropdownItem onClick={() => handleSelect(el)} key={el}>
                  //   Report: {i + 1}
                  // </CDropdownItem>
                ))}
              </CTableBody>
            </CTable>
          </div>
        </CCardBody>
      </CCard>
    </>
  )
}

export default Report_ap
