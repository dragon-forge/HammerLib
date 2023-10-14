package com.zeitheron.hammercore.net.internal;

import com.zeitheron.hammercore.net.*;
import com.zeitheron.hammercore.tile.TileSyncable;
import com.zeitheron.hammercore.utils.*;
import com.zeitheron.hammercore.utils.base.Cast;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;

@MainThreaded
public class PacketSyncSyncableTile
		implements IPacket
{
	private String pos;
	private int world;
	private NBTTagCompound nbt;
	
	private String clazz;
	
	static
	{
		IPacket.handle(PacketSyncSyncableTile.class, PacketSyncSyncableTile::new);
	}
	
	public PacketSyncSyncableTile()
	{
	}
	
	public PacketSyncSyncableTile(TileSyncable tile)
	{
		nbt = tile.getUpdateTag();
		pos = WritablePos.toStr(tile.getPos());
		world = tile.getWorld().provider.getDimension();
		clazz = tile.getClass().getName();
	}
	
	@Override
	public void writeToNBT(NBTTagCompound nbt)
	{
		nbt.setTag("data", this.nbt);
		nbt.setString("pos", pos);
		nbt.setInteger("dim", world);
	}
	
	@Override
	public void readFromNBT(NBTTagCompound nbt)
	{
		this.nbt = nbt.getCompoundTag("data");
		pos = nbt.getString("pos");
		world = nbt.getInteger("dim");
	}
	
	@Override
	public IPacket execute(Side side, PacketContext net)
	{
		World world = WorldUtil.getWorld(net, this.world);
		BlockPos pos = WritablePos.fromStr(this.pos);
		if(world != null && world.isAreaLoaded(pos, pos) /* prevent crashing... */)
		{
			TileSyncable sync = Cast.cast(world.getTileEntity(pos), TileSyncable.class);
			
			// try to recreate tile if we can
			// @since 1.5.3
			if(sync == null)
				try
				{
					sync = (TileSyncable) Class.forName(clazz).newInstance();
				} catch(Throwable err)
				{
				}
			
			if(sync != null)
			{
				sync.onPreSync(nbt);
				sync.handleUpdateTag(nbt);
				sync.onSynced();
			}
		}
		return null;
	}
}