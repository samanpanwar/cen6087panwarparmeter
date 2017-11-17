/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.smartcity.model;

import java.util.LinkedList;

/**
 *
 * @author Blake
 */
public class ConvoyQueue {
    
    private static LinkedList<Convoy> convoyQueue = new LinkedList();
    
    public static void addToQueue(Convoy convoy){
        convoyQueue.addLast(convoy);
    }
    
    public static Convoy pollQueue(){
        return convoyQueue.poll();
    }
}
