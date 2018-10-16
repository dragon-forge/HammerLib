package com.zeitheron.hammercore.client.utils.texture.gif;

import net.minecraft.util.ResourceLocation;

public class GLGifInfo
{
	public final ResourceLocation[] tex;
	public final int width, height;
	public final int[] delay;
	
	// Delay info start
	
	public long[] frameDelAI;
	public long lastFrameDel;
	
	// Delay info end
	
	public GLGifInfo(ResourceLocation[] tex, int width, int height, int[] delay)
	{
		this.tex = tex;
		this.width = width;
		this.height = height;
		this.delay = delay;
		initDelayInfo();
	}
	
	public void initDelayInfo()
	{
		long td = 0;
		frameDelAI = new long[delay.length];
		for(int i = 0; i < delay.length; ++i)
		{
			long dm = delay[i] * 10L;
			td += dm;
			frameDelAI[i] = td;
		}
		lastFrameDel = td;
	}
	
	public int getFrameFromMS(long time)
	{
		time = time % lastFrameDel;
		
		int frame;
		
		for(frame = 0; frame < frameDelAI.length && time >= frameDelAI[frame]; ++frame)
			;
		
		return frame % delay.length;
	}
}