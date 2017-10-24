/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.smartcity.event;

import com.smartcity.model.Car;
import com.smartcity.model.Intersection;
import com.smartcity.model.Intersection.LightDirection;
import java.util.Queue;

/**
 *
 * @author Blake
 */
public class IntersectionDequeueEvent extends Event{

    private final Intersection intersection;
    private final LightDirection direction;
    private final Queue<Car> carsToDequeue;
    
    public IntersectionDequeueEvent(double eventTime, Intersection intersection, LightDirection direction, Queue<Car> carsToDequeue) {
        super(eventTime);
        this.intersection = intersection;
        this.direction = direction;
        this.carsToDequeue = carsToDequeue;
    }

    @Override
    public void resolveEvent() {
        
        //Queues up all the cars in waiting at the light
        Car car = carsToDequeue.poll(); 
        if(car != null){
            Intersection intersectionToMoveTo;
            if(intersection.isPastYellowLightVector(car)){
                intersectionToMoveTo = car.getRoute().getNextIntersection(intersection);
            } else {
                intersectionToMoveTo = intersection;
            }    
            EventBus.submitEvent(new ApproachIntersectionEvent(eventTime, car, intersectionToMoveTo));
            EventBus.submitEvent(new IntersectionDequeueEvent(eventTime + Car.DEQUQE_LIGHT_TIME, intersection, direction, carsToDequeue));
        }
    }
}
