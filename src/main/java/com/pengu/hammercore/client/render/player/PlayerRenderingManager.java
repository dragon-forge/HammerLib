package com.pengu.hammercore.client.render.player;

import java.util.HashMap;
import java.util.Map;

import com.pengu.hammercore.client.render.player.players.EndieDargon;
import com.pengu.hammercore.common.utils.classes.ClassWrapper;

public class PlayerRenderingManager
{
	private static final iPlayerModel EMPTY = player ->
	{
	};
	
	private static final Map<String, iPlayerModel> RENDERS = new HashMap<>();
	
	static
	{
		bind(new EndieDargon(), "EndieDargon");
	}
	
	public static void bind(iPlayerModel type, String username)
	{
		RENDERS.put(username, RENDERS.getOrDefault(username, type).and(type));
	}
	
	public static iPlayerModel get(String username)
	{
//		System.out.println(ClassWrapper.getCallerClassName());
		return RENDERS.getOrDefault(username, EMPTY);
	}
}