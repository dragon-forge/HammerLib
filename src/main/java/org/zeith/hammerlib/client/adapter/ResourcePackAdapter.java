package org.zeith.hammerlib.client.adapter;

import net.minecraft.SharedConstants;
import net.minecraft.network.chat.Component;
import net.minecraft.server.packs.PackResources;
import net.minecraft.server.packs.PackType;
import net.minecraft.server.packs.metadata.pack.PackMetadataSection;
import net.minecraft.server.packs.repository.Pack;
import net.minecraft.server.packs.repository.PackSource;
import net.minecraft.world.flag.FeatureFlag;
import net.minecraft.world.flag.FeatureFlagSet;
import net.minecraftforge.event.AddPackFindersEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import org.zeith.hammerlib.api.fml.IRegisterListener;

import java.util.ArrayList;
import java.util.List;

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
				if(pack instanceof IRegisterListener)
					((IRegisterListener) pack).onPreRegistered();
				
				add.accept(Pack.create(
						pack.packId(),
						Component.literal(pack.packId()),
						true,
						(s) -> pack,
						new Pack.Info(
								Component.translatable("fml.resources.modresources", 1),
								SharedConstants.getCurrentVersion().getPackVersion(PackType.CLIENT_RESOURCES),
								FeatureFlagSet.of()
						),
						PackType.CLIENT_RESOURCES,
						Pack.Position.TOP,
						pack.isHidden(),
						PackSource.BUILT_IN
						));
				if(pack instanceof IRegisterListener)
					((IRegisterListener) pack).onPostRegistered();
			}
		});
	}
}