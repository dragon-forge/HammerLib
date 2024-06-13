package org.zeith.hammerlib.event;

import lombok.AllArgsConstructor;
import lombok.Getter;
import net.minecraft.server.packs.resources.ResourceManager;
import net.neoforged.bus.api.Event;
import org.zeith.hammerlib.api.LanguageHelper.LangMap;

/**
 * This event is fired on {@link org.zeith.hammerlib.HammerLib#EVENT_BUS}
 */
@AllArgsConstructor
@Getter
public class LanguageReloadEvent
		extends Event
{
	final LangMap map;
	final ResourceManager resources;
	final String lang;
	
	public LangMap get()
	{
		return map;
	}
	
	public void translate(String key, String value)
	{
		map.translate(key, value);
	}
}