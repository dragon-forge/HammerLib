package org.zeith.hammerlib.util.java.io.win32;

import java.io.*;
import java.util.*;

public class ZoneIdentifier
{
	public int zoneId;
	public String referrerUrl;
	public String hostUrl;
	
	@Override
	public String toString()
	{
		return "ZoneIdentifier{\"ZoneId\":" + zoneId + ",\"ReferrerUrl\":\"" + referrerUrl + "\",\"HostUrl\":\"" + hostUrl + "\"}";
	}
	
	public void read(BufferedReader reader) throws IOException
	{
		Map<String, String> strs = new HashMap<>(3);
		
		String ln = reader.readLine();
		
		if(!ln.equals("[ZoneTransfer]"))
		{
			throw new IllegalArgumentException("Invalid format");
		}
		
		while((ln = reader.readLine()) != null)
		{
			if(!ln.contains("="))
				continue;
			String[] ln0 = ln.split("=", 2);
			strs.put(ln0[0], ln0[1]);
		}
		
		zoneId = Integer.parseInt(strs.getOrDefault("ZoneId", "3"));
		referrerUrl = strs.get("ReferrerUrl");
		hostUrl = strs.get("HostUrl");
	}
	
	public void write(BufferedWriter writer) throws IOException
	{
		Map<String, String> strs = new HashMap<>(3);
		strs.put("ZoneId", Integer.toString(zoneId));
		strs.put("ReferrerUrl", referrerUrl);
		strs.put("HostUrl", hostUrl);
		
		writer.write("[ZoneTransfer]");
		writer.newLine();
		int written = strs.size();
		for(String key : strs.keySet())
		{
			writer.write(key + "=" + strs.get(key));
			if(--written > 0)
				writer.newLine();
		}
	}
	
	public static ZoneIdentifierFile getZoneIdentifierFile(File file)
	{
		if(file instanceof ZoneIdentifierFile)
			return (ZoneIdentifierFile) file;
		String f = file.getAbsolutePath();
		if(f.indexOf(':', 3) != -1)
			f = f.substring(0, f.lastIndexOf(':'));
		return new ZoneIdentifierFile(file, f + ":Zone.Identifier");
	}
	
	public static boolean createInternetDownloadFor(File file, String referrerUrl, String hostUrl) throws IOException
	{
		if(file.isFile())
		{
			ZoneIdentifierFile zif = getZoneIdentifierFile(file);
			if(zif != null)
			{
				ZoneIdentifier id = new ZoneIdentifier();
				return zif.set(id);
			}
		}
		return false;
	}
	
	public static Optional<ZoneIdentifier> forFile(File file) throws IOException
	{
		ZoneIdentifierFile zif = getZoneIdentifierFile(file);
		if(zif.isFile())
		{
			try(BufferedReader bf = new BufferedReader(new FileReader(zif)))
			{
				ZoneIdentifier zi = new ZoneIdentifier();
				zi.read(bf);
				return Optional.of(zi);
			}
		}
		return Optional.empty();
	}
	
	public static Optional<ZoneIdentifier> forFileSafe(File file)
	{
		try
		{
			return forFile(file);
		} catch(Throwable ignored)
		{
		}
		
		return Optional.empty();
	}
}