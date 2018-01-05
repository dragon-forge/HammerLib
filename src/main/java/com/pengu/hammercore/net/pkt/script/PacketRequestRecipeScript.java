package com.pengu.hammercore.net.pkt.script;

import com.pengu.hammercore.HammerCore.GRCProvider;
import com.pengu.hammercore.net.packetAPI.iPacket;
import com.pengu.hammercore.net.packetAPI.iPacketListener;

import net.minecraft.nbt.NBTTagCompound;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;
import net.minecraftforge.fml.relauncher.Side;

public class PacketRequestRecipeScript implements iPacket, iPacketListener<PacketRequestRecipeScript, iPacket>
{
	public int id;
	
	public PacketRequestRecipeScript()
	{
	}
	
	public PacketRequestRecipeScript(int id)
	{
		this.id = id;
	}
	
	@Override
	public iPacket onArrived(PacketRequestRecipeScript packet, MessageContext context)
	{
		if(context.side == Side.SERVER && GRCProvider.getScriptCount() > packet.id)
			return new PacketSendGlobalRecipeScripts(packet.id, GRCProvider.getScript(packet.id));
		else if(context.side == Side.SERVER)
			return new PacketReloadScripts();
		return null;
	}
	
	@Override
	public void writeToNBT(NBTTagCompound nbt)
	{
		nbt.setInteger("a0", id);
	}
	
	@Override
	public void readFromNBT(NBTTagCompound nbt)
	{
		id = nbt.getInteger("a0");
	}
}