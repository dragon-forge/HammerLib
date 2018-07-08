package com.zeitheron.hammercore.client.userlocal;

import org.lwjgl.input.Keyboard;

import com.zeitheron.hammercore.HammerCore;
import com.zeitheron.hammercore.client.PerUserModule;
import com.zeitheron.hammercore.client.UserModule;
import com.zeitheron.hammercore.net.HCNet;
import com.zeitheron.hammercore.net.internal.PacketTeleportPlayer;
import com.zeitheron.hammercore.raytracer.RayTracer;

import net.minecraft.client.Minecraft;
import net.minecraft.client.settings.KeyBinding;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.util.math.Vec3d;
import net.minecraft.util.math.RayTraceResult.Type;
import net.minecraftforge.client.settings.KeyConflictContext;
import net.minecraftforge.fml.client.registry.ClientRegistry;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent.ClientTickEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent.Phase;

@UserModule(username = "_MasterEnderman_")
public class _MasterEnderman_ extends PerUserModule
{
	public static final KeyBinding tpKey = new KeyBinding("TP Keybind", Keyboard.KEY_APOSTROPHE, "key.categories.gameplay");
	
	@Override
	public void preInit()
	{
		HammerCore.instance.MCFBusObjects.add(this);
	}
	
	@Override
	public void init()
	{
		ClientRegistry.registerKeyBinding(tpKey);
		tpKey.setKeyConflictContext(KeyConflictContext.IN_GAME);
	}
	
	public boolean lastState;
	
	@SubscribeEvent
	public void clientTick(ClientTickEvent cte)
	{
		if(cte.phase != Phase.START)
			return;
		
		if(lastState != tpKey.isKeyDown())
		{
			lastState = tpKey.isKeyDown();
			if(!lastState)
				return;
			RayTraceResult hit = RayTracer.retrace(Minecraft.getMinecraft().player, 128);
			if(hit != null && hit.typeOfHit == Type.BLOCK)
			{
				Vec3d target = new Vec3d(hit.getBlockPos().offset(hit.sideHit)).addVector(.5, .5, .5);
				HCNet.INSTANCE.sendToServer(new PacketTeleportPlayer().withTarget(target));
			}
		}
	}
}