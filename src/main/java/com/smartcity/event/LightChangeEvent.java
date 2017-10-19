/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.smartcity.event;

import com.smartcity.application.Simulation;
import com.smartcity.model.Car;
import com.smartcity.model.Intersection;
import com.smartcity.model.Intersection.LightDirection;
import java.math.BigInteger;
import java.util.Arrays;

/**
 *
 * @author Blake
 */
public class LightChangeEvent extends Event {
    
    private final static long YELLOW_TIME = 50;
    private final static long GREEN_TIME = 150;
    private final static long CAR_DEQUEUE_TIME = 10;
    
    private final Intersection intersection;
    private final LightDirection lightDirection;
    private final boolean isInitial;

    public LightChangeEvent(double eventTime, Intersection intersection, LightDirection lightDirection, boolean isInitial) {
        super(eventTime);
        this.intersection = intersection;
        this.lightDirection = lightDirection;
        this.isInitial = isInitial;
    }

    @Override
    public void resolveEvent() {
        if(isInitial){
            intersection.setLightState(lightDirection, isInitial);
            double nextEventTime = eventTime + YELLOW_TIME;
            EventBus.submitEvent(new LightChangeEvent(nextEventTime, intersection, lightDirection, false));
        } else {
            Car[] carsDequeued = intersection.setLightState(lightDirection, isInitial);
            if(carsDequeued.length != 0){
                System.out.println(this.toString() + " " + carsDequeued.length + " cars have been dequeued: " + Arrays.toString(carsDequeued));
            }
            
            //Queues up a new light change direction
            LightDirection newDirection;
            double nextEventTime = eventTime + GREEN_TIME;
            if(lightDirection.equals(LightDirection.EW_BOUND)){
                newDirection = LightDirection.NS_BOUND;
            } else { 
                newDirection = LightDirection.EW_BOUND;
            }
            EventBus.submitEvent(new LightChangeEvent(nextEventTime, intersection, newDirection, true));
            
            //Queues up all the cars in waiting at the light
            double carMoveTime = eventTime;
            for(Car car : carsDequeued){
                EventBus.submitEvent(new CarMoveEvent(carMoveTime, car, intersection));
                carMoveTime += CAR_DEQUEUE_TIME;
            }
        }
        Simulation.WORLD.renderIntersectionLights(intersection);
    }
}
