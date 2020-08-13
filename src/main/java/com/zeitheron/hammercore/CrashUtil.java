package com.zeitheron.hammercore;

import net.minecraftforge.fml.common.ICrashCallable;
import net.minecraftforge.fml.common.Loader;
import net.minecraftforge.fml.common.ModContainer;
import net.minecraftforge.fml.common.versioning.ArtifactVersion;

class CrashUtil
		implements ICrashCallable
{
	@Override
	public String call()
	{
		StringBuilder sb = new StringBuilder();

		sb.append("\n\t\tDependent Mods:");

		int f = 0;
		withMods:
		for(ModContainer mc : Loader.instance().getActiveModList())
			for(ArtifactVersion av : mc.getRequirements())
				if(av.getLabel().equals("hammercore"))
				{
					sb.append("\n\t\t\t-").append(mc.getName()).append(" (").append(mc.getModId()).append(") @ ").append(mc.getVersion());
					++f;
					continue withMods;
				}
		if(f == 0)
			sb.append(" None.");

		sb.append("\n");

		return sb.toString();
	}

	@Override
	public String getLabel()
	{
		return "HammerCore Debug Information";
	}
}