/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.smartcity.utility;

import com.google.common.collect.BoundType;
import com.google.common.collect.TreeMultiset;
import com.smartcity.application.Simulation;
import com.smartcity.event.Event;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.atomic.AtomicLong;
import javafx.application.Platform;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.EventHandler;
import javafx.event.EventType;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.chart.LineChart;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.XYChart;
import javafx.scene.control.CheckBox;
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
    private static final Map<Class, TreeMultiset<Long>> eventTimesMap = new HashMap(); 
    private static final TreeMultiset<Double> carVelocityAverages = TreeMultiset.create();
    private static final TreeMultiset<Double> carSimulationTimes = TreeMultiset.create();
    private static final TreeMultiset<Double> carWaitTimes = TreeMultiset.create();
    private static final long updateInterval = 250;//ms
    private static final DecimalFormat lambdaFormatter = new DecimalFormat("0.###########");
    
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
    
    public static long getNumCarsActive(){
        return numCars.longValue();
    }
    
    public static long getNumCarsAdded(){
        return numCarsAdded.longValue();
    }
    
    public static long getNumCarsRemoved(){
        return numCarsRemoved.longValue();
    }
    
    public static void putCarVelocityAverage(double average){
        if(Simulation.GATHER_DATA){
            carVelocityAverages.add(average);
        }
    }
    
    public static void putCarSimulationTime(double time){
        if(Simulation.GATHER_DATA){
            carSimulationTimes.add(time);
        }
    }
    
    public static void putCarTotalWaitTime(double time){
        if(Simulation.GATHER_DATA){
            carWaitTimes.add(time);
        }
    }
    
    public static void generateCarAverageChart(){
        
        Stats velocityAverages = new Stats(carVelocityAverages, "Velocity Averages");
        Stats simulationTimes = new Stats(carSimulationTimes, "Simulation Times");
        Stats waitTimes = new Stats(carWaitTimes, "Wait Times");
        
        //builds the chart
        final NumberAxis xAxis = new NumberAxis();
        final NumberAxis yAxis = new NumberAxis();
        final LineChart<String,Number> lc = new LineChart(xAxis,yAxis);
        XYChart.Series velocitySeries = new XYChart.Series("Average Velocity (carLengths/s)", velocityAverages.getChartData(Simulation.CAR_VELOCITY));
        XYChart.Series simulationTimeSeries = new XYChart.Series("Time in Simulation (s)", simulationTimes.getChartData(simulationTimes.max));
        XYChart.Series waitTimeSeries = new XYChart.Series("Wait Times (s)", waitTimes.getChartData(waitTimes.max));
        lc.getData().addAll(simulationTimeSeries, waitTimeSeries);
        lc.setTitle("Lambda: " + lambdaFormatter.format(Simulation.NUM_CARS_LAMBDA) + "\nLight Change Type: " + Simulation.LIGHT_CHANGE_TYPE);
        lc.setCreateSymbols(false);
        xAxis.setLabel("");       
        yAxis.setLabel("Number of Cars");
        
        //adds the buttons
        CheckBox velBox = new CheckBox("Average Velocity");
        velBox.setSelected(false);
        velBox.selectedProperty().addListener(new ToggleEvent(velocitySeries, lc.getData()));
        CheckBox timeInSimBox = new CheckBox("Time in Simulation");
        timeInSimBox.setSelected(true);
        timeInSimBox.selectedProperty().addListener(new ToggleEvent(simulationTimeSeries, lc.getData()));
        CheckBox waitTimeBox = new CheckBox("Wait Times");
        waitTimeBox.setSelected(true);
        waitTimeBox.selectedProperty().addListener(new ToggleEvent(waitTimeSeries, lc.getData()));
        HBox checkBoxes = new HBox();
        checkBoxes.setAlignment(Pos.CENTER);
        checkBoxes.setSpacing(10);
        checkBoxes.getChildren().setAll(velBox, timeInSimBox, waitTimeBox);
        
        //Lays out the components
        VBox mainBox = new VBox();
        mainBox.setSpacing(5);
        mainBox.getChildren().addAll(lc, checkBoxes,
                velocityAverages.getStatsPane(),
                simulationTimes.getStatsPane(),
                waitTimes.getStatsPane());
        
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
    
    private static class ToggleEvent implements ChangeListener<Boolean>{

        private final Object obj;
        private final ObservableList list;
        
        public ToggleEvent(Object obj, ObservableList list){
            this.obj = obj;
            this.list = list;
        }
        
        @Override
        public void changed(ObservableValue<? extends Boolean> observable, Boolean oldValue, Boolean newValue) {
            if(newValue && !list.contains(obj)){
                list.add(obj);
            } else {
                list.remove(obj);
            }
        }
    }
}
