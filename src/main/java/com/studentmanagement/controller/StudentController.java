package com.studentmanagement.controller;

import javafx.fxml.FXML;
import javafx.geometry.Pos;
import javafx.scene.control.*;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import com.studentmanagement.model.Grade;
import com.studentmanagement.model.Student;
import com.studentmanagement.model.SubjectComment;
import com.studentmanagement.service.GradeService;
import com.studentmanagement.service.StudentService;
import com.studentmanagement.service.SubjectCommentService;
import com.studentmanagement.utils.AlertUtils;
import com.studentmanagement.utils.DialogUtils;
import com.studentmanagement.utils.GradeValidator;
import com.studentmanagement.utils.SearchCriteria;
import com.studentmanagement.utils.SubjectResult;


public class StudentController extends BaseTableController<SubjectResult>  {

@FXML
    private TextField studentIdField;
    @FXML
    private Label studentNameLabel;
    @FXML
    private Label studentClassLabel;
    @FXML
    private ComboBox<String> subjectComboBox;
    @FXML
    private TextField gradeField;
    @FXML
    private TextField coefficientField;
    @FXML
    private Button addGradeButton;
    @FXML
    private TextArea commentArea;
    @FXML
    private Button addCommentButton;
    @FXML
    private Button okButton;
    @FXML
    private Button searchButton;
    @FXML
    private TableView<SubjectResult> gradesTable;
    @FXML
    private TableColumn<SubjectResult, String> subjectColumn;
    @FXML
    private TableColumn<SubjectResult, String> gradesColumn;
    @FXML
    private TableColumn<SubjectResult, Number> minAverageColumn;
    @FXML
    private TableColumn<SubjectResult, Number> maxAverageColumn;
    @FXML
    private TableColumn<SubjectResult, Number> studentAverageColumn;
    @FXML
    private TableColumn<SubjectResult, String> commentsColumn;

    private StudentService studentService;
    private GradeService gradeService;
    private SubjectCommentService commentService;
    private Student currentStudent;
    private List<String> subjects = Arrays.asList(
            "Français", "Anglais", "Mathématiques", "Histoire", "Géographie", 
            "Sciences Physiques", "Sciences de la Vie et de la Terre", "Arts Plastiques",
            "Éducation Physique et Sportive", "Technologie", "Musique", "Espagnol", "Allemand"
        );

    public StudentController() {
        // Empty constructor - dependencies will be injected
    }

    @FXML
    @Override
    protected void initialize(){
        //Initialize services
        studentService = new StudentService();
        gradeService = new GradeService();
        commentService = new SubjectCommentService();

        //Set up subject combo box
        subjectComboBox.setItems(FXCollections.observableArrayList(subjects));

        //Rename dataTable reference to match FXML
        dataTable = gradesTable;

        //Call parent initialization
        super.initialize();

        //Setup specific column sizing for this table
        setupColumnSizing();

        //Disable grade adding until a student is loaded
        disableGradeControls(true);
    }

    @Override
    protected void setupColumnSizing() {
        //Specific config
        subjectColumn.setPrefWidth(120);
        subjectColumn.setMinWidth(100);
        subjectColumn.setResizable(true);
        
        gradesColumn.setPrefWidth(130);
        gradesColumn.setMinWidth(120);
        gradesColumn.setResizable(true);
        
        minAverageColumn.setPrefWidth(80);
        minAverageColumn.setMinWidth(70);
        minAverageColumn.setResizable(true);
        
        maxAverageColumn.setPrefWidth(80);
        maxAverageColumn.setMinWidth(70);
        maxAverageColumn.setResizable(true);
        
        studentAverageColumn.setPrefWidth(80);
        studentAverageColumn.setMinWidth(70);
        studentAverageColumn.setResizable(true);
        
        commentsColumn.setPrefWidth(300);
        commentsColumn.setMinWidth(200);
        commentsColumn.setResizable(true);
    }
    
