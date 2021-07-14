package org.zeith.hammerlib.tiles;

import net.minecraft.block.BlockState;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.network.NetworkManager;
import net.minecraft.network.play.server.SUpdateTileEntityPacket;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.tileentity.TileEntityType;
import org.zeith.hammerlib.api.io.NBTSerializationHelper;
import org.zeith.hammerlib.api.tiles.ISyncableTile;
import org.zeith.hammerlib.net.properties.IPropertyTile;
import org.zeith.hammerlib.net.properties.PropertyDispatcher;

import java.util.Random;

public class TileSyncable
		extends TileEntity
		implements ISyncableTile, IPropertyTile
{
	protected final PropertyDispatcher dispatcher = new PropertyDispatcher(this::syncProperties);
	protected Random rand = new Random();

	public TileSyncable(TileEntityType<?> type)
	{
		super(type);
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

	public CompoundNBT writeNBT(CompoundNBT nbt)
	{
		return NBTSerializationHelper.serialize(this);
	}

	public void readNBT(CompoundNBT nbt)
	{
		NBTSerializationHelper.deserialize(this, nbt);
	}

	@Override
	public CompoundNBT save(CompoundNBT nbt)
	{
		nbt = super.save(nbt);
		nbt.put("HL", writeNBT(new CompoundNBT()));
		return nbt;
	}

	@Override
	public void load(BlockState stateIn, CompoundNBT nbtIn)
	{
		super.load(stateIn, nbtIn);
		readNBT(nbtIn.getCompound("HL"));
	}

	@Override
	public SUpdateTileEntityPacket getUpdatePacket()
	{
		return new SUpdateTileEntityPacket(this.worldPosition, 0, this.getUpdateTag());
	}

	@Override
	public void onDataPacket(NetworkManager net, SUpdateTileEntityPacket packet)
	{
		this.readNBT(packet.getTag());
	}

	@Override
	public CompoundNBT getUpdateTag()
	{
		return this.writeNBT(new CompoundNBT());
	}
}