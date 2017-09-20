/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package factory;

import static model.MoveDirection.FORWARD;
import static model.MoveDirection.LEFT;
import static model.MoveDirection.RIGHT;
import static model.MoveDirection.U_TURN;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import model.CardinalDirection;
import model.MoveDirection;
import model.Grid;
import model.Intersection;
import model.Route;

/**
 *
 * @author Blake
 */
public class RouteFactory {
    
    //Sets the likelihood of the directions
    private static final MoveDirection[] DIR_SEED = {
        FORWARD, FORWARD, FORWARD, FORWARD, RIGHT, RIGHT, LEFT, LEFT, U_TURN
    };
    
    private final Random random;
    private final Grid grid;
    
    /**
     * @param grid the grid to build the route on
     * @param seed a seed that can be used to generate the RNG. If the grid dimensions are the same and
     * the seed is the same between runs then the resultant generated routes will be identical aswell. 
     */
    public RouteFactory(Grid grid, long seed){
        this.grid = grid;
        this.random = new Random(seed);
    }
    
    public Route generateRoute(){
        
        //Initalizes the intersections
        List<Intersection> intersections = new ArrayList();
        Intersection initialIntersection = grid.getEdgeIntersections().get(random.nextInt(grid.getEdgeIntersections().size()));
        intersections.add(initialIntersection);
        
        //Moves the route until it gets off the grid
        CardinalDirection currentDirection = grid.getEdgeDirection(initialIntersection);
        Intersection currentIntersection = initialIntersection;
        int NSBlock = currentIntersection.getNSBlock();
        int EWBlock = currentIntersection.getEWBlock();
        while(true){
            
            //Finds a move direction and sets the cardinal direction, if there is a turn an intersection is added
            MoveDirection moveDirection = DIR_SEED[random.nextInt(DIR_SEED.length)];
            switch(moveDirection){
                case FORWARD:
                    break;

                case LEFT:
                    currentDirection = CardinalDirection.values()[currentDirection.index -1 % 4];
                    intersections.add(grid.getIntersection(NSBlock, EWBlock));
                    break;

                case RIGHT:
                    currentDirection = CardinalDirection.values()[currentDirection.index +1 % 4];
                    intersections.add(grid.getIntersection(NSBlock, EWBlock));
                    break;

                case U_TURN:
                    currentDirection = CardinalDirection.values()[currentDirection.index -2 % 4];
                    intersections.add(grid.getIntersection(NSBlock, EWBlock));
                    break;

                default:
                    throw new UnsupportedOperationException("Direction: " + moveDirection + " not supported");
            }
            
            //updates the block the route is on 
            Intersection beforeMove = grid.getIntersection(NSBlock, EWBlock);
            switch(currentDirection){
                case NORTH:
                    NSBlock ++;
                    break;
                    
                case EAST:
                    EWBlock ++;
                    break;
                    
                case SOUTH:
                    NSBlock --;
                    break;
                    
                case WEST:
                    EWBlock --;
                    break;
            }
            
            //breaks if the current route is off the grid, adds the intersection the route was at before moving
            if(NSBlock < 0 || NSBlock >= grid.getNSBlockSize() ||
                    EWBlock < 0 || EWBlock >=grid.getEWBlockSize()){
                
                intersections.add(beforeMove);
                break;
            }
        }
        
        return new Route(intersections);
    }
}
