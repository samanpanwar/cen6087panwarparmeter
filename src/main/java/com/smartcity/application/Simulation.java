package com.smartcity.application;

import com.smartcity.application.enumeration.LightDirection;
import com.smartcity.gui.World;
import com.smartcity.event.CarGenerateEvent;
import com.smartcity.event.EventBus;
import com.smartcity.event.LightChangeEvent;
import com.smartcity.model.Grid;
import com.smartcity.model.Intersection;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Properties;
import java.util.Random;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Blake
 */
public class Simulation {
    
    public enum LightChangeType{DUMB, CAR_BASED, COORDINATED, CONVOY_AWARE}
    
    private static final Properties prop = getProperties();
    
    //Used for rendering / data gathering
    public static final boolean REAL_TIME = Boolean.valueOf(prop.getProperty("real_time"));
    public static final boolean SHOW_GUI = Boolean.valueOf(prop.getProperty("show_gui"));
    public static final double SIM_SPEED = Double.valueOf(prop.getProperty("sim_speed"));   
    public static final boolean GATHER_DATA = Boolean.valueOf(prop.getProperty("gather_data"));
    public static final double CHART_BUCKETS = Double.valueOf(prop.getProperty("chart_buckets"));
    
    //configuration variables
    public static final double LAMBDA =  Double.valueOf(prop.getProperty("lambda"));
    public static final double CONVOY_ENTRY_INTERVAL =  Double.valueOf(prop.getProperty("convoy_entry_interval"));
    public static final int CONVOY_AVERAGE_SIZE =  Integer.valueOf(prop.getProperty("convoy_average_size"));
    public static final LightChangeType LIGHT_CHANGE_TYPE = LightChangeType.valueOf(prop.getProperty("light_change_type"));
    public static final int NUM_CARS = Integer.valueOf(prop.getProperty("num_cars"));
    public static final int NUM_EW_STREETS = Integer.valueOf(prop.getProperty("num_ew_streets"));
    public static final int NUM_NS_STREETS = Integer.valueOf(prop.getProperty("num_ns_streets"));
    public static final int CAR_CHANGE_LIGHT_NUM = Integer.valueOf(prop.getProperty("car_change_light_num"));
    public static final long LIGHT_CHANGE_TIME = Long.valueOf(prop.getProperty("light_change_time"));
    public static final long LIGHT_CHANGE_GREEN_TIME = Long.valueOf(prop.getProperty("light_change_green_time"));
    public static final long DEQUQE_LIGHT_TIME = Long.valueOf(prop.getProperty("dequqe_light_time")); //Time after the light changes or a car in front moves that this car moves 
    public static final double CAR_VELOCITY = Double.valueOf(prop.getProperty("car_velocity"));//distance units / time unit   
    public static final Random RNG = new Random(Long.valueOf(prop.getProperty("seed")));
    
    //Size variables
    public static final int INTERSECTION_DISTANCE = Integer.valueOf(prop.getProperty("intersection_distance"));
    public static final int STREET_WIDTH = Integer.valueOf(prop.getProperty("street_width")); 
    public static final int CAR_STOP_DISTANCE = Integer.valueOf(prop.getProperty("car_stop_distance"));//Distace between stopped cars
    
    //Objects
    public static final Grid GRID = new Grid(NUM_EW_STREETS, NUM_NS_STREETS);
    public static final World WORLD = new World(GRID);
    public static final long START_TIME = System.currentTimeMillis();
    
    private static Properties getProperties(){
        Properties properties = new Properties();
        try {
            properties.load(new FileInputStream("simulation.properties"));
        } catch (FileNotFoundException ex) {
            Logger.getLogger(Simulation.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IOException ex) {
            Logger.getLogger(Simulation.class.getName()).log(Level.SEVERE, null, ex);
        }
        return properties;
    }
    
    public static void start(){
        
        //Starts up the "dumb" light switch algorithm for the traffic lights
        System.out.println("Simulation Started. Sim Type: " + Simulation.LIGHT_CHANGE_TYPE + " Lambda:" + Simulation.LAMBDA);
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
