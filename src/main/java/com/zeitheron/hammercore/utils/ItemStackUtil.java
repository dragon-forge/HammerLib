package com.zeitheron.hammercore.utils;

import java.util.Arrays;
import java.util.List;
import java.util.Random;

import com.zeitheron.hammercore.utils.inventory.InventoryDummy;

import it.unimi.dsi.fastutil.ints.IntList;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.ISidedInventory;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.Ingredient;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.NonNullList;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.items.CapabilityItemHandler;
import net.minecraftforge.items.IItemHandler;
import net.minecraftforge.oredict.OreDictionary;

/**
 * Contains some utilities to make life with {@link ItemStack}s easier
 */
public class ItemStackUtil
{
	public static void setItem(ItemStack stack, Item newItem)
	{
		stack.item = newItem;
	}
	
	public static ItemStack cycleItemStack(Object input)
	{
		NonNullList<ItemStack> q;
		ItemStack it = ItemStack.EMPTY;
		
		if(input instanceof ItemStack)
		{
			it = (ItemStack) input;
			
			if(it.getMetadata() == OreDictionary.WILDCARD_VALUE && it.getItem().getHasSubtypes())
			{
				NonNullList<ItemStack> q2 = NonNullList.create();
				it.getItem().getSubItems(it.getItem().getCreativeTab(), q2);
				if(q2 != null && q2.size() > 0)
				{
					int md = (int) (System.currentTimeMillis() / 1000 % q2.size());
					ItemStack it2 = new ItemStack(it.getItem(), 1, md);
					it2.setTagCompound(it.getTagCompound());
					it = it2;
				}
			} else if(it.getMetadata() == OreDictionary.WILDCARD_VALUE && it.isItemStackDamageable())
			{
				int md = (int) (System.currentTimeMillis() / 10 % it.getMaxDamage());
				ItemStack it2 = new ItemStack(it.getItem(), 1, md);
				it2.setTagCompound(it.getTagCompound());
				it = it2;
			}
			
			if(it.getMetadata() != 0 && !it.getHasSubtypes() && !it.isItemStackDamageable())
				it.setItemDamage(0);
		} else if(input instanceof ItemStack[])
		{
			ItemStack[] q3 = (ItemStack[]) input;
			if(q3 != null && q3.length > 0)
			{
				int idx = (int) (System.currentTimeMillis() / 1000L % q3.length);
				it = ItemStackUtil.cycleItemStack(q3[idx]);
			}
		} else if(input instanceof Ingredient)
			it = ItemStackUtil.cycleItemStack(((Ingredient) input).getMatchingStacks());
		else if(input instanceof List)
		{
			List q3 = (List) input;
			if(q3 != null && q3.size() > 0)
			{
				int idx = (int) (System.currentTimeMillis() / 1000L % q3.size());
				it = ItemStackUtil.cycleItemStack(q3.get(idx));
			}
		} else if(input instanceof String && !(q = OreDictionary.getOres(input + "")).isEmpty())
		{
			int idx = (int) (System.currentTimeMillis() / 1000L % q.size());
			it = ItemStackUtil.cycleItemStack(q.get(idx));
		}
		
		return it;
	}
	
	public static ItemStack compact(IInventory inv, ItemStack stack, IntList founds)
	{
		int count = 0;
		for(int i = 0; i < inv.getSizeInventory(); ++i)
			if(inv.getStackInSlot(i).isItemEqual(stack))
			{
				int j = inv.getStackInSlot(i).getCount();
				count += j;
				founds.add(i);
			}
		ItemStack st = stack.copy();
		st.setCount(count);
		return st;
	}
	
	public static boolean tagsEqual(NBTTagCompound a, NBTTagCompound b)
	{
		if(a == b)
			return true;
		if(a == null || b == null)
			return false;
		return a.equals(b);
	}
	
	public static boolean itemsEqual(ItemStack a, ItemStack b)
	{
		if(("" + a).equals("" + b))
			return true;
		if(InterItemStack.isStackNull(a) || InterItemStack.isStackNull(b))
			return false;
		return a.getItem() == b.getItem() && a.getItemDamage() == b.getItemDamage() && tagsEqual(a.getTagCompound(), b.getTagCompound());
	}
	
	public static boolean contains(ItemStack[] stacks, ItemStack stack)
	{
		stacks = Arrays.copyOf(stacks, stacks.length);
		stack = stack.copy();
		for(ItemStack s : stacks)
			if(itemsEqual(stack, s))
				return true;
		return false;
	}
	
	public static void dropStack(ItemStack stack, World world, BlockPos pos)
	{
		Random rand = world.rand;
		if(!stack.isEmpty() && !world.isRemote)
		{
			AxisAlignedBB aabb = world.getBlockState(pos).getCollisionBoundingBox(world, pos);
			if(aabb == null)
				aabb = new AxisAlignedBB(pos);
			
			EntityItem ei = new EntityItem(world, pos.getX() + (aabb.maxX - aabb.minY) / 2, pos.getY() + aabb.maxY, pos.getZ() + (aabb.maxZ - aabb.minZ) / 2, stack.copy());
			
			ei.motionX = (rand.nextDouble() - rand.nextDouble()) * 0.045;
			ei.motionY = rand.nextDouble() * 0.25;
			ei.motionZ = (rand.nextDouble() - rand.nextDouble()) * 0.045;
			
			world.spawnEntity(ei);
		}
	}
	
	public static void ejectOrDrop(ItemStack stack, TileEntity from)
	{
		if(stack.isEmpty())
			return;
		
		for(EnumFacing face : EnumFacing.VALUES)
		{
			TileEntity tile = from.getWorld().getTileEntity(from.getPos().offset(face));
			stack = inject(stack, tile, face.getOpposite());
			if(stack.isEmpty())
				break;
		}
		
		if(!stack.isEmpty())
			dropStack(stack, from.getWorld(), from.getPos());
	}
	
