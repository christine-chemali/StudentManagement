package com.studentmanagement.service;

import com.studentmanagement.model.Grade;
import com.studentmanagement.utils.SearchCriteria;
import com.studentmanagement.utils.SubjectResult;

import java.util.ArrayList;
import java.util.List;

public class GradeService {
    
    public GradeService() {
    }
    
    public List<SubjectResult> searchBySubject(Long studentId, SearchCriteria criteria) {
        return new ArrayList<>();
    }
    
    public void saveGrade(Grade grade) {
    }
    
    public void updateGrade(Long studentId, String subject, double oldValue, double oldCoeff, 
                           double newValue, double newCoeff) {
    }
    
    public void deleteGrade(Long studentId, String subject, double value, double coefficient) {
    }
    
    public int countBySubject(Long studentId, String searchValue) {
        return 13;
    }
    
}