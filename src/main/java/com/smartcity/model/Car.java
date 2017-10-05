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

    @Override
    public String toString() {
        return "Car: " + carNum;
    }
}
