package org.zeith.hammerlib.core.recipes.replacers;

import com.google.gson.*;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.registries.*;
import org.zeith.api.registry.RegistryMapping;
import org.zeith.hammerlib.annotations.*;
import org.zeith.hammerlib.proxy.HLConstants;

import java.util.*;

@SimplyRegister
@Mod.EventBusSubscriber(bus = Mod.EventBusSubscriber.Bus.MOD)
public class RemainingReplacerRegistrar
{
	@RegistryName("none")
	public static final IRemainingItemReplacer NONE = (container, slot, item) -> item;
	
	private static IForgeRegistry<IRemainingItemReplacer> registry;
	
	public static List<IRemainingItemReplacer> fromJson(JsonArray object)
	{
		List<IRemainingItemReplacer> lst = new ArrayList<>();
		for(JsonElement element : object)
		{
			var r = get(new ResourceLocation(element.getAsString()));
			if(r != null) lst.add(r);
		}
		return lst;
	}
	
	public static void toNetwork(List<IRemainingItemReplacer> lst, FriendlyByteBuf buf)
	{
		var replacers = lst.stream().map(RemainingReplacerRegistrar::key).filter(Objects::nonNull).toList();
		buf.writeShort(replacers.size());
		for(var r : replacers) buf.writeResourceLocation(r);
	}
	
	public static List<IRemainingItemReplacer> fromNetwork(FriendlyByteBuf buf)
	{
		List<IRemainingItemReplacer> lst = new ArrayList<>();
		short size = buf.readShort();
		for(int i = 0; i < size; i++)
		{
			var r = get(buf.readResourceLocation());
			if(r != null) lst.add(r);
		}
		return lst;
	}
	
	public static IForgeRegistry<IRemainingItemReplacer> registry()
	{
		return registry;
	}
	
	public static IRemainingItemReplacer get(ResourceLocation key)
	{
		return registry.getValue(key);
	}
	
	public static ResourceLocation key(IRemainingItemReplacer replacer)
	{
		return registry.getKey(replacer);
	}
	
	@SubscribeEvent
	public static void allocRegistry(NewRegistryEvent e)
	{
		e.create(new RegistryBuilder<IRemainingItemReplacer>()
						.setName(HLConstants.id("recipe_replacer"))
						.disableSync()
						.disableSaving()
						.disableOverrides()
						.setDefaultKey(HLConstants.id("none")),
				r -> RegistryMapping.report(IRemainingItemReplacer.class, registry = r, false)
		);
	}
}