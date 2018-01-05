package com.pengu.hammercore.intent;

import java.util.Optional;
import java.util.stream.Stream;

import javax.annotation.Nullable;

import com.pengu.hammercore.utils.IndexedMap;
import com.pengu.hammercore.utils.NPEUtils;

import net.minecraftforge.fml.common.Loader;
import net.minecraftforge.fml.common.ModContainer;

public class IntentManager
{
	private static final iIntentHandler DEF_INTENT = (modid, data) -> null;
	private static final IndexedMap<String, IndexedMap<Class, iIntentHandler>> intents = new IndexedMap<>();
	
	/**
	 * Registers an {@link iIntentHandler}. Should be called during mod
	 * construction.
	 */
	public static <T> void registerIntentHandler(String name, Class<T> data, iIntentHandler<T> intent)
	{
		IndexedMap<Class, iIntentHandler> ints = intents.get(name);
		if(ints == null)
			intents.put(name, ints = new IndexedMap<>());
		ints.put(data, intent);
	}
	
	/**
	 * Gets an {@link iIntentHandler} that was ALREADY registered.
	 */
	public static <T> iIntentHandler<T> getIntentHandler(String name, Class<T> data)
	{
		IndexedMap<Class, iIntentHandler> ints = intents.get(name);
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
		iIntentHandler intent = getIntentHandler(name, data.getClass());
		ModContainer mc = Loader.instance().activeModContainer();
		if(intent != null)
			return intent.execute(mc != null ? mc.getModId() : "hammercore", data);
		return null;
	}
}