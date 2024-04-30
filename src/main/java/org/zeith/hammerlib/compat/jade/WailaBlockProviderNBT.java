package org.zeith.hammerlib.compat.jade;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import org.zeith.hammerlib.proxy.HLConstants;
import snownee.jade.api.IServerDataProvider;

public class WailaBlockProviderNBT
		implements IServerDataProvider<BlockEntity>
{
	protected final ResourceLocation id = HLConstants.id("root");
	
	@Override
	public void appendServerData(CompoundTag compoundTag, ServerPlayer serverPlayer, Level level, BlockEntity blockEntity, boolean b)
	{
	
	}
	
	@Override
	public ResourceLocation getUid()
	{
		return id;
	}
}