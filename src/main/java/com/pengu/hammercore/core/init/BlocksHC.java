package com.pengu.hammercore.core.init;

import com.pengu.hammercore.common.blocks.BlockChunkLoader;
import com.pengu.hammercore.common.blocks.BlockIWrenchGhost;
import com.pengu.hammercore.common.blocks.BlockInfiRF;
import com.pengu.hammercore.common.blocks.BlockLyingItem;
import com.pengu.hammercore.common.blocks.multipart.BlockMultipart;
import com.pengu.hammercore.common.blocks.tesseract.BlockTesseract;

import net.minecraft.block.Block;

public class BlocksHC
{
	public static final Block //
	INFI_RF = new BlockInfiRF(), //
	        MULTIPART = new BlockMultipart(), //
	        TESSERACT = new BlockTesseract(), //
	        CHUNK_LOADER = new BlockChunkLoader(), //
	        LYING_ITEM = new BlockLyingItem(), //
	        IWRENCH_GHOST = new BlockIWrenchGhost();
}