package com.zeitheron.hammercore.utils;

import java.io.File;

import com.zeitheron.hammercore.HammerCore;
import com.zeitheron.hammercore.net.PacketContext;

import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.Entity;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.inventory.IInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.network.play.server.SPacketBlockBreakAnim;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraft.world.WorldServer;
import net.minecraftforge.fml.common.FMLCommonHandler;

public class WorldUtil
{
	public static <T> T cast(Object obj, Class<T> to)
	{
		if(obj != null && to.isAssignableFrom(obj.getClass()))
			return (T) obj;
		return null;
	}
	
	public static int getMoonPhase(World world)
	{
		return (int) ((world.getWorldTime() / 24000L % 8L + 8L) % 8);
	}
	
	public static EnumMoonPhase getEMoonPhase(World world)
	{
		return EnumMoonPhase.values()[getMoonPhase(world) % EnumMoonPhase.values().length];
	}
	
	public static World getWorld(PacketContext context, int dim)
	{
		return HammerCore.renderProxy.getWorld(context, dim);
	}
	
	public static File getWorldSubfile(String file)
	{
		MinecraftServer server = FMLCommonHandler.instance().getMinecraftServerInstance();
		return new File((server.isDedicatedServer() ? "" : "saves" + File.separator) + server.getFolderName(), file);
	}
	
	public static NBTTagList saveInv(IInventory inventory)
	{
		NBTTagList list = new NBTTagList();
		for(int i = 0; i < inventory.getSizeInventory(); ++i)
		{
			NBTTagCompound nbt = new NBTTagCompound();
			inventory.getStackInSlot(i).writeToNBT(nbt);
			nbt.setInteger("Slot", i);
			list.appendTag(nbt);
		}
		return list;
	}
	
	public static void readInv(NBTTagList list, IInventory inv)
	{
		for(int i = 0; i < list.tagCount(); ++i)
		{
			NBTTagCompound nbt = list.getCompoundTagAt(i);
			inv.setInventorySlotContents(nbt.getInteger("Slot"), new ItemStack(nbt));
		}
	}
	
	public static EntityItem spawnItemStack(World worldIn, double x, double y, double z, ItemStack stackIn)
	{
		EntityItem entityItem = new EntityItem(worldIn, x, y, z, stackIn);
		entityItem.motionX = 0;
		entityItem.motionZ = 0;
		if(!worldIn.isRemote)
			worldIn.spawnEntity(entityItem);
		return entityItem;
	}
	
	public static EnumFacing getFacing(int meta)
	{
		return EnumFacing.byIndex(meta & 7);
	}
	
	public static boolean isEnabled(int meta)
	{
		return (meta & 8) != 8;
	}
	
	public static EnumFacing getFacing(IBlockState state)
	{
		return getFacing(state.getBlock().getMetaFromState(state));
	}
	
	public static boolean isEnabled(IBlockState state)
	{
		return isEnabled(state.getBlock().getMetaFromState(state));
	}
	
	public static EntityItem spawnItemStack(World worldIn, BlockPos pos, ItemStack stackIn)
	{
		return spawnItemStack(worldIn, pos.getX() + .5, pos.getY() + .5, pos.getZ() + .5, stackIn);
	}
	
	public static EntityItem spawnItemStack(WorldLocation loc, ItemStack stackIn)
	{
		return spawnItemStack(loc.getWorld(), loc.getPos(), stackIn);
	}
	
	public static void breakBlockPartially(World world, int breakerId, BlockPos pos, int progress)
	{
		for(EntityPlayer entityplayer : world.playerEntities)
		{
			if(entityplayer == null || !(entityplayer instanceof EntityPlayerMP) || entityplayer.world != FMLCommonHandler.instance().getMinecraftServerInstance().getEntityWorld() || entityplayer.getEntityId() == breakerId || entityplayer.getDistanceSq(pos) >= 1024.0)
				continue;
			((EntityPlayerMP) entityplayer).connection.sendPacket(new SPacketBlockBreakAnim(breakerId, pos, progress));
		}
	}
	
	public static void teleportPlayer(EntityPlayerMP mp, int dim, double x, double y, double z)
	{
		if(!mp.world.isRemote)
		{
			// if(mp.world.provider.getDimension() != dim)
			// {
			// MinecraftServer server = mp.mcServer;
			// WorldServer nev = server.getWorld(dim);
			// server.getPlayerList().transferPlayerToDimension(mp, dim, new
			// BlankTeleporter(nev));
			// }
			
			TeleporterDimPos.of(x, y, z, dim).teleport(mp);
			
			// mp.setPositionAndUpdate(x, y, z);
		}
	}
	
	public static void teleportEntity(Entity ent, int dim, double x, double y, double z)
	{
		if(!ent.world.isRemote)
		{
			if(ent.world.provider.getDimension() != dim)
			{
				MinecraftServer server = FMLCommonHandler.instance().getMinecraftServerInstance();
				
				WorldServer old = server.getWorld(ent.world.provider.getDimension());
				WorldServer nev = server.getWorld(dim);
				
				server.getPlayerList().transferEntityToWorld(ent, ent.world.provider.getDimension(), old, nev, new BlankTeleporter(nev));
			}
			
			ent.setPositionAndUpdate(x, y, z);
		}
	}
	
	public static void teleportEntity(Entity ent, double x, double y, double z)
	{
		teleportEntity(ent, ent.world.provider.getDimension(), x, y, z);
	}
	
	public static void teleportEntity(Entity ent, int dim)
	{
		teleportEntity(ent, dim, ent.posX, ent.posY, ent.posZ);
	}
}