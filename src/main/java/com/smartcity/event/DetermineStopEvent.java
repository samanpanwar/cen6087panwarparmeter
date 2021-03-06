/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.smartcity.event;

import com.smartcity.application.Simulation;
import com.smartcity.model.Car;
import com.smartcity.model.CardinalDirection;
import com.smartcity.model.GridVector;
import com.smartcity.model.Intersection;
import com.smartcity.model.Intersection.LightState;
import com.smartcity.utility.VectorUtility;

/**
 *
 * @author Blake
 */
public class DetermineStopEvent extends Event{
    
    private final Car car;
    private final Intersection stopAt;

    public DetermineStopEvent(double eventTime, Car car, Intersection stopAt) {
        super(eventTime);
        this.car = car;
        this.stopAt = stopAt;
    }

    @Override
    protected void eventAction() {
        
        LightState lightState;
        switch(car.getVector().direction){
            case NORTH: case SOUTH:
                lightState = stopAt.getNSLightState();
                break;
                
            case EAST: case WEST:
                lightState = stopAt.getEWLightState();
                break;
                
            default:
                throw new IllegalArgumentException(car.getVector().direction + " is not handled");
        }
                
        //Move the car to center of the current intersection then submit a new approach intersection event for the next intersection
        if(lightState.equals(Intersection.LightState.GREEN)){
            Intersection nextIntersection = car.getRoute().getNextIntersection(stopAt);
            if(nextIntersection != null){
                
                //Determine the center and set the rotation
                GridVector moveToVector = stopAt.getEdge(car.getVector().direction.getOppisite());
                
                double deltaTime = car.getTimeTo(moveToVector);
                Simulation.WORLD.moveCar(car, moveToVector, deltaTime);
                car.setVector(moveToVector);
                EventBus.submitEvent(new IntersectionCrossEvent(eventTime + deltaTime, car, stopAt));
            
            //There are no more intersections, the car is exiting thoug this interseciton in it's current direction
            } else {
                GridVector exitVector = VectorUtility.getIntersectionEdge(stopAt, car.getVector().direction.getOppisite());
                exitVector = new GridVector(exitVector.ewPoint, exitVector.nsPoint, car.getVector().direction);
                double deltaTime = car.getTimeTo(exitVector);
                Simulation.WORLD.moveCar(car, exitVector, deltaTime);
                car.setVector(exitVector);
                EventBus.submitEvent(new CarExitEvent(eventTime + deltaTime, car));
            }

        } else {
            GridVector stopVector = stopAt.getNextCarStopVector(car);
            double deltaTime = car.getTimeTo(stopVector);
            Simulation.WORLD.moveCar(car, stopVector, deltaTime);
            car.setVector(stopVector);
            car.setState(Car.State.STOPPING);
            EventBus.submitEvent(new IntersectionStopEvent(deltaTime + eventTime, car));
        }
    }
}
