package com.studentmanagement.utils;

//Minimum verion to compile without errors
public class SearchCriteria {

    private String searchValue;
    @SuppressWarnings("unused")
    private int pageNumber = 1;
    @SuppressWarnings("unused")
    private int pageSize = 15;
    @SuppressWarnings("unused")
    private String sortField;
    @SuppressWarnings("unused")
    private String sortDirection;

    public SearchCriteria(String searchValue){
        this.searchValue = searchValue;
    }

    public void setPageNumber(int pageNumber){
        this.pageNumber = pageNumber;
    }

    public void setPageSize(int pageSize) { 
        this.pageSize = pageSize; 
    }
    public void setSortField(String sortField){
        this.sortField = sortField;
    }

    public void setSortDirection(String sortDirection){
        this.sortDirection = sortDirection;
    }

    public String getSearchValue() {
                return searchValue;
    }
}
