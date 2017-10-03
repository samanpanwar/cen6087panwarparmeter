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
    
    private final int NSBlock, EWBlock;
    
    public Intersection(int NSBlock, int EWBlock){
        this.NSBlock = NSBlock;
        this.EWBlock = EWBlock;
    }
    
    public int getNSBlock(){
        return NSBlock;
    }
    
    public int getEWBlock(){
        return EWBlock;
    }
    
    @Override
    public String toString(){
        return "EW: " + EWBlock + " NS: " + NSBlock;
    }
    
    public CardinalDirection getInitialDirection(Grid grid){
        if(EWBlock == 0){
            return CardinalDirection.NORTH;
        } else if(EWBlock == grid.getNSBlockSize()-1){
            return CardinalDirection.SOUTH;
        } else if(NSBlock == 0){
            return CardinalDirection.EAST;
        } else if(NSBlock == grid.getEWBlockSize()-1){
            return CardinalDirection.WEST;
        } else {
            throw new IllegalArgumentException(this + " is not an edge");
        }
    }
    
    public CardinalDirection getDirectionTo(Intersection intersection){
        
        if(this.equals(intersection)){
            throw new IllegalArgumentException("The passed in intersecion is equal to this.");
        }
        System.out.println("compare:" + this + ":" + intersection);
        int otherNSBlock = intersection.NSBlock;
        int otherEWBlock = intersection.EWBlock;
        if(EWBlock == otherEWBlock){
            if(NSBlock < otherNSBlock){
                return CardinalDirection.SOUTH;
            } else {
                return CardinalDirection.NORTH;
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
}
