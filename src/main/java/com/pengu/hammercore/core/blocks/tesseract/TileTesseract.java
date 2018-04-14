package com.pengu.hammercore.core.blocks.tesseract;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import com.pengu.hammercore.common.capabilities.CapabilityEJ;
import com.pengu.hammercore.core.gui.GuiTesseract;
import com.pengu.hammercore.core.gui.container.ContainerEmpty;
import com.pengu.hammercore.core.init.BlocksHC;
import com.pengu.hammercore.core.init.ItemsHC;
import com.pengu.hammercore.net.utils.NetPropertyBool;
import com.pengu.hammercore.net.utils.NetPropertyNumber;
import com.pengu.hammercore.net.utils.NetPropertyString;
import com.pengu.hammercore.net.utils.NetPropertyUUID;
import com.pengu.hammercore.tile.TileSyncableTickable;
import com.pengu.hammercore.tile.iMalfunctionable;
import com.pengu.hammercore.tile.iTileDroppable;

import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.util.text.translation.I18n;
import net.minecraft.world.World;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.util.Constants.NBT;
import net.minecraftforge.energy.CapabilityEnergy;
import net.minecraftforge.fluids.capability.CapabilityFluidHandler;
import net.minecraftforge.items.CapabilityItemHandler;

public class TileTesseract extends TileSyncableTickable implements iTileDroppable, iMalfunctionable
{
	public static final Map<String, List<TileTesseract>> TESSERACTS = new HashMap<>();
	
	public final NetPropertyString frequency;
	public final NetPropertyUUID owner;
	public final NetPropertyBool isPrivate;
	public final NetPropertyNumber<Integer> ioPage;
	private int malfunctionTicks = 0;
	
	{
		frequency = new NetPropertyString(this, "");
		owner = new NetPropertyUUID(this);
		isPrivate = new NetPropertyBool(this);
		ioPage = new NetPropertyNumber<Integer>(this, 0);
	}
	
	public final Map<Capability, TransferMode> MODES = new HashMap<>();
	private static final List<Capability> ALLOWED_CAPABILITIES = new ArrayList<>();
	private static final List<String> ALLOWED_CAPABILITY_NAMES = new ArrayList<>();
	private static final List<ItemStack> ALLOWED_CAPABILITY_ICONS = new ArrayList<>();
	
	static
	{
		registerTesseractCapability(CapabilityEnergy.ENERGY, "fe", new ItemStack(ItemsHC.BATTERY));
		registerTesseractCapability(CapabilityEJ.ENERGY, "ej", new ItemStack(ItemsHC.BATTERY));
		registerTesseractCapability(CapabilityFluidHandler.FLUID_HANDLER_CAPABILITY, "fluid", new ItemStack(Items.BUCKET));
		registerTesseractCapability(CapabilityItemHandler.ITEM_HANDLER_CAPABILITY, "items", new ItemStack(Items.STICK));
	}
	
	public static void registerTesseractCapability(Capability cap, String id, ItemStack icon)
	{
		ALLOWED_CAPABILITIES.add(cap);
		ALLOWED_CAPABILITY_NAMES.add(id);
		ALLOWED_CAPABILITY_ICONS.add(icon);
	}
	
	@Override
	public void addProperties(Map<String, Object> properties, RayTraceResult trace)
	{
		properties.put("private", isPrivate.get());
		
		String frequency = this.frequency.get();
		
		if(frequency != null && !frequency.isEmpty())
			for(int i = 0; i < ALLOWED_CAPABILITIES.size(); ++i)
			{
				Capability cap = ALLOWED_CAPABILITIES.get(i);
				properties.put(getCapName(cap), getMode(cap) == TransferMode.ALLOW);
			}
		
		properties.put((frequency != null && !frequency.isEmpty() ? TextFormatting.GREEN : TextFormatting.RED) + "frequency" + TextFormatting.RESET, frequency != null && !frequency.isEmpty() ? frequency : TextFormatting.RED + "?" + TextFormatting.RESET);
	}
	
	public static List<Capability> getAllowedCapabilities()
	{
		return ALLOWED_CAPABILITIES;
	}
	
	public static String getCapNameRaw(Capability cap)
	{
		int index = ALLOWED_CAPABILITIES.indexOf(cap);
		if(index == -1)
			return null;
		return ALLOWED_CAPABILITY_NAMES.get(index);
	}
	
