package com.pengu.hammercore.common.items.debug;

import com.pengu.hammercore.HammerCore;
import com.pengu.hammercore.raytracer.RayTracer;
import com.pengu.hammercore.utils.ColorHelper;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ActionResult;
import net.minecraft.util.DamageSource;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.util.math.RayTraceResult.Type;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;

public final class ItemZapper extends Item
{
	public ItemZapper()
	{
		setUnlocalizedName("zapper");
		setMaxStackSize(1);
	}
	
	@Override
	public ActionResult<ItemStack> onItemRightClick(World worldIn, EntityPlayer playerIn, EnumHand hand)
	{
		RayTraceResult result = RayTracer.retrace(playerIn, 128, true);
		
		if(result != null && result.entityHit != null && !result.entityHit.isDead)
		{
			if(!worldIn.isRemote)
			{
				AxisAlignedBB aabb = result.entityHit.getEntityBoundingBox();
				
				HammerCore.particleProxy.spawnZap(worldIn, new Vec3d(playerIn.posX, playerIn.posY + 1, playerIn.posZ), new Vec3d(result.entityHit.posX, result.entityHit.posY + (aabb.maxY - aabb.minY) / 2, result.entityHit.posZ), ColorHelper.packRGB(playerIn.getRNG().nextFloat(), playerIn.getRNG().nextFloat(), playerIn.getRNG().nextFloat()));
				result.entityHit.attackEntityFrom(DamageSource.causePlayerDamage(playerIn), Float.POSITIVE_INFINITY);
			}
			
			playerIn.swingArm(hand);
		} else if(result != null && result.typeOfHit == Type.BLOCK)
		{
			if(!worldIn.isRemote)
			{
				if(playerIn.capabilities.isCreativeMode || worldIn.getBlockState(result.getBlockPos()).getBlockHardness(worldIn, result.getBlockPos()) != -1F)
					worldIn.destroyBlock(result.getBlockPos(), true);
				
				HammerCore.particleProxy.spawnSlowZap(worldIn, new Vec3d(playerIn.posX, playerIn.posY + 1, playerIn.posZ), new Vec3d(result.getBlockPos().getX() + .5, result.getBlockPos().getY() + .5, result.getBlockPos().getZ() + .5), ColorHelper.packRGB(playerIn.getRNG().nextFloat(), playerIn.getRNG().nextFloat(), playerIn.getRNG().nextFloat()), 10, .15F);
			}
			
			playerIn.swingArm(hand);
		}
		
		return super.onItemRightClick(worldIn, playerIn, hand);
	}
}