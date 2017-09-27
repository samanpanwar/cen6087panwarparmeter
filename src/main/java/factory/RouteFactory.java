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
import java.util.Arrays;
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
        
        //gets the entry and exit sides
        List<Integer> entryPoints = new ArrayList(Arrays.asList(0,1,2,3));
        int entrySide = entryPoints.get(random.nextInt(4));
        entryPoints.remove(entrySide);
        int exitSide = entryPoints.get(random.nextInt(3));
        if(entrySide == exitSide){
            throw new IllegalStateException("The algroithim generated the same entry and exit side");
        }
        
        //gets random entry and exit intersections
        List<Intersection> entryInteresections = grid.getEdgeIntersections().get(entrySide);
        Intersection entryIntersection = entryInteresections.get(random.nextInt(entryInteresections.size()));
        List<Intersection> exitInteresections = grid.getEdgeIntersections().get(exitSide);
        Intersection exitIntersection = exitInteresections.get(random.nextInt(exitInteresections.size()));
        
        int entryNSBlock = entryIntersection.getNSBlock();
        int entryEWBlock = entryIntersection.getEWBlock();
        int exitNSBlock = exitIntersection.getNSBlock();
        int exitEWBlock = exitIntersection.getEWBlock();
        
        //The sides are opposite
        if(Math.abs(entrySide - exitSide) % 2 == 0){
            
            //There are no turns
            if(entryNSBlock == exitNSBlock || entryEWBlock == exitEWBlock){
                return new Route(Arrays.asList(entryIntersection, exitIntersection));
                
            //There are two turns
            } else {
                
                //The route is mainly N->S or S->N
                if(entryNSBlock == 0 || entryNSBlock == grid.getNSBlockSize()-1){
                    int xBlock = random.nextInt(grid.getNSBlockSize()-3)+1;
                    Intersection x1 = grid.getIntersection(xBlock, entryEWBlock);
                    Intersection x2 = grid.getIntersection(xBlock, exitEWBlock);
                    return new Route(Arrays.asList(entryIntersection, x1, x2, exitIntersection));
                    
                //The route is mainly E->W or W->E
                } else if(entryEWBlock == 0 || entryEWBlock == grid.getEWBlockSize()-1){
                    int xBlock = random.nextInt(grid.getEWBlockSize()-3)+1;
                    Intersection x1 = grid.getIntersection(entryNSBlock, xBlock);
                    Intersection x2 = grid.getIntersection(exitNSBlock, xBlock);
                    return new Route(Arrays.asList(entryIntersection, x1, x2, exitIntersection));
                    
                } else {
                    throw new IllegalStateException("The main direction could not be determined"); //This should not happen
                }
            }
            
        //The sides are next to each other
        } else {
            
            //The route starts heading North or South bound
            if(entryNSBlock == 0 || entryNSBlock == grid.getNSBlockSize()-1){
                Intersection x = grid.getIntersection(entryNSBlock, exitEWBlock);
                return new Route(Arrays.asList(entryIntersection, x, exitIntersection));
            
            //The route starts heading East or West bound
            } else if(entryEWBlock == 0 || entryEWBlock == grid.getEWBlockSize()-1){
                Intersection x = grid.getIntersection(exitNSBlock, entryEWBlock);
                return new Route(Arrays.asList(entryIntersection, x, exitIntersection));
                
            }else {
                throw new IllegalStateException("The main direction could not be determined"); //This should not happen
            }
        }
    }
}
