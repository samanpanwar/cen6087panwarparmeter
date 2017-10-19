/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.smartcity.event;

import com.smartcity.model.Intersection;
import java.math.BigInteger;

/**
 *
 * @author Blake
 */
public class IntersectionExitEvent extends Event{

    public IntersectionExitEvent(double eventTime, Intersection from, Intersection to) {
        super(eventTime);
    }

    @Override
    public void resolveEvent() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }
    
}
