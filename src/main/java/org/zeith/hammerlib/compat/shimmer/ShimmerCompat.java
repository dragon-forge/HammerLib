package org.zeith.hammerlib.compat.shimmer;

import com.lowdragmc.shimmer.client.light.*;
import com.lowdragmc.shimmer.event.ShimmerReloadEvent;
import com.lowdragmc.shimmer.forge.event.ForgeShimmerReloadEvent;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import net.minecraftforge.registries.ForgeRegistries;
import org.zeith.hammerlib.HammerLib;
import org.zeith.hammerlib.api.lighting.ColoredLight;
import org.zeith.hammerlib.api.lighting.impl.*;
import org.zeith.hammerlib.compat.base.*;
import org.zeith.hammerlib.compat.base._hl.*;
import org.zeith.hammerlib.util.java.Cast;

import java.util.*;
import java.util.function.Supplier;

@BaseCompat.LoadCompat(
		modid = "shimmer",
		compatType = BaseHLCompat.class
)
public class ShimmerCompat
		extends BaseHLCompat
{
	private static Set<IGlowingBlock> BLOCKS;
	private static Set<IGlowingItem> ITEMS;
	
	public final BloomAbilityBase bloom = new BloomAbilityBase()
	{
		@Override
		protected Supplier<Supplier<ClientBloomAbilityBase>> forClient()
		{
			return () -> () -> new ClientBloomAbilityBase()
			{
				@Override
				public RenderType emissiveTranslucentArmor(ResourceLocation resourceLocation)
				{
					return RenderType.entityTranslucentEmissive(resourceLocation);
				}
			};
		}
	};
	
	public ShimmerCompat()
	{
		var modBus = FMLJavaModLoadingContext.get().getModEventBus();
		modBus.addListener(this::reloadShimmer);
		HammerLib.LOG.info("Enabling Shimmer compatibility module.");
	}
	
	public static Set<IGlowingBlock> getColoredBlocks()
	{
		if(BLOCKS != null) return BLOCKS;
		BLOCKS = new HashSet<>();
		for(var block : ForgeRegistries.BLOCKS)
			if(block instanceof IGlowingBlock i)
				BLOCKS.add(i);
		return BLOCKS = Set.copyOf(BLOCKS);
	}
	
	public static Set<IGlowingItem> getColoredItems()
	{
		if(ITEMS != null) return ITEMS;
		ITEMS = new HashSet<>();
		for(var item : ForgeRegistries.ITEMS)
			if(item instanceof IGlowingItem i)
				ITEMS.add(i);
		return ITEMS = Set.copyOf(ITEMS);
	}
	
	private ColorPointLight.Template fromPP(ColoredLight src)
	{
		if(src == null) return null;
		return new ColorPointLight.Template(src.radius, src.r, src.g, src.b, src.a);
	}
	
	private void reloadShimmer(ForgeShimmerReloadEvent e)
	{
		if(e.event.getReloadType() == ShimmerReloadEvent.ReloadType.COLORED_LIGHT)
		{
			for(var block : getColoredBlocks())
			{
				LightManager.INSTANCE.registerBlockLight(Cast.cast(block), (state, pos) ->
				{
					var player = HammerLib.PROXY.getClientPlayer();
					if(player == null) return null;
					return fromPP(block.produceColoredLight(player.level(), pos, state, 1F));
				});
			}
			
			for(IGlowingItem item : getColoredItems())
			{
				LightManager.INSTANCE.registerItemLight(Cast.cast(item), stack ->
				{
					var player = HammerLib.PROXY.getClientPlayer();
					if(player == null) return null;
					return fromPP(item.produceColoredLight(player, stack));
				});
			}
		}
	}
	
	@Override
	public <R> Optional<R> getAbility(Ability<R> ability)
	{
		return ability.findIn(
				bloom
		);
	}
}