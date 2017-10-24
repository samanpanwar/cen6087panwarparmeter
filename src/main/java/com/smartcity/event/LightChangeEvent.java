/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.smartcity.event;

import com.smartcity.application.Simulation;
import com.smartcity.model.Car;
import com.smartcity.model.Intersection;
import com.smartcity.model.Intersection.LightDirection;

/**
 *
 * @author Blake
 */
public class LightChangeEvent extends Event {
    
    public enum ChangeType{INITIAL, ALL_RED, GREEN};
    
    private final static long YELLOW_TIME = 50;
    private final static long GREEN_TIME = 250;
    
    private final Intersection intersection;
    private final LightDirection lightDirection;
    private final ChangeType changeType;

    public LightChangeEvent(double eventTime, Intersection intersection, LightDirection lightDirection, ChangeType changeType) {
        super(eventTime);
        this.intersection = intersection;
        this.lightDirection = lightDirection;
        this.changeType = changeType;
    }

    @Override
    public void resolveEvent() {
        switch(changeType){
            
            case INITIAL:
                EventBus.submitEvent(new LightChangeEvent(eventTime + YELLOW_TIME, intersection, lightDirection, ChangeType.ALL_RED));
                break;
                
            case ALL_RED:
                EventBus.submitEvent(new LightChangeEvent(eventTime + YELLOW_TIME, intersection, lightDirection, ChangeType.GREEN));
                break;
                
            case GREEN:

                //Queues up a new light change direction
                LightDirection newDirection;
                if(lightDirection.equals(LightDirection.EW_BOUND)){
                    newDirection = LightDirection.NS_BOUND;
                } else { 
                    newDirection = LightDirection.EW_BOUND;
                }
                EventBus.submitEvent(new IntersectionDequeueEvent(eventTime + Car.DEQUQE_LIGHT_TIME, intersection, lightDirection, intersection.getAndEmptyCars(lightDirection)));
                EventBus.submitEvent(new LightChangeEvent(eventTime + GREEN_TIME, intersection, newDirection, ChangeType.INITIAL));
                break;
            
            default:
                throw new IllegalArgumentException(changeType + " is not handled");
        }
        intersection.setLightState(lightDirection, changeType);
        Simulation.WORLD.renderIntersectionLights(intersection);
    }
}
