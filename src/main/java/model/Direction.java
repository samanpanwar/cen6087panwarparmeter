/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package model;

/**
 *
 * @author Blake
 */
public enum Direction {
    
    NORTH(0), SOUTH(1), EAST(2), WEST(3);
    
    public final int index;

    private Direction(int index){
        this.index = index;
    }
}
