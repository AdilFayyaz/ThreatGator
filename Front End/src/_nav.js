import React from 'react'
import CIcon from '@coreui/icons-react'
import {
  cilBell,
  cilBookmark,
  cilCalculator,
  cilChartPie,
  cilCursor,
  cilDrop,
  cilGlobeAlt,
  cilGraph,
  cilHeart,
  cilNotes,
  cilPencil,
  cilPuzzle,
  cilSearch,
  cilShieldAlt,
  cilSpeedometer,
  cilStar,
} from '@coreui/icons'
import { CNavGroup, CNavItem, CNavTitle } from '@coreui/react'
import Searchbar from './components/Searchbar'

var _nav = [
  {
    component: Searchbar,
    name: 'Search',
    to: { pathname: '/searchResults', org_id: 'someTitle', userid: 'someid', isadmin: 'false' },
    icon: <CIcon icon={cilSearch} customClassName="nav-icon" />,
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
  {
    component: CNavItem,
    name: 'Inference',
    to: { pathname: '/inference', org_id: 'someTitle', userid: 'someid', isadmin: 'false' },
    icon: <CIcon icon={cilGlobeAlt} customClassName="nav-icon" />,
  },
]

export default _nav
