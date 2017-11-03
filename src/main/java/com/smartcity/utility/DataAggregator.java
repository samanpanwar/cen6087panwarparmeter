/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.smartcity.utility;

import com.google.common.collect.TreeMultiset;
import com.smartcity.application.Simulation;
import com.smartcity.event.Event;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicLong;
import javafx.application.Platform;
import javafx.scene.Scene;
import javafx.scene.chart.BarChart;
import javafx.scene.chart.CategoryAxis;
import javafx.scene.chart.NumberAxis;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

/**
 *
 * @author Blake
 */
public class DataAggregator {
    
    private static final AtomicLong numCars = new AtomicLong(0);
    private static final AtomicLong numCarsAdded = new AtomicLong(0);
    private static final AtomicLong numCarsRemoved = new AtomicLong(0);
    private static final AtomicLong numEvents = new AtomicLong(0);
    private static final Map<Class, List<Long>> eventTimesMap = new HashMap(); 
    private static final TreeMultiset<Double> carAverages = TreeMultiset.create();
    private static final long updateInterval = 250;//ms
    
    private static long lastUpdateTime = System.currentTimeMillis();
    
    public static void putEventEntry(Event event, long nanos){
        if(Simulation.GATHER_DATA){
//        numEvents.incrementAndGet();
//        List<Long> eventTimes = eventTimesMap.get(event.getClass());
//        if(eventTimes == null){
//            eventTimes = new ArrayList();
//            eventTimesMap.put(event.getClass(), eventTimes);
//        }
//        eventTimes.add(nanos);
        }
    }
    
    public static void addCar(){
        numCars.incrementAndGet();
        numCarsAdded.incrementAndGet();
        checkForPrintStatus();
    }
    
    public static void removeCar(){
        numCars.decrementAndGet();
        numCarsRemoved.incrementAndGet();
        checkForPrintStatus();
    }
    
    public static long getNumCars(){
        return numCars.longValue();
    }
    
    public static void putCarAverage(double average){
        if(Simulation.GATHER_DATA){
            carAverages.add(average);
        }
    }
    
    public static void generateCarAverageChart(){
        
        if(carAverages.isEmpty()){
            System.out.println("There is no data to plot");
            return;
        }
        
        //caculates the stats
        long start = System.currentTimeMillis();
        System.out.println("calculating stats...");
        double min = carAverages.firstEntry().getElement();
        double max = carAverages.lastEntry().getElement();
        double total = 0;
        for(double avg : carAverages){
            total += avg;
            if(total == Double.MAX_VALUE){
                throw new IllegalStateException("The average cannot be calculated, double overflow");
            }
        }
        double avg = total / carAverages.size();
        System.out.println("Stats calculation took " + (System.currentTimeMillis() - start) + "ms. Average: " + avg);
        
        //Builds the stats pane
        HBox statsPane = new HBox();
        statsPane.setSpacing(10);
        statsPane.getChildren().addAll(
                new Label("NumCars:" + carAverages.size()), 
                new Label("Mean:" + avg), 
                new Label("Min:" + min), 
                new Label("Max:" + max),
                new Label("Range:" + (max-min)));
        
        //Builds the copy data area
        TextField copyDataArea = new TextField(carAverages.size() + "," + avg + "," + min + "," + max + "," + (max-min));
        copyDataArea.setEditable(false);
        
        //builds the chart
        final CategoryAxis xAxis = new CategoryAxis();
        final NumberAxis yAxis = new NumberAxis();
        final BarChart<String,Number> bc = new BarChart(xAxis,yAxis);
        bc.setTitle("Car Average Speed");
        xAxis.setLabel("Time (In Time Units)");       
        yAxis.setLabel("Number of Cars");
        
        //Lays out the components
        VBox mainBox = new VBox();
        mainBox.getChildren().addAll(bc, statsPane, copyDataArea);
        
        //opens the chart in a new window
        Scene scene = new Scene(mainBox, 600, 400);
        Platform.runLater(()->{
            Stage stage = new Stage();
            stage.setTitle("New Window");
            stage.setScene(scene);
            stage.show();
        });
    }
    
    public static void generateEventTimesAveragesChart(){
        
    }
    
    private static void checkForPrintStatus(){
        if(System.currentTimeMillis() - lastUpdateTime > updateInterval){
            System.out.println(numCarsAdded.get() + " cars added\t" + numCarsRemoved.get() + " cars removed\t" + numCars + " cars in the system.");
            lastUpdateTime = System.currentTimeMillis();
        }
    }
}
