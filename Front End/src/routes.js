import React from 'react'

const Dashboard = React.lazy(() => import('./views/dashboard/Dashboard'))
const Colors = React.lazy(() => import('./views/theme/colors/Colors'))
const LatestReports = React.lazy(() => import('./views/sidebar/latest/LatestReports'))
const TEPReports = React.lazy(() => import('./views/sidebar/tep/TEPReports'))
const Visualizer = React.lazy(() => import('./views/visualizer/visualizer.js'))
const searchResults = React.lazy(() => import('./views/searchResults/searchResults.js'))
const Report = React.lazy(() => import('./views/sidebar/latest/Report.js'))
const LatestReports_a = React.lazy(() => import('./views/AdminPanel/LatestReports_a.js'))
const Report_ap = React.lazy(() => import('./views/AdminPanel/Report_ap.js'))
const notification = React.lazy(() => import('./views/dashboard/notification.js'))
const assetmanagement = React.lazy(() => import('./views/AdminPanel/AssetManagement.js'))
const bookmarks = React.lazy(() => import('./views/sidebar/bookmarks.js'))
const inference = React.lazy(() => import('./views/AdminPanel/Inference.js'))

const routes = [
  { path: '/', exact: true, name: 'Home' },
  { path: '/dashboard', name: 'Dashboard', component: Dashboard },
  { path: '/theme', name: 'Theme', component: Colors, exact: true },
  { path: '/latestReports', name: 'Latest Reports', component: LatestReports },
  { path: '/TEPReports', name: 'TEP Reports', component: TEPReports },
  { path: '/visualizer', name: 'Visualizer', component: Visualizer },
  { path: '/searchResults', name: 'SearchResults', component: searchResults },
  { path: '/Report', name: 'ReportDetails', component: Report },
  { path: '/latestReports_admin', name: 'Latest Reports Admin', component: LatestReports_a },
  { path: '/Report_admin', name: 'ReportDetails', component: Report_ap },
  { path: '/notification', name: 'notification', component: notification },
  { path: '/bookmarks', name: 'bookmarks', component: bookmarks },
  { path: '/inference', name: 'inference', component: inference },
  {
    path: '/assetManagement',
    name: 'assetManagement',
    component: assetmanagement,
    props: { title: '1' },
  },
]

export default routes
