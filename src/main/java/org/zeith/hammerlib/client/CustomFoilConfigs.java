package org.zeith.hammerlib.client;

import net.minecraft.*;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.*;
import net.minecraftforge.fml.loading.FMLPaths;
import org.zeith.hammerlib.HammerLib;
import org.zeith.hammerlib.api.items.IColoredFoilItem;
import org.zeith.hammerlib.proxy.HLConstants;
import org.zeith.hammerlib.util.java.IOUtils;
import org.zeith.hammerlib.util.java.tuples.*;
import org.zeith.hammerlib.util.shaded.json.*;

import java.io.IOException;
import java.nio.file.*;
import java.util.*;

public class CustomFoilConfigs
{
	private static final List<Tuple2<Item, IColoredFoilItem>> overrides = new ArrayList<>();
	
	@SuppressWarnings("deprecation")
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
				
				$.put("constant_colors",
						new JSONObject()
								.put(BuiltInRegistries.ITEM.getKey(Items.NETHER_STAR).toString(),
										0xFF185D | IColoredFoilItem.FULL_ALPHA
								)
								.put(BuiltInRegistries.ITEM.getKey(Items.ENCHANTED_GOLDEN_APPLE).toString(),
										0xFDFF96 | IColoredFoilItem.FULL_ALPHA
								)
				);
				
				Files.writeString(path, $.toString(2));
			}
			
			var $ = new JSONTokener(Files.readString(path)).nextValueOBJ().orElseThrow();
			
			var constant_colors = $.getJSONObject("constant_colors");
			
			for(String s : constant_colors.keySet())
			{
				var loc = ResourceLocation.tryParse(s);
				if(loc == null)
				{
					HammerLib.LOG.info("foil_colors.json/constant_colors: Unable to parse {} as a valid resource path. Skipping.", JSONObject.quote(s));
					continue;
				}
				
				var item = BuiltInRegistries.ITEM.get(loc);
				if(item == null || item == Items.AIR)
				{
					HammerLib.LOG.info("foil_colors.json/constant_colors: Unable to find item " + loc + ". Skipping.");
					continue;
				}
				
				var foil = IColoredFoilItem.constant(constant_colors.getInt(s));
				overrides.add(Tuples.immutable(item, foil));
				IColoredFoilItem.override(item, foil);
			}
		} catch(IOException | JSONException e)
		{
			throw new ReportedException(new CrashReport("", e));
		}
	}
}