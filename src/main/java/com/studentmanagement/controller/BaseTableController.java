package com.studentmanagement.controller;

import java.io.File;
import java.util.List;

import com.studentmanagement.service.ImportExportService;
import com.studentmanagement.utils.AlertUtils;
import com.studentmanagement.utils.SearchCriteria;

import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Pagination;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableRow;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

/* Abstract base controller for tables with pagination
 * search and export
 * @param <T> type of entities displayed in the table
 */
public  abstract class BaseTableController<T> {
    
    @FXML
    protected TextField searchField;
    @FXML
    protected Button searchButton;
    @FXML
    protected Button exportButton;
    @FXML
    protected Button importButton;
    @FXML
    protected TableView<T> dataTable;
    @FXML
    protected Pagination pagination;

    protected ImportExportService importExportService;
    protected final int ROWS_PER_PAGE = 15;

    //Common initialization for all table controllers
    @FXML
    protected void initialize(){
        initializeServices();
        setupTableColumns();
        setupTableResizing();
        setupPagination();
        setupSearchAndExport();
        setupImport();
        setupColumnSorting();
        loadInitialData();
    }

    //Init required services
    protected void initializeServices(){
        importExportService = new ImportExportService();
    }
    //Set up table auto-resizing
    protected void setupTableResizing(){
        if (dataTable != null){
            dataTable.setColumnResizePolicy(TableView.UNCONSTRAINED_RESIZE_POLICY);
            //Rows config
            dataTable.setRowFactory(tv -> {
                TableRow<T> row = new TableRow<>();
                //JavaFx auto calculate
                row.setPrefHeight(-1);
                //Min height
                row.setMinHeight(25); 
                return row;
            });
            //Listener for refresh
            dataTable.getItems().addListener((javafx.collections.ListChangeListener<T>) change -> {
                Platform.runLater(() -> dataTable.refresh());
            });
        }
    }

    //Force height re calculate
    protected void refreshTableRowHeights(){
        if (dataTable != null) {
            Platform.runLater(() -> {
                dataTable.refresh();
                //double refresh to be sure heights are re calculated
                Platform.runLater(() -> dataTable.refresh());
            });
        }
    }

    //Auto Resize
    protected void autoResizeTable(){
        if(dataTable != null){
            Platform.runLater(() -> {
                dataTable.refresh();
                dataTable.autosize();
            });
        }
    }

    //Configure column sizing
    protected void setupColumnSizing(){
        // Default implementation - subclasses can override
        // to set specific column widths and constraints
    }

    //Set up pagination
    private void setupPagination(){
        pagination.setPageCount(calculatePageCount());
        pagination.currentPageIndexProperty().addListener((obs, oldIndex, newIndex) -> {
            loadDataPage(newIndex.intValue());
        });
    }

    //Set up search and export buttons
    private void setupSearchAndExport(){
        searchButton.setOnAction(event -> handleSearch());
        exportButton.setOnAction(event -> handleExport());
    } 

    //Sets up import button
    private void setupImport(){
        if(importButton !=null){
            importButton.setOnAction(event -> handleImport());
        }
    }

    //Sets up column sorting
    private void setupColumnSorting(){
        dataTable.getSortOrder().addListener((javafx.collections.ListChangeListener.Change<? extends TableColumn<T, ?>> change) ->{
            if(change.next() && !change.wasPermutated() && !dataTable.getSortOrder().isEmpty()){
                loadDataPage(pagination.getCurrentPageIndex());
            }
        });
    }

    //Loads initial data
    private void loadInitialData(){
        loadDataPage(0);
    }

    //Handles search
    @FXML
    protected void handleSearch(){
        loadDataPage(0);
        pagination.setCurrentPageIndex(0);
        pagination.setPageCount(calculatePageCount());
    }

    //Handles CSV export
    @FXML
    protected void handleExport(){
        try{
            FileChooser fileChooser = new FileChooser();
            fileChooser.setTitle("Export des données");
            fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("CSV Files", "*.csv"));

            Stage stage = (Stage) exportButton.getScene().getWindow();
            File selectedFile = fileChooser.showSaveDialog(stage);

            if(selectedFile != null){
                //Get all results from current search
                SearchCriteria criteria = createSearchCriteria();
                criteria.setPageSize(Integer.MAX_VALUE);
                List<T> dataToExport = searchData(criteria);
                exportToCSV(dataToExport, selectedFile);
                
                AlertUtils.showInformation("Succès de l'export", "Les données ont été exportées avec succès dans :\n" + selectedFile.getName());
            }
        } catch (Exception e){
            AlertUtils.showError("Erreur export", "Une erreur est survenue durant l'export : " + e.getMessage());
        }
    }

    //loads a page of data
    protected void loadDataPage(int pageIndex){
        try{
            SearchCriteria criteria = createSearchCriteria();
            criteria.setPageNumber( pageIndex + 1);
            criteria.setPageSize(ROWS_PER_PAGE);

            //Add sorting informatin if available
            if(!dataTable.getSortOrder().isEmpty()){
                TableColumn<T, ?> sortColumn = dataTable.getSortOrder().get(0);
                String sortField = extractSortField(sortColumn);
                String sortDirection = sortColumn.getSortType().toString();
                criteria.setSortField(sortField);
                criteria.setSortDirection(sortDirection);
            }

            List<T> pageItems = searchData(criteria);
            ObservableList<T> data = FXCollections.observableArrayList(pageItems);
            dataTable.setItems(data);
        } catch (Exception e){
            AlertUtils.showError("Erreur", "Impossible de charger les données\n" + e.getMessage());
        }
    }

    //Calculates the total number of pages
    protected int calculatePageCount(){
        try{
            SearchCriteria criteria = createSearchCriteria();
            int totalItems = getTotalCount(criteria);
            return (totalItems + ROWS_PER_PAGE -1) / ROWS_PER_PAGE;
        } catch (Exception e){
            AlertUtils.showError("Erreur", "Impossible de calculer le nombre de pages :\n" + e.getMessage());
            return 1;
        }
    }

    //Creates search criteria based on the user interface
    protected SearchCriteria createSearchCriteria(){
        return new SearchCriteria(searchField.getText());
    }

    //Extracts the sort field name from a column
    protected String extractSortField(TableColumn<T, ?> column){
        return column.getId().replace("Column", "");
    }

    //Refreshes the current table
    protected void refreshTable(){
        loadDataPage(pagination.getCurrentPageIndex());
        pagination.setPageCount(calculatePageCount());
    }

    //Refreshes and returns to the first page
    protected void refreshTableFromStart(){
        loadDataPage(0);
        pagination.setCurrentPageIndex(0);
        pagination.setPageCount(calculatePageCount());
    }

    //=========== ABSTRACT METHODS TO IMPLEMENT =======
    //Sets up table columns
    protected abstract void setupTableColumns();

    //Searches data according to criteria
    protected abstract List<T> searchData(SearchCriteria criteria);

    //Exports data to a CSV file
    protected abstract void exportToCSV(List<T> data, File file);

    //Gets the total count of items
    protected abstract int getTotalCount(SearchCriteria criteria);

    //Handles import functionality
    protected abstract void handleImport();
    
    // ================ Setters for dependency injection ===========

    public void ImportExportService(ImportExportService importExportService){
        this.importExportService = importExportService;
    }

}