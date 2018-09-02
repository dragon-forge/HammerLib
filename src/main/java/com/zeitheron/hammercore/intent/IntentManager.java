package com.zeitheron.hammercore.intent;

import java.util.Optional;
import java.util.stream.Stream;

import javax.annotation.Nullable;

import com.zeitheron.hammercore.lib.zlib.utils.IndexedMap;
import com.zeitheron.hammercore.utils.NPEUtils;

import net.minecraftforge.fml.common.Loader;
import net.minecraftforge.fml.common.ModContainer;

public class IntentManager
{
	private static final IIntentHandler DEF_INTENT = (modid, data) -> null;
	private static final IndexedMap<String, IndexedMap<Class, IIntentHandler>> intents = new IndexedMap<>();
	
	/**
	 * Registers an {@link IIntentHandler}. Should be called during mod
	 * construction.
	 */
	public static <T> void registerIntentHandler(String name, Class<T> data, IIntentHandler<T> intent)
	{
		IndexedMap<Class, IIntentHandler> ints = intents.get(name);
		if(ints == null)
			intents.put(name, ints = new IndexedMap<>());
		ints.put(data, intent);
	}
	
	/**
	 * Gets an {@link IIntentHandler} that was ALREADY registered.
	 */
	public static <T> IIntentHandler<T> getIntentHandler(String name, Class<T> data)
	{
		IndexedMap<Class, IIntentHandler> ints = intents.get(name);
		if(ints != null)
		{
			Stream<Class> str = ints.getKeys().stream().filter(c -> c.isAssignableFrom(data));
			Optional<Class> opt = str.findFirst();
			return opt.isPresent() ? ints.get(opt.get()) : DEF_INTENT;
		}
		return DEF_INTENT;
	}
	
	@Nullable
	public static <T> Object sendIntent(String name, T data)
	{
		NPEUtils.checkNotNull(data, "data can not be null!");
		NPEUtils.checkNotNull(name, "name can not be null!");
		IIntentHandler intent = getIntentHandler(name, data.getClass());
		ModContainer mc = Loader.instance().activeModContainer();
		if(intent != null)
			return intent.execute(mc != null ? mc.getModId() : "hammercore", data);
		return null;
	}
}