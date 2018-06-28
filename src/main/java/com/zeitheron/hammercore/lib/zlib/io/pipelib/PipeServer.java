package com.zeitheron.hammercore.lib.zlib.io.pipelib;

import java.io.Closeable;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketException;
import java.net.SocketTimeoutException;
import java.util.ArrayList;
import java.util.List;

import javax.xml.parsers.ParserConfigurationException;

import org.xml.sax.SAXException;

import com.zeitheron.hammercore.lib.zlib.weupnp.AttuneResult;
import com.zeitheron.hammercore.lib.zlib.weupnp.EnumProtocol;
import com.zeitheron.hammercore.lib.zlib.weupnp.WeUPnP;

public class PipeServer implements AutoCloseable, Closeable
{
	{
		if(PipeFlow.ACCEPTOR.get() != null)
			throw new RuntimeException("This thread already flows to " + PipeFlow.ACCEPTOR.get().name().toLowerCase() + "!");
		PipeFlow.ACCEPTOR.set(FlowSide.SERVER);
	}
	
	private static WeUPnP upnp = new WeUPnP();
	public List<IFlowListener> listeners = new ArrayList<>();
	private boolean prepared = false;
	private boolean started = true;
	private boolean closed = false;
	private ServerSocket server;
	private AttuneResult res;
	private int port;
	private String channel;
	
	{
		upnp.setup();
		try
		{
			upnp.discover();
		} catch(IOException | SAXException | ParserConfigurationException e)
		{
			e.printStackTrace();
		}
	}
	
	public List<IFlowListener> getListeners()
	{
		return listeners;
	}
	
	public void addListener(IFlowListener l)
	{
		listeners.add(l);
	}
	
	public String getChannel()
	{
		return channel;
	}
	
	public int getPort()
	{
		return port;
	}
	
	public boolean isClosed()
	{
		return closed;
	}
	
	public boolean isPrepared()
	{
		return prepared;
	}
	
	public boolean isStarted()
	{
		return started;
	}
	
	public PipeServer prepare()
	{
		if(prepared)
			throw new IllegalStateException("Cannot prepare prepared pipe server!");
		
		try
		{
			res = upnp.attune(EnumProtocol.TCP, port, port, "Endie's PipeLib Server");
		} catch(IOException | SAXException e1)
		{
			e1.printStackTrace();
		}
		
		try
		{
			server = new ServerSocket(port, 150);
		} catch(IOException e)
		{
			throw new RuntimeException("Unable to prepare pipe server!", e);
		}
		
		prepared = true;
		return this;
	}
	
	public void start()
	{
		if(!prepared)
			throw new IllegalStateException("Cannot start unprepared pipe server!");
		
		started = true;
		
		try
		{
			server.setSoTimeout(1000);
		} catch(SocketException e)
		{
			e.printStackTrace();
		}
		
		while(!closed)
		{
			try
			{
				process(server.accept());
			} catch(SocketTimeoutException e)
			{
			} catch(IOException e)
			{
				e.printStackTrace();
			}
		}
		
		System.out.println("Pipe server closed.");
	}
	
	private void process(Socket s) throws IOException
	{
		DataInputStream i = new DataInputStream(s.getInputStream());
		DataOutputStream o = new DataOutputStream(s.getOutputStream());
		
		int heap = i.readInt();
		if(heap > 1_000_000)
			return;
		
		byte[] data = new byte[heap];
		i.read(data);
		PipeFlow flow = new PipeFlow().deserialize(data);
		
		if(flow == null || (flow.event != null && flow.event.equals("Channel")))
		{
			String rem = new String(flow.data);
			if(flow == null || !rem.equals(getChannel()))
			{
				o.writeBoolean(false);
				o.flush();
				i.close();
				o.close();
				s.close();
				return;
			}
			
			o.writeBoolean(true);
			o.flush();
			
			List<PipeFlow> ps = new ArrayList<>();
			
			short packets = i.readShort();
			
			for(short f = 0; f < packets; ++f)
			{
				data = new byte[i.readInt()];
				i.read(data);
				ps.addAll(new PipeFlow().deserialize(data).perform(getListeners()));
			}
			
			o.writeShort(ps.size());
			
			for(short f = 0; f < ps.size(); ++f)
			{
				PipeFlow p = ps.get(f);
				data = p.serialize();
				o.writeInt(data.length);
				o.write(data);
			}
			
			o.flush();
			i.close();
			o.close();
			s.close();
		}
	}
	
	public PipeServer link(String channel, int port)
	{
		return link(channel).link(port);
	}
	
	public PipeServer link(int port)
	{
		if(prepared)
			throw new IllegalStateException("Cannot set endpoint port to prepared pipe server!");
		
		this.port = port;
		
		return this;
	}
	
	public PipeServer link(String channel)
	{
		if(prepared)
			throw new IllegalStateException("Cannot set channel to prepared pipe server!");
		
		this.channel = channel;
		return this;
	}
	
	@Override
	public void close()
	{
		if(!prepared)
			throw new IllegalStateException("Cannot close unprepared pipe server!");
		
		/* Reset flow side */
		PipeFlow.ACCEPTOR.set(null);
		
		/* Close server socket */
		if(server != null)
			try
			{
				server.close();
			} catch(IOException e)
			{
				e.printStackTrace();
			}
		
		/* Unbind uPnP port */
		if(res != null)
			try
			{
				res.undo();
			} catch(IOException | SAXException e)
			{
				e.printStackTrace();
			}
	}
}