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
    private final List<Intersection> edges;
    
    public Grid(int NSBlocks, int EWBlocks){
        grid = new Intersection[NSBlocks][EWBlocks];
        for(int i = 0; i < NSBlocks; i++){
            for(int j = 0; j < EWBlocks; j++){
                grid[i][j] = new Intersection(i, j);
            }
        }
        
        edges = new ArrayList();
        for(int i = 0; i < grid.length; i++){
            edges.add(grid[0][i]);
            edges.add(grid[grid.length-1][i]);
        }
        for(int i = 0; i < grid[0].length; i++){
            edges.add(grid[i][0]);
            edges.add(grid[i][grid[0].length-1]);
        }
    }
    
    public List<Intersection> getEdgeIntersections(){
        return edges;
    }
    
    public Direction getEdgeDirection(Intersection intersection){
        if(edges.contains(intersection) == false){
            throw new IllegalArgumentException("The edge: " + intersection + " was not on the edge of the map.");
        }
        
        if(intersection.getNSBlock() == 0) {
            return Direction.SOUTH;
        } else if(intersection.getNSBlock() == grid.length-1) {
            return Direction.NORTH;
        } else if(intersection.getEWlock() == 0) {
            return Direction.EAST;
        } else {
            return Direction.WEST;
        }
    }
    
    public Intersection getIntersection(int NSBlock, int EWBlock){
        return grid[NSBlock][EWBlock];
    }
}
