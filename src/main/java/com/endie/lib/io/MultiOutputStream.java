package com.endie.lib.io;

import java.io.IOException;
import java.io.OutputStream;

public class MultiOutputStream extends OutputStream
{
	private final OutputStream[] outs;
	
	public MultiOutputStream(OutputStream... outs)
	{
		if(outs == null || outs.length == 0)
			throw new NullPointerException();
		
		this.outs = outs;
	}
	
	@Override
	public void write(int b) throws IOException
	{
		for(OutputStream o : outs)
			o.write(b);
	}
	
	@Override
	public void write(byte[] b) throws IOException
	{
		for(OutputStream o : outs)
			o.write(b);
	}
	
	@Override
	public void write(byte[] b, int off, int len) throws IOException
	{
		for(OutputStream o : outs)
			o.write(b, off, len);
	}
	
	@Override
	public void flush() throws IOException
	{
		for(OutputStream o : outs)
			o.flush();
	}
	
	@Override
	public void close() throws IOException
	{
		for(OutputStream o : outs)
			o.close();
	}
}