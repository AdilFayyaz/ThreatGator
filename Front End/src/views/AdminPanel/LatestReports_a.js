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
  CToaster,
  CToast,
  CToastHeader,
  CToastBody,
  CModal,
  CModalHeader,
  CModalTitle,
  CModalBody,
  CModalFooter,
} from '@coreui/react'

import { useHistory, useLocation } from 'react-router-dom'

const LatestReports_a = () => {
  const location = useLocation()
  const [reportsData, SetReportsData] = useState({})
  const [toast, addToast] = useState(false)
  const [visible, setVisible] = useState(false)
  var confirm
  const [deleteData, setDeleteData] = useState()
  // function confirmationPrompt(el, e) {
  //   e.preventDefault()
  //   return (
  //
  //
  //     </>
  //   )
  // }
  // fetching data from data analysis service for reports
  const getReports = () => {
    // event.preventDefault()

    fetch('http://127.0.0.1:8082/dataAnalysis/getReports')
      .then((res) => res.json())
      .then((data) => {
        SetReportsData(data)
      })
    console.log('in function')
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
    history.push('/Report_admin', {
      hash: hash,
      source: source,
      rawText: rawtext,
      malwares: malwares,
      vulnerabilities: vulnerabilities,
      locations: locations,
      threatActors: threatActors,
      identities: identities,
      tools: tools,
      infrastructure: infrastructure,
      campaigns: campaigns,
      attackPatterns: attackPatterns,
    })
  }
  const history = useHistory()
  var isVulnerability = false
  var isMalware = false
  var isLocation = false
  var isThreatActor = false
  var isIdentitites = false
  var isTools = false
  var isInfra = false
  var isCampaign = false
  var isAttackPatterns = false
  function setTags(
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
    if (malwares) {
      isMalware = true
    }
    if (vulnerabilities) {
      isVulnerability = true
    }
    if (threatActors) {
      isThreatActor = true
    }
    if (locations) {
      isLocation = true
    }
    if (identities) {
      isIdentitites = true
    }
    if (tools) {
      isTools = true
    }
    if (infrastructure) {
      isInfra = true
    }
    if (campaigns) {
      isCampaign = true
    }
    if (attackPatterns) {
      isAttackPatterns = true
    }
  }

  function deleteRow(item) {
    // e.preventDefault()

    console.log('confirm' + confirm)
    if (confirm == 'yes') {
      const data2 = Object.values(reportsData).filter((i) => i.hash !== item.hash)
      // const data2 = reportsData.filter((i) => i.id !== item.id)
      SetReportsData(data2)
      deleteReport(item.hash)
    }
  }

  useEffect(() => {
    getReports()
    return () => {
      console.log('returning -xyzzz')
    }
  }, [location, confirm])

  function deleteReport(hash) {
    // /deleteFromElastic/{hash}
    fetch('http://127.0.0.1:8082/dataAnalysis/deleteFromElastic/' + hash)
      .then((res) => res.json())
      .then((data) => {
        console.log(data)
        addToast(true)
      })
  }

  return (
    <>
      {/* {getReports()} */}
      <CCard className="mb-4">
        <CCardHeader>ThreatGator&apos;s Latest Reports</CCardHeader>

        <CCardBody>
          <CTable align="middle" className="mb-0 border" hover responsive>
            <CTableHead color="light">
              <CTableRow>
                {/*<CTableHeaderCell className="text-center">*/}
                {/*<CIcon icon={cilPeople} />*/}
                {/*</CTableHeaderCell>*/}
                <CTableHeaderCell className="text-center">Raw text</CTableHeaderCell>
                <CTableHeaderCell className="text-center">tags</CTableHeaderCell>

                <CTableHeaderCell className="text-center"> </CTableHeaderCell>
                <CTableHeaderCell className="text-center"> </CTableHeaderCell>
                <CTableHeaderCell className="text-center"> </CTableHeaderCell>
              </CTableRow>
            </CTableHead>
            <CTableBody>
              {Object.values(reportsData).map((el) => (
                <CTableRow key={el} id={el}>
                  <CTableDataCell>
                    <div className="rawText">{el.rawText}</div>
                  </CTableDataCell>
                  <CTableDataCell className="tags">
                    {(isVulnerability = false)}
                    {(isMalware = false)}
                    {(isLocation = false)}
                    {(isThreatActor = false)}
                    {(isIdentitites = false)}
                    {(isTools = false)}
                    {(isInfra = false)}
                    {(isCampaign = false)}
                    {(isAttackPatterns = false)}
                    {setTags(
                      el.malwares,
                      el.vulnerabilities,
                      el.locations,
                      el.threatActors,
                      el.identities,
                      el.tools,
                      el.infrastructure,
                      el.campaigns,
                      el.attackPatterns,
                    )}
                    {/*vulnerability tag*/}
                    {isVulnerability ? (
                      <CBadge
                        className="rounded-pill"
                        style={{ margin: '1%', backgroundColor: '#85BFC5' }}
                      >
                        vulnerability
                      </CBadge>
                    ) : (
                      <div></div>
                    )}
                    {/*if malware then malware tag*/}
                    {isMalware ? (
                      <CBadge
                        className="rounded-pill"
                        style={{ margin: '1%', backgroundColor: '#ea8e8e' }}
                      >
                        malware
                      </CBadge>
                    ) : (
                      <div></div>
                    )}
                    {/*if locations tag*/}
                    {isLocation ? (
                      <CBadge
                        className="rounded-pill"
                        style={{ margin: '1%', backgroundColor: '#9A8BF3' }}
                      >
                        locations
                      </CBadge>
                    ) : (
                      <div></div>
                    )}
                    {/*if threat actor tag*/}
                    {isThreatActor ? (
                      <CBadge
                        className="rounded-pill"
                        style={{ margin: '1%', backgroundColor: '#FA714A' }}
                      >
                        threatActors
                      </CBadge>
                    ) : (
                      <div></div>
                    )}
                    {/*if identitites tag*/}
                    {isIdentitites ? (
                      <CBadge
                        className="rounded-pill"
                        style={{ margin: '1%', backgroundColor: '#6AC267' }}
                      >
                        identities
                      </CBadge>
                    ) : (
                      <div></div>
                    )}
                    {/*if tools tag*/}
                    {isTools ? (
                      <CBadge
                        className="rounded-pill"
                        style={{ margin: '1%', backgroundColor: '#D2B6B2' }}
                      >
                        tools
                      </CBadge>
                    ) : (
                      <div></div>
                    )}
                    {/*if infra tag*/}
                    {isInfra ? (
                      <CBadge
                        className="rounded-pill"
                        style={{ margin: '1%', backgroundColor: '#B6BF7A' }}
                      >
                        infra
                      </CBadge>
                    ) : (
                      <div></div>
                    )}
                    {/*if campaign tag*/}
                    {isCampaign ? (
                      <CBadge
                        className="rounded-pill"
                        style={{ margin: '1%', backgroundColor: '#F39E29' }}
                      >
                        campaigns
                      </CBadge>
                    ) : (
                      <div></div>
                    )}
                    {/*if campaign tag*/}
                    {isAttackPatterns ? (
                      <CBadge
                        className="rounded-pill"
                        style={{ margin: '1%', backgroundColor: '#BF749B' }}
                      >
                        Attack-Pattern
                      </CBadge>
                    ) : (
                      <div></div>
                    )}
                  </CTableDataCell>
                  <CTableDataCell className="text-center">
                    {/*{console.log('hashhh' + el.hash)}*/}
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
                  <CTableDataCell className="text-center">
                    {/*{console.log('hashhh' + el.hash)}*/}
                    <CButton
                      style={{ backgroundColor: 'blue', margin: '1%' }}
                      onClick={() => {
                        setVisible(!visible)
                        setDeleteData(el)
                      }}
                    >
                      Delete
                    </CButton>
                  </CTableDataCell>
                  <CTableDataCell className="text-center"></CTableDataCell>

                  {/*  <CTableRow key={el}>*/}
                  {/*    <CTableDataCell>{el.source}</CTableDataCell>*/}
                  {/*    <CTableDataCell>{el.malwares}</CTableDataCell>*/}
                  {/*    <CTableDataCell>{el.vulnerabilities}</CTableDataCell>*/}
                  {/*    <CTableDataCell>{el.locations}</CTableDataCell>*/}
                  {/*    <CTableDataCell>{el.threatActors}</CTableDataCell>*/}
                  {/*    /!*<CTableDataCell>{el.identities}</CTableDataCell>*!/*/}
                  {/*    /!*<CTableDataCell>{el.tools}</CTableDataCell>*!/*/}
                  {/*    /!*<CTableDataCell>{el.infrastructure}</CTableDataCell>*!/*/}
                  {/*    /!*<CTableDataCell>{el.campaigns}</CTableDataCell>*!/*/}
                  {/*  </CTableRow>*/}
                </CTableRow>
              ))}
            </CTableBody>
          </CTable>
        </CCardBody>
      </CCard>
      {/*  TOAST */}
      <CToaster placement="top-end">
        <CToast title="ThreatGator" autohide={true} visible={toast}>
          <CToastHeader close>
            <strong className="me-auto">ThreatGator</strong>
            <small>Latest</small>
          </CToastHeader>
          <CToastBody>A Report Has been Deleted</CToastBody>
        </CToast>
      </CToaster>
      <CModal visible={visible} onClose={() => setVisible(false)}>
        <CModalHeader onClose={() => setVisible(false)}>
          <CModalTitle>Confirmation</CModalTitle>
        </CModalHeader>
        <CModalBody>Do you want to delete this report?</CModalBody>
        <CModalFooter>
          <CButton
            color="secondary"
            onClick={() => {
              setVisible(false)
              confirm = 'no'
            }}
          >
            No
          </CButton>
          <CButton
            color="primary"
            onClick={() => {
              setVisible(false)
              confirm = 'yes'
              deleteRow(deleteData)
            }}
          >
            Yes
          </CButton>
        </CModalFooter>
      </CModal>
    </>
  )
}

export default LatestReports_a
