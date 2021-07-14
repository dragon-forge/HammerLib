package org.zeith.hammerlib.api.lighting;

import net.minecraft.block.Block;
import net.minecraft.entity.EntityType;
import net.minecraft.particles.ParticleType;
import net.minecraft.tileentity.TileEntityType;
import net.minecraftforge.eventbus.api.Event;
import org.zeith.hammerlib.api.lighting.handlers.ILightBlockHandler;
import org.zeith.hammerlib.api.lighting.handlers.ILightEntityHandler;

/**
 * Subscribe to allow colored lighting mods to gather all tiles, entities, particle and other things when they decide it's a proper time to.
 */
public abstract class ReloadLightManagerEvent
		extends Event
{
	public abstract void blockTile(TileEntityType<?> tileType);

	public abstract void blockEntity(EntityType<?> entityType);

	public abstract void blockParticle(ParticleType<?> particleType);

	public abstract void registerBlockLight(Block blk, ILightBlockHandler handler);

	public abstract void registerEntityLight(EntityType<?> ent, ILightEntityHandler handler);
}