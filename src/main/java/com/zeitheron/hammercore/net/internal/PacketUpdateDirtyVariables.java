package com.zeitheron.hammercore.net.internal;

import java.util.Map;

import com.zeitheron.hammercore.internal.variables.VariableManager;
import com.zeitheron.hammercore.internal.variables.IVariable;
import com.zeitheron.hammercore.net.IPacket;
import com.zeitheron.hammercore.net.MainThreaded;
import com.zeitheron.hammercore.net.PacketContext;

import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.common.util.Constants.NBT;
import net.minecraftforge.fml.relauncher.Side;

@MainThreaded
public class PacketUpdateDirtyVariables implements IPacket
{
	public NBTTagList nbt;
	
	static
	{
		IPacket.handle(PacketUpdateDirtyVariables.class, PacketUpdateDirtyVariables::new);
	}
	
	public PacketUpdateDirtyVariables(Map<ResourceLocation, IVariable<?>> vars)
	{
		nbt = new NBTTagList();
		for(ResourceLocation key : vars.keySet())
		{
			NBTTagCompound tag = new NBTTagCompound();
			tag.setString("Id", key.toString());
			tag.setTag("Data", vars.get(key).writeToNBT(new NBTTagCompound()));
			nbt.appendTag(tag);
		}
	}
	
	public PacketUpdateDirtyVariables()
	{
	}
	
	@Override
	public IPacket execute(Side side, PacketContext ctx)
	{
		NBTTagList list = nbt;
		for(int i = 0; i < list.tagCount(); ++i)
		{
			NBTTagCompound nbt = list.getCompoundTagAt(i);
			IVariable<?> var = VariableManager.getVariable(new ResourceLocation(nbt.getString("Id")));
			if(var != null && var.direction().allowedTo(side))
			{
				var.readFromNBT(nbt.getCompoundTag("Data"));
				var.notifyUpdate(side);
			}
		}
		return null;
	}
	
	@Override
	public void writeToNBT(NBTTagCompound nbt)
	{
		nbt.setTag("List", this.nbt);
	}
	
	@Override
	public void readFromNBT(NBTTagCompound nbt)
	{
		this.nbt = nbt.getTagList("List", NBT.TAG_COMPOUND);
	}
}