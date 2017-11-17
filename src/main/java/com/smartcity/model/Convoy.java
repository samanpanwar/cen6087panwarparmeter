/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.smartcity.model;

import java.util.Collections;
import java.util.List;

/**
 *
 * @author Blake
 */
public class Convoy {
    
    private List<Car> cars;
    public final Route route;
    
    public Convoy(Route route){
        this.route = route;
    }
    
    public void setCars(List<Car> cars){
        if(this.cars != null){
            throw new IllegalStateException("The cars have already been set!");
        }
        this.cars = Collections.unmodifiableList(cars);
    }
    
    public List<Car> getCars(){
        return cars;
    }
    
    public boolean isLast(Car car){
        Car lastCar = cars.get(cars.size()-1);
        return lastCar.equals(car);
    }
}
