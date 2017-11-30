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
import com.smartcity.model.Convoy;
import com.smartcity.model.Intersection;
import com.smartcity.model.Route;
import com.smartcity.utility.DataAggregator;
import java.util.ArrayList;

/**
 *
 * @author Blake
 */
public class CarGenerateEvent extends Event{
    
    private static final RouteFactory ROUTE_FACTORY = new RouteFactory(Simulation.GRID);
    private static long numZeroEvents = 0;
    private static final long NUM_ZERO_EVENT_FOR_QUIT = 100;

    public CarGenerateEvent(double eventTime) {
        super(eventTime);
    }

    @Override
    protected void eventAction() {
        
        int numCarsToGenerate = getNumCars();
        double insertTime = eventTime;
        Route route = ROUTE_FACTORY.generateRoute();
//        Simulation.WORLD.drawRoute(route);
        Convoy convoy = new Convoy(route);
        List<Car> cars = new ArrayList();
        for(int i=0; i<numCarsToGenerate; i++){
            insertTime += Simulation.CONVOY_ENTRY_INTERVAL;
            Car car = new Car(insertTime, DataAggregator.getNumCarsAdded(), route, convoy);
            cars.add(car);
            List<Intersection> intersections = car.getRoute().getIntersections();
            if(Simulation.LIGHT_CHANGE_TYPE != Simulation.LightChangeType.CONVOY_AWARE){
                EventBus.submitEvent(new ApproachIntersectionEvent(insertTime, car, intersections.get(0)));
            }
            DataAggregator.addCar();
            if(DataAggregator.getNumCarsAdded() >= Simulation.NUM_CARS){
                System.out.println("Car generation completed.");
                break;
            }
        }
        convoy.setCars(cars);
        if(Simulation.LIGHT_CHANGE_TYPE == Simulation.LightChangeType.CONVOY_AWARE){
            EventBus.submitEvent(new ConvoyEnterEvent(eventTime, convoy));
        }
        
        if(DataAggregator.getNumCarsAdded() >= Simulation.NUM_CARS){
            return;
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
        double carGenerateTime = numCarsToGenerate * Simulation.CONVOY_ENTRY_INTERVAL;
        EventBus.submitEvent(new CarGenerateEvent(insertTime + carGenerateTime + getNextGenerateTime(Simulation.LAMBDA)));
    }
    
    private int getNumCars(){
        return Simulation.RNG.nextInt(Simulation.CONVOY_AVERAGE_SIZE);
    }

    double getNextGenerateTime(double lambda) {
        double X = Simulation.RNG.nextDouble();
        return Math.log(1-X)/-(1/lambda);
    }
}
