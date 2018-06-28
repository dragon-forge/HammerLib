package com.zeitheron.hammercore.lib.zlib.weupnp.impl;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.InetAddress;
import java.net.URL;
import java.net.URLConnection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import org.xml.sax.InputSource;
import org.xml.sax.SAXException;
import org.xml.sax.XMLReader;
import org.xml.sax.helpers.XMLReaderFactory;

@SuppressWarnings({ "rawtypes", "unchecked" })
public class GatewayDevice
{
	private String st;
	private String location;
	private String serviceType;
	private String serviceTypeCIF;
	private String urlBase;
	private String controlURL;
	private String controlURLCIF;
	private String eventSubURL;
	private String eventSubURLCIF;
	private String sCPDURL;
	private String sCPDURLCIF;
	private String deviceType;
	private String deviceTypeCIF;
	private String friendlyName;
	private String manufacturer;
	private String modelDescription;
	private String presentationURL;
	private InetAddress localAddress;
	private String modelNumber;
	private String modelName;
	private static int httpReadTimeout = 7000;
	
	public void loadDescription() throws SAXException, IOException
	{
		URLConnection urlConn = (new URL(this.getLocation())).openConnection();
		urlConn.setReadTimeout(httpReadTimeout);
		XMLReader parser = XMLReaderFactory.createXMLReader();
		parser.setContentHandler(new GatewayDeviceHandler(this));
		parser.parse(new InputSource(urlConn.getInputStream()));
		String ipConDescURL;
		if(this.urlBase != null && this.urlBase.trim().length() > 0)
		{
			ipConDescURL = this.urlBase;
		} else
		{
			ipConDescURL = this.location;
		}
		
		int lastSlashIndex = ipConDescURL.indexOf(47, 7);
		if(lastSlashIndex > 0)
		{
			ipConDescURL = ipConDescURL.substring(0, lastSlashIndex);
		}
		
		this.sCPDURL = this.copyOrCatUrl(ipConDescURL, this.sCPDURL);
		this.controlURL = this.copyOrCatUrl(ipConDescURL, this.controlURL);
		this.controlURLCIF = this.copyOrCatUrl(ipConDescURL, this.controlURLCIF);
		this.presentationURL = this.copyOrCatUrl(ipConDescURL, this.presentationURL);
	}
	
	public static Map<String, Object> simpleUPnPcommand(String url, String service, String action, Map args) throws IOException, SAXException
	{
		String soapAction = "\"" + service + "#" + action + "\"";
		StringBuilder soapBody = new StringBuilder();
		soapBody.append("<?xml version=\"1.0\"?>\r\n<SOAP-ENV:Envelope xmlns:SOAP-ENV=\"http://schemas.xmlsoap.org/soap/envelope/\" SOAP-ENV:encodingStyle=\"http://schemas.xmlsoap.org/soap/encoding/\"><SOAP-ENV:Body><m:" + action + " xmlns:m=\"" + service + "\">");
		if(args != null && args.size() > 0)
		{
			Set postUrl = args.entrySet();
			Iterator conn = postUrl.iterator();
			
			while(conn.hasNext())
			{
				Entry soapBodyBytes = (Entry) conn.next();
				soapBody.append("<" + (String) soapBodyBytes.getKey() + ">" + (String) soapBodyBytes.getValue() + "</" + (String) soapBodyBytes.getKey() + ">");
			}
		}
		
		soapBody.append("</m:" + action + ">");
		soapBody.append("</SOAP-ENV:Body></SOAP-ENV:Envelope>");
		URL postUrl1 = new URL(url);
		HttpURLConnection conn1 = (HttpURLConnection) postUrl1.openConnection();
		conn1.setRequestMethod("POST");
		conn1.setConnectTimeout(httpReadTimeout);
		conn1.setReadTimeout(httpReadTimeout);
		conn1.setDoOutput(true);
		conn1.setRequestProperty("Content-Type", "text/xml");
		conn1.setRequestProperty("SOAPAction", soapAction);
		conn1.setRequestProperty("Connection", "Close");
		byte[] soapBodyBytes1 = soapBody.toString().getBytes();
		conn1.setRequestProperty("Content-Length", String.valueOf(soapBodyBytes1.length));
		conn1.getOutputStream().write(soapBodyBytes1);
		HashMap<String, Object> nameValue = new HashMap();
		XMLReader parser = XMLReaderFactory.createXMLReader();
		parser.setContentHandler(new NameValueHandler(nameValue));
		if(conn1.getResponseCode() == 500)
		{
			try
			{
				parser.parse(new InputSource(conn1.getErrorStream()));
			} catch(SAXException var12)
			{
				;
			}
			
			conn1.disconnect();
			return nameValue;
		} else
		{
			parser.parse(new InputSource(conn1.getInputStream()));
			conn1.disconnect();
			return nameValue;
		}
	}
	
