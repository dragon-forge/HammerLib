package com.zeitheron.hammercore.internal.init;

import com.zeitheron.hammercore.api.inconnect.EnumConnTexVersion;
import com.zeitheron.hammercore.internal.blocks.BlockBorderedCobblestone;
import com.zeitheron.hammercore.internal.blocks.BlockLyingItem;
import com.zeitheron.hammercore.internal.blocks.multipart.BlockMultipart;

public class BlocksHC
{
	public static final BlockLyingItem LYING_ITEM = new BlockLyingItem();
	public static final BlockMultipart MULTIPART = new BlockMultipart();
	
	public static final BlockBorderedCobblestone IRON_BORDERED_COBBLESTONE = new BlockBorderedCobblestone("iron");
	public static final BlockBorderedCobblestone GOLD_BORDERED_COBBLESTONE = new BlockBorderedCobblestone("gold");
	public static final BlockBorderedCobblestone DIAMOND_BORDERED_COBBLESTONE = new BlockBorderedCobblestone("diamond", EnumConnTexVersion.V2);
	public static final BlockBorderedCobblestone EMERALD_BORDERED_COBBLESTONE = new BlockBorderedCobblestone("emerald", EnumConnTexVersion.V2);
}