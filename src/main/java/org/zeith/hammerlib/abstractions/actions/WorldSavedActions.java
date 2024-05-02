package org.zeith.hammerlib.abstractions.actions;

import net.minecraft.nbt.*;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.level.saveddata.SavedData;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import org.jetbrains.annotations.Contract;
import org.zeith.hammerlib.HammerLib;

import java.util.ArrayList;
import java.util.List;

@Mod.EventBusSubscriber
public class WorldSavedActions
		extends SavedData
{
	private final ServerLevel level;
	
	private final List<RunnableLevelAction> actions = new ArrayList<>();
	private final List<ContinuousLevelAction> continuousActions = new ArrayList<>();
	
	public WorldSavedActions(ServerLevel level)
	{
		this.level = level;
	}
	
	public WorldSavedActions(ServerLevel level, CompoundTag nbt)
	{
		this.level = level;
		
		ListTag actionsTag = nbt.getList("Actions", Tag.TAG_COMPOUND);
		for(int i = 0; i < actionsTag.size(); i++)
		{
			var tag = actionsTag.getCompound(i);
			var action = LevelAction.read(level, tag).orElse(null);
			if(action == null)
			{
				HammerLib.LOG.warn("Unable to find level action type \"{}\" (with data {})", tag.getString("Type"), tag.getCompound("Data"));
				continue;
			}
			
			if(!(action instanceof RunnableLevelAction r))
			{
				HammerLib.LOG.warn("Level action type \"{}\" is not runnable! (with data {})", tag.getString("Type"), tag.getCompound("Data"));
				continue;
			}
			
			if(r instanceof ContinuousLevelAction c) continuousActions.add(c);
			else actions.add(r);
		}
	}
	
	public void tick()
	{
		boolean hasNone = actions.isEmpty() && continuousActions.isEmpty();
		
		while(!actions.isEmpty())
		{
			var a = actions.remove(0);
			if(a instanceof ContinuousLevelAction c)
			{
				continuousActions.add(c);
				continue;
			}
			a.run(level);
		}
		
		for(int i = 0; i < continuousActions.size(); i++)
		{
			var a = continuousActions.get(i);
			a.run(level);
			if(a.isDone())
			{
				continuousActions.remove(i);
				--i;
			}
		}
		
		if(hasNone && continuousActions.isEmpty())
			return;
		
		setDirty();
	}
	
	@Override
	public CompoundTag save(CompoundTag nbt)
	{
		ListTag actionsTag = new ListTag();
		for(RunnableLevelAction action : actions) actionsTag.add(LevelAction.write(level, action));
		for(RunnableLevelAction action : continuousActions) actionsTag.add(LevelAction.write(level, action));
		nbt.put("Actions", actionsTag);
		return nbt;
	}
	
	public ServerLevel getLevel()
	{
		return level;
	}
	
	/**
	 * Enqueues action to be dispatched at next server tick.
	 * The action can be {@link ContinuousLevelAction} to be called until it's done.
	 */
	public static boolean enqueue(ServerLevel level, RunnableLevelAction action)
	{
		var actions = get(level);
		if(action == null || actions == null) return false;
		if(action instanceof ContinuousLevelAction c)
			actions.continuousActions.add(c);
		else actions.actions.add(action);
		actions.setDirty();
		return true;
	}
	
	@Contract("_ -> _")
	public static WorldSavedActions get(ServerLevel level)
	{
		return level != null ? level.getDataStorage().computeIfAbsent(
				(nbt) -> new WorldSavedActions(level, nbt),
				() -> new WorldSavedActions(level),
				"hammerlib_level_actions"
		) : null;
	}
	
	@SubscribeEvent
	public static void levelTick(TickEvent.LevelTickEvent e)
	{
		if(e.phase != TickEvent.Phase.START || !(e.level instanceof ServerLevel sl)) return;
		get(sl).tick();
	}
}