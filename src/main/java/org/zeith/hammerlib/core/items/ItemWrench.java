package org.zeith.hammerlib.core.items;

import net.minecraft.block.*;
import net.minecraft.item.*;
import net.minecraft.util.*;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.vector.Vector3d;
import net.minecraft.world.World;
import net.minecraft.world.server.ServerWorld;
import org.zeith.api.wrench.*;
import org.zeith.hammerlib.annotations.*;
import org.zeith.hammerlib.proxy.HLConstants;
import org.zeith.hammerlib.util.java.Cast;

@SimplyRegister
public class ItemWrench
		extends Item
		implements IWrenchItem
{
	@RegistryName("wrench")
	public static final ItemWrench WRENCH = new ItemWrench(new Properties().tab(HLConstants.HL_TAB).stacksTo(1));

	public ItemWrench(Properties props)
	{
		super(props.addToolType(WRENCH_TOOL_TYPE, 1));
	}

	@Override
	public ActionResultType onItemUseFirst(ItemStack stack, ItemUseContext context)
	{
		World world = context.getLevel();
		if(!world.isClientSide && canWrench(stack))
		{
			BlockPos pos = context.getClickedPos();
			BlockState state = world.getBlockState(pos);
			Block blk = state.getBlock();
			if(Cast.firstInstanceof(IWrenchable.class, world.getBlockEntity(pos), blk)
					.map(w -> w.onWrenchUsed(context))
					.orElse(false))
			{
				onWrenchUsed(context);
				return ActionResultType.SUCCESS;
			}
		}

		return ActionResultType.PASS;
	}

	@Override
	public boolean canWrench(ItemStack stack)
	{
		return true;
	}

	@Override
	public void onWrenchUsed(ItemUseContext context)
	{
		World world = context.getLevel();
		if(!world.isClientSide && world instanceof ServerWorld)
		{
			Vector3d e = context.getClickLocation();
			world.playSound(null, e.x, e.y, e.z, SoundEvents.TRIPWIRE_CLICK_ON, SoundCategory.PLAYERS, 1.0F, 1.5F);
		}
	}
}