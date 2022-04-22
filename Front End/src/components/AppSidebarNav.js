import React from 'react'
import { NavLink, useLocation } from 'react-router-dom'
import PropTypes from 'prop-types'

import { CBadge } from '@coreui/react'
import { cilDiamond, cilHeart } from '@coreui/icons'
import CIcon from '@coreui/icons-react'

export const AppSidebarNav = ({ items, graph1, isadmin, userid }) => {
  function changePath(rest, icon, name) {
    if (rest.to.pathname == '/latestReports' && isadmin == 'true') {
      rest.to.pathname = '/latestReports_admin'
      console.log('path changed for admin')
    }
    // } else if (rest.to.pathname == '/bookmarks' && isadmin == 'true') {
    //   console.log('$$' + JSON.stringify(icon.props.icon), '##', name)
    //   rest.to.pathname = '/assetManagement'
    //   name = 'Asset Management'
    //   icon = <CIcon icon={cilDiamond} customClassName="nav-icon" />
    //   console.log(
    //     'path changed for admin2',
    //     '>>>>' + JSON.stringify(icon.props.icon),
    //     '-----',
    //     name,
    //   )
    // }
    return
  }
  // AppSidebarNav.propTypes = {
  //   graph1: PropTypes.string,
  //   //... other props you will use in this component
  // }
  var bundle = graph1
  var isadmin2 = isadmin
  var userid2 = userid
  console.log('user id! ', userid)
  var location = useLocation()
  const navLink = (name, icon, badge) => {
    return (
      <>
        {console.log('admin check', isadmin2)}
        {icon && icon}
        {name && name}
        {badge && (
          <CBadge color={badge.color} className="ms-auto">
            {badge.text}
          </CBadge>
        )}
      </>
    )
  }

  const navItem = (item, index) => {
    let { component, name, badge, icon, ...rest } = item
    let Component = component

    changePath(rest, icon, name)
    console.log('nav bar^', location.org_id, location.isadmin, location.userid)
    if (
      (rest.to.pathname == '/bookmarks' || rest.to.pathname == '/assetManagement') &&
      isadmin == 'true'
    ) {
      console.log('$$' + JSON.stringify(icon.props.icon), '##', name)
      rest.to.pathname = '/assetManagement'
      name = 'Asset Management'
      icon = <CIcon icon={cilDiamond} customClassName="nav-icon" />
    }

    location.org_id = bundle.toString()
    location.isadmin = isadmin2.toString()
    location.userid = userid2.toString()
    return (
      <Component
        {...(rest.to &&
          !rest.items && {
            component: NavLink,
            activeClassName: 'active',
            graph1: bundle,
            isadmin: isadmin2,
            userid: userid2,
            // props: bundle,
          })}
        key={index}
        {...rest}
        // style={{ backgroundColor: 'pink', border: 'black 2px solid' }}
        graph1={bundle}
        isadmin={isadmin2}
        userid={userid2}
      >
        {navLink(name, icon, badge)}
      </Component>
    )
  }
  const navGroup = (item, index) => {
    // console.log('navItem')

    // console.log('thisssssssssss', JSON.stringify(rest.to.state) + bundle)
    let { component, name, icon, to, ...rest } = item
    // rest.to.state = bundle.toString()
    let Component = component
    console.log('navgrp')
    {
      changePath(rest, icon, name)
      console.log('after', 'after' + JSON.stringify(icon.props.icon), 'after', name)
    }
    return (
      <Component
        idx={String(index)}
        key={index}
        toggler={navLink(name, icon)}
        visible={location.pathname.startsWith(to)}
        {...rest}
      >
        {item.items?.map((item, index) =>
          item.items ? navGroup(item, index) : navItem(item, index),
        )}
      </Component>
    )
  }

  return (
    <React.Fragment>
      {items &&
        items.map((item, index) => (item.items ? navGroup(item, index) : navItem(item, index)))}
    </React.Fragment>
  )
}

AppSidebarNav.propTypes = {
  items: PropTypes.arrayOf(PropTypes.any).isRequired,
  graph1: PropTypes.string,
  isadmin: PropTypes.string,
  userid: PropTypes.string,
}
