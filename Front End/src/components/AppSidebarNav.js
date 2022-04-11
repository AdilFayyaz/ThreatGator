import React from 'react'
import { NavLink, useLocation } from 'react-router-dom'
import PropTypes from 'prop-types'

import { CBadge } from '@coreui/react'

export const AppSidebarNav = ({ items, graph1 }) => {
  // AppSidebarNav.propTypes = {
  //   graph1: PropTypes.string,
  //   //... other props you will use in this component
  // }
  var bundle = graph1
  console.log(bundle)
  var location = useLocation()
  const navLink = (name, icon, badge) => {
    return (
      <>
        {console.log('navlink')}
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

    location.org_id = bundle.toString()
    return (
      <Component
        {...(rest.to &&
          !rest.items && {
            component: NavLink,
            activeClassName: 'active',
            graph1: bundle,
            // props: bundle,
          })}
        key={index}
        {...rest}
        // style={{ backgroundColor: 'pink', border: 'black 2px solid' }}
        graph1={bundle}
      >
        {console.log('rest ' + JSON.stringify(rest) + JSON.stringify(rest.items))}
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
}
