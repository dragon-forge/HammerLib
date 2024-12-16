package org.zeith.hammerlib.core.recipes.replacers;

import com.mojang.serialization.Codec;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.CraftingInput;
import net.minecraft.world.item.crafting.ShapedRecipe;
import org.zeith.hammerlib.core.RegistriesHL;

import java.util.*;

/**
 * An interface for replacing items in a crafting container.
 * Implementing lambdas (or classes) should provide the logic to determine the remaining item based on what is currently stored in the container's slot.
 * Additionally, all instances of {@link IRemainingItemReplacer} must be registered into the IForgeRegistry.
 * A good way of doing so would be @{@link org.zeith.hammerlib.annotations.SimplyRegister} with @{@link org.zeith.hammerlib.annotations.RegistryName}
 */
@FunctionalInterface
public interface IRemainingItemReplacer
{
	Codec<IRemainingItemReplacer> CODEC = RegistriesHL.REMAINING_REPLACER.byNameCodec();
	Codec<List<IRemainingItemReplacer>> LIST_CODEC = CODEC.listOf();
	StreamCodec<RegistryFriendlyByteBuf, List<IRemainingItemReplacer>> STREAM_CODEC = StreamCodec.of(IRemainingItemReplacer::toNetwork, IRemainingItemReplacer::fromNetwork);
	
	/**
	 * Returns the remaining item based on what is currently stored in the container's slot.
	 * If no modifications happen, the method should return the prevItem.
	 * The prevItem parameter is used when multiple instances of {@link IRemainingItemReplacer} are used sequentially.
	 * Depending on the order of replacers, they may perform various replacements on each other, or avoid replacing if the prevItem is not empty.
	 * A good reference point for the behavior of this method is the {@link WaterBottleReplacer},
	 * which takes into account previous replacers before replacing a water bottle with an empty glass bottle.
	 *
	 * @param container
	 * 		the crafting input
	 * @param slot
	 * 		the slot index in the container
	 * @param prevItem
	 * 		the previous item stack
	 *
	 * @return the remaining item stack, or prevItem if no modification should take place.
	 */
	ItemStack replace(CraftingInput container, int slot, ItemStack prevItem);
	
	static void toNetwork(FriendlyByteBuf buf, List<IRemainingItemReplacer> lst)
	{
		var replacers = lst.stream().map(RegistriesHL.REMAINING_REPLACER::getKey).filter(Objects::nonNull).toList();
		buf.writeShort(replacers.size());
		for(var r : replacers) buf.writeResourceLocation(r);
	}
	
	static List<IRemainingItemReplacer> fromNetwork(FriendlyByteBuf buf)
	{
		var g = RegistriesHL.REMAINING_REPLACER;
		List<IRemainingItemReplacer> lst = new ArrayList<>();
		short size = buf.readShort();
		for(int i = 0; i < size; i++)
		{
			var r = g.getValue(buf.readResourceLocation());
			if(r != null) lst.add(r);
		}
		return lst;
	}
}