package com.smartcity.gui;

import com.smartcity.application.Simulation;
import java.util.List;
import javafx.application.Platform;
import javafx.scene.Node;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import com.smartcity.model.Car;
import com.smartcity.application.enumeration.CardinalDirection;
import com.smartcity.model.Grid;
import com.smartcity.model.GridVector;
import com.smartcity.model.Intersection;
import com.smartcity.model.Intersection.LightState;
import com.smartcity.model.Route;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import javafx.animation.Interpolator;
import javafx.animation.PathTransition;
import javafx.animation.RotateTransition;
import javafx.scene.shape.ArcTo;
import javafx.scene.shape.LineTo;
import javafx.scene.shape.MoveTo;
import javafx.scene.shape.Path;
import javafx.util.Duration;

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
    
    private static final Map<Car, Rectangle> CAR_MAP = new HashMap();
    private static final Map<Intersection, List<Node>> INTERSECTION_LIGHT_MAP = new HashMap();
    
    private final Pane root = new Pane();
    private final Canvas roads;
    private final Grid grid;    
    
    public World(Grid grid){
        this.grid = grid;
        roads = new Canvas(grid.getEWBlockSize() * Simulation.INTERSECTION_DISTANCE, grid.getNSBlockSize() * Simulation.INTERSECTION_DISTANCE);
        drawGrid(grid);
        
        //adds all the layers to the root
        root.getChildren().add(roads);
    }
    
    public Node getRoot(){
        return root;
    }
    
    public void addCar(Car car){    
        if(Simulation.SHOW_GUI == false){
            return;
        }
        
        GridVector v = car.getVector();
        final Rectangle carObj = new Rectangle(v.ewPoint, v.nsPoint, 3, 5);
        carObj.setRotate(v.direction.getDegrees());
        carObj.setFill(Color.GREEN);
        CAR_MAP.put(car, carObj);
        
        Platform.runLater(()->{
            root.getChildren().add(carObj);
        });
    }
    
    public void moveCar(Car car, GridVector to, double time){
        if(Simulation.SHOW_GUI == false){
            return;
        }
        
        GridVector from = car.getVector();
        Rectangle carObj = CAR_MAP.get(car);
        if(carObj == null){
            throw new IllegalArgumentException("The car " + car + " cannot be found in the world.");
        }
        if(from.direction != to.direction){
            throw new IllegalArgumentException("This function can only move cars in a straight line");
        }
        
        Platform.runLater(()->{                
            Path path = new Path();
            path.getElements().add(new MoveTo(from.ewPoint, from.nsPoint));
            path.getElements().add(new LineTo(to.ewPoint, to.nsPoint));
            
            PathTransition pt = new PathTransition();
            pt.setInterpolator(Interpolator.LINEAR);
            pt.setNode(carObj);
            pt.setPath(path);
            pt.setDuration(Duration.millis(time * (1/Simulation.SIM_SPEED)));
            pt.play();
        });
    }
    
    public void turnCarTo(Car car, GridVector to, double time){
        if(Simulation.SHOW_GUI == false){
            return;
        }
        
        GridVector from = car.getVector();
        Rectangle carObj = CAR_MAP.get(car);
        if(carObj == null){
            throw new IllegalArgumentException("The car " + car + " cannot be found in the world.");
        }
        if(from.direction == to.direction){
            throw new IllegalArgumentException("This function can only move cars that are turning.");
        }
        
        Platform.runLater(()->{
            Path path = new Path();
            path.getElements().add(new MoveTo(from.ewPoint, from.nsPoint));
            double radius = Simulation.STREET_WIDTH;
            double duration = time * (1/Simulation.SIM_SPEED);
            path.getElements().add(new ArcTo(radius, radius, 90, to.ewPoint, to.nsPoint, false, true));
            
            PathTransition pt = new PathTransition();
            pt.setInterpolator(Interpolator.LINEAR);
            pt.setNode(carObj);
            pt.setPath(path);
            pt.setDuration(Duration.millis(duration));
            pt.play();
            
            RotateTransition rt = new RotateTransition();
            rt.setInterpolator(Interpolator.LINEAR);
            rt.setNode(carObj);
            rt.setFromAngle(from.direction.getDegrees());
            rt.setToAngle(to.direction.getDegrees());
            rt.setDuration(Duration.millis(duration));
            rt.play();
        });
    }
    
    public void removeCar(Car car){
        if(Simulation.SHOW_GUI == false){
            return;
        }
        Rectangle carObj = CAR_MAP.remove(car);
        if(carObj == null){
            throw new IllegalArgumentException("The car " + car + " cannot be found in the world.");
        }
        
        Platform.runLater(()->{
            root.getChildren().remove(carObj);
        });
    }
    
    public void renderIntersectionLights(Intersection intersection){
        if(Simulation.SHOW_GUI == false){
            return;
        }
        
        int lightDistance = 15;
        int gridSize = Simulation.INTERSECTION_DISTANCE;
        int xCenter = (intersection.getEWBlock() * gridSize) + (gridSize/2);
        int yCenter = (intersection.getNSBlock() * gridSize) + (gridSize/2);
        
        //Generate NS Light
        int NSXPos, NSYPos;
        double NSRotation;
        switch(intersection.getNSDirection()){
            case NORTH:
                NSXPos = xCenter;
                NSYPos = yCenter + lightDistance;
                NSRotation = 180;
                break;
                
            case SOUTH:
                NSXPos = xCenter;
                NSYPos = yCenter - lightDistance;
                NSRotation = 0;
                break;
                
            default:
                throw new IllegalArgumentException(intersection.getNSDirection() + " is not handled");
        }
        Rectangle NSLight = new Rectangle(NSXPos-8, NSYPos-5, 16, 10);
        NSLight.setFill(getColor(intersection.getNSLightState()));
        NSLight.setRotate(NSRotation);
        
        int EWXPos, EWYPos;
        double EWRotation;
        switch(intersection.getEWDirection()){
            case EAST:
                EWXPos = xCenter - lightDistance;
                EWYPos = yCenter;
                EWRotation = 270;
                break;
                
            case WEST:
                EWXPos = xCenter + lightDistance;
                EWYPos = yCenter;
                EWRotation = 90;
                break;
                
            default:
                throw new IllegalArgumentException(intersection.getNSDirection() + " is not handled");
        }
        
        Rectangle EWLight = new Rectangle(EWXPos-8, EWYPos-5, 16, 10);
        EWLight.setFill(getColor(intersection.getEWLightState()));
        EWLight.setRotate(EWRotation);
        
        Platform.runLater(()->{
            List<Node> oldLights = INTERSECTION_LIGHT_MAP.get(intersection);
            if(oldLights != null){
                root.getChildren().removeAll(oldLights);
            }
            List<Node> newLights = Arrays.asList(NSLight, EWLight);
            root.getChildren().addAll(newLights);
            INTERSECTION_LIGHT_MAP.put(intersection, newLights);
        });
    }
    
    public void drawRoute(Route route){
        if(Simulation.SHOW_GUI == false){
            return;
        }
        GraphicsContext gc = roads.getGraphicsContext2D();
        int gridSize = Simulation.INTERSECTION_DISTANCE;
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
        if(Simulation.SHOW_GUI == false){
            return;
        }
        GraphicsContext gc = roads.getGraphicsContext2D();
        int gridSize = Simulation.INTERSECTION_DISTANCE;
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
        if(Simulation.SHOW_GUI == false){
            return;
        }
        
        //Draws the black background
        GraphicsContext gc = roads.getGraphicsContext2D();
        int width = grid.getEWBlockSize() * Simulation.INTERSECTION_DISTANCE;
        int height = grid.getEWBlockSize() * Simulation.INTERSECTION_DISTANCE;
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
        if(Simulation.SHOW_GUI == false){
            return;
        }
        
        //sets up variables
        GraphicsContext gc = roads.getGraphicsContext2D();
        int gridSize = Simulation.INTERSECTION_DISTANCE;
        int streetWidth = Simulation.STREET_WIDTH;
        int cellX = intersection.getEWBlock() * gridSize;
        int cellY = intersection.getNSBlock()* gridSize;
        
        //draws the NS street
        int NSx = cellX + (gridSize / 2) - (streetWidth / 2);
        int NSy = cellY;
        int NSw = streetWidth;
        int NSh = gridSize;
        gc.setFill(intersection.getNSDirection() == CardinalDirection.NORTH ? Color.LIGHTGRAY : Color.DARKGRAY);
        gc.fillRect(NSx, NSy, NSw, NSh);
        
        //draws the EW street
        int EWx = cellX;
        int EWy = cellY + (gridSize / 2) - (streetWidth / 2);
        int EWw = gridSize;
        int EWh = streetWidth;
        gc.setFill(intersection.getEWDirection()== CardinalDirection.EAST ? Color.LIGHTGRAY : Color.DARKGRAY);
        gc.fillRect(EWx, EWy, EWw, EWh);
        
        //draws the middle of the intersection
        int Mx = cellX + (gridSize / 2) - (streetWidth / 2);
        int My = cellY + (gridSize / 2) - (streetWidth / 2);
        int Mw = streetWidth;
        int Mh = streetWidth;
        gc.setFill(Color.BLACK);
        gc.fillRect(Mx, My, Mw, Mh);
        
        //draws the coordinate
        gc.setStroke(Color.BLUE);
        gc.strokeText("("+intersection.getEWBlock() + "," + intersection.getNSBlock() + ")", cellX+10, cellY+30);
        
        //Draws the lights of the intersection
        renderIntersectionLights(intersection);
    }
    
    private static Color getColor(LightState state){
        double lightOpacity = .5;
        switch(state){
            case GREEN:
                return Color.color(0, 1, 0, lightOpacity);
                
            case RED:
                return Color.color(1, 0, 0, lightOpacity);
                
            case YELLOW:
                return Color.color(1, 1, 0, lightOpacity);
                
            default:
                throw new IllegalArgumentException("cannot determine the color for state: " + state);
        }
    }
}
