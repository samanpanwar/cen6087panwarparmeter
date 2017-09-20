/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package factory;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import model.CardinalDirection;
import model.MoveDirection;
import static model.MoveDirection.FORWARD;
import static model.MoveDirection.LEFT;
import static model.MoveDirection.RIGHT;
import static model.MoveDirection.U_TURN;
import model.Grid;
import model.Intersection;
import model.Route;

/**
 *
 * @author Blake
 */
public class RouteFactory {
    
    private static final MoveDirection[] DIR_SEED = {
        FORWARD, FORWARD, FORWARD, FORWARD, RIGHT, RIGHT, LEFT, LEFT, U_TURN
    };
    
    private final Random random;
    private final Grid grid;
    
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
            
            //updates the block the route is on TODO:
            switch(currentDirection){
                
            }
            
            //breaks if the current route is off the grid TODO: get the max lengths
            if(NSBlock < 0 || EWBlock < 0){
                break;
            }
        }
        
        return new Route(intersections);
    }
}
