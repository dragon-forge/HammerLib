package com.zeitheron.hammercore.api.lighting;

import com.zeitheron.hammercore.client.utils.RenderUtil;
import com.zeitheron.hammercore.client.utils.gl.shading.ShaderVar;
import net.minecraftforge.fml.relauncher.*;
import org.lwjgl.opengl.*;

@SideOnly(Side.CLIENT)
public class ShaderLightingVariable
		extends ShaderVar<Integer>
{
	// usually is 2048, but we should query OpenGL in case the limit ever gets increased.
	private int blockLimit;
	
	private final String lightStructName;
	
	public ShaderLightingVariable(String key)
	{
		this(key, "Light");
	}
	
	public ShaderLightingVariable(String key, String lightStructName)
	{
		super(key);
		this.lightStructName = lightStructName;
		RenderUtil.glTaskAsync(() -> blockLimit = GL11.glGetInteger(GL31.GL_MAX_UNIFORM_BLOCK_SIZE) / ColoredLight.FLOAT_SIZE / 4);
	}
	
	@Override
	protected Integer getState()
	{
		int lightCount = ColoredLightManager.UNIFORM_LIGHT_COUNT.getAsInt();
		return (int) Math.ceil(lightCount / (float) blockLimit);
	}
	
	@Override
	protected String compute(Integer blocks)
	{
		String layoutTemplate = String.format("layout(std140) uniform lightBuffer%%s\n{\n  %s lights%%s[%d];\n};\n\n", lightStructName, blockLimit);
		StringBuilder gen = new StringBuilder();
		for(int i = 0; i < blocks; ++i)
		{
			String s = Integer.toString(i);
			gen.append(String.format(layoutTemplate, s, s));
		}
		
		gen.append("\n").append(lightStructName).append(" getLight(int idx)\n{");
		
		for(int i = 0; i < blocks; ++i)
		{
			int start = blockLimit * i;
			int end = blockLimit * (i + 1);
			gen.append(String.format("\n  %sif(idx >= %d && idx < %d) return lights%d[idx - %d];", i > 0 ? "else " : "", start, end, i, start));
		}
		gen.append("\n  return lights0[0];");
		gen.append("\n}");
		return gen.toString();
	}
}