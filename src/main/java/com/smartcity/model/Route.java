/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.smartcity.model;

import java.util.List;

/**
 *
 * @author Blake
 */
public class Route {

    private final List<Intersection> intersections;

    public Route(List<Intersection> intersections){
        this.intersections = intersections;
    }
    
    public List<Intersection> getIntersections(){
       return intersections;
    }
}
