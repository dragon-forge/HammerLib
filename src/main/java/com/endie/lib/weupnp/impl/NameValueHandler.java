package com.endie.lib.weupnp.impl;

import java.util.Map;

import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

public class NameValueHandler extends DefaultHandler
{
	private Map nameValue;
	private String currentElement;
	
	public NameValueHandler(Map nameValue)
	{
		this.nameValue = nameValue;
	}
	
	@Override
	public void startElement(String uri, String localName, String qName, Attributes attributes) throws SAXException
	{
		this.currentElement = localName;
	}
	
	@Override
	public void endElement(String uri, String localName, String qName) throws SAXException
	{
		this.currentElement = null;
	}
	
	@Override
	public void characters(char[] ch, int start, int length) throws SAXException
	{
		if(this.currentElement != null)
		{
			String value = new String(ch, start, length);
			String old = (String) this.nameValue.put(this.currentElement, value);
			if(old != null)
				this.nameValue.put(this.currentElement, old + value);
		}
	}
}