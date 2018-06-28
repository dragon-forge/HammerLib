package com.zeitheron.hammercore.client.audio;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import javax.sound.sampled.AudioFormat;
import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.DataLine;
import javax.sound.sampled.SourceDataLine;
import javax.sound.sampled.TargetDataLine;

import net.minecraftforge.fml.common.FMLLog;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

@SideOnly(Side.CLIENT)
public class MicrophoneSetup
{
	public static AudioFormat format;
	public static TargetDataLine microphone;
	
	static
	{
		try
		{
			format = new AudioFormat(32000F, 16, 1, true, true);
			microphone = AudioSystem.getTargetDataLine(format);
			DataLine.Info info = new DataLine.Info(TargetDataLine.class, format);
			microphone = (TargetDataLine) AudioSystem.getLine(info);
			microphone.open(format);
		} catch(Throwable err)
		{
			FMLLog.severe("WARNING: Microphone not found!");
		}
	}
	
	public static byte[] captuteMicBytes(final int bytes, boolean asThread)
	{
		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		captureMic(baos, bytes, asThread);
		return baos.toByteArray();
	}
	
	public static void captureMic(final OutputStream out, final int bytes, boolean asThread)
	{
		if(asThread)
			new Thread()
			{
				@Override
				public void run()
				{
					captureMic(out, bytes);
				}
			}.start();
		else
			captureMic(out, bytes);
	}
	
	private static void captureMic(OutputStream out, int bytes)
	{
		
		try
		{
			int numBytesRead;
			int CHUNK_SIZE = 1024;
			byte[] data = new byte[microphone.getBufferSize() / 5];
			microphone.start();
			int bytesRead = 0;
			try
			{
				while(bytesRead < bytes || bytes == -1)
				{
					numBytesRead = microphone.read(data, 0, CHUNK_SIZE);
					bytesRead = bytesRead + numBytesRead;
					out.write(data, 0, numBytesRead);
				}
			} catch(Exception e)
			{
			}
		} catch(Exception e)
		{
			e.printStackTrace();
		}
	}
	
	public static void playMic(InputStream output)
	{
		AudioInputStream audioInputStream;
		SourceDataLine sourceDataLine;
		try
		{
			audioInputStream = new AudioInputStream(output, format, output.available() / format.getFrameSize());
			DataLine.Info dataLineInfo = new DataLine.Info(SourceDataLine.class, format);
			sourceDataLine = (SourceDataLine) AudioSystem.getLine(dataLineInfo);
			sourceDataLine.open(format);
			sourceDataLine.start();
			int cnt = 0;
			byte tempBuffer[] = new byte[10000];
			try
			{
				while((cnt = audioInputStream.read(tempBuffer, 0, tempBuffer.length)) != -1)
					if(cnt > 0)
						sourceDataLine.write(tempBuffer, 0, cnt);
			} catch(IOException e)
			{
				e.printStackTrace();
			}
			sourceDataLine.drain();
			sourceDataLine.close();
		} catch(Exception e)
		{
		}
	}
	
	public static void playMic(byte[] bytes)
	{
		ByteArrayInputStream output = new ByteArrayInputStream(bytes);
		playMic(output);
	}
	
	public static void prepareSetups()
	{
		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		captureMic(baos, 30000, false);
	}
}