    @Override
    protected void setupTableColumns() {
        //Configure columns
        subjectColumn.setCellValueFactory(data -> new SimpleStringProperty(data.getValue().getSubject()));

        //Grades column with checkbox for each grade
        gradesColumn.setCellValueFactory(data -> new SimpleStringProperty(data.getValue().getGrades()));
        gradesColumn.setCellFactory(column -> new TableCell<SubjectResult, String>() {
            @Override
            protected void updateItem(String item, boolean empty){
                super.updateItem(item, empty); 
                if(empty || item == null || item.isEmpty()){
                    setGraphic(null);
                    setText(null);
                    return;
                }
                
                VBox container = new VBox(3);
                container.setAlignment(Pos.TOP_LEFT);
                //fixed height
                container.setPrefWidth(120);
                container.setMaxWidth(120);
                
                String[] gradesArray = item.split(", ");
                
                //Calculate init height without buttons
                double initialHeight = Math.max(25, gradesArray.length * 27 + 6);
                container.setPrefHeight(initialHeight);
                
                for (String gradeStr : gradesArray){
                    VBox gradeContainer = new VBox(2); //Vertical container for each row
                    gradeContainer.setAlignment(Pos.TOP_LEFT);
                    
                    //Up row checkBox + grade
                    HBox gradeBox = new HBox(5);
                    gradeBox.setAlignment(Pos.CENTER_LEFT);
                    gradeBox.setPrefHeight(25);
                    gradeBox.setMaxHeight(25);
                    
                    CheckBox checkBox = new CheckBox();
                    checkBox.setPrefWidth(20);
                    checkBox.setMinWidth(20);
                    checkBox.setMaxWidth(20);
                    
                    Label gradeLabel = new Label(gradeStr);
                    gradeLabel.setWrapText(false);
                    gradeLabel.setPrefWidth(-1); 
                    gradeLabel.setMaxWidth(Double.MAX_VALUE);
                    
                    gradeBox.getChildren().addAll(checkBox, gradeLabel);
                    
                    //bottom row button hidden by default and out of the layout
                    HBox buttonBox = new HBox(5);
                    buttonBox.setAlignment(Pos.CENTER_LEFT);
                    buttonBox.setPrefHeight(0); //no height by default
                    buttonBox.setMaxHeight(0);  //no height by default
                    buttonBox.setVisible(false); //hide by default
                    buttonBox.setManaged(false); //not handled by the default layout
                    buttonBox.setPadding(new javafx.geometry.Insets(0, 0, 0, 25)); //Indentation
                    
                    Button editButton = new Button("+");
                    editButton.setPrefSize(25, 25);
                    editButton.setMinSize(25, 25);
                    editButton.setMaxSize(25, 25);
                    editButton.setTooltip(new Tooltip("Je modifie cette note"));
                    
                    Button deleteButton = new Button("-");
                    deleteButton.setPrefSize(25, 25);
                    deleteButton.setMinSize(25, 25);
                    deleteButton.setMaxSize(25, 25);
                    deleteButton.setTooltip(new Tooltip("Je supprime cette note"));
                    
                    buttonBox.getChildren().addAll(editButton, deleteButton);
                    
                    //Show/hide button depending on checkbox state 
                    checkBox.selectedProperty().addListener((obs, oldVal, newVal) -> {
                        if (newVal) {
                            //Show give a height make it visible
                            buttonBox.setPrefHeight(25);
                            buttonBox.setMaxHeight(25);
                            buttonBox.setVisible(true);
                            //Take on the layout
                            buttonBox.setManaged(true);
                        } else {
                            //Hide take of the size and make it invisible 
                            buttonBox.setPrefHeight(0);
                            buttonBox.setMaxHeight(0);
                            buttonBox.setVisible(false);
                            //Take of the layout 
                            buttonBox.setManaged(false);
                        }
                        
                        //Re calculate height row
                        double newHeight = calculateContainerHeight(container);
                        container.setPrefHeight(newHeight);
                        container.setMaxHeight(newHeight);
                        
                        //Force layout to re calculate
                        container.requestLayout();
                    });
                    
                    editButton.setOnAction(e -> {
                        SubjectResult result = getTableView().getItems().get(getIndex());
                        showGradeEditDialog(result.getSubject(), gradeStr);
                    });
                    
                    deleteButton.setOnAction(e -> {
                        SubjectResult result = getTableView().getItems().get(getIndex());
                        showGradeDeleteConfirmation(result.getSubject(), gradeStr);
                    });
                    
                    //Add two rows to  vertical container
                    gradeContainer.getChildren().addAll(gradeBox, buttonBox);
                    container.getChildren().add(gradeContainer);
                }
                
                setGraphic(container);
                setText(null);
            }
            
            //Method to calculate container height
            private double calculateContainerHeight(VBox container){
                double height = 6; //padding
                for (javafx.scene.Node child : container.getChildren()){
                    if(child instanceof VBox){
                        VBox gradeContainer = (VBox) child;
                        height += 27; //height for the grade row
                        //Verify if buttons are visible
                        for(javafx.scene.Node subChild : gradeContainer.getChildren()){
                            if(subChild instanceof HBox){
                                HBox buttonBox = (HBox) subChild;
                                if (buttonBox.isVisible() && buttonBox.isManaged()){
                                    height += 25; //button height
                                }
                            }
                        }
                    }
                }
                return Math.max(25, height);
            }
        });
        
        minAverageColumn.setCellValueFactory(data -> 
            new SimpleDoubleProperty(data.getValue().getClassMinAverage()));
        
        maxAverageColumn.setCellValueFactory(data -> 
            new SimpleDoubleProperty(data.getValue().getClassMaxAverage()));
        
        studentAverageColumn.setCellValueFactory(data -> 
            new SimpleDoubleProperty(data.getValue().getStudentAverage()));
        
        //Comments column with checkbox for editing
        commentsColumn.setCellValueFactory(data -> 
            new SimpleStringProperty(data.getValue().getTeacherComment()));
        commentsColumn.setCellFactory(column -> new TableCell<SubjectResult, String>() {
            @Override
            protected void updateItem(String item, boolean empty) {
                super.updateItem(item, empty);
                
                if (empty) {
                    setGraphic(null);
                    setText(null);
                    return;
                }
                
                VBox container = new VBox(5);
                container.setAlignment(Pos.TOP_LEFT);
                container.setPrefWidth(280);
                container.setMaxWidth(280);
                CheckBox checkBox = new CheckBox();
                Label commentLabel = new Label(item != null ? item : "");
                commentLabel.setWrapText(true);
                commentLabel.setPrefWidth(250);
                commentLabel.setMaxWidth(250);
                commentLabel.setAlignment(Pos.TOP_LEFT);
                
                HBox buttonBox = new HBox(5);
                buttonBox.setAlignment(Pos.CENTER_LEFT);
                buttonBox.setVisible(false);
                buttonBox.setManaged(false);
                
                Button editButton = new Button("+");
                editButton.setPrefSize(25, 25);
                editButton.setMinSize(25, 25);
                editButton.setMaxSize(25, 25);
                editButton.setTooltip(new Tooltip("Je modifie le commentaire"));
                
                Button deleteButton = new Button("-");
                deleteButton.setPrefSize(25, 25);
                deleteButton.setMinSize(25, 25);
                deleteButton.setMaxSize(25, 25);
                deleteButton.setTooltip(new Tooltip("Je supprime le commentaire"));
                
                buttonBox.getChildren().addAll(editButton, deleteButton);
                
                checkBox.selectedProperty().addListener((obs, oldVal, newVal) -> {
                    buttonBox.setVisible(newVal);
                    buttonBox.setManaged(newVal);
                    
                    //Re calculate height
                    double textHeight = calculateTextHeight(commentLabel);
                    double buttonHeight = newVal ? 50 : 0; //2 buttons + space
                    double newHeight = Math.max(50, textHeight + buttonHeight + 35);
                    
                    container.setPrefHeight(newHeight);
                    container.setMaxHeight(newHeight);
                    container.requestLayout();
                });
                
                editButton.setOnAction(e -> {
                    SubjectResult result = getTableView().getItems().get(getIndex());
                    showCommentEditDialog(result.getSubject(), result.getTeacherComment());
                });
                
                deleteButton.setOnAction(e -> {
                    SubjectResult result = getTableView().getItems().get(getIndex());
                    showCommentDeleteConfirmation(result.getSubject());
                });
                
                container.getChildren().addAll(checkBox, commentLabel, buttonBox);
                
                //Calculate init height
                double initialHeight = Math.max(50, calculateTextHeight(commentLabel) + 35);
                container.setPrefHeight(initialHeight);
                container.setMaxHeight(initialHeight);
                
                setGraphic(container);
                setText(null);
            }
            //Calculate height based on text
            private double calculateTextHeight(Label label){
                String text = label.getText();
                if (text == null || text.isEmpty()){
                    return 20; //minimum height
                }
                //Estimation
                int estimatedLines = Math.max(1, (int) Math.ceil(text.length() / 35.0));
                return estimatedLines * 18; //18px per line
            }
        });
    }

