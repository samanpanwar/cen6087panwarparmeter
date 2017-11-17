package com.smartcity.event;

import com.smartcity.application.Simulation;
import com.smartcity.application.enumeration.CardinalDirection;
import com.smartcity.application.enumeration.LightDirection;
import com.smartcity.model.Car;
import com.smartcity.model.Convoy;
import com.smartcity.model.ConvoyQueue;
import com.smartcity.model.Intersection;
import com.smartcity.utility.VectorUtility;

/**
 *
 * @author Blake
 */
public class ConvoyEnterEvent extends Event{
    
    private final Convoy convoy;

    public ConvoyEnterEvent(double eventTime, Convoy convoy) {
        super(eventTime);
        this.convoy = convoy;
    }

    @Override
    protected void eventAction() {
        
        //If the route is clear then submit the convoy, else wait for 
        LightDirection direction = LightDirection.get(VectorUtility.initializePositionDirection(convoy.route).direction);
        Intersection lastIntersection = null;
        for(Intersection intersection : convoy.route.getIntersections()){
            if(lastIntersection != null){
                direction = LightDirection.get(VectorUtility.getDirectionTo(lastIntersection, intersection));
            }
            if(intersection.isConvoyLocked() && intersection.getLightDirection() != direction){
                ConvoyQueue.addToQueue(convoy);
                return;
            }
            lastIntersection = intersection;
        }
        
        double changeTime = eventTime;
        direction = LightDirection.get(VectorUtility.initializePositionDirection(convoy.route).direction);
        lastIntersection = null;
        for(Intersection intersection : convoy.route.getIntersections()){
            if(lastIntersection != null){
                direction = LightDirection.get(VectorUtility.getDirectionTo(lastIntersection, intersection));
            }
            EventBus.submitEvent(new ConvoyIntersectionChangeEvent(changeTime, intersection, direction, convoy));
//            changeTime += (double) Simulation.INTERSECTION_DISATANCE / Simulation.CAR_VELOCITY;
            lastIntersection = intersection;
        }
        
        double insertTime = eventTime;
        for(Car car : convoy.getCars()){
            insertTime += Simulation.CAR_ENTRY_INTERVAL;
            EventBus.submitEvent(new ApproachIntersectionEvent(insertTime, car, convoy.route.getIntersections().get(0)));
        }
    }
}
