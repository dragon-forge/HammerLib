package org.zeith.hammerlib.client;

import net.minecraft.*;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.*;
import net.minecraft.world.level.ItemLike;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.InterModComms;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.*;
import net.minecraftforge.fml.loading.FMLPaths;
import net.minecraftforge.registries.ForgeRegistries;
import org.zeith.hammerlib.HammerLib;
import org.zeith.hammerlib.api.items.IColoredFoilItem;
import org.zeith.hammerlib.core.init.ItemsHL;
import org.zeith.hammerlib.core.test.machine.BlockTestMachine;
import org.zeith.hammerlib.proxy.HLConstants;
import org.zeith.hammerlib.util.java.*;
import org.zeith.hammerlib.util.java.tuples.*;
import org.zeith.hammerlib.util.shaded.json.*;

import java.io.IOException;
import java.nio.file.*;
import java.util.*;
import java.util.function.ToIntFunction;

@Mod.EventBusSubscriber(bus = Mod.EventBusSubscriber.Bus.MOD)
public class CustomFoilConfigs
{
	private static final List<Tuple2<Item, IColoredFoilItem>> overrides = new ArrayList<>();
	
	public static boolean disable_with_rubidium = true;
	public static Runnable rubidiumInstaller = null;
	
	@SubscribeEvent
	public static void enqueueInterComms(InterModEnqueueEvent e)
	{
		// Constant color example
		InterModComms.sendTo("hammerlib", "registerFoil",
				() -> Map.<ItemLike, Integer>entry(ItemsHL.WRENCH, 0xFFFF0F | IColoredFoilItem.FULL_ALPHA)
		);
		
		// Dynamic color example
		InterModComms.sendTo("hammerlib", "registerFoil",
				() -> Map.<ItemLike, ToIntFunction<ItemStack>>entry(BlockTestMachine.TEST_MACHINE, (ItemStack stack) ->
						0x0F0FFF | IColoredFoilItem.FULL_ALPHA)
		);
	}
	
	@SubscribeEvent
	public static void handleInterComms(InterModProcessEvent evt)
	{
		evt.getIMCStream("registerFoil"::equals).forEach(msg ->
		{
			ItemLike item = null;
			
			var obj = msg.messageSupplier().get();
			if(obj instanceof Map.Entry<?, ?> e)
			{
				item = Cast.cast(e.getKey(), ItemLike.class);
				obj = e.getValue();
			}
			
			IColoredFoilItem foil = null;
			String provider = "";
			
			if(obj instanceof Integer constant)
			{
				foil = IColoredFoilItem.constant(constant);
				provider = "constant(0x" + Integer.toHexString(constant).toUpperCase(Locale.ROOT) + ")";
			} else if(obj instanceof ToIntFunction<?> fun)
			{
				ToIntFunction<ItemStack> stack = (ToIntFunction<ItemStack>) fun;
				foil = stack::applyAsInt;
				provider = "dynamic(" + fun + ")";
			}
			
			Item theItem;
			//noinspection ConstantValue
			if(item == null || (theItem = item.asItem()) == null)
			{
				HammerLib.LOG.warn("{} sent a {} IMC message, but the item can not be determined. Must be ItemLike that returns non-null item.", msg.senderModId(), msg.method());
				return;
			}
			
			if(foil == null)
			{
				HammerLib.LOG.warn("{} sent a {} IMC message, but the foil can not be determined. Must be either Integer, or ToIntFunction<ItemStack>.", msg.senderModId(), msg.method());
				return;
			}
			
			IColoredFoilItem.override(theItem, foil);
			HammerLib.LOG.info("Performed {} from {}. Determined {} foil provider", msg.method(), msg.senderModId(), provider);
		});
	}
	
	public static void reload()
			throws ReportedException
	{
		for(Tuple2<Item, IColoredFoilItem> tup : overrides)
			tup.accept(IColoredFoilItem::removeOverride);
		overrides.clear();
		
		try
		{
			Path path = FMLPaths.CONFIGDIR.get().resolve(HLConstants.MOD_ID).resolve("foil_colors.json");
			
			if(Files.isDirectory(path))
			{
				IOUtils.deleteFolder(path);
			}
			
			if(!Files.exists(path))
			{
				JSONObject $ = new JSONObject();
				
				$.put("disable_with_rubidium", true);
				
				$.put("constant_colors",
						new JSONObject()
								.put(Objects.toString(ForgeRegistries.ITEMS.getKey(Items.NETHER_STAR)),
										0xFF185D | IColoredFoilItem.FULL_ALPHA
								)
								.put(Objects.toString(ForgeRegistries.ITEMS.getKey(Items.ENCHANTED_GOLDEN_APPLE)),
										0xFDFF96 | IColoredFoilItem.FULL_ALPHA
								)
				);
				
				Files.writeString(path, $.toString(2));
			}
			
			var $ = new JSONTokener(Files.readString(path)).nextValueOBJ().orElseThrow();
			
			if(!$.has("disable_with_rubidium"))
			{
				$.put("disable_with_rubidium", true);
				Files.writeString(path, $.toString(2));
			}
			
			disable_with_rubidium = $.optBoolean("disable_with_rubidium");
			
			var constant_colors = $.getJSONObject("constant_colors");
			
			for(String s : constant_colors.keySet())
			{
				var loc = ResourceLocation.tryParse(s);
				if(loc == null)
				{
					HammerLib.LOG.info("foil_colors.json/constant_colors: Unable to parse {} as a valid resource path. Skipping.", JSONObject.quote(s));
					continue;
				}
				
				var item = ForgeRegistries.ITEMS.getValue(loc);
				if(item == null || item == Items.AIR)
				{
					HammerLib.LOG.info("foil_colors.json/constant_colors: Unable to find item " + loc + ". Skipping.");
					continue;
				}
				
				var foil = IColoredFoilItem.constant(constant_colors.getInt(s));
				overrides.add(Tuples.immutable(item, foil));
				IColoredFoilItem.override(item, foil);
			}
			
			if(rubidiumInstaller != null) rubidiumInstaller.run();
		} catch(IOException | JSONException e)
		{
			throw new ReportedException(new CrashReport("", e));
		}
	}
}