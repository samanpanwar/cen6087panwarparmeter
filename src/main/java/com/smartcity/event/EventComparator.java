/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.smartcity.event;

import java.util.Comparator;

/**
 *
 * @author Blake
 */
public class EventComparator implements Comparator<Event>{

    private static double MULTILLER = 100_000;
    
    @Override
    public int compare(Event o1, Event o2) {
        return (int) ((o1.eventTime - o2.eventTime) * MULTILLER);
    }
}
