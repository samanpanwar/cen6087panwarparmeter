/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.smartcity.event;

import com.smartcity.application.Simulation;
import com.smartcity.factory.RouteFactory;
import java.util.List;
import com.smartcity.model.Car;
import com.smartcity.model.Grid;
import com.smartcity.model.Intersection;

/**
 *
 * @author Blake
 */
public class CarGenerateEvent extends Event{
    
    private static final RouteFactory ROUTE_FACTORY = new RouteFactory(Simulation.GRID, 0L);
    private static long carsGenerated = 0;

    public CarGenerateEvent(double eventTime) {
        super(eventTime);
    }

    @Override
    public void resolveEvent() {
        
        Car car = new Car(eventTime, carsGenerated, ROUTE_FACTORY.generateRoute());
        List<Intersection> intersections = car.getRoute().getIntersections();
        double moveTime = eventTime + (Grid.INTERSECTION_DISATANCE / car.velocity);
        System.out.println(this.toString() + car + " location: " + intersections.get(0));
        EventBus.submitEvent(new ApproachIntersectionEvent(moveTime, car, intersections.get(0)));
        Simulation.WORLD.addCar(car);
//        Simulation.WORLD.drawRoute(car.getRoute());
        
        //generates the next car if there is a time to
        carsGenerated ++;
        if(carsGenerated < Simulation.NUM_CARS){
            EventBus.submitEvent(new CarGenerateEvent(eventTime + Simulation.CAR_ENTRY_INTERVAL));
        } else {
            System.out.println("Car generation completed.");
        }
    }
}
