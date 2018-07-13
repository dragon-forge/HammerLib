package com.zeitheron.hammercore.specials.zeitheron;

import java.util.UUID;

import com.zeitheron.hammercore.ServerHCClientPlayerData;
import com.zeitheron.hammercore.annotations.MCFBus;
import com.zeitheron.hammercore.client.HCClientOptions;
import com.zeitheron.hammercore.client.color.PlayerInterpolator;
import com.zeitheron.hammercore.net.HCNet;
import com.zeitheron.hammercore.utils.color.ColorNamePicker;
import com.zeitheron.hammercore.utils.color.Rainbow;

import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.entity.ai.attributes.AttributeModifier;
import net.minecraft.entity.ai.attributes.IAttributeInstance;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraftforge.common.util.Constants.NBT;
import net.minecraftforge.event.entity.living.LivingHurtEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent.Phase;
import net.minecraftforge.fml.common.gameevent.TickEvent.PlayerTickEvent;

@MCFBus(log = false)
public class ZeitheronGlobEvents
{
	public float calculateMultipl(EntityPlayer player)
	{
		float addf = 0;
		HCClientOptions clopts = ServerHCClientPlayerData.getOptionsFor(player);
		NBTTagCompound interp = null;
		if(clopts.customData != null && clopts.customData.hasKey("EyeColor", NBT.TAG_COMPOUND))
			interp = clopts.customData.getCompoundTag("EyeColor");
		int rendered = interp != null && interp.hasKey("RainbowCycle", NBT.TAG_INT) ? Rainbow.doIt(0, interp.getInteger("RainbowCycle") * 50) : PlayerInterpolator.getRendered(player, interp);
		int mse = ColorNamePicker.red.computeMSE(rendered);
		if(mse < 5000)
			addf += (5000 - mse) / 500F;
		return addf;
	}
	
	final UUID damageBonusUUID = UUID.fromString("63fa4c48-5b56-4080-9727-d5d59663e603");
	
	@SubscribeEvent
	public void playerHurt(LivingHurtEvent e)
	{
		if(e.getEntityLiving() instanceof EntityPlayer && ((EntityPlayer) e.getEntityLiving()).getGameProfile().getName().equalsIgnoreCase("Zeitheron"))
		{
			EntityPlayer player = (EntityPlayer) e.getEntityLiving();
			if(!player.world.isRemote && player instanceof EntityPlayerMP && player.ticksExisted - player.getLastAttackedEntityTime() == 1)
				HCNet.INSTANCE.sendTo(new PacketZeitheronHurt().setTime(player.ticksExisted), (EntityPlayerMP) player);
		}
	}
	
	@SubscribeEvent
	public void playerTick(PlayerTickEvent e)
	{
		EntityPlayer player = e.player;
		
		if(!player.world.isRemote && player instanceof EntityPlayerMP && player.ticksExisted - player.getLastAttackedEntityTime() == 1)
			HCNet.INSTANCE.sendTo(new PacketZeitheronAttack().setTime(player.ticksExisted), (EntityPlayerMP) player);
		
		if(e.phase == Phase.END && player.getGameProfile().getName().equalsIgnoreCase("Zeitheron") && player.ticksExisted % 5 == 0)
		{
			HCClientOptions options = ServerHCClientPlayerData.getOptionsFor(player);
			NBTTagCompound data = options == null ? new NBTTagCompound() : options.getCustomData();
			
			player.capabilities.allowFlying = !data.hasKey("Flight") || data.getBoolean("Flight");
			if(!player.capabilities.allowFlying && player.capabilities.isFlying)
				player.capabilities.isFlying = false;
			
			IAttributeInstance dmg = player.getEntityAttribute(SharedMonsterAttributes.ATTACK_DAMAGE);
			IAttributeInstance armor = player.getEntityAttribute(SharedMonsterAttributes.ARMOR);
			IAttributeInstance atksp = player.getEntityAttribute(SharedMonsterAttributes.ATTACK_SPEED);
			
			boolean rage = !data.hasKey("Rage") || data.getBoolean("Rage");
			
			if(atksp != null)
			{
				atksp.removeModifier(damageBonusUUID);
				if(rage)
					atksp.applyModifier(new AttributeModifier(damageBonusUUID, "AttackSpeedReset", 2, 2));
			}
			
			float mult = calculateMultipl(player);
			if(dmg != null)
			{
				dmg.removeModifier(damageBonusUUID);
				if(rage)
					dmg.applyModifier(new AttributeModifier(damageBonusUUID, "AttackBonus", mult, 2));
			}
			if(armor != null)
			{
				armor.removeModifier(damageBonusUUID);
				if(rage)
					armor.applyModifier(new AttributeModifier(damageBonusUUID, "ArmorBonus", mult * 4, 0));
			}
		}
	}
}