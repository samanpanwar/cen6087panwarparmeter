/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.smartcity.model;

import com.smartcity.application.enumeration.CardinalDirection;
import com.smartcity.application.Simulation;
import com.smartcity.application.enumeration.LightDirection;
import com.smartcity.utility.VectorUtility;
import com.smartcity.event.LightChangeEvent.ChangeType;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;

/**
 *
 * @author Blake
 */
public class Intersection {
    
    public enum LightState{RED, YELLOW, GREEN};
    
    private final GridVector center;
    private final int EWBlock, NSBlock;
    private final CardinalDirection NSDirection, EWDirection;
    private final LinkedList<Car> lightQueueNSBound, NSBoundCars, lightQueueEWBound, EWBoundCars;
    private final List<Convoy> convoysCrossing = new ArrayList();
    
    private LightState NSLightState, EWLightState;
    private LightDirection lightDirection;
    private boolean isSwitching = false;
//    private boolean convoyLocked = false;
    private double lastGreenChange, currentDequeueFinishTime;
    
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
        lightQueueNSBound = new LinkedList();
        NSBoundCars = new LinkedList();
        lightQueueEWBound = new LinkedList();
        EWBoundCars = new LinkedList();
        
        //Logic for initial light state, right now all start as NSBound, then all will tick to EWBound
        lightDirection = LightDirection.NS_BOUND;
        NSLightState = LightState.GREEN;
        EWLightState = LightState.RED;
    }

    public boolean isConvoyLocked() {
        return !convoysCrossing.isEmpty();
    }
    
    public void addConvoy(Convoy convoy){
        convoysCrossing.add(convoy);
    }
    
    public void removeConvoy(Convoy convoy){
        convoysCrossing.remove(convoy);
    }
    
    public double getLastGreenChange() {
        return lastGreenChange;
    }

    public void setLastGreenChange(double lastGreenChange) {
        this.lastGreenChange = lastGreenChange;
    }

    public double getCurrentDequeueFinishTime() {
        return currentDequeueFinishTime;
    }

    public void setCurrentDequeueFinishTime(double currentDequeueFinishTime) {
        this.currentDequeueFinishTime = currentDequeueFinishTime;
    }
    
    public void setSwitching(boolean switching){
        isSwitching = switching;
    }
    
    public boolean getIsSwitching(){
        return isSwitching;
    }
    
    public LightDirection getLightDirection(){
        return lightDirection;
    }
    
    public GridVector getYellowLightVector(Car car){
        double magnitude = car.stoppingDistance + (Simulation.STREET_WIDTH/2);
        return VectorUtility.addMagnitude(center, car.getVector().direction.getOppisite(), magnitude);
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
    
    public GridVector getNextCarStopVector(Car car){
        double magnitude = Simulation.STREET_WIDTH;
        
        //adds the car behind the last car 
        //TODO: there should be a check for the distance being too long
        Car lastCar;
        switch(car.getVector().direction){
            case NORTH: case SOUTH:
                lastCar = lightQueueNSBound.peekLast();
                lightQueueNSBound.add(car);
                break;
            case EAST: case WEST:
                lastCar = lightQueueEWBound.peekLast();
                lightQueueEWBound.add(car);
                break;
            default:
                throw new IllegalArgumentException(car.getVector().direction + " is not handled");
        }
        if(lastCar != null){
            magnitude = center.distanceTo(lastCar.getVector()) + Simulation.CAR_STOP_DISTANCE;
        }
        
        return VectorUtility.addMagnitude(center, car.getVector().direction.getOppisite(), magnitude);
    }
    
    public GridVector getCenter(){
        return center;
    }
    
    public GridVector getEdge(CardinalDirection direction){
        return VectorUtility.addMagnitude(center, direction, Simulation.STREET_WIDTH);
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
    
    public boolean isPastYellowLightVector(Car car){
        GridVector yellowVector = getYellowLightVector(car);
        GridVector carVector = car.getVector();
        switch(carVector.direction){
            case EAST:
                return carVector.ewPoint > yellowVector.ewPoint;
                
            case WEST:
                return carVector.ewPoint < yellowVector.ewPoint;
                
            case NORTH:
                return carVector.nsPoint < yellowVector.nsPoint;
                
            case SOUTH:
                return carVector.nsPoint > yellowVector.nsPoint;
            
            default:
                throw new IllegalArgumentException(car.getVector().direction + " is not handled");
        }
    }
    
    /**
     * @param direction
     * @return the cars waiting, only given when the state is not initial
     */
    public Queue<Car> getAndEmptyCars(LightDirection direction){
        switch(direction){
            case EW_BOUND:
                try{
                    return (Queue<Car>) lightQueueEWBound.clone();
                } finally {
                    lightQueueEWBound.clear();
                }

            case NS_BOUND:
                try{
                    return (Queue<Car>) lightQueueNSBound.clone();
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
//                throw new IllegalArgumentException("The state was already set to " + direction);
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
