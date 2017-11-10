package com.smartcity.application;


import com.smartcity.gui.World;
import com.smartcity.event.CarGenerateEvent;
import com.smartcity.event.EventBus;
import com.smartcity.event.LightChangeEvent;
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
    
    public enum LightChangeType{DUMB, CAR_BASED}
    
    //Used for rendering / data gathering
    public static final boolean GATHER_DATA = true; //Displays data at the end of the simulation
    public static final boolean REAL_TIME = false;  //Sleeps the time between events
    public static final boolean SHOW_GUI = false;   //Renders a GUI 
    public static final double SIM_SPEED = 0.03;    //The simulation speed
    public static final double CHART_BUCKETS = 20;
    
    //configuration variables
    public static final LightChangeType LIGHT_CHANGE_TYPE = LightChangeType.DUMB;
    public static final int NUM_CARS = 5000;
    public static final int NUM_EW_STREETS = 10;
    public static final int NUM_NS_STREETS = 8;
    public static final int CAR_CHANGE_LIGHT_NUM = 5;
    public static final long CAR_ENTRY_INTERVAL = 10; //time units
    public static final long LIGHT_CHANGE_TIME = 50;
    public static final long LIGHT_CHANGE_GREEN_TIME = 250;
    public static final long DEQUQE_LIGHT_TIME = 10; //Time after the light changes or a car in front moves that this car moves 
    public static final double CAR_VELOCITY = 5;//distance units / time unit
    
    //Size variables
    public static final int INTERSECTION_DISATANCE = 150;
    public static final int STREET_WIDTH = 10; 
    public static final int CAR_STOP_DISTANCE = 8;//Distace between stopped cars
    
    //Objects
    public static final Grid GRID = new Grid(NUM_EW_STREETS, NUM_NS_STREETS);
    public static final World WORLD = new World(GRID);
    public static final long START_TIME = System.currentTimeMillis();
    
    public static void start(){
        
        //Starts up the "dumb" light switch algorithm for the traffic lights
        System.out.println("Simulation Started.");
        if(LIGHT_CHANGE_TYPE == LightChangeType.DUMB){
            for(int i = 0; i < GRID.getEWBlockSize(); i++){
                for(int j = 0; j < GRID.getNSBlockSize(); j++){
                    Intersection intersection = GRID.getIntersection(i, j);
                    EventBus.submitEvent(new LightChangeEvent(0.0, intersection, LightDirection.NS_BOUND, LightChangeEvent.ChangeType.GREEN));
                }
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
