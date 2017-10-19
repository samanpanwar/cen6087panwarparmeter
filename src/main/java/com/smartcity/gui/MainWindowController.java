/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.smartcity.gui;

import com.smartcity.application.Simulation;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.ScrollPane;
import javafx.scene.input.ScrollEvent;

/**
 * FXML Controller class
 *
 * @author Blake
 */
public class MainWindowController implements Initializable {
    
    @FXML
    private ScrollPane canvasPane;
    
    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        canvasPane.setContent(Simulation.WORLD.getRoot());
        Simulation.start();
    }
    
    @FXML
    private void resetZoom(ActionEvent event){
        Simulation.WORLD.getRoot().setScaleX(1);
        Simulation.WORLD.getRoot().setScaleY(1);
    }
    
    @FXML
    private void zoomOut(ActionEvent event){
        Simulation.WORLD.getRoot().setScaleX(Simulation.WORLD.getRoot().getScaleX()*.95);
        Simulation.WORLD.getRoot().setScaleY(Simulation.WORLD.getRoot().getScaleY()*.95);
    }
    
    @FXML
    private void zoomIn(ActionEvent event){
        Simulation.WORLD.getRoot().setScaleX(Simulation.WORLD.getRoot().getScaleX()*1.1);
        Simulation.WORLD.getRoot().setScaleY(Simulation.WORLD.getRoot().getScaleY()*1.1);
    }
    
    @FXML
    private void scroll(ScrollEvent event){
//        Simulation.WORLD.getRoot().setScaleX(Simulation.WORLD.getRoot().getScaleX()*event.getDeltaY());
//        Simulation.WORLD.getRoot().setScaleY(Simulation.WORLD.getRoot().getScaleY()*event.getDeltaY());
//        event.consume();
    }
}
