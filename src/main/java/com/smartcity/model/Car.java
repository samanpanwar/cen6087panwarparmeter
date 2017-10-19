/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.smartcity.model;

import com.smartcity.application.Simulation;
import java.math.BigInteger;

/**
 *
 * @author Blake
 */
public class Car {
    
    private static int carsCreated = 0;
    
    public final double velocity = 5;//distance units / time unit
    
    private final double entryTime;
    private final Route route;
    private final int carNum;
    private GridCoordinate position;
    private double direction;
    
    public Car(double entryTime, Route route){
        this.entryTime = entryTime;
        this.route = route;
        this.carNum = carsCreated++;
        initializePositionDirection();
    }
    
    public double getEntryTime(){
        return entryTime;
    }
    
    public Route getRoute(){
        return route;
    }
    
    public GridCoordinate getPosition(){
        return position;
    }
    
    public void setPosition(GridCoordinate position){
        this.position = position;
    }
    
    private void initializePositionDirection(){
        
        Intersection location = route.getIntersections().get(0);
        CardinalDirection cardinalDirection = Simulation.GRID.getEntryDirection(location);
        int gridSize = Grid.INTERSECTION_DISATANCE;
        int xCell = location.getEWBlock() * gridSize;
        int yCell = location.getNSBlock() * gridSize;
        
        //Direction Modifier
        int xDir;
        int yDir;
        int rotation;
        switch(cardinalDirection){
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
                throw new IllegalArgumentException(cardinalDirection + " is not handled");   
        }
                
        int x = xCell + xDir;
        int y = yCell + yDir;
    }

    @Override
    public String toString() {
        return "Car: " + carNum;
    }
    
    @Override
    public int hashCode() {
        return carNum;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final Car other = (Car) obj;
        return this.carNum == other.carNum;
    }
}
