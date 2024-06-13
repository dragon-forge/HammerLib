package org.zeith.hammerlib.api;

import net.minecraft.server.packs.resources.ResourceManager;
import net.minecraft.world.entity.player.Player;
import org.zeith.hammerlib.HammerLib;
import org.zeith.hammerlib.event.LanguageReloadEvent;

import java.util.*;
import java.util.function.BiConsumer;

public class LanguageHelper
{
	public static void reloadLanguage(Map<String, String> handler, ResourceManager resources, List<String> languageStack)
	{
		for(var lng : languageStack)
		{
			LangMap exist = new LangMap(lng);
			reloadLang(lng, resources, exist);
			exist.apply(handler);
		}
	}

	private static void reloadLang(String lang, ResourceManager resources, LangMap lmap)
	{
		HammerLib.postEvent(new LanguageReloadEvent(lmap, resources, lang));
	}

	public static String getLanguage(Player player)
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

		public void apply(BiConsumer<String, String> langs)
		{
			for(Entry<String, String> e : entrySet())
				langs.accept(e.getKey(), e.getValue());
		}
	}
}