 @FXML
    protected void handleLoadStudent(){
        try{
            String studentIdText = studentIdField.getText().trim();
            if(studentIdText.isEmpty()){
                AlertUtils.showError("Erreur", "Tu dois remplir l'ID d'étudiant !");
                return;
            }
            try{
                Long studentId = Long.parseLong(studentIdText);
                currentStudent = studentService.getStudentByID(studentId);
                if(currentStudent == null){
                    AlertUtils.showError("Erreur", "Aucun étudiant trouvé avec cet ID.");
                    disableGradeControls(true);
                    return;
                }
                
                //Update UI with student info
                studentNameLabel.setText("Nom de l'étudiant : " + currentStudent.getFullName());
                studentClassLabel.setText("Classe : " + currentStudent.getStudentClassName());
                
                //Enable grade controls
                disableGradeControls(false);
                
                //Load student grades
                refreshTable();
                
            } catch(NumberFormatException e){
                AlertUtils.showError("Erreur", "L'ID étudiant doit être un nombre entier.");
            }
            
        } catch (Exception e){
            AlertUtils.showError("Erreur", "Une erreur est survenue lors du chargement de l'étudiant :\n" + e.getMessage());
        }
    }

    @FXML
    protected void handleAddGrade(){
        try{
            String subject = subjectComboBox.getValue();
            if (subject == null || subject.isEmpty()) {
                AlertUtils.showError("Erreur", "Tu dois sélectionner une matière!");
                return;
            }
        
            //Validate input using the validator
            GradeValidator.ValidationResult result = GradeValidator.validateGradeInput(gradeField, coefficientField);
            if(!result.isValid()){
                AlertUtils.showError("Erreur", result.getErrorMessage());
                if(result.getFocusField() != null){
                    ((TextField)result.getFocusField()).requestFocus();
                }
                return;
            }
            //Create and save grade using the validator
            Grade newGrade = GradeValidator.createGradeFromFields(
                currentStudent.getStudentId(), subject, gradeField, coefficientField);
            gradeService.saveGrade(newGrade);

            //Clear fields and refresh
            gradeField.clear();
            coefficientField.clear();
            refreshTable();
            AlertUtils.showInformation("Succès", "La note a été ajoutée avec succès.");
        } catch(Exception e){
            AlertUtils.showError("Erreur", "Une erreur est survenue lors de l'ajout de la note :\n" + e.getMessage());
        }
    }

