/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.smartcity.model;

import java.util.LinkedList;
import java.util.Queue;

/**
 *
 * @author Blake
 */
public class Intersection {
    
    public enum LightDirection{NS_BOUND, EW_BOUND};
    public enum LightState{RED, YELLOW, GREEN};
    
    private final int EWBlock, NSBlock;
    private final CardinalDirection NSDirection, EWDirection;
    private final Queue<Car> lightQueueNSBound, lightQueueEWBound;
    
    private LightState NSLightState, EWLightState;
    private LightDirection lightDirection;
    
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
        
        //Light variables
        lightQueueNSBound = new LinkedList();
        lightQueueEWBound = new LinkedList();
        
        //Logic for initial light state, right now all start as NSBound, then all will tick to EWBound
        lightDirection = LightDirection.NS_BOUND;
        NSLightState = LightState.GREEN;
        EWLightState = LightState.RED;
    }
    
    /**
     * @param car
     * @param to
     * @return true = the car can move though the intersection 
     */
    public boolean submitCar(Car car, Intersection to){
        CardinalDirection heading = getDirectionTo(to);
        switch(heading){
            
            case NORTH: case SOUTH:
                if(NSLightState == LightState.GREEN){
                    return true;
                } else {
                    lightQueueNSBound.add(car);
                    return false;
                }
                
            case EAST: case WEST:
                if(EWLightState == LightState.GREEN){
                    return true;
                } else {
                    lightQueueEWBound.add(car);
                    return false;
                }
                
            default:
                throw new IllegalArgumentException(heading + "-bound headings are not handled.");
        }
    }
    
    /**
     * @param direction
     * @param isInitial the opposing direction will turn yellow, if not initial
     * the direction will turn green
     * @return the cars waiting, only given when the state is not initial
     */
    public Car[] setLightState(LightDirection direction, boolean isInitial){
        
        if(isInitial && direction.equals(lightDirection)){
            throw new IllegalArgumentException("The state was already set to " + direction);
        }
        
        switch(direction){
            case NS_BOUND:
                if(isInitial){
                    NSLightState = LightState.RED;      //should already be red
                    EWLightState = LightState.YELLOW;   
                    lightDirection = direction;
                    return null;
                } else {
                    NSLightState = LightState.GREEN;
                    EWLightState = LightState.RED;
                    try{
                        return lightQueueNSBound.toArray(new Car[0]);
                    } finally {
                        lightQueueNSBound.clear();
                    }
                }
                
            case EW_BOUND:
                if(isInitial){
                    NSLightState = LightState.YELLOW;
                    EWLightState = LightState.RED;      //should already be red
                    lightDirection = direction;
                    return null;
                } else {
                    NSLightState = LightState.RED;
                    EWLightState = LightState.GREEN;
                    try{
                        return lightQueueEWBound.toArray(new Car[0]);
                    } finally {
                        lightQueueEWBound.clear();
                    }
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
    
    public CardinalDirection getDirectionTo(Intersection intersection){
        
        if(this.equals(intersection)){
            throw new IllegalArgumentException("The passed in intersecion is equal to this.");
        }
        
        int otherNSBlock = intersection.NSBlock;
        int otherEWBlock = intersection.EWBlock;
        if(EWBlock == otherEWBlock){
            if(NSBlock > otherNSBlock){
                return CardinalDirection.NORTH;
            } else {
                return CardinalDirection.SOUTH;
            }
        }else if(NSBlock == otherNSBlock){
            if(EWBlock > otherEWBlock){
                return CardinalDirection.WEST;
            } else {
                return CardinalDirection.EAST;
            }
        } else {
            throw new IllegalArgumentException("The passed in intersection is not orthogonal to this intersection");
        }
    }
    
    public LightState getEWLightState(){
        return EWLightState; 
    }
    
    public LightState getNSLightState(){
        return NSLightState;
    }
    
    @Override
    public String toString(){
        return "EW: " + EWBlock + " NS: " + NSBlock;
    }
}
