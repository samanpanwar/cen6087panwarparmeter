/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

import factory.RouteFactory;
import model.Grid;
import model.Intersection;
import model.Route;
import org.junit.Assert;
import org.junit.Test;

/**
 *
 * @author Blake
 */
public class RouteFactoryTest {
    
    @Test
    public void basicRouteFactoryTest() {
        
        //initalizes the intersection
        Grid grid = new Grid(10, 10);
        RouteFactory factory = new RouteFactory(grid, 1L);
        Route route = factory.generateRoute();
        
        //Ensures the first and last intersections are edges
        if(grid.getEdgeIntersections().contains(route.getIntersections().get(0)) == false){
            Assert.fail("The first intersection is not an edge intersection");
        }
        
        //Ensures the first and last intersections are edges
        if(grid.getEdgeIntersections().contains(route.getIntersections().get(route.getIntersections().size()-1)) == false){
            Assert.fail("The last intersection is not an edge intersection");
        }
        
        //ensures the route is with in the grid and the same intersection does not repeat
        Intersection lastIntersection = null;
        for(Intersection intersection : route.getIntersections()){
            System.out.println("Testing intersection: " + intersection);
            if(intersection.getNSBlock() < 0 || intersection.getNSBlock() > grid.getNSBlockSize()){
                Assert.fail("intersection out of range");
            }
            if(intersection.getEWBlock() < 0 || intersection.getEWBlock() > grid.getEWBlockSize()){
                Assert.fail("intersection out of range");
            }
            if(intersection == lastIntersection){
                Assert.fail("The same intersection repeats");
            }
            lastIntersection = intersection;
        }
    }
}
