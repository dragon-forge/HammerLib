package org.zeith.hammerlib.client.flowgui;

import net.minecraft.client.renderer.RenderType;
import net.minecraft.resources.ResourceLocation;
import org.zeith.hammerlib.client.utils.IRenderTypeFactory;
import org.zeith.hammerlib.util.mcf.Resources;

import java.util.HashMap;
import java.util.Map;

public class FlowguiShaderRegistry
{
	public static final IRenderTypeFactory GUI_SHADER = RenderType::guiTextured;
	public static final IRenderTypeFactory COLOR_SHADER = tex -> RenderType.gui();
	private static final Map<ResourceLocation, IRenderTypeFactory> REGISTRY = new HashMap<>();
	
	static
	{
		register(Resources.location("gui"), GUI_SHADER);
		register(Resources.location("color"), COLOR_SHADER);
	}
	
	public static void register(ResourceLocation id, IRenderTypeFactory shader)
	{
		REGISTRY.put(id, shader);
	}
	
	public static IRenderTypeFactory get(ResourceLocation id)
	{
		return REGISTRY.getOrDefault(id, GUI_SHADER);
	}
}