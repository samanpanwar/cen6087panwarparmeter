/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package event;

import GUI.World;
import java.math.BigInteger;
import java.util.TreeSet;

/**
 *
 * @author Blake
 */
public class EventBus {
    
    private static BigInteger simulationTime = BigInteger.ZERO;
    private static final TreeSet<Event> EVENT_QUEUE = new TreeSet(new EventComparator());
    public static World world;
    
    public static void submitEvent(Event event){
        EVENT_QUEUE.add(event);
        //System.out.println(event + " has been submitted");
    }
    
    public static BigInteger getSimulationTime(){
        return simulationTime;
    }
    
    public static void runQueue(){
        while(EVENT_QUEUE.isEmpty() == false){
            Event evt = EVENT_QUEUE.pollFirst();
            simulationTime = evt.eventTime;
            evt.resolveEvent();
        }
        System.out.println("The queue is empty.");
    }
    
    public static void setWorld(World world){
        EventBus.world = world;
    }
}
