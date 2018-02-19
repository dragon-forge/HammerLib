package com.pengu.hammercore.common.utils;

public class Chars
{
	/**
	 * @deprecated Power of two and three are replaced with
	 *             {@link #SUPERSCRIPT_2} and {@link #SUPERSCRIPT_3}
	 */
	@Deprecated
	public static final char //
	POWER_OF_TWO = '\u00b2', //
	        POWER_OF_THREE = '\u00b3';
	
	public static final char //
	DEGREE_SIGN = '\u00b0', //
	        RHO = '\u03c1', //
	        SQRT = '\u221A', //
	        PI = '\u1D28', //
	        PSI = '\u1D2A', //
	        SECTION_SIGN = '\u00A7';
	
	/** Superscript numbers */
	public static final char //
	SUPERSCRIPT_0 = '\u2070', //
	        SUPERSCRIPT_1 = '\u00b9', //
	        SUPERSCRIPT_2 = '\u00b2', //
	        SUPERSCRIPT_3 = '\u00b3', //
	        SUPERSCRIPT_4 = '\u2074', //
	        SUPERSCRIPT_5 = '\u2075', //
	        SUPERSCRIPT_6 = '\u2076', //
	        SUPERSCRIPT_7 = '\u2077', //
	        SUPERSCRIPT_8 = '\u2078', //
	        SUPERSCRIPT_9 = '\u2079', //
	        SUPERSCRIPT_MINUS = '\u02C9';
	
	/** Superscripts a given number. */
	public static String superscript(long i)
	{
		String v = i + "";
		StringBuilder s = new StringBuilder();
		while(!v.isEmpty())
		{
			switch(v.charAt(0))
			{
			case '0':
				s.append(SUPERSCRIPT_0);
			break;
			case '1':
				s.append(SUPERSCRIPT_1);
			break;
			case '2':
				s.append(SUPERSCRIPT_2);
			break;
			case '3':
				s.append(SUPERSCRIPT_3);
			break;
			case '4':
				s.append(SUPERSCRIPT_4);
			break;
			case '5':
				s.append(SUPERSCRIPT_5);
			break;
			case '6':
				s.append(SUPERSCRIPT_6);
			break;
			case '7':
				s.append(SUPERSCRIPT_7);
			break;
			case '8':
				s.append(SUPERSCRIPT_8);
			break;
			case '9':
				s.append(SUPERSCRIPT_9);
			break;
			case '-':
				s.append(SUPERSCRIPT_MINUS);
			break;
			}
			v = v.substring(1);
		}
		return s.toString();
	}
}