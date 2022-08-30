package org.zeith.hammerlib.core.items;

import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.context.UseOnContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.Vec3;
import org.zeith.api.wrench.IWrenchItem;
import org.zeith.api.wrench.IWrenchable;
import org.zeith.hammerlib.annotations.RegistryName;
import org.zeith.hammerlib.annotations.SimplyRegister;
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
		super(props);
	}
	
	@Override
	public InteractionResult onItemUseFirst(ItemStack stack, UseOnContext context)
	{
		Level world = context.getLevel();
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
				return InteractionResult.SUCCESS;
			}
		}

		return InteractionResult.PASS;
	}

	@Override
	public boolean canWrench(ItemStack stack)
	{
		return true;
	}

	@Override
	public void onWrenchUsed(UseOnContext context)
	{
		Level world = context.getLevel();
		if(!world.isClientSide && world instanceof ServerLevel)
		{
			Vec3 e = context.getClickLocation();
			world.playSound(null, e.x, e.y, e.z, SoundEvents.TRIPWIRE_CLICK_ON, SoundSource.PLAYERS, 1.0F, 1.5F);
		}
	}
}