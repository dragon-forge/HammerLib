package org.zeith.hammerlib.mixins.client;

import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.core.Holder;
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
	
	protected ClientLevelMixin(WritableLevelData p_220352_, ResourceKey<Level> p_220353_, Holder<DimensionType> p_220354_, Supplier<ProfilerFiller> p_220355_, boolean p_220356_, boolean p_220357_, long p_220358_, int p_220359_)
	{
		super(p_220352_, p_220353_, p_220354_, p_220355_, p_220356_, p_220357_, p_220358_, p_220359_);
	}
	
	public UUID iguid$getGUID()
	{
		if(hl$guid == null) // Instead of seed we use current system time.
			hl$guid = new UUID(System.currentTimeMillis(), Hashers.hashCodeL(dimension()));
		return hl$guid;
	}
}