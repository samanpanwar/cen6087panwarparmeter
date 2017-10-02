package com.blakeparmeter.cen6087application.application;


import event.CarEntryEvent;
import event.EventBus;
import factory.RouteFactory;
import java.math.BigInteger;
import model.Car;
import model.Grid;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

/**
 *
 * @author Blake
 */
public class Simulation {
    
    public static final Grid grid = new Grid(8, 8);
    private static final RouteFactory routeFactory = new RouteFactory(grid, 0L);
    
    //configuration variables
    private static final long simulationTime = 100_000; //time units
    private static final int numCars = 10_000;
    
    public static final void startSimulation(){
        for(int i = 0; i<numCars; i++){
            BigInteger entryTime = BigInteger.valueOf(i * (simulationTime/numCars));
            Car car = new Car(entryTime, routeFactory.generateRoute());
            EventBus.submitEvent(new CarEntryEvent(entryTime, car));
        }
        EventBus.runQueue();
    }
}
