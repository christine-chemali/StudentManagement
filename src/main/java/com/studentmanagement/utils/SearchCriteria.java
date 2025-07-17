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
    
    /**
     * Constructs SearchCriteria with a specified search value
     * @param searchValue the value to search for
     */
    public SearchCriteria(String searchValue){
        this.searchValue = searchValue != null ? searchValue : "";
    }
    
    /**
     * Gets the current search value
     * @return the search value
     */
    public String getSearchValue(){
        return searchValue;
    }
    
    /**
     * Sets the search value
     * @param searchValue the value to search for
     */
    public void setSearchValue(String searchValue){
        this.searchValue = searchValue != null ? searchValue : "";
    }
    
    /**
     * Gets the current page number
     * @return the page number
     */
    public int getPageNumber(){
        return pageNumber;
    }
    
    /**
     * Sets the current page number
     * @param pageNumber
     */
    public void setPageNumber(int pageNumber){
        this.pageNumber = Math.max(1, pageNumber);
    }
    
    /**
     * Gets the number of items per page
     * @return the page size
     */
    public int getPageSize(){
        return pageSize;
    }
    
    /**
     * Sets the number of items per page
     * @param pageSize the page size to set
     */
    public void setPageSize(int pageSize){
        this.pageSize = Math.max(1, pageSize);
    }
    
    /**
     * Gets teh field used for sorting
     * @return the sort field
     */
    public String getSortField(){
        return sortField;
    }
    
    /**
     * Sets the field used for sorting
     * @param sortField sortField the field to sort by
     */
    public void setSortField(String sortField){
        this.sortField = sortField;
    }
    
    /**
     * Gets the direction of sorting (ascending or descending)
     * @return the sort direction 
     */
    public String getSortDirection(){
        return sortDirection;
    }
    
    /**
     * Sets the direction of sorting
     * @param sortDirection the sort direction to set
     */
    public void setSortDirection(String sortDirection){
        this.sortDirection = sortDirection;
    }
    
    /**
     * Calculates the offset for paginated database queries
     * @return the offset value
     */
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
    
    @Override
    /**
     * Checks for equality with another object
     * @param obj the object to compare with
     * @return true if equal, false otherwise
     */
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
    
    @Override
    /**
     * Generates a hash code for the SearchCriteria
     * @return the hash code
     */
    public int hashCode(){
        int result = searchValue != null ? searchValue.hashCode() : 0;
        result = 31 * result + pageNumber;
        result = 31 * result + pageSize;
        result = 31 * result + (sortField != null ? sortField.hashCode() : 0);
        result = 31 * result + (sortDirection != null ? sortDirection.hashCode() : 0);
        return result;
    }
}
