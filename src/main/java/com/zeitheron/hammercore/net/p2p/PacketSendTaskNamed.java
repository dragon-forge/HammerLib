package com.zeitheron.hammercore.net.p2p;

import com.zeitheron.hammercore.net.HCNet;
import com.zeitheron.hammercore.net.IPacket;
import com.zeitheron.hammercore.net.PacketContext;
import com.zeitheron.hammercore.utils.NBTUtils;

import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.nbt.NBTTagCompound;

public class PacketSendTaskNamed implements IPacket
{
	private NBTTagCompound task;
	private String sender;
	private String[] receivers;
	
	static
	{
		IPacket.handle(PacketSendTaskNamed.class, () -> new PacketSendTaskNamed());
	}
	
	public PacketSendTaskNamed(ITask task, String... receivers)
	{
		this.task = new NBTTagCompound();
		NBTTagCompound tag = new NBTTagCompound();
		task.writeToNBT(tag);
		this.task.setTag("Data", tag);
		this.task.setString("Class", task.getClass().getName());
		
		this.sender = "?";
		this.receivers = receivers;
	}
	
	public PacketSendTaskNamed()
	{
	}
	
	@Override
	public void executeOnServer2(PacketContext net)
	{
		sender = net.sender.asPlayer().getGameProfile().getName();
		for(String $receiver : receivers)
		{
			EntityPlayerMP receiver = net.server.getPlayerList().getPlayerByUsername($receiver);
			if(receiver != null)
				HCNet.INSTANCE.sendTo(this, receiver);
		}
	}
	
	@Override
	public void executeOnClient2(PacketContext net)
	{
		try
		{
			ITask task = (ITask) Class.forName(this.task.getString("Class")).newInstance();
			task.readFromNBT(this.task.getCompoundTag("Data"));
			task.execute(net);
		} catch(Throwable err)
		{
			err.printStackTrace();
		}
	}
	
	@Override
	public void writeToNBT(NBTTagCompound nbt)
	{
		nbt.setTag("Task", this.task);
		nbt.setString("Sen", sender);
		NBTUtils.writeStringArrayToNBT("Rec", nbt, receivers);
	}
	
	@Override
	public void readFromNBT(NBTTagCompound nbt)
	{
		this.task = nbt.getCompoundTag("Task");
		sender = nbt.getString("Sen");
		receivers = NBTUtils.readStringArrayFromNBT("Rec", nbt);
	}
}