package GUI;


import javafx.scene.Node;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import model.Car;
import model.CardinalDirection;
import model.Grid;
import model.Intersection;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

/**
 *
 * @author Blake
 */
public class World {
    
    private static final int STREET_WIDTH = 10; //px
    
    private final StackPane root = new StackPane();
    private final StackPane carLayer = new StackPane();
    private final Canvas gridPane;
    
    
    public World(Grid grid){
        gridPane = new Canvas(grid.getEWBlockSize() * Grid.INTERSECTION_DISATANCE, grid.getNSBlockSize() * Grid.INTERSECTION_DISATANCE);
        drawGrid(grid);
        
        //adds all the layers to the root
        root.getChildren().setAll(gridPane, carLayer);
    }
    
    public Node getRoot(){
        return root;
    }
    
    private void addCar(Car car){
        
    }
    
    private void drawGrid(Grid grid){
        
        //Draws the black background
        GraphicsContext gc = gridPane.getGraphicsContext2D();
        int width = grid.getEWBlockSize() * Grid.INTERSECTION_DISATANCE;
        int height = grid.getEWBlockSize() * Grid.INTERSECTION_DISATANCE;
        gc.setFill(Color.WHITE);
        gc.fillRect(0, 0, width, height);
        
        //Draws all the intersections
        for(int i = 0; i < grid.getEWBlockSize(); i++){
            for(int j = 0; j < grid.getNSBlockSize(); j++){
                drawIntersection(grid.getIntersection(i, j));
            }
        }
    }
    
    private void drawIntersection(Intersection intersection){
        
        //sets up variables
        GraphicsContext gc = gridPane.getGraphicsContext2D();
        int gridSize = Grid.INTERSECTION_DISATANCE;
        int cellX = intersection.getEWBlock() * gridSize;
        int cellY = intersection.getNSBlock()* gridSize;
        
        //draws the NS street
        int NSx = cellX + (gridSize / 2) - (STREET_WIDTH / 2);
        int NSy = cellY;
        int NSw = STREET_WIDTH;
        int NSh = gridSize;
        //gc.setFill(intersection.getNSDirection() == CardinalDirection.NORTH ? Color.LIGHTGRAY : Color.DARKGRAY); //TODO: integrate the direction branch
        gc.setFill(Color.LIGHTGRAY);
        gc.fillRect(NSx, NSy, NSw, NSh);
        
        //draws the EW street
        int EWx = cellX;
        int EWy = cellY + (gridSize / 2) - (STREET_WIDTH / 2);
        int EWw = gridSize;
        int EWh = STREET_WIDTH;
        gc.setFill(Color.LIGHTGRAY);
        //gc.setFill(intersection.getEWDirection()== CardinalDirection.EAST ? Color.LIGHTGRAY : Color.DARKGRAY); //TODO: integrate the direction branch
        gc.fillRect(EWx, EWy, EWw, EWh);
        
        //draws the middle of the intersection
        int Mx = cellX + (gridSize / 2) - (STREET_WIDTH / 2);
        int My = cellY + (gridSize / 2) - (STREET_WIDTH / 2);
        int Mw = STREET_WIDTH;
        int Mh = STREET_WIDTH;
        gc.setFill(Color.BLACK);
        gc.fillRect(Mx, My, Mw, Mh);
        
        //draws the coordinate
        gc.setStroke(Color.BLUE);
        gc.strokeText("("+intersection.getEWBlock() + "," + intersection.getNSBlock() + ")", cellX+10, cellY+30);
    }
}
