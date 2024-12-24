package org.zeith.hammerlib.api.data;

import net.minecraft.resources.ResourceLocation;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.HashSet;
import java.util.Set;

public class DataNodeTransformer
{
	public static IDataNode mergeAttributesFromParent(IDataNode parent, IDataNode child)
	{
		return new DelegatingDataNode(child)
		{
			@Override
			public @Nullable Object get(String key)
			{
				var fromParent = parent.get(key);
				if(fromParent != null) return fromParent;
				return super.get(key);
			}
			
			@Override
			public @NotNull Set<String> keys()
			{
				Set<String> child = new HashSet<>(super.keys());
				child.addAll(parent.keys());
				return child;
			}
		};
	}
	
	public static IDataNode convertToComponent(IDataNode node, ResourceLocation comType)
	{
		String compName = comType.toString();
		return new DelegatingDataNode(node)
		{
			@Override
			public String getMyName()
			{
				return "com";
			}
			
			@Override
			public @Nullable Object get(String key)
			{
				if("class".equals(key)) return compName;
				return super.get(key);
			}
		};
	}
	
	public static IDataNode changeMyName(IDataNode node, String newName)
	{
		return new DelegatingDataNode(node)
		{
			@Override
			public String getMyName()
			{
				return newName;
			}
		};
	}
	
	public static IDataNode replaceProperty(IDataNode node, String srcKey, Object value)
	{
		return new DelegatingDataNode(node)
		{
			@Override
			public @Nullable Object get(String key)
			{
				if(srcKey.equals(key)) return value;
				return super.get(key);
			}
		};
	}
}