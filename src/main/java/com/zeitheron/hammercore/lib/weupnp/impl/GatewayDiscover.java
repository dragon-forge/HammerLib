package com.zeitheron.hammercore.lib.weupnp.impl;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.Inet4Address;
import java.net.Inet6Address;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.NetworkInterface;
import java.net.SocketException;
import java.net.SocketTimeoutException;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.StringTokenizer;

import javax.xml.parsers.ParserConfigurationException;

import org.xml.sax.SAXException;

public class GatewayDiscover
{
	public static final int PORT = 1900;
	public static final String IP = "239.255.255.250";
	private int timeout;
	private String[] searchTypes;
	private static final String[] DEFAULT_SEARCH_TYPES = new String[] { "urn:schemas-upnp-org:device:InternetGatewayDevice:1", "urn:schemas-upnp-org:service:WANIPConnection:1", "urn:schemas-upnp-org:service:WANPPPConnection:1" };
	private final Map<InetAddress, GatewayDevice> devices;
	
	public GatewayDiscover()
	{
		this(DEFAULT_SEARCH_TYPES);
	}
	
	public GatewayDiscover(String st)
	{
		this(new String[] { st });
	}
	
	public GatewayDiscover(String[] types)
	{
		this.timeout = 3000;
		this.devices = new HashMap();
		this.searchTypes = types;
	}
	
	public int getTimeout()
	{
		return this.timeout;
	}
	
	public void setTimeout(int milliseconds)
	{
		this.timeout = milliseconds;
	}
	
	public Map<InetAddress, GatewayDevice> discover() throws SocketException, UnknownHostException, IOException, SAXException, ParserConfigurationException
	{
		List ips = this.getLocalInetAddresses(true, false, false);
		
		for(int i = 0; i < this.searchTypes.length; ++i)
		{
			String searchMessage = "M-SEARCH * HTTP/1.1\r\nHOST: 239.255.255.250:1900\r\nST: " + this.searchTypes[i] + "\r\n" + "MAN: \"ssdp:discover\"\r\n" + "MX: 2\r\n" + "\r\n";
			ArrayList threads = new ArrayList();
			Iterator i$ = ips.iterator();
			
			while(i$.hasNext())
			{
				InetAddress thread = (InetAddress) i$.next();
				GatewayDiscover.SendDiscoveryThread e = new GatewayDiscover.SendDiscoveryThread(thread, searchMessage);
				threads.add(e);
				e.start();
			}
			
			i$ = threads.iterator();
			
			while(i$.hasNext())
			{
				GatewayDiscover.SendDiscoveryThread var9 = (GatewayDiscover.SendDiscoveryThread) i$.next();
				
				try
				{
					var9.join();
				} catch(InterruptedException var8)
				{
					;
				}
			}
			
			if(!this.devices.isEmpty())
			{
				break;
			}
		}
		
		return this.devices;
	}
	
	private GatewayDevice parseMSearchReply(byte[] reply)
	{
		GatewayDevice device = new GatewayDevice();
		String replyString = new String(reply);
		StringTokenizer st = new StringTokenizer(replyString, "\n");
		
		while(st.hasMoreTokens())
		{
			String line = st.nextToken().trim();
			if(!line.isEmpty() && !line.startsWith("HTTP/1.") && !line.startsWith("NOTIFY *"))
			{
				String key = line.substring(0, line.indexOf(58));
				String value = line.length() > key.length() + 1 ? line.substring(key.length() + 1) : null;
				key = key.trim();
				if(value != null)
				{
					value = value.trim();
				}
				
				if(key.compareToIgnoreCase("location") == 0)
				{
					device.setLocation(value);
				} else if(key.compareToIgnoreCase("st") == 0)
				{
					device.setSt(value);
				}
			}
		}
		
		return device;
	}
	
