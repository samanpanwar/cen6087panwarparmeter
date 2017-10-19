/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.smartcity.gui;

import com.smartcity.application.Simulation;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.ScrollPane;

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
}
