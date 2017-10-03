/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package event;

import java.math.BigInteger;
import java.util.List;
import model.Car;
import model.CardinalDirection;
import model.Grid;
import model.Intersection;

/**
 *
 * @author Blake
 */
public class CarEntryEvent extends Event{
    
    private final Car car;

    public CarEntryEvent(BigInteger eventTime, Car car) {
        super(eventTime);
        this.car = car;
    }

    @Override
    public void resolveEvent() {
        System.out.println(car + " has entered at: " + eventTime);
        List<Intersection> intersections = car.getRoute().getIntersections();
        BigInteger moveTime = eventTime.add(BigInteger.valueOf(Grid.INTERSECTION_DISATANCE / car.velocity));
        EventBus.submitEvent(new CarMoveEvent(moveTime, car, intersections.get(0), intersections.get(1)));
        EventBus.world.addCar(car, intersections.get(0), CardinalDirection.NORTH);
    }
}
