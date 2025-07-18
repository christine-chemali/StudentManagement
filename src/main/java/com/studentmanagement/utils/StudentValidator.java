package com.studentmanagement.utils;

import com.studentmanagement.model.Student;
import javafx.scene.control.TextField;

public class StudentValidator {
    
    public static class ValidationResult{
        private boolean valid;
        private String errorMessage;
        private TextField focusField;

        /**
         * Construct a ValidationResult object
         * @param valid indicates if the validation passed
         * @param errorMessage the error message if validation failed
         * @param focusField the TextField that should receive focus if validation fails
         */
        public ValidationResult(boolean valid, String errorMessage, TextField focusField){
            this.valid = valid;
            this.errorMessage = errorMessage;
            this.focusField = focusField;
        }

        /**
         * Checks if the validation passed
         * @return true if valid, false otherwise
         */
        public boolean isValid(){
            return valid;
        }

        /**
         * Gets the error message
         * @return the error message if validation failed
         */
        public String getErrorMessage(){
            return errorMessage;
        }

        /**
         * Gets the TextField that should receive focus if validation fails
         * @return the focus field
         */
        public TextField getFocusField(){
            return focusField;
        }
    }

    /**
     * Validates the field for creating a new student
     * @param firstNameField The TextField for the first name
     * @param lastNameField the TextField for the last name
     * @param ageField the TextField for age
     * @param classNameField the TextField for class name
     * @return ValidationResult indicating the result of the validation
     */
    public static ValidationResult validateForCreation(TextField firstNameField, TextField lastNameField, TextField ageField, TextField classNameField){
        //Verify if all fields are full
        String firstName = firstNameField.getText().trim();
        String lastName = lastNameField.getText().trim();
        String ageText = ageField.getText().trim();
        String className = classNameField.getText().trim();
        
        if(firstName.isEmpty() || lastName.isEmpty() || ageText.isEmpty() || className.isEmpty()){
            return new ValidationResult(false, "Tous les champs doivent être remplis !", firstNameField);
        }
        //validate each field
        ValidationResult result = validateFirstName(firstName, firstNameField);
        if(!result.isValid()) return result;
        result = validateLastName(lastName, lastNameField);
        if(!result.isValid()) return result;
        result = validateAge(ageText, ageField);
        if(!result.isValid()) return result;
        result = validateClassName(className, classNameField);
        if(!result.isValid()) return result;

        return new ValidationResult(true, null, null);
    }

    /**
     * Validates and applies changes to the student object based on the provided fields
     * @param student the Student object to update
     * @param firstNameField the TextField for the first name
     * @param lastNameField the TextField for the last name
     * @param ageField the TextField for age
     * @param classNameField the TextField for class name
     * @return ValidationResult indicating the result of the validation changes
     */
    public static ValidationResult validateAndApplyChanges(Student student, TextField firstNameField, TextField lastNameField, TextField ageField, TextField classNameField){
        boolean hasChanges = false;

        String firstName = firstNameField.getText().trim();
        if(!firstName.isEmpty()){
            ValidationResult result = validateFirstName(firstName, firstNameField);
            if(!result.isValid()) return result;
            firstName = formatName(firstName);
            if(!firstName.equals(student.getFirstName())){
                student.setFirstName(firstName);
                hasChanges = true;
            }
        }

        String lastName = lastNameField.getText().trim();
        if(!lastName.isEmpty()){
            ValidationResult result = validateLastName(lastName, lastNameField);
            if(!result.isValid()) return result;
            lastName = formatName(lastName);
            if(!lastName.equals(student.getLastName())){
                student.setLastName(lastName);
                hasChanges = true;
            }
        }

        String ageText = ageField.getText().trim();
        if(!ageText.isEmpty()){
            ValidationResult result = validateAge(ageText, ageField);
            if(!result.isValid()) return result;
            int age = Integer.parseInt(ageText);
            if(age != student.getAge()){
                student.setAge(age);
                hasChanges = true;
            }
        }

        String className = classNameField.getText().trim();
        if(!className.isEmpty()){
            ValidationResult result = validateClassName(className, classNameField);
            if(!result.isValid()) return result;
            className = formatClassName(className);
            if(className.equals(student.getClassName())){
                student.setClassName(className);
                hasChanges = true;
            }
        }
        if(!hasChanges){
            return new ValidationResult(false, "Tu n'as rien modifié !\nSi tu veux fermer cette fenêtre, clique sur Annuler", null);
        }
        return new ValidationResult(true, null, null);
    }
   
