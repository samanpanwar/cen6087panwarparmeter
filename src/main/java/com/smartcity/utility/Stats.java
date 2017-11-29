/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.smartcity.utility;

import com.google.common.collect.BoundType;
import com.google.common.collect.TreeMultiset;
import com.smartcity.application.Simulation;
import java.text.DecimalFormat;
import java.util.ArrayList;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.chart.XYChart;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.HBox;

/**
 *
 * @author Blake
 */
public class Stats {

    private static final DecimalFormat nf = new DecimalFormat("0.###");
    public final TreeMultiset<Double> data;
    public final double min, max, total, avg;

    public Stats(TreeMultiset<Double> data, String name){

        if(data.isEmpty()){
            throw new IllegalArgumentException("There is no data to plot");
        }

        //caculates the stats
        min = data.firstEntry().getElement();
        max = data.lastEntry().getElement();
        double totalCalc = 0;
        for(double anAvg : data){
            totalCalc += anAvg;
            if(totalCalc == Double.MAX_VALUE){
                throw new IllegalStateException("The average cannot be calculated, double overflow");
            }
        }
        total = totalCalc;
        avg = total / data.size();
        System.out.println(name + " Average:" + avg);
        this.data = data;
    }

    public Node getStatsPane(){

        //Builds the copy data area
        TextField copyDataArea = new TextField(data.size() + "," + avg + "," + min + "," + max + "," + (max-min));
        System.out.println("Simulation Data:\n"+copyDataArea.getText());
        copyDataArea.setEditable(false);

        //Builds the stats pane
        HBox statsPane = new HBox();
        statsPane.setSpacing(10);
        statsPane.setAlignment(Pos.TOP_CENTER);
        statsPane.getChildren().addAll(
                new Label("NumCars: " + nf.format(data.size())), 
                new Label("Mean: " + nf.format(avg)), 
                new Label("Min: " + nf.format(min)), 
                new Label("Max: " + nf.format(max)),
                new Label("Range: " + nf.format(max-min)),
                copyDataArea);

        return statsPane;
    }

    //Get data for the chart
    public ObservableList<XYChart.Data> getChartData(double max){
        double bucketRange = max / Simulation.CHART_BUCKETS;
        double bucketMin = 0;
        ObservableList<XYChart.Data> chartData = FXCollections.observableList(new ArrayList());
        for(int i=0; i < Simulation.CHART_BUCKETS; i++){
            double bucketMax = bucketMin + bucketRange;
            int numRecords = data.subMultiset(bucketMin, BoundType.OPEN, bucketMax, BoundType.CLOSED).size();
            double middle = bucketMin + ((bucketMax - bucketMin) / 2);
            chartData.add(new XYChart.Data(middle, numRecords));
            bucketMin = bucketMax;
        }
        return chartData;
    }
}
