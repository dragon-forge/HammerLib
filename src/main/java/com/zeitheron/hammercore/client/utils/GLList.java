package com.zeitheron.hammercore.client.utils;

import com.zeitheron.hammercore.client.utils.rendering.ISimpleRenderable;
import org.lwjgl.opengl.GL11;

import javax.annotation.Nullable;
import java.util.Map;

public class GLList
{
	public final int list;

	public GLList()
	{
		list = RenderUtil.glTask(() -> GL11.glGenLists(2));
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
		RenderUtil.glTask(() -> GL11.glDeleteLists(list, 2));
	}

	public void store(ISimpleRenderable renderable, @Nullable Map<String, Object> properties)
	{
		RenderUtil.glTask(() ->
		{
			start();
			renderable.render(properties);
			end();
		});
	}
}