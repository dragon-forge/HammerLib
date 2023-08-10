package com.zeitheron.hammercore.api.lighting;

import com.zeitheron.hammercore.HammerCore;
import com.zeitheron.hammercore.api.lighting.impl.*;
import com.zeitheron.hammercore.utils.ReflectionUtil;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;

import java.util.*;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.function.*;
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
	
	public static void registerOperator(BooleanSupplier enabled, BooleanSupplier uniforms, BooleanSupplier bindTerrain, BooleanSupplier bindEntity, BooleanSupplier unbindTerrain, BooleanSupplier unbindEntity)
	{
		BooleanSupplier prevBS = ColoredLightManager.COLORED_LIGHTING_ENABLED;
		ReflectionUtil.setStaticFinalField(ColoredLightManager.class, "COLORED_LIGHTING_ENABLED", (BooleanSupplier) () ->
				(prevBS != null && prevBS.getAsBoolean()) || enabled.getAsBoolean());
		
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
	
	
	// This is used for caching tile entities to prevent lags when you have lots of tiles.
	public static List<IGlowingEntity> glowingTiles = new ArrayList<>();
	public static AtomicInteger prevTileSize = new AtomicInteger();
	
	public static void resetTileCache()
	{
		prevTileSize.set(-1);
		glowingTiles.clear();
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
					if(ent.isInvisibleToPlayer(pl) || !ent.isAddedToWorld())
						return Stream.empty();
					
					if(ent instanceof EntityPlayer && ((EntityPlayer) ent).isSpectator())
						return Stream.empty();
					
					Stream.Builder<ColoredLight> lights = Stream.builder();
					
					IGlowingItem igi;
					if(ent instanceof EntityLivingBase)
					{
						EntityLivingBase base = (EntityLivingBase) ent;
						ItemStack main = base.getHeldItemMainhand();
						if((igi = IGlowingItem.fromStack(main)) != null)
							lights.add(igi.produceColoredLight(base, main));
						ItemStack off = base.getHeldItemOffhand();
						if((igi = IGlowingItem.fromStack(off)) != null)
							lights.add(igi.produceColoredLight(base, off));
					} else if(ent instanceof EntityItem)
					{
						EntityItem ei = (EntityItem) ent;
						ItemStack item = ei.getItem();
						if((igi = IGlowingItem.fromStack(item)) != null)
							lights.add(igi.produceColoredLight(ei, item));
					}
					
					if(ent instanceof IGlowingEntity)
						lights.add(((IGlowingEntity) ent).produceColoredLight(partialTicks));
					
					return lights.build().filter(Objects::nonNull).map(l -> l.reposition(ent, partialTicks));
				});
				
				// Fix for LittleTiles and other cases where there are thousands of TileEntity instances.
				List<TileEntity> allTiles = pl.world.loadedTileEntityList;
				if(prevTileSize.getAndSet(allTiles.size()) != allTiles.size())
				{
					glowingTiles.clear();
					for(TileEntity tile : allTiles)
						if(tile instanceof IGlowingEntity)
							glowingTiles.add((IGlowingEntity) tile);
				}
				
				return Stream.concat(players, glowingTiles.stream().map(e -> e.produceColoredLight(partialTicks)));
			} else resetTileCache();
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
		return lightGenerators.stream().flatMap(f -> f.apply(partialTicks)).filter(Objects::nonNull);
	}
}