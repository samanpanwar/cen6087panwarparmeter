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
import com.smartcity.model.Intersection;
import com.smartcity.model.Route;
import com.smartcity.utility.DataAggregator;

/**
 *
 * @author Blake
 */
public class CarGenerateEvent extends Event{
    
    private static final RouteFactory ROUTE_FACTORY = new RouteFactory(Simulation.GRID);
    private static long eventNum = 0;
    private static long numZeroEvents = 0;
    private static final long NUM_ZERO_EVENT_FOR_QUIT = 100;

    public CarGenerateEvent(double eventTime) {
        super(eventTime);
    }

    @Override
    protected void eventAction() {
        
        long numCarsToGenerate = getNumCars(eventNum);
        double insertTime = eventTime;
        Route route = ROUTE_FACTORY.generateRoute();
//        Simulation.WORLD.drawRoute(route);
        for(int i=0; i<numCarsToGenerate; i++){
            insertTime += Simulation.CAR_ENTRY_INTERVAL;
            Car car = new Car(insertTime, DataAggregator.getNumCarsAdded(), route);
            List<Intersection> intersections = car.getRoute().getIntersections();
            EventBus.submitEvent(new ApproachIntersectionEvent(insertTime, car, intersections.get(0)));
            DataAggregator.addCar();
            if(DataAggregator.getNumCarsAdded() >= Simulation.NUM_CARS){
                System.out.println("Car generation completed.");
                return;
            }
        }
        
        if(numCarsToGenerate == 0){
            numZeroEvents++;
            if(numZeroEvents > NUM_ZERO_EVENT_FOR_QUIT){
                System.out.println("Not enough cars could be generated given the parameters, car generation will now stop");
                return;
            }
        } else {
            numZeroEvents = 0;
        }
        
        //generates the next car if we have not broken out
        EventBus.submitEvent(new CarGenerateEvent(insertTime + Simulation.CAR_ENTRY_INTERVAL));
        eventNum++;
    }
    
    private long getNumCars(long EventNum){
        double t = (double) EventNum;
        double X = Simulation.RNG.nextDouble();
        double l = Simulation.NUM_CARS_LAMBDA;
        double mult = Simulation.CAR_ENTRY_MULTIPLIER;
        return Math.round(X * mult * l * Math.exp(-l * t));
    }
}
