/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package model;

import java.math.BigInteger;

/**
 *
 * @author Blake
 */
public class Car {
    
    public final long velocity = 5L;//distance units / time unit
    private final BigInteger entryTime;
    private final Route route;
    
    public Car(BigInteger entryTime, Route route){
        this.entryTime = entryTime;
        this.route = route;
    }
    
    public BigInteger getEntryTime(){
        return entryTime;
    }
    
    public Route getRoute(){
        return route;
    }
}
