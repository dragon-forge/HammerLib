package com.zeitheron.hammercore.net.internal;

import com.zeitheron.hammercore.net.IPacket;
import com.zeitheron.hammercore.net.MainThreaded;
import com.zeitheron.hammercore.net.PacketContext;

import net.minecraft.advancements.Advancement;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.toasts.AdvancementToast;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

@MainThreaded
public class PacketAdvancementToast implements IPacket
{
	private ResourceLocation advancement;
	
	static
	{
		IPacket.handle(PacketAdvancementToast.class, PacketAdvancementToast::new);
	}
	
	public PacketAdvancementToast(ResourceLocation advancement)
	{
		this.advancement = advancement;
	}
	
	public PacketAdvancementToast()
	{
	}
	
	@Override
	public void writeToNBT(NBTTagCompound nbt)
	{
		nbt.setString("Advancement", advancement.toString());
	}
	
	@Override
	public void readFromNBT(NBTTagCompound nbt)
	{
		advancement = new ResourceLocation(nbt.getString("Advancement"));
	}
	
	@Override
	@SideOnly(Side.CLIENT)
	public void executeOnClient2(PacketContext net)
	{
		Advancement adv = Minecraft.getMinecraft().getConnection().getAdvancementManager().getAdvancementList().getAdvancement(advancement);
		if(adv != null)
			Minecraft.getMinecraft().getToastGui().add(new AdvancementToast(adv));
	}
}