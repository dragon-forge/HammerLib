package com.pengu.hammercore.client.render.shader;

import java.util.HashMap;

import org.lwjgl.opengl.ARBShaderObjects;

import net.minecraft.client.Minecraft;

public class ShaderRenderTimeOperation implements iShaderOperation
{
	public static final int operationID = HCShaderPipeline.registerOperation();
	private final HashMap<ShaderProgram, Float> shaderRenderTimeCache = new HashMap<>();
	
	@Override
	public boolean load(ShaderProgram program)
	{
		return true;
	}
	
	@Override
	public void operate(ShaderProgram program)
	{
		float renderTime = Minecraft.getSystemTime();
		if(renderTime != shaderRenderTimeCache.get(program))
		{
			int location = program.getAttribLoc("time");
			ARBShaderObjects.glUniform1fARB(location, renderTime);
			shaderRenderTimeCache.put(program, renderTime);
		}
	}
	
	@Override
	public int operationID()
	{
		return operationID;
	}
}