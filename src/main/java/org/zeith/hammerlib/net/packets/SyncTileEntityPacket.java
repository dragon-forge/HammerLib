package org.zeith.hammerlib.net.packets;

import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.neoforged.api.distmarker.*;
import net.neoforged.fml.LogicalSide;
import net.neoforged.neoforge.common.util.LogicalSidedProvider;
import org.zeith.hammerlib.net.*;

@MainThreaded
public class SyncTileEntityPacket
		implements IPacket
{
	BlockPos pos;
	CompoundTag nbt;
	boolean updateTag;
	
	public SyncTileEntityPacket()
	{
	}
	
	public SyncTileEntityPacket(BlockEntity tile, boolean updateTag)
	{
		this.pos = tile.getBlockPos();
		this.nbt = updateTag ? tile.getUpdateTag() : tile.serializeNBT();
		this.updateTag = updateTag;
	}
	
	@Override
	public void write(FriendlyByteBuf buf)
	{
		buf.writeBlockPos(pos);
		buf.writeNbt(nbt);
		buf.writeBoolean(updateTag);
	}
	
	@Override
	public void read(FriendlyByteBuf buf)
	{
		pos = buf.readBlockPos();
		nbt = buf.readNbt();
		updateTag = buf.readBoolean();
	}
	
	@Override
	@OnlyIn(Dist.CLIENT)
	public void clientExecute(PacketContext ctx)
	{
		Level world = ctx.getLevel();
		if(world == null) return;
		
		BlockEntity tile = world.getBlockEntity(pos);
		if(tile != null)
		{
			if(updateTag)
				tile.handleUpdateTag(nbt);
			else
				tile.load(nbt);
		}
	}
}