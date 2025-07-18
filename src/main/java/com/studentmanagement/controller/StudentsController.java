package com.studentmanagement.controller;

import com.studentmanagement.model.Student;
import com.studentmanagement.service.ImportExportService;
import com.studentmanagement.service.StudentService;
import com.studentmanagement.utils.AlertUtils;
import com.studentmanagement.utils.SearchCriteria;
import com.studentmanagement.utils.StudentValidator;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.stage.FileChooser;
import javafx.stage.Modality;
import javafx.stage.Stage;

import java.io.File;
import java.util.List;

//Controller for student management
//Inherits from BaseTableController
public class StudentsController extends BaseTableController<Student> {

    @FXML
    private TextField searchField;
    @FXML
    private Button searchButton;
    @FXML 
    private Button importButton;
    @FXML
    private Button exportButton;

    @FXML
    protected TableView<Student> studentTable;
    @FXML
    private TableColumn<Student, Integer> idColumn;
    @FXML
    private TableColumn<Student, String> firstNameColumn;
    @FXML
    private TableColumn<Student, String> lastNameColumn;
    @FXML
    private TableColumn<Student, Integer> ageColumn;
    @FXML
    private TableColumn<Student, String> classNameColumn;
    @FXML
    private TableColumn<Student, Double> averageGradeColumn;
    @FXML
    private TableColumn<Student, Void> editColumn;
    @FXML
    private TableColumn<Student, Void> deleteColumn;

    @FXML
    private Pagination pagination;

    @FXML
    private TextField firstNameField;
    @FXML
    private TextField lastNameField;
    @FXML
    private TextField ageField;
    @FXML
    private TextField classNameField;
    @FXML
    private Button addButton;

    private StudentService studentService;
    private ImportExportService importExportService;
    private final int ROWS_PER_PAGE = 15;

    public StudentsController(){
        //Empty constructor - dependancies will be injected
    }

    @FXML
    @Override
    protected void initialize(){
        //Init services
        studentService = new StudentService();
        importExportService = new ImportExportService();
        
        //Set dataTable for parent class
        dataTable = studentTable;

        //Config columns
        setupTableColumns();

        //Config action columns edit and delete
        setupEditColumn();
        setupDeleteColumn();

        //Init pagination
        pagination.setPageCount(calculatePageCount());
        pagination.currentPageIndexProperty().addListener((obs, oldIndex, newIndex)-> {
           loadStudentsPage(newIndex.intValue()); 
        });

        //Config buttons events
        setupButtonHandlers();

        //Activate sorting columns
        setupColumnSorting();

        //load initial data
        loadStudentsPage(0);
    }

    //Config the TableView for displaying student data
    @Override
    protected void setupTableColumns(){
        idColumn.setCellValueFactory(new PropertyValueFactory<>("studentId"));
        firstNameColumn.setCellValueFactory(new PropertyValueFactory<>("firstName"));
        lastNameColumn.setCellValueFactory(new PropertyValueFactory<>("lastName"));
        ageColumn.setCellValueFactory(new PropertyValueFactory<>("age"));
        classNameColumn.setCellValueFactory(new PropertyValueFactory<>("className"));
        averageGradeColumn.setCellValueFactory(new PropertyValueFactory<>("averageGrade"));
    }

    //Config action handlers for buttons
    private void setupButtonHandlers(){
        searchButton.setOnAction(event -> handleSearch());
        importButton.setOnAction(event -> handleImport());
        exportButton.setOnAction(event -> handleExport());
        addButton.setOnAction(event -> handleAddStudent());
    }

