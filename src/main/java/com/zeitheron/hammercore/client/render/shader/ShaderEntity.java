package com.zeitheron.hammercore.client.render.shader;

import com.zeitheron.hammercore.HammerCore;
import com.zeitheron.hammercore.api.events.ResourceManagerReloadEvent;
import net.minecraft.client.Minecraft;
import net.minecraft.client.resources.IResourceManager;
import net.minecraftforge.client.resource.VanillaResourceType;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import java.util.ArrayList;
import java.util.List;

@SideOnly(Side.CLIENT)
@Mod.EventBusSubscriber(Side.CLIENT)
public abstract class ShaderEntity
{
	private static final List<ShaderEntity> ENTITIES = new ArrayList<>();

	protected ShaderProgram currentProgram;

	{
		ENTITIES.add(this);
	}

	protected abstract ShaderProgram createShader(IResourceManager manager);

	protected void onReloaded()
	{
	}

	public final void reloadShaderEntity(IResourceManager manager)
	{
		if(currentProgram != null) currentProgram.cleanup();
		currentProgram = null;
		currentProgram = createShader(manager);
		onReloaded();
	}

	@SubscribeEvent
	public static void reloadShaders(ResourceManagerReloadEvent e)
	{
		if(e.isType(VanillaResourceType.SHADERS)) Minecraft.getMinecraft().addScheduledTask(() ->
		{
			HammerCore.LOG.info("Reloading " + ENTITIES.size() + " shader entities.");
			ENTITIES.forEach(se -> se.reloadShaderEntity(e.getManager()));
		});
	}
}