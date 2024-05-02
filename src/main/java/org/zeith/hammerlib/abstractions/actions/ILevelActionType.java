package org.zeith.hammerlib.abstractions.actions;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.Level;
import org.zeith.hammerlib.core.RegistriesHL;

/**
 * Represents a type of {@link LevelAction}, used to create new instances from NBT.
 *
 * @see org.zeith.hammerlib.abstractions.actions.impl.DelayedLevelAction.DelayedType
 * @see org.zeith.hammerlib.core.init.LevelActionTypesHL
 */
public interface ILevelActionType
{
	LevelAction read(Level level, CompoundTag tag);
	
	default ResourceLocation getRegistryKey()
	{
		return RegistriesHL.levelActions().getKey(this);
	}
}