package com.pengu.hammercore.common.structure.io;

import java.io.Closeable;
import java.io.DataOutputStream;
import java.io.Flushable;
import java.io.IOException;
import java.io.OutputStream;
import java.util.zip.GZIPOutputStream;

public class StructureOutputStream implements Closeable, AutoCloseable, Flushable
{
	public final DataOutputStream out;
	
	public StructureOutputStream(OutputStream stream) throws IOException
	{
		out = new DataOutputStream(new GZIPOutputStream(stream));
	}
	
	public void write(StructureElement... es) throws IOException
	{
		for(StructureElement e : es)
			write(e);
	}
	
	public void write(StructureElement e) throws IOException
	{
		out.writeBoolean(true);
		out.writeBoolean(e != null);
		if(e == null)
			return;
		string(e.block);
		out.writeByte(e.meta);
		string(e.nbt == null ? "" : e.nbt.toString());
		string(e.relativePos.getX() + " " + e.relativePos.getY() + " " + e.relativePos.getZ());
	}
	
	@Override
	public void close() throws IOException
	{
		out.writeBoolean(false);
		out.close();
	}
	
	@Override
	public void flush() throws IOException
	{
		out.flush();
	}
	
	private void string(String str) throws IOException
	{
		byte[] bb = str.getBytes();
		out.writeInt(bb.length);
		out.write(bb);
	}
}