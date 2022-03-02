import React from 'react'
import CIcon from '@coreui/icons-react'
import {
  cilBell,
  cilCalculator,
  cilChartPie,
  cilCursor,
  cilDrop,
  cilGraph,
  cilNotes,
  cilPencil,
  cilPuzzle,
  cilShieldAlt,
  cilSpeedometer,
  cilStar,
} from '@coreui/icons'
import { CNavGroup, CNavItem, CNavTitle } from '@coreui/react'
import Searchbar from './components/Searchbar'

// fetching data from data analysis service for reports
const getReports = (event) => {
  event.preventDefault()
  console.log('hello threatgator')
  fetch('http://127.0.0.1:8082/dataAnalysis/getReports')
    .then((res) => res.json())
    .then((data) => {
      // SetReportsData(data)
      // openModalReport()
    })
}
const _nav = [
  {
    component: Searchbar,
    // name: 'Latest Reports',
    to: '/searchResults',
    // icon: <CIcon icon={cilNotes} customClassName="nav-icon" />,
  },
  {
    component: CNavItem,
    name: 'Dashboard',
    to: '/dashboard',
    icon: <CIcon icon={cilSpeedometer} customClassName="nav-icon" />,
    // badge: {
    //   color: 'info',
    //   text: 'NEW',
    // },
  },
  {
    component: CNavItem,
    name: 'Latest Reports',
    to: '/latestReports',
    icon: <CIcon icon={cilNotes} customClassName="nav-icon" />,
    // attributes: { onClick: { getReports } },
  },
  {
    component: CNavItem,
    name: 'TEP Reports',
    to: '/TEPReports',
    icon: <CIcon icon={cilShieldAlt} customClassName="nav-icon" />,
  },
  {
    component: CNavItem,
    name: 'Visualizer',
    to: '/visualizer',
    icon: <CIcon icon={cilGraph} customClassName="nav-icon" />,
  },
]

export default _nav
