package org.zeith.hammerlib.annotations.client;

import net.minecraft.Util;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.fml.DistExecutor;
import org.jetbrains.annotations.ApiStatus;
import org.objectweb.asm.Type;
import org.zeith.hammerlib.HammerLib;
import org.zeith.hammerlib.client.render.tile.IBESR;
import org.zeith.hammerlib.util.mcf.ScanDataHelper;

import java.lang.annotation.*;
import java.util.HashMap;
import java.util.Map;

/**
 * Annotate a TileEntityType constant, and provide a value to {@link net.minecraft.client.renderer.blockentity.BlockEntityRenderer} or {@link IBESR}
 */
@Retention(RetentionPolicy.CLASS)
@Target(ElementType.FIELD)
public @interface TileRenderer
{
	Class<?> value();
	
	@ApiStatus.Internal
	record RendererTarget(Type owner, String field)
	{
		public static final Map<TileRenderer.RendererTarget, TileRenderer.RendererInfo> TILE_RENDERS = DistExecutor.unsafeCallWhenOn(Dist.CLIENT, () -> () -> Util.make(new HashMap<>(), map ->
		{
			ScanDataHelper.lookupAnnotatedObjects(TileRenderer.class).forEach(data ->
			{
				if(data.getTargetType() == ElementType.FIELD)
					data.getOwnerMod()
							.ifPresent(mc ->
									map.put(new TileRenderer.RendererTarget(data.clazz(), data.getMemberName()), new TileRenderer.RendererInfo(
											() -> mc.getEventBus().addListener(
													HammerLib.PROXY.addTESR(data.clazz(), data.getMemberName(), data.getProperty("value").map(Type.class::cast).orElse(null))
											)
									))
							);
			});
		}));
		
		public static RendererInfo get(Class<?> cls, String field)
		{
			return TILE_RENDERS.get(new RendererTarget(Type.getType(cls), field));
		}
	}
	
	@ApiStatus.Internal
	record RendererInfo(Runnable applicant)
	{
		public void apply()
		{
			applicant.run();
		}
	}
}