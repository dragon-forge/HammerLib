package org.zeith.api.wrench;

import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.context.UseOnContext;
import org.zeith.hammerlib.core.init.TagsHL;
import org.zeith.hammerlib.util.java.Cast;

/**
 * Interface representing items that can be used as wrenches.
 *
 * @see IWrenchable
 */
public interface IWrenchItem
{
	/**
	 * Determines if this wrench item can be used to perform a wrench action.
	 *
	 * @param stack The item stack to check.
	 * @return Whether this item can be used as a wrench.
	 */
	default boolean canWrench(ItemStack stack)
	{
		return true;
	}
	
	/**
	 * Use this to play sounds if the wrench operation is performed on a given block.
	 *
	 * @param context The context of the wrench action.
	 */
	void playWrenchSound(UseOnContext context);
	
	/**
	 * Determines if the given item is a wrench.
	 *
	 * @param stack Item to check.
	 * @return If the item is a wrench.
	 */
	static boolean isWrench(ItemStack stack)
	{
		return !stack.isEmpty() && (stack.is(TagsHL.Items.TOOLS_WRENCH) || (stack.getItem() instanceof IWrenchItem it && it.canWrench(stack)));
	}
	
	/**
	 * Performs the wrench action on the given context.
	 *
	 * @param context Context of the wrench action.
	 * @return If the wrench operation was successful and had any result.
	 */
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