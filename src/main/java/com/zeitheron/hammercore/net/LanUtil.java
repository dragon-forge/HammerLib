package com.zeitheron.hammercore.net;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.ServerSocket;

import com.zeitheron.hammercore.cfg.HammerCoreConfigs;

import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

@SideOnly(Side.CLIENT)
public class LanUtil
{
	public static int port = 0;
	public static int maxPlayers = 8;
	public static boolean pvp = true;
	public static boolean online = true;
	
	private static final File file = new File("hc-lan-prefs.bin");
	
	public static void load()
	{
		try(DataInputStream in = new DataInputStream(new FileInputStream(file)))
		{
			port = in.readInt();
			maxPlayers = in.readInt();
			pvp = in.readBoolean();
			online = in.readBoolean();
		} catch(Throwable err)
		{
		}
		
		if(maxPlayers <= 0)
			maxPlayers = 8;
	}
	
	public static void save()
	{
		if(!file.isFile())
			try
			{
				file.createNewFile();
			} catch(Throwable err)
			{
			}
		
		try(DataOutputStream out = new DataOutputStream(new FileOutputStream(file)))
		{
			out.writeInt(port);
			out.writeInt(maxPlayers);
			out.writeBoolean(pvp);
			out.writeBoolean(online);
			out.close();
		} catch(Throwable err)
		{
		}
	}
	
	@SideOnly(Side.CLIENT)
	public static int getSuitableLanPort_Old() throws IOException
	{
		int i = -1;
		try(ServerSocket serversocket = new ServerSocket(0))
		{
			i = serversocket.getLocalPort();
		}
		return i;
	}
	
	@SideOnly(Side.CLIENT)
	public static int getSuitableLanPort() throws IOException
	{
		if(port > 0 && HammerCoreConfigs.CustomLANPortInstalled)
			return port;
		return getSuitableLanPort_Old();
	}
}