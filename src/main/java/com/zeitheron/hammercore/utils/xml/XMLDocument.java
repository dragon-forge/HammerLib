package com.zeitheron.hammercore.utils.xml;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Function;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;

public class XMLDocument
{
	final Document doc;
	
	final Map<String, XMLElement> elementsById = new HashMap<>();
	final Map<String, XMLElement[]> elementsByTagName = new HashMap<>();
	final Function<String, XMLElement> elementByIdNew;
	final Function<String, XMLElement[]> elementByTagNameNew;
	
	public XMLDocument(Document doc)
	{
		this.doc = doc;
		this.elementByIdNew = name -> new XMLElement(doc.getElementById(name));
		this.elementByTagNameNew = name ->
		{
			List<XMLElement> elems = new ArrayList<>();
			NodeList nl = doc.getElementsByTagName(name);
			for(int i = 0; i < nl.getLength(); ++i)
				elems.add(new XMLElement((Element) nl.item(i)));
			return elems.toArray(new XMLElement[elems.size()]);
		};
	}
	
	public XMLElement getElementById(String name)
	{
		return elementsById.computeIfAbsent(name, elementByIdNew);
	}
	
	public XMLElement[] getElementsByTagName(String name)
	{
		return elementsByTagName.computeIfAbsent(name, elementByTagNameNew);
	}
	
	public String getTextContent()
	{
		return doc.getTextContent();
	}
}