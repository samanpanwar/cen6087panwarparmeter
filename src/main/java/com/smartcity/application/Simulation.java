package com.smartcity.application;


import com.smartcity.gui.World;
import com.smartcity.event.CarEntryEvent;
import com.smartcity.event.EventBus;
import com.smartcity.event.LightChangeEvent;
import com.smartcity.factory.RouteFactory;
import java.math.BigInteger;
import javafx.scene.Node;
import com.smartcity.model.Car;
import com.smartcity.model.Grid;
import com.smartcity.model.Intersection;
import com.smartcity.model.Intersection.LightDirection;

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
    private static final long simulationTime = 1000; //time units
    private static final int numCars = 500;
    
    //for testing
    private static final int numLightChanges = 10;
    private int lightChangeNum = 0;
    
    public final Grid grid = new Grid(12, 8);
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
            
            LightDirection direction = LightDirection.NS_BOUND;
            while(lightChangeNum < numLightChanges){
                lightChangeNum ++;

                if(direction.equals(LightDirection.NS_BOUND)){
                    direction = LightDirection.EW_BOUND;
                } else {
                    direction = LightDirection.NS_BOUND;
                }
                
                BigInteger changeTime = EventBus.getSimulationTime();
                for(int i = 0; i < grid.getEWBlockSize(); i++){
                    for(int j = 0; j < grid.getNSBlockSize(); j++){
                        Intersection intersection = grid.getIntersection(i, j);
                        EventBus.submitEvent(new LightChangeEvent(changeTime, intersection, direction, true));
                        changeTime = changeTime.add(BigInteger.ONE);
                    }
                }
                EventBus.runQueue();
            }
            
        }).start();
    }
}
