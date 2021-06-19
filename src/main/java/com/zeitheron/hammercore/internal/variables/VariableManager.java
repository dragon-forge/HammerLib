package com.zeitheron.hammercore.internal.variables;

import com.zeitheron.hammercore.event.PlayerLoadReadyEvent;
import com.zeitheron.hammercore.net.internal.PacketUpdateDirtyVariables;
import com.zeitheron.hammercore.net.transport.NetTransport;
import com.zeitheron.hammercore.utils.base.Cast;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Used to sync data between client and server. For example: tooltip.
 */
@Mod.EventBusSubscriber
public class VariableManager
{
	private VariableManager()
	{
		throw new AssertionError("No " + getClass().getName() + " instances for you!");
	}

	static final List<IVariable<?>> VARIABLES = new ArrayList<>();
	static final Map<ResourceLocation, IVariable<?>> DIRTY_VARS = new HashMap<>();

	public static <T> IVariable<T> getVariable(ResourceLocation id)
	{
		for(int i = 0; i < VARIABLES.size(); ++i)
			if(VARIABLES.get(i).getId().equals(id))
				return Cast.cast(VARIABLES.get(i));
		return null;
	}

	public static void registerVariable(IVariable<?> var)
	{
		if(VARIABLES.contains(var) || getVariable(var.getId()) != null)
			return;
		VARIABLES.add(var);
	}

	@SubscribeEvent
	public static void serverTick(TickEvent.ServerTickEvent evt)
	{
		updateManager();
	}

	static void updateManager()
	{
		DIRTY_VARS.clear();

		for(int i = 0; i < VARIABLES.size(); ++i)
		{
			IVariable<?> var = VARIABLES.get(i);
			if(var.hasChanged())
			{
				DIRTY_VARS.put(var.getId(), var);
				var.setNotChanged();
			}
		}

		if(!DIRTY_VARS.isEmpty())
			NetTransport.wrap(new PacketUpdateDirtyVariables(DIRTY_VARS)).sendToAll();
	}

	@SubscribeEvent
	public static void playerJoin(PlayerLoadReadyEvent evt)
	{
		sendVarsTo(evt.playerMP);
	}

	public static void sendVarsTo(EntityPlayerMP mp)
	{
		DIRTY_VARS.clear();

		for(int i = 0; i < VARIABLES.size(); ++i)
		{
			IVariable<?> var = VARIABLES.get(i);
			DIRTY_VARS.put(var.getId(), var);
			var.setNotChanged();
		}

		if(!DIRTY_VARS.isEmpty())
			NetTransport.wrap(new PacketUpdateDirtyVariables(DIRTY_VARS)).sendTo(mp);
	}
}