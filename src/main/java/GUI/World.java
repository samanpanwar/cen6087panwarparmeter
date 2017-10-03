package GUI;

import java.util.List;
import javafx.animation.PathTransition;
import javafx.application.Platform;
import javafx.scene.Node;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.scene.shape.LineTo;
import javafx.scene.shape.MoveTo;
import javafx.scene.shape.Path;
import javafx.scene.shape.Rectangle;
import javafx.util.Duration;
import model.Car;
import model.CardinalDirection;
import model.Grid;
import model.Intersection;
import model.Route;

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
    private final Canvas canvas;
    private final Grid grid;    
    
    public World(Grid grid){
        this.grid = grid;
        canvas = new Canvas(grid.getEWBlockSize() * Grid.INTERSECTION_DISATANCE, grid.getNSBlockSize() * Grid.INTERSECTION_DISATANCE);
        drawGrid(grid);
        
        //adds all the layers to the root
        root.getChildren().setAll(canvas, carLayer);
    }
    
    public Node getRoot(){
        return root;
    }
    
    public void addCar(Car car){        
        
        Intersection location = car.getRoute().getIntersections().get(0);
        CardinalDirection direction = CardinalDirection.EAST;
        int gridSize = Grid.INTERSECTION_DISATANCE;
        int xCell = location.getEWBlock() * gridSize;
        int yCell = location.getNSBlock() * gridSize;
        
        //Direction Modifier
        int xDir;
        int yDir;
        int rotation;
        switch(direction){
            case NORTH:
                xDir = gridSize/2;
                yDir = gridSize;
                rotation = 0;
                break;
                
            case EAST:
                xDir = 0;
                yDir = gridSize/2;
                rotation = 90;
                break;
                
            case SOUTH:
                xDir = gridSize/2; 
                yDir = 0;
                rotation = 180;
                break; 
                
            case WEST:
                xDir = gridSize;
                yDir = gridSize/2;
                rotation = 270;
                break;
                
            default:
                throw new IllegalArgumentException(direction + " is not handled");   
        }
                
        int x = xCell + xDir;
        int y = yCell + yDir;
        final Rectangle carObj = new Rectangle(x, y, 3, 5);
        carObj.setRotate(rotation);
        carObj.setFill(Color.GREEN);
        System.out.println(carObj);
        
        Platform.runLater(()->{
            carLayer.getChildren().add(carObj);
            
            Path path = new Path();
            path.getElements().add(new MoveTo(x, y));
            path.getElements().add(new LineTo(x, y+100));
            
            PathTransition pt = new PathTransition();
            pt.setNode(carObj);
            pt.setPath(path);
            pt.setDuration(Duration.seconds(5));
            pt.play();
        });


    }
    
    public void drawRoute(Route route){
        GraphicsContext gc = canvas.getGraphicsContext2D();
        int gridSize = Grid.INTERSECTION_DISATANCE;
        Intersection last = null;
        for(Intersection inter : route.getIntersections()){
            int x1 = (inter.getEWBlock() * gridSize) + (gridSize /2);
            int y1 = (inter.getNSBlock() * gridSize) + (gridSize /2);
            if(last != null){
                int x2 = (last.getEWBlock() * gridSize) + (gridSize /2);
                int y2 = (last.getNSBlock() * gridSize) + (gridSize /2);
                gc.setStroke(Color.RED);
                gc.strokeLine(x1, y1, x2, y2);
            } else {
                gc.setStroke(Color.RED);
                gc.strokeText("START", x1, y1+15);
            }
            last = inter;
        }
    }
    
    public void drawEntriesAndExits(){
        GraphicsContext gc = canvas.getGraphicsContext2D();
        int gridSize = Grid.INTERSECTION_DISATANCE;
        for(List<Intersection> entrances : grid.getEntryIntersections()){
            for(Intersection entry : entrances){
                int x1 = (entry.getEWBlock() * gridSize) + (gridSize /2);
                int y1 = (entry.getNSBlock() * gridSize) + (gridSize /2);
                gc.setStroke(Color.RED);
                gc.strokeText("ENTRY", x1, y1);
            }
        }
        for(List<Intersection> exits : grid.getExitIntersections()){
            for(Intersection exit : exits){
                int x1 = (exit.getEWBlock() * gridSize) + (gridSize /2);
                int y1 = (exit.getNSBlock() * gridSize) + (gridSize /2);
                gc.setStroke(Color.RED);
                gc.strokeText("EXIT", x1, y1);
            }
        }
        
    }
    
    private void drawGrid(Grid grid){
        
        //Draws the black background
        GraphicsContext gc = canvas.getGraphicsContext2D();
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
        GraphicsContext gc = canvas.getGraphicsContext2D();
        int gridSize = Grid.INTERSECTION_DISATANCE;
        int cellX = intersection.getEWBlock() * gridSize;
        int cellY = intersection.getNSBlock()* gridSize;
        
        //draws the NS street
        int NSx = cellX + (gridSize / 2) - (STREET_WIDTH / 2);
        int NSy = cellY;
        int NSw = STREET_WIDTH;
        int NSh = gridSize;
        gc.setFill(intersection.getNSDirection() == CardinalDirection.NORTH ? Color.LIGHTGRAY : Color.DARKGRAY);
        gc.fillRect(NSx, NSy, NSw, NSh);
        
        //draws the EW street
        int EWx = cellX;
        int EWy = cellY + (gridSize / 2) - (STREET_WIDTH / 2);
        int EWw = gridSize;
        int EWh = STREET_WIDTH;
        gc.setFill(intersection.getEWDirection()== CardinalDirection.EAST ? Color.LIGHTGRAY : Color.DARKGRAY);
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
