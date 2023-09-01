package org.zeith.hammerlib.tiles;

import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.Connection;
import net.minecraft.network.protocol.game.ClientboundBlockEntityDataPacket;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import org.zeith.hammerlib.abstractions.sources.IObjectSource;
import org.zeith.hammerlib.api.io.NBTSerializationHelper;
import org.zeith.hammerlib.api.tiles.ISyncableTile;
import org.zeith.hammerlib.net.properties.IPropertyTile;
import org.zeith.hammerlib.net.properties.PropertyDispatcher;

import java.util.Random;

public class TileSyncable
		extends BlockEntity
		implements ISyncableTile, IPropertyTile
{
	protected final PropertyDispatcher dispatcher = new PropertyDispatcher(IObjectSource.ofTile(this), this::syncProperties);
	protected Random rand = new Random();

	public TileSyncable(BlockEntityType<?> type, BlockPos pos, BlockState state)
	{
		super(type, pos, state);
	}

	@Override
	public PropertyDispatcher getProperties()
	{
		return dispatcher;
	}

	public Random getRNG()
	{
		if(rand == null)
			rand = new Random();
		return rand;
	}

	public boolean isOnServer()
	{
		return level != null && !level.isClientSide;
	}

	public boolean isOnClient()
	{
		return level != null && level.isClientSide;
	}

	public CompoundTag writeNBT(CompoundTag nbt)
	{
		return NBTSerializationHelper.serialize(this);
	}

	public void readNBT(CompoundTag nbt)
	{
		NBTSerializationHelper.deserialize(this, nbt);
	}

	@Override
	public void saveAdditional(CompoundTag nbt)
	{
		super.saveAdditional(nbt);
		nbt.put("HL", writeNBT(new CompoundTag()));
	}

	@Override
	public void load(CompoundTag nbt)
	{
		super.load(nbt);
		readNBT(nbt.getCompound("HL"));
	}

	@Override
	public ClientboundBlockEntityDataPacket getUpdatePacket()
	{
		return ClientboundBlockEntityDataPacket.create(this);
	}

	@Override
	public void onDataPacket(Connection net, ClientboundBlockEntityDataPacket pkt)
	{
		CompoundTag tag = pkt.getTag();
		if(tag != null) handleUpdateTag(tag);
	}

	@Override
	public CompoundTag getUpdateTag()
	{
		return this.writeNBT(new CompoundTag());
	}

	@Override
	public void handleUpdateTag(CompoundTag tag)
	{
		this.readNBT(tag);
	}
}