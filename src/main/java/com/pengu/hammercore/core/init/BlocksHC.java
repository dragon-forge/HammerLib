package com.pengu.hammercore.core.init;

import com.pengu.hammercore.cfg.HammerCoreConfigs;
import com.pengu.hammercore.common.blocks.BlockIWrenchGhost;
import com.pengu.hammercore.common.blocks.BlockInfiRF;
import com.pengu.hammercore.common.blocks.BlockLyingItem;
import com.pengu.hammercore.common.blocks.multipart.BlockMultipart;
import com.pengu.hammercore.core.blocks.BlockCCT;
import com.pengu.hammercore.core.blocks.BlockChunkLoader;
import com.pengu.hammercore.core.blocks.tesseract.BlockTesseract;

import net.minecraft.block.Block;

public class BlocksHC
{
	public static final Block //
	INFI_RF = new BlockInfiRF(), //
	        MULTIPART = new BlockMultipart(), //
	        TESSERACT, //
	        CHUNK_LOADER, //
	        LYING_ITEM = new BlockLyingItem(), //
	        IWRENCH_GHOST = new BlockIWrenchGhost(); //
	// COMPOUND_CRAFTING_TABLE = new BlockCCT();
	
	static
	{
		CHUNK_LOADER = HammerCoreConfigs.blocks_addChunkloader ? new BlockChunkLoader() : null;
		TESSERACT = HammerCoreConfigs.blocks_addTesseract ? new BlockTesseract() : null;
	}
}