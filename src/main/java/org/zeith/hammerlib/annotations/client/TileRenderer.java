package org.zeith.hammerlib.annotations.client;

import net.neoforged.api.distmarker.Dist;
import net.neoforged.fml.DistExecutor;
import org.jetbrains.annotations.ApiStatus;
import org.objectweb.asm.Type;
import org.zeith.hammerlib.HammerLib;
import org.zeith.hammerlib.client.render.tile.IBESR;
import org.zeith.hammerlib.util.mcf.ScanDataHelper;

import java.lang.annotation.*;
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
	record Info(Runnable applicant)
	{
		public static final Map<ScanDataHelper.ScanTarget, Info> TILE_RENDERS = DistExecutor.unsafeCallWhenOn(Dist.CLIENT, () -> () -> ScanDataHelper.gatherScans(TileRenderer.class, ElementType.FIELD, (target, mc, data) ->
				new Info(
						() -> mc.getEventBus().addListener(
								HammerLib.PROXY.addTESR(data.clazz(), data.getMemberName(), data.getProperty("value")
										.map(Type.class::cast).orElse(null))
						)
				)
		));
		
		public static Info get(Class<?> cls, String field)
		{
			return TILE_RENDERS.get(ScanDataHelper.ScanTarget.of(cls, field));
		}
		
		public void apply()
		{
			applicant.run();
		}
	}
}