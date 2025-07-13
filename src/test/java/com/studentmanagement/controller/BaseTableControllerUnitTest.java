package com.studentmanagement.controller;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;

import com.studentmanagement.service.ImportExportService;
import com.studentmanagement.utils.SearchCriteria;

@ExtendWith(MockitoExtension.class)
class BaseTableControllerUnitTest {

    //Simple test to verify that SearchCriteria can be created
    @Test
    void testSearchCriteriaCreation(){
        SearchCriteria criteria = new SearchCriteria("test");
        assertNotNull(criteria);
    }

    //Test to verify that import/export can be created
    @Test
    void testImportExportServiceCreation(){
        ImportExportService service = new ImportExportService();
        assertNotNull(service);
    }

    //Test to verify the number of pages calculation
    @Test
    void testPageCalculation(){
        int totalItems = 30;
        int itemsPerPage = 15;
        int expectedPages = (totalItems + itemsPerPage -1) / itemsPerPage;
        assertEquals(2, expectedPages);
    }

    //More test for the calculate formula of page number with different values
    @Test
    void testPageCalculationFormula(){
        int itemsPerPage = 15;
        //for 0 elements, formula should return 0 but in the app it return almost 1
        assertEquals(0, (0 + itemsPerPage - 1) / itemsPerPage);
        //for all the other cases
        assertEquals(1, (1 + itemsPerPage - 1) / itemsPerPage, "1 élément devrait donner 1 page");
        assertEquals(1, (15 + itemsPerPage -1) / itemsPerPage, "15 éléments devraient donner 1 page");
        assertEquals(2, (16 + itemsPerPage -1) / itemsPerPage, "16 éléments devraient donner 2 pages");
        assertEquals(2, (30 + itemsPerPage - 1) / itemsPerPage, "30 éléments devraiend donner 2 pages");
        assertEquals(3, (30 + itemsPerPage - 1) / itemsPerPage, "31 éléments devraient donner 3 pages");
    }
}
