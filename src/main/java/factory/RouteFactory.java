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
import static model.Direction.FORWARD;
import static model.Direction.LEFT;
import static model.Direction.RIGHT;
import static model.Direction.U_TURN;
import model.Grid;
import model.Intersection;
import model.Route;

/**
 *
 * @author Blake
 */
public class RouteFactory {
    
    private static final Direction[] DIR_SEED = {
        FORWARD, FORWARD, FORWARD, FORWARD, RIGHT, RIGHT, LEFT, LEFT, U_TURN
    };
    
    private final Random random;
    private final Grid grid;
    
    public RouteFactory(Grid grid, long seed){
        this.grid = grid;
        this.random = new Random(seed);
    }
    
    public Route generateRoute(){
        
        List<Intersection> intersections = new ArrayList();
        intersections.add(grid.getEdgeIntersections().get(random.nextInt(grid.getEdgeIntersections().size())));
        
        
        return new Route(intersections);
    }
}