	public static ItemStack getCapIcon(Capability cap)
	{
		int index = ALLOWED_CAPABILITIES.indexOf(cap);
		if(index == -1)
			return null;
		return ALLOWED_CAPABILITY_ICONS.get(index).copy();
	}
	
	public static String getCapName(Capability cap)
	{
		int index = ALLOWED_CAPABILITIES.indexOf(cap);
		if(index == -1)
			return null;
		return I18n.translateToLocal("capability." + ALLOWED_CAPABILITY_NAMES.get(index) + ".name");
	}
	
	@Override
	public void onPlacedBy(EntityPlayer player, EnumHand hand)
	{
		owner.set(player.getGameProfile().getId());
	}
	
	@Override
	public void tick()
	{
		if(malfunctionTicks > 0)
			malfunctionTicks--;
		
		// Put all tesseracts
		if(frequency.get() != null && !frequency.get().isEmpty() && malfunctionTicks <= 0 && atTickRate(20))
			addTesseract(this);
		if(frequency.get() != null && !frequency.get().isEmpty() && malfunctionTicks > 0 && atTickRate(20))
			removeTesseract(this);
		
		if(atTickRate(20))
		{
			IBlockState state = world.getBlockState(pos);
			if(state.getBlock() == BlocksHC.TESSERACT)
			{
				boolean isActive = state.getValue(BlockTesseract.active);
				boolean shouldBeActive = frequency.get() != null && !frequency.get().isEmpty() && malfunctionTicks <= 0;
				
				if(isActive != shouldBeActive)
				{
					state = state.withProperty(BlockTesseract.active, shouldBeActive);
					world.setBlockState(pos, state, 1);
					validate();
					world.setTileEntity(pos, this);
				}
			}
		}
	}
	
	@Override
	public Object getClientGuiElement(EntityPlayer player)
	{
		return new GuiTesseract(this);
	}
	
	@Override
	public Object getServerGuiElement(EntityPlayer player)
	{
		return new ContainerEmpty();
	}
	
	@Override
	public boolean hasGui()
	{
		return true;
	}
	
	@Override
	public void writeNBT(NBTTagCompound nbt)
	{
		NBTTagList list = new NBTTagList();
		for(Capability c : MODES.keySet())
		{
			TransferMode mode = getMode(c);
			NBTTagCompound tag = new NBTTagCompound();
			tag.setString("Name", getCapNameRaw(c));
			tag.setBoolean("Active", mode.active());
			list.appendTag(tag);
		}
		nbt.setTag("Caps", list);
	}
	
	@Override
	public void readNBT(NBTTagCompound nbt)
	{
		NBTTagList list = nbt.getTagList("Caps", NBT.TAG_COMPOUND);
		for(int i = 0; i < list.tagCount(); ++i)
		{
			NBTTagCompound tag = list.getCompoundTagAt(i);
			int index = ALLOWED_CAPABILITY_NAMES.indexOf(tag.getString("Name"));
			if(index != -1)
				setMode(ALLOWED_CAPABILITIES.get(index), tag.getBoolean("Active") ? TransferMode.ALLOW : TransferMode.DECLINE);
		}
	}
	
	public void setFrequency(String freq)
	{
		frequency.set(freq);
		if(freq != null && !freq.isEmpty())
			addTesseract(this);
		else
			removeTesseract(this);
		
		IBlockState state = world.getBlockState(pos);
		if(state.getBlock() == BlocksHC.TESSERACT)
		{
			boolean isActive = state.getValue(BlockTesseract.active);
			boolean shouldBeActive = frequency.get() != null && !frequency.get().isEmpty();
			
			if(isActive != shouldBeActive)
			{
				state = state.withProperty(BlockTesseract.active, shouldBeActive);
				world.setBlockState(pos, state, 1);
				validate();
				world.setTileEntity(pos, this);
			}
		}
	}
	
	public boolean isValid()
	{
		return world != null && pos != null && world.isBlockLoaded(pos) && world.getTileEntity(pos) == this && frequency.get() != null && !frequency.get().isEmpty();
	}
	
	public static void revalidateTesseracts()
	{
		Iterator<List<TileTesseract>> tiles = TESSERACTS.values().iterator();
		
		while(tiles.hasNext())
		{
			List<TileTesseract> tess = tiles.next();
			if(tess == null || tess.isEmpty())
				continue;
			for(int i = 0; i < tess.size(); ++i)
			{
				TileTesseract tess2 = tess.get(i);
				if(tess2 == null || !tess2.isValid())
					tess.remove(i);
			}
			if(tess.isEmpty())
				tiles.remove();
		}
		
		// System.out.println(TESSERACTS);
	}
	
