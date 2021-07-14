package org.zeith.hammerlib.client.utils;

import org.lwjgl.opengl.GL11;

public class GLList
{
	public final int list;

	public GLList()
	{
		list = GL11.glGenLists(2);
	}

	public void start()
	{
		GL11.glNewList(list, 4864);
	}

	public void end()
	{
		GL11.glEndList();
	}

	public void render()
	{
		GL11.glCallList(list);
	}

	public void delete()
	{
		GL11.glDeleteLists(list, 2);
	}
}