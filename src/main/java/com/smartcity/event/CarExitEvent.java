/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.smartcity.event;

import com.smartcity.application.Simulation;
import com.smartcity.model.Car;
import com.smartcity.model.Intersection;
import com.smartcity.utility.DataAggregator;

/**
 *
 * @author Blake
 */
public class CarExitEvent extends Event{

    private final Car car;
    
    public CarExitEvent(double eventTime, Car car) {
        super(eventTime);
        this.car = car;
    }

    @Override
    protected void eventAction() {
        double duration = eventTime - car.getEntryTime();
        double distance = car.getRoute().getIntersections().size() * Simulation.INTERSECTION_DISATANCE;
        double velocity = (distance / duration);
        Intersection intersection = car.getRoute().getIntersections().get(car.getRoute().getIntersections().size()-1);
//        System.out.println(this.toString() + car + " has exited at " +intersection + " the car was in the simulation for " + duration + " time units and covered " + distance + " distance units the velocity was: " + (distance / duration));
        Simulation.WORLD.removeCar(car);
        DataAggregator.removeCar();
        DataAggregator.putCarAverage(velocity);
        
        //end of the simulation
        if(DataAggregator.getNumCarsActive() == 0 && DataAggregator.getNumCarsAdded() == Simulation.NUM_CARS){
            long simulationTime = System.currentTimeMillis() - Simulation.START_TIME;
            double rate = ((double)Simulation.NUM_CARS / (double)simulationTime) * 1000;
            System.out.println("All cars have exited. The simulation took: " + simulationTime + "ms. " + rate + " cars processed / sec");
            EventBus.stopEventBus();
        }
    }
}
