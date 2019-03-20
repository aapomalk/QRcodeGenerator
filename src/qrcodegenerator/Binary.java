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
public class Binary {
    private final byte[] bits;
    private final int length;
    
    public Binary(byte[] bits, int length) {
        this.length = length;
        this.bits = bits;
    }
    
    public Binary(int bits, int length) {
        this.length = length;
        this.bits = new byte[length/8+1];
        for (int i=0; i<length; i++) {
            if ((bits & (1 << length-1-i)) > 0) {
                this.bits[i/8] += 1 << (7-(i%8));
            }
        }
    }
    
    public Binary combine(Binary first, Binary second) {
        byte[] f = first.bits();
        byte[] s = second.bits();
        int l = first.length()+second.length();
        byte[] t = new byte[l/8+1];
        
        for (int i=0; i<l; i++) {
            if (i<first.length()) {
                if ((f[i/8] & (1 << 7-(i%8))) > 0) {
                    t[i/8] += (1 << 7-(i%8));
                }
            } else {
                int j = i - first.length();
                if ((s[j/8] & (1 << 7-(j%8))) > 0) {
                    t[i/8] += (1 << 7-(i%8));
                }
            }
        }
        
        return new Binary(t, l);
    }
    
    public byte[] bits() {
        return bits;
    }
    
    public int length() {
        return length;
    }
    
    public String toString() {
        String ret = "";
        for (byte bit : bits) {
            ret += Integer.toBinaryString((bit & 0xFF) + 0x100).substring(1);
        }
        return ret;
    }
}
