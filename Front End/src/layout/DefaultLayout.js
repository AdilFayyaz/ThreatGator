import React from 'react'
import { AppContent, AppSidebar, AppFooter, AppHeader } from '../components/index'
import PropTypes from 'prop-types'

const DefaultLayout = (props) => {
  DefaultLayout.propTypes = {
    dataFromParent: PropTypes.string,
    isadmin: PropTypes.string,
    userid: PropTypes.string,
    //... other props you will use in this component
  }
  // <h1>The data from parent is:{props.dataFromParent}</h1>
  var test = props.dataFromParent
  var isadmin2 = props.isadmin
  var userid2 = props.userid
  return (
    <div>
      <AppSidebar graph1={test} isadmin={isadmin2} userid={userid2} />
      <div className="wrapper d-flex flex-column min-vh-100 bg-light">
        <AppHeader />
        <div className="body flex-grow-1 px-3">
          <AppContent />
        </div>
        <AppFooter />
      </div>
    </div>
  )
}

export default DefaultLayout
