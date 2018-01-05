package com.pengu.hammercore.common.utils;

import java.text.DecimalFormat;

public class FileSizeMetric
{
	public static final float KiB = 1024;
	public static final float MeB = KiB * 1024;
	public static final float GiB = MeB * 1024;
	public static final float TeB = GiB * 1024;
	
	public static final DecimalFormat format = new DecimalFormat("#,##0.00");
	public static final DecimalFormat formati = new DecimalFormat("#,###");
	
	public static String toMaxSize(long bytes)
	{
		if(bytes < KiB)
			return formati.format(bytes) + "B";
		else if(bytes < MeB)
			return format.format(bytes / KiB) + "KiB";
		else if(bytes < GiB)
			return format.format(bytes / MeB) + "MiB";
		else if(bytes < TeB)
			return format.format(bytes / GiB) + "GiB";
		else
			return (bytes / GiB) + "TeB";
	}
}