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
public enum AlphaNumeric {
    AN_0("0", 0),
	AN_1("1", 1),
	AN_2("2", 2),
	AN_3("3", 3),
	AN_4("4", 4),
	AN_5("5", 5),
	AN_6("6", 6),
	AN_7("7", 7),
	AN_8("8", 8),
	AN_9("9", 9),
	AN_A("A", 10),
	AN_B("B", 11),
	AN_C("C", 12),
	AN_D("D", 13),
	AN_E("E", 14),
	AN_F("F", 15),
	AN_G("G", 16),
	AN_H("H", 17),
	AN_I("I", 18),
	AN_J("J", 19),
	AN_K("K", 20),
	AN_L("L", 21),
	AN_M("M", 22),
	AN_N("N", 23),
	AN_O("O", 24),
	AN_P("P", 25),
	AN_Q("Q", 26),
	AN_R("R", 27),
	AN_S("S", 28),
	AN_T("T", 29),
	AN_U("U", 30),
	AN_V("V", 31),
	AN_W("W", 32),
	AN_X("X", 33),
	AN_Y("Y", 34),
	AN_Z("Z", 35),
	AN_space(" ", 36),
	AN_$("$", 37),
	AN_x1("%", 38),
	AN_x2("*", 39),
	AN_x3("+", 40),
	AN_x4("-", 41),
	AN_x5(".", 42),
	AN_x6("/", 43),
	AN_x7(":", 44);

	private final String c;
	private final int num;

	private AlphaNumeric(String c, int num){
		this.c = c;
		this.num = num;
	}

	public String getChar() {
		return this.c;
	}

	public int getNum() {
		return this.num;
	}

	public static AlphaNumeric get(String c) {
		for (AlphaNumeric an : AlphaNumeric.values()) {
			if (an.getChar().equals(c)) {
				return an;
			}
		}
		return null;
	}

	public static int getNum(String c) {
		AlphaNumeric an = get(c);
		if (an == null) {
			return -1;
		}
		return an.getNum();
	}

	public static Binary getBinary(String text) {
		Binary ret = new Binary(0, 0);
		for (int i=0; i<text.length(); i += 2) {
			int num = 0;
			if (i < text.length()-1) {
				num = 45 * getNum(text.charAt(i)+"");
				if (num < 0) return null;
				int temp = getNum(text.charAt(i+1)+"");
				if  (temp < 0) return null;
				num += temp;
			} else {
				num = getNum(text.charAt(i)+"");
				if (num < 0) return null;
			}
			Binary add = new Binary(num, 11);
			ret = ret.combine(ret, add);
		}
		return ret;
	}
}
