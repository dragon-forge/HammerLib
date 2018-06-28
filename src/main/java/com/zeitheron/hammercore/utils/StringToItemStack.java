package com.zeitheron.hammercore.utils;

import net.minecraft.item.ItemStack;
import net.minecraftforge.fml.common.registry.GameRegistry;

/**
 * Utility class focused on parsing {@link String} to {@link ItemStack}
 */
public class StringToItemStack
{
	/**
	 * Parses string to item stack if possible, returns empty stack otherwise
	 * <br>
	 * Example: <br>
	 * {@link StringToItemStack#toItemStack}("minecraft:diamond_pickaxe:100@1",
	 * "{\"Example\":1}") //Returns stack with 1 diamond pickaxe and damage 100
	 * and NBT tag that contains INT "Example" = 1 <br>
	 * {@link StringToItemStack#toItemStack}("diamond:0@16", null) //Returns
	 * stack with 16 diamonds and damage 0 and NBT is null; This example shows
	 * that items from vanilla don't have to have "minecraft:" prefix
	 */
	public static ItemStack toItemStack(String origin, String nbt)
	{
		try
		{
			String rawId = origin.substring(0, origin.lastIndexOf(":"));
			int otpMeta = Integer.parseInt(origin.substring(origin.lastIndexOf(":") + 1, origin.lastIndexOf("@")));
			int otpStackSize = Integer.parseInt(origin.substring(origin.lastIndexOf("@") + 1, origin.length()));
			return GameRegistry.makeItemStack(rawId, otpMeta, otpStackSize, nbt);
		} catch(Throwable err)
		{
		}
		return InterItemStack.NULL_STACK;
	}
}