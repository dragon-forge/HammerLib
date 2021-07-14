package org.zeith.hammerlib.event;

import net.minecraftforge.eventbus.api.Event;
import org.zeith.hammerlib.api.LanguageHelper.LangMap;

public class LanguageReloadEvent
		extends Event
{
	final LangMap map;
	final String lng;

	public LanguageReloadEvent(LangMap map, String lng)
	{
		this.map = map;
		this.lng = lng;
	}

	public LangMap get()
	{
		return map;
	}

	public String getLang()
	{
		return lng;
	}

	public void translate(String key, String value)
	{
		map.translate(key, value);
	}
}