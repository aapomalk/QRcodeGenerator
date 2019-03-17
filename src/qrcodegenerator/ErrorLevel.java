/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package qrcodegenerator;

/**
 *
 * @author aapom
 */
public enum ErrorLevel {
    L(0b01),M(0b00),Q(0b11),H(0b10);
    
    private int bits;
    
    ErrorLevel(int bits) {
        this.bits = bits;
    }
    
    public int bits() {
        return bits;
    }
}
