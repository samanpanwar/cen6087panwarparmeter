/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.smartcity.model;

/**
 *
 * @author Blake
 */
public class GridVector {
    
    public final double ewPoint, nsPoint;
    public final CardinalDirection direction;
    
    public GridVector(double ewPoint, double nsPoint, CardinalDirection direction){
        this.ewPoint = ewPoint;
        this.nsPoint = nsPoint;
        this.direction = direction;
    }
    
    public final double distanceTo(GridVector that){
        double deltaX = ewPoint - that.ewPoint;
        double deltaY = nsPoint - that.nsPoint;
        return Math.sqrt(Math.pow(deltaX, 2) + Math.pow(deltaY, 2));
    }
    
    @Override
    public String toString(){
        return "x:" + this.ewPoint + " y:" + this.nsPoint + " dir:"+ this.direction;
    }
}
