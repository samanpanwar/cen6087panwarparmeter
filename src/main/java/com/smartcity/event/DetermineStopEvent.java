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
import com.smartcity.model.Intersection.LightState;

/**
 *
 * @author Blake
 */
public class DetermineStopEvent extends Event{
    
    private final Car car;
    private final Intersection to;

    public DetermineStopEvent(double eventTime, Car car, Intersection to) {
        super(eventTime);
        this.car = car;
        this.to = to;
    }

    @Override
    public void resolveEvent() {
        
        LightState lightState;
        switch(car.getVector().direction){
            case NORTH: case SOUTH:
                lightState = to.getNSLightState();
                break;
                
            case EAST: case WEST:
                lightState = to.getEWLightState();
                break;
                
            default:
                throw new IllegalArgumentException(car.getVector().direction + " is not handled");
        }
                
        //Move the car though the intersection then submit a new approach intersection event
        if(lightState.equals(Intersection.LightState.GREEN)){
            Intersection nextIntersection = car.getRoute().getNextIntersection(to);
            if(nextIntersection != null){
                //TODO: render the move
//                EventBus.submitEvent(new ApproachIntersectionEvent(get the time, car, nextIntersection));
            } else {
                //TODO: render the move
//                EventBus.submitEvent(new CarExitEvent(get the time, car));
            }

        } else {
            GridVector stopLocation = to.StopCar(car);
            double deltaTime = car.getTimeTo(stopLocation);
            EventBus.submitEvent(new IntersectionStopEvent(deltaTime + eventTime, car, stopLocation));
            Simulation.WORLD.moveCar(car, stopLocation, deltaTime);
        }
    }
}