	public GatewayDevice getValidGateway()
	{
		Iterator i$ = this.devices.values().iterator();
		
		while(i$.hasNext())
		{
			GatewayDevice device = (GatewayDevice) i$.next();
			
			try
			{
				if(device.isConnected())
				{
					return device;
				}
			} catch(Exception var4)
			{
				;
			}
		}
		
		return null;
	}
	
	public Map getAllGateways()
	{
		return this.devices;
	}
	
	private List getLocalInetAddresses(boolean getIPv4, boolean getIPv6, boolean sortIPv4BeforeIPv6)
	{
		ArrayList arrayIPAddress = new ArrayList();
		int lastIPv4Index = 0;
		
		Enumeration networkInterfaces;
		try
		{
			networkInterfaces = NetworkInterface.getNetworkInterfaces();
		} catch(SocketException var11)
		{
			return arrayIPAddress;
		}
		
		if(networkInterfaces == null)
		{
			return arrayIPAddress;
		} else
		{
			while(networkInterfaces.hasMoreElements())
			{
				NetworkInterface card = (NetworkInterface) networkInterfaces.nextElement();
				
				try
				{
					if(card.isLoopback() || card.isPointToPoint() || card.isVirtual() || !card.isUp())
					{
						continue;
					}
				} catch(SocketException var12)
				{
					continue;
				}
				
				Enumeration addresses = card.getInetAddresses();
				if(addresses != null)
				{
					while(addresses.hasMoreElements())
					{
						InetAddress inetAddress = (InetAddress) addresses.nextElement();
						int index = arrayIPAddress.size();
						if(getIPv4 && getIPv6)
						{
							if(sortIPv4BeforeIPv6 && Inet4Address.class.isInstance(inetAddress))
							{
								index = lastIPv4Index++;
							}
						} else if(getIPv4 && !Inet4Address.class.isInstance(inetAddress) || getIPv6 && !Inet6Address.class.isInstance(inetAddress))
						{
							continue;
						}
						
						arrayIPAddress.add(index, inetAddress);
					}
				}
			}
			
			return arrayIPAddress;
		}
	}
	
	private class SendDiscoveryThread extends Thread
	{
		
		InetAddress ip;
		String searchMessage;
		
		SendDiscoveryThread(InetAddress localIP, String searchMessage)
		{
			this.ip = localIP;
			this.searchMessage = searchMessage;
		}
		
		@Override
		public void run()
		{
			DatagramSocket ssdp = null;
			
			try
			{
				ssdp = new DatagramSocket(new InetSocketAddress(this.ip, 0));
				byte[] e = this.searchMessage.getBytes();
				DatagramPacket ssdpDiscoverPacket = new DatagramPacket(e, e.length);
				ssdpDiscoverPacket.setAddress(InetAddress.getByName("239.255.255.250"));
				ssdpDiscoverPacket.setPort(1900);
				ssdp.send(ssdpDiscoverPacket);
				ssdp.setSoTimeout(GatewayDiscover.this.timeout);
				boolean waitingPacket = true;
				
				while(waitingPacket)
				{
					DatagramPacket receivePacket = new DatagramPacket(new byte[1536], 1536);
					
					try
					{
						ssdp.receive(receivePacket);
						byte[] ste = new byte[receivePacket.getLength()];
						System.arraycopy(receivePacket.getData(), 0, ste, 0, receivePacket.getLength());
						GatewayDevice gatewayDevice = GatewayDiscover.this.parseMSearchReply(ste);
						gatewayDevice.setLocalAddress(this.ip);
						gatewayDevice.loadDescription();
						if(Arrays.asList(GatewayDiscover.this.searchTypes).contains(gatewayDevice.getSt()))
						{
							synchronized(GatewayDiscover.this.devices)
							{
								GatewayDiscover.this.devices.put(this.ip, gatewayDevice);
								break;
							}
						}
					} catch(SocketTimeoutException var16)
					{
						waitingPacket = false;
					}
				}
			} catch(Exception var17)
			{
				;
			} finally
			{
				if(null != ssdp)
				{
					ssdp.close();
				}
				
			}
			
		}
	}
}
