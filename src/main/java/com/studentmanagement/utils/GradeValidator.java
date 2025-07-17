package com.studentmanagement.utils;

import com.studentmanagement.model.Grade;
import com.studentmanagement.model.SubjectComment;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;

public class GradeValidator {
    public static class ValidationResult{
        private boolean valid;
        private String errorMessage;
        private Object focusField;

        /**
         * Constructs a ValidationResult
         * @param valid indicates if the validation passes
         * @param errorMessage the error message if validation failed
         * @param focusField the field to focus on if validation failed
         */
        public ValidationResult(boolean valid, String errorMessage, Object focusField){
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
         * @return the error message
         */
        public String getErrorMessage(){
            return errorMessage;
        }

        /**
         * Gets the field that should receive focus
         * @return the focus field
         */
        public Object getFocusField(){
            return focusField;
        }
    }

    /**
     * Validates the input for grade and coefficient fields
     * @param gradeField the field for the grade input
     * @param coefficientField the field for the coefficient input
     * @return ValidationResult indicating the validation outcome
     */
    public static ValidationResult validateGradeInput(TextField gradeField, TextField coefficientField){
        String gradeText = gradeField.getText().trim();
        String coeffText = coefficientField.getText().trim();
        //Check if fields are empty
        if(gradeText.isEmpty() && coeffText.isEmpty()){
            return new ValidationResult(false, "Tu n'as rien modifié.\nSi tu veux quitter cette fenêtre,\nclique sur J'annule.", null);
        }
        if(gradeText.isEmpty()){
            return new ValidationResult(false, "La note ne peut pas être vide", gradeField);
        }
        if(coeffText.isEmpty()){
            return new ValidationResult(false, "Le coefficient ne peut pas être vide.", coefficientField);
        }
        //Validate grade
        try{
            double grade = Double.parseDouble(gradeText);
            if(grade < 0 || grade > 20) {
                return new ValidationResult(false, "La note doit être comprise entre 0 et 20.", gradeField);
            }
        } catch(NumberFormatException e){
            return new ValidationResult(false, "La note doit être un nombre valide.", gradeField);
        }
        //Validate coefficient
        try {
            double coeff = Double.parseDouble(coeffText);
            if(coeff < 0 || coeff > 5 ){
                return new ValidationResult(false, "Le coefficient doit être compris entre 0 et 5.", coefficientField);
            }
        }catch (NumberFormatException e){
            return new ValidationResult(false, "Le coefficient doit être un nombre valide.", coefficientField);
        }
        return new ValidationResult(true, null, null);
    }

    /**
     * Check  if the grade or coefficient values have changed
     * @param originalGrade the original grade value
     * @param originalCoeff the original coefficient value
     * @param gradeField the field for the grade input
     * @param coefficientField the field for the coefficient input
     * @return ValidationResult indicating if changes were detected
     */
    public static ValidationResult checkForChanges(double originalGrade, double originalCoeff, TextField gradeField, TextField coefficientField){
        try{
            double newGrade = Double.parseDouble(gradeField.getText().trim());
            double newCoeff = Double.parseDouble(coefficientField.getText().trim());
            if(newGrade == originalGrade && newCoeff == originalCoeff){
                return new ValidationResult(false, "Tu n'as rien modifié.\nSi tu veux quitter cette fenêtre,\nclique sur J'annule.", null);
            }
            return new ValidationResult(true, null, null);
        } catch (NumberFormatException e){
            //This should not happen if validateGradeInput is called first
            return new ValidationResult(false, "Format de nombre invalide.", null);
        }
    }
    
    /**
     * Creates a Grade object from the validated fields
     * @param studentId The ID of the student
     * @param subject the subject of the grade
     * @param gradeField the field for the grade input
     * @param coefficientField the field for the coefficient input
     * @return the created Grade object
     */
    public static Grade createGradeFromFields(Long studentId, String subject, 
                                            TextField gradeField, TextField coefficientField) {
        Grade grade = new Grade();
        grade.setStudentId(studentId);
        grade.setSubject(subject);
        grade.setValue(Double.parseDouble(gradeField.getText().trim()));
        grade.setCoefficient(Double.parseDouble(coefficientField.getText().trim()));
        return grade;
    }
    
    /**
     * Creates a SubjectComment object from the validated field
     * @param studentId the ID of the student
     * @param subject the subject of the comment
     * @param commentArea the TextArea for the comment input
     * @return the created SubjectComment object
     */
    public static SubjectComment createCommentFromField(Long studentId, String subject, TextArea commentArea) {
        SubjectComment comment = new SubjectComment();
        comment.setStudentId(studentId);
        comment.setSubject(subject);
        comment.setComment(commentArea.getText().trim());
        return comment;
    }
}
