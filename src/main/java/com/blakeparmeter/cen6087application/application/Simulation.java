package com.blakeparmeter.cen6087application.application;


import GUI.World;
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
    
    
    //configuration variables
    private static final long simulationTime = 100_000; //time units
    private static final int numCars = 5;
    
    public final Grid grid = new Grid(5, 5);
    private final RouteFactory routeFactory = new RouteFactory(grid, 0L);
    private World world = new World(grid);
    
    public Simulation(){
    }
    
    public void start(){
        new Thread(()->{
            for(int i = 0; i<numCars; i++){
                BigInteger entryTime = BigInteger.valueOf(i * (simulationTime/numCars));
                Car car = new Car(entryTime, routeFactory.generateRoute());
                EventBus.submitEvent(new CarEntryEvent(entryTime, car));
            }
            EventBus.runQueue();
        },"simulation thread").start();
    }
    
    public World getWorld(){
        return world;
    }
}
