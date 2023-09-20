package org.zeith.hammerlib.mixins;

import net.minecraft.core.*;
import net.minecraft.resources.ResourceKey;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.profiling.ProfilerFiller;
import net.minecraft.world.level.*;
import net.minecraft.world.level.dimension.DimensionType;
import net.minecraft.world.level.storage.WritableLevelData;
import org.spongepowered.asm.mixin.*;
import org.zeith.api.util.IGUID;
import org.zeith.hammerlib.util.java.Hashers;

import java.util.*;
import java.util.function.Supplier;

@Mixin(ServerLevel.class)
@Implements({
		@Interface(iface = IGUID.class, prefix = "iguid$")
})
public abstract class ServerLevelMixin
		extends Level
		implements WorldGenLevel
{
	@Unique
	protected UUID hl$guid;
	
	protected ServerLevelMixin(WritableLevelData pLevelData, ResourceKey<Level> pDimension, RegistryAccess pRegistryAccess, Holder<DimensionType> pDimensionTypeRegistration, Supplier<ProfilerFiller> pProfiler, boolean pIsClientSide, boolean pIsDebug, long pBiomeZoomSeed, int pMaxChainedNeighborUpdates)
	{
		super(pLevelData, pDimension, pRegistryAccess, pDimensionTypeRegistration, pProfiler, pIsClientSide, pIsDebug, pBiomeZoomSeed, pMaxChainedNeighborUpdates);
	}
	
	public UUID iguid$getGUID()
	{
		if(hl$guid == null)
			hl$guid = new UUID(getSeed(), Hashers.hashCodeL(dimension()));
		return hl$guid;
	}
}