	public boolean isConnected() throws IOException, SAXException
	{
		Map nameValue = simpleUPnPcommand(this.controlURL, this.serviceType, "GetStatusInfo", (Map) null);
		String connectionStatus = (String) nameValue.get("NewConnectionStatus");
		return connectionStatus != null && connectionStatus.equalsIgnoreCase("Connected");
	}
	
	public String getExternalIPAddress() throws IOException, SAXException
	{
		Map nameValue = simpleUPnPcommand(this.controlURL, this.serviceType, "GetExternalIPAddress", (Map) null);
		return (String) nameValue.get("NewExternalIPAddress");
	}
	
	public Map<String, Object> addPortMapping(int externalPort, int internalPort, String internalClient, String protocol, String description) throws IOException, SAXException
	{
		LinkedHashMap<String, Object> args = new LinkedHashMap<>();
		args.put("NewRemoteHost", "");
		args.put("NewExternalPort", Integer.toString(externalPort));
		args.put("NewProtocol", protocol);
		args.put("NewInternalPort", Integer.toString(internalPort));
		args.put("NewInternalClient", internalClient);
		args.put("NewEnabled", Integer.toString(1));
		args.put("NewPortMappingDescription", description);
		args.put("NewLeaseDuration", Integer.toString(0));
		Map<String, Object> nameValue = simpleUPnPcommand(this.controlURL, this.serviceType, "AddPortMapping", args);
		return nameValue;
	}
	
	public boolean getSpecificPortMappingEntry(int externalPort, String protocol, PortMappingEntry portMappingEntry) throws IOException, SAXException
	{
		portMappingEntry.setExternalPort(externalPort);
		portMappingEntry.setProtocol(protocol);
		LinkedHashMap args = new LinkedHashMap();
		args.put("NewRemoteHost", "");
		args.put("NewExternalPort", Integer.toString(externalPort));
		args.put("NewProtocol", protocol);
		Map nameValue = simpleUPnPcommand(this.controlURL, this.serviceType, "GetSpecificPortMappingEntry", args);
		if(!nameValue.isEmpty() && !nameValue.containsKey("errorCode"))
		{
			if(nameValue.containsKey("NewInternalClient") && nameValue.containsKey("NewInternalPort"))
			{
				portMappingEntry.setProtocol(protocol);
				portMappingEntry.setEnabled((String) nameValue.get("NewEnabled"));
				portMappingEntry.setInternalClient((String) nameValue.get("NewInternalClient"));
				portMappingEntry.setExternalPort(externalPort);
				portMappingEntry.setPortMappingDescription((String) nameValue.get("NewPortMappingDescription"));
				portMappingEntry.setRemoteHost((String) nameValue.get("NewRemoteHost"));
				
				try
				{
					portMappingEntry.setInternalPort(Integer.parseInt((String) nameValue.get("NewInternalPort")));
				} catch(NumberFormatException var7)
				{
					;
				}
				
				return true;
			} else
			{
				return false;
			}
		} else
		{
			return false;
		}
	}
	
	public boolean getGenericPortMappingEntry(int index, PortMappingEntry portMappingEntry) throws IOException, SAXException
	{
		LinkedHashMap args = new LinkedHashMap();
		args.put("NewPortMappingIndex", Integer.toString(index));
		Map nameValue = simpleUPnPcommand(this.controlURL, this.serviceType, "GetGenericPortMappingEntry", args);
		if(!nameValue.isEmpty() && !nameValue.containsKey("errorCode"))
		{
			portMappingEntry.setRemoteHost((String) nameValue.get("NewRemoteHost"));
			portMappingEntry.setInternalClient((String) nameValue.get("NewInternalClient"));
			portMappingEntry.setProtocol((String) nameValue.get("NewProtocol"));
			portMappingEntry.setEnabled((String) nameValue.get("NewEnabled"));
			portMappingEntry.setPortMappingDescription((String) nameValue.get("NewPortMappingDescription"));
			
			try
			{
				portMappingEntry.setInternalPort(Integer.parseInt((String) nameValue.get("NewInternalPort")));
			} catch(Exception var7)
			{
				;
			}
			
			try
			{
				portMappingEntry.setExternalPort(Integer.parseInt((String) nameValue.get("NewExternalPort")));
			} catch(Exception var6)
			{
				;
			}
			
			return true;
		} else
		{
			return false;
		}
	}
	
	public Integer getPortMappingNumberOfEntries() throws IOException, SAXException
	{
		Map nameValue = simpleUPnPcommand(this.controlURL, this.serviceType, "GetPortMappingNumberOfEntries", (Map) null);
		Integer portMappingNumber = null;
		
		try
		{
			portMappingNumber = Integer.valueOf((String) nameValue.get("NewPortMappingNumberOfEntries"));
		} catch(Exception var4)
		{
			;
		}
		
		return portMappingNumber;
	}
	
