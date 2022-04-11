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

var _nav = [
  {
    component: Searchbar,
    // name: 'Latest Reports',
    to: { pathname: '/searchResults', org_id: 'someTitle' },
    // icon: <CIcon icon={cilNotes} customClassName="nav-icon" />,
  },
  {
    component: CNavItem,
    name: 'Dashboard',
    to: { pathname: '/dashboard', org_id: 'someTitle' },
    icon: <CIcon icon={cilSpeedometer} customClassName="nav-icon" />,
    // badge: {
    //   color: 'info',
    //   text: 'NEW',
    // },
  },
  {
    component: CNavItem,
    name: 'Latest Reports',
    to: { pathname: '/latestReports', org_id: 'someTitle' },
    icon: <CIcon icon={cilNotes} customClassName="nav-icon" />,
    graph1: '--',
  },
  {
    component: CNavItem,
    name: 'TEP Reports',
    to: { pathname: '/TEPReports', org_id: 'someTitle' },
    icon: <CIcon icon={cilShieldAlt} customClassName="nav-icon" />,
  },
  {
    component: CNavItem,
    name: 'Visualizer',
    to: { pathname: '/visualizer', org_id: 'someTitle' },
    icon: <CIcon icon={cilGraph} customClassName="nav-icon" />,
  },
]

export default _nav
