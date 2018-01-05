package com.endie.lib.weupnp;

import java.io.IOException;
import java.net.InetAddress;
import java.util.Iterator;
import java.util.Map;

import javax.xml.parsers.ParserConfigurationException;

import org.xml.sax.SAXException;

import com.endie.lib.weupnp.impl.GatewayDevice;
import com.endie.lib.weupnp.impl.GatewayDiscover;
import com.endie.lib.weupnp.impl.PortMappingEntry;
import com.pengu.hammercore.common.utils.WrappedLog;

public class WeUPnP
{
	GatewayDiscover gatewayDiscover;
	Map<InetAddress, GatewayDevice> gateways;
	GatewayDevice valid;
	ThreadLocal<PortMappingEntry> portMapping = ThreadLocal.withInitial(() -> new PortMappingEntry());
	
	public WeUPnP()
	{
	}
	
	public void setup()
	{
		gatewayDiscover = new GatewayDiscover();
	}
	
	public void discover() throws IOException, SAXException, ParserConfigurationException
	{
		gateways = gatewayDiscover.discover();
		
		Iterator<GatewayDevice> activeGW = gateways.values().iterator();
		
		while(activeGW.hasNext())
			activeGW.next();
		
		valid = gatewayDiscover.getValidGateway();
	}
	
	public void logFound(WrappedLog log)
	{
		if(gateways == null)
			throw new RuntimeException("WeUPnP#discover() wasn't called yet!");
		
		if(gateways.isEmpty())
		{
			log.error("No gateways found");
			log.error("Stopping weupnp");
		} else
		{
			log.info(gateways.size() + " gateway" + (gateways.size() != 1 ? "s" : "") + " found");
			int counter = 0;
			Iterator<GatewayDevice> activeGW = gateways.values().iterator();
			
			while(activeGW.hasNext())
			{
				GatewayDevice portMapCount = activeGW.next();
				++counter;
				log.info("Listing gateway details of device #" + counter + "\n\tFriendly name: " + portMapCount.getFriendlyName() + "\n\tPresentation URL: " + portMapCount.getPresentationURL() + "\n\tModel name: " + portMapCount.getModelName() + "\n\tModel number: " + portMapCount.getModelNumber() + "\n\tLocal interface address: " + portMapCount.getLocalAddress().getHostAddress() + (valid == portMapCount ? ("\n\t::This gateway is going to be used.") : ""));
			}
			
			if(valid == null)
				log.error("Failed to find valid gateway! weUPnP won't work!");
		}
	}
	
	public boolean isAttuned(EnumProtocol protocol, int extPort) throws IOException, SAXException
	{
		return valid.getSpecificPortMappingEntry(extPort, protocol.name(), portMapping.get());
	}
	
	public AttuneResult attune(EnumProtocol protocol, int intPort, int extPort, String desc) throws IOException, SAXException
	{
		return new AttuneResult(this, extPort, intPort, protocol, valid.addPortMapping(extPort, intPort, valid.getLocalAddress().getHostAddress(), protocol.name(), desc));
	}
	
	public boolean unattune(EnumProtocol protocol, int extPort) throws IOException, SAXException
	{
		return valid.deletePortMapping(extPort, protocol.name());
	}
}