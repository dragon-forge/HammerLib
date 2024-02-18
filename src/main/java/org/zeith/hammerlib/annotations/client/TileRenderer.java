package org.zeith.hammerlib.annotations.client;

import net.minecraft.resources.ResourceLocation;
import org.objectweb.asm.Type;
import org.zeith.hammerlib.HammerLib;
import org.zeith.hammerlib.annotations.ap.*;
import org.zeith.hammerlib.client.render.tile.IBESR;

import java.lang.annotation.*;
import java.lang.reflect.Field;

/**
 * Annotate a TileEntityType constant, and provide a value to {@link net.minecraft.client.renderer.blockentity.BlockEntityRenderer} or {@link IBESR}
 */
@Target(ElementType.FIELD)
public @interface TileRenderer
{
	Class<?> value();
	
	class AP
			implements IAnnotationProcessor<TileRenderer>
	{
		@Override
		public void onPostRegistered(IAPContext context, TileRenderer annotation, Field field, Object value)
		{
			var mc = context.getOwnerMod().orElse(null);
			if(mc != null)
			{
				mc.getEventBus().addListener(
						HammerLib.PROXY.addTESR(
								Type.getType(field.getDeclaringClass()), field.getName(),
								Type.getType(annotation.value())
						)
				);
				var id = context.getRegistryName().map(ResourceLocation::toString).orElse("??:??");
				HammerLib.LOG.debug("Applied TileRenderer for {}[{}] {}.{}", field.getType().getSimpleName(), id, field.getDeclaringClass().getSimpleName(), field.getName());
			}
		}
	}
}