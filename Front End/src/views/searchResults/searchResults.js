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

const SearchResult = () => {
  const location = useLocation()
  const KeywordResult = location.state.data
  console.log(KeywordResult)
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
                {Object.keys(KeywordResult).map((item) => (
                  <CTableRow key={item}>
                    <CTableDataCell>{KeywordResult[item]}</CTableDataCell>
                  </CTableRow>
                ))}
              </CTableBody>
            </CTable>
          </CCardBody>
        </CCard>
      </div>
    </>
  )
}

export default SearchResult
