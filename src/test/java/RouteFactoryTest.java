/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

import com.smartcity.utility.VectorUtility;
import com.smartcity.gui.World;
import com.smartcity.factory.RouteFactory;
import java.util.List;
import javafx.application.Platform;
import javafx.embed.swing.JFXPanel;
import javafx.scene.Scene;
import javafx.scene.control.ScrollPane;
import javafx.stage.Stage;
import com.smartcity.model.CardinalDirection;
import com.smartcity.model.Grid;
import com.smartcity.model.Intersection;
import com.smartcity.model.Route;
import org.junit.Assert;
import org.junit.Test;

/**
 *
 * @author Blake
 */
public class RouteFactoryTest {
    
    private final int NUM_TEST_ROUTES = 100_000;
    private final boolean debug = false;
    private final Grid grid = new Grid(12, 8);
    private final RouteFactory factory = new RouteFactory(grid, 0L);
    private Stage stage;
    
    @Test 
    public void gridTest(){
        for(int i = 0; i < grid.getEWBlockSize(); i++){
            CardinalDirection direction = grid.getIntersection(i, 0).getNSDirection();
            for(int j = 0; j < grid.getNSBlockSize(); j++){
                Assert.assertEquals(grid.getIntersection(i, j).getNSDirection(),direction);
            }
        }
        for(int i = 0; i < grid.getNSBlockSize(); i++){
            CardinalDirection direction = grid.getIntersection(0, i).getEWDirection();
            for(int j = 0; j < grid.getEWBlockSize(); j++){
                Assert.assertEquals(grid.getIntersection(j, i).getEWDirection(),direction);
            }
        }
    }
    
    @Test
    public void basicRouteFactoryTest() {
        if(debug){
            new JFXPanel(); // initializes JavaFX environment
            Platform.runLater(()->{
                stage = new Stage();
                stage.show();
            });
        }
        for(int i = 0; i < NUM_TEST_ROUTES; i++){
            testGenerateRoute(factory.generateRoute(), i);
        }
        System.out.println(NUM_TEST_ROUTES + " Routes tested");
    }
    
    private void testGenerateRoute(Route route, int testNum){
        
        //prints out the route information
        if(debug){
            System.out.println("\nTest no: " + (testNum+1) + " Route Size: " + route.getIntersections().size());
            for(int i=0; i < route.getIntersections().size(); i ++){
                System.out.println("Intersection:" + i + "\t" + route.getIntersections().get(i));
            }
            Platform.runLater(()->{
                World world = new World(grid);
                world.drawRoute(route);
                world.drawEntriesAndExits();
                stage.setScene(new Scene(new ScrollPane(world.getRoot())));
            });
            
            //Need to sleep, if it's too fast it will crash the system with too many render calls. 
            try{
                Thread.sleep(50);
            }catch(Exception ex){
                
            }
        }
        
        //Ensures the first intersection is an entrance
        boolean foundEntry = false;
        for(List<Intersection> entrances : grid.getEntryIntersections()){
            if(entrances.contains(route.getIntersections().get(0))){
                foundEntry = true;
            }
        }
        Assert.assertTrue("The first intersection is not an entrance", foundEntry);
        
        //Ensures the last intersection is an exit
        boolean foundExit = false;
        for(List<Intersection> exits : grid.getExitIntersections()){
            if(exits.contains(route.getIntersections().get(route.getIntersections().size()-1))){
                foundExit = true;
            }
        }
        Assert.assertTrue("The last intersection is not an exit", foundExit);
        
        //Tests to ensure that the directions are legal
        Intersection lastIntersection = null;
        for(Intersection intersection : route.getIntersections()){
            int NSBlock = intersection.getNSBlock();
            int EWBlock = intersection.getEWBlock();
            
            //Ensures the intersections are within the grid
            if(NSBlock < 0 || NSBlock > grid.getNSBlockSize()){
                Assert.fail("intersection out of range");
            }
            if(EWBlock < 0 || EWBlock > grid.getEWBlockSize()){
                Assert.fail("intersection out of range");
            }
            
            //Tests which involve neighboring intersections
            if(lastIntersection != null){
                int lastNSBlock = lastIntersection.getNSBlock();
                int lastEWBlock = lastIntersection.getEWBlock();
                
                if(intersection == lastIntersection){
                    Assert.fail("The same intersection repeats");
                }
                
                if((NSBlock == lastNSBlock || EWBlock == lastEWBlock) == false){
                    Assert.fail("The intersections are not orthogional");
                }
                
                if(Math.abs(NSBlock-lastNSBlock) > 1 || Math.abs(EWBlock-lastEWBlock) > 1){
                    Assert.fail("The blocks are further than one intersection apart");
                }
                
                //Ensures the directions respect the intersection's directions
                CardinalDirection direction = VectorUtility.getDirectionTo(lastIntersection, intersection);
                if(direction.equals(CardinalDirection.NORTH) || direction.equals(CardinalDirection.SOUTH)){
                    Assert.assertEquals(intersection.getNSDirection(), direction);
                } else {
                    Assert.assertEquals(intersection.getEWDirection(), direction);
                }
            }
            lastIntersection = intersection;
        }
    }
}
