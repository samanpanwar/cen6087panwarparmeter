/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package factory;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import model.Direction;
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
        Direction currentDirection = grid.getEdgeDirection(initialIntersection);
        MoveDirection moveDirection = DIR_SEED[random.nextInt(DIR_SEED.length)];
        switch(moveDirection){
            case FORWARD:
                break;
                
            case LEFT:
                currentDirection = Direction.values()[%Direction.values().length-1]
                break;
                
            case RIGHT:
                break;
                
            case U_TURN:
                break;
                
            default:
                throw new UnsupportedOperationException("Direction: " + moveDirection + " not supported");
        }
        
        
        return new Route(intersections);
    }
}
