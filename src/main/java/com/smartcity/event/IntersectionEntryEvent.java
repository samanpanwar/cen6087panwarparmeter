/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.smartcity.event;

import com.smartcity.model.Car;
import com.smartcity.model.Intersection;
import java.math.BigInteger;

/**
 *
 * @author Blake
 */
public class IntersectionEntryEvent extends Event {

    private final Intersection interseciton;
    private final Car car;
    
    public IntersectionEntryEvent(BigInteger eventTime, Intersection intersection, Car car) {
        super(eventTime);
        this.interseciton = intersection;
        this.car = car;
    }

    @Override
    public void resolveEvent() {
        interseciton.submitCar(car, interseciton);
    }
}
