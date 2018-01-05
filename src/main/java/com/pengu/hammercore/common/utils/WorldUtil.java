package com.pengu.hammercore.common.utils;

import java.io.File;

import com.pengu.hammercore.HammerCore;
import com.pengu.hammercore.utils.WorldLocation;

import net.minecraft.entity.Entity;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.inventory.IInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraft.world.WorldServer;
import net.minecraftforge.fml.common.FMLCommonHandler;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;

public class WorldUtil
{
	public static <T> T cast(Object obj, Class<T> to)
	{
		if(obj != null && to.isAssignableFrom(obj.getClass()))
			return (T) obj;
		return null;
	}
	
	public static World getWorld(MessageContext context, int dim)
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
	
	public static EntityItem spawnItemStack(World worldIn, BlockPos pos, ItemStack stackIn)
	{
		return spawnItemStack(worldIn, pos.getX() + .5, pos.getY() + .5, pos.getZ() + .5, stackIn);
	}
	
	public static EntityItem spawnItemStack(WorldLocation loc, ItemStack stackIn)
	{
		return spawnItemStack(loc.getWorld(), loc.getPos(), stackIn);
	}
	
	public static void teleportPlayer(EntityPlayerMP mp, int dim, double x, double y, double z)
	{
		if(!mp.world.isRemote)
		{
			if(mp.world.provider.getDimension() != dim)
			{
				MinecraftServer server = mp.mcServer;
				WorldServer nev = server.getWorld(dim);
				server.getPlayerList().transferPlayerToDimension(mp, dim, new BlankTeleporter(nev));
			}
			
			mp.setPositionAndUpdate(x, y, z);
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