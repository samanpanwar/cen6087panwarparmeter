package com.smartcity.application;


import com.smartcity.gui.World;
import com.smartcity.event.CarEntryEvent;
import com.smartcity.event.EventBus;
import com.smartcity.factory.RouteFactory;
import java.math.BigInteger;
import javafx.scene.Node;
import com.smartcity.model.Car;
import com.smartcity.model.Grid;

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
    
    public final Grid grid = new Grid(10, 5);
    public final World world = new World(grid);
    private final RouteFactory routeFactory = new RouteFactory(grid, 0L);
    
    public Simulation(){
        EventBus.setWorld(world);
    }
    
    public Node getWorldNode(){
        return world.getRoot();
    }
    
    public void start(){
        new Thread(()->{
            for(int i = 0; i<numCars; i++){
                BigInteger entryTime = BigInteger.valueOf(i * (simulationTime/numCars));
                Car car = new Car(entryTime, routeFactory.generateRoute());
                EventBus.submitEvent(new CarEntryEvent(entryTime, car));
            }
            EventBus.runQueue();
        }).start();
    }
}
