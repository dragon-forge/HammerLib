package org.zeith.hammerlib.core.recipes;

import net.minecraft.core.HolderLookup;
import net.minecraft.world.item.crafting.RecipeManager;
import net.neoforged.neoforge.common.conditions.ICondition;
import org.jetbrains.annotations.Nullable;

public record ServerContext(RecipeManager manager, ICondition.IContext context, @Nullable HolderLookup.Provider registryAccess)
{
	public static ServerContext gather(RecipeManager manager, ICondition.IContext context)
	{
		return new ServerContext(manager, context, manager.registries);
	}
}