package org.zeith.hammerlib.client.model;

import com.google.gson.*;
import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.fml.unsafe.UnsafeHacks;
import net.neoforged.fml.ModLoadingContext;
import net.neoforged.fml.javafmlmod.FMLModContainer;
import net.neoforged.neoforge.client.event.ModelEvent;
import net.neoforged.neoforge.client.model.geometry.IGeometryLoader;
import org.jetbrains.annotations.ApiStatus;
import org.zeith.hammerlib.HammerLib;
import org.zeith.hammerlib.annotations.OnlyIf;
import org.zeith.hammerlib.core.adapter.OnlyIfAdapter;
import org.zeith.hammerlib.proxy.HLConstants;
import org.zeith.hammerlib.util.java.Cast;
import org.zeith.hammerlib.util.mcf.ScanDataHelper;
import org.zeith.hammerlib.util.shaded.json.JSONObject;

import java.util.function.*;

public class SimpleModelGenerator<T extends org.zeith.hammerlib.client.model.IUnbakedGeometry<T>>
		implements IGeometryLoader<T>
{
	private final BiFunction<JsonObject, JsonDeserializationContext, T> factory;
	
	public SimpleModelGenerator(BiFunction<JsonObject, JsonDeserializationContext, T> factory)
	{
		this.factory = factory;
	}
	
	@Override
	public T read(JsonObject jsonObject, JsonDeserializationContext deserializationContext) throws JsonParseException
	{
		return factory.apply(jsonObject, deserializationContext);
	}
	
	@ApiStatus.Internal
	@SuppressWarnings("rawtypes")
	public static void setup()
	{
		ScanDataHelper.lookupAnnotatedObjects(LoadUnbakedGeometry.class).forEach(data ->
		{
			var cTmp = data.getOwnerClass();
			if(org.zeith.hammerlib.client.model.IUnbakedGeometry.class.isAssignableFrom(cTmp))
			{
				var c = cTmp.asSubclass(org.zeith.hammerlib.client.model.IUnbakedGeometry.class);
				var path = data.getProperty("path").map(String.class::cast).orElseThrow();
				
				BiFunction<JsonObject, JsonDeserializationContext, org.zeith.hammerlib.client.model.IUnbakedGeometry> factory = (json, context) -> Cast.cast(UnsafeHacks.newInstance(c));
				
				var loaderId = new ResourceLocation(data.getOwnerMod().map(FMLModContainer::getModId).orElse(HLConstants.MOD_ID), path);
				
				OnlyIf condition = null;
				
				for(var ctor : c.getDeclaredConstructors())
				{
					condition = ctor.getDeclaredAnnotation(OnlyIf.class);
					if(!OnlyIfAdapter.checkCondition(condition, c.toString(), "UnbakedModel", null, loaderId))
						continue;
					
					if(ctor.getParameterCount() == 2 && ((ctor.getParameterTypes()[0].isAssignableFrom(JsonObject.class) && ctor.getParameterTypes()[1].isAssignableFrom(JsonDeserializationContext.class)) || (ctor.getParameterTypes()[0].isAssignableFrom(JsonDeserializationContext.class) && ctor.getParameterTypes()[1].isAssignableFrom(JsonObject.class))))
					{
						var flip = ctor.getParameterTypes()[0].isAssignableFrom(JsonDeserializationContext.class);
						final var ctxCtor = ctor;
						factory = (json, context) ->
						{
							try
							{
								ctxCtor.setAccessible(true);
								return Cast.cast(flip ? ctxCtor.newInstance(context, json) : ctxCtor.newInstance(json, context));
							} catch(Throwable err)
							{
								err.printStackTrace();
							}
							return Cast.cast(UnsafeHacks.newInstance(c));
						};
						break;
					} else if(ctor.getParameterCount() == 1 && ctor.getParameterTypes()[0].isAssignableFrom(JsonObject.class))
					{
						final var ctxCtor = ctor;
						factory = (json, context) ->
						{
							try
							{
								ctxCtor.setAccessible(true);
								return Cast.cast(ctxCtor.newInstance(json));
							} catch(Throwable err)
							{
								err.printStackTrace();
							}
							return Cast.cast(UnsafeHacks.newInstance(c));
						};
						break;
					} else if(ctor.getParameterCount() == 1 && ctor.getParameterTypes()[0].isAssignableFrom(JsonDeserializationContext.class))
					{
						final var ctxCtor = ctor;
						factory = (json, context) ->
						{
							try
							{
								ctxCtor.setAccessible(true);
								return Cast.cast(ctxCtor.newInstance(context));
							} catch(Throwable err)
							{
								err.printStackTrace();
							}
							return Cast.cast(UnsafeHacks.newInstance(c));
						};
						break;
					}
				}
				
				if(condition == null)
				{
					try
					{
						condition = c.getDeclaredConstructor().getDeclaredAnnotation(OnlyIf.class);
					} catch(NoSuchMethodException ignored)
					{
					}
				}
				
				final var factoryFinal = factory;
				
				if(OnlyIfAdapter.checkCondition(condition, c.toString(), "UnbakedModel", null, loaderId))
					data.getOwnerMod()
							.ifPresent(mc ->
									mc.getEventBus().addListener((Consumer<ModelEvent.RegisterGeometryLoaders>) evt ->
											{
												evt.register(path, new SimpleModelGenerator<>(factoryFinal));
												HammerLib.LOG.info("Registered a new model with loader " + JSONObject.quote(ModLoadingContext.get().getActiveNamespace() + ":" + path));
											}
									)
							);
			}
		});
	}
}