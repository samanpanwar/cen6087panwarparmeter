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
import com.smartcity.utility.VectorUtility;

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
            if((Simulation.LIGHT_CHANGE_TYPE == Simulation.LightChangeType.CAR_BASED ||
                    Simulation.LIGHT_CHANGE_TYPE == Simulation.LightChangeType.COORDINATED)&& 
                    eventTime > to.getCurrentDequeueFinishTime() && to.getIsSwitching() == false){
                
                boolean switchCriteria = false;
                double lightChangeTime = Simulation.LIGHT_CHANGE_TIME + Simulation.LIGHT_CHANGE_TIME;
                double lastCarMoveTime = (carsInIntersection * Simulation.CAR_STOP_DISTANCE) / Simulation.CAR_VELOCITY;
                double dequeueTime = Simulation.DEQUQE_LIGHT_TIME * carsInIntersection;
                double nextDequeueFinish = eventTime + lastCarMoveTime + dequeueTime + lightChangeTime;
                if(carsInIntersection > Simulation.CAR_CHANGE_LIGHT_NUM){
                    switchCriteria = true;
                } else if(eventTime > to.getLastGreenChange() + lightChangeTime + Simulation.LIGHT_CHANGE_GREEN_TIME){
                    to.setLastGreenChange(eventTime + lightChangeTime + Simulation.LIGHT_CHANGE_GREEN_TIME);
                    switchCriteria = true;
                }
                
                if(switchCriteria){
                    to.setSwitching(true);
                    to.setCurrentDequeueFinishTime(nextDequeueFinish);
                    double lightEventTime = eventTime + Simulation.LIGHT_CHANGE_TIME;
                    LightDirection direction = to.getLightDirection().getOppisite();
                    EventBus.submitEvent(new LightChangeEvent(lightEventTime, to, direction, LightChangeEvent.ChangeType.INITIAL));
                    
                    if(Simulation.LIGHT_CHANGE_TYPE == Simulation.LightChangeType.COORDINATED){
                        Intersection last = to;
                        Intersection next = car.getRoute().getNextIntersection(to);
                        while(next != null && lightEventTime > next.getCurrentDequeueFinishTime() && next.getIsSwitching() == false){
                            lightEventTime += (double) Simulation.INTERSECTION_DISATANCE / Simulation.CAR_VELOCITY;
                            LightDirection carDirection = LightDirection.get(VectorUtility.getDirectionTo(last, next));
                            direction = next.getLightDirection().getOppisite();
                            next.setCurrentDequeueFinishTime(lightEventTime + nextDequeueFinish);
                            if(carDirection != direction){
                                EventBus.submitEvent(new LightChangeEvent(lightEventTime, next, direction, LightChangeEvent.ChangeType.INITIAL));
                            }
                            last = next;
                            next = car.getRoute().getNextIntersection(next);
                        }
                    }
                }
            }
        }
    }
}
