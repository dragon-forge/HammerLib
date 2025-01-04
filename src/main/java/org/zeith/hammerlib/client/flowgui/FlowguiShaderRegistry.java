package org.zeith.hammerlib.client.flowgui;

import net.minecraft.client.renderer.GameRenderer;
import net.minecraft.client.renderer.ShaderInstance;
import net.minecraft.resources.ResourceLocation;
import org.zeith.hammerlib.util.mcf.Resources;

import java.util.HashMap;
import java.util.Map;
import java.util.function.Supplier;

public class FlowguiShaderRegistry
{
	public static final Supplier<ShaderInstance> GUI_SHADER = GameRenderer::getPositionColorTexShader;
	public static final Supplier<ShaderInstance> COLOR_SHADER = GameRenderer::getPositionColorShader;
	private static final Map<ResourceLocation, Supplier<ShaderInstance>> REGISTRY = new HashMap<>();
	
	static
	{
		register(Resources.location("gui"), GUI_SHADER);
		register(Resources.location("color"), COLOR_SHADER);
	}
	
	public static void register(ResourceLocation texture, Supplier<ShaderInstance> shader)
	{
		REGISTRY.put(texture, shader);
	}
	
	public static Supplier<ShaderInstance> get(ResourceLocation id)
	{
		return REGISTRY.getOrDefault(id, GUI_SHADER);
	}
}