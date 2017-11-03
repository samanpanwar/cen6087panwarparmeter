/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.smartcity.event;

import com.smartcity.application.Simulation;
import com.smartcity.application.enumeration.LightDirection;
import com.smartcity.model.Car;
import com.smartcity.model.GridVector;
import com.smartcity.model.Intersection;

/**
 *
 * @author Blake
 */
public class ApproachIntersectionEvent extends Event{
    
    private final Car car;
    private final Intersection to;

    public ApproachIntersectionEvent(double eventTime, Car car, Intersection to) {
        super(eventTime);
        this.car = car;
        this.to = to;
    }

    @Override
    protected void eventAction() {
        
        //If this is the entry event add the car to the GUI
        if(car.getEntryTime() == eventTime){
            Simulation.WORLD.addCar(car);
        }
        
        //Approach the yellow light vector and submit a car to the queue 
        int carsInIntersection = to.getNumCarsWaiting(car.getVector().direction);
        if(carsInIntersection == 0){
            GridVector newVector = to.getYellowLightVector(car);
            double deltaTime = car.getTimeTo(newVector);
            Simulation.WORLD.moveCar(car, newVector, deltaTime);
            car.setVector(newVector);
            EventBus.submitEvent(new DetermineStopEvent(deltaTime + eventTime, car, to));
        } else {
            
            //Move the car to the next available spot in the intersection
            GridVector stopVector = to.getNextCarStopVector(car);
            double deltaTime = car.getTimeTo(stopVector);
            Simulation.WORLD.moveCar(car, stopVector, deltaTime);
            car.setVector(stopVector);
            EventBus.submitEvent(new IntersectionStopEvent(deltaTime + eventTime, car));
            
            //determines when to switch the intersection based on the car's
            if(Simulation.LIGHT_CHANGE_TYPE == Simulation.LightChangeType.CAR_BASED && 
                    eventTime > to.getCurrentDequeueFinishTime() && to.getIsSwitching() == false){
                
                boolean switchCriteria = false;
                double lightChangeTime = Simulation.LIGHT_CHANGE_TIME + Simulation.LIGHT_CHANGE_TIME;
                if(carsInIntersection > Simulation.CAR_CHANGE_LIGHT_NUM){
                    double lastCarMoveTime = (carsInIntersection * Simulation.CAR_STOP_DISTANCE) / Simulation.CAR_VELOCITY;
                    double dequeueTime = Simulation.DEQUQE_LIGHT_TIME * carsInIntersection;
                    to.setCurrentDequeueFinishTime(eventTime + lastCarMoveTime + dequeueTime + lightChangeTime);
                    switchCriteria = true;
                } else if(eventTime > to.getLastGreenChange() + lightChangeTime + Simulation.LIGHT_CHANGE_GREEN_TIME){
                    to.setLastGreenChange(eventTime + lightChangeTime + Simulation.LIGHT_CHANGE_GREEN_TIME);
                    switchCriteria = true;
                }
                
                if(switchCriteria){
                    to.setSwitching(true);
                    double lightEventTime = eventTime + Simulation.LIGHT_CHANGE_TIME;
                    LightDirection direction = to.getLightDirection().getOppisite();
                    EventBus.submitEvent(new LightChangeEvent(lightEventTime, to, direction, LightChangeEvent.ChangeType.INITIAL));
                }
            }
        }
    }
}
