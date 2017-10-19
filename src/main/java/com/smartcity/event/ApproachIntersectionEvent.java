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
    public void resolveEvent() {
        
        //Approach the yellow light vector and submit a car to the queue 
        if(to.getNumCarsWaiting(car.getVector().direction) == 0){
            GridVector newVector = to.getYellowLightVector(car);
            double deltaTime = car.getTimeTo(newVector);
            Simulation.WORLD.moveCar(car, newVector, deltaTime);
            car.setVector(newVector);
            EventBus.submitEvent(new DetermineStopEvent(deltaTime + eventTime, car, to));
        } else {
            
            //TODO if the car is past the yellow light then move the car or stop the car based on the light state. 
            
            //Move the car to the next available spot in the intersection
            GridVector newVector = to.StopCar(car);
            double deltaTime = car.getTimeTo(newVector);
            EventBus.submitEvent(new ApproachIntersectionEvent(deltaTime + eventTime, car, to));
//            Simulation.WORLD.moveCar(car, newVector, deltaTime);
        }
    }
}
