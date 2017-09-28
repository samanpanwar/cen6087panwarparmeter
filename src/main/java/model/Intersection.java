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
        return "NS: " + NSBlock + " EW: " + EWBlock;
    }
}
