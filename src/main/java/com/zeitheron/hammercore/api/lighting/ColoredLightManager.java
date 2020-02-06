package com.zeitheron.hammercore.api.lighting;

import com.google.common.base.Predicates;
import com.zeitheron.hammercore.HammerCore;
import com.zeitheron.hammercore.api.lighting.impl.IGlowingEntity;
import com.zeitheron.hammercore.api.lighting.impl.IGlowingItem;
import com.zeitheron.hammercore.utils.ReflectionUtil;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;

import java.util.ArrayList;
import java.util.List;
import java.util.function.BooleanSupplier;
import java.util.function.Function;
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
			EntityPlayer pl = getClientPlayer();
			if(pl != null)
			{
				Stream<ColoredLight> players = pl.world.loadedEntityList.stream().flatMap(ent ->
				{
					IGlowingItem igi = null;
					if(ent instanceof EntityLivingBase)
					{
						EntityLivingBase base = (EntityLivingBase) ent;
						Stream.Builder<ColoredLight> lights = Stream.builder();
						ItemStack main = base.getHeldItemMainhand();
						if((igi = IGlowingItem.fromStack(main)) != null)
							lights.add(igi.produceColoredLight(base, main));
						ItemStack off = base.getHeldItemOffhand();
						if((igi = IGlowingItem.fromStack(off)) != null)
							lights.add(igi.produceColoredLight(base, off));
						return lights.build().filter(Predicates.notNull()).map(l -> l.reposition(ent, partialTicks));
					} else if(ent instanceof EntityItem)
					{
						EntityItem ei = (EntityItem) ent;
						Stream.Builder<ColoredLight> lights = Stream.builder();
						ItemStack item = ei.getItem();
						if((igi = IGlowingItem.fromStack(item)) != null)
							lights.add(igi.produceColoredLight(ei, item));
						return lights.build().filter(Predicates.notNull()).map(l -> l.reposition(ent, partialTicks));
					}
					return Stream.empty();
				});
				Stream<ColoredLight> entities = Stream.concat(players, pl.world.loadedEntityList.stream().filter(Predicates.instanceOf(IGlowingEntity.class)).map(e -> ((IGlowingEntity) e).produceColoredLight(partialTicks)));
				Stream<ColoredLight> tiles = pl.world.loadedTileEntityList.stream().filter(e -> e instanceof IGlowingEntity).map(e -> ((IGlowingEntity) e).produceColoredLight(partialTicks));
				return Stream.concat(tiles, entities);
			}
			return Stream.empty();
		});
	}

	public static <A, B, C> Function<A, C> dfunc(Function<A, B> fa, Function<B, C> fb)
	{
		return a -> fb.apply(fa.apply(a));
	}

	public static EntityPlayer getClientPlayer()
	{
		return HammerCore.renderProxy.getClientPlayer();
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