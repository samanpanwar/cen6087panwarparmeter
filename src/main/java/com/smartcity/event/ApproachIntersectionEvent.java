/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.smartcity.event;

import com.smartcity.application.Simulation;
import com.smartcity.model.Car;
import com.smartcity.model.GridVector;
import com.smartcity.model.Intersection;

/**
 *
 * @author Blake
 */
public class ApproachIntersectionEvent extends Event{
    
    private final Car car;
    private final Intersection to;

    public ApproachIntersectionEvent(double eventTime, Car car, Intersection to) {
        super(eventTime);
        this.car = car;
        this.to = to;
    }

    @Override
    protected void eventAction() {
        
        //If this is the entry event add the car to the GUI
        if(car.getEntryTime() == eventTime){
            Simulation.WORLD.addCar(car);
        }
        
        //Approach the yellow light vector and submit a car to the queue 
        if(to.getNumCarsWaiting(car.getVector().direction) == 0){
            GridVector newVector = to.getYellowLightVector(car);
            double deltaTime = car.getTimeTo(newVector);
            Simulation.WORLD.moveCar(car, newVector, deltaTime);
            car.setVector(newVector);
            EventBus.submitEvent(new DetermineStopEvent(deltaTime + eventTime, car, to));
        } else {
            
            //Move the car to the next available spot in the intersection
            GridVector stopVector = to.getNextCarStopVector(car);
            double deltaTime = car.getTimeTo(stopVector);
            Simulation.WORLD.moveCar(car, stopVector, deltaTime);
            car.setVector(stopVector);
            EventBus.submitEvent(new IntersectionStopEvent(deltaTime + eventTime, car));
        }
    }
}
