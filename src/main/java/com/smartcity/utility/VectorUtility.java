/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.smartcity.utility;

import com.smartcity.model.CardinalDirection;
import com.smartcity.model.Grid;
import com.smartcity.model.GridVector;
import com.smartcity.model.Intersection;
import com.smartcity.model.Route;

/**
 *
 * @author Blake
 */
public class VectorUtility {
    
    
    public static CardinalDirection getDirectionTo(Intersection from, Intersection to){
        
        if(from.equals(to)){
            throw new IllegalArgumentException("The passed in intersection is equal to this.");
        }
        
        int NSBlock = from.getNSBlock();
        int EWBlock = from.getEWBlock();
        int otherNSBlock = to.getNSBlock();
        int otherEWBlock = to.getEWBlock();
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
    
    public static GridVector getCenter(Intersection intersection){
        int gridSize = Grid.INTERSECTION_DISATANCE;
        int x = (intersection.getEWBlock() * gridSize) + (gridSize/2);
        int y = (intersection.getNSBlock()* gridSize) + (gridSize/2);
        return new GridVector(x, y, null);
    }
    
    public static GridVector getIntersectionEdge(Intersection intersection, CardinalDirection direction){
        int gridSize = Grid.INTERSECTION_DISATANCE;
        int xCell = intersection.getEWBlock() * gridSize;
        int yCell = intersection.getNSBlock() * gridSize;
        
        //Direction Modifier
        int xDir;
        int yDir;
        switch(direction){
            case NORTH:
                xDir = gridSize/2;
                yDir = gridSize;
                break;
                
            case EAST:
                xDir = 0;
                yDir = gridSize/2;
                break;
                
            case SOUTH:
                xDir = gridSize/2; 
                yDir = 0;
                break; 
                
            case WEST:
                xDir = gridSize;
                yDir = gridSize/2;
                break;
                
            default:
                throw new IllegalArgumentException(direction + " is not handled");   
        }
                
        return new GridVector(xCell + xDir, yCell + yDir, direction);
    }
    
    public static GridVector initializePositionDirection(Route route){
        Intersection firstIntersection = route.getIntersections().get(0);
        CardinalDirection direction = getDirectionTo(firstIntersection, route.getNextIntersection(firstIntersection));
        return getIntersectionEdge(firstIntersection, direction);
    }
    
    /**
     * 
     * @param vector the vector to mutate will add the magnitude to the direction, the direction will be added to.
     * @param magnitude the magnitude to add to the vector
     * @return 
     */
    public static GridVector addMagnitude(GridVector vector, double magnitude){
        double xModifier;
        double yModifier;
        switch(vector.direction){
            
            case NORTH:
                xModifier = 0;
                yModifier = magnitude * -1;
                break;
                
            case EAST:
                xModifier = magnitude;
                yModifier = 0;
                break;
            
            case SOUTH:
                xModifier = 0;
                yModifier = magnitude;
                break;
                
            case WEST:
                xModifier = magnitude * -1;
                yModifier = 0;
                break;
            
            default:
                throw new IllegalArgumentException(vector.direction + " is not handled");
        }    
        return new GridVector(xModifier + vector.ewPoint, yModifier + vector.nsPoint, vector.direction);
    }
}
