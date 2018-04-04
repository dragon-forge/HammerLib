package com.pengu.hammercore.core.blocks.tesseract;

public enum TransferMode
{
	ALLOW, DECLINE;
	
	public static TransferMode fromByte(byte val)
	{
		return values()[val % values().length];
	}
	
	public byte asByte()
	{
		return (byte) ordinal();
	}
	
	public boolean active()
	{
		return this == ALLOW;
	}
}