    @FXML
    protected void handleAddComment(){
        try{
            String subject = subjectComboBox.getValue();
            if (subject == null || subject.isEmpty()){
                AlertUtils.showError("Erreur", "Sélectionne une matière !");
                return;
            }
            String comment = commentArea.getText().trim();
            if(comment.isEmpty()){
                AlertUtils.showInformation("Information", "Il n'y a pas de commentaire à ajouter.\nRédiges-en un !");
                commentArea.requestFocus();
                return;
            }
            
            //Create and save the comment
            SubjectComment newComment = new SubjectComment();
            newComment.setStudentId(currentStudent.getStudentId());
            newComment.setSubject(subject);
            newComment.setComment(comment);
            commentService.saveComment(newComment);
            commentArea.clear();
            refreshTable();
            AlertUtils.showInformation("Succès", "Le commentaire a été ajouté avec succès.");
        } catch (Exception e){
            AlertUtils.showError("Erreur", "Une erreur est survenue lors de l'ajout du commentaire :\n" + e.getMessage());
        }
    }

    @FXML
    protected void handleSort(){
        try{
            refreshTable();
            
        } catch (Exception e){
            AlertUtils.showError("Erreur", "Une erreur est survenue lors du tri :\n" + e.getMessage());
        }
    }

    private void showGradeEditDialog(String subject, String gradeText) {
        try{
            //Parse grade value and coefficient from the grade text
            String[] parts = gradeText.split(" \\(coef: ");
            double gradeValue = Double.parseDouble(parts[0]);
            double coeffValue = Double.parseDouble(parts[1].replace(")", ""));
            //Create dialog fields
            List<DialogUtils.DialogField> fields = new ArrayList<>();
            fields.add(new DialogUtils.DialogField("Note :", String.valueOf(gradeValue)));
            fields.add(new DialogUtils.DialogField("Coefficient :", String.valueOf(coeffValue)));
            //Show dialog
            DialogUtils.showEditDialog(
                "Modifier la note", 
                fields,
                //On save action
                (updatedFields) -> {
                    try{
                        //Get new values
                        double newGrade = Double.parseDouble(updatedFields.get(0).getValue());
                        double newCoeff = Double.parseDouble(updatedFields.get(1).getValue());
                        //Update grade in database
                        gradeService.updateGrade(currentStudent.getStudentId(), subject, 
                            gradeValue, coeffValue, newGrade, newCoeff);
                        //Refresh table
                        refreshTable();
                        //Show success message
                        AlertUtils.showInformation("Succès", "La note a été modifiée avec succès.");
                    } catch (Exception ex) {
                        AlertUtils.showError("Erreur", "Une erreur est survenue lors de la modification : " + ex.getMessage());
                    }
                },
                //Validator
                (fieldsToValidate) -> {
                    try {
                        //Create temporary TextFields for validation
                        TextField tempGradeField = new TextField(fieldsToValidate.get(0).getValue());
                        TextField tempCoeffField = new TextField(fieldsToValidate.get(1).getValue());
                        //Validate input using the validator
                        GradeValidator.ValidationResult result = GradeValidator.validateGradeInput(
                            tempGradeField, tempCoeffField);
                        if (!result.isValid()){
                            return result.getErrorMessage();
                        }
                        //Check if values have changed
                        result = GradeValidator.checkForChanges(
                            gradeValue, coeffValue, tempGradeField, tempCoeffField);
                        if (!result.isValid()){
                            return result.getErrorMessage();
                        }
                        return null;
                    } catch (Exception e){
                        return "Une erreur est survenue :\n" + e.getMessage();
                    }
                }
            );
        } catch (Exception e){
            AlertUtils.showError("Erreur", "Une erreur est survenue lors de l'édition de la note :\n" + e.getMessage());
        }
    }

