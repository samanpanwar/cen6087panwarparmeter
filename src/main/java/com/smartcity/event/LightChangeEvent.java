/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.smartcity.event;

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
    private final static long CAR_DEQUEUE_TIME = 10;
    
    private final Intersection intersection;
    private final LightDirection lightDirection;
    private final boolean isInitial;

    public LightChangeEvent(BigInteger eventTime, Intersection intersection, LightDirection lightDirection, boolean isInitial) {
        super(eventTime);
        this.intersection = intersection;
        this.lightDirection = lightDirection;
        this.isInitial = isInitial;
    }

    @Override
    public void resolveEvent() {
        if(isInitial){
            intersection.setLightState(lightDirection, isInitial);
            BigInteger nextEventTime = eventTime.add(BigInteger.valueOf(YELLOW_TIME));
            EventBus.submitEvent(new LightChangeEvent(nextEventTime, intersection, lightDirection, false));
        } else {
            Car[] carsDequeued = intersection.setLightState(lightDirection, false);
            if(carsDequeued.length != 0){
                System.out.println(carsDequeued.length + " cars have been dequeued: " + Arrays.toString(carsDequeued));
            }
            
            BigInteger carMoveTime = eventTime;
            for(Car car : carsDequeued){
                EventBus.submitEvent(new CarMoveEvent(carMoveTime, car, intersection));
                carMoveTime = carMoveTime.add(BigInteger.valueOf(CAR_DEQUEUE_TIME));
            }
        }
    }
}
