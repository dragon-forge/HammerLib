package com.zeitheron.hammercore.utils;

import java.math.BigDecimal;
import java.text.DecimalFormat;

import com.zeitheron.hammercore.utils.math.BigMath;

public class FileSizeMetric
{
	public static final double KB = 1024;
	public static final double MB = KB * 1024;
	public static final double GB = MB * 1024;
	public static final double TB = GB * 1024;
	public static final double PB = TB * 1024;
	
	public static final BigDecimal B_KB = new BigDecimal(KB);
	public static final BigDecimal B_MB = B_KB.multiply(B_KB);
	public static final BigDecimal B_GB = B_MB.multiply(B_KB);
	public static final BigDecimal B_TB = B_GB.multiply(B_KB);
	public static final BigDecimal B_PB = B_TB.multiply(B_KB);
	public static final BigDecimal B_EB = B_PB.multiply(B_KB);
	public static final BigDecimal B_ZB = B_EB.multiply(B_KB);
	public static final BigDecimal B_YB = B_ZB.multiply(B_KB);
	
	public static final DecimalFormat format = new DecimalFormat("#,##0.00");
	public static final DecimalFormat formati = new DecimalFormat("#,###");
	
	public static String toMaxSize(long bytes)
	{
		if(bytes < KB)
			return formati.format(bytes) + " B";
		else if(bytes < MB)
			return format.format(bytes / KB) + " KB";
		else if(bytes < GB)
			return format.format(bytes / MB) + " MB";
		else if(bytes < TB)
			return format.format(bytes / GB) + " GB";
		else if(bytes < PB)
			return format.format(bytes / TB) + " TB";
		return format.format(bytes / PB) + " PB";
	}
	
	public static String toMaxSize(BigDecimal bytes)
	{
		if(BigMath.isALesserThenB(bytes, B_KB, true))
			return formati.format(bytes) + " B";
		else if(BigMath.isALesserThenB(bytes, B_MB, true))
			return format.format(bytes.divide(B_KB)) + " KB";
		else if(BigMath.isALesserThenB(bytes, B_GB, true))
			return format.format(bytes.divide(B_MB)) + " MB";
		else if(BigMath.isALesserThenB(bytes, B_TB, true))
			return format.format(bytes.divide(B_GB)) + " GB";
		else if(BigMath.isALesserThenB(bytes, B_PB, true))
			return format.format(bytes.divide(B_TB)) + " TB";
		else if(BigMath.isALesserThenB(bytes, B_EB, true))
			return format.format(bytes.divide(B_PB)) + " PB";
		else if(BigMath.isALesserThenB(bytes, B_ZB, true))
			return format.format(bytes.divide(B_EB)) + " EB";
		else if(BigMath.isALesserThenB(bytes, B_YB, true))
			return format.format(bytes.divide(B_ZB)) + " ZB";
		return format.format(bytes.divide(B_YB)) + " YB";
	}
}