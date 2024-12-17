package org.zeith.hammerlib.util.data;

import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.xml.sax.SAXException;

import javax.xml.parsers.*;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.nio.charset.StandardCharsets;

public class XmlHelper
{
	private static final DocumentBuilderFactory DOCUMENT_BUILDER_FACTORY = DocumentBuilderFactory.newInstance();
	
	static
	{
		DOCUMENT_BUILDER_FACTORY.setIgnoringComments(true);
	}
	
	public static XmlNode parse(String input)
			throws ParserConfigurationException, IOException, SAXException
	{
		if(input == null || input.isBlank()) return null;
		DocumentBuilder dBuilder = DOCUMENT_BUILDER_FACTORY.newDocumentBuilder();
		Document doc = dBuilder.parse(new ByteArrayInputStream(input.getBytes(StandardCharsets.UTF_8)));
		doc.getDocumentElement().normalize();
		return toTree(doc.getFirstChild());
	}
	
	public static XmlNode toTree(Node obj)
	{
		return new XmlNode(obj);
	}
}