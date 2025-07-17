package com.studentmanagement.utils;

//Search criteria class for student queries with pagination support
public class SearchCriteria {
    private String searchValue;
    private int pageNumber = 1;
    private int pageSize = 15;
    private String sortField;
    private String sortDirection;
    
    //Default constructor
    public SearchCriteria(){
        this.searchValue = "";
    }
    
    //Constructor with search value
    public SearchCriteria(String searchValue){
        this.searchValue = searchValue != null ? searchValue : "";
    }
    
    //Gets the search value
    public String getSearchValue(){
        return searchValue;
    }
    
    //Sets the search value
    public void setSearchValue(String searchValue){
        this.searchValue = searchValue != null ? searchValue : "";
    }
    
    //Gets the page number
    public int getPageNumber(){
        return pageNumber;
    }
    
    //Sets the page number
    public void setPageNumber(int pageNumber){
        this.pageNumber = Math.max(1, pageNumber);
    }
    
    //Gets the page size
    public int getPageSize(){
        return pageSize;
    }
    
    //Sets the page size
    public void setPageSize(int pageSize){
        this.pageSize = Math.max(1, pageSize);
    }
    
    //Gets the sort field
    public String getSortField(){
        return sortField;
    }
    
    //Sets the sort field
    public void setSortField(String sortField){
        this.sortField = sortField;
    }
    
    //Gets the sort direction
    public String getSortDirection(){
        return sortDirection;
    }
    
    //Sets the sort direction
    public void setSortDirection(String sortDirection){
        this.sortDirection = sortDirection;
    }
    
    //Calculates the offset for database queries
    public int getOffset(){
        return (pageNumber - 1) * pageSize;
    }
    
    //String representation of the search criteria
    @Override
    public String toString() {
        return "SearchCriteria{" +
                "searchValue='" + searchValue + '\'' +
                ", pageNumber=" + pageNumber +
                ", pageSize=" + pageSize +
                ", sortField='" + sortField + '\'' +
                ", sortDirection='" + sortDirection + '\'' +
                ", offset=" + getOffset() +
                '}';
    }
    
    //Equals method for comparison
    @Override
    public boolean equals(Object obj){
        if (this == obj) return true;
        if (obj == null || getClass() != obj.getClass()) return false;
        
        SearchCriteria that = (SearchCriteria) obj;
        
        if (pageNumber != that.pageNumber) return false;
        if (pageSize != that.pageSize) return false;
        if (searchValue != null ? !searchValue.equals(that.searchValue) : that.searchValue != null) return false;
        if (sortField != null ? !sortField.equals(that.sortField) : that.sortField != null) return false;
        return sortDirection != null ? sortDirection.equals(that.sortDirection) : that.sortDirection == null;
    }
    
    //Hash code method
    @Override
    public int hashCode(){
        int result = searchValue != null ? searchValue.hashCode() : 0;
        result = 31 * result + pageNumber;
        result = 31 * result + pageSize;
        result = 31 * result + (sortField != null ? sortField.hashCode() : 0);
        result = 31 * result + (sortDirection != null ? sortDirection.hashCode() : 0);
        return result;
    }
}
