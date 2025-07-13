package com.studentmanagement.controller;

import static org.junit.jupiter.api.Assertions.assertNotNull;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;

import com.studentmanagement.utils.SearchCriteria;

@ExtendWith(MockitoExtension.class)
class BaseTableControllerUnitTest {
    //Simple test to verify that SearchCriteria can be created
    @Test
    void testSearchCriteriaCreation(){
        SearchCriteria criteria = new SearchCriteria("test");
        assertNotNull(criteria);
    }
}