    //Enable sorting functionnality 
    private void setupColumnSorting(){
        //Activate sorting for appropriate columns
        idColumn.setSortable(true);
        firstNameColumn.setSortable(true);
        lastNameColumn.setSortable(true);
        ageColumn.setSortable(true);
        classNameColumn.setSortable(true);
        averageGradeColumn.setSortable(true);

        //Config sorting events to update the search
        studentTable.getSortOrder().addListener((javafx.collections.ListChangeListener.Change<? extends TableColumn<Student, ?>> change) -> {
            if (change.next() && !change.wasPermutated() && !studentTable.getSortOrder().isEmpty()){
                //get the sorted column and the direction
                TableColumn<Student, ?> sortColumn = studentTable.getSortOrder().get(0);
                String sortField = sortColumn.getId().replace("Column", "");
                String sortDirection = sortColumn.getSortType().toString();

                //Update search criteria and reload data
                SearchCriteria criteria = new SearchCriteria(searchField.getText());
                criteria.setSortField(sortField);
                criteria.setSortDirection(sortDirection);

                //Reload data with the new sort
                loadStudentsPage(pagination.getCurrentPageIndex());
            }
        });
    }

    /**
     * Sets the StudentService to be used by this controller
     * @param studentService the StudentService instance
     */
    public void setStudentService(StudentService studentService){
        this.studentService = studentService;
    }

    /**
     * Sets the ImportExportService to be used by this controller
     * @param importExportService the ImportExportServide instance
     */
    public void setImportExportService(ImportExportService importExportService){
        this.importExportService = importExportService;
    }

    //Create the modify button for editing student data
    private void setupEditColumn(){
        editColumn.setCellFactory(col -> {
            TableCell<Student, Void> cell = new TableCell<>(){
                private final Button editButton = new Button("Modifier");
                {
                    editButton.getStyleClass().add("button");
                    editButton.setMinHeight(30);
                    editButton.setOnAction(event -> {
                        Student student = getTableView().getItems().get(getIndex());
                        showEditDialog(student);
                    });
                }

                @Override
                /**
                 * Updates the item in the cell
                 * @param item the item to display
                 * @param empty indicates whether the cell is empty
                 */
                protected void updateItem(Void item, boolean empty){
                    super.updateItem(item, empty);
                    if (empty){
                        setGraphic(null);
                    } else {
                        setGraphic(editButton);
                    }
                }
            };
            return cell;
        });
    }

    //Create the delete button for deleting student data
    private void setupDeleteColumn(){
        deleteColumn.setCellFactory(col -> {
            TableCell<Student, Void> cell = new TableCell<>(){
                private final Button deleteButton = new Button("Supprimer");
                {
                    deleteButton.getStyleClass().add("button");
                    deleteButton.setMinHeight(30);
                    deleteButton.setOnAction(event -> {
                        Student student = getTableView().getItems().get(getIndex());
                        showDeleteConfirmation(student);
                    });
                }
                @Override
                /**
                 * Updates the item in the cell
                 * @param item the item to display
                 * @param empty indicates whether the cell is empty
                 */
                protected void updateItem(Void item, boolean empty){
                    super.updateItem(item, empty);
                    if (empty){
                        setGraphic(null);
                    } else {
                        setGraphic(deleteButton);
                    }
                }
            };
            return cell;
        });
    }

    @FXML
    protected void handleSearch(){
        refreshTableFromStart();
    }

    @FXML
    @Override
    protected void handleImport(){
        try {
            FileChooser fileChooser = new FileChooser();
            fileChooser.setTitle("Importer la recherche");
            fileChooser.getExtensionFilters().add(
                new FileChooser.ExtensionFilter("Fichiers CSV", "*.csv")
            );

            Stage stage = (Stage) importButton.getScene().getWindow();
            File selectedFile = fileChooser.showOpenDialog(stage);

            if (selectedFile != null){
                //Use the import service
                int importedCount = importExportService.ImportFromCSV(selectedFile);

                //Refesh table
                refreshTableFromStart();

                //Show a succes message
                AlertUtils.showInformation("Import réussi", importedCount + " étudiant(s) ont été importés avec succès !");
            }
        } catch (Exception e){
            AlertUtils.showError("Erreur d'Importation", "Oups ! Une erreur est survenue lors de l'importation : " + e.getMessage());
        }
    }

