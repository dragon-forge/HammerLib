package org.zeith.api.blocks.redstone;

import org.jetbrains.annotations.Range;

public class SimpleRedstoneBundle
		implements IRedstoneBundle
{
	public boolean connected = true;
	public int serial;
	
	@Override
	public boolean isConnected()
	{
		return connected;
	}
	
	@Override
	public boolean setSerializedBundleSignal(@Range(from = 0, to = 65535) int serialized)
	{
		serial = serialized;
		return true;
	}
	
	@Override
	public @Range(from = 0, to = 65535) int getSerializedBundleSignal()
	{
		return serial;
	}
}