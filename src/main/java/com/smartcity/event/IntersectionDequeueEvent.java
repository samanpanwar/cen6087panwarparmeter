/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.smartcity.event;

import com.smartcity.model.Car;
import com.smartcity.model.Intersection;
import com.smartcity.application.enumeration.LightDirection;

/**
 *
 * @author Blake
 */
public class IntersectionDequeueEvent extends Event{

    private final Intersection intersection;
    private final LightDirection direction;
    private final int index;
    
    public IntersectionDequeueEvent(double eventTime, Intersection intersection, LightDirection direction, int index) {
        super(eventTime);
        this.intersection = intersection;
        this.direction = direction;
        this.index = index;
    }

    @Override
    protected void eventAction() {
        
        if(intersection.getNumCarsWaiting(direction) > index){
            Car car = intersection.dequeueCar(direction, index);
            if(car != null){
                if(intersection.isPastYellowLightVector(car)){
                    EventBus.submitEvent(new DetermineStopEvent(eventTime + Car.DEQUQE_LIGHT_TIME, car, intersection));
                } else {
                    EventBus.submitEvent(new ApproachIntersectionEvent(eventTime + Car.DEQUQE_LIGHT_TIME, car, intersection));
                }    
                EventBus.submitEvent(new IntersectionDequeueEvent(eventTime + Car.DEQUQE_LIGHT_TIME, intersection, direction, index+1));
            }
        }
    }
}
