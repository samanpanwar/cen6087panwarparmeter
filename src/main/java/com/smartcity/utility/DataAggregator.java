/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.smartcity.utility;

import com.smartcity.event.Event;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.atomic.AtomicLong;

/**
 *
 * @author Blake
 */
public class DataAggregator {
    
    private static final AtomicLong numCars = new AtomicLong(0);
    private static final AtomicLong numEvents = new AtomicLong(0);
    private static final Map<Class, Long> eventCount = new HashMap(); 
    
    public static void putEventEntry(Event event, long nanos){
        
    }
    
}
