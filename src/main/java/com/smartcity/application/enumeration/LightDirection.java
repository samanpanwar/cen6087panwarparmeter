/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.smartcity.application.enumeration;

/**
 *
 * @author Blake
 */
public enum LightDirection {
    
    NS_BOUND, EW_BOUND;
    
    public LightDirection getOppisite(){
        switch(this){
            case EW_BOUND:
                return NS_BOUND;
                
            case NS_BOUND:
                return EW_BOUND;
                
            default:
                throw new IllegalArgumentException(this + " is not handled");
        }
    }
}
