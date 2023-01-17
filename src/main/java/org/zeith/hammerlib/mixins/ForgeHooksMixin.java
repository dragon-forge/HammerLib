package org.zeith.hammerlib.mixins;

import net.minecraft.world.level.storage.loot.LootTable;
import net.minecraftforge.common.ForgeHooks;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyVariable;
import org.zeith.hammerlib.core.adapter.LootTableAdapter;

@Mixin(value = ForgeHooks.class, remap = false)
public class ForgeHooksMixin
{
	@ModifyVariable(
			method = "loadLootTable",
			at = @At(value = "INVOKE", target = "Lnet/minecraft/world/level/storage/loot/LootTable;freeze()V"),
			index = 6
	)
	private static LootTable loadLootTable_BotanicAdditions(LootTable table)
	{
		return LootTableAdapter.alter(table);
	}
}