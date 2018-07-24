package com.zeitheron.hammercore.client.render.player;

import java.util.HashMap;
import java.util.Map;

import com.zeitheron.hammercore.specials.Zeitheron.ZeitheronRenderer;

public class PlayerRenderingManager
{
	private static final IPlayerModel EMPTY = player ->
	{
	};
	
	private static final Map<String, IPlayerModel> RENDERS = new HashMap<>();
	
	static
	{
		bind(new ZeitheronRenderer(), "Zeitheron");
	}
	
	public static void bind(IPlayerModel type, String username)
	{
		RENDERS.put(username, RENDERS.getOrDefault(username, type).and(type));
	}
	
	public static IPlayerModel get(String username)
	{
		return RENDERS.getOrDefault(username, EMPTY);
	}
}