	public boolean deletePortMapping(int externalPort, String protocol) throws IOException, SAXException
	{
		LinkedHashMap args = new LinkedHashMap();
		args.put("NewRemoteHost", "");
		args.put("NewExternalPort", Integer.toString(externalPort));
		args.put("NewProtocol", protocol);
		simpleUPnPcommand(this.controlURL, this.serviceType, "DeletePortMapping", args);
		return true;
	}
	
	public InetAddress getLocalAddress()
	{
		return this.localAddress;
	}
	
	public void setLocalAddress(InetAddress localAddress)
	{
		this.localAddress = localAddress;
	}
	
	public String getSt()
	{
		return this.st;
	}
	
	public void setSt(String st)
	{
		this.st = st;
	}
	
	public String getLocation()
	{
		return this.location;
	}
	
	public void setLocation(String location)
	{
		this.location = location;
	}
	
	public String getServiceType()
	{
		return this.serviceType;
	}
	
	public void setServiceType(String serviceType)
	{
		this.serviceType = serviceType;
	}
	
	public String getServiceTypeCIF()
	{
		return this.serviceTypeCIF;
	}
	
	public void setServiceTypeCIF(String serviceTypeCIF)
	{
		this.serviceTypeCIF = serviceTypeCIF;
	}
	
	public String getControlURL()
	{
		return this.controlURL;
	}
	
	public void setControlURL(String controlURL)
	{
		this.controlURL = controlURL;
	}
	
	public String getControlURLCIF()
	{
		return this.controlURLCIF;
	}
	
	public void setControlURLCIF(String controlURLCIF)
	{
		this.controlURLCIF = controlURLCIF;
	}
	
	public String getEventSubURL()
	{
		return this.eventSubURL;
	}
	
	public void setEventSubURL(String eventSubURL)
	{
		this.eventSubURL = eventSubURL;
	}
	
	public String getEventSubURLCIF()
	{
		return this.eventSubURLCIF;
	}
	
	public void setEventSubURLCIF(String eventSubURLCIF)
	{
		this.eventSubURLCIF = eventSubURLCIF;
	}
	
	public String getSCPDURL()
	{
		return this.sCPDURL;
	}
	
	public void setSCPDURL(String sCPDURL)
	{
		this.sCPDURL = sCPDURL;
	}
	
	public String getSCPDURLCIF()
	{
		return this.sCPDURLCIF;
	}
	
	public void setSCPDURLCIF(String sCPDURLCIF)
	{
		this.sCPDURLCIF = sCPDURLCIF;
	}
	
	public String getDeviceType()
	{
		return this.deviceType;
	}
	
	public void setDeviceType(String deviceType)
	{
		this.deviceType = deviceType;
	}
	
	public String getDeviceTypeCIF()
	{
		return this.deviceTypeCIF;
	}
	
	public void setDeviceTypeCIF(String deviceTypeCIF)
	{
		this.deviceTypeCIF = deviceTypeCIF;
	}
	
	public String getURLBase()
	{
		return this.urlBase;
	}
	
	public void setURLBase(String uRLBase)
	{
		this.urlBase = uRLBase;
	}
	
	public String getFriendlyName()
	{
		return this.friendlyName;
	}
	
	public void setFriendlyName(String friendlyName)
	{
		this.friendlyName = friendlyName;
	}
	
	public String getManufacturer()
	{
		return this.manufacturer;
	}
	
	public void setManufacturer(String manufacturer)
	{
		this.manufacturer = manufacturer;
	}
	
	public String getModelDescription()
	{
		return this.modelDescription;
	}
	
	public void setModelDescription(String modelDescription)
	{
		this.modelDescription = modelDescription;
	}
	
	public String getPresentationURL()
	{
		return this.presentationURL;
	}
	
	public void setPresentationURL(String presentationURL)
	{
		this.presentationURL = presentationURL;
	}
	
	public String getModelName()
	{
		return this.modelName;
	}
	
	public void setModelName(String modelName)
	{
		this.modelName = modelName;
	}
	
	public String getModelNumber()
	{
		return this.modelNumber;
	}
	
	public void setModelNumber(String modelNumber)
	{
		this.modelNumber = modelNumber;
	}
	
	public static int getHttpReadTimeout()
	{
		return httpReadTimeout;
	}
	
	public static void setHttpReadTimeout(int milliseconds)
	{
		httpReadTimeout = milliseconds;
	}
	
	private String copyOrCatUrl(String dst, String src)
	{
		if(src != null)
		{
			if(src.startsWith("http://"))
			{
				dst = src;
			} else
			{
				if(!src.startsWith("/"))
				{
					dst = dst + "/";
				}
				
				dst = dst + src;
			}
		}
		
		return dst;
	}
	
}
