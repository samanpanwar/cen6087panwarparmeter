/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.smartcity.model;

import java.util.LinkedList;
import java.util.Queue;

/**
 *
 * @author Blake
 */
public class Intersection {
    
    public enum LightState{NORTH_SOUTH, EAST_WEST};
    
    private final int EWBlock, NSBlock;
    private final CardinalDirection NSDirection, EWDirection;
    private final Queue<Car> lightQueueNB, lightQueueEB, lightQueueSB, lightQueueWB;
    
    public Intersection(int EWBlock, int NSBlock){
        this.EWBlock = EWBlock;
        this.NSBlock = NSBlock;
        if(EWBlock % 2 == 0){
            NSDirection = CardinalDirection.NORTH;
        } else { 
            NSDirection = CardinalDirection.SOUTH;
        }
        if(NSBlock % 2 == 0){
            EWDirection = CardinalDirection.WEST;
        } else { 
            EWDirection = CardinalDirection.EAST;
        }
        lightQueueNB = new LinkedList();
        lightQueueEB = new LinkedList();
        lightQueueSB = new LinkedList();
        lightQueueWB = new LinkedList();
    }
    
    public void submitCar
    
    public CardinalDirection getNSDirection(){
        return NSDirection;
    }
    
    public CardinalDirection getEWDirection(){
        return EWDirection;
    }
    
    public int getNSBlock(){
        return NSBlock;
    }
    
    public int getEWBlock(){
        return EWBlock;
    }
    
    public CardinalDirection getDirectionTo(Intersection intersection){
        
        if(this.equals(intersection)){
            throw new IllegalArgumentException("The passed in intersecion is equal to this.");
        }
        
        int otherNSBlock = intersection.NSBlock;
        int otherEWBlock = intersection.EWBlock;
        if(EWBlock == otherEWBlock){
            if(NSBlock > otherNSBlock){
                return CardinalDirection.NORTH;
            } else {
                return CardinalDirection.SOUTH;
            }
        }else if(NSBlock == otherNSBlock){
            if(EWBlock > otherEWBlock){
                return CardinalDirection.WEST;
            } else {
                return CardinalDirection.EAST;
            }
        } else {
            throw new IllegalArgumentException("The passed in intersection is not orthogonal to this intersection");
        }
    }
    
    @Override
    public String toString(){
        return "EW: " + EWBlock + " NS: " + NSBlock;
    }
}
