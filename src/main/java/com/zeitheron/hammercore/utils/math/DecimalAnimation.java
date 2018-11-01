package com.zeitheron.hammercore.utils.math;

import com.zeitheron.hammercore.lib.zlib.utils.Timer;

public class DecimalAnimation
{
	public final Timer timer;
	
	public boolean sine;
	
	public DecimalAnimation(float tps, boolean sine)
	{
		this.timer = new Timer(tps);
		this.sine = sine;
	}
	
	public float prevVal;
	public float val;
	
	public void setVal(float val)
	{
		prevVal = getActualValue();
		this.val = val;
		timer.resetTimer();
	}
	
	public float getActualValue()
	{
		timer.advanceTime();
		return prevVal + (val - prevVal) * (sine ? (float) Math.sin(Math.toRadians(timer.partialTime * 90F)) : timer.partialTime);
	}
}