/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.smartcity.model;

import com.smartcity.application.Simulation;
import com.smartcity.utility.VectorUtility;

/**
 *
 * @author Blake
 */
public class Car {
    
    public enum State{STOPPED, ACCELERATING, CRUISING, STOPPING};
    
    public final double stoppingDistance = 25;
    public final double accelerationDistance = 25;
    
    private final double entryTime;
    private final Route route;
    private final long carNum;
    private final Convoy convoy;
    private GridVector vector;
    private State state;
    private double waitTime = 0;
    private Double stopTime;
    
    public Car(double entryTime, long carNum, Route route, Convoy convoy){
        this.entryTime = entryTime;
        this.route = route;
        this.carNum = carNum;
        this.vector = VectorUtility.initializePositionDirection(route);
        this.convoy = convoy;
    }
    
    public double getTotalWaitTime(){
        return waitTime;
    }
    
    public State getState(){
        return state;
    }
    
    public Convoy getConvoy(){
        return convoy;
    }
    
    public void setState(State state){
        this.state = state;
    }
    
    public double getTimeTo(GridVector vector){
        return this.vector.distanceTo(vector) / Simulation.CAR_VELOCITY;
    }
    
    public double getEntryTime(){
        return entryTime;
    }
    
    public Route getRoute(){
        return route;
    }
    
    public GridVector getVector(){
        return vector;
    }
    
    public void setVector(GridVector vector){
        this.vector = vector;
    }
    
    public void setStopTime(double stopTime){
        this.stopTime = stopTime;
    }
    
    public void incrementWaitTime(double currentTime){
        if(stopTime != null){
            waitTime += currentTime - stopTime;
            stopTime = null;
        }
    }

    @Override
    public String toString() {
        return "Car: " + carNum;
    }
    
    @Override
    public int hashCode() {
        return (int) carNum;
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
