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
    
    private final boolean debug = false;
    private final Grid grid = new Grid(100, 500);
    private final RouteFactory factory = new RouteFactory(grid, 0L);
    
    @Test
    public void basicRouteFactoryTest() {
        
        for(int i = 0; i < 100_000; i++){
            testGenerateRoute(factory.generateRoute(), i);
        }
    }
    
    private void testGenerateRoute(Route route, int testNum){
        
        //prints out the route information
        if(debug){
            System.out.println("\nTest no: " + (testNum+1) + " Route Size: " + route.getIntersections().size());
            for(int i=0; i < route.getIntersections().size(); i ++){
                System.out.println("Intersection:" + i + ":" + route.getIntersections().get(i));
            }
        }
        
        //Ensures the first and last intersections are edges
        if(grid.isEdge(route.getIntersections().get(0)) == false){
            Assert.fail("The first intersection is not an edge intersection");
        }
        
        //Ensures the first and last intersections are edges
        if(grid.isEdge(route.getIntersections().get(route.getIntersections().size()-1)) == false){
            Assert.fail("The last intersection is not an edge intersection");
        }
        
        //ensures the route is with in the grid and the same intersection does not repeat
        Intersection lastIntersection = null;
        for(Intersection intersection : route.getIntersections()){
            if(intersection.getNSBlock() < 0 || intersection.getNSBlock() > grid.getNSBlockSize()){
                Assert.fail("intersection out of range");
            }
            if(intersection.getEWBlock() < 0 || intersection.getEWBlock() > grid.getEWBlockSize()){
                Assert.fail("intersection out of range");
            }
            if(lastIntersection != null){
                if((intersection.getNSBlock() == lastIntersection.getNSBlock() || 
                        intersection.getEWBlock() == lastIntersection.getEWBlock()) == false){
                    
                    Assert.fail("The intersections are not orthagional");
                }
            }
            if(intersection == lastIntersection){
                Assert.fail("The same intersection repeats");
            }
            lastIntersection = intersection;
        }
    }
}
