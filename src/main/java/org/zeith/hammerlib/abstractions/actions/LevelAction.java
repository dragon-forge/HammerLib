package org.zeith.hammerlib.abstractions.actions;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.Level;
import org.zeith.hammerlib.core.RegistriesHL;

import javax.annotation.Nullable;
import java.util.Optional;

/**
 * An abstract interface of an action that can be saved and/or passed over network, while holding a unit of data and/or work.
 * <p>
 * The built-in implementation of this would be {@link RunnableLevelAction} and {@link ContinuousLevelAction}.
 * <p>
 * An example of such action:
 * <code>new MethodHandleLevelAction( MethodHandleTest.makePerformTest(GlobalPos.of(serverLevel.dimension(), blockPos)) ) .delay(100) .enqueue(serverLevel);</code>
 */
public abstract class LevelAction
{
	protected final ILevelActionType type;
	
	public LevelAction(ILevelActionType type)
	{
		this.type = type;
	}
	
	public abstract CompoundTag write(Level level);
	
	public final ILevelActionType getType()
	{
		return type;
	}
	
	public static CompoundTag write(Level level, LevelAction action)
	{
		var tag = new CompoundTag();
		tag.put("Data", action.write(level));
		tag.putString("Type", action.getType().getRegistryKey().toString());
		return tag;
	}
	
	public static Optional<LevelAction> read(Level level, @Nullable CompoundTag tag)
	{
		if(tag == null) return Optional.empty();
		ILevelActionType type = RegistriesHL.levelActions().getValue(new ResourceLocation(tag.getString("Type")));
		if(type == null) return Optional.empty();
		return Optional.ofNullable(type.read(level, tag.getCompound("Data")));
	}
}