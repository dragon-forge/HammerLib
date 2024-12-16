package org.zeith.hammerlib.mixins;

import net.minecraft.core.Holder;
import net.minecraft.core.RegistryAccess;
import net.minecraft.resources.ResourceKey;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.WorldGenLevel;
import net.minecraft.world.level.dimension.DimensionType;
import net.minecraft.world.level.storage.WritableLevelData;
import org.spongepowered.asm.mixin.*;
import org.zeith.api.util.IGUID;
import org.zeith.hammerlib.util.java.Hashers;

import java.util.UUID;

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
	
	protected ServerLevelMixin(WritableLevelData pLevelData, ResourceKey<Level> pDimension, RegistryAccess pRegistryAccess, Holder<DimensionType> pDimensionTypeRegistration, boolean pIsClientSide, boolean pIsDebug, long pBiomeZoomSeed, int pMaxChainedNeighborUpdates)
	{
		super(pLevelData, pDimension, pRegistryAccess, pDimensionTypeRegistration, pIsClientSide, pIsDebug, pBiomeZoomSeed, pMaxChainedNeighborUpdates);
	}
	
	public UUID iguid$getGUID()
	{
		if(hl$guid == null)
			hl$guid = new UUID(getSeed(), Hashers.hashCodeL(dimension()));
		return hl$guid;
	}
}