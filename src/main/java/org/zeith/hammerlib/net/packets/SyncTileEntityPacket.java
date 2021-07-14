package org.zeith.hammerlib.net.packets;

import net.minecraft.nbt.CompoundNBT;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.fml.LogicalSide;
import net.minecraftforge.fml.LogicalSidedProvider;
import org.zeith.hammerlib.net.INBTPacket;
import org.zeith.hammerlib.net.PacketContext;

import java.util.Optional;

public class SyncTileEntityPacket
		implements INBTPacket
{
	CompoundNBT nbt;

	public SyncTileEntityPacket()
	{
	}

	public SyncTileEntityPacket(TileEntity tile)
	{
		this.nbt = tile.serializeNBT();
	}

	@Override
	public void write(CompoundNBT buf)
	{
		buf.merge(this.nbt);
	}

	@Override
	public void read(CompoundNBT buf)
	{
		nbt = buf;
	}

	@Override
	@OnlyIn(Dist.CLIENT)
	public void clientExecute(PacketContext ctx)
	{
		World world = LogicalSidedProvider.CLIENTWORLD.<Optional<World>> get(LogicalSide.CLIENT).orElse(null);
		BlockPos pos = new BlockPos(nbt.getInt("x"), nbt.getInt("y"), nbt.getInt("z"));
		TileEntity tile = world == null ? null : world.getBlockEntity(pos);
		if(tile != null) tile.load(world.getBlockState(pos), nbt);
	}
}