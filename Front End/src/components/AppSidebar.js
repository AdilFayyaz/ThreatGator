import React from 'react'
import { useSelector, useDispatch } from 'react-redux'

import { CImage, CSidebar, CSidebarBrand, CSidebarNav, CSidebarToggler } from '@coreui/react'
import CIcon from '@coreui/icons-react'

import { AppSidebarNav } from './AppSidebarNav'

import SimpleBar from 'simplebar-react'
import 'simplebar/dist/simplebar.min.css'

// sidebar nav config
import navigation from '../_nav'
import PropTypes from 'prop-types'

const AppSidebar = (props) => {
  AppSidebar.propTypes = {
    graph1: PropTypes.string,
    isadmin: PropTypes.string,
    userid: PropTypes.string,
    //... other props you will use in this component
  }
  var bundle = props.graph1
  console.log(bundle)
  const dispatch = useDispatch()
  const unfoldable = useSelector((state) => state.sidebarUnfoldable)
  const sidebarShow = useSelector((state) => state.sidebarShow)

  return (
    <CSidebar
      position="fixed"
      unfoldable={unfoldable}
      visible={sidebarShow}
      onVisibleChange={(visible) => {
        dispatch({ type: 'set', sidebarShow: visible })
      }}
    >
      <CSidebarBrand className="d-none d-md-flex" to="/dashboard" style={{ height: '45rem' }}>
        {/*<CIcon className="sidebar-brand-full" icon={logoNegative} height={35} />*/}
        <CImage
          src="./threatgator.png"
          height={70}
          style={{ marginTop: '5%', marginBottom: '5%' }}
        />
        {/*<CIcon className="sidebar-brand-narrow" icon={sygnet} height={35} />*/}
      </CSidebarBrand>
      <CSidebarNav>
        <SimpleBar>
          <AppSidebarNav
            items={navigation}
            graph1={bundle}
            isadmin={props.isadmin}
            userid={props.userid}
          />
        </SimpleBar>
      </CSidebarNav>
      <CSidebarToggler
        className="d-none d-lg-flex"
        onClick={() => dispatch({ type: 'set', sidebarUnfoldable: !unfoldable })}
      />
    </CSidebar>
  )
}

export default React.memo(AppSidebar)
