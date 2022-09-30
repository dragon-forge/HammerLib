package org.zeith.hammerlib.net.packets;

import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.common.util.LogicalSidedProvider;
import net.minecraftforge.fml.LogicalSide;
import org.zeith.hammerlib.net.*;

@MainThreaded
public class SyncTileEntityPacket
		implements INBTPacket
{
	CompoundTag nbt;
	
	public SyncTileEntityPacket()
	{
	}
	
	public SyncTileEntityPacket(BlockEntity tile)
	{
		this.nbt = tile.serializeNBT();
	}
	
	@Override
	public void write(CompoundTag buf)
	{
		buf.merge(this.nbt);
	}
	
	@Override
	public void read(CompoundTag buf)
	{
		nbt = buf;
	}
	
	@Override
	@OnlyIn(Dist.CLIENT)
	public void clientExecute(PacketContext ctx)
	{
		Level world = LogicalSidedProvider.CLIENTWORLD.get(LogicalSide.CLIENT).orElse(null);
		BlockPos pos = new BlockPos(nbt.getInt("x"), nbt.getInt("y"), nbt.getInt("z"));
		BlockEntity tile = world == null ? null : world.getBlockEntity(pos);
		if(tile != null) tile.load(nbt);
	}
}