/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package model;

/**
 *
 * @author Blake
 */
public class Car {
    
    private static final long VELOCITY = 1L;//intersections / time unit
    
    private final long entryTime;
    private final Route route;
    
    public Car(long entryTime, Route route){
        this.entryTime = entryTime;
        this.route = route;
    }
}
