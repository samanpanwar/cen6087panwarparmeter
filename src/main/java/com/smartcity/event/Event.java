/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.smartcity.event;

import com.smartcity.utility.DataAggregator;
import java.util.HashMap;
import java.util.Map;

/**
 *
 * @author Blake
 */
public abstract class Event {
    
    private static Map<Class, Object> data = new HashMap();
    public final double eventTime;
    
    public Event(double eventTime){
        this.eventTime = eventTime;
    }
    
    public void resolveEvent(){
        long startNanos = System.nanoTime();
        eventAction();
        DataAggregator.putEventEntry(this, System.nanoTime() - startNanos);
    }
    
    protected abstract void eventAction();
    
    @Override
    public String toString(){
        return getClass().getSimpleName() + " time: " + eventTime + " ";
    }
    
    
}
