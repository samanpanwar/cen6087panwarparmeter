package com.smartcity.application;


import com.smartcity.gui.World;
import com.smartcity.event.CarGenerateEvent;
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
    public static final long CAR_ENTRY_INTERVAL = 12; //time units
    public static final int NUM_CARS = 5;
    public static final long NUM_TICKS = 1_000;
    public static final boolean REAL_TIME = true;
    public static final double SIM_SPEED = 0.01;
    public static final int NUM_EW_STREETS = 12;
    public static final int NUM_NS_STREETS = 8;
    
    //Objects
    public static final Grid GRID = new Grid(NUM_EW_STREETS, NUM_NS_STREETS);
    public static final World WORLD = new World(GRID);
    
    public static void start(){
        
        //Starts up the "dumb" light switch algorithm for the traffic lights
        for(int i = 0; i < GRID.getEWBlockSize(); i++){
            for(int j = 0; j < GRID.getNSBlockSize(); j++){
                Intersection intersection = GRID.getIntersection(i, j);
                EventBus.submitEvent(new LightChangeEvent(0.0, intersection, LightDirection.EW_BOUND, true));
            }
        }
        
        //Initializes the car generate event
        EventBus.submitEvent(new CarGenerateEvent(0.0));
        
        new Thread(()->{
            try{
                EventBus.runQueue();
            }catch(InterruptedException ex){
                ex.printStackTrace(System.err);
            }
        },"Simulation Thread").start();
    }
}
