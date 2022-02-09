package org.zeith.hammerlib.client.adapter;

import net.minecraft.server.packs.PackResources;

import java.util.ArrayList;
import java.util.List;

public class ResourcePackAdapter
{
	public static final List<PackResources> BUILTIN_PACKS = new ArrayList<>();

	public static void registerResourcePack(PackResources pack)
	{
		if(!BUILTIN_PACKS.contains(pack))
			BUILTIN_PACKS.add(pack);
	}
}