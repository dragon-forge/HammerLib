package com.pengu.hammercore.tile.tooltip;

public class ProgressBar
{
	private long maxValue = 100L;
	private long value = 0L;
	public eNumberFormat numberFormat;
	public String prefix, suffix;
	public int backgroundColor, borderColor;
	public int filledMainColor, filledAlternateColor;
	
	public ProgressBar(long maxValue)
	{
		if(maxValue < 1)
			throw new IndexOutOfBoundsException("maxValue");
		this.maxValue = maxValue;
	}
	
	public float getProgress()
	{
		return value;
	}
	
	public long getMaxValue()
	{
		return maxValue;
	}
	
	public void setMaxValue(long maxValue)
	{
		if(maxValue < 1)
			throw new IndexOutOfBoundsException("maxValue");
		this.maxValue = maxValue;
	}
	
	public int getProgressPercent()
	{
		return (int) Math.round((value / (double) maxValue) * 100D);
	}
	
	public void setProgress(long value)
	{
		if(value < 0)
			value = 0;
		if(value > maxValue)
			value = maxValue;
		this.value = value;
	}
}