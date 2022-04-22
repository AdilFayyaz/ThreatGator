import React from 'react'
import CIcon from '@coreui/icons-react'
import {
  cilBell,
  cilBookmark,
  cilCalculator,
  cilChartPie,
  cilCursor,
  cilDrop,
  cilGraph,
  cilHeart,
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
    to: { pathname: '/searchResults', org_id: 'someTitle', userid: 'someid', isadmin: 'false' },
    // icon: <CIcon icon={cilNotes} customClassName="nav-icon" />,
  },
  {
    component: CNavItem,
    name: 'Dashboard',
    to: { pathname: '/dashboard', org_id: 'someTitle', userid: 'someid', isadmin: 'false' },
    icon: <CIcon icon={cilSpeedometer} customClassName="nav-icon" />,
    // badge: {
    //   color: 'info',
    //   text: 'NEW',
    // },
  },
  {
    component: CNavItem,
    name: 'Latest Reports',
    to: { pathname: '/latestReports', org_id: 'someTitle', userid: 'someid', isadmin: 'false' },
    icon: <CIcon icon={cilNotes} customClassName="nav-icon" />,
    graph1: '--',
  },
  // {
  //   component: CNavItem,
  //   name: 'TEP Reports',
  //   to: { pathname: '/TEPReports', org_id: 'someTitle', userid: 'someid', isadmin: 'false' },
  //   icon: <CIcon icon={cilShieldAlt} customClassName="nav-icon" />,
  // },
  // {
  //   component: CNavItem,
  //   name: 'Visualizer',
  //   to: { pathname: '/visualizer', org_id: 'someTitle', userid: 'someid', isadmin: 'false' },
  //   icon: <CIcon icon={cilGraph} customClassName="nav-icon" />,
  // },
  {
    component: CNavItem,
    name: 'Favourites',
    to: { pathname: '/bookmarks', org_id: 'someTitle', userid: 'someid', isadmin: 'false' },
    icon: <CIcon icon={cilHeart} customClassName="nav-icon" />,
  },
]

export default _nav
