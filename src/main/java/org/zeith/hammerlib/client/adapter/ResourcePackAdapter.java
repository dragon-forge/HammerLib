package org.zeith.hammerlib.client.adapter;

import net.minecraft.SharedConstants;
import net.minecraft.network.chat.Component;
import net.minecraft.server.packs.PackResources;
import net.minecraft.server.packs.PackType;
import net.minecraft.server.packs.metadata.pack.PackMetadataSection;
import net.minecraft.server.packs.repository.Pack;
import net.minecraft.server.packs.repository.PackSource;
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
		e.addRepositorySource((add, ctor) ->
		{
			for(PackResources pack : ResourcePackAdapter.BUILTIN_PACKS)
			{
				if(pack instanceof IRegisterListener)
					((IRegisterListener) pack).onPreRegistered();
				add.accept(ctor.create(pack.getName(), Component.literal(pack.getName()), true, () -> pack, new PackMetadataSection(Component.translatable("fml.resources.modresources", 1), PackType.CLIENT_RESOURCES.getVersion(SharedConstants.getCurrentVersion())), Pack.Position.TOP, PackSource.BUILT_IN, pack.isHidden()));
				if(pack instanceof IRegisterListener)
					((IRegisterListener) pack).onPostRegistered();
			}
		});
	}
}