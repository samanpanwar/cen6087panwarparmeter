/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.smartcity.event;

import java.math.BigInteger;
import com.smartcity.model.Car;
import com.smartcity.model.Intersection;

/**
 *
 * @author Blake
 */
public class CarExitEvent extends Event{

    private final Car car;
    
    public CarExitEvent(BigInteger eventTime, Car car) {
        super(eventTime);
        this.car = car;
    }

    @Override
    public void resolveEvent() {
        double duration = eventTime.subtract(car.getEntryTime()).longValue();
        double distance = (car.getRoute().getIntersections().size() - 1) * 100;
        Intersection intersection = car.getRoute().getIntersections().get(car.getRoute().getIntersections().size()-1);
        System.out.println(this.toString() + car + " has exited at " +intersection + " the car was in the simulation for " + duration + " time units and covered " + distance + " distance units the velocity was: " + (distance / duration));
    }
}
