package org.zeith.hammerlib.api.lighting;

import com.google.common.base.Predicates;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.item.ItemEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraftforge.common.MinecraftForge;
import org.zeith.hammerlib.HammerLib;
import org.zeith.hammerlib.api.lighting.impl.IGlowingEntity;
import org.zeith.hammerlib.api.lighting.impl.IGlowingItem;
import org.zeith.hammerlib.util.java.ReflectionUtil;

import java.util.ArrayList;
import java.util.List;
import java.util.function.BooleanSupplier;
import java.util.function.Function;
import java.util.function.IntSupplier;
import java.util.stream.Stream;

public class ColoredLightManager
{
	private static final List<Function<Float, Stream<ColoredLight>>> lightGenerators = new ArrayList<>();

	public static final BooleanSupplier COLORED_LIGHTING_ENABLED = () -> false;
	public static final BooleanSupplier SHADER_UNIFORM_SETUP = () -> false;

	public static final BooleanSupplier BIND_TERRAIN = () -> false;
	public static final BooleanSupplier BIND_ENTITY = () -> false;

	public static final BooleanSupplier UNBIND_TERRAIN = () -> false;
	public static final BooleanSupplier UNBIND_ENTITY = () -> false;

	public static IntSupplier UNIFORM_LIGHT_COUNT = () -> 0;

	public static int LAST_LIGHTS;

	public static boolean isColoredLightActive()
	{
		return COLORED_LIGHTING_ENABLED.getAsBoolean();
	}

	@Deprecated
	public static void registerOperator(BooleanSupplier enabled, BooleanSupplier uniforms, BooleanSupplier bindTerrain, BooleanSupplier unbindTerrain)
	{
		BooleanSupplier prevBS = ColoredLightManager.COLORED_LIGHTING_ENABLED;
		ReflectionUtil.setStaticFinalField(ColoredLightManager.class, "COLORED_LIGHTING_ENABLED", (BooleanSupplier) () -> (prevBS != null && prevBS.getAsBoolean()) || enabled.getAsBoolean());

		BooleanSupplier prevSUS = SHADER_UNIFORM_SETUP;
		ReflectionUtil.setStaticFinalField(ColoredLightManager.class, "SHADER_UNIFORM_SETUP", (BooleanSupplier) () ->
		{
			boolean got = prevSUS.getAsBoolean();
			return uniforms.getAsBoolean() || got;
		});

		BooleanSupplier prevBT = BIND_TERRAIN;
		ReflectionUtil.setStaticFinalField(ColoredLightManager.class, "BIND_TERRAIN", (BooleanSupplier) () ->
		{
			boolean got = prevBT.getAsBoolean();
			return bindTerrain.getAsBoolean() || got;
		});

		BooleanSupplier prevUBT = UNBIND_TERRAIN;
		ReflectionUtil.setStaticFinalField(ColoredLightManager.class, "UNBIND_TERRAIN", (BooleanSupplier) () ->
		{
			boolean got = prevUBT.getAsBoolean();
			return unbindTerrain.getAsBoolean() || got;
		});
	}

	public static void registerOperator(BooleanSupplier enabled, BooleanSupplier uniforms, BooleanSupplier bindTerrain, BooleanSupplier bindEntity, BooleanSupplier unbindTerrain, BooleanSupplier unbindEntity)
	{
		BooleanSupplier prevBS = ColoredLightManager.COLORED_LIGHTING_ENABLED;
		ReflectionUtil.setStaticFinalField(ColoredLightManager.class, "COLORED_LIGHTING_ENABLED", (BooleanSupplier) () -> (prevBS != null && prevBS.getAsBoolean()) || enabled.getAsBoolean());

		BooleanSupplier prevSUS = SHADER_UNIFORM_SETUP;
		ReflectionUtil.setStaticFinalField(ColoredLightManager.class, "SHADER_UNIFORM_SETUP", (BooleanSupplier) () ->
		{
			boolean got = prevSUS.getAsBoolean();
			return uniforms.getAsBoolean() || got;
		});

		BooleanSupplier prevBT = BIND_TERRAIN;
		ReflectionUtil.setStaticFinalField(ColoredLightManager.class, "BIND_TERRAIN", (BooleanSupplier) () ->
		{
			boolean got = prevBT.getAsBoolean();
			return bindTerrain.getAsBoolean() || got;
		});

		BooleanSupplier prevBE = BIND_ENTITY;
		ReflectionUtil.setStaticFinalField(ColoredLightManager.class, "BIND_ENTITY", (BooleanSupplier) () ->
		{
			boolean got = prevBE.getAsBoolean();
			return bindEntity.getAsBoolean() || got;
		});

		BooleanSupplier prevUBT = UNBIND_TERRAIN;
		ReflectionUtil.setStaticFinalField(ColoredLightManager.class, "UNBIND_TERRAIN", (BooleanSupplier) () ->
		{
			boolean got = prevUBT.getAsBoolean();
			return unbindTerrain.getAsBoolean() || got;
		});

		BooleanSupplier prevUBE = UNBIND_ENTITY;
		ReflectionUtil.setStaticFinalField(ColoredLightManager.class, "UNBIND_ENTITY", (BooleanSupplier) () ->
		{
			boolean got = prevUBE.getAsBoolean();
			return unbindEntity.getAsBoolean() || got;
		});
	}

	static
	{
		addGenerator(partialTicks ->
		{
			PlayerEntity pl = getClientPlayer();
			if(pl != null)
			{
				List<Entity> ents = pl.level.getEntitiesOfClass(Entity.class, pl.getBoundingBox().inflate(128));
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
					HandleLightOverrideEvent<Entity> evt = new HandleLightOverrideEvent<>(e, partialTicks, l);
					MinecraftForge.EVENT_BUS.post(evt);
					return evt.getNewLight();
				}));
				entities = Stream.concat(entities, HammerLib.PROXY.getGlowingParticles(partialTicks));
				Stream<ColoredLight> tiles = pl.level.blockEntityList.stream().map(e ->
				{
					ColoredLight l = e instanceof IGlowingEntity ? ((IGlowingEntity) e).produceColoredLight(partialTicks) : null;
					HandleLightOverrideEvent<TileEntity> evt = new HandleLightOverrideEvent<>(e, partialTicks, l);
					MinecraftForge.EVENT_BUS.post(evt);
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

	public static PlayerEntity getClientPlayer()
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