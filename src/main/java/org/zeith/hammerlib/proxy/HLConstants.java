package org.zeith.hammerlib.proxy;

import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.common.registry.GameRegistry;
import net.minecraftforge.registries.IForgeRegistry;
import org.apache.logging.log4j.Logger;
import org.zeith.hammerlib.HammerLib;
import org.zeith.hammerlib.api.multipart.MultipartBlock;

import java.util.*;

public class HLConstants
{
	public static final Map<UUID, String> PLAYER_LANGUAGE_MAP = new HashMap<>();
	public static final HLCommonProxy PROXY = HammerLib.PROXY;
	public static final Logger LOG = HammerLib.LOG;
	public static final String MOD_ID = "hammerlib";
	public static final IForgeRegistry<MultipartBlock> MULTIPARTS = GameRegistry.findRegistry(MultipartBlock.class);
	
	public static ResourceLocation id(String path)
	{
		return new ResourceLocation(MOD_ID, path);
	}
}