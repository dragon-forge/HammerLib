package com.zeitheron.hammercore.net.internal;

import com.zeitheron.hammercore.net.IPacket;
import com.zeitheron.hammercore.net.PacketContext;

import net.minecraft.client.Minecraft;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.EnumHand;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class PacketSwingArm implements IPacket
{
	public EnumHand hand;
	
	static
	{
		IPacket.handle(PacketSwingArm.class, PacketSwingArm::new);
	}
	
	public PacketSwingArm(EnumHand hand)
	{
		this.hand = hand;
	}
	
	public PacketSwingArm()
	{
	}
	
	@Override
	@SideOnly(Side.CLIENT)
	public IPacket executeOnClient(PacketContext net)
	{
		Minecraft.getMinecraft().player.swingArm(hand);
		return null;
	}
	
	@Override
	public void writeToNBT(NBTTagCompound nbt)
	{
		nbt.setInteger("p1", hand.ordinal());
	}
	
	@Override
	public void readFromNBT(NBTTagCompound nbt)
	{
		hand = EnumHand.values()[nbt.getInteger("p1")];
	}
}