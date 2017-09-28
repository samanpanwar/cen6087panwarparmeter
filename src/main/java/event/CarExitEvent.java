/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package event;

import java.math.BigInteger;
import model.Car;

/**
 *
 * @author Blake
 */
public class CarExitEvent extends Event{

    private final Car car;
    
    public CarExitEvent(BigInteger eventTime, Car car) {
        super(eventTime);
        this.car = car;
    }

    @Override
    public void resolveEvent() {
        System.out.println(car + " has exited at: " + eventTime);
    }
}
