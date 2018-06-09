package com.pengu.hammercore.common.items;

import java.util.ArrayList;

import com.pengu.hammercore.HammerCore;
import com.pengu.hammercore.common.capabilities.CapabilityEJ;
import com.pengu.hammercore.common.utils.ChatUtil;
import com.pengu.hammercore.energy.iPowerContainerItem;
import com.pengu.hammercore.energy.iPowerStorage;
import com.zeitheron.hammercore.utils.charging.ItemChargeHelper;
import com.zeitheron.hammercore.utils.charging.fe.FECharge;

import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.ActionResult;
import net.minecraft.util.EnumHand;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.text.translation.I18n;
import net.minecraft.world.World;
import net.minecraftforge.energy.CapabilityEnergy;
import net.minecraftforge.energy.IEnergyStorage;

public class ItemBattery extends ItemFEBase
{
	public ItemBattery(int capacity, int maxIO)
	{
		super(capacity);
		maxExtract = maxIO;
		maxReceive = maxIO;
	}
	
	@Override
	public ActionResult<ItemStack> onItemRightClick(World worldIn, EntityPlayer playerIn, EnumHand handIn)
	{
		if(!worldIn.isRemote && playerIn.isSneaking())
		{
			ItemStack stack = playerIn.getHeldItem(handIn);
			setMode(stack, getMode(stack).cycle());
			HammerCore.audioProxy.playSoundAt(worldIn, "entity.experience_orb.pickup", playerIn.getPosition(), 1F, 2F, SoundCategory.PLAYERS);
			ChatUtil.sendNoSpam(playerIn, I18n.translateToLocal("info.hammercore:changed_mode") + ": " + getMode(stack));
		}
		
		return super.onItemRightClick(worldIn, playerIn, handIn);
	}
	
	@Override
	public void onUpdate(ItemStack stack, World worldIn, Entity entityIn, int itemSlot, boolean isSelected)
	{
		EnumBatteryShareMode mode = getMode(stack);
		
		if(mode.isChargingEntireInventory() && entityIn instanceof EntityPlayer)
		{
			FECharge leftover = ItemChargeHelper.chargePlayer((EntityPlayer) entityIn, new FECharge(getEnergyStored(stack)), false);
			setFE(stack, leftover.FE);
		} else
		{
			if(mode.isChargingArmor())
			{
				Iterable<ItemStack> armorStacks = entityIn.getArmorInventoryList();
				for(ItemStack a : armorStacks)
					chargeItem(stack, a);
			}
			
			if(mode.isChargingHotbar())
			{
				Iterable<ItemStack> heldEquipmentStacks = entityIn.getHeldEquipment();
				
				if(entityIn instanceof EntityPlayer)
				{
					InventoryPlayer inv = ((EntityPlayer) entityIn).inventory;
					ArrayList<ItemStack> stacks;
					heldEquipmentStacks = stacks = new ArrayList<>();
					for(int i = 0; i < 9; ++i)
						stacks.add(inv.getStackInSlot(i));
				}
				
				for(ItemStack a : heldEquipmentStacks)
					chargeItem(stack, a);
			}
		}
	}
	
	protected void chargeItem(ItemStack batteryStack, ItemStack targetStack)
	{
		// don't charge other active batteries
		if(targetStack.getItem() instanceof ItemBattery && getMode(targetStack) != EnumBatteryShareMode.NOT_SHARE)
			return;
		
		if(targetStack.hasCapability(CapabilityEnergy.ENERGY, null))
		{
			IEnergyStorage storage = targetStack.getCapability(CapabilityEnergy.ENERGY, null);
			if(storage != null && storage.canReceive())
				extractEnergy(batteryStack, storage.receiveEnergy(Math.min(getEnergyStored(batteryStack), maxExtract), false), false);
		} else if(targetStack.hasCapability(CapabilityEJ.ENERGY, null))
		{
			iPowerStorage storage = targetStack.getCapability(CapabilityEJ.ENERGY, null);
			if(storage != null)
				extractEnergy(batteryStack, storage.receiveEnergy(Math.min(getEnergyStored(batteryStack), maxExtract), false), false);
		} else if(targetStack.getItem() instanceof iPowerContainerItem)
		{
			iPowerContainerItem pci = (iPowerContainerItem) targetStack.getItem();
			extractEnergy(batteryStack, pci.receiveEnergy(targetStack, Math.min(getEnergyStored(batteryStack), maxExtract), false), false);
		}
	}
	
	@Override
	public boolean hasEffect(ItemStack stack)
	{
		return getMode(stack) != EnumBatteryShareMode.NOT_SHARE;
	}
	
	public EnumBatteryShareMode getMode(ItemStack stack)
	{
		if(stack.getTagCompound() == null)
			return EnumBatteryShareMode.NOT_SHARE;
		return EnumBatteryShareMode.values()[stack.getTagCompound().getByte("ShareMode") % EnumBatteryShareMode.values().length];
	}
	
	public void setMode(ItemStack stack, EnumBatteryShareMode mode)
	{
		if(stack.getTagCompound() == null)
			stack.setTagCompound(new NBTTagCompound());
		stack.getTagCompound().setByte("ShareMode", (byte) (mode != null ? mode.ordinal() : 1));
	}
	
	public enum EnumBatteryShareMode
	{
		NOT_SHARE("noshare"), //
		HOTBAR("hotbar"), //
		ARMOR("armor"), //
		ARMOR_AND_HOTBAR("armor_hotbar"), //
		ENTIRE_INVENTORY("inventory");
		
		private final String i18n;
		
		private EnumBatteryShareMode(String i18n)
		{
			this.i18n = i18n;
		}
		
		public EnumBatteryShareMode cycle()
		{
			return values()[(ordinal() + 1) % values().length];
		}
		
		public boolean isChargingArmor()
		{
			return this == EnumBatteryShareMode.ARMOR || this == ARMOR_AND_HOTBAR;
		}
		
		public boolean isChargingHotbar()
		{
			return this == EnumBatteryShareMode.HOTBAR || this == ARMOR_AND_HOTBAR;
		}
		
		public boolean isChargingEntireInventory()
		{
			return this == EnumBatteryShareMode.ENTIRE_INVENTORY;
		}
		
		@Override
		public String toString()
		{
			return I18n.translateToLocal("battery.hammercore:mode." + i18n);
		}
	}
}