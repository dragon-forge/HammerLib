package com.zeitheron.hammercore.event;

import com.zeitheron.hammercore.utils.base.Cast;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraftforge.event.entity.living.LivingEvent;

/**
 * Called when entity has finished using an item.
 */
public class ItemUseFinishEvent extends LivingEvent
{
	public final ItemStack stack;
	private final ItemStack copy;
	
	public ItemUseFinishEvent(EntityLivingBase living, ItemStack stack)
	{
		super(living);
		this.copy = stack.copy();
		this.stack = stack;
	}
	
	public ItemStack getOriginStack()
	{
		return copy.copy();
	}
	
	public boolean isPlayer()
	{
		return getEntityLiving() instanceof EntityPlayer;
	}
	
	public EntityPlayer getEntityPlayer()
	{
		return Cast.cast(getEntityLiving(), EntityPlayer.class);
	}
}