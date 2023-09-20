package org.zeith.hammerlib.mixins.client;

import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.core.*;
import net.minecraft.resources.ResourceKey;
import net.minecraft.util.profiling.ProfilerFiller;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.dimension.DimensionType;
import net.minecraft.world.level.storage.WritableLevelData;
import org.spongepowered.asm.mixin.*;
import org.zeith.api.util.IGUID;
import org.zeith.hammerlib.util.java.Hashers;

import java.util.UUID;
import java.util.function.Supplier;

@Mixin(ClientLevel.class)
@Implements({
		@Interface(iface = IGUID.class, prefix = "iguid$")
})
public abstract class ClientLevelMixin
		extends Level
{
	@Unique
	protected UUID hl$guid;
	
	protected ClientLevelMixin(WritableLevelData pLevelData, ResourceKey<Level> pDimension, RegistryAccess pRegistryAccess, Holder<DimensionType> pDimensionTypeRegistration, Supplier<ProfilerFiller> pProfiler, boolean pIsClientSide, boolean pIsDebug, long pBiomeZoomSeed, int pMaxChainedNeighborUpdates)
	{
		super(pLevelData, pDimension, pRegistryAccess, pDimensionTypeRegistration, pProfiler, pIsClientSide, pIsDebug, pBiomeZoomSeed, pMaxChainedNeighborUpdates);
	}
	
	public UUID iguid$getGUID()
	{
		if(hl$guid == null) // Instead of seed we use current system time.
			hl$guid = new UUID(System.currentTimeMillis(), Hashers.hashCodeL(dimension()));
		return hl$guid;
	}
}