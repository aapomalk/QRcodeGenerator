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
public class QRcodeGenerator {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        // TODO code application logic here
        QRcode code = new QRcode(3, ErrorLevel.L, 0);
        Binary mode = new Binary(0b0100, 4);
        String hello = "hallo aapo";
        Binary length = new Binary(hello.length(), 11);
        Binary comb1 = mode.combine(mode, length);
        Binary hello_b = new Binary(hello.getBytes(), 8*hello.length());
        Binary comb2 = mode.combine(comb1, hello_b);
        code.add_data(comb2.bits());
        System.out.println(code);
    }
    
}
