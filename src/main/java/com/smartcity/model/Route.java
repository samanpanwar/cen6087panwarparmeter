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
    
    /**
     * @param intersection gets the intersection after this one. 
     * @return null if the route is at the end, throws exception 
     * if the route does not contain the intersection
     */
    public Intersection getNextIntersection(Intersection intersection){
        int currentIndex = this.intersections.indexOf(intersection);
        if(currentIndex == -1){
            throw new IllegalArgumentException("The intersection: " + intersection + " is not in the route");
        } else if(currentIndex == this.intersections.size()-1){
            return null;
        } else {
            return this.intersections.get(currentIndex+1);
        }
    }
}