    private void showGradeDeleteConfirmation(String subject, String gradeText) {
        try{
            //Parse grade value and coefficient from the grade text
            String[] parts = gradeText.split(" \\(coef: ");
            double gradeValue = Double.parseDouble(parts[0]);
            double coeffValue = Double.parseDouble(parts[1].replace(")", ""));        
            //Show confirmation dialog
            DialogUtils.showDeleteConfirmation(
                "Supprimer la note",
                "Es tu sûr de vouloir supprimer cette note (" + gradeText + ") pour la matière " + subject + " ?",
                () -> {
                    try{
                        //Delete grade from database
                        gradeService.deleteGrade(currentStudent.getStudentId(), subject, gradeValue, coeffValue);
                        //Refresh table
                        refreshTable();
                        //Show success message
                        AlertUtils.showInformation("Succès", "La note a été supprimée avec succès.");
                    } catch (Exception e) {
                        AlertUtils.showError("Erreur", "Une erreur est survenue lors de la suppression :\n" + e.getMessage());
                        throw new RuntimeException(e);
                    }
                }
            );
        } catch (Exception e){
            AlertUtils.showError("Erreur", "Une erreur est survenue lors de la suppression de la note : " + e.getMessage());
        }
    }

    private void showCommentEditDialog(String subject, String comment){
        try{
            //Create the text field
            DialogUtils.TextAreaField commentField = new DialogUtils.TextAreaField(
                "Commentaire :", comment != null ? comment : "");
            //Create the save button
            Button saveButton = new Button("Je modifie");
            //Save button action
            saveButton.setOnAction(e -> {
                try {
                    String newComment = commentField.getValue();
                    //If comment is empty
                    if (newComment.isEmpty()) {
                        AlertUtils.showInformation("Information", 
                            "Le commentaire ne peut pas être vide.\nPour supprimer le commentaire,\nutilisez le bouton Je supprime.");
                        commentField.focus();
                        //prevent dialog from closing
                        e.consume();
                        return;
                    }
                    //Verify if there is a change
                    if (comment != null && comment.equals(newComment)) {
                        AlertUtils.showInformation("Information", 
                            "Tu n'as rien changé.\nPour quitter la fenêtre sans modifier,\nclique sur J'annule.");
                        e.consume(); 
                        return;
                    }
                    //Update the comment in the database
                    commentService.updateComment(currentStudent.getStudentId(), subject, newComment);
                    refreshTable();
                    AlertUtils.showInformation("Succès", "Tes modifications ont bien été prises en compte.");            
                } catch (Exception ex){
                    AlertUtils.showError("Erreur", "Une erreur est survenue lors de la modification :\n" + ex.getMessage());
                    e.consume();
                }
            });
            //show dialog 
            DialogUtils.showTextAreaDialog("Modifier le commentaire", commentField, saveButton);    
        } catch (Exception e){
            AlertUtils.showError("Erreur", "Une erreur est survenue lors de l'édition du commentaire :\n" + e.getMessage());
        }
    }

