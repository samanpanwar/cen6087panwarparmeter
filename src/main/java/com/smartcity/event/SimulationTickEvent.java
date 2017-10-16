/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.smartcity.event;

import com.smartcity.factory.RouteFactory;
import com.smartcity.model.Car;
import com.smartcity.model.Grid;
import com.smartcity.model.Intersection;
import com.smartcity.model.Intersection.LightDirection;
import java.math.BigInteger;
import static com.smartcity.application.Simulation.CAR_ENTRY_INTERVAL;
import static com.smartcity.application.Simulation.NUM_CARS;
import static com.smartcity.application.Simulation.NUM_TICKS;

/**
 *
 * @author Blake
 */
public class SimulationTickEvent extends Event {
    
    private static final long TICK_INTERVAL = 100;
    private static final long CARS_PER_TICK = 10;
    
    private static long carsGenerated = 0;
    private static long tickNum = 0;
    private static RouteFactory routeFactory;
    private static Grid grid;
    private static LightDirection direction = LightDirection.NS_BOUND;
    
    private final long thisTickNum;

    public SimulationTickEvent(BigInteger eventTime) {
        super(eventTime);
        thisTickNum = tickNum++;
    }

    @Override
    public void resolveEvent() {
        
        int carsGeneratedThisTick = 0;
        
        for(int i = 0; i<CARS_PER_TICK && carsGenerated < NUM_CARS; i++){
            BigInteger entryTime = eventTime.add(BigInteger.valueOf(i * (CAR_ENTRY_INTERVAL/NUM_CARS)));
            Car car = new Car(entryTime, routeFactory.generateRoute());
            EventBus.submitEvent(new CarEntryEvent(entryTime, car));
            carsGeneratedThisTick++;
        }
        carsGenerated += carsGeneratedThisTick;

        //Changes the traffic lights on each tick
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
        
        if(carsGeneratedThisTick > 0){
            System.out.println(this.toString() + " " + carsGeneratedThisTick + " cars generated.");
        }
        
        if(tickNum < NUM_TICKS){
            EventBus.submitEvent(new SimulationTickEvent(eventTime.add(BigInteger.valueOf(TICK_INTERVAL))));
        } else {
            System.out.println("End of simulation ticks");
        }
    }
    
    public static void init(RouteFactory routeFactory, Grid grid){
        SimulationTickEvent.routeFactory = routeFactory;
        SimulationTickEvent.grid = grid;
    }
}
