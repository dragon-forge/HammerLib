package org.zeith.hammerlib.api.lighting;

import com.google.common.base.Predicates;
import net.minecraft.world.entity.*;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import org.zeith.hammerlib.HammerLib;
import org.zeith.hammerlib.api.forge.BlockAPI;
import org.zeith.hammerlib.api.lighting.impl.*;

import java.util.*;
import java.util.function.*;
import java.util.stream.Stream;

public class ColoredLightManager
{
	private static final List<Function<Float, Stream<ColoredLight>>> lightGenerators = new ArrayList<>();

	public static BooleanSupplier COLORED_LIGHTING_ENABLED = () -> false;
	public static BooleanSupplier SHADER_UNIFORM_SETUP = () -> false;

	public static BooleanSupplier BIND_TERRAIN = () -> false;
	public static BooleanSupplier BIND_ENTITY = () -> false;

	public static BooleanSupplier UNBIND_TERRAIN = () -> false;
	public static BooleanSupplier UNBIND_ENTITY = () -> false;

	public static IntSupplier UNIFORM_LIGHT_COUNT = () -> 0;

	public static int LAST_LIGHTS;

	public static boolean isColoredLightActive()
	{
		return COLORED_LIGHTING_ENABLED.getAsBoolean();
	}

	static
	{
		addGenerator(partialTicks ->
		{
			Player pl = getClientPlayer();
			if(pl != null)
			{
				List<Entity> ents = pl.level().getEntitiesOfClass(Entity.class, pl.getBoundingBox().inflate(128));
				Stream<ColoredLight> players = ents.stream().flatMap(ent ->
				{
					Stream.Builder<ColoredLight> lights = Stream.builder();
					IGlowingItem igi;
					if(ent instanceof LivingEntity)
					{
						LivingEntity base = (LivingEntity) ent;

						ItemStack main = base.getMainHandItem();
						if((igi = IGlowingItem.fromStack(main)) != null)
							lights.add(igi.produceColoredLight(base, main));

						ItemStack off = base.getOffhandItem();
						if((igi = IGlowingItem.fromStack(off)) != null)
							lights.add(igi.produceColoredLight(base, off));
					} else if(ent instanceof ItemEntity)
					{
						ItemEntity ei = (ItemEntity) ent;

						ItemStack item = ei.getItem();
						if((igi = IGlowingItem.fromStack(item)) != null)
							lights.add(igi.produceColoredLight(ei, item));
					}

					return lights.build().filter(Predicates.notNull()).map(l -> l.reposition(ent, partialTicks));
				});
				Stream<ColoredLight> entities = Stream.concat(players, ents.stream().map(e ->
				{
					ColoredLight l = e instanceof IGlowingEntity ? ((IGlowingEntity) e).produceColoredLight(partialTicks) : null;
					HandleLightOverrideEvent evt = new HandleLightOverrideEvent(e, partialTicks, l);
					HammerLib.postEvent(evt);
					return evt.getNewLight();
				}));
				entities = Stream.concat(entities, HammerLib.PROXY.getGlowingParticles(partialTicks));
				Stream<ColoredLight> tiles = BlockAPI.getAllLoadedBlockEntities(pl.level()).stream().map(e ->
				{
					ColoredLight l = e instanceof IGlowingEntity ? ((IGlowingEntity) e).produceColoredLight(partialTicks) : null;
					HandleLightOverrideEvent evt = new HandleLightOverrideEvent(e, partialTicks, l);
					HammerLib.postEvent(evt);
					return evt.getNewLight();
				});
				return Stream.concat(tiles, entities);
			}
			return Stream.empty();
		});
	}

	public static <A, B, C> Function<A, C> dfunc(Function<A, B> fa, Function<B, C> fb)
	{
		return a -> fb.apply(fa.apply(a));
	}

	public static Player getClientPlayer()
	{
		return HammerLib.PROXY.getClientPlayer();
	}

	public static void addGenerator(Function<Float, Stream<ColoredLight>> gen)
	{
		lightGenerators.add(gen);
	}

	/**
	 * @return A stream of non-null lights currently observable for the
	 * {@link #getClientPlayer()}.
	 */
	public static Stream<ColoredLight> generate(float partialTicks)
	{
		return lightGenerators.stream().flatMap(f -> f.apply(partialTicks)).filter(Predicates.notNull());
	}
}