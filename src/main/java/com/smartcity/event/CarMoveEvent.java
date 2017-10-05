/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.smartcity.event;

import java.math.BigInteger;
import java.util.List;
import com.smartcity.model.Car;
import com.smartcity.model.CardinalDirection;
import com.smartcity.model.Grid;
import com.smartcity.model.Intersection;

/**
 *
 * @author Blake
 */
public class CarMoveEvent extends Event {

    private final Car car;
    private final Intersection currentIntersection;
    
    public CarMoveEvent(BigInteger eventTime, Car car, Intersection currentIntersection) {
        super(eventTime);
        this.car = car;
        this.currentIntersection = currentIntersection;
    }

    @Override
    public void resolveEvent() {
        
        List<Intersection> intersections = car.getRoute().getIntersections();
        int currentIndex = intersections.indexOf(currentIntersection);
        
        //last intersection
        if(currentIndex  == (intersections.size()-2)){
            EventBus.submitEvent(new CarExitEvent(eventTime, car));
        
        //The car will move to the next intersection
        } else { 
            Intersection nextIntersection = intersections.get(currentIndex + 1);
            if(currentIntersection.submitCar(car, nextIntersection)){
                BigInteger nextEventTime = eventTime.add(BigInteger.valueOf(Grid.INTERSECTION_DISATANCE / car.velocity));
                System.out.println(car + " moved to " + nextIntersection + " at: " + eventTime);
                EventBus.submitEvent(new CarMoveEvent(nextEventTime, car, nextIntersection));
            } else {
                System.out.println(car + " has been stopped at intersection " + currentIntersection);
                //Do nothing, this car is queued in the intersection.
            }
        }
    }
}
