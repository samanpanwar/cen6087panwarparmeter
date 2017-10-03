/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package GUI;

import com.blakeparmeter.cen6087application.application.Simulation;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.ScrollPane;
import javafx.scene.layout.VBox;

/**
 * FXML Controller class
 *
 * @author Blake
 */
public class MainWindowController implements Initializable {
    
    @FXML
    private VBox root;
    
    @FXML
    private ScrollPane canvasPane;
    
    private final World world = new World(Simulation.grid);
    
    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        canvasPane.setContent(world.getRoot());
    }   
}
