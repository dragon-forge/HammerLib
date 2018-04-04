package com.pengu.hammercore.proxy;

import org.lwjgl.input.Keyboard;

import net.minecraft.client.Minecraft;

public class NativeProxy_Client extends NativeProxy_Common
{
	@Override
	public boolean npCallb(int pointer, int... params)
	{
		switch(pointer)
		{
		case 0x001:
			return isKeyPressed(params);
		default:
			throw new IllegalArgumentException("Invalid pointer key");
		}
	}
	
	private boolean isKeyPressed(int... params)
	{
		return Keyboard.isKeyDown(params[0]);
	}
}