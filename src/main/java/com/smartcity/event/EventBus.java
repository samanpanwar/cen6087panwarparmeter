/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.smartcity.event;

import com.smartcity.gui.World;
import java.math.BigInteger;
import java.util.PriorityQueue;
import java.util.Queue;
import javafx.application.Platform;

/**
 *
 * @author Blake
 */
public class EventBus {
    
    private static BigInteger simulationTime = BigInteger.ZERO;
    private static final Queue<Event> EVENT_QUEUE = new PriorityQueue(new EventComparator());
    
    static World world;
    
    public static void submitEvent(Event event){
        EVENT_QUEUE.add(event);
        //System.out.println(event + " has been submitted");
    }
    
    static BigInteger getSimulationTime(){
        return simulationTime;
    }
    
    public static void runQueue(){
        while(EVENT_QUEUE.isEmpty() == false){
            Event evt = EVENT_QUEUE.poll();
            simulationTime = evt.eventTime;
            evt.resolveEvent();
        }
        System.out.println("The queue is empty.");
        Platform.exit();
    }
    
    public static void setWorld(World world){
        EventBus.world = world;
    }
}
