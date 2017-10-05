/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.smartcity.event;

import java.math.BigInteger;
import java.util.List;
import com.smartcity.model.Car;
import com.smartcity.model.Grid;
import com.smartcity.model.Intersection;

/**
 *
 * @author Blake
 */
public class CarMoveEvent extends Event {

    private final Car car;
    private final Intersection currentIntersection, nextIntersection;
    
    public CarMoveEvent(BigInteger eventTime, Car car, Intersection currentIntersection, Intersection nextIntersection) {
        super(eventTime);
        this.car = car;
        this.currentIntersection = currentIntersection;
        this.nextIntersection = nextIntersection;
    }

    @Override
    public void resolveEvent() {
        
        List<Intersection> intersections = car.getRoute().getIntersections();
        
        //last intersection
        if(intersections.get(intersections.size()-1).equals(nextIntersection)){
            EventBus.submitEvent(new CarExitEvent(eventTime, car));
        
        //The car will move to the next intersection
        } else { 
            BigInteger nextEventTime = eventTime.add(BigInteger.valueOf(Grid.INTERSECTION_DISATANCE / car.velocity));
            int intersectionIndex = intersections.indexOf(nextIntersection);
            System.out.println(car + " moved to " + nextIntersection + " at: " + eventTime);
            EventBus.submitEvent(new CarMoveEvent(nextEventTime, car, nextIntersection, intersections.get(intersectionIndex + 1)));
        }
    }
}
