package com.pengu.hammercore.tile.tooltip;

public class SimpleProgressBar extends ProgressBar
{
	public static final int QUAL = 100_000;
	
	public SimpleProgressBar()
	{
		super(QUAL);
	}
	
	@Override
	public long getMaxValue()
	{
		return QUAL;
	}

	@Override
	public SimpleProgressBar setMaxValue(long maxValue)
	{
		return this;
	}

	@Override
	public int getProgressPercent()
	{
		return (int) Math.round((value / (float) QUAL) * 100D);
	}
	
	@Override
	public ProgressBar setProgress(long value)
	{
		throw new UnsupportedOperationException("Cannot set long progress in simple progress bars!");
	}
	
	public SimpleProgressBar setProgress(float value)
	{
		if(value < 0)
			value = 0;
		if(value > maxValue)
			value = maxValue;
		this.value = (long) (value * QUAL);
		return this;
	}
}