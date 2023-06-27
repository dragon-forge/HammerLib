package org.zeith.hammerlib.mixins;

import net.minecraft.world.level.storage.loot.*;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

import java.util.List;

@Mixin(LootTable.class)
public interface LootTableAccessor
{
	// this is always ArrayList, since it's a final value.
	@Accessor("pools")
	List<LootPool> getPools();
}