	public final String getMapName()
	{
		return (isPrivate.get() ? owner.get() + "" : "public") + ":" + frequency.get();
	}
	
	public static void addTesseract(TileTesseract tesseract)
	{
		removeTesseract(tesseract);
		if(tesseract == null || tesseract.frequency.get() == null || tesseract.frequency.get().isEmpty())
			return;
		List<TileTesseract> tiles = TESSERACTS.get(tesseract.getMapName());
		if(tiles == null)
			TESSERACTS.put(tesseract.getMapName(), tiles = new ArrayList<>());
		if(!tiles.contains(tesseract))
			tiles.add(tesseract);
	}
	
	public static void removeTesseract(TileTesseract tesseract)
	{
		Iterator<List<TileTesseract>> tiles = TESSERACTS.values().iterator();
		
		while(tiles.hasNext())
		{
			List<TileTesseract> tess = tiles.next();
			if(tess == null || tess.isEmpty())
			{
				tiles.remove();
				continue;
			}
			for(int i = 0; i < tess.size(); ++i)
			{
				TileTesseract tess2 = tess.get(i);
				if(tess2 == tesseract)
					tess.remove(i);
			}
			if(tess.isEmpty())
				tiles.remove();
		}
	}
	
	@Override
	public void onChunkUnload()
	{
		super.onChunkUnload();
		removeTesseract(this);
	}
	
	@Override
	public void createDrop(EntityPlayer player, World world, BlockPos pos)
	{
		removeTesseract(this);
	}
	
	public <T> T getSidedCapability(Capability<T> cap, EnumFacing facing)
	{
		if(facing == null)
		{
			for(EnumFacing face : EnumFacing.VALUES)
			{
				T cap0 = getSidedCapability(cap, face);
				if(cap0 != null)
					return cap0;
			}
			return null;
		}
		
		if(isValid())
		{
			BlockPos npos = pos.offset(facing.getOpposite());
			if(world.isBlockLoaded(npos) && world.getTileEntity(npos) != null)
			{
				TileEntity tile = world.getTileEntity(npos);
				if(tile instanceof TileTesseract)
					return null;
				T c = tile.getCapability(cap, facing);
				if(c != null)
					return c;
			}
		}
		
		return null;
	}
	
	public <T> boolean hasSidedCapability(Capability<T> cap, EnumFacing facing)
	{
		if(facing == null)
		{
			for(EnumFacing face : EnumFacing.VALUES)
				if(hasSidedCapability(cap, face))
					return true;
			return false;
		}
		
		if(isValid())
		{
			BlockPos npos = pos.offset(facing.getOpposite());
			if(world.isBlockLoaded(npos) && world.getTileEntity(npos) != null)
			{
				TileEntity tile = world.getTileEntity(npos);
				if(tile instanceof TileTesseract)
					return false;
				if(tile.hasCapability(cap, facing))
					return true;
			}
		}
		
		return false;
	}
	
	public void setMode(Capability capability, TransferMode mode)
	{
		if(!ALLOWED_CAPABILITIES.contains(capability))
			return;
		MODES.put(capability, mode);
	}
	
	public TransferMode getMode(Capability capability)
	{
		if(!ALLOWED_CAPABILITIES.contains(capability))
			return TransferMode.DECLINE;
		if(MODES.get(capability) == null)
			MODES.put(capability, TransferMode.ALLOW);
		return MODES.get(capability);
	}
	
	@Override
	public <T> T getCapability(Capability<T> capability, EnumFacing facing)
	{
		if(!getMode(capability).active())
			return null;
		
		if(isValid())
		{
			List<TileTesseract> tile = TESSERACTS.get(getMapName());
			
			if(tile == null)
			{
				TESSERACTS.put(getMapName(), tile = new ArrayList<>());
				tile.add(this);
			}
			
			for(int i = 0; i < tile.size(); ++i)
			{
				TileTesseract tess = tile.get(i);
				if(tess == this || !tess.getMode(capability).active())
					continue;
				T cap = tess.getSidedCapability(capability, null);
				if(tess != null && cap != null)
					return cap;
			}
		}
		
		return super.getCapability(capability, facing);
	}
	
	@Override
	public boolean hasCapability(Capability<?> capability, EnumFacing facing)
	{
		return getCapability(capability, facing) != null;
	}
	
	@Override
	public void causeGeneralMalfunction()
	{
		malfunctionTicks += 80 + world.rand.nextInt(80);
	}
}