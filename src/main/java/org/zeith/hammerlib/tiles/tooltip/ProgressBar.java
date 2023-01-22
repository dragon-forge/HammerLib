package org.zeith.hammerlib.tiles.tooltip;

public class ProgressBar
{
	protected long maxValue = 100L;
	protected long value = 0L;
	public EnumNumberFormat numberFormat = EnumNumberFormat.COMPACT;
	public String prefix, suffix;
	public int backgroundColor, borderColor;
	public int filledMainColor, filledAlternateColor;
	
	public ProgressBar(long maxValue)
	{
		if(maxValue < 1)
			throw new IndexOutOfBoundsException("maxValue");
		this.maxValue = maxValue;
	}
	
	public ProgressBar withNumberFormat(EnumNumberFormat format)
	{
		this.numberFormat = format;
		return this;
	}
	
	public ProgressBar withStyle(ProgressBarStyle style)
	{
		return style.apply(this);
	}
	
	public float getProgress()
	{
		return (float) (value / (double) maxValue);
	}
	
	public long getMaxValue()
	{
		return maxValue;
	}
	
	public ProgressBar setMaxValue(long maxValue)
	{
		if(maxValue < 1)
			throw new IndexOutOfBoundsException("maxValue");
		this.maxValue = maxValue;
		return this;
	}
	
	public int getProgressPercent()
	{
		return (int) Math.round((value / (double) maxValue) * 100D);
	}
	
	public ProgressBar setProgress(long value)
	{
		if(value < 0)
			value = 0;
		if(value > maxValue)
			value = maxValue;
		this.value = value;
		return this;
	}
	
	public record ProgressBarStyle(int backgroundColor, int borderColor, int filledMainColor, int filledAlternateColor)
	{
		public static final ProgressBarStyle FORGE_ENERGY_STYLE = new ProgressBarStyle(0xFF373737, 0xFF8B8B8B, 0xFFD63535, 0xFFFF2121);
		
		public <T extends ProgressBar> T apply(T bar)
		{
			bar.backgroundColor = backgroundColor;
			bar.borderColor = borderColor;
			bar.filledMainColor = filledMainColor;
			bar.filledAlternateColor = filledAlternateColor;
			return bar;
		}
	}
}