package org.zeith.hammerlib.net.packets;

import net.minecraft.client.Minecraft;
import net.minecraft.network.chat.Component;
import net.neoforged.api.distmarker.*;
import org.zeith.hammerlib.client.CustomFoilConfigs;
import org.zeith.hammerlib.net.*;


@MainThreaded
public class PacketReloadFoils
		implements IPacket
{
	@Override
	@OnlyIn(Dist.CLIENT)
	public void clientExecute(PacketContext ctx)
	{
		var mc = Minecraft.getInstance();
		mc.chatListener.handleSystemMessage(Component.literal("Reloading foil colors."), false);
		CustomFoilConfigs.reload();
		mc.chatListener.handleSystemMessage(Component.literal("Foil colors reloaded."), false);
	}
}