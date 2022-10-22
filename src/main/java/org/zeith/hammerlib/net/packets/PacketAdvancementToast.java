package org.zeith.hammerlib.net.packets;

import net.minecraft.advancements.Advancement;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.components.toasts.AdvancementToast;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import org.zeith.hammerlib.net.*;

@MainThreaded
public class PacketAdvancementToast
		implements IPacket
{
	private ResourceLocation advancement;
	
	public PacketAdvancementToast(ResourceLocation advancement)
	{
		this.advancement = advancement;
	}
	
	public PacketAdvancementToast()
	{
	}
	
	@Override
	public void write(FriendlyByteBuf buf)
	{
		buf.writeResourceLocation(advancement);
	}
	
	@Override
	public void read(FriendlyByteBuf buf)
	{
		advancement = buf.readResourceLocation();
	}
	
	@Override
	@OnlyIn(Dist.CLIENT)
	public void clientExecute(PacketContext ctx)
	{
		var mc = Minecraft.getInstance();
		
		var connection = mc.getConnection();
		if(connection == null) return;
		
		Advancement adv = connection.getAdvancements().getAdvancements().get(advancement);
		if(adv == null) return;
		
		mc.getToasts().addToast(new AdvancementToast(adv));
	}
}