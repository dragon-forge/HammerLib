package org.zeith.hammerlib.util.java;

@SuppressWarnings("ALL")
public interface Chars
{
	//<editor-fold desc="Generic handy unicode characters">
	char DEGREE_SIGN = '\u00b0';
	char RHO = '\u03c1';
	char SQRT = '\u221A';
	char PI = '\u1D28';
	char PSI = '\u1D2A';
	char SECTION_SIGN = '\u00A7';
	char PLUS_MINUS_SIGN = '\u00B1';
	char MICRO_SIGN = '\u00B5';
	char MULTIPLICATION_SIGN = '\u00D7';
	char DIVISION_SIGN = '\u00F7';
	char BULLET = '\u2022';
	char INFINITY = '\u221E';
	char INTERSECTION = '\u2229';
	char ALMOST_EQUAL_TO = '\u2248';
	char NOT_EQUAL_TO = '\u2260';
	char IDENTICAL_TO = '\u2261';
	char LESS_EQUAL_TO = '\u2264';
	char GREATER_EQUAL_TO = '\u2265';
	char BLACK_UP_POINTING_TRIANLE = '\u25B2';
	char BLACK_RIGHT_POINTING_TRIANGLE = '\u25BA';
	char BLACK_DOWN_POINTING_TRIANGLE = '\u25BC';
	char BLACK_LEFT_POINTING_TRIANGLE = '\u25C4';
	char BLACK_CIRCLE = '\u25CF';
	//</editor-fold>
	
	//<editor-fold desc="Superscript numbers">
	char SUPERSCRIPT_0 = '\u2070';
	char SUPERSCRIPT_1 = '\u00b9';
	char SUPERSCRIPT_2 = '\u00b2';
	char SUPERSCRIPT_3 = '\u00b3';
	char SUPERSCRIPT_4 = '\u2074';
	char SUPERSCRIPT_5 = '\u2075';
	char SUPERSCRIPT_6 = '\u2076';
	char SUPERSCRIPT_7 = '\u2077';
	char SUPERSCRIPT_8 = '\u2078';
	char SUPERSCRIPT_9 = '\u2079';
	char SUPERSCRIPT_MINUS = '\u02C9';
	//</editor-fold>
	
	/**
	 * Superscripts a given number.
	 *
	 * @param i
	 * 		The number to superscript
	 *
	 * @return superscripted number as string
	 */
	static String superscript(long i)
	{
		String v = i + "";
		StringBuilder s = new StringBuilder();
		while(!v.isEmpty())
		{
			switch(v.charAt(0))
			{
				case '0' -> s.append(SUPERSCRIPT_0);
				case '1' -> s.append(SUPERSCRIPT_1);
				case '2' -> s.append(SUPERSCRIPT_2);
				case '3' -> s.append(SUPERSCRIPT_3);
				case '4' -> s.append(SUPERSCRIPT_4);
				case '5' -> s.append(SUPERSCRIPT_5);
				case '6' -> s.append(SUPERSCRIPT_6);
				case '7' -> s.append(SUPERSCRIPT_7);
				case '8' -> s.append(SUPERSCRIPT_8);
				case '9' -> s.append(SUPERSCRIPT_9);
				case '-' -> s.append(SUPERSCRIPT_MINUS);
			}
			v = v.substring(1);
		}
		return s.toString();
	}
}