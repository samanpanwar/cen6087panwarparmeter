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
public class GridCoordinate {
    
    public final double ewPoint, nsPoint;
    
    public GridCoordinate(double ewPoint, double nsPoint){
        this.ewPoint = ewPoint;
        this.nsPoint = nsPoint;
    }
    
    public final double distanceTo(GridCoordinate that){
        double deltaX = ewPoint - that.ewPoint;
        double deltaY = nsPoint - that.nsPoint;
        return Math.sqrt(Math.pow(deltaX, 2) + Math.pow(deltaY, 2));
    }
}
