package com.zeitheron.hammercore.utils.xml;

import org.xml.sax.SAXException;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.function.Consumer;

public class XMLReader
{
	private static final DocumentBuilder XML_READER;

	static
	{
		DocumentBuilder builder = null;
		DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
		try
		{
			builder = factory.newDocumentBuilder();
		} catch(Throwable err)
		{
		}
		XML_READER = builder;
	}

	public static XMLDocument parse(File in) throws IOException, XMLException
	{
		if(in.isFile()) try
		{
			return new XMLDocument(XML_READER.parse(in));
		} catch(SAXException e)
		{
			throw new XMLException(e);
		}
		return new XMLDocument(XML_READER.newDocument());
	}

	public static XMLDocument parse(URL url) throws IOException, XMLException
	{
		return parse(url, null);
	}

	public static XMLDocument parse(URL url, Consumer<HttpURLConnection> connector) throws IOException, XMLException
	{
		HttpURLConnection connection = (HttpURLConnection) url.openConnection();
		if(connector != null)
			connector.accept(connection);
		if(connection.getResponseCode() != 200)
			throw new IOException("Unable to parse given url: CODE " + connection.getResponseCode() + " @ " + url.toString());
		try(InputStream in = connection.getInputStream())
		{
			return new XMLDocument(XML_READER.parse(in));
		} catch(SAXException e)
		{
			throw new XMLException(e);
		}
	}

	public static XMLDocument parse(String text) throws XMLException
	{
		try
		{
			return new XMLDocument(XML_READER.parse(new ByteArrayInputStream(text.getBytes())));
		} catch(SAXException e)
		{
			throw new XMLException(e);
		} catch(IOException ioe)
		{
			throw new XMLException(new SAXException("Unable to read from string... wtf?"));
		}
	}

	public static XMLDocument parse(InputStream in) throws IOException, XMLException
	{
		try
		{
			return new XMLDocument(XML_READER.parse(in));
		} catch(SAXException e)
		{
			throw new XMLException(e);
		}
	}
}