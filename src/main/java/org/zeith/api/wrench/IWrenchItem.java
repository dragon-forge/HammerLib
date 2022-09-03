package org.zeith.api.wrench;

import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.context.UseOnContext;
import org.zeith.hammerlib.core.init.TagsHL;
import org.zeith.hammerlib.util.java.Cast;

public interface IWrenchItem
{
	default boolean canWrench(ItemStack stack)
	{
		return true;
	}
	
	/**
	 * Use this to play sounds if the wrench operation is performed on a given block.
	 */
	void playWrenchSound(UseOnContext context);
	
	@Deprecated(forRemoval = true, since = "")
	default void onWrenchUsed(UseOnContext context)
	{
	}
	
	static boolean isWrench(ItemStack stack)
	{
		return !stack.isEmpty() && (stack.is(TagsHL.Items.TOOLS_WRENCH) || (stack.getItem() instanceof IWrenchItem it && it.canWrench(stack)));
	}
	
	static boolean performWrenchAction(UseOnContext context)
	{
		var item = context.getItemInHand();
		if(!isWrench(item)) return false;
		
		var world = context.getLevel();
		var pos = context.getClickedPos();
		var state = world.getBlockState(pos);
		var blk = state.getBlock();
		
		if(Cast.firstInstanceof(IWrenchable.class, world.getBlockEntity(pos), blk)
				.map(w -> w.onWrenchUsed(context))
				.orElse(false))
		{
			if(item.getItem() instanceof IWrenchItem it)
				it.playWrenchSound(context);
			return true;
		}
		
		return false;
	}
}