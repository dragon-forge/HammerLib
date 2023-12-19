package org.zeith.hammerlib.mixins;

import net.minecraft.world.level.storage.loot.LootTable;
import net.neoforged.neoforge.common.CommonHooks;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.*;
import org.zeith.hammerlib.core.adapter.LootTableAdapter;

@Mixin(value = CommonHooks.class, remap = false)
public class ForgeHooksMixin
{
	@ModifyVariable(
			method = "loadLootTable",
			at = @At(value = "INVOKE", target = "Lnet/minecraft/world/level/storage/loot/LootTable;freeze()V"),
			index = 5
	)
	private static LootTable loadLootTable_HammerLib(LootTable table)
	{
		return LootTableAdapter.alter(table);
	}
}