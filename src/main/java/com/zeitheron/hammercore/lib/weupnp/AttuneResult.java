package com.zeitheron.hammercore.lib.weupnp;

import java.io.IOException;
import java.util.Map;

import org.xml.sax.SAXException;

public class AttuneResult
{
	private WeUPnP client;
	private int extPort, intPort;
	private EnumProtocol protocol;
	private Map<String, Object> result;
	
	public AttuneResult(WeUPnP client, int extPort, int intPort, EnumProtocol protocol, Map<String, Object> result)
	{
		this.client = client;
		this.extPort = extPort;
		this.protocol = protocol;
		this.result = result;
		this.intPort = intPort;
	}
	
	public WeUPnP getClient()
	{
		return client;
	}
	
	public int getInternalPort()
	{
		return intPort;
	}
	
	public int getExternalPort()
	{
		return extPort;
	}
	
	public EnumProtocol getProtocol()
	{
		return protocol;
	}
	
	public Map<String, Object> getResult()
	{
		return result;
	}
	
	public String getError()
	{
		return result.get("errorCode") + "";
	}
	
	public boolean isSuccessful()
	{
		return result.get("errorCode") == null;
	}
	
	public boolean undo() throws IOException, SAXException
	{
		return client.unattune(protocol, extPort);
	}
}