    @FXML
    protected void handleExport(){
        try {
            FileChooser fileChooser = new FileChooser();
            fileChooser.setTitle("Exporter la recherche");
            fileChooser.getExtensionFilters().add(
                new FileChooser.ExtensionFilter("Fichiers CSV", "*.csv")
            );
            Stage stage = (Stage) exportButton.getScene().getWindow();
            File selectedFile = fileChooser.showSaveDialog(stage);

            if (selectedFile != null){
                //Get the actual search criteria
                SearchCriteria criteria = new SearchCriteria(searchField.getText());

                //To export all the search results of the actual search
                criteria.setPageSize(Integer.MAX_VALUE);

                //To get all the students to export
                List<Student> studentsToExport = searchData(criteria);
                 
                //Use export service
                exportToCSV(studentsToExport, selectedFile);

                //Show success message
                AlertUtils.showInformation("Export réussi", "Les données ont été exportées avec succès vers\n" + selectedFile.getName());
            }
        } catch (Exception e){
            AlertUtils.showError("Erreur d'exportation", "Une erreur est survenue lors de l'exportation " + e.getMessage());
        }
    }

    @FXML
    private void handleAddStudent(){
        try {
            StudentValidator.ValidationResult result = StudentValidator.validateForCreation(
                firstNameField, lastNameField, ageField, classNameField
            );
            
            if (!result.isValid()) {
                AlertUtils.showError("Erreur de saisie", result.getErrorMessage());
                if (result.getFocusField() != null) {
                    result.getFocusField().selectAll();
                    result.getFocusField().requestFocus();
                }
                return;
            }

            //Create student with validated data
            Student newStudent = StudentValidator.createStudentFromFields(
                firstNameField, lastNameField, ageField, classNameField
            );

            //Use the createSudent method from StudentService
            studentService.createStudent(newStudent);

            //Refresh the table empty the fields
            refreshTable();
            clearAddForm();

            //Show a success message
            AlertUtils.showInformation("Succès", "L'étudiant a été ajouté avec succès");                
        } catch (Exception e){
            AlertUtils.showError("Erreur", "Une erreur est survenue lors de l'ajout : " + e.getMessage());
        }
    }

    @Override
    protected List<Student> searchData(SearchCriteria criteria) {
        return studentService.searchStudents(criteria);
    }
    
    @Override
    protected int getTotalCount(SearchCriteria criteria) {
        return studentService.getTotalStudents(criteria);
    }
    
    @Override
    protected void exportToCSV(List<Student> data, File file) {
        importExportService.exportToCSV(data, file);
    }

    private void clearAddForm(){
        firstNameField.clear();
        lastNameField.clear();
        ageField.clear();
        classNameField.clear();
    }

    /**
     * Loads a page of students based on the provided page index
     * @param pageIndex the index of the page to load
     */
    private void loadStudentsPage(int pageIndex){
        try {
            //Create a SearchCriteria object for pagination
            SearchCriteria criteria = new SearchCriteria(searchField.getText());
            criteria.setPageNumber(pageIndex + 1); // because pageIndex begins at 0
            criteria.setPageSize(ROWS_PER_PAGE);

            //Add sort information if available
            if (!studentTable.getSortOrder().isEmpty()){
                TableColumn<Student, ?> sortColumn = studentTable.getSortOrder().get(0);
                String sortField = sortColumn.getId().replace("Column", "");
                String sortDirection = sortColumn.getSortType().toString();
                criteria.setSortField(sortField);
                criteria.setSortDirection(sortDirection);
            }

            //Get students for the current page
            List<Student> pageItems = searchData(criteria);

            //Update the table
            ObservableList<Student> students = FXCollections.observableArrayList(pageItems);
            studentTable.setItems(students);
        } catch (Exception e){
            AlertUtils.showError("Erreur", "Impossible de charger les données : " + e.getMessage());
        }
    }

    protected int calculatePageCount(){
        try {
            //Create an SearchCriteria object to obtain the total number
            SearchCriteria criteria = new SearchCriteria(searchField.getText());
            int totalStudents = getTotalCount(criteria);
            return (totalStudents + ROWS_PER_PAGE -1 ) / ROWS_PER_PAGE;
        } catch (Exception e){
            AlertUtils.showError("Erreur", "Impossible de calculer le nombre de pages " + e.getMessage());
        }
        return 1;
    }

