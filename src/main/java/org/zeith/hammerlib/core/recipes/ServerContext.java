package org.zeith.hammerlib.core.recipes;

import net.minecraft.core.RegistryAccess;
import net.minecraft.world.item.crafting.RecipeManager;
import net.minecraftforge.common.crafting.conditions.ICondition;
import org.jetbrains.annotations.Nullable;
import org.zeith.hammerlib.mixins.recipe.ConditionContextAccessor;
import org.zeith.hammerlib.mixins.recipe.TagManagerAccessor;

public record ServerContext(RecipeManager manager, ICondition.IContext context, @Nullable RegistryAccess registryAccess)
{
	public static ServerContext gather(RecipeManager manager, ICondition.IContext context)
	{
		RegistryAccess ra = null;
		if(context instanceof ConditionContextAccessor cca && cca.getTagManager() instanceof TagManagerAccessor tma)
			ra = tma.getRegistryAccess();
		return new ServerContext(manager, context, ra);
	}
}