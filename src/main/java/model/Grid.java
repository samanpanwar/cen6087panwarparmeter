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
    
    private final Intersection[][] grid;
    
    //Data structures used to assist with algroithims. 
    private final List<List<Intersection>> edges;
    
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
        for(int i = 1; i < grid.length-1; i++){
            if(grid[i][0].getNSDirection().equals(CardinalDirection.SOUTH)){
                edges.get(0).add(grid[i][0]);               //Northern edges (Southbound direction)
            } 
            if(grid[i][grid[0].length-1].getNSDirection().equals(CardinalDirection.NORTH)){
                edges.get(2).add(grid[i][grid[0].length-1]);//Southern edges (Northbound direction)
            }
        }
        for(int i = 1; i < grid[0].length-1; i++){
            if(grid[0][i].getEWDirection().equals(CardinalDirection.WEST)){
                edges.get(1).add(grid[0][i]);               //Eastern edges (Westbound direction)
            } 
            if(grid[grid.length-1][i].getEWDirection().equals(CardinalDirection.EAST)){
                edges.get(3).add(grid[grid.length-1][i]);   //Western edges (Eastbound direction)
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
    
    public int getNSBlockSize(){
        return grid.length;
    }
    
    public int getEWBlockSize(){
        return grid[0].length;
    }
    
    public CardinalDirection getEdgeDirection(Intersection intersection){
        if(isEdge(intersection) == false){
            throw new IllegalArgumentException("The edge: " + intersection + " was not on the edge of the map.");
        }
        
        if(intersection.getNSBlock() == 0) {
            return CardinalDirection.SOUTH;
        } else if(intersection.getNSBlock() == grid.length-1) {
            return CardinalDirection.NORTH;
        } else if(intersection.getEWBlock() == 0) {
            return CardinalDirection.EAST;
        } else {
            return CardinalDirection.WEST;
        }
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