    /**
     * Displays a dialog for editing the specified student
     * @param student the student to be edited
     */
    private void showEditDialog(Student student){
        //Create a new dialog window
        Stage dialogStage = new Stage();
        dialogStage.setTitle("Modifier l'étudiant");
        dialogStage.initModality(Modality.WINDOW_MODAL);
        dialogStage.setResizable(false);

        //Create the dialog content
        GridPane grid = new GridPane();
        grid.setHgap(10);
        grid.setVgap(10);
        grid.setPadding(new Insets(20, 20, 20, 20));

        //Add the fields
        grid.add(new Label("Prénom"), 0,0);
        TextField firstNameField = new TextField(student.getFirstName());
        grid.add(firstNameField, 1, 0);

        grid.add(new Label("Nom"), 0,1);
        TextField lastNameField = new TextField(student.getLastName());
        grid.add(lastNameField, 1,1);

        grid.add(new Label("Age"), 0,2);
        TextField ageField = new TextField(String.valueOf(student.getAge()));
        grid.add(ageField, 1, 2);

        grid.add(new Label("Classe"), 0, 3);
        TextField classNameField = new TextField(student.getClassName());
        grid.add(classNameField, 1,3);

        //Buttons
        Button saveButton = new Button("J'enregistre");
        Button cancelButton = new Button("J'annule");

        HBox buttonBox = new HBox(10);
        buttonBox.setAlignment(Pos.CENTER_RIGHT);
        buttonBox.getChildren().addAll(saveButton, cancelButton);
        grid.add(buttonBox, 0, 4, 2, 1);

        //Buttons actions
        saveButton.setOnAction(e -> {
            try {
                //Use validator for modification
                StudentValidator.ValidationResult result = StudentValidator.validateAndApplyChanges(
                    student, firstNameField, lastNameField, ageField, classNameField
                );
                
                if (!result.isValid()) {
                    AlertUtils.showError("Erreur", result.getErrorMessage());
                    if (result.getFocusField() != null) {
                        result.getFocusField().selectAll();
                        result.getFocusField().requestFocus();
                    }
                    return;
                }

                //Save modifications
                studentService.updateStudent(student);

                //Close dialog window
                dialogStage.close();

                //update the  table
                refreshTable();

                //Show success message
                AlertUtils.showInformation("Succès", "L'étudiant a été modifié avec succès !");

            } catch (Exception ex){
                AlertUtils.showError("Erreur", "Une erreur est survenue lors de la modification : " + ex.getMessage());
            }
        });

        cancelButton.setOnAction(e -> dialogStage.close());

        //Config scene and show dialog window
        Scene scene = new Scene(grid);
        dialogStage.setScene(scene);
        dialogStage.showAndWait();
    }

    /**
     * Displays a confirmation dialog for deleting the specified studetn
     * @param student the student to be deleted
     */
    private void showDeleteConfirmation(Student student){
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Confirmation de suppression");
        alert.setHeaderText("Supprimer l'étudiant");
        alert.setContentText("Êtes-vous sûr de vouloir supprimer les données de l'étudiant " + 
                             student.getFirstName() + " " + student.getLastName() + " ?");
        
        alert.showAndWait().ifPresent(response -> {
            if(response == ButtonType.OK){
                try {
                    //Delete student data
                    studentService.deleteStudent(student.getStudentId());

                    //refresh the table
                    int currentPageIndex = pagination.getCurrentPageIndex();
                    refreshTable();

                    //Update page numbers
                    int newPageCount = calculatePageCount();
                    pagination.setPageCount(newPageCount);

                    //if the current page doesn't exist, go to the last page
                    if(currentPageIndex >= newPageCount && newPageCount > 0){
                       pagination.setCurrentPageIndex(newPageCount -1); 
                    }
                    //Show success message
                    AlertUtils.showInformation("Succès", "Les données de l'étudiant ont été supprimées avec succès.");
                }catch (Exception e){
                    AlertUtils.showError("Erreur", "Une erreur est survenue lors de la suppression : " + e.getMessage());
                }
            }
        });
    }

}