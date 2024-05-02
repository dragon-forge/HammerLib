package org.zeith.hammerlib.abstractions.actions.impl;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.level.Level;
import org.zeith.hammerlib.HammerLib;
import org.zeith.hammerlib.abstractions.actions.*;
import org.zeith.hammerlib.annotations.ExposedToLevelAction;
import org.zeith.hammerlib.core.init.LevelActionTypesHL;
import org.zeith.hammerlib.util.java.reflection.SerializableMethodHandle;

/**
 * This action enables calling arbitrary method handle with nbt-serializable instance and arguments. The target method must contain @{@link ExposedToLevelAction} annotation for security reasons.
 * <p>
 * An example of such action:
 * <code>new MethodHandleLevelAction( {@link org.zeith.hammerlib.core.test.MethodHandleTest#makePerformTest}(GlobalPos.of(serverLevel.dimension(), blockPos)) ) .delay(100) .enqueue(serverLevel);</code>
 * will create and invoke the test action after 100 ticks, printing to console after 100 ticks are up.
 */
public class MethodHandleLevelAction
		extends RunnableLevelAction
{
	protected final SerializableMethodHandle handle;
	
	public MethodHandleLevelAction(SerializableMethodHandle handle)
	{
		this(LevelActionTypesHL.METHOD_HANDLE_TYPE, handle);
		if(!handle.isResolved())
			HammerLib.LOG.warn("Warning: created unresolved method handle level action! {}", handle);
	}
	
	public MethodHandleLevelAction(ILevelActionType type, SerializableMethodHandle handle)
	{
		super(type);
		this.handle = handle != null && handle.isResolved() ? handle : null;
	}
	
	public MethodHandleLevelAction(ILevelActionType type, Level level, CompoundTag tag)
	{
		this(type, new SerializableMethodHandle(tag.getCompound("Target")));
	}
	
	@Override
	public void run(ServerLevel level)
	{
		if(handle == null || !handle.isResolved()) return;
		
		if(!handle.getMethod().isAnnotationPresent(ExposedToLevelAction.class))
		{
			HammerLib.LOG.warn("Completely ignored non-exposed call method handle: {}", handle.serializeNBT());
			return;
		}
		
		try
		{
			handle.call();
		} catch(SerializableMethodHandle.MethodHandleInvocationException e)
		{
			HammerLib.LOG.error("Failed to invoke {}", handle);
		}
	}
	
	@Override
	public CompoundTag write(Level level)
	{
		CompoundTag nbt = new CompoundTag();
		if(handle != null && handle.isResolved())
			nbt.put("Target", handle.serializeNBT());
		return nbt;
	}
	
	public static class MethodHandleActionType
			implements ILevelActionType
	{
		@Override
		public LevelAction read(Level level, CompoundTag tag)
		{
			return new MethodHandleLevelAction(this, level, tag);
		}
	}
}