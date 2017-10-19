/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.smartcity.event;

import com.smartcity.model.Car;
import com.smartcity.model.GridVector;

/**
 *
 * @author Blake
 */
public class IntersectionStopEvent extends Event{

    private final Car car;
    private final GridVector stopLocation;
    
    public IntersectionStopEvent(double eventTime, Car car, GridVector stopLocation) {
        super(eventTime);
        this.car = car;
        this.stopLocation = stopLocation;
    }

    @Override
    public void resolveEvent() {
        this.car.setVector(stopLocation);
    
    }
    
}
