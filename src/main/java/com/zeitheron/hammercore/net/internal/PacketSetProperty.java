package com.zeitheron.hammercore.net.internal;

import com.zeitheron.hammercore.HammerCore;
import com.zeitheron.hammercore.net.HCNet;
import com.zeitheron.hammercore.net.IPacket;
import com.zeitheron.hammercore.net.MainThreaded;
import com.zeitheron.hammercore.net.PacketContext;
import com.zeitheron.hammercore.net.props.NetPropertyAbstract;
import com.zeitheron.hammercore.tile.TileSyncable;

import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.WorldServer;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

@MainThreaded
public class PacketSetProperty implements IPacket
{
	protected NBTTagCompound nbt;
	
	static
	{
		IPacket.handle(PacketSetProperty.class, PacketSetProperty::new);
	}
	
	@SideOnly(Side.CLIENT)
	public static void toServer(TileSyncable tile, NetPropertyAbstract abs)
	{
		HCNet.INSTANCE.sendToServer(new PacketSetProperty(tile, abs));
	}
	
	public PacketSetProperty(TileSyncable tile, NetPropertyAbstract property)
	{
		nbt = new NBTTagCompound();
		nbt.setLong("Pos", tile.getPos().toLong());
		nbt.setInteger("Dim", tile.getWorld().provider.getDimension());
		nbt.setTag("Data", property.writeToNBT(new NBTTagCompound()));
		nbt.setInteger("Id", property.getId());
	}
	
	public PacketSetProperty()
	{
	}
	
	@Override
	public void executeOnServer2(PacketContext net)
	{
		int dim = nbt.getInteger("Dim");
		BlockPos pos = BlockPos.fromLong(nbt.getLong("Pos"));
		NBTTagCompound prop = nbt.getCompoundTag("Data");
		int id = nbt.getInteger("Id");
		
		MinecraftServer server = net.server;
		WorldServer world = server.getWorld(dim);
		
		if(world != null && world.isBlockLoaded(pos) && world.getTileEntity(pos) instanceof TileSyncable)
			((TileSyncable) world.getTileEntity(pos)).load(id, prop);
	}
	
	@Override
	public void writeToNBT(NBTTagCompound nbt)
	{
		nbt.setTag("Data", this.nbt);
	}
	
	@Override
	public void readFromNBT(NBTTagCompound nbt)
	{
		this.nbt = nbt.getCompoundTag("Data");
	}
}