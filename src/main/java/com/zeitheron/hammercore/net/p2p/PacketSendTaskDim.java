package com.zeitheron.hammercore.net.p2p;

import com.zeitheron.hammercore.net.HCNet;
import com.zeitheron.hammercore.net.IPacket;
import com.zeitheron.hammercore.net.PacketContext;

import net.minecraft.nbt.NBTTagCompound;

public class PacketSendTaskDim implements IPacket
{
	private NBTTagCompound task;
	private String sender;
	private int[] receivers;
	
	static
	{
		IPacket.handle(PacketSendTaskDim.class, () -> new PacketSendTaskDim());
	}
	
	public PacketSendTaskDim(ITask task, int... receivers)
	{
		this.task = new NBTTagCompound();
		NBTTagCompound tag = new NBTTagCompound();
		task.writeToNBT(tag);
		this.task.setTag("Data", tag);
		this.task.setString("Class", task.getClass().getName());
		
		this.sender = "?";
		this.receivers = receivers;
	}
	
	public PacketSendTaskDim()
	{
	}
	
	@Override
	public void executeOnServer2(PacketContext net)
	{
		sender = net.sender.asPlayer().getGameProfile().getName();
		for(int $receiver : receivers)
			HCNet.INSTANCE.sendToDimension(this, $receiver);
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
		nbt.setIntArray("Rec", receivers);
	}
	
	@Override
	public void readFromNBT(NBTTagCompound nbt)
	{
		this.task = nbt.getCompoundTag("Task");
		sender = nbt.getString("Sen");
		receivers = nbt.getIntArray("Rec");
	}
}