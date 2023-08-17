package com.zeitheron.hammercore.internal.init;

import com.zeitheron.hammercore.HammerCore;
import com.zeitheron.hammercore.annotations.*;
import com.zeitheron.hammercore.api.inconnect.EnumConnTexVersion;
import com.zeitheron.hammercore.internal.blocks.*;
import com.zeitheron.hammercore.internal.blocks.multipart.BlockMultipart;

@SimplyRegister(
		creativeTab = @FieldReference(clazz = HammerCore.class, field = "tab")
)
public class BlocksHC
{
	@RegistryName("lying_item")
	public static final BlockLyingItem LYING_ITEM = new BlockLyingItem();
	
	@RegistryName("multipart")
	public static final BlockMultipart MULTIPART = new BlockMultipart();
	
	@RegistryName("iron_bordered_cobblestone")
	@RegisterIf("com.zeitheron.hammercore.cfg.HammerCoreConfigs.hc_registerConnectedBlocks")
	public static final BlockBorderedCobblestone IRON_BORDERED_COBBLESTONE = new BlockBorderedCobblestone();
	
	@RegistryName("gold_bordered_cobblestone")
	@RegisterIf("com.zeitheron.hammercore.cfg.HammerCoreConfigs.hc_registerConnectedBlocks")
	public static final BlockBorderedCobblestone GOLD_BORDERED_COBBLESTONE = new BlockBorderedCobblestone();
	
	@RegistryName("diamond_bordered_cobblestone")
	@RegisterIf("com.zeitheron.hammercore.cfg.HammerCoreConfigs.hc_registerConnectedBlocks")
	public static final BlockBorderedCobblestone DIAMOND_BORDERED_COBBLESTONE = new BlockBorderedCobblestone(EnumConnTexVersion.V2);
	
	@RegistryName("emerald_bordered_cobblestone")
	@RegisterIf("com.zeitheron.hammercore.cfg.HammerCoreConfigs.hc_registerConnectedBlocks")
	public static final BlockBorderedCobblestone EMERALD_BORDERED_COBBLESTONE = new BlockBorderedCobblestone(EnumConnTexVersion.V2);
	
	@RegistryName("quartz_bordered_cobblestone")
	@RegisterIf("com.zeitheron.hammercore.cfg.HammerCoreConfigs.hc_registerConnectedBlocks")
	public static final BlockBorderedCobblestone QUARTZ_BORDERED_COBBLESTONE = new BlockBorderedCobblestone(EnumConnTexVersion.V3);
}