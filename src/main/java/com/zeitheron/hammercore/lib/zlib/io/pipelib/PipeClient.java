package com.zeitheron.hammercore.lib.zlib.io.pipelib;

import java.io.Closeable;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.Socket;
import java.net.SocketException;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.List;

import com.zeitheron.hammercore.lib.zlib.error.JSONException;
import com.zeitheron.hammercore.lib.zlib.io.IOUtils;
import com.zeitheron.hammercore.lib.zlib.json.JSONObject;
import com.zeitheron.hammercore.lib.zlib.json.JSONTokener;

public class PipeClient implements AutoCloseable, Closeable
{
	public final String MAC_ADDR;
	private JSONObject server;
	public List<IFlowListener> listeners = new ArrayList<>();
	
	private String ip;
	private int port;
	public String channel;
	
	{
		if(PipeFlow.ACCEPTOR.get() != null)
			throw new RuntimeException("This thread already flows to " + PipeFlow.ACCEPTOR.get().name().toLowerCase() + "!");
		PipeFlow.ACCEPTOR.set(FlowSide.CLIENT);
	}
	
	public PipeClient(String server, String channel)
	{
		String mac = "";
		try
		{
			InetAddress ip = InetAddress.getLocalHost();
			NetworkInterface network = NetworkInterface.getByInetAddress(ip);
			
			byte[] ha = network.getHardwareAddress();
			
			StringBuilder sb = new StringBuilder();
			for(int i = 0; i < ha.length; i++)
				sb.append(String.format("%02X%s", ha[i], (i < ha.length - 1) ? "-" : ""));
			mac = sb.toString();
		} catch(UnknownHostException e)
		{
			e.printStackTrace();
		} catch(SocketException e)
		{
			e.printStackTrace();
		}
		MAC_ADDR = mac;
		
		this.channel = channel;
		
		try
		{
			this.server = (JSONObject) (server.startsWith("{") ? new JSONTokener(server.trim()).nextValue() : IOUtils.downloadjson(server));
			
			port = this.server.getInt("port");
			ip = this.server.getString("ip");
		} catch(JSONException e)
		{
			throw new RuntimeException("Failed to parse server info", e);
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
	
	public String getIp()
	{
		return ip;
	}
	
	public int getPort()
	{
		return port;
	}
	
	public JSONObject getServer()
	{
		return server;
	}
	
	public void flow(PipeFlow... flows)
	{
		try(Socket s = new Socket(ip, port))
		{
			process(s, flows);
		} catch(IOException e)
		{
			e.printStackTrace();
		}
	}
	
	private void process(Socket s, PipeFlow... flows) throws IOException
	{
		DataInputStream i = new DataInputStream(s.getInputStream());
		DataOutputStream o = new DataOutputStream(s.getOutputStream());
		
		{
			PipeFlow flow = new PipeFlow();
			flow.event = "Channel";
			flow.data = channel.getBytes();
			
			byte[] data = flow.serialize();
			
			o.writeInt(data.length);
			o.write(data);
			o.flush();
		}
		
		if(!i.readBoolean())
			throw new IOException("Channel mismatch!");
		
		o.writeShort(flows.length);
		
		for(short f = 0; f < flows.length; ++f)
		{
			PipeFlow p = flows[f];
			byte[] data = p.serialize();
			o.writeInt(data.length);
			o.write(data);
		}
		
		List<PipeFlow> ps = new ArrayList<>();
		
		short packets = i.readShort();
		
		for(short f = 0; f < packets; ++f)
		{
			byte[] data = new byte[i.readInt()];
			i.read(data);
			ps.addAll(new PipeFlow().deserialize(data).perform(getListeners()));
		}
		
		o.close();
		i.close();
		s.close();
		
		if(!ps.isEmpty())
			flow(ps.toArray(new PipeFlow[ps.size()]));
	}
	
	@Override
	public void close()
	{
		/* Reset flow side */
		PipeFlow.ACCEPTOR.set(null);
	}
}