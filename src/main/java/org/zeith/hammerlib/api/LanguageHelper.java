package org.zeith.hammerlib.api;

import net.minecraft.world.entity.player.Player;
import org.zeith.hammerlib.HammerLib;
import org.zeith.hammerlib.event.LanguageReloadEvent;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.BiConsumer;

public class LanguageHelper
{
	public static void reloadLanguage(BiConsumer<String, String> handler)
	{
		String lng = HammerLib.PROXY.getLanguage();
		List<String> langs = new ArrayList<>();
		langs.add("en_us");
		if(!langs.contains(lng)) langs.add(lng);

		LangMap exist = new LangMap(lng);
		reloadLang("en_us", exist);
		if(!lng.equals("en_us")) reloadLang(lng, exist);
		exist.apply(handler);
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