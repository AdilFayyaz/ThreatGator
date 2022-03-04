import {
  CCard,
  CCardBody,
  CCardHeader,
  CCardTitle,
  CTable,
  CTableBody,
  CTableDataCell,
  CTableHead,
  CTableHeaderCell,
  CTableRow,
} from '@coreui/react'
import React, { lazy, useEffect, useState } from 'react'
import LatestReports from '../sidebar/latest/LatestReports'
import { useLocation } from 'react-router-dom'

{
  /* Search Results */
}
// <Modal show={isOpenSearch} onHide={closeModalSearch}
//        fullscreen={true}
//        aria-labelledby="contained-modal-title-vcenter"
//        centered

// >

const SearchResult = () => {
  const [SearchKeyword, SetSearchKeyword] = useState('')
  const [KeywordResult, SetKeywordResult] = useState({})
  // fetching data from data analysis service for any keyword's search result

  const getSearchResults = (event) => {
    event.preventDefault()
    let reports = []
    const requestOptions = {
      method: 'POST',
      headers: { 'Content-Type': 'application/json' },
      body: SearchKeyword,
    }
    fetch('http://127.0.0.1:8082/dataAnalysis/getSearchResults', requestOptions)
      .then((res) => res.json())
      .then((data) => {
        SetKeywordResult(data)
      })
  }
  //handlers for input fields
  const handleSearchChange = (event) => {
    event.preventDefault()
    SetSearchKeyword(event.target.value)
    console.log(SearchKeyword)
  }
  const location = useLocation()
  return (
    <>
      <div>
        <CCard>
          <CCardHeader>
            <CCardTitle id="contained-modal-title-vcenter">
              Search Results for {location.state.keyword}
            </CCardTitle>
          </CCardHeader>
          <CCardBody>
            <CTable singleLine>
              <CTableHead>
                <CTableRow>
                  <CTableHeaderCell>Searches</CTableHeaderCell>
                </CTableRow>
              </CTableHead>

              <CTableBody>
                {Object.keys(KeywordResult).map((item, i) => {
                  ;<CTableRow>
                    <CTableDataCell>{KeywordResult[item]}</CTableDataCell>
                  </CTableRow>
                })}
              </CTableBody>
            </CTable>
          </CCardBody>
        </CCard>
      </div>
    </>
  )
}

export default SearchResult
