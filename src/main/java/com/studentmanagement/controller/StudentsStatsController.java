package com.studentmanagement.controller;

import com.studentmanagement.service.StatisticsService;
import com.studentmanagement.chart.ChartSelector;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ContextMenu;
import javafx.scene.layout.BorderPane;

public class StudentsStatsController {

    @FXML
    private BorderPane mainContainer;
    @FXML
    private Button exportButton;

    private ChartSelector ChartSelector;
    private StatisticsService statisticsService;
    private ContextMenu exportMenu;
    
    /**
     * Init the controller
     */
    @FXML
    public void initialize(){
        //Will be called after injecting the statisticsService
    }

    public void setStatisticsService(StatisticsService statisticsService) {

        
    }
    
}
