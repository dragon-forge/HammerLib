package org.zeith.hammerlib.net.packets;

import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.common.util.LogicalSidedProvider;
import net.minecraftforge.fml.LogicalSide;
import org.zeith.hammerlib.net.*;

@MainThreaded
public class SyncTileEntityPacket
		implements IPacket
{
	CompoundTag nbt;
	boolean updateTag;
	
	public SyncTileEntityPacket()
	{
	}
	
	public SyncTileEntityPacket(BlockEntity tile, boolean updateTag)
	{
		this.nbt = updateTag ? tile.getUpdateTag() : tile.serializeNBT();
	}
	
	@Override
	public void write(FriendlyByteBuf buf)
	{
		buf.writeNbt(nbt);
		buf.writeBoolean(updateTag);
	}
	
	@Override
	public void read(FriendlyByteBuf buf)
	{
		nbt = buf.readNbt();
		updateTag = buf.readBoolean();
	}
	
	@Override
	@OnlyIn(Dist.CLIENT)
	public void clientExecute(PacketContext ctx)
	{
		Level world = LogicalSidedProvider.CLIENTWORLD.get(LogicalSide.CLIENT).orElse(null);
		BlockPos pos = new BlockPos(nbt.getInt("x"), nbt.getInt("y"), nbt.getInt("z"));
		BlockEntity tile = world == null ? null : world.getBlockEntity(pos);
		if(tile != null)
		{
			if(updateTag)
				tile.handleUpdateTag(nbt);
			else
				tile.load(nbt);
		}
	}
}