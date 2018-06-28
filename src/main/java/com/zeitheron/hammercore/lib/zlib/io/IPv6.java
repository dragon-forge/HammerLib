package com.zeitheron.hammercore.lib.zlib.io;

import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.SocketException;
import java.net.UnknownHostException;
import java.util.UUID;

public class IPv6
{
	public static UUID getHardwareUUID()
	{
		try
		{
			InetAddress ip = InetAddress.getLocalHost();
			NetworkInterface network = NetworkInterface.getByInetAddress(ip);
			return UUID.nameUUIDFromBytes(network.getHardwareAddress());
		} catch(UnknownHostException e)
		{
			e.printStackTrace();
		} catch(SocketException e)
		{
			e.printStackTrace();
		}
		return null;
	}
	
	public static String getMACAddr()
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
		return mac;
	}
}