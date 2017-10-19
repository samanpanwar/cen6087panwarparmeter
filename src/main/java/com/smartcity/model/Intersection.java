/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.smartcity.model;

import com.smartcity.utility.VectorUtility;
import com.smartcity.event.LightChangeEvent.ChangeType;
import java.util.LinkedList;
import java.util.Queue;
import java.util.SortedSet;
import java.util.TreeSet;

/**
 *
 * @author Blake
 */
public class Intersection {
    
    public enum LightDirection{NS_BOUND, EW_BOUND};
    public enum LightState{RED, YELLOW, GREEN};
    
    private final static double carStopDistance = 8;//Distace between stopped cars
    
    private final int EWBlock, NSBlock;
    private final CardinalDirection NSDirection, EWDirection;
    private final SortedSet<Car> lightQueueNSBound, NSBoundCars, lightQueueEWBound, EWBoundCars;
    
    private LightState NSLightState, EWLightState;
    private LightDirection lightDirection;
    private GridVector center;
    
    public Intersection(int EWBlock, int NSBlock){
        this.EWBlock = EWBlock;
        this.NSBlock = NSBlock;
        if(EWBlock % 2 == 0){
            NSDirection = CardinalDirection.NORTH;
        } else { 
            NSDirection = CardinalDirection.SOUTH;
        }
        if(NSBlock % 2 == 0){
            EWDirection = CardinalDirection.WEST;
        } else { 
            EWDirection = CardinalDirection.EAST;
        }
        
        //helper variables
        center = VectorUtility.getCenter(this);
        
        //Light variables
        lightQueueNSBound = new TreeSet();
        NSBoundCars = new TreeSet();
        lightQueueEWBound = new TreeSet();
        EWBoundCars = new TreeSet();
        
        //Logic for initial light state, right now all start as NSBound, then all will tick to EWBound
        lightDirection = LightDirection.NS_BOUND;
        NSLightState = LightState.GREEN;
        EWLightState = LightState.RED;
    }
    
    public GridVector getYellowLightVector(Car car){
        double xModifier;
        double yModifier;
        double magnitude = car.stoppingDistance + (Grid.INTERSECTION_DISATANCE/2);
        switch(car.getVector().direction){
            
            case NORTH:
                xModifier = 0;
                yModifier = magnitude * -1;
                break;
                
            case EAST:
                xModifier = magnitude * -1;
                yModifier = 0;
                break;
            
            case SOUTH:
                xModifier = 0;
                yModifier = magnitude;
                break;
                
            case WEST:
                xModifier = magnitude;
                yModifier = 0;
                break;
            
            default:
                throw new IllegalArgumentException(car.getVector().direction + " is not handled");
        }
        return new GridVector(xModifier + center.ewPoint, yModifier + center.nsPoint, car.getVector().direction);
    }
    
    public int getNumCarsWaiting(CardinalDirection direction){
        switch(direction){
            case NORTH: case SOUTH:
                return this.lightQueueNSBound.size();
                
            case EAST: case WEST:
                return this.lightQueueEWBound.size();
                
            default :
                throw new IllegalArgumentException(direction + " is not handled");
        }
    }
    
    public GridVector StopCar(Car car){
        
        double xModifier;
        double yModifier;
        double magnitude = Grid.INTERSECTION_DISATANCE/2;
        
        //adds the car behind the last car 
        //TODO: there should be a check for the distance being too long
        Car lastCar = null;
        switch(car.getVector().direction){
            case NORTH: case SOUTH:
                if(!this.lightQueueNSBound.isEmpty()){
                    lastCar = this.lightQueueNSBound.last();
                }
                break;
            case EAST: case WEST:
                if(!this.lightQueueEWBound.isEmpty()){
                    lastCar = this.lightQueueEWBound.last();
                }
                break;
            default:
                throw new IllegalArgumentException(car.getVector().direction + " is not handled");
        }
        if(lastCar != null){
            magnitude += car.getVector().distanceTo(lastCar.getVector()) + carStopDistance;
        }
        
        switch(car.getVector().direction){
            
            case NORTH:
                xModifier = 0;
                yModifier = magnitude * -1;
                break;
                
            case EAST:
                xModifier = magnitude * -1;
                yModifier = 0;
                break;
            
            case SOUTH:
                xModifier = 0;
                yModifier = magnitude;
                break;
                
            case WEST:
                xModifier = magnitude;
                yModifier = 0;
                break;
            
            default:
                throw new IllegalArgumentException(car.getVector().direction + " is not handled");
        }    
        return new GridVector(xModifier + center.ewPoint, yModifier + center.nsPoint, car.getVector().direction);
    }
    
