/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.smartcity.event;

import java.math.BigInteger;

/**
 *
 * @author Blake
 */
public abstract class Event {
    
    public final BigInteger eventTime;
    
    public Event(BigInteger eventTime){
        this.eventTime = eventTime;
    }
    
    public abstract void resolveEvent();
}