    private void showCommentDeleteConfirmation(String subject){
        try{
            // Show confirmation dialog
            DialogUtils.showDeleteConfirmation(
                "Supprimer le commentaire",
                "Es-tu sûr de vouloir supprimer le commentaire pour la matière " + subject + " ?",
                () -> {
                    try {
                        //Delete comment from database
                        commentService.deleteComment(currentStudent.getStudentId(), subject);
                        refreshTable();
                        //Show success message
                        AlertUtils.showInformation("Succès", "Le commentaire a été supprimé avec succès.");
                    } catch (Exception e){
                        AlertUtils.showError("Erreur", "Une erreur est survenue lors de la suppression :\n" + e.getMessage());
                        throw new RuntimeException(e);
                    }
                }
            );
        } catch (Exception e){
            AlertUtils.showError("Erreur", "Une erreur est survenue lors de la suppression du commentaire : " + e.getMessage());
        }
    }

    //Disable all ui fields buttons and text area until user enter student ID
    private void disableGradeControls(boolean disable){
        subjectComboBox.setDisable(disable);
        gradeField.setDisable(disable);
        coefficientField.setDisable(disable);
        addGradeButton.setDisable(disable);
        commentArea.setDisable(disable);
        addCommentButton.setDisable(disable);
        searchButton.setDisable(disable);
        exportButton.setDisable(disable);
    }

     @Override
    protected List<SubjectResult> searchData(SearchCriteria criteria) {
        try{
            if (currentStudent == null){
                return new ArrayList<>();
            }
            return gradeService.searchBySubject(currentStudent.getStudentId(), criteria);
        } catch (Exception e){
            AlertUtils.showError("Erreur", "Une erreur est survenue lors de la recherche :\n" + e.getMessage());
            return new ArrayList<>();
        }
    }

    @Override
    protected int getTotalCount(SearchCriteria criteria) {
        try{
            if (currentStudent == null){
                return 0;
            }
            return gradeService.countBySubject(currentStudent.getStudentId(), criteria.getSearchValue()); 
        } catch (Exception e){
            AlertUtils.showError("Erreur", "Une erreur est survenue lors du comptage :\n" + e.getMessage());
            return 0;
        }
    }

    @Override
    protected void exportToCSV(List<SubjectResult> data, File file) {
        try {
            List<Student> studentData = new ArrayList<>();
            Student studentWithGrades = new Student();
            studentWithGrades.setStudentId(currentStudent.getStudentId());
            studentWithGrades.setFirstName(currentStudent.getFirstName());
            studentWithGrades.setLastName(currentStudent.getLastName());
            studentData.add(studentWithGrades);
            
            importExportService.exportToCSV(studentData, file);
            
        } catch (Exception e){
            AlertUtils.showError("Erreur", "Une erreur est survenue lors de l'export :\n" + e.getMessage());
        }
    }
  
    @Override
    protected void handleImport() {
        //Import functionality not required for this controller
        AlertUtils.showInformation("Information", "L'importation n'est pas disponible pour les notes d'étudiant.");
    }

    //Setters for dependency injection
    public void setStudentService(StudentService studentService){
        this.studentService = studentService;
    }

    public void setGradeService(GradeService gradeService){
        this.gradeService = gradeService;
    }

    public void setCommentService(SubjectCommentService commentService) {
        this.commentService = commentService;
    }

}