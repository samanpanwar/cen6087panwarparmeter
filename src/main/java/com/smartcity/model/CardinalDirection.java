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
public enum CardinalDirection {
    
    NORTH(0), SOUTH(1), EAST(2), WEST(3);
    
    public final int index;

    private CardinalDirection(int index){
        this.index = index;
    }
    
    public CardinalDirection getOppisite(){
        switch(this){
            case NORTH:
                return SOUTH;
                
            case EAST: 
                return WEST;
                
            case SOUTH:
                return NORTH;
                
            case WEST:
                return EAST;
                
            default:
                throw new IllegalArgumentException(this + " is not handled.");
        }
    }
    
    public double getDegrees(){
        switch(this){
            case NORTH:
                return 0;
                
            case EAST: 
                return 90;
                
            case SOUTH:
                return 180;
                
            case WEST:
                return 270;
                
            default:
                throw new IllegalArgumentException(this + " is not handled.");
        }
    }
}
