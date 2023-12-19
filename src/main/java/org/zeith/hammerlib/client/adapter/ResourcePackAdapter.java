package org.zeith.hammerlib.client.adapter;

import net.minecraft.SharedConstants;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.packs.*;
import net.minecraft.server.packs.repository.*;
import net.minecraft.world.flag.FeatureFlagSet;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.Mod;
import net.neoforged.neoforge.event.AddPackFindersEvent;
import org.zeith.hammerlib.api.fml.IRegisterListener;

import java.util.*;

@Mod.EventBusSubscriber(bus = Mod.EventBusSubscriber.Bus.MOD)
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
				
				add.accept(Pack.create(
						pack.packId(),
						Component.literal(pack.packId()),
						true,
						new Pack.ResourcesSupplier()
						{
							@Override
							public PackResources openPrimary(String p_294636_)
							{
								return pack;
							}
							
							@Override
							public PackResources openFull(String p_251717_, Pack.Info p_294956_)
							{
								return pack;
							}
						},
						new Pack.Info(
								Component.translatable("fml.resources.modresources", 1),
								SharedConstants.getCurrentVersion().getPackVersion(PackType.SERVER_DATA),
								SharedConstants.getCurrentVersion().getPackVersion(PackType.CLIENT_RESOURCES),
								PackCompatibility.COMPATIBLE,
								FeatureFlagSet.of(),
								List.of(),
								true
						),
						Pack.Position.TOP,
						pack.isHidden(),
						PackSource.BUILT_IN
				));
				
				if(pack instanceof IRegisterListener rl)
					rl.onPostRegistered(new ResourceLocation(pack.packId()));
			}
		});
	}
}