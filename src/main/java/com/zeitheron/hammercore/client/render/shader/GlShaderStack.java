package com.zeitheron.hammercore.client.render.shader;

import it.unimi.dsi.fastutil.ints.IntArrayList;
import net.minecraftforge.fml.relauncher.*;
import org.lwjgl.opengl.*;

@SideOnly(Side.CLIENT)
public class GlShaderStack
{
	private static final IntArrayList shaders = new IntArrayList();
	public static final IShaderStack pop = GlShaderStack::glsPopShader;
	
	public static int glsActiveProgram()
	{
		return GL11.glGetInteger(GL20.GL_CURRENT_PROGRAM);
	}
	
	public static int glsGetActiveUniformLoc(String name)
	{
		return GL20.glGetUniformLocation(glsActiveProgram(), name);
	}
	
	public static void clearStack()
	{
		shaders.clear();
	}
	
	public static IShaderStack glsPushShader()
	{
		shaders.push(glsActiveProgram());
		return pop;
	}
	
	public static void glsPopShader()
	{
		if(!shaders.isEmpty())
			GL20.glUseProgram(shaders.popInt());
		else
		{
			GL20.glUseProgram(0);
			System.out.println("GLShaderStack underflow!");
		}
	}
	
	@FunctionalInterface
	public interface IShaderStack
			extends AutoCloseable
	{
		@Override
		void close();
		
		default int id()
		{
			return glsActiveProgram();
		}
	}
}