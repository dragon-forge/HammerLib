package com.zeitheron.hammercore.internal.init;

import com.zeitheron.hammercore.annotations.RegisterIf;
import com.zeitheron.hammercore.api.inconnect.EnumConnTexVersion;
import com.zeitheron.hammercore.internal.blocks.BlockBorderedCobblestone;
import com.zeitheron.hammercore.internal.blocks.BlockLyingItem;
import com.zeitheron.hammercore.internal.blocks.multipart.BlockMultipart;

public class BlocksHC
{
	public static final BlockLyingItem LYING_ITEM = new BlockLyingItem();
	public static final BlockMultipart MULTIPART = new BlockMultipart();
	
	@RegisterIf("com.zeitheron.hammercore.cfg.HammerCoreConfigs.hc_registerConnectedBlocks")
	public static final BlockBorderedCobblestone IRON_BORDERED_COBBLESTONE = new BlockBorderedCobblestone("iron");
	
	@RegisterIf("com.zeitheron.hammercore.cfg.HammerCoreConfigs.hc_registerConnectedBlocks")
	public static final BlockBorderedCobblestone GOLD_BORDERED_COBBLESTONE = new BlockBorderedCobblestone("gold");
	
	@RegisterIf("com.zeitheron.hammercore.cfg.HammerCoreConfigs.hc_registerConnectedBlocks")
	public static final BlockBorderedCobblestone DIAMOND_BORDERED_COBBLESTONE = new BlockBorderedCobblestone("diamond", EnumConnTexVersion.V2);
	
	@RegisterIf("com.zeitheron.hammercore.cfg.HammerCoreConfigs.hc_registerConnectedBlocks")
	public static final BlockBorderedCobblestone EMERALD_BORDERED_COBBLESTONE = new BlockBorderedCobblestone("emerald", EnumConnTexVersion.V2);
	
	@RegisterIf("com.zeitheron.hammercore.cfg.HammerCoreConfigs.hc_registerConnectedBlocks")
	public static final BlockBorderedCobblestone QUARTZ_BORDERED_COBBLESTONE = new BlockBorderedCobblestone("quartz", EnumConnTexVersion.V3);
}