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
public class Intersection {
    
    public final int distanceToNext = 100; //Distance units. 
    private final int NSBlock, EWBlock;
    private final CardinalDirection NSDirection, EWDirection;
    
    public Intersection(int NSBlock, int EWBlock){
        this.NSBlock = NSBlock;
        this.EWBlock = EWBlock;
        if(NSBlock % 2 == 0){
            NSDirection = CardinalDirection.NORTH;
        } else { 
            NSDirection = CardinalDirection.SOUTH;
        }
        if(EWBlock % 2 == 0){
            EWDirection = CardinalDirection.EAST;
        } else { 
            EWDirection = CardinalDirection.WEST;
        }
    }
    
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
            if(NSBlock < otherNSBlock){
                return CardinalDirection.NORTH;
            } else {
                return CardinalDirection.SOUTH;
            }
        }else if(NSBlock == otherNSBlock){
            if(EWBlock > otherEWBlock){
                return CardinalDirection.EAST;
            } else {
                return CardinalDirection.WEST;
            }
        } else {
            throw new IllegalArgumentException("The passed in intersection is not orthogonal to this intersection");
        }
    }
    
    @Override
    public String toString(){
        return "NS: " + NSBlock + " EW: " + EWBlock;
    }
}
