package com.zeitheron.hammercore.api.events;

import com.zeitheron.hammercore.asm.McHooks;
import com.zeitheron.hammercore.asm.McHooks.EnumMcHook;

import net.minecraft.entity.item.EntityItem;
import net.minecraft.util.DamageSource;
import net.minecraftforge.event.entity.EntityEvent;
import net.minecraftforge.fml.common.eventhandler.Cancelable;

@Cancelable
public class EntityItemHurtEvent extends EntityEvent
{
	private final EntityItem entityItem;
	private final DamageSource src;
	private final float amount;
	
	public EntityItemHurtEvent(EntityItem entityItem, DamageSource src, float amount)
	{
		super(entityItem);
		this.entityItem = entityItem;
		this.src = src;
		this.amount = amount;
	}
	
	public EntityItem getEntityItem()
	{
		return entityItem;
	}
	
	public DamageSource getDamageSrc()
	{
		return src;
	}
	
	public float getAmount()
	{
		return amount;
	}
	
	public static void enable()
	{
		McHooks.enableFeature(EnumMcHook.ENTITY_ITEM_HURT);
	}
}