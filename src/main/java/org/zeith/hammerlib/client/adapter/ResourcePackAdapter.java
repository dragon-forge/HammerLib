package org.zeith.hammerlib.client.adapter;

import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.packs.*;
import net.minecraft.server.packs.repository.Pack;
import net.minecraft.server.packs.repository.PackSource;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.event.AddPackFindersEvent;
import org.zeith.hammerlib.api.fml.IRegisterListener;

import java.util.*;

@EventBusSubscriber(bus = EventBusSubscriber.Bus.MOD)
public class ResourcePackAdapter
{
	public static final List<PackResources> BUILTIN_PACKS = new ArrayList<>();
	
	public static void registerResourcePack(PackResources pack)
	{
		if(!BUILTIN_PACKS.contains(pack))
			BUILTIN_PACKS.add(pack);
	}
	
	@SubscribeEvent
	public static void addPacks(AddPackFindersEvent e)
	{
		e.addRepositorySource((add) ->
		{
			for(PackResources pack : ResourcePackAdapter.BUILTIN_PACKS)
			{
				if(pack instanceof IRegisterListener rl)
					rl.onPreRegistered(new ResourceLocation(pack.packId()));
				
				add.accept(Pack.readMetaAndCreate(
						new PackLocationInfo(
								pack.packId(),
								Component.literal(pack.packId()),
								PackSource.BUILT_IN,
								Optional.empty()
						),
						new Pack.ResourcesSupplier()
						{
							@Override
							public PackResources openPrimary(PackLocationInfo info)
							{
								return pack;
							}
							
							@Override
							public PackResources openFull(PackLocationInfo info, Pack.Metadata meta)
							{
								return pack;
							}
						},
						PackType.CLIENT_RESOURCES,
						new PackSelectionConfig(
								true,
								Pack.Position.TOP,
								true
						)
				));
				
				if(pack instanceof IRegisterListener rl)
					rl.onPostRegistered(new ResourceLocation(pack.packId()));
			}
		});
	}
}