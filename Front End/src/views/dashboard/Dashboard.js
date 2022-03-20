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
// import { Line ,Doughnut} from "react-chartjs-2";
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

import { useLocation, useParams } from 'react-router-dom'

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
  const random = (min, max) => {
    return Math.floor(Math.random() * (max - min + 1) + min)
  }
  const location = useLocation()
  const [DoughnutData, setDoughnutData] = useState({})
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
  // fetching data from data analysis service for doughnut graph e.g top 3 malwares
  let arr = []
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

        // SetHashNotif(Object.keys(data))
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

  const handleNotifClick = (event) => {
    event.preventDefault()
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
    fetch('http://127.0.0.1:8082/dataAnalysis/getResultOnHash', requestOptions)
      .then((res) => res.json())
      .then((data) => {
        SetFieldsData(data)
        openModal()
      })
  }
  // const openVisualizer = (event) => {
  //   navigate('/visualizer', { replace: true })
  //   window.location.reload(false)
  // }
  useEffect(() => {
    doughnut()
    notifications()
    hitsGraph()
    return () => {
      console.log('returning -xyzzz')
    }
  }, [location])
  return (
    <>
      {/* <CRow>
        <h1>Welcome {location.state.username}!</h1>
      </CRow> */}
      {/*<WidgetsDropdown />*/}
      {/*<CAlert color="primary">A simple primary alert—check it out!</CAlert>*/}
      {/*<CAlert color="secondary">A simple secondary alert—check it out!</CAlert>*/}
      {/*<CAlert color="success">A simple success alert—check it out!</CAlert>*/}
      {/*<CAlert color="danger">A simple danger alert—check it out!</CAlert>*/}
      <CCard className="overflow-auto mb-4" style={{ height: '15rem', padding: '2rem' }}>
        <CRow>
          <CCol sm={5}>
            <h4 id="notifications" className="card-title mb-0">
              Notifications
            </h4>
          </CCol>
        </CRow>
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
            <CCardFooter></CCardFooter>
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
            {/* first row*/}
          </CCol>
        </CCol>
      </CRow>
    </>
  )
}

export default Dashboard
