package org.zeith.hammerlib.util.data;

import lombok.ToString;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;
import org.zeith.hammerlib.api.data.IDataNode;

import java.util.*;

@ToString
public class XmlNode
		implements IDataNode
{
	protected final Map<String, String> attributes;
	protected final List<XmlNode> children;
	protected final String myName;
	
	public XmlNode(Node domNode)
	{
		this.myName = domNode.getNodeName();
		
		Map<String, String> attributes = new HashMap<>();
		
		// Attribute parsing
		if(domNode.hasAttributes())
		{
			NamedNodeMap attr = domNode.getAttributes();
			for(int i = 0, len = attr.getLength(); i < len; i++)
			{
				var node = attr.item(i);
				attributes.put(node.getNodeName(), node.getNodeValue());
			}
		}
		
		var text = domNode.getTextContent().trim().replace('\t', ' ');
		if(!text.isBlank()) attributes.put("#text", text);
		this.attributes = Map.copyOf(attributes);
		
		List<XmlNode> children = new ArrayList<>();
		var childNodes = domNode.getChildNodes();
		for(int i = 0, len = childNodes.getLength(); i < len; i++)
		{
			var node = childNodes.item(i);
			if("#text".equals(node.getNodeName())) continue;
			children.add(new XmlNode(node));
		}
		this.children = List.copyOf(children);
	}
	
	@Override
	public @Nullable Object get(int index)
	{
		return children.get(index);
	}
	
	@Override
	public int length()
	{
		return children.size();
	}
	
	@Override
	public @Nullable Object get(String key)
	{
		return attributes.get(key);
	}
	
	@Override
	public @NotNull Set<String> keys()
	{
		return attributes.keySet();
	}
	
	@Override
	public String getMyName()
	{
		return myName;
	}
}