    /**
     * @param car
     * @param to
     */
    public void submitCar(Car car, Intersection to){
        
        if(EWBoundCars.contains(car)){
            throw new IllegalArgumentException(car + " is already in the EW queue");
        }
        if(NSBoundCars.contains(car)){
            throw new IllegalArgumentException(car + " is already in the NS queue");
        }
        
        CardinalDirection heading = VectorUtility.getDirectionTo(this, to);
        switch(heading){
                
            case EAST: case WEST:
                EWBoundCars.add(car);
                break;
            
            case NORTH: case SOUTH:
                NSBoundCars.add(car);
                break;
                
            default:
                throw new IllegalArgumentException(heading + "-bound headings are not handled.");
        }
    }
    
    /**
     * @param direction
     * @return the cars waiting, only given when the state is not initial
     */
    public Car[] getAndEmptyCars(LightDirection direction){
        switch(direction){
            case EW_BOUND:
                try{
                    return lightQueueEWBound.toArray(new Car[0]);
                } finally {
                    lightQueueEWBound.clear();
                }

            case NS_BOUND:
                try{
                    return lightQueueNSBound.toArray(new Car[0]);
                } finally {
                    lightQueueNSBound.clear();
                }
            default:
                throw new IllegalArgumentException(direction + " is not handled");
        }
    }
    
    /**
     * @param direction
     * @param changeType
     */
    public void setLightState(LightDirection direction, ChangeType changeType){
        
        if(changeType.equals(ChangeType.INITIAL)){
            if(direction.equals(lightDirection)){
                throw new IllegalArgumentException("The state was already set to " + direction);
            }
            lightDirection = direction;
        }
        
        switch(direction){
            case NS_BOUND:
                switch(changeType){
                    case INITIAL:
                        NSLightState = LightState.RED;      //should already be red
                        EWLightState = LightState.YELLOW;   
                        return;
                        
                    case ALL_RED:
                        NSLightState = LightState.RED;      //should already be red
                        EWLightState = LightState.RED;  
                        return;
                        
                    case GREEN:
                        NSLightState = LightState.GREEN;
                        EWLightState = LightState.RED;
                        return;
                        
                    default:
                        throw new IllegalArgumentException(changeType + " is not handled");
                }
                
            case EW_BOUND:
                switch(changeType){
                    case INITIAL:
                        NSLightState = LightState.YELLOW;
                        EWLightState = LightState.RED;      //should already be red
                        return;
                        
                    case ALL_RED:
                        NSLightState = LightState.RED;      //should already be red
                        EWLightState = LightState.RED;  
                        return;
                        
                    case GREEN:
                        NSLightState = LightState.RED;
                        EWLightState = LightState.GREEN;
                        return;
                    
                    default:
                        throw new IllegalArgumentException(changeType + " is not handled");
                }
            
            default: 
                throw new IllegalArgumentException(direction + " is not a handled light state");
                    
        }
    }
    
    public CardinalDirection getNSDirection(){
        return NSDirection;
    }
    
    public CardinalDirection getEWDirection(){
        return EWDirection;
    }
    
    public int getNSBlock(){
        return NSBlock;
    }
    
    public int getEWBlock(){
        return EWBlock;
    }
    
    public LightState getEWLightState(){
//        return EWLightState; 
        return LightState.GREEN;
    }
    
    public LightState getNSLightState(){
//        return NSLightState;
        return LightState.GREEN;
    }
    
    @Override
    public String toString(){
        return "EW: " + EWBlock + " NS: " + NSBlock;
    }
}
