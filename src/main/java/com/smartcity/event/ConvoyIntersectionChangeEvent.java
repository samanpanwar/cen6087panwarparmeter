/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.smartcity.event;

import com.smartcity.application.enumeration.LightDirection;
import com.smartcity.model.Convoy;
import com.smartcity.model.Intersection;

/**
 *
 * @author Blake
 */
public class ConvoyIntersectionChangeEvent extends Event{

    private final Intersection intersection;
    private final LightDirection direction;
    private final Convoy convoy;
    
    public ConvoyIntersectionChangeEvent(double eventTime, Intersection intersection, LightDirection direction, Convoy convoy) {
        super(eventTime);
        this.intersection = intersection;
        this.direction = direction;
        this.convoy = convoy;
    }

    @Override
    protected void eventAction() {
        intersection.addConvoy(convoy);
        EventBus.submitEvent(new LightChangeEvent(eventTime, intersection, direction, LightChangeEvent.ChangeType.INITIAL));
    }
}
