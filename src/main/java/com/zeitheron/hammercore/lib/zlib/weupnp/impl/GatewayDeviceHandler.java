package com.zeitheron.hammercore.lib.zlib.weupnp.impl;

import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

public class GatewayDeviceHandler extends DefaultHandler
{
	private GatewayDevice device;
	private String currentElement;
	public int level = 0;
	private short state = 0;
	
	public GatewayDeviceHandler(GatewayDevice device)
	{
		this.device = device;
	}
	
	public void startElement(String uri, String localName, String qName, Attributes attributes) throws SAXException
	{
		this.currentElement = localName;
		++this.level;
		if(this.state < 1 && "serviceList".compareTo(this.currentElement) == 0)
		{
			this.state = 1;
		}
		
	}
	
	public void endElement(String uri, String localName, String qName) throws SAXException
	{
		this.currentElement = "";
		--this.level;
		if(localName.compareTo("service") == 0)
		{
			if(this.device.getServiceTypeCIF() != null && this.device.getServiceTypeCIF().compareTo("urn:schemas-upnp-org:service:WANCommonInterfaceConfig:1") == 0)
			{
				this.state = 2;
			}
			
			if(this.device.getServiceType() != null && (this.device.getServiceType().contains("urn:schemas-upnp-org:service:WANIPConnection:") || this.device.getServiceType().contains("urn:schemas-upnp-org:service:WANPPPConnection:")))
			{
				this.state = 3;
			}
		}
		
	}
	
	public void characters(char[] ch, int start, int length) throws SAXException
	{
		if(this.currentElement.compareTo("URLBase") == 0)
		{
			this.device.setURLBase(new String(ch, start, length));
		} else if(this.state <= 1)
		{
			if(this.state == 0)
			{
				if("friendlyName".compareTo(this.currentElement) == 0)
				{
					this.device.setFriendlyName(new String(ch, start, length));
				} else if("manufacturer".compareTo(this.currentElement) == 0)
				{
					this.device.setManufacturer(new String(ch, start, length));
				} else if("modelDescription".compareTo(this.currentElement) == 0)
				{
					this.device.setModelDescription(new String(ch, start, length));
				} else if("presentationURL".compareTo(this.currentElement) == 0)
				{
					this.device.setPresentationURL(new String(ch, start, length));
				} else if("modelNumber".compareTo(this.currentElement) == 0)
				{
					this.device.setModelNumber(new String(ch, start, length));
				} else if("modelName".compareTo(this.currentElement) == 0)
				{
					this.device.setModelName(new String(ch, start, length));
				}
			}
			
			if(this.currentElement.compareTo("serviceType") == 0)
			{
				this.device.setServiceTypeCIF(new String(ch, start, length));
			} else if(this.currentElement.compareTo("controlURL") == 0)
			{
				this.device.setControlURLCIF(new String(ch, start, length));
			} else if(this.currentElement.compareTo("eventSubURL") == 0)
			{
				this.device.setEventSubURLCIF(new String(ch, start, length));
			} else if(this.currentElement.compareTo("SCPDURL") == 0)
			{
				this.device.setSCPDURLCIF(new String(ch, start, length));
			} else if(this.currentElement.compareTo("deviceType") == 0)
			{
				this.device.setDeviceTypeCIF(new String(ch, start, length));
			}
		} else if(this.state == 2)
		{
			if(this.currentElement.compareTo("serviceType") == 0)
			{
				this.device.setServiceType(new String(ch, start, length));
			} else if(this.currentElement.compareTo("controlURL") == 0)
			{
				this.device.setControlURL(new String(ch, start, length));
			} else if(this.currentElement.compareTo("eventSubURL") == 0)
			{
				this.device.setEventSubURL(new String(ch, start, length));
			} else if(this.currentElement.compareTo("SCPDURL") == 0)
			{
				this.device.setSCPDURL(new String(ch, start, length));
			} else if(this.currentElement.compareTo("deviceType") == 0)
			{
				this.device.setDeviceType(new String(ch, start, length));
			}
		}
		
	}
}
