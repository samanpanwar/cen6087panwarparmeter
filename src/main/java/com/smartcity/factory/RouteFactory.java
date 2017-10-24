/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.smartcity.factory;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Random;
import com.smartcity.model.Grid;
import com.smartcity.model.Intersection;
import com.smartcity.model.Route;

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
        List<Intersection> entryInteresections = grid.getEntryIntersections().get(entrySide);
        Intersection entryIntersection = entryInteresections.get(random.nextInt(entryInteresections.size()));
        List<Intersection> exitInteresections = grid.getExitIntersections().get(exitSide);
        Intersection exitIntersection = exitInteresections.get(random.nextInt(exitInteresections.size()));
        
        entryIntersection = new Intersection(3, 0);
        int xEntry = entryIntersection.getEWBlock();
        int yEntry = entryIntersection.getNSBlock();
        int xExit = exitIntersection.getEWBlock();
        int yExit = exitIntersection.getNSBlock();
        
        //The sides are opposite
        if(Math.abs(entrySide - exitSide) % 2 == 0){
            
            //There are no turns
            if(yEntry == yExit || xEntry == xExit){
                return new Route(connectIntersections(entryIntersection, exitIntersection));
                
            //There are two turns
            } else {
                
                //The route is mainly N->S or S->N
                if(yEntry == 0 || yEntry == grid.getNSBlockSize()-1){
                    int crossBlock;
                    if(xEntry > xExit){
                        crossBlock = grid.getWBStreets().get(random.nextInt(grid.getWBStreets().size()-2)+1);
                    } else {
                        crossBlock = grid.getEBStreets().get(random.nextInt(grid.getEBStreets().size()-2)+1);
                    }
                    Intersection x1 = grid.getIntersection(xEntry, crossBlock);
                    Intersection x2 = grid.getIntersection(xExit, crossBlock);
                    return new Route(connectIntersections(entryIntersection, x1, x2, exitIntersection));
                    
                //The route is mainly E->W or W->E
                } else if(xEntry == 0 || xEntry == grid.getEWBlockSize()-1){
                    int crossBlock;
                    if(yEntry > yExit){
                        crossBlock = grid.getNBStreets().get(random.nextInt(grid.getNBStreets().size()-2)+1);
                    } else {
                        crossBlock = grid.getSBStreets().get(random.nextInt(grid.getSBStreets().size()-2)+1);
                    }
                    Intersection x1 = grid.getIntersection(crossBlock, yEntry);
                    Intersection x2 = grid.getIntersection(crossBlock, yExit);
                    return new Route(connectIntersections(entryIntersection, x1, x2, exitIntersection));
                    
                } else {
                    throw new IllegalStateException("The main direction could not be determined"); //This should not happen
                }
            }
            
        //The sides are next to each other
        } else {
            
            //The route starts heading North or South bound
            if(yEntry == 0 || yEntry == grid.getNSBlockSize()-1){
                Intersection x = grid.getIntersection(xEntry, yExit);
                return new Route(connectIntersections(entryIntersection, x, exitIntersection));
            
            //The route starts heading East or West bound
            } else if(xEntry == 0 || xEntry == grid.getEWBlockSize()-1){
                Intersection x = grid.getIntersection(xExit, yEntry);
                return new Route(connectIntersections(entryIntersection, x, exitIntersection));
                
            }else {
                throw new IllegalStateException("The main direction could not be determined"); //This should not happen
            }
        }
    }
    
    private List<Intersection> connectIntersections(Intersection... intersections){
        List<Intersection> resultIntersections = new ArrayList();
        Intersection lastIntersection = null;
        for(Intersection intersection : intersections){
            if(lastIntersection != null){
                
                //Restricts the 
                if(resultIntersections.size() > 0){
                    List<Intersection> connectionIntersections = getConnectingIntersections(lastIntersection, intersection);
                    resultIntersections.addAll(connectionIntersections.subList(1, connectionIntersections.size()));
                } else {
                    resultIntersections.addAll(getConnectingIntersections(lastIntersection, intersection));
                }
            }
            lastIntersection = intersection;
        }
        return resultIntersections;
    }
    
    private List<Intersection> getConnectingIntersections(Intersection entryIntersection, Intersection exitIntersection){

        List<Intersection> intersections = new ArrayList();
        int yEntry = entryIntersection.getNSBlock();
        int xEntry = entryIntersection.getEWBlock();
        int yExit = exitIntersection.getNSBlock();
        int xExit = exitIntersection.getEWBlock();
        
        //east / west
        if(xEntry == xExit){
            
            //Northbound
            if(yEntry < yExit){
                for(int i = yEntry; i <= yExit; i++){
                    intersections.add(grid.getIntersection(xEntry, i));
                }
                
            //Southbound
            } else {
                for(int i = yEntry; i >= yExit; i--){
                    intersections.add(grid.getIntersection(xEntry, i));
                }
            }
            
        //north / south
        } else if(yEntry == yExit){
            
            //Eastbound
            if(xEntry < xExit){
                for(int i = xEntry; i <= xExit; i++){
                    intersections.add(grid.getIntersection(i, yEntry));
                }
                
            //Westbound
            } else {
                for(int i = xEntry; i >= xExit; i--){
                    intersections.add(grid.getIntersection(i, yEntry));
                }
            }
        } else { 
            throw new IllegalArgumentException("The intersections were not ortogonal.\n\t"+entryIntersection+"\n\t"+exitIntersection);
        }
        return intersections;
    }
}
