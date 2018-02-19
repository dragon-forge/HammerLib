package com.pengu.hammercore.tile;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Random;

import javax.annotation.Nullable;

import com.pengu.hammercore.HammerCore;
import com.pengu.hammercore.net.HCNetwork;
import com.pengu.hammercore.net.pkt.PacketSyncSyncableTile;
import com.pengu.hammercore.net.utils.NetPropertyAbstract;
import com.pengu.hammercore.net.utils.iPropertyChangeHandler;
import com.pengu.hammercore.utils.WorldLocation;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.ISidedInventory;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.network.NetworkManager;
import net.minecraft.network.play.server.SPacketUpdateTileEntity;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.world.World;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.util.Constants.NBT;
import net.minecraftforge.fml.common.network.NetworkRegistry.TargetPoint;
import net.minecraftforge.fml.common.network.internal.FMLNetworkHandler;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import net.minecraftforge.items.CapabilityItemHandler;
import net.minecraftforge.items.IItemHandler;
import net.minecraftforge.items.wrapper.InvWrapper;
import net.minecraftforge.items.wrapper.SidedInvWrapper;

public abstract class TileSyncable extends TileEntity implements iPropertyChangeHandler
{
	protected World readNBT_world;
	private final List<NetPropertyAbstract> properties = new ArrayList<>();
	private NBTTagCompound lastSyncTag;
	protected WorldLocation loc;
	protected Random rand = new Random();
	
	/**
	 * Turn this to false to force this tile to sync even if it's old and new
	 * tags are equal
	 */
	public boolean escapeSyncIfIdentical = false;
	
	{
		initProperties();
	}
	
	/** Called while constructing this tile */
	public void initProperties()
	{
		
	}
	
	public Random getRNG()
	{
		if(rand == null)
			rand = new Random();
		return rand;
	}
	
	@Override
	public void markDirty()
	{
		super.markDirty();
		sync();
	}
	
	public void sync()
	{
		getLocation();
		
		if(escapeSyncIfIdentical)
		{
			NBTTagCompound nbt = new NBTTagCompound();
			writeNBT(nbt);
			if(lastSyncTag != null && lastSyncTag.equals(nbt))
				return; // Escape unnecessary sync if it is the same
			lastSyncTag = nbt;
		}
		
		if(world != null && !world.isRemote) // Apply sync only if server
		{
			PacketSyncSyncableTile tile = new PacketSyncSyncableTile(this);
			HCNetwork.manager.sendToAllAround(tile, getSyncPoint(260));
		}
	}
	
	@Override
	public NBTTagCompound getUpdateTag()
	{
		return writeToNBT(new NBTTagCompound());
	}
	
	@Override
	public SPacketUpdateTileEntity getUpdatePacket()
	{
		return new SPacketUpdateTileEntity(pos, 0, getUpdateTag());
	}
	
	@Override
	public void onDataPacket(NetworkManager net, SPacketUpdateTileEntity pkt)
	{
		readFromNBT(pkt.getNbtCompound());
	}
	
	public TargetPoint getSyncPoint(int range)
	{
		return new TargetPoint(world.provider.getDimension(), pos.getX(), pos.getY(), pos.getZ(), range);
	}
	
	public abstract void writeNBT(NBTTagCompound nbt);
	
	public abstract void readNBT(NBTTagCompound nbt);
	
	@Override
	public final NBTTagCompound writeToNBT(NBTTagCompound nbt)
	{
		nbt = super.writeToNBT(nbt);
		
		{
			NBTTagCompound tag = new NBTTagCompound();
			writeNBT(tag);
			nbt.setTag("Tags", tag);
		}
		
		if(this instanceof TileSyncableTickable)
			nbt.setInteger("TicksExisted", ((TileSyncableTickable) this).ticksExisted);
		
		NBTTagList props = new NBTTagList();
		
		for(NetPropertyAbstract prop : properties)
		{
			NBTTagCompound tag = new NBTTagCompound();
			prop.writeToNBT(tag);
			tag.setString("Class", prop.getClass().getName());
			tag.setInteger("Id", properties.indexOf(prop));
			props.appendTag(tag);
		}
		
		nbt.setTag("Properties", props);
		
		return nbt;
	}
	
