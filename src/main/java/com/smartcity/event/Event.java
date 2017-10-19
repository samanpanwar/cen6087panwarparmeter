/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.smartcity.event;

/**
 *
 * @author Blake
 */
public abstract class Event {
    
    public final double eventTime;
    
    public Event(double eventTime){
        this.eventTime = eventTime;
    }
    
    public abstract void resolveEvent();
    
    @Override
    public String toString(){
        return getClass().getSimpleName() + " time: " + eventTime + " ";
    }
}