	public static ItemStack inject(ItemStack item, TileEntity tile, EnumFacing capFace)
	{
		return tile != null && tile.hasCapability(CapabilityItemHandler.ITEM_HANDLER_CAPABILITY, capFace) ? inject(item, tile.getCapability(CapabilityItemHandler.ITEM_HANDLER_CAPABILITY, capFace)) : item;
	}
	
	public static ItemStack inject(ItemStack item, IItemHandler h)
	{
		for(int i = 0; h != null && i < h.getSlots() && !item.isEmpty(); ++i)
			if(!item.isEmpty())
				item = h.insertItem(i, item, false);
		return item;
	}
	
	public static final class ItemDropData
	{
		public ItemDropData(int pickupDelayMode, int despawnTime)
		{
			this.pickupDelayMode = pickupDelayMode;
			this.despawnTime = despawnTime;
		}
		
		/** 0 - never, 1 - normal, 2 - instant */
		public int pickupDelayMode = 1;
		public int despawnTime = 6000;
	}
	
	public static void splitOrDropItemAround(ItemStack stack, World w, BlockPos from, ItemDropData data, EnumFacing... blackist)
	{
		stack = splitItemAround(stack, w, from, blackist);
		if(shouldReturn(stack))
			return;
		EntityItem i = new EntityItem(w, from.getX() + .5, from.getY() + 1, from.getZ() + .5, stack);
		i.setVelocity(0, 0, 0);
		i.ticksExisted = -data.despawnTime;
		if(data.pickupDelayMode == 0)
			i.setInfinitePickupDelay();
		else if(data.pickupDelayMode == 2)
			i.setNoPickupDelay();
		w.spawnEntity(i);
	}
	
	public static ItemStack splitItemAround(ItemStack stack, World w, BlockPos from, EnumFacing... blackist)
	{
		if(w.isRemote)
			return stack;
		List<EnumFacing> b = Arrays.asList(blackist);
		for(EnumFacing f : EnumFacing.VALUES)
		{
			if(b.contains(f))
				continue;
			TileEntity entity = w.getTileEntity(from.offset(f));
			if(entity instanceof IInventory)
			{
				if(entity instanceof ISidedInventory)
				{
					ISidedInventory i = (ISidedInventory) entity;
					stack = insertSided(i, stack, f.getOpposite());
				} else
				{
					IInventory i = (IInventory) entity;
					stack = insert(i, stack);
				}
			}
			
			if(shouldReturn(stack))
				return null;
		}
		return stack;
	}
	
	public static ItemStack insert(IInventory to, ItemStack what)
	{
		for(int i = 0; i < to.getSizeInventory(); ++i)
		{
			if(!to.isItemValidForSlot(i, what))
				continue;
			ItemStack in = to.getStackInSlot(i);
			if(in == null)
			{
				to.setInventorySlotContents(i, what);
				return null;
			} else if(itemsEqual(what, in) && InterItemStack.getStackSize(in) < in.getMaxStackSize())
			{
				int howM = InterItemStack.getStackSize(what);
				while(howM + InterItemStack.getStackSize(in) > in.getMaxStackSize())
					howM--;
				InterItemStack.setStackSize(what, InterItemStack.getStackSize(what) - howM);
				InterItemStack.setStackSize(in, InterItemStack.getStackSize(in) + howM);
			}
			if(shouldReturn(what))
				return null;
		}
		return what;
	}
	
	public static ItemStack insertSided(ISidedInventory to, ItemStack what, EnumFacing from)
	{
		for(int i = 0; i < to.getSizeInventory(); ++i)
		{
			if(!to.canInsertItem(i, what, from) && !to.isItemValidForSlot(i, what))
				continue;
			ItemStack in = to.getStackInSlot(i);
			if(in == null)
			{
				to.setInventorySlotContents(i, what);
				return null;
			} else if(itemsEqual(what, in) && InterItemStack.getStackSize(in) < in.getMaxStackSize())
			{
				int howM = InterItemStack.getStackSize(what);
				while(howM + InterItemStack.getStackSize(in) > in.getMaxStackSize())
					howM--;
				InterItemStack.setStackSize(what, InterItemStack.getStackSize(what) - howM);
				InterItemStack.setStackSize(in, InterItemStack.getStackSize(in) + howM);
			}
			if(shouldReturn(what))
				return null;
		}
		return what;
	}
	
	public static boolean shouldReturn(ItemStack stack)
	{
		return stack == null || InterItemStack.getStackSize(stack) <= 0;
	}
	
	public static NonNullList<ItemStack> getTopItems(IInventory inv, int count)
	{
		NonNullList<ItemStack> top = NonNullList.withSize(count, ItemStack.EMPTY);
		NonNullList<ItemStack> all = NonNullList.create();
		InventoryDummy invd = new InventoryDummy(0);
		invd.inventory = all;
		for(int i = 0; i < inv.getSizeInventory(); ++i)
		{
			ItemStack item = inv.getStackInSlot(i).copy();
			if(count(item, invd) == 0)
			{
				item.setCount(count(item, inv));
				all.add(item);
			}
		}
		all.sort((a, b) -> count(b, invd) - count(a, invd));
		for(int i = 0; i < Math.min(all.size(), count); ++i)
			top.set(i, all.get(i));
		return top;
	}
	
	public static int count(ItemStack stack, IInventory stacks)
	{
		int found = 0;
		for(int i = 0; i < stacks.getSizeInventory(); ++i)
			if(stack.isItemEqual(stacks.getStackInSlot(i)))
				found += stacks.getStackInSlot(i).getCount();
		return found;
	}
}