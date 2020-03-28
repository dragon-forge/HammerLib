package com.zeitheron.hammercore.client.render.shader.impl;

import com.zeitheron.hammercore.client.render.shader.HCShaderPipeline;
import com.zeitheron.hammercore.client.render.shader.IShaderOperation;
import com.zeitheron.hammercore.client.render.shader.ShaderProgram;
import com.zeitheron.hammercore.client.utils.RenderUtil;
import com.zeitheron.hammercore.client.utils.UtilsFX;
import com.zeitheron.hammercore.client.utils.texture.TextureAtlasSpriteFull;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.OpenGlHelper;
import net.minecraft.client.renderer.Tessellator;
import org.lwjgl.opengl.ARBShaderObjects;


/**
 * @deprecated moved to {@link com.zeitheron.hammercore.client.shaders.GLSLEnderField} with new shader flow.
 */
@Deprecated
public class ShaderEnderField
{
	public static ShaderProgram endShader;
	public static EnderFieldOperation operation;

	public static void reloadShader()
	{
		try
		{
			if(endShader != null)
				endShader.cleanup();
			endShader = new ShaderProgram();
			endShader.attachFrag("/assets/hammercore/shaders/ender.fsh");
			endShader.attachVert("/assets/hammercore/shaders/ender.vsh");
			endShader.attachShaderOperation(operation = new EnderFieldOperation(HCShaderPipeline.registerOperation()));
			endShader.validate();
		} catch(Throwable err)
		{
			err.printStackTrace();
		}
	}

	public static void renderIntoGui(double xCoord, double yCoord, double widthIn, double heightIn)
	{
		if(useShaders() && endShader == null)
			reloadShader();
		if(useShaders() && endShader != null)
			endShader.freeBindShader();
		UtilsFX.bindTexture("minecraft", "textures/entity/end_portal.png");
		RenderUtil.drawTexturedModalRect(xCoord, yCoord, TextureAtlasSpriteFull.sprite, widthIn, heightIn);
		if(useShaders())
			ShaderProgram.unbindShader();
	}

	public static void finishDrawWithShaders(Tessellator tess)
	{
		if(useShaders() && endShader == null)
			reloadShader();
		if(useShaders() && endShader != null)
			endShader.freeBindShader();
		UtilsFX.bindTexture("minecraft", "textures/entity/end_portal.png");
		tess.draw();
		if(useShaders())
			ShaderProgram.unbindShader();
	}

	public static class EnderFieldOperation
			implements IShaderOperation
	{
		public final int op;

		public EnderFieldOperation(int op)
		{
			this.op = op;
		}

		@Override
		public boolean load(ShaderProgram program)
		{
			return true;
		}

		@Override
		public void operate(ShaderProgram program)
		{
			ARBShaderObjects.glUniform1fARB(program.getUniformLoc("time"), (float) (Minecraft.getMinecraft().world != null ? Minecraft.getMinecraft().world.getTotalWorldTime() / 10600D : Minecraft.getSystemTime() / 530000D));
			ARBShaderObjects.glUniform1fARB(program.getUniformLoc("yaw"), 0);
			ARBShaderObjects.glUniform1fARB(program.getUniformLoc("pitch"), 0);
			ARBShaderObjects.glUniform4fARB(program.getUniformLoc("color"), 0.044F, 0.036F, 0.063F, 1F);
		}

		@Override
		public int operationID()
		{
			return op;
		}
	}

	public static boolean useShaders()
	{
		return OpenGlHelper.shadersSupported;
	}
}