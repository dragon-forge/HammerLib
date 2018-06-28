package com.zeitheron.hammercore.lib.zlib.audio;

public class AudioSampleHelper
{
	public static float getRMS(float[] samples)
	{
		float rms = 0.0f;
		float[] arrf = samples;
		int n = arrf.length;
		int n2 = 0;
		while(n2 < n)
		{
			float sample = arrf[n2];
			rms += sample * sample;
			++n2;
		}
		return (float) Math.sqrt(rms / (float) samples.length);
	}
	
	public static float[] intoSamples(byte[] data, int len, float[] sampleBuf)
	{
		int i = 0;
		int s = 0;
		while(i < len)
		{
			int sample = 0;
			sample |= data[i++] & 255;
			sampleBuf[s++] = (float) (sample |= data[i++] << 8) / 32768.0f;
		}
		return sampleBuf;
	}
	
	public static float[] asSamples(byte[] data, int len)
	{
		return AudioSampleHelper.intoSamples(data, len, new float[len / 2]);
	}
}
