package com.pengu.hammercore.common.structure.io;

import java.io.Closeable;
import java.io.DataInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.zip.GZIPInputStream;

import net.minecraft.util.math.BlockPos;

public class StructureInputStream implements Closeable, AutoCloseable
{
	public final DataInputStream in;
	private boolean hasMore = false;
	
	public StructureInputStream(InputStream stream) throws IOException
	{
		in = new DataInputStream(new GZIPInputStream(stream));
		hasMore = in.readBoolean();
	}
	
	public StructureElement read() throws IOException
	{
		if(!in.readBoolean())
			return null;
		String block = string();
		byte meta = in.readByte();
		String nbt = string();
		String[] pos = string().split(" ");
		hasMore = in.readBoolean();
		return new StructureElement(block, meta, nbt, new BlockPos(Integer.parseInt(pos[0]), Integer.parseInt(pos[1]), Integer.parseInt(pos[2])));
	}
	
	public boolean hasMore()
	{
		return hasMore;
	}
	
	private String string() throws IOException
	{
		byte[] buf = new byte[in.readInt()];
		in.read(buf);
		return new String(buf);
	}

	@Override
	public void close() throws IOException
	{
		in.close();
	}
}