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

    @Override
    public int compare(Event o1, Event o2) {
        int timeDelta = o1.eventTime.compareTo(o2.eventTime);
        if(timeDelta != 0){
            return timeDelta;
        } else {
            return o1.hashCode() - o2.hashCode();
        }
    }
}
