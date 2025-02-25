import React, { Component } from 'react'
import { BrowserRouter, HashRouter, Route, Switch } from 'react-router-dom'
import './scss/style.scss'
//
// Citation: The UI for threat gator has been developed with the help of CoreUI Admin Panel Template
//https://coreui.io/react/
//
const loading = (
  <div className="pt-3 text-center">
    <div className="sk-spinner sk-spinner-pulse"></div>
  </div>
)

// Containers
const DefaultLayout = React.lazy(() => import('./layout/DefaultLayout'))

// Pages
const Login = React.lazy(() => import('./views/pages/login/Login'))
const Register = React.lazy(() => import('./views/pages/register/Register'))
const Page404 = React.lazy(() => import('./views/pages/page404/Page404'))
const Page500 = React.lazy(() => import('./views/pages/page500/Page500'))

class App extends Component {
  constructor() {
    super()
    this.state = JSON.parse(window.localStorage.getItem('state')) || {
      message: '1',
      isadmin: 'false',
      userid: '1',
    }
  }
  setState(state, callback) {
    window.localStorage.setItem('state', JSON.stringify(state))
    super.setState(state, callback)
  }

  // state = { message: '1', isadmin: 'false', userid: '1' }
  // state = { message: '1', isadmin: 'false' }
  callbackFunction = (childData) => {
    this.setState({ ...this.state, message: childData })
  }
  callbackFunction2 = (childData2) => {
    this.setState({ ...this.state, isadmin: childData2 })
  }
  callbackFunction3 = (childData3) => {
    this.setState({ ...this.state, userid: childData3 })
  }
  render() {
    return (
      <BrowserRouter>
        <React.Suspense fallback={loading}>
          <Switch>
            <Route
              exact
              path="/"
              name="Login Page"
              render={(props) => (
                <Login
                  {...props}
                  parentCallback={this.callbackFunction}
                  parentCallbackIsadmin={this.callbackFunction2}
                  parentCallbackUserid={this.callbackFunction3}
                />
              )}
            />
            <Route
              exact
              path="/register"
              name="Register Page"
              render={(props) => <Register {...props} />}
            />
            {console.log('msg ' + this.state.message, ' ', this.state.userid)}
            <Route exact path="/404" name="Page 404" render={(props) => <Page404 {...props} />} />
            <Route exact path="/500" name="Page 500" render={(props) => <Page500 {...props} />} />
            <Route
              path="/"
              name="Home"
              render={(props) => (
                <DefaultLayout
                  dataFromParent={this.state.message}
                  isadmin={this.state.isadmin}
                  userid={this.state.userid}
                  {...props}
                />
              )}
            />
          </Switch>
        </React.Suspense>
      </BrowserRouter>
    )
  }
}

export default App
