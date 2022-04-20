import React from 'react'
import { Link, Redirect, useHistory } from 'react-router-dom'
import Wave from 'react-wavify'
import {
  CButton,
  CCard,
  CCardBody,
  CCardGroup,
  CCol,
  CContainer,
  CForm,
  CFormInput,
  CInputGroup,
  CInputGroupText,
  CRow,
} from '@coreui/react'
import CIcon from '@coreui/icons-react'
import { cilLockLocked, cilUser } from '@coreui/icons'
import { lazy, useEffect, useState } from 'react'
import PropTypes from 'prop-types'

const Login = (props) => {
  const [username, setUsername] = useState({})
  const [password, setPassword] = useState({})
  let org = 'not'
  let admin = 'false'
  let userid = ' '
  const [data2, setData] = useState({})

  Login.propTypes = {
    parentCallback: PropTypes.string,
    parentCallbackIsadmin: PropTypes.string,
    parentCallbackUserid: PropTypes.string,
    //... other props you will use in this component
  }
  const sendData = () => {
    props.parentCallback(org)
    props.parentCallbackIsadmin(admin)
    props.parentCallbackUserid(userid)
  }

  //function decides whether the credetials are correct w.e.t the database(both admin's and user's)
  //navigates accordingly
  const history = useHistory()
  const authenticateUser = (event) => {
    event.preventDefault()
    const req = {
      username: username,
      password: password,
      organization: {},
    }
    console.log('here ' + JSON.stringify(req))
    const requestOptions = {
      method: 'POST',
      headers: { 'Content-Type': 'application/json' },
      body: JSON.stringify(req),
    }
    //uses the user management system
    fetch('http://127.0.0.1:8084/users/validateCredentials', requestOptions)
      .then((response) => response.json())
      .then(async (data) => {
        setData(JSON.stringify(data))

        console.log('--' + JSON.stringify(data))
        if (JSON.parse(JSON.stringify(data)).id != null) {
          console.log('is user')
          var x = await fetch('http://127.0.0.1:8084/users/getUserId', requestOptions)
            .then((response) => response.json())
            .then((data) => {
              // setData(JSON.stringify(data))
              console.log('got userid' + data)
              userid = data.toString()
            })
          org = JSON.parse(JSON.stringify(data)).id.toString()
          admin = 'false'

          sendData()
          history.push('/dashboard', {
            org_id: JSON.parse(JSON.stringify(data)).id,
          })
        } else {
          //Admin checking
          fetch('http://127.0.0.1:8084/Admin/validateAdminCredentials', requestOptions)
            .then((response) => response.json())
            .then((data) => {
              setData(JSON.stringify(data))

              console.log('--' + JSON.stringify(data))
              if (JSON.parse(JSON.stringify(data)).id != null) {
                org = JSON.parse(JSON.stringify(data)).id.toString()
                admin = 'true'
                sendData()
                history.push('/assetManagement', {
                  org_id: JSON.parse(JSON.stringify(data)).id,
                })
              } else {
                alert('Enter the correct credentials')
              }
            })
        }
      })
  }
  // handler functions for email and password

  const handleEmailChange = (event) => {
    event.preventDefault()
    setUsername(event.target.value)
  }

  const handlePasswordChange = (event) => {
    event.preventDefault()
    setPassword(event.target.value)
  }
  useEffect(() => {
    return () => {
      console.log('returning ')
    }
  }, [])
  return (
    <div className="bg-light min-vh-100 d-flex flex-row align-items-center">
      <CContainer>
        <CRow className="justify-content-center">
          <CCol md={8}>
            <CCardGroup>
              <CCard className="p-4">
                <CCardBody>
                  <CForm
                    onSubmit={
                      //check  email and password before any nabvigation
                      authenticateUser
                    }
                  >
                    <h1>Login</h1>
                    <p className="text-medium-emphasis">Sign In to your account</p>
                    <CInputGroup className="mb-3">
                      <CInputGroupText>
                        <CIcon icon={cilUser} />
                      </CInputGroupText>
                      <CFormInput
                        placeholder="Username"
                        autoComplete="username"
                        onChange={handleEmailChange}
                      />
                    </CInputGroup>
                    <CInputGroup className="mb-4">
                      <CInputGroupText>
                        <CIcon icon={cilLockLocked} />
                      </CInputGroupText>
                      <CFormInput
                        type="password"
                        placeholder="Password"
                        autoComplete="current-password"
                        onChange={handlePasswordChange}
                      />
                    </CInputGroup>
                    <CRow>
                      <CCol xs={6}>
                        <CButton
                          type="submit"
                          style={{ backgroundColor: '#162237' }}
                          className="px-4"
                        >
                          Login
                        </CButton>
                      </CCol>
                      <CCol xs={6} className="text-right">
                        <CButton
                          type="submit"
                          style={{
                            color: 'black',
                            backgroundColor: 'transparent',
                            border: 'transparent',
                          }}
                          className="px-0"
                        >
                          Forgot password?
                        </CButton>
                      </CCol>
                    </CRow>
                  </CForm>
                </CCardBody>
              </CCard>
              <CCard
                className="text-white py-5"
                style={{ width: '44%', backgroundColor: '#162237' }}
              >
                <CCardBody className="text-center" style={{ backgroundColor: '#162237' }}>
                  <div>
                    <img src={'./threatgator.png'} style={{ height: '75%', width: '75%' }} />
                  </div>
                </CCardBody>
              </CCard>
            </CCardGroup>
          </CCol>
        </CRow>
      </CContainer>
    </div>
  )
}

export default Login
