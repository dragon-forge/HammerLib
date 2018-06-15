package com.pengu.hammercore.core.blocks.tesseract;

import java.util.List;

import com.pengu.hammercore.api.iTileBlock;
import com.pengu.hammercore.common.iWrenchable;
import com.pengu.hammercore.common.utils.WorldUtil;
import com.pengu.hammercore.core.gui.GuiManager;
import com.pengu.hammercore.tile.TileSyncable;
import com.pengu.hammercore.utils.WorldLocation;
import com.zeitheron.hammercore.netv2.HCV2Net;
import com.zeitheron.hammercore.netv2.internal.PacketPing;

import net.minecraft.block.Block;
import net.minecraft.block.ITileEntityProvider;
import net.minecraft.block.SoundType;
import net.minecraft.block.material.Material;
import net.minecraft.block.properties.PropertyBool;
import net.minecraft.block.state.BlockStateContainer;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.BlockRenderLayer;
import net.minecraft.util.EnumBlockRenderType;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.world.World;
import net.minecraftforge.common.util.Constants.NBT;

public class BlockTesseract extends Block implements ITileEntityProvider, iTileBlock<TileTesseract>, iWrenchable
{
	public static final PropertyBool active = PropertyBool.create("active");
	
	public BlockTesseract()
	{
		super(Material.IRON);
		setSoundType(SoundType.METAL);
		setHardness(15);
		setResistance(2000);
		setHarvestLevel("pickaxe", 2);
		setUnlocalizedName("tesseract");
	}
	
	@Override
	public void addInformation(ItemStack stack, World player, List<String> tooltip, ITooltipFlag advanced)
	{
		if(stack.hasTagCompound())
		{
			if(stack.getTagCompound().hasKey("Private", NBT.TAG_BYTE))
			{
				boolean b = stack.getTagCompound().getBoolean("Private");
				tooltip.add("Private: " + (b ? TextFormatting.GREEN : TextFormatting.RED) + b + TextFormatting.RESET);
			}
			
			if(stack.getTagCompound().hasKey("Frequency", NBT.TAG_STRING))
			{
				String s = stack.getTagCompound().getString("Frequency");
				tooltip.add("Frequency: " + s);
			}
		}
	}
	
	@Override
	public void onBlockPlacedBy(World worldIn, BlockPos pos, IBlockState state, EntityLivingBase placer, ItemStack stack)
	{
		WorldLocation loc = new WorldLocation(worldIn, pos);
		TileTesseract tess = loc.getTileOfType(TileTesseract.class);
		if(tess == null)
			loc.setTile(tess = new TileTesseract());
		if(stack.hasTagCompound())
		{
			NBTTagCompound nbt = stack.getTagCompound();
			if(nbt.hasKey("Frequency", NBT.TAG_STRING))
				tess.setFrequency(nbt.getString("Frequency"));
			if(nbt.hasKey("Private", NBT.TAG_BYTE))
				tess.isPrivate.set(nbt.getBoolean("Private"));
		}
	}
	
	@Override
	public boolean onBlockActivated(World worldIn, BlockPos pos, IBlockState state, EntityPlayer playerIn, EnumHand hand, EnumFacing facing, float hitX, float hitY, float hitZ)
	{
		TileSyncable tile = WorldUtil.cast(worldIn.getTileEntity(pos), TileSyncable.class);
		GuiManager.openGui(playerIn, tile);
		return tile != null && tile.hasGui();
	}
	
	@Override
	public Class<TileTesseract> getTileClass()
	{
		return TileTesseract.class;
	}
	
	@Override
	public TileEntity createNewTileEntity(World worldIn, int meta)
	{
		return new TileTesseract();
	}
	
	@Override
	public boolean canEntitySpawn(IBlockState state, Entity entityIn)
	{
		return false;
	}
	
	@Override
	public boolean isOpaqueCube(IBlockState state)
	{
		return false;
	}
	
	@Override
	public int getMetaFromState(IBlockState state)
	{
		return state.getValue(active) ? 1 : 0;
	}
	
	@Override
	public IBlockState getStateFromMeta(int meta)
	{
		return getDefaultState().withProperty(active, meta == 0 ? false : true);
	}
	
	@Override
	protected BlockStateContainer createBlockState()
	{
		return new BlockStateContainer(this, active);
	}
	
	@Override
	public EnumBlockRenderType getRenderType(IBlockState state)
	{
		return EnumBlockRenderType.MODEL;
	}
	
	@Override
	public BlockRenderLayer getBlockLayer()
	{
		return BlockRenderLayer.CUTOUT;
	}
	
	@Override
	public boolean onWrenchUsed(WorldLocation loc, EntityPlayer player, EnumHand hand)
	{
		TileTesseract tess = loc.getTileOfType(TileTesseract.class);
		if(player.isSneaking() && tess != null)
		{
			loc.setState(Blocks.AIR.getDefaultState());
			ItemStack stack = new ItemStack(this);
			stack.setTagCompound(new NBTTagCompound());
			stack.getTagCompound().setString("Frequency", tess.frequency.get());
			stack.getTagCompound().setBoolean("Private", tess.isPrivate.get());
			WorldUtil.spawnItemStack(loc, stack);
		}
		return true;
	}
}