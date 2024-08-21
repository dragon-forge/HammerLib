package org.zeith.hammerlib.tiles;

import com.zeitheron.hammercore.HammerCore;
import com.zeitheron.hammercore.api.io.NBTSerializationHelper;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.NetworkManager;
import net.minecraft.network.play.server.SPacketUpdateTileEntity;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumHand;
import net.minecraft.world.World;
import net.minecraftforge.common.util.Constants.NBT;
import net.minecraftforge.fml.common.network.NetworkRegistry.TargetPoint;
import net.minecraftforge.fml.common.network.internal.FMLNetworkHandler;
import org.zeith.hammerlib.abstractions.sources.IObjectSource;
import org.zeith.hammerlib.api.tiles.ISyncableTile;
import org.zeith.hammerlib.net.properties.IPropertyTile;
import org.zeith.hammerlib.net.properties.PropertyDispatcher;

import java.util.Random;

public abstract class TileSyncable
		extends TileEntity
		implements ISyncableTile, IPropertyTile
{
	protected World readNBT_world;
	protected final PropertyDispatcher dispatcher = new PropertyDispatcher(IObjectSource.ofTile(this), this::syncProperties);
	protected Random rand = new Random();
	
	public Random getRNG()
	{
		if(rand == null) rand = new Random();
		return rand;
	}
	
	@Override
	public void markDirty()
	{
		super.markDirty();
		sync();
	}
	
	@Override
	public NBTTagCompound getUpdateTag()
	{
		return writeNBT(new NBTTagCompound());
	}
	
	@Override
	public SPacketUpdateTileEntity getUpdatePacket()
	{
		return new SPacketUpdateTileEntity(pos, 0, getUpdateTag());
	}
	
	@Override
	public void onDataPacket(NetworkManager net, SPacketUpdateTileEntity pkt)
	{
		readNBT(pkt.getNbtCompound());
	}
	
	public TargetPoint getSyncPoint(int range)
	{
		return new TargetPoint(world.provider.getDimension(), pos.getX(), pos.getY(), pos.getZ(), range);
	}
	
	public NBTTagCompound writeNBT(NBTTagCompound nbt)
	{
		nbt.merge(NBTSerializationHelper.serialize(this));
		return nbt;
	}
	
	public void readNBT(NBTTagCompound nbt)
	{
		NBTSerializationHelper.deserialize(this, nbt);
	}
	
	@Override
	public final NBTTagCompound writeToNBT(NBTTagCompound nbt)
	{
		nbt = super.writeToNBT(nbt);
		
		{
			NBTTagCompound tag = new NBTTagCompound();
			writeNBT(tag);
			nbt.setTag("HL", tag);
		}
		
		if(this instanceof TileSyncableTickable)
			nbt.setInteger("TicksExisted", ((TileSyncableTickable) this).ticksExisted);
		
		return nbt;
	}
	
	@Override
	public final void readFromNBT(NBTTagCompound nbt)
	{
		if(readNBT_world == null && world != null)
			readNBT_world = world;
		
		super.readFromNBT(nbt);
		
		if(!nbt.hasKey("HL", NBT.TAG_COMPOUND))
			HammerCore.LOG.warn("TileEntity {} tried to load old NBT Key: \"Tags\". It is going to be renamed to \"HL\"!", this);
		readNBT(!nbt.hasKey("HL", NBT.TAG_COMPOUND) ? nbt.getCompoundTag("Tags") : nbt.getCompoundTag("HL"));
		
		if(this instanceof TileSyncableTickable)
			((TileSyncableTickable) this).ticksExisted = nbt.getInteger("TicksExisted");
		
		readNBT_world = null;
	}
	
	@Override
	protected void setWorldCreate(World worldIn)
	{
		this.world = worldIn;
		readNBT_world = worldIn;
	}
	
	public boolean atTickRate(int rate)
	{
		return (world.getTotalWorldTime() + pos.toLong()) % rate == 0;
	}
	
	public final void tryOpenGui(EntityPlayer player, World world)
	{
		if(!world.isRemote)
			FMLNetworkHandler.openGui(player, HammerCore.instance, 0, world, pos.getX(), pos.getY(), pos.getZ());
	}
	
	public void onPlacedBy(EntityPlayer player, EnumHand hand)
	{
	
	}
	
	/** NEW GUI API */
	
	public boolean hasGui()
	{
		return false;
	}
	
	public Object getServerGuiElement(EntityPlayer player)
	{
		return null;
	}
	
	public Object getClientGuiElement(EntityPlayer player)
	{
		return null;
	}
	
	@Override
	public PropertyDispatcher getProperties()
	{
		return dispatcher;
	}
}