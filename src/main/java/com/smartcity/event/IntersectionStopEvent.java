/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.smartcity.event;

import com.smartcity.model.Car;

/**
 *
 * @author Blake
 */
public class IntersectionStopEvent extends Event{

    private final Car car;
    
    public IntersectionStopEvent(double eventTime, Car car) {
        super(eventTime);
        this.car = car;
    }

    @Override
    public void resolveEvent() {
        if(car.getState() == Car.State.STOPPING){
            car.setState(Car.State.STOPPED);
        }
    }
}
