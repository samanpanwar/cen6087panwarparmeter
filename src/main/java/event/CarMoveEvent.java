/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package event;

import java.math.BigInteger;

/**
 *
 * @author Blake
 */
public class CarMoveEvent extends Event {

    public CarMoveEvent(BigInteger eventTime) {
        super(eventTime);
    }

    @Override
    public void resolveEvent() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }
}
