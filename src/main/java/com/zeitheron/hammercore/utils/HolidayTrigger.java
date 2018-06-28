package com.zeitheron.hammercore.utils;

import java.util.Calendar;
import java.util.Date;

public class HolidayTrigger
{
	public static boolean isAprilFools()
	{
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(new Date());
		return calendar.get(Calendar.MONTH) == Calendar.APRIL && calendar.get(Calendar.DAY_OF_MONTH) == 1;
	}
	
	public static boolean isChristmas()
	{
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(new Date());
		return calendar.get(Calendar.MONTH) == Calendar.DECEMBER && calendar.get(Calendar.DAY_OF_MONTH) == 24;
	}
	
	public static boolean isHalloween()
	{
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(new Date());
		return calendar.get(Calendar.MONTH) == Calendar.NOVEMBER && (calendar.get(Calendar.DAY_OF_MONTH) == 31 || calendar.get(Calendar.DAY_OF_MONTH) == 30);
	}
	
	public static boolean isNewYear()
	{
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(new Date());
		return calendar.get(Calendar.MONTH) == Calendar.JANUARY && calendar.get(Calendar.DAY_OF_MONTH) == 1;
	}
}