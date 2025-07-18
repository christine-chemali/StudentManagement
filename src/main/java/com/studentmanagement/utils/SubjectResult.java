package com.studentmanagement.utils;

public class SubjectResult {
    private String subject;
    private String grades;
    private double studentAverage;
    private double classMinAverage;
    private double classMaxAverage;
    private String teacherComment;
    
    public SubjectResult() {
    }
    /**
     * Constructs a SubjectResult object with the specified parameters
     * @param subject the name of the subject
     * @param grades the grades received in the subject
     * @param studentAverage the average grade of the student
     * @param classMinAverage the minimum average grade in the class
     * @param classMaxAverage the maximum average grade in the class
     * @param teacherComment the teacher's comment on the student's performance
     */
    public SubjectResult(String subject, String grades, double studentAverage, 
                         double classMinAverage, double classMaxAverage, String teacherComment) {
        this.subject = subject;
        this.grades = grades;
        this.studentAverage = studentAverage;
        this.classMinAverage = classMinAverage;
        this.classMaxAverage = classMaxAverage;
        this.teacherComment = teacherComment;
    }
    
    /**
     * Gets the name of the subject
     * @return the name of the subject
     */
    public String getSubject() {
        return subject;
    }
    
    /**
     * Sets the name of the subject
     * @param subject the name of the subject to set
     */
    public void setSubject(String subject) {
        this.subject = subject;
    }
    
    /**
     * Gets the grades received in the subject
     * @return the grades received in the subject
     */
    public String getGrades() {
        return grades;
    }
    
    /**
     * Sets the grades received in the subject
     * @param grades the grades to set
     */
    public void setGrades(String grades) {
        this.grades = grades;
    }
    
    /**
     * Gets the average grade of the student
     * @return the average grade of the student
     */
    public double getStudentAverage() {
        return studentAverage;
    }
    
    /**
     * Sets the average grade of the student
     * @param studentAverage the average grade to set
     */
    public void setStudentAverage(double studentAverage) {
        this.studentAverage = studentAverage;
    }
    
    /**
     * Gets the minimum average grade in the class
     * @return the minimum average grade in the class
     */
    public double getClassMinAverage() {
        return classMinAverage;
    }
    
    /**
     * Sets the minimum average grade in the class
     * @param classMinAverage the minimum average grade to set
     */
    public void setClassMinAverage(double classMinAverage) {
        this.classMinAverage = classMinAverage;
    }
    
    /**
     * Gets the maximum average grade in the class
     * @return the maximum average grade in the class
     */
    public double getClassMaxAverage() {
        return classMaxAverage;
    }
    
    /**
     * Sets the maximum average grade in the class
     * @param classMaxAverage the maximum average grade to set
     */
    public void setClassMaxAverage(double classMaxAverage) {
        this.classMaxAverage = classMaxAverage;
    }
    
    /**
     * Gets the teacher's comment on the student's performance
     * @return the teacher's comment
     */
    public String getTeacherComment() {
        return teacherComment;
    }
    
    /**
     * Sets the teacher's comment on the student's performance
     * @param teacherComment the comment to set
     */
    public void setTeacherComment(String teacherComment) {
        this.teacherComment = teacherComment;
    }
    
    /**
     * @return Returns a string representation of the SubjectResult
     */
    @Override
    public String toString() {
        return "SubjectResult{" +
                "subject='" + subject + '\'' +
                ", grades='" + grades + '\'' +
                ", studentAverage=" + studentAverage +
                ", classMinAverage=" + classMinAverage +
                ", classMaxAverage=" + classMaxAverage +
                ", teacherComment='" + teacherComment + '\'' +
                '}';
    }
}