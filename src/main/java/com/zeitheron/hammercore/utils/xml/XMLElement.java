package com.zeitheron.hammercore.utils.xml;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Function;

import org.w3c.dom.Element;
import org.w3c.dom.NodeList;

public class XMLElement
{
	final Element elem;
	
	final Map<String, XMLElement[]> elementsByTagName = new HashMap<>();
	final Function<String, XMLElement[]> elementByTagNameNew;
	
	public XMLElement(Element elem)
	{
		this.elem = elem;
		
		this.elementByTagNameNew = name ->
		{
			List<XMLElement> elems = new ArrayList<>();
			NodeList nl = elem.getElementsByTagName(name);
			for(int i = 0; i < nl.getLength(); ++i)
				elems.add(new XMLElement((Element) nl.item(i)));
			return elems.toArray(new XMLElement[elems.size()]);
		};
	}
	
	public String getAttribute(String name)
	{
		return elem.getAttribute(name);
	}
	
	public String getTextContent()
	{
		return elem.getTextContent();
	}
	
	public XMLElement[] getElementsByTagName(String name)
	{
		return elementsByTagName.computeIfAbsent(name, elementByTagNameNew);
	}
}