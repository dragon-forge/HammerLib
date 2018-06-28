package com.zeitheron.hammercore.lib.zlib.io.pipelib;

import java.nio.ByteBuffer;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class PipeFlow
{
	public static final ThreadLocal<FlowSide> ACCEPTOR = ThreadLocal.withInitial(() -> null);
	
	public String event;
	public byte[] data;
	
	public PipeFlow(String evt, byte[] data)
	{
		this();
		this.event = evt;
		this.data = data;
	}
	
	/* Creates an uninitialized flow */
	PipeFlow()
	{
	}
	
	public List<PipeFlow> perform(Collection<IFlowListener> listeners)
	{
		List<PipeFlow> response = new ArrayList<>();
		listeners.stream().filter(l -> l != null).forEach(l -> response.add(l.onFlow(ACCEPTOR.get(), this)));
		response.removeIf(l -> l == null);
		return response;
	}
	
	public byte[] serialize()
	{
		byte[] evt = event.getBytes();
		
		ByteBuffer buf = ByteBuffer.allocate(evt.length + data.length + 8);
		
		buf.putInt(evt.length);
		buf.put(evt);
		
		buf.putInt(data.length);
		buf.put(data);
		
		buf.flip();
		
		return buf.array();
	}
	
	public PipeFlow deserialize(byte[] data)
	{
		ByteBuffer buf = ByteBuffer.wrap(data);
		
		int heap = buf.getInt();
		if(heap > 1_000_000)
			return this;
		
		byte[] evt = new byte[heap];
		buf.get(evt, 0, evt.length);
		event = new String(evt);
		
		heap = buf.getInt();
		if(heap > 1_000_000)
			return this;
		
		this.data = new byte[heap];
		buf.get(this.data, 0, this.data.length);
		
		return this;
	}
}