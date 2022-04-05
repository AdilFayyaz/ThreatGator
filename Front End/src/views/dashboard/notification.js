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
import { useLocation } from 'react-router-dom'

const NotificationsDetails = () => {
  const [FieldsData, SetFieldsData] = useState({})
  const get_notif = (event) => {
    fetch('http://127.0.0.1:8082/dataAnalysis/getResultOnHash', location.state.requestOptions)
      .then((res) => res.json())
      .then((data) => {
        SetFieldsData(data)
      })
  }
  const location = useLocation()

  useEffect(() => {
    get_notif()
    // const el = ref.current.parentNode.firstChild
    // const varColor = window.getComputedStyle(el).getPropertyValue('background-color')
    // setColor(varColor)
  }, [])

  return (
    <>
      <CCard className="mb-4">
        <CCardHeader>ThreatGator&apos;s Notification</CCardHeader>
        <CCardBody>
          <CTable align="middle" className="mb-0 border" hover responsive>
            <CTableHead color="light">
              <CTableRow>
                {FieldsData.source ? (
                  <CTableHeaderCell className="text-center">Source</CTableHeaderCell>
                ) : (
                  console.log('-')
                )}

                {FieldsData.rawText ? (
                  <CTableHeaderCell className="text-center">Raw Text</CTableHeaderCell>
                ) : (
                  console.log('-')
                )}

                {FieldsData.malware ? (
                  <CTableHeaderCell className="text-center">Malwares</CTableHeaderCell>
                ) : (
                  console.log('-')
                )}

                {FieldsData.vulnerabilities ? (
                  <CTableHeaderCell className="text-center">Vulnerabilities</CTableHeaderCell>
                ) : (
                  console.log('-')
                )}

                {FieldsData.location ? (
                  <CTableHeaderCell className="text-center">Locations</CTableHeaderCell>
                ) : (
                  console.log('-')
                )}

                {FieldsData.threatActors ? (
                  <CTableHeaderCell className="text-center">Threat Actors</CTableHeaderCell>
                ) : (
                  console.log('-')
                )}

                {FieldsData.identities ? (
                  <CTableHeaderCell className="text-center">Identities</CTableHeaderCell>
                ) : (
                  console.log('-')
                )}
                {FieldsData.tools ? (
                  <CTableHeaderCell className="text-center">Tools</CTableHeaderCell>
                ) : (
                  console.log('-')
                )}
                {FieldsData.infrastructure ? (
                  <CTableHeaderCell className="text-center">Infrastructures</CTableHeaderCell>
                ) : (
                  console.log('-')
                )}
                {FieldsData.indicator ? (
                  <CTableHeaderCell className="text-center">Indicators</CTableHeaderCell>
                ) : (
                  console.log('-')
                )}

                {FieldsData.campaigns ? (
                  <CTableHeaderCell className="text-center">Campaigns</CTableHeaderCell>
                ) : (
                  console.log('-')
                )}
              </CTableRow>
            </CTableHead>

            <CTableBody>
              <CTableRow>
                {FieldsData.source ? (
                  <CTableDataCell>{FieldsData.source}</CTableDataCell>
                ) : (
                  console.log('-')
                )}

                {FieldsData.rawText ? (
                  <CTableDataCell>{FieldsData.rawText}</CTableDataCell>
                ) : (
                  console.log('-')
                )}

                {FieldsData.malware ? (
                  <CTableDataCell>{FieldsData.malware}</CTableDataCell>
                ) : (
                  console.log('-')
                )}

                {FieldsData.vulnerabilities ? (
                  <CTableDataCell>{FieldsData.vulnerabilities}</CTableDataCell>
                ) : (
                  console.log('-')
                )}

                {FieldsData.location ? (
                  <CTableDataCell>{FieldsData.locations}</CTableDataCell>
                ) : (
                  console.log('-')
                )}

                {FieldsData.threatActors ? (
                  <CTableDataCell>{FieldsData.threatActors}</CTableDataCell>
                ) : (
                  console.log('-')
                )}

                {FieldsData.identities ? (
                  <CTableDataCell>{FieldsData.identities}</CTableDataCell>
                ) : (
                  console.log('-')
                )}
                {FieldsData.tools ? (
                  <CTableDataCell>{FieldsData.tools}</CTableDataCell>
                ) : (
                  console.log('-')
                )}
                {FieldsData.infrastructure ? (
                  <CTableDataCell>{FieldsData.infrastructure}</CTableDataCell>
                ) : (
                  console.log('-')
                )}
                {FieldsData.indicator ? (
                  <CTableDataCell>{FieldsData.indicator}</CTableDataCell>
                ) : (
                  console.log('-')
                )}

                {FieldsData.campaigns ? (
                  <CTableDataCell>{FieldsData.campaigns}</CTableDataCell>
                ) : (
                  console.log('-')
                )}
                {/**/}
              </CTableRow>
            </CTableBody>
          </CTable>
        </CCardBody>
      </CCard>
    </>
  )
}

export default NotificationsDetails
