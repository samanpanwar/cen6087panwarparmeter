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
public class CarEntryEvent extends Event{
    
    private final Car car;

    public CarEntryEvent(BigInteger eventTime, Car car) {
        super(eventTime);
        this.car = car;
    }

    @Override
    public void resolveEvent() {
        List<Intersection> intersections = car.getRoute().getIntersections();
        BigInteger moveTime = eventTime.add(BigInteger.valueOf(Grid.INTERSECTION_DISATANCE / car.velocity));
        System.out.println(car + " has entered at: " + eventTime + " location: " + intersections.get(0));
        EventBus.submitEvent(new CarMoveEvent(moveTime, car, intersections.get(0)));
        EventBus.world.addCar(car);
    }
}
