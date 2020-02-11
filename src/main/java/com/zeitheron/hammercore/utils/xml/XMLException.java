package com.zeitheron.hammercore.utils.xml;

import org.xml.sax.SAXException;

public class XMLException extends RuntimeException
{
	private static final long serialVersionUID = -3291413568729345580L;

	public XMLException(SAXException err)
	{
		super(err);
	}
}