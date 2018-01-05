package com.pengu.hammercore.net.packetAPI.p2p;

import java.util.UUID;

import com.pengu.hammercore.net.HCNetwork;
import com.pengu.hammercore.net.packetAPI.iPacket;
import com.pengu.hammercore.net.packetAPI.iPacketListener;
import com.pengu.hammercore.utils.NBTUtils;

import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;
import net.minecraftforge.fml.relauncher.Side;

public class PacketSendTaskUUID implements iPacket, iPacketListener<PacketSendTaskUUID, iPacket>
{
	private NBTTagCompound task;
	private String sender;
	private UUID[] receivers;
	
	public PacketSendTaskUUID(iTask task, UUID... receivers)
	{
		this.task = new NBTTagCompound();
		NBTTagCompound tag = new NBTTagCompound();
		task.writeToNBT(tag);
		this.task.setTag("Data", tag);
		this.task.setString("Class", task.getClass().getName());
		
		this.sender = "?";
		this.receivers = receivers;
	}
	
	public PacketSendTaskUUID()
	{
	}
	
	@Override
	public iPacket onArrived(PacketSendTaskUUID packet, MessageContext context)
	{
		if(context.side == Side.SERVER)
		{
			packet.sender = context.getServerHandler().player.getGameProfile().getName();
			for(UUID $receiver : packet.receivers)
			{
				EntityPlayerMP receiver = context.getServerHandler().player.mcServer.getPlayerList().getPlayerByUUID($receiver);
				if(receiver != null)
					HCNetwork.manager.sendTo(packet, receiver);
			}
		}
		if(context.side == Side.CLIENT)
		{
			try
			{
				iTask task = (iTask) Class.forName(this.task.getString("Class")).newInstance();
				task.readFromNBT(this.task.getCompoundTag("Data"));
				task.execute(context);
			} catch(Throwable err)
			{
				err.printStackTrace();
			}
		}
		return null;
	}
	
	@Override
	public void writeToNBT(NBTTagCompound nbt)
	{
		nbt.setTag("Task", this.task);
		nbt.setString("Sen", sender);
		NBTUtils.writeUUIDArrayToNBT("Rec", nbt, receivers);
	}
	
	@Override
	public void readFromNBT(NBTTagCompound nbt)
	{
		this.task = nbt.getCompoundTag("Task");
		sender = nbt.getString("Sen");
		receivers = NBTUtils.readUUIDArrayFromNBT("Rec", nbt);
	}
}