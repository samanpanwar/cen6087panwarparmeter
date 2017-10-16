package com.smartcity.application;


import com.smartcity.gui.World;
import com.smartcity.event.CarEntryEvent;
import com.smartcity.event.EventBus;
import com.smartcity.event.LightChangeEvent;
import com.smartcity.event.SimulationTickEvent;
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
    public static final long CAR_ENTRY_INTERVAL = 50; //time units
    public static final int NUM_CARS = 5;
    public static final long NUM_TICKS = 1_000;
    
    public final Grid grid = new Grid(12, 8);
    public final World world = new World(grid);
    private final RouteFactory routeFactory = new RouteFactory(grid, 0L);
    
    public Simulation(){
        EventBus.setWorld(world);
        SimulationTickEvent.init(routeFactory, grid);
    }
    
    public Node getWorldNode(){
        return world.getRoot();
    }
    
    public void start(){
        EventBus.submitEvent(new SimulationTickEvent(BigInteger.ZERO));
        new Thread(()->{
            EventBus.runQueue();
        },"Simulation Thread").start();
    }
}
