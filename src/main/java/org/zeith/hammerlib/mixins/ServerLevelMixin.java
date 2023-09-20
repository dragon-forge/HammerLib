package org.zeith.hammerlib.mixins;

import net.minecraft.core.Holder;
import net.minecraft.resources.ResourceKey;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.profiling.ProfilerFiller;
import net.minecraft.world.level.*;
import net.minecraft.world.level.dimension.DimensionType;
import net.minecraft.world.level.storage.WritableLevelData;
import org.spongepowered.asm.mixin.*;
import org.zeith.api.util.IGUID;
import org.zeith.hammerlib.util.java.Hashers;

import java.util.UUID;
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
	
	protected ServerLevelMixin(WritableLevelData p_220352_, ResourceKey<Level> p_220353_, Holder<DimensionType> p_220354_, Supplier<ProfilerFiller> p_220355_, boolean p_220356_, boolean p_220357_, long p_220358_, int p_220359_)
	{
		super(p_220352_, p_220353_, p_220354_, p_220355_, p_220356_, p_220357_, p_220358_, p_220359_);
	}
	
	public UUID iguid$getGUID()
	{
		if(hl$guid == null)
			hl$guid = new UUID(getSeed(), Hashers.hashCodeL(dimension()));
		return hl$guid;
	}
}