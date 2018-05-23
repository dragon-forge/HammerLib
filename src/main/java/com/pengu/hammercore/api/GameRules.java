package com.pengu.hammercore.api;

import java.util.HashMap;
import java.util.Map;

import javax.annotation.Nonnull;

import com.pengu.hammercore.HammerCore;

import net.minecraft.server.MinecraftServer;
import net.minecraft.world.World;
import net.minecraft.world.WorldServer;

public class GameRules
{
	public static final Map<String, GameRuleEntry> entries = new HashMap<>();
	
	public static void registerGameRule(GameRuleEntry entry)
	{
		entries.put(entry.name, entry);
	}
	
	@Nonnull
	public static GameRuleEntry getEntry(String name)
	{
		return entries.getOrDefault(name, GameRuleEntry.NIL);
	}
	
	public static void load(MinecraftServer server)
	{
		WorldServer ws = server.getWorld(0);
		net.minecraft.world.GameRules gr = ws != null ? ws.getGameRules() : null;
		if(gr == null)
		{
			HammerCore.LOG.error("Unable to get GameRule map!");
			return;
		}
		
		HammerCore.LOG.info("Injecting GameRules...");
		
		// Loads rules if they are not present already
		entries.values().forEach(e ->
		{
			String val = e.defVal;
			if(gr.hasRule(e.name))
				val = gr.getString(e.name);
			gr.addGameRule(e.name, val, e.type.toNM());
		});
	}
	
	public static void cleanup()
	{
		// HammerCore.LOG.info("Saving custom GameRule data...");
		//
		//
	}
	
	public static class GameRuleEntry
	{
		private static final GameRuleEntry NIL = new GameRuleEntry("unknown", "", "gamerules.hc_unknown", ValueType.STRING_VALUE);
		
		public final String name, defVal, i18nDesc;
		public final ValueType type;
		
		public GameRuleEntry(String name, String defValue, String i18nDesc, ValueType type)
		{
			this.i18nDesc = i18nDesc;
			this.name = name;
			this.defVal = defValue;
			this.type = type;
		}
		
		public String getValue(World world)
		{
			if(this == NIL)
				return "";
			return world.getGameRules().getString(name);
		}
		
		public int getInt(World world)
		{
			if(this == NIL)
				return 0;
			if(type != ValueType.INT_VALUE)
				return 0;
			return world.getGameRules().getInt(name);
		}
		
		public boolean getBool(World world)
		{
			if(this == NIL)
				return false;
			if(type != ValueType.BOOLEAN_VALUE)
				return false;
			return world.getGameRules().getBoolean(name);
		}
		
		public double getDouble(World world)
		{
			if(this == NIL)
				return 0;
			if(type != ValueType.DECIMAL_VALUE)
				return 0;
			try
			{
				return Double.parseDouble(getValue(world));
			} catch(Throwable err)
			{
				// Not a double!
			}
			return 0;
		}
		
		public float getFloat(World world)
		{
			if(this == NIL)
				return 0;
			if(type != ValueType.DECIMAL_VALUE && !name.equals("hc_falldamagemult"))
				return 0;
			try
			{
				return Float.parseFloat(getValue(world));
			} catch(Throwable err)
			{
				// Not a double!
			}
			return 0;
		}
	}
	
	public static enum ValueType
	{
		STRING_VALUE(0), //
		BOOLEAN_VALUE(1), //
		INT_VALUE(2), //
		DECIMAL_VALUE(0);
		
		final int ord;
		
		private ValueType(int ord)
		{
			this.ord = ord;
		}
		
		public net.minecraft.world.GameRules.ValueType toNM()
		{
			return net.minecraft.world.GameRules.ValueType.values()[ord];
		}
	}
}