/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.smartcity.model;

import java.math.BigInteger;

/**
 *
 * @author Blake
 */
public class Car {
    
    private static int carsCreated = 0;
    
    public final long velocity = 5L;//distance units / time unit
    
    private final BigInteger entryTime;
    private final Route route;
    private final int carNum;
    private GridCoordinate position;
    
    public Car(BigInteger entryTime, Route route){
        this.entryTime = entryTime;
        this.route = route;
        this.carNum = carsCreated++;
    }
    
    public BigInteger getEntryTime(){
        return entryTime;
    }
    
    public Route getRoute(){
        return route;
    }
    
    public GridCoordinate getPosition(){
        return position;
    }
    
    public void setPosition(GridCoordinate position){
        this.position = position;
    }

    @Override
    public String toString() {
        return "Car: " + carNum;
    }
    
    @Override
    public int hashCode() {
        return carNum;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final Car other = (Car) obj;
        return this.carNum == other.carNum;
    }
}
