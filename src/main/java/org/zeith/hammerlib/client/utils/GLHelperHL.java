package org.zeith.hammerlib.client.utils;

import com.mojang.blaze3d.systems.RenderSystem;
import it.unimi.dsi.fastutil.ints.IntArrayList;
import it.unimi.dsi.fastutil.ints.IntStack;
import net.minecraft.client.Minecraft;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL20;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;
import java.util.function.Supplier;

public class GLHelperHL
{
	private static final IntStack shaders = new IntArrayList();
	private static final IntStack textures = new IntArrayList();

	public static final IShaderStack popShader = GLHelperHL::glPopShader;
	public static final ITextureStack popTexture = GLHelperHL::glPopTexture;

	public static int activeShaderProgram()
	{
		return GL11.glGetInteger(GL20.GL_CURRENT_PROGRAM);
	}

	public static int getActiveUniformLoc(String name)
	{
		return GL20.glGetUniformLocation(activeShaderProgram(), name);
	}

	public static int activeTexture()
	{
		return GL11.glGetInteger(GL11.GL_TEXTURE_BINDING_2D);
	}

	public static int activeTextureWidth()
	{
		return GL11.glGetTexLevelParameteri(GL11.GL_TEXTURE_2D, 0, GL11.GL_TEXTURE_WIDTH);
	}

	public static int activeTextureHeight()
	{
		return GL11.glGetTexLevelParameteri(GL11.GL_TEXTURE_2D, 0, GL11.GL_TEXTURE_HEIGHT);
	}

	public static void glTask(Runnable task)
	{
		if(RenderSystem.isOnRenderThread())
			task.run();
		else
			Minecraft.getInstance().execute(task);
	}

	public static <T> T glTask(Supplier<T> task)
	{
		if(RenderSystem.isOnRenderThread())
			return task.get();
		else try
		{
			return CompletableFuture.supplyAsync(task, Minecraft.getInstance()).get();
		} catch(InterruptedException | ExecutionException e)
		{
			throw new RuntimeException(e);
		}
	}

	public static void glTaskAsync(Runnable task)
	{
		Minecraft.getInstance().execute(task);
	}

	public static IShaderStack glPushShader()
	{
		shaders.push(activeShaderProgram());
		return popShader;
	}

	public static ITextureStack glPushTexture()
	{
		textures.push(activeTexture());
		return popTexture;
	}

	public static void glPopShader()
	{
		if(!shaders.isEmpty()) GL20.glUseProgram(shaders.popInt());
		else
		{
			GL20.glUseProgram(0);
			System.out.println("GLShaderStack underflow!");
		}
	}

	public static void glPopTexture()
	{
		if(!textures.isEmpty()) GL11.glBindTexture(GL11.GL_TEXTURE_2D, textures.popInt());
		else
		{
			GL11.glBindTexture(GL11.GL_TEXTURE_2D, 0);
			System.out.println("GLTextureStack underflow!");
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
			return activeShaderProgram();
		}
	}

	@FunctionalInterface
	public interface ITextureStack
			extends AutoCloseable
	{
		@Override
		void close();

		default int id()
		{
			return activeTexture();
		}
	}
}