import React, { lazy, useEffect, useState } from 'react'
import {
  CAlert,
  CAvatar,
  CButton,
  CButtonGroup,
  CCard,
  CCardBody,
  CCardFooter,
  CCardHeader,
  CCol,
  CProgress,
  CRow,
  CTable,
  CTableBody,
  CTableDataCell,
  CTableHead,
  CTableHeaderCell,
  CTableRow,
  CToast,
  CToastBody,
  CToastClose,
  CToaster,
  CToastHeader,
} from '@coreui/react'
import { CChartDoughnut, CChartLine } from '@coreui/react-chartjs'
import { countries, lookup } from 'country-data-list'
import WorldMap from 'react-svg-worldmap'
import { getStyle, hexToRgba } from '@coreui/utils'
import CIcon from '@coreui/icons-react'
import {
  cibCcAmex,
  cibCcApplePay,
  cibCcMastercard,
  cibCcPaypal,
  cibCcStripe,
  cibCcVisa,
  cibGoogle,
  cibFacebook,
  cibLinkedin,
  cifBr,
  cifEs,
  cifFr,
  cifIn,
  cifPl,
  cifUs,
  cibTwitter,
  cilCloudDownload,
  cilPeople,
  cilUser,
  cilUserFemale,
} from '@coreui/icons'

import { Redirect, useHistory, useLocation, useParams } from 'react-router-dom'
import { sha256 } from 'js-sha256'

const WidgetsDropdown = lazy(() => import('../widgets/WidgetsDropdown.js'))
const WidgetsBrand = lazy(() => import('../widgets/WidgetsBrand.js'))

// returns notification tiles by writing string x to it
function getNotificationCards(x) {
  return (
    <CButton
      style={{ width: '40rem', height: '3rem', backgroundColor: '#1c2b45', borderRadius: '3rem' }}
      value={x}
    >
      {x}
    </CButton>
  )
}

