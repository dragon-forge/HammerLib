package org.zeith.hammerlib.api.data;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Set;

public class DelegatingDataNode
		implements IDataNode
{
	protected final IDataNode node;
	
	public DelegatingDataNode(IDataNode node)
	{
		this.node = node;
	}
	
	@Override
	public @Nullable Object get(int index)
	{
		return node.get(index);
	}
	
	@Override
	public int length()
	{
		return node.length();
	}
	
	@Override
	public String getMyName()
	{
		return node.getMyName();
	}
	
	@Override
	public @Nullable Object get(String key)
	{
		return node.get(key);
	}
	
	@Override
	public @NotNull Set<String> keys()
	{
		return node.keys();
	}
}
