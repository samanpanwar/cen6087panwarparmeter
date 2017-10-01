/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package model;

import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author Blake
 */
public class Grid {
    
    public static final int INTERSECTION_DISATANCE = 100;
    
    private final Intersection[][] grid;
    
    //Data structures used to assist with algroithims. 
    private final List<List<Intersection>> edges;
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
     * @param NSBlocks
     * @param EWBlocks 
     */
    public Grid(int NSBlocks, int EWBlocks){
        grid = new Intersection[EWBlocks][NSBlocks];
        for(int i = 0; i < EWBlocks; i++){
            for(int j = 0; j < NSBlocks; j++){
                grid[i][j] = new Intersection(i, j);
            }
        }
        
        //Builds the edges data structure (note: corners are omitted)
        edges = new ArrayList();
        for(int i = 0; i < 4; i++){
            edges.add(new ArrayList());
        }
        for(int i = 1; i < getEWBlockSize()-1; i++){
            if(grid[i][0].getNSDirection().equals(CardinalDirection.SOUTH)){
                SBStreets.add(i);
                edges.get(0).add(grid[i][0]);               //Northern edges (Southbound direction)
            } 
            if(grid[i][getEWBlockSize()-1].getNSDirection().equals(CardinalDirection.NORTH)){
                NBStreets.add(i);
                edges.get(2).add(grid[i][getEWBlockSize()-1]);//Southern edges (Northbound direction)
            }
        }
        for(int i = 1; i < getEWBlockSize()-1; i++){
            if(grid[0][i].getEWDirection().equals(CardinalDirection.WEST)){
                WBStreets.add(i);
                edges.get(1).add(grid[0][i]);               //Eastern edges (Westbound direction)
            } 
            if(grid[getEWBlockSize()-1][i].getEWDirection().equals(CardinalDirection.EAST)){
                EBStreets.add(i);
                edges.get(3).add(grid[getEWBlockSize()-1][i]);   //Western edges (Eastbound direction)
            }   
        }
    }
    
    /**
     * @return A two dimensional list, the first list is the edges in the following form:
     * 0 = North
     * 1 = West
     * 2 = South
     * 3 = North
     * The second dimension corresponds to the intersections in those edges. 
     */
    public List<List<Intersection>> getEdgeIntersections(){
        return edges;
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
    
    public final int getNSBlockSize(){
        return grid.length;
    }
    
    public final int getEWBlockSize(){
        return grid[0].length;
    }
    
    public Intersection getIntersection(int NSBlock, int EWBlock){
        return grid[NSBlock][EWBlock];
    }
    
    public boolean isEdge(Intersection intersection){
        for(List<Intersection> edgeList : edges){
            if(edgeList.contains(intersection)){
                return true;
            }
        }
        return false;
    }
}