const Dashboard = (props) => {
  function capitalizeFirstLetter(str) {
    const c = str.replace(/^./, str[0].toUpperCase())
    return c
  }
  const random = (min, max) => {
    return Math.floor(Math.random() * (max - min + 1) + min)
  }
  const location = useLocation()
  const [DoughnutData, setDoughnutData] = useState({})
  const [LocationData, setLocationData] = useState([])
  const [WeekData, setWeekData] = useState({})
  const [NotificationsData, setNotificationsData] = useState([])
  const [SearchKeyword, SetSearchKeyword] = useState('')
  const [KeywordResult, SetKeywordResult] = useState({})
  const [HashNotif, SetHashNotif] = useState([])
  const [FieldsData, SetFieldsData] = useState({})
  const [vulnerabilitiesData, SetVulnerabilitiesData] = useState({})
  const [reportsData, SetReportsData] = useState({})
  const [exchangeData, SetExchangeData] = useState({})
  const [isOpen, SetIsOpen] = useState()
  const [isOpenSearch, SetIsOpenSearch] = useState()
  const [isOpenVuln, SetIsOpenVuln] = useState()
  const [isOpenReport, SetIsOpenReport] = useState()
  const [isOpenExchange, SetIsOpenExchange] = useState()

  const openModal = () => SetIsOpen(true)
  const closeModal = () => SetIsOpen(false)

  const openModalSearch = () => SetIsOpenSearch(true)
  const closeModalSearch = () => SetIsOpenSearch(false)

  const openModalVuln = () => SetIsOpenVuln(true)
  const closeModalVuln = () => SetIsOpenVuln(false)

  const openModalReport = () => SetIsOpenReport(true)
  const closeModalReport = () => SetIsOpenReport(false)

  const openModalExchange = () => SetIsOpenExchange(true)
  const closeModalExchange = () => SetIsOpenExchange(false)
  //
  const objects = []
  // fetching data from data analysis service for doughnut graph e.g top 3 malwares
  let arr = []
  const getLocations = (event) => {
    let loc = []
    var lookup = require('country-data-list').lookup
    // event.preventDefault()

    fetch('http://127.0.0.1:8082/dataAnalysis/getLocations')
      .then((res) => res.json())
      .then((data) => {
        loc = Object.keys(data)

        for (var i = 0; i < loc.length; i++) {
          var countryFullName = capitalizeFirstLetter(loc[i])
          if (lookup.countries({ name: countryFullName })[0] != undefined) {
            console.log(lookup.countries({ name: countryFullName })[0].alpha2)
            objects.push({
              country: lookup.countries({ name: countryFullName })[0].alpha2,
              value: ' ',
            })
          }
        }
      })

    // countryFullName = capitalizeFirstLetter('pakistan')
    // objects.push({ country: lookup.countries({ name: countryFullName })[0].alpha2, value: ' ' })

    setLocationData(objects)
  }

  const doughnut = () => {
    let topMalwares = []
    let topMalwaresData = []

    fetch('http://127.0.0.1:8082/dataAnalysis/getTopMalwares/3')
      .then((res) => res.json())
      .then((data) => {
        topMalwares = Object.keys(data)
        topMalwaresData = Object.values(data)

        setDoughnutData({
          labels: topMalwares,
          datasets: [
            {
              label: 'statistics',
              data: topMalwaresData,
              borderColor: ['rgba(255,206,86,0.2)'],
              backgroundColor: [
                'rgba(232,99,132,1)',
                'rgba(232,211,6,1)',
                'rgba(54,162,235,1)',
                'rgba(255,159,64,1)',
              ],
              pointBackgroundColor: 'rgba(255,206,86,0.2)',
            },
          ],
        })
      })
  }
  // fetching data from data analysis service for line graph e.g week hits

  const hitsGraph = () => {
    let hits = []

    fetch('http://127.0.0.1:8082/dataAnalysis/getWeekHits')
      .then((res) => res.json())
      .then((data) => {
        hits = data
        console.log(hits)
        setWeekData({
          labels: ['Monday', 'Tuesday', 'Wednesday', 'Thursday', 'Friday', 'Saturday', 'Sunday'],
          datasets: [
            {
              label: 'Number of Hits',
              fill: false,
              lineTension: 0.1,
              backgroundColor: 'rgba(75,192,192,0.4)',
              borderColor: 'rgba(75,192,192,1)',
              borderCapStyle: 'butt',
              borderDash: [],
              borderDashOffset: 0.0,
              borderJoinStyle: 'miter',
              pointBorderColor: 'rgba(75,192,192,1)',
              pointBackgroundColor: '#fff',
              pointBorderWidth: 1,
              pointHoverRadius: 5,
              pointHoverBackgroundColor: 'rgba(75,192,192,1)',
              pointHoverBorderColor: 'rgba(220,220,220,1)',
              pointHoverBorderWidth: 2,
              pointRadius: 1,
              pointHitRadius: 10,
              data: hits,
            },
          ],
        })
      })
  }
  // fetching data from data analysis service for notifications

  const notifications = () => {
    let alerts = []
    let keys = []
    fetch('http://127.0.0.1:8082/dataAnalysis/getNotifications/5')
      .then((res) => res.json())
      .then((data) => {
        alerts = Object.values(data)
        keys = Object.keys(data)
        SetHashNotif(keys)
        setNotificationsData(alerts)

        SetHashNotif(Object.keys(data))
      })
  }
  // fetching data from data analysis service for vulnerability button

  const getVulnerabilities = (event) => {
    event.preventDefault()
    fetch('http://127.0.0.1:8082/dataAnalysis/getVulnerabilities')
      .then((res) => res.json())
      .then((data) => {
        SetVulnerabilitiesData(data)
        openModalVuln()
      })
  }
  // fetching data from data analysis service for reports
  const getReports = (event) => {
    event.preventDefault()
    fetch('http://127.0.0.1:8082/dataAnalysis/getReports')
      .then((res) => res.json())
      .then((data) => {
        SetReportsData(data)
        openModalReport()
      })
  }
  // fetching data from data analysis service for threat exchange platform's data

  const getTEPReports = (event) => {
    event.preventDefault()
    fetch('http://127.0.0.1:8082/dataAnalysis/getThreatExchangeData')
      .then((res) => res.json())
      .then((data) => {
        console.log(data)
        SetExchangeData(data)
        // console.log(Object.keys(data))
        openModalExchange()
      })
  }
  const history = useHistory()
  const handleNotifClick = (event) => {
    // event.preventDefault()
    // console.log(event.target.value)
    var count = 0
    var preHash
    NotificationsData.forEach((m) => {
      count += 1
      if (m == event.target.value) {
        preHash = count
      }
    })
    var count2 = 0
    var _hash = ''
    HashNotif.forEach((x) => {
      count2 += 1
      if (preHash == count2) {
        _hash = x
      }
    })
    // Now that we have the hash
    // we can call the fetch req to get all values
    let alerts2 = []
    let keys2 = []
    const requestOptions = {
      method: 'POST',
      headers: { 'Content-Type': 'application/json' },
      body: _hash,
    }
    // <Redirect to={ pathname: "/notification"}></Redirect>
    history.push('/notification', { requestOptions: requestOptions })
  }
  // const openVisualizer = (event) => {
  //   navigate('/visualizer', { replace: true })
  //   window.location.reload(false)
  // }
  function getStrHash(str) {
    var hash = 0
    for (var i = 0; i < str.length; i++) {
      var code = str.charCodeAt(i)
      hash = (hash << 5) - hash + code
      hash = hash & hash // Convert to 32bit integer
    }
    return hash
  }
  useEffect(() => {
    doughnut()
    notifications()
    hitsGraph()
    getLocations()
    return () => {
      console.log('returning -xyzzz')
    }
  }, [location])
  return (
    <>
      <CCard className="mb-4" style={{ backgroundColor: 'transparent', border: 'transparent' }}>
        {/* <CRow>
        <h1>Welcome {location.state.username}!</h1>
      </CRow> */}

        <CCard className="mb-4" style={{ height: '15rem', padding: '2rem' }}>
          <CRow>
            <CCol sm={5}>
              <h4 id="notifications" className="card-title mb-0">
                Notifications
              </h4>
            </CCol>
          </CRow>
          <CRow className="overflow-auto">
            {/*<CButton color="primary" style={{ marginBottom: '1rem' }}>*/}
            {/*  Button*/}
            {/*  some notification*/}
            {/*</CButton>*/}
            {NotificationsData.map((notif) => (
              <CRow key={notif}>
                <CButton
                  // color="primary"
                  onClick={handleNotifClick}
                  style={{
                    marginBottom: '1rem',
                    width: '80rem',
                    height: '3rem',
                    backgroundColor: '#1c2b45',
                    borderRadius: '3rem',
                  }}
                  value={notif}
                >
                  {notif}
                </CButton>
              </CRow>

              // return getNotificationCards(notif).bind(this);
            ))}
          </CRow>
        </CCard>
        {/*  charts start here*/}
        <CRow>
          <CCol sm={7} className="d-none d-md-block">
            <CCard className="mb-4">
              <CCardBody>
                <CRow>
                  <CCol sm={5}>
                    <h4 id="hits" className="card-title mb-0">
                      Number of Hits
                    </h4>
                    <div className="small text-medium-emphasis">Weekly</div>
                  </CCol>
                  <CCol sm={7} className="d-none d-md-block"></CCol>
                </CRow>
                <CChartLine
                  style={{ height: '300px', marginTop: '40px' }}
                  // WeekData
                  data={WeekData}
                  options={{
                    maintainAspectRatio: false,
                    plugins: {
                      legend: {
                        display: false,
                      },
                    },
                    scales: {
                      x: {
                        grid: {
                          drawOnChartArea: false,
                        },
                      },
                      y: {
                        ticks: {
                          beginAtZero: true,
                          maxTicksLimit: 5,
                          stepSize: Math.ceil(250 / 5),
                          max: 250,
                        },
                      },
                    },
                    elements: {
                      line: {
                        tension: 0.4,
                      },
                      point: {
                        radius: 0,
                        hitRadius: 10,
                        hoverRadius: 4,
                        hoverBorderWidth: 3,
                      },
                    },
                  }}
                />
              </CCardBody>
              {/*<CCardFooter></CCardFooter>*/}
            </CCard>
          </CCol>
          <CCol>
            <CCol xs>
              <CCard className="mb-4" style={{ position: 'absolute' }}>
                <CCardBody>
                  {/* {donughnut} */}
                  <CCol sm={5}>
                    <h4 id="topmalwares" className="card-title mb-0">
                      Top Malwares
                    </h4>
                  </CCol>
                  {/*replace data1 with DoughnutData*/}
                  {/* <Doughnut data={DoughnutData}  /> */}
                  <CChartDoughnut data={DoughnutData} />
                </CCardBody>
              </CCard>
            </CCol>
          </CCol>
        </CRow>

        <CCard className="mb-4" style={{ position: 'relative' }}>
          <CCardBody>
            <CCol sm={5}>
              <h4 id="High Activity Countries" className="card-title mb-0">
                High Activity Countries
              </h4>
            </CCol>
            <div style={{ marginLeft: '15%' }}>
              <WorldMap
                strokeOpacity="3"
                borderColor="black"
                backgroundColor="#white"
                color="#3C4B64"
                size="xl"
                data={LocationData}
              />
            </div>
          </CCardBody>
        </CCard>
      </CCard>
    </>
  )
}

export default Dashboard
