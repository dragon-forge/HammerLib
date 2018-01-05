package com.pengu.hammercore.common.items;

import com.pengu.hammercore.common.blocks.BlockIWrenchGhost;
import com.pengu.hammercore.raytracer.RayTracer;
import com.pengu.hammercore.utils.WorldLocation;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ActionResult;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.util.math.RayTraceResult.Type;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;

public class ItemIWrench extends ItemWrench
{
	public ItemIWrench()
	{
		setUnlocalizedName("iwrench");
	}
	
	@Override
	public void onWrenchUsed(EntityPlayer player, BlockPos pos, EnumHand hand)
	{
	}
	
	@Override
	public ActionResult<ItemStack> onItemRightClick(World worldIn, EntityPlayer playerIn, EnumHand handIn)
	{
		ghost: if(!worldIn.isRemote && playerIn.isSneaking())
		{
			RayTraceResult res = RayTracer.retrace(playerIn);
			if(res != null && res.typeOfHit == Type.BLOCK)
				break ghost;
			Vec3d headVec = RayTracer.getCorrectedHeadVec(playerIn);
			Vec3d lookVec = playerIn.getLook(1.0F);
			double reach = 2;
			Vec3d pos = headVec.addVector(lookVec.x * reach, lookVec.y * reach, lookVec.z * reach);
			WorldLocation l = new WorldLocation(worldIn, new BlockPos(pos.x, pos.y, pos.z));
			if(l.isAir())
				BlockIWrenchGhost.to(l, 1200);
		}
		return super.onItemRightClick(worldIn, playerIn, handIn);
	}
}