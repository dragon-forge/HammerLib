package org.zeith.hammerlib.api.lighting;

import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL31;
import org.zeith.hammerlib.client.pipelines.shaders.ShaderVar;
import org.zeith.hammerlib.client.utils.GLHelperHL;

public class ShaderLightingVariable
		extends ShaderVar
{
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
		GLHelperHL.glTaskAsync(() -> blockLimit = GL11.glGetInteger(GL31.GL_MAX_UNIFORM_BLOCK_SIZE) / ColoredLight.FLOAT_SIZE / 4);
	}

	@Override
	protected String getCurrentValue()
	{
		int lightCount = ColoredLightManager.UNIFORM_LIGHT_COUNT.getAsInt();
		String layoutTemplate = "layout(std140) uniform lightBuffer%s\n{\n  " + lightStructName + " lights%s[2048];\n};\n\n";
		String methodHeader = lightStructName + " getLight(int idx)\n{";
		int blocks = (int) Math.ceil(lightCount / (float) blockLimit);
		StringBuilder gen = new StringBuilder();
		for(int i = 0; i < blocks; ++i)
		{
			String s = Integer.toString(i);
			gen.append(String.format(layoutTemplate, s, s));
		}

		gen.append("\n" + methodHeader);

//		gen.append("\n  " + lightStructName + " mat[" + blocks + "][];\n");
//		for(int i = 0; i < blocks; ++i) gen.append("\n  mat[" + i + "] = lights" + i + ";");
//		gen.append("\n  " + lightStructName + "[] arr = mat[0];");
//		gen.append("\n  return arr[idx % 2048];");

		for(int i = 0; i < blocks; ++i)
		{
			int start = blockLimit * i;
			int end = Math.min(lightCount, blockLimit * (i + 1));
			gen.append("\n  " + (i > 0 ? "else " : "") + "if(idx >= " + start + " && idx < " + end + ") return lights" + i + "[idx - " + start + "];");
		}
		gen.append("\n  return lights0[0];");
		gen.append("\n}");
		return gen.toString();
	}
}