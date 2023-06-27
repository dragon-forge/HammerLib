package org.zeith.hammerlib.mixins;

import net.minecraft.world.item.*;
import net.minecraft.world.item.alchemy.PotionBrewing;
import net.minecraft.world.item.crafting.Ingredient;
import org.spongepowered.asm.mixin.*;
import org.spongepowered.asm.mixin.gen.*;

import java.util.List;

@Mixin(PotionBrewing.class)
public interface PotionBrewingAccessor
{
	@Accessor("CONTAINER_MIXES")
	static List<PotionBrewing.Mix<Item>> getContainerMixes() {throw new UnsupportedOperationException();}
	
	@Accessor("ALLOWED_CONTAINERS")
	static List<Ingredient> getAllowedContainers() {throw new UnsupportedOperationException();}
}
