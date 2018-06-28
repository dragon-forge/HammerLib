package com.zeitheron.hammercore.client.audio;

import java.io.ByteArrayOutputStream;

public class Recorder implements Runnable
{
	public static interface Function<V>
	{
		public void call(V v);
	}
	
	private static int recorders = 0;
	public final Thread asThread = new Thread(this);
	public boolean recording = true;
	public boolean running = false;
	public int packetMaxSize = 8000;
	public Function<byte[]> onReRecord1;
	public Function<ByteArrayOutputStream> onReRecord2;
	
	public Recorder()
	{
		asThread.setName("HammerCoreMicrophoneRecorder#" + recorders);
		recorders++;
	}
	
	public void start()
	{
		asThread.start();
	}
	
	@Override
	public void run()
	{
		running = true;
		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		while(running)
			if(!recordPiece(baos))
				break;
	}
	
	public boolean recordPiece(ByteArrayOutputStream baos)
	{
		if(recording)
		{
			try
			{
				MicrophoneSetup.captureMic(baos, packetMaxSize, false);
				if(onReRecord1 != null)
					onReRecord1.call(baos.toByteArray());
				if(onReRecord2 != null)
					onReRecord2.call(baos);
				baos.reset();
			} catch(Throwable err)
			{
				return false;
			}
		} else
			try
			{
				Thread.sleep(5L);
			} catch(Exception e)
			{
			}
		return true;
	}
}