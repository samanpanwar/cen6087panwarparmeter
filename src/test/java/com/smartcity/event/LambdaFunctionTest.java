/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.smartcity.event;

import org.junit.Test;

/**
 *
 * @author Blake
 */
public class LambdaFunctionTest {
    
    private static final double NUM_LAMBDAS = 10;
    private static final double NUM_TESTS = 10_000;
    private static final double SLOPE = 2;
    private static final double OFFSET = -20;
    
    boolean debug = true;
    
    @Test 
    public void lambdaTest(){
        CarGenerateEvent e = new CarGenerateEvent(0);
        for(int i = 0; i < NUM_LAMBDAS; i ++){
            double lambda = SLOPE * i + OFFSET;
            double total = 0;
            double max = 0;
            for(int j = 0; j < NUM_TESTS; j ++){
                double val = e.getNextGenerateTime(lambda);
                if(val == Double.POSITIVE_INFINITY){
                    System.out.println("Inifinty!");
                    continue;
                }
                total += val;
                //System.out.println("lambda: " + lambda + "\tproduced:" + val);
                if(val > max){
                    max = val;
                }
            }
            System.out.println("lambda: " + lambda + "\tproduced avg time of:" + total / NUM_TESTS + " and max of:" + max);
        }
    }
}
