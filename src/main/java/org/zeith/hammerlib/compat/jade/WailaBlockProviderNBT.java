package org.zeith.hammerlib.compat.jade;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.resources.ResourceLocation;
import org.zeith.hammerlib.proxy.HLConstants;
import snownee.jade.api.BlockAccessor;
import snownee.jade.api.IServerDataProvider;

public class WailaBlockProviderNBT
		implements IServerDataProvider<BlockAccessor>
{
	protected final ResourceLocation id = HLConstants.id("root");
	
	@Override
	public void appendServerData(CompoundTag nbt, BlockAccessor accessor)
	{
	
	}
	
	@Override
	public ResourceLocation getUid()
	{
		return id;
	}
}