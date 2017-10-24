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
import com.smartcity.utility.VectorUtility;

/**
 *
 * @author Blake
 */
public class IntersectionCrossEvent extends Event{
    
    private final Car car;
    private final Intersection from;

    public IntersectionCrossEvent(double eventTime, Car car, Intersection from) {
        super(eventTime);
        this.from = from;
        this.car = car;
    }

    @Override
    public void resolveEvent() {
        Intersection to = car.getRoute().getNextIntersection(from);
        if(to != null){
            CardinalDirection newDirection = VectorUtility.getDirectionTo(from, to);
            GridVector newVector = from.getEdge(newDirection);
            GridVector newCarVector = new GridVector(newVector.ewPoint, newVector.nsPoint, newDirection);
            double deltaTime;
            
            //The next intersection is straight ahead. 
            if(newDirection == car.getVector().direction){
                deltaTime = car.getVector().distanceTo(newCarVector)/4;
                Simulation.WORLD.moveCar(car, newCarVector, deltaTime);
                
            } else if(newDirection == car.getVector().direction.getOppisite()){
                throw new IllegalArgumentException("The next vector is behind the car!");
                
            //There is a curve
            } else {
                deltaTime = car.getVector().distanceTo(newCarVector)/4;
                Simulation.WORLD.turnCarTo(car, newVector, deltaTime);
            }
            
            car.setVector(newCarVector);
            EventBus.submitEvent(new ApproachIntersectionEvent(eventTime + deltaTime, car, to));
        } else {
            throw new IllegalArgumentException("There was not another intersection to travel to!");
        }
    }
}
