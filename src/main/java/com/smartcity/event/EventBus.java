/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.smartcity.event;

import com.smartcity.application.Simulation;
import com.smartcity.utility.DataAggregator;
import java.util.PriorityQueue;
import java.util.Queue;
import javafx.application.Platform;

/**
 *
 * @author Blake
 */
public class EventBus {
    
    private static double simulationTime = 0.0;
    private static final Queue<Event> EVENT_QUEUE = new PriorityQueue(new EventComparator());
    private static boolean isStopped = false;
    
    public static boolean cancelEvent(Event event){
        return EVENT_QUEUE.remove(event);
    }
    
    public static void submitEvent(Event event){
        if(simulationTime > event.eventTime){
            throw new IllegalArgumentException("The event cannot be submitted, it occurs in the past." + event);
        }
        EVENT_QUEUE.add(event);
    }
    
    static double getSimulationTime(){
        return simulationTime;
    }
    
    @SuppressWarnings("SleepWhileInLoop")
    public static void runQueue() throws InterruptedException{
        while(EVENT_QUEUE.isEmpty() == false && isStopped == false){
            Event evt = EVENT_QUEUE.poll();
            
            if(Simulation.REAL_TIME){
                long sleepTime = (long) ((evt.eventTime - simulationTime) * (1/Simulation.SIM_SPEED));
                if(sleepTime > 0){
                    Thread.sleep(sleepTime);
                } else {
                    if(sleepTime < 0){
                       System.out.println("There was a negative sleep time: " + sleepTime);
                    }
                }
            }
            simulationTime = evt.eventTime;
            evt.resolveEvent();
        }
        System.out.println("The queue is empty or has been stopped.");
        DataAggregator.generateCarAverageChart();
        DataAggregator.generateEventTimesAveragesChart();
    }
    
    public static void stopEventBus(){
        isStopped = true;
    }
}