	@Override
	public final void readFromNBT(NBTTagCompound nbt)
	{
		if(readNBT_world == null && world != null)
			readNBT_world = world;
		
		super.readFromNBT(nbt);
		
		if(!nbt.hasKey("Tags", NBT.TAG_COMPOUND))
			HammerCore.LOG.warn("TileEntity " + this + " tried to load old NBT Key: \"tags\". It is going to be renamed to \"Tags\"!");
		readNBT(!nbt.hasKey("Tags", NBT.TAG_COMPOUND) ? nbt.getCompoundTag("tags") : nbt.getCompoundTag("Tags"));
		
		if(this instanceof TileSyncableTickable)
			((TileSyncableTickable) this).ticksExisted = nbt.getInteger("TicksExisted");
		
		NBTTagList props = nbt.getTagList("Properties", NBT.TAG_COMPOUND);
		
		for(int i = 0; i < props.tagCount(); ++i)
		{
			try
			{
				NBTTagCompound tag = props.getCompoundTagAt(i);
				int id = tag.getInteger("Id");
				NetPropertyAbstract prop;
				
				if(properties.size() > id)
				{
					prop = properties.get(id);
					prop.readFromNBT(tag);
				}
			} catch(Throwable err)
			{
			}
		}
		
		readNBT_world = null;
	}
	
	public WorldLocation getLocation()
	{
		if((loc == null && pos != null) || (loc != null && pos != null && !loc.getPos().equals(pos)))
			loc = new WorldLocation(world, pos);
		return loc;
	}
	
	@Override
	protected void setWorldCreate(World worldIn)
	{
		readNBT_world = worldIn;
	}
	
	/**
	 * Was previously "new {@link SidedInvWrapper}[6]", which could give
	 * {@link ArrayStoreException} Now "new {@link IItemHandler}[6]"
	 * 
	 * @since 1.7.1
	 */
	protected IItemHandler[] itemHandlers = new IItemHandler[6];
	
	protected IItemHandler createSidedHandler(EnumFacing side)
	{
		if(this instanceof ISidedInventory)
		{
			if(side == null)
				return null;
			return itemHandlers[side.ordinal()] = new SidedInvWrapper((ISidedInventory) this, side);
		}
		
		if(this instanceof IInventory)
		{
			int ord = side == null ? 0 : side.ordinal();
			return itemHandlers[ord] = new InvWrapper((IInventory) this);
		}
		
		return null;
	}
	
	@Override
	@Nullable
	public <T> T getCapability(Capability<T> capability, @Nullable EnumFacing facing)
	{
		if(capability == CapabilityItemHandler.ITEM_HANDLER_CAPABILITY && this instanceof IInventory)
			return (T) createSidedHandler(facing);
		return super.getCapability(capability, facing);
	}
	
	@Override
	public boolean hasCapability(Capability<?> capability, @Nullable EnumFacing facing)
	{
		return (capability == CapabilityItemHandler.ITEM_HANDLER_CAPABILITY && this instanceof IInventory) || super.hasCapability(capability, facing);
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
	
	/** NEW PROPERTY API */
	
	@Override
	public int registerProperty(NetPropertyAbstract prop)
	{
		if(properties.contains(prop))
			return properties.indexOf(prop);
		properties.add(prop);
		return properties.size() - 1;
	}
	
	@Override
	public void load(int id, NBTTagCompound nbt)
	{
		if(id >= 0 && id < properties.size())
			properties.get(id).readFromNBT(nbt);
	}
	
	@Override
	public void notifyOfChange(NetPropertyAbstract prop)
	{
	}
	
	@Override
	public void sendChangesToNearby()
	{
		sync();
	}
	
	@SideOnly(Side.CLIENT)
	public void addProperties(Map<String, Object> properties, RayTraceResult trace)
	{
		
	}
}