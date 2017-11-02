/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.smartcity.model;

import com.smartcity.application.enumeration.CardinalDirection;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author Blake
 */
public class Grid {
    
    private final Intersection[][] grid;
    
    //Data structures used to assist with algroithims. 
    private final List<List<Intersection>> entries;
    private final List<List<Intersection>> exits;
    private final List<Integer> NBStreets = new ArrayList();
    private final List<Integer> EBStreets = new ArrayList();
    private final List<Integer> SBStreets = new ArrayList();
    private final List<Integer> WBStreets = new ArrayList();
    
    /**
     * Generates the grid. index 
     * 0,       0        = NW corner 
     * 0,       NSBlocks = NE corner
     * EWBlocks,NSBlocks = SE corner
     * EWBlocks,0        = SW corner
     * @param numNSBlocks
     * @param numEWBlocks 
     */
    public Grid(int numEWBlocks, int numNSBlocks){
        grid = new Intersection[numEWBlocks][numNSBlocks];
        for(int i = 0; i < numEWBlocks; i++){
            for(int j = 0; j < numNSBlocks; j++){
                grid[i][j] = new Intersection(i, j);
            }
        }
        
        //Builds the edges data structure (note: corners are omitted)
        entries = new ArrayList();
        exits = new ArrayList();
        for(int i = 0; i < 4; i++){
            entries.add(new ArrayList());
            exits.add(new ArrayList());
        }
        for(int i = 1; i < getEWBlockSize()-1; i++){
            if(grid[i][0].getNSDirection().equals(CardinalDirection.SOUTH)){
                SBStreets.add(i);
                entries.get(0).add(grid[i][0]);               //Northern entrances (Southbound direction)
            }  else {
                exits.get(0).add(grid[i][0]);                 //Northern exits (NorthBound direction)
            }
            if(grid[i][getNSBlockSize()-1].getNSDirection().equals(CardinalDirection.NORTH)){
                NBStreets.add(i);
                entries.get(2).add(grid[i][getNSBlockSize()-1]);//Southern entrances (Northbound direction)
            } else {
                exits.get(2).add(grid[i][getNSBlockSize()-1]);//Southern exits (Southbound direction)
            }
        }
        for(int j = 1; j < getNSBlockSize()-1; j++){
            if(grid[0][j].getEWDirection().equals(CardinalDirection.WEST)){
                WBStreets.add(j);
                exits.get(1).add(grid[0][j]);               //Eastern exits (Eastbound direction)
            } else {
                entries.get(1).add(grid[0][j]);               //Eastern entrances (Westbound direction)
            }
            if(grid[getEWBlockSize()-1][j].getEWDirection().equals(CardinalDirection.EAST)){
                EBStreets.add(j);
                exits.get(3).add(grid[getEWBlockSize()-1][j]);   //Western exits (Westbound direction)
            } else {
                entries.get(3).add(grid[getEWBlockSize()-1][j]);   //Western entrances (Eastbound direction)
            }
        }
    }
    
    /**
     * @return A two dimensional list, the first list is the entrance in the following form corresponding to the sides:
     * 0 = North (x,0)
     * 1 = West  (0,y) 
     * 2 = South (x,m)
     * 3 = East  (m,y)
     * The second dimension corresponds to the intersections in those edges. 
     */
    public List<List<Intersection>> getEntryIntersections(){
        return entries;
    }
    
    /**
     * @return A two dimensional list, the first list is the exits in the following form corresponding to the sides:
     * 0 = North (x,0)
     * 1 = West  (0,y) 
     * 2 = South (x,m)
     * 3 = East  (m,y)
     * The second dimension corresponds to the intersections in those edges. 
     */
    public List<List<Intersection>> getExitIntersections(){
        return exits;
    }
    
    public List<Integer> getNBStreets(){
        return NBStreets;
    }
    
    public List<Integer> getEBStreets(){
        return EBStreets;
    }
    
    public List<Integer> getSBStreets(){
        return SBStreets;
    }
    
    public List<Integer> getWBStreets(){
        return WBStreets;
    }
    
    public final int getEWBlockSize(){
        return grid.length;
    }
    
    public final int getNSBlockSize(){
        return grid[0].length;
    }
    
    public Intersection getIntersection(int EWBlock, int NSBlock){
        return grid[EWBlock][NSBlock];
    }
}
