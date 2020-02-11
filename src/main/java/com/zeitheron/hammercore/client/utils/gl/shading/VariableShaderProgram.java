package com.zeitheron.hammercore.client.utils.gl.shading;

import com.zeitheron.hammercore.HammerCore;
import com.zeitheron.hammercore.api.events.ResourceManagerReloadEvent;
import com.zeitheron.hammercore.client.render.shader.GlShaderStack;
import com.zeitheron.hammercore.client.utils.gl.GLBuffer;
import it.unimi.dsi.fastutil.ints.Int2ObjectArrayMap;
import it.unimi.dsi.fastutil.ints.IntArrayList;
import it.unimi.dsi.fastutil.ints.IntList;
import it.unimi.dsi.fastutil.objects.Object2IntArrayMap;
import net.minecraft.client.Minecraft;
import net.minecraftforge.client.resource.VanillaResourceType;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import org.lwjgl.opengl.ARBShaderObjects;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL20;
import org.lwjgl.opengl.GL32;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;

/**
 * Represents a constant type for shader program to be used in any desired way.
 * You may add variables to it to make constants a variable in glsl.
 * This program reloads alongside minecraft shader reload event, too.
 */
@SideOnly(Side.CLIENT)
@Mod.EventBusSubscriber(Side.CLIENT)
public class VariableShaderProgram
{
	private static final List<VariableShaderProgram> PROGRAMS = new ArrayList<>();
	private final Int2ObjectArrayMap<ShaderSource> sources = new Int2ObjectArrayMap<>();
	private final List<ShaderVar> variables = new ArrayList<>();
	private final Object2IntArrayMap<String> uniformCache = new Object2IntArrayMap<>();
	private final List<Consumer<VariableShaderProgram>> onBind = new ArrayList<>();
	private Integer program;

	public VariableShaderProgram subscribe4Events()
	{
		if(!PROGRAMS.contains(this)) PROGRAMS.add(this);
		return this;
	}

	public VariableShaderProgram addVariable(ShaderVar var)
	{
		var.setProgram(this);
		variables.add(var);
		return this;
	}

	public VariableShaderProgram onBind(Consumer<VariableShaderProgram> onBind)
	{
		this.onBind.add(onBind);
		return this;
	}

	public VariableShaderProgram linkGeometrySource(ShaderSource src)
	{
		return linkSource(GL32.GL_GEOMETRY_SHADER, src);
	}

	public VariableShaderProgram linkVertexSource(ShaderSource src)
	{
		return linkSource(GL20.GL_VERTEX_SHADER, src);
	}

	public VariableShaderProgram linkFragmentSource(ShaderSource src)
	{
		return linkSource(GL20.GL_FRAGMENT_SHADER, src);
	}

	public VariableShaderProgram linkSource(int type, ShaderSource src)
	{
		sources.put(type, src);
		return this;
	}

	protected void createProgram()
	{
		if(program != null) GL20.glDeleteProgram(program);
		uniformCache.clear();
		program = GL20.glCreateProgram();
		IntList shaders = new IntArrayList();
		for(int key : sources.keySet())
		{
			int shader = GL20.glCreateShader(key);
			if(shader == 0) continue;
			GL20.glShaderSource(shader, sources.get(key).read(variables));
			GL20.glCompileShader(shader);
			if(GL20.glGetShaderi(shader, GL20.GL_COMPILE_STATUS) == GL11.GL_FALSE)
			{
				HammerCore.LOG.error("Error: failed to load shader(" + Integer.toHexString(key) + ") source: " + sources.get(key));
				HammerCore.LOG.error("****  " + GL20.glGetProgramInfoLog(shader, ARBShaderObjects.glGetObjectParameteriARB(shader, ARBShaderObjects.GL_OBJECT_INFO_LOG_LENGTH_ARB)));
				GL20.glDeleteShader(shader);
				continue;
			}
			GL20.glAttachShader(program, shader);
			shaders.add(shader);
		}
		GL20.glLinkProgram(program);
		String s = GL20.glGetProgramInfoLog(program, ARBShaderObjects.glGetObjectParameteriARB(program, ARBShaderObjects.GL_OBJECT_INFO_LOG_LENGTH_ARB));
		if(!s.isEmpty()) System.out.println("GL LOG: " + s);
		for(int i : shaders) GL20.glDeleteShader(i);
	}

	public void update()
	{
		if(variables.stream().peek(ShaderVar::update).anyMatch(v -> v.hasChanged))
		{
			createProgram();
			variables.forEach(v -> v.hasChanged = false);
		}
	}

	public void onReload()
	{
		createProgram();
	}

	public int getUniformLocation(String location)
	{
		if(program == null) return 0;
		if(!uniformCache.containsKey(location))
			uniformCache.put(location, GL20.glGetUniformLocation(program, location));
		return uniformCache.getInt(location);
	}

	public void setUniform(String uniform, int value)
	{
		GL20.glUniform1i(getUniformLocation(uniform), value);
	}

	public void setUniform(String uniform, boolean value)
	{
		GL20.glUniform1i(getUniformLocation(uniform), value ? 1 : 0);
	}

	public void setUniform(String uniform, float value)
	{
		GL20.glUniform1f(getUniformLocation(uniform), value);
	}

	public void setUniform(String uniform, int v1, int v2)
	{
		GL20.glUniform2i(getUniformLocation(uniform), v1, v2);
	}

	public void setUniform(String uniform, int v1, int v2, int v3)
	{
		GL20.glUniform3i(getUniformLocation(uniform), v1, v2, v3);
	}

	public void setUniform(String uniform, float v1, float v2)
	{
		GL20.glUniform2f(getUniformLocation(uniform), v1, v2);
	}

	public void setUniform(String uniform, float v1, float v2, float v3)
	{
		GL20.glUniform3f(getUniformLocation(uniform), v1, v2, v3);
	}

	public void setUniform(String uniform, float v1, float v2, float v3, float v4)
	{
		GL20.glUniform4f(getUniformLocation(uniform), v1, v2, v3, v4);
	}

	public void setBuffer(String blockName, GLBuffer buffer)
	{
		buffer.bindToShader(program, 0, blockName);
	}

	public void bindShader()
	{
		if(program == null) createProgram();
		GL20.glUseProgram(program);
		if(!onBind.isEmpty()) onBind.forEach(c -> c.accept(this));
	}

	public void unbindShader()
	{
		GL20.glUseProgram(0);
	}

	@SubscribeEvent
	public static void reloadShaders(ResourceManagerReloadEvent e)
	{
		if(e.isType(VanillaResourceType.SHADERS)) Minecraft.getMinecraft().addScheduledTask(() ->
		{
			HammerCore.LOG.info("Reloading " + PROGRAMS.size() + " variable shader programs.");
			PROGRAMS.forEach(VariableShaderProgram::onReload);
		});
	}

	@SubscribeEvent
	public static void tickShader(TickEvent.ClientTickEvent e)
	{
		if(e.phase == TickEvent.Phase.START) PROGRAMS.forEach(VariableShaderProgram::update);
	}

	public boolean isActive()
	{
		return program != null && program.equals(GlShaderStack.glsActiveProgram());
	}
}