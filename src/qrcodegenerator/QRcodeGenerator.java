/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package qrcodegenerator;

import java.util.Arrays;

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
        String hello = "http://geo.arisoft.fi";
        Binary length = new Binary(hello.length(), 8);
		
		String web_end = "/GC366ZB/3-3-2-3-5";
		Binary mode2 = new Binary(0b0010, 4);
		Binary length2 = new Binary(web_end.length(), 9);
		Binary web = AlphaNumeric.getBinary(web_end);

		String png = ".png";
		Binary length3 = new Binary(png.length(), 8);
        
        Binary hello_b;
		Binary png_b;
                Binary mode_end = new Binary(0, 4);
                Binary fill = new Binary(0b1110110000010001, 16);
        try {
            hello_b = new Binary(hello.getBytes("ISO-8859-1"),8*hello.length());
			png_b =  new Binary(png.getBytes("ISO-8859-1"),8*png.length());
        } catch (Exception e) {
            return;
        }
        Binary comb2 = Binary.combine(Arrays.asList(mode, length, hello_b,
													mode2// , length2, web,
													// mode, length3, png_b,
													// mode_end
													));
        // Binary padded = new Binary(comb2.bits(), ((comb2.length()-1)/8)*8+8);
        // comb2 = Binary.combine(Arrays.asList(padded, fill, fill, fill, 
		// 									 fill, fill, fill, fill, 
		// 									 fill, fill, fill, fill, 
		// 									 fill, fill, fill, fill));
        // comb2 = comb2.cut(55*8);
        // comb2 = padded;
        
        System.out.println(comb2);
        code.add_data(comb2);
        System.out.println(code);
        code.toFile("test.png");
    }
    
}