    /**
     * Validates the first name input
     * @param firstName the first name to validate
     * @param field the TextField associated with the first name
     * @return ValidationResult indicating the result of the validation
     */
    private static ValidationResult validateFirstName(String firstName, TextField field) {
        if(!firstName.matches("^[a-zA-ZÀ-ÿ\\s-]+$")) {
            return new ValidationResult(false, "Oups !\nLe prénom doit contenir uniquement des lettres.\nLe trait d'union est autorisé pour les prénoms composés.", field);
        }
        return new ValidationResult(true, null, null);
    }

    /**
     * Validate the last name input
     * @param lastName the last name to validate
     * @param field the TextField associated with the last name
     * @return ValidationRestult indicating the result of validation
     */
    private static ValidationResult validateLastName(String lastName, TextField field) {
        if(!lastName.matches("^[a-zA-ZÀ-ÿ\\s-]+$")) {
            return new ValidationResult(false, "Oups!\nLe nom doit contenir uniquement des lettres et des espaces.\nLe trait d'union est autorisé pour les noms composés comme Dupont-Martin !", field);
        }
        return new ValidationResult(true, null, null);
    }

    /**
     * Validates the age input
     * @param ageText the age input as text
     * @param field the TextField associated with the age
     * @return ValidationResult indicating the result of the validation
     */
    private static ValidationResult validateAge(String ageText, TextField field) {
         try{
            int age = Integer.parseInt(ageText);
            if(age <= 3 || age >= 150) {
                return new ValidationResult(false, "Rappelle toi !\nNous n'acceptons pas les étudiants de moins de 3 ans\nni ceux de plus de 150 ans !", field);
            }
        }catch (NumberFormatException e) {
            return new ValidationResult(false, "L'âge doit être un nombre entier\n5 ans 1/2 c'est pas possible !", field);
        }
        return new ValidationResult(true, null, null);
    }

    /**
     * Validates the class name input
     * @param className the class name to validate
     * @param field the TextField associated with the class name
     * @return ValidationResult indicating the result of the validation
     */
    private static ValidationResult validateClassName(String className, TextField field) {
         String cleanClassName = className.replaceAll("\\s+", "");
        if(!cleanClassName.matches("^[a-zA-Z][0-9]$") && !cleanClassName.matches("^[0-9][a-zA-Z]$")){
            return new ValidationResult(false, "La classe doit contenir une lettre et un chiffre.\nExemples valides : B1, 1B, A3, 2C...\nPas d'espaces ni de caractères spéciaux !", field);
        }
        return new ValidationResult(true, null, null);
    }

    /**
     * Formats the name by capitalizong the first letter and correcting spacing around hyphens
     * @param name the name to format
     * @return the formatted name
     */
    private static String formatName(String name){
        name = name.replaceAll("\\s*-\\s*", "-");
        return name.substring(0, 1).toUpperCase() + name.substring(1).toLowerCase();
    }

    /**
     * Formats the class name by correcting spacing and capitalization
     * @param className the class name to format
     * @return the formatted class name
     */
    private static String formatClassName(String className){
        className = className.replaceAll("\\s+", "");
        if (className.matches("^[a-zA-Z][0-9]$")) {
            return className.substring(0, 1).toUpperCase() + className.substring(1);
        } else {
            return className.substring(0, 1) + className.substring(1).toUpperCase();
        }
    }

    /**
     * Creates a Sudent object form the provided TextFields
     * @param firstNameField the TextField for the first name
     * @param lastNameField the TextField for the last name
     * @param ageField the TextField for age
     * @param classNameField the TextField for class name
     * @return a new Student object populated with the provided data
     */
    public static Student createStudentFromFields(TextField firstNameField, TextField lastNameField, 
                                                 TextField ageField, TextField classNameField) {
        Student student = new Student();
        student.setFirstName(formatName(firstNameField.getText().trim()));
        student.setLastName(formatName(lastNameField.getText().trim()));
        student.setAge(Integer.parseInt(ageField.getText().trim()));
        student.setClassName(formatClassName(classNameField.getText().trim()));
        return student;
    }
}