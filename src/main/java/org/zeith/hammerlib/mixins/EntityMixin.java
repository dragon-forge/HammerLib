package org.zeith.hammerlib.mixins;

import net.minecraft.world.entity.Entity;
import org.spongepowered.asm.mixin.*;
import org.zeith.api.util.IGUID;

import java.util.UUID;

@Mixin(Entity.class)
@Implements({
		@Interface(iface = IGUID.class, prefix = "iguid$")
})
public abstract class EntityMixin
{
	@Shadow
	public abstract UUID getUUID();
	
	public UUID iguid$getGUID()
	{
		return getUUID();
	}
}