import { CButton, CForm, CFormInput, CFormLabel, CImage } from '@coreui/react'
import React, { useState } from 'react'
import * as PropTypes from 'prop-types'
import { useHistory } from 'react-router-dom'
// fetching data from data analysis service for any keyword's search result

const Searchbar = () => {
  const history = useHistory()
  //hooks to cater data state changes
  const [DoughnutData, setDoughnutData] = useState({})
  const [WeekData, setWeekData] = useState({})
  const [NotificationsData, setNotificationsData] = useState([])
  const [SearchKeyword, SetSearchKeyword] = useState('')
  const [KeywordResult, SetKeywordResult] = useState({})
  const [HashNotif, SetHashNotif] = useState([])
  const [FieldsData, SetFieldsData] = useState({})
  const [vulnerabilitiesData, SetVulnerabilitiesData] = useState({})
  const [reportsData, SetReportsData] = useState({})
  const [exchangeData, SetExchangeData] = useState({})
  const [isOpen, SetIsOpen] = useState()
  const [isOpenSearch, SetIsOpenSearch] = useState()
  const [isOpenVuln, SetIsOpenVuln] = useState()
  const [isOpenReport, SetIsOpenReport] = useState()
  const [isOpenExchange, SetIsOpenExchange] = useState()
  const openModal = () => SetIsOpen(true)
  const closeModal = () => SetIsOpen(false)
  const openModalSearch = () => SetIsOpenSearch(true)
  const closeModalSearch = () => SetIsOpenSearch(false)

  const openModalVuln = () => SetIsOpenVuln(true)
  const closeModalVuln = () => SetIsOpenVuln(false)

  const openModalReport = () => SetIsOpenReport(true)
  const closeModalReport = () => SetIsOpenReport(false)

  const openModalExchange = () => SetIsOpenExchange(true)
  const closeModalExchange = () => SetIsOpenExchange(false)

  const getSearchResults = (event) => {
    event.preventDefault()

    // let reports = []
    // const requestOptions = {
    //   method: 'POST',
    //   headers: { 'Content-Type': 'application/json' },
    //   body: SearchKeyword,
    // }
    // fetch('http://127.0.0.1:8082/dataAnalysis/getSearchResults', requestOptions)
    //   .then((res) => res.json())
    //   .then((data) => {
    //     SetKeywordResult(data)
    //     // console.log(data)
    //     // openModalSearch()
    //     history.push('/searchResults', { keyword: SearchKeyword, data: KeywordResult })
    //   })
    var myHeaders = new Headers()
    myHeaders.append('Content-Type', 'application/json')

    var raw = JSON.stringify({
      keyword: SearchKeyword,
    })

    var requestOptions = {
      method: 'POST',
      headers: myHeaders,
      body: raw,
      redirect: 'follow',
    }

    fetch('http://127.0.0.1:8082/dataAnalysis/getSearchResults', requestOptions)
      .then((response) => response.json())
      .then((result) => {
        console.log('search results', result)
        SetKeywordResult(result)

        history.push('/searchResults', { keyword: SearchKeyword, data: result })
      })
      .catch((error) => console.log('error', error))
  }

  //handlers for input fields
  const handleSearchChange = (event) => {
    event.preventDefault()
    SetSearchKeyword(event.target.value)
    console.log(SearchKeyword)
  }
  return (
    <CForm onSubmit={getSearchResults}>
      <div className="d-flex flex-row" style={{ width: '15rem', marginLeft: '0.5rem' }}>
        {/*<CFormLabel htmlFor="search">sear</CFormLabel>*/}
        <CFormInput
          type="search"
          id="Keyword"
          style={{ borderRadius: '50px', backgroundColor: 'whitesmoke', opacity: '70%' }}
          onChange={handleSearchChange}
        />
        {/*<CFormText id="emailHelp">We'll never share your email with anyone else.</CFormText>*/}
        <CButton
          type="submit"
          style={{
            marginLeft: 0,
            borderRadius: '50px',
            // width: '1.5rem',
            // height: '1.5rem',
            paddingRight: '0px',
            backgroundColor: 'transparent',
            borderColor: 'transparent',
          }}
        >
          <CImage fluid src={'./search.png'} height={35} width={35} roundedCircle />
        </CButton>
      </div>
    </CForm>
  )
}
export default React.memo(Searchbar)
