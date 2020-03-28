package com.zeitheron.hammercore.client.shaders;

import com.zeitheron.hammercore.HammerCore;
import com.zeitheron.hammercore.client.utils.gl.shading.InitializeShadersEvent;
import com.zeitheron.hammercore.client.utils.gl.shading.ShaderSource;
import com.zeitheron.hammercore.client.utils.gl.shading.VariableShaderProgram;
import net.minecraft.client.Minecraft;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

@SideOnly(Side.CLIENT)
@Mod.EventBusSubscriber(Side.CLIENT)
public class GLSLEnderField
{
	private static VariableShaderProgram shader;

	@SubscribeEvent
	public static void init(InitializeShadersEvent e)
	{
		shader = new VariableShaderProgram()
				.id(new ResourceLocation("hammercore", "ender"))
				.linkFragmentSource(new ShaderSource(new ResourceLocation("hammercore", "shaders/ender.fsh")))
				.linkVertexSource(new ShaderSource(new ResourceLocation("hammercore", "shaders/ender.vsh")))
				.onCompilationFailed(VariableShaderProgram.ToastCompilationErrorHandler.INSTANCE)
				.onBind(GLSLEnderField::uniforms)
				.doGLLog(false)
				.subscribe4Events();
		HammerCore.LOG.info("Created Ender Field GLSL Shader.");
	}

	public static VariableShaderProgram getShader()
	{
		return shader;
	}

	private static void uniforms(VariableShaderProgram program)
	{
		program.setUniform("time", (float) (Minecraft.getMinecraft().world != null ? Minecraft.getMinecraft().world.getTotalWorldTime() / 10600D : Minecraft.getSystemTime() / 530000D));
		program.setUniform("yaw", 0F);
		program.setUniform("pitch", 0F);
		program.setUniform("color", 0.044F, 0.036F, 0.063F, 1F);
	}
}