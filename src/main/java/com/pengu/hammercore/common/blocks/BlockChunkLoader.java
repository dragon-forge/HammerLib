package com.pengu.hammercore.common.blocks;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import com.pengu.hammercore.client.particle.def.ParticleSlowZap;
import com.pengu.hammercore.proxy.ParticleProxy_Client;
import com.pengu.hammercore.utils.ChunkUtils;
import com.pengu.hammercore.world.WorldGenHelper;

import net.minecraft.block.Block;
import net.minecraft.block.SoundType;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.item.ItemStack;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.TextComponentString;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class BlockChunkLoader extends Block
{
	public BlockChunkLoader()
	{
		super(Material.IRON);
		setSoundType(SoundType.METAL);
		setHardness(2F);
		setUnlocalizedName("chunk_loader");
	}
	
	@Override
	public boolean canPlaceBlockAt(World worldIn, BlockPos pos)
	{
		return true;
	}
	
	@Override
	public void onBlockPlacedBy(World worldIn, BlockPos pos, IBlockState state, EntityLivingBase placer, ItemStack stack)
	{
		if(!worldIn.isRemote)
		{
			int world = worldIn.provider.getDimension();
			List<Long> longs = WorldGenHelper.CHUNKLOADERS.get(world);
			if(longs == null)
				WorldGenHelper.CHUNKLOADERS.put(world, longs = new ArrayList<>());
			for(int i = 0; i < longs.size(); ++i)
			{
				long l = longs.get(i);
				BlockPos p = BlockPos.fromLong(l);
				
				if(p.getX() >> 4 == pos.getX() >> 4 && p.getZ() >> 4 == pos.getZ() >> 4)
				{
					placer.sendMessage(new TextComponentString(TextFormatting.RED + "Destroyed Chunk Loader at " + p.getX() + ", " + p.getY() + ", " + p.getZ()));
					longs.remove(l);
					worldIn.destroyBlock(p, true);
				}
			}
			WorldGenHelper.loadChunk(worldIn.provider.getDimension(), pos);
		}
	}
	
	@Override
	public void breakBlock(World worldIn, BlockPos pos, IBlockState state)
	{
		if(!worldIn.isRemote)
			WorldGenHelper.unloadChunk(worldIn.provider.getDimension(), pos);
		super.breakBlock(worldIn, pos, state);
	}
	
	@Override
	@SideOnly(Side.CLIENT)
	public void randomDisplayTick(IBlockState stateIn, World worldIn, BlockPos pos, Random rand)
	{
		if(Minecraft.getMinecraft().debugRenderer.shouldRender())
		{
			BlockPos pos2 = ChunkUtils.getChunkPos(worldIn.getChunkFromChunkCoords(pos.getX() >> 4, pos.getZ() >> 4), new BlockPos(0, pos.getY(), 0));
			BlockPos pos3 = ChunkUtils.getChunkPos(worldIn.getChunkFromChunkCoords(pos.getX() >> 4, pos.getZ() >> 4), new BlockPos(16, pos.getY(), 16));
			
			BlockPos p4, p5;
			
			if(rand.nextBoolean())
			{
				p4 = pos2;
				p5 = pos3;
			} else
			{
				p5 = pos2;
				p4 = pos3;
			}
			
			double x = p4.getX();
			double y = p4.getY() + .5;
			double z = p4.getZ();
			
			double tx = p5.getX();
			double tz = p5.getZ();
			
			ParticleProxy_Client.queueParticleSpawn(new ParticleSlowZap(worldIn, 40, .1F, x, y, z, x, y, tz, .6F, .6F, 1));
			ParticleProxy_Client.queueParticleSpawn(new ParticleSlowZap(worldIn, 40, .1F, x, y, z, tx, y, z, .6F, .6F, 1));
			ParticleProxy_Client.queueParticleSpawn(new ParticleSlowZap(worldIn, 40, .1F, tx, y, tz, x, y, tz, .6F, .6F, 1));
			ParticleProxy_Client.queueParticleSpawn(new ParticleSlowZap(worldIn, 40, .1F, tx, y, tz, tx, y, z, .6F, .6F, 1));
		}
	}
}