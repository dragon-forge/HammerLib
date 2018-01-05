package com.pengu.hammercore.var;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.pengu.hammercore.net.HCNetwork;

import net.minecraft.entity.player.EntityPlayerMP;

/**
 * Used to sync data between client and server. For example: tooltip.
 */
public class VariableManager
{
	private VariableManager()
	{
		throw new AssertionError("No " + getClass().getName() + " instances for you!");
	}
	
	static final List<iVariable> VARIABLES = new ArrayList<>();
	
	public static <T> iVariable<T> getVariable(String id)
	{
		for(int i = 0; i < VARIABLES.size(); ++i)
			if(VARIABLES.get(i).getId().equals(id))
				return VARIABLES.get(i);
		return null;
	}
	
	public static void registerVariable(iVariable var)
	{
		if(VARIABLES.contains(var) || getVariable(var.getId()) != null)
			return;
		VARIABLES.add(var);
	}
	
	static void updateManager()
	{
		Map<String, iVariable> dirty = null;
		
		for(int i = 0; i < VARIABLES.size(); ++i)
		{
			iVariable var = VARIABLES.get(i);
			if(var.hasChanged())
			{
				if(dirty == null)
					dirty = new HashMap<>();
				dirty.put(var.getId(), var);
				var.setNotChanged();
			}
		}
		
		if(dirty != null)
			HCNetwork.manager.sendToAll(new PacketUpdateDirtyVariables(dirty));
	}
	
	public static void sendVarsTo(EntityPlayerMP mp)
	{
		Map<String, iVariable> dirty = null;
		
		for(int i = 0; i < VARIABLES.size(); ++i)
		{
			iVariable var = VARIABLES.get(i);
			if(dirty == null)
				dirty = new HashMap<>();
			dirty.put(var.getId(), var);
			var.setNotChanged();
		}
		
		if(dirty != null)
			HCNetwork.manager.sendTo(new PacketUpdateDirtyVariables(dirty), mp);
	}
}