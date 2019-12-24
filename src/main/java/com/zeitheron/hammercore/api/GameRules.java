package com.zeitheron.hammercore.api;

import java.util.HashMap;
import java.util.Map;

import javax.annotation.Nonnull;

import com.zeitheron.hammercore.HammerCore;

import net.minecraft.server.MinecraftServer;
import net.minecraft.world.World;
import net.minecraft.world.WorldServer;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;

/**
 * A complete control over Game Rule API
 */
public class GameRules
{
	public static final Map<String, GameRuleEntry> entries = new HashMap<>();

	/**
	 * Registers a gamerule to the game. Call under
	 * {@link FMLPreInitializationEvent}
	 *
	 * @param entry The entry to register
	 */
	public static void registerGameRule(GameRuleEntry entry)
	{
		entries.put(entry.name, entry);
	}

	/**
	 * Gets the registered gamerule by the id.
	 *
	 * @param name The name of custom gamerule to obtain
	 * @return The registered gamerule, or NIL
	 */
	@Nonnull
	public static GameRuleEntry getEntry(String name)
	{
		return entries.getOrDefault(name, GameRuleEntry.NIL);
	}

	/**
	 * Internal method.
	 *
	 * @param server The server
	 */
	public static void load(MinecraftServer server)
	{

	}

	/**
	 * Internal method.
	 */
	public static void cleanup()
	{
		// HammerCore.LOG.info("Saving custom GameRule data...");
		//
		//
	}

	/**
	 * The class for the game rule.
	 */
	public static class GameRuleEntry
	{
		private static final GameRuleEntry NIL = new GameRuleEntry("unknown", "", "gamerules.hc_unknown", ValueType.STRING_VALUE);

		public final String name, i18nDesc;
		public final Object defVal;
		public final ValueType type;

		/**
		 * Creates a gamerule entry, register with
		 * {@link GameRules#registerGameRule(GameRuleEntry)} at
		 * {@link FMLPreInitializationEvent}.
		 *
		 * @param name     the name of gamerule (will be used as 2nd argument in
		 *                 /gamerule command)
		 * @param defValue the default value of this gamerule
		 * @param i18nDesc the description of this gamerule.
		 * @param type     the value type of this gamerule.
		 */
		public GameRuleEntry(String name, Object defValue, String i18nDesc, ValueType type)
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
			if(!world.getGameRules().hasRule(name))
				return (String) defVal;
			return world.getGameRules().getString(name);
		}

		public int getInt(World world)
		{
			if(this == NIL)
				return 0;
			if(type != ValueType.INT_VALUE)
				return 0;
			if(!world.getGameRules().hasRule(name))
				return (int) defVal;
			return world.getGameRules().getInt(name);
		}

		public boolean getBool(World world)
		{
			if(this == NIL)
				return false;
			if(type != ValueType.BOOLEAN_VALUE)
				return false;
			if(!world.getGameRules().hasRule(name))
				return (boolean) defVal;
			return world.getGameRules().getBoolean(name);
		}

		public double getDouble(World world)
		{
			if(this == NIL)
				return 0;
			if(type != ValueType.DECIMAL_VALUE)
				return 0;
			if(!world.getGameRules().hasRule(name))
				return ((Number) defVal).doubleValue();
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
			if(!world.getGameRules().hasRule(name))
				return ((Number) defVal).floatValue();
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