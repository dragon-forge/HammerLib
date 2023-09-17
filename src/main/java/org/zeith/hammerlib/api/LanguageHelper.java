package org.zeith.hammerlib.api;

import net.minecraft.world.entity.player.Player;
import org.zeith.hammerlib.HammerLib;
import org.zeith.hammerlib.event.LanguageReloadEvent;

import java.util.*;
import java.util.function.BiConsumer;

public class LanguageHelper
{
	// This is a direct reference to a hash map of languages stored in client's i18n language at any given moment.
	public static HashMap<String, String> clientLanguageMap = new HashMap<>();
	
	public static void reloadLanguage(HashMap<String, String> handler)
	{
		String lng = HammerLib.PROXY.getLanguage();

		LangMap exist = new LangMap(lng);
		reloadLang("en_us", exist);
		if(!lng.equals("en_us")) reloadLang(lng, exist);
		exist.apply(handler);
		
		clientLanguageMap = handler;
	}

	private static void reloadLang(String lang, LangMap lmap)
	{
		HammerLib.postEvent(new LanguageReloadEvent(lmap, lang));
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