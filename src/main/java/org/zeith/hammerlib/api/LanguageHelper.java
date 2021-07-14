package org.zeith.hammerlib.api;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraftforge.common.MinecraftForge;
import org.zeith.hammerlib.HammerLib;
import org.zeith.hammerlib.event.LanguageReloadEvent;
import org.zeith.hammerlib.util.SidedLocal;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Function;

public class LanguageHelper
{
	private static final Function<String, LangMap> MAP_FACTORY = LangMap::new;
	private static final SidedLocal<String> PREV_LANGS = new SidedLocal<>();
	public static final Map<String, LangMap> LANGS = new HashMap<>();

	public static LangMap getMap(String lang)
	{
		return LANGS.computeIfAbsent(lang.toLowerCase(), MAP_FACTORY);
	}

	public static void reloadLanguage()
	{
		String lng = HammerLib.PROXY.getLanguage();
		List<String> langs = new ArrayList<>();
		langs.add("en_us");
		if(!langs.contains(lng)) langs.add(lng);

		PREV_LANGS.set(lng);
		LangMap exist = LANGS.get(lng);
		if(exist == null)
		{
			exist = MAP_FACTORY.apply(lng);
			for(String lang : langs)
				MinecraftForge.EVENT_BUS.post(new LanguageReloadEvent(exist, lang));
			if(!exist.isEmpty()) LANGS.put(lng, exist);
		} else
		{
			for(String lang : langs)
				MinecraftForge.EVENT_BUS.post(new LanguageReloadEvent(exist, lang));
		}
	}

	public static void update()
	{
		String lng = HammerLib.PROXY.getLanguage();

		if(!PREV_LANGS.equalsTo(lng))
			reloadLanguage();

		if(LANGS.containsKey(lng))
		{
			LangMap map = LANGS.get(lng);
			if(map.isDirty())
			{
				HammerLib.PROXY.applyLang(map);
				map.setDirty(false);
			}
		}
	}

	public static String getLanguage(PlayerEntity player)
	{
		return HammerLib.PROXY.getLanguage(player);
	}

	public static class LangMap
			extends HashMap<String, String>
	{
		final String lang;
		boolean dirty;

		public LangMap(String lng)
		{
			this.lang = lng;
		}

		public void translate(String key, String value)
		{
			put(key, value);
			dirty = true;
		}

		public boolean isDirty()
		{
			return dirty;
		}

		public void setDirty(boolean dirty)
		{
			this.dirty = dirty;
		}

		public void apply(Map<String, String> langs)
		{
			langs.putAll(this);
		}
	}
}