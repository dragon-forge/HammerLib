package org.zeith.hammerlib.event;

import net.neoforged.bus.api.Event;
import org.zeith.hammerlib.api.LanguageHelper.LangMap;

/**
 * This event is fired on {@link org.zeith.hammerlib.HammerLib#EVENT_BUS}
 */
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