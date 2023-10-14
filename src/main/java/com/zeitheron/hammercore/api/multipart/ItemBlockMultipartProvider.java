package com.zeitheron.hammercore.api.multipart;

import com.zeitheron.hammercore.internal.blocks.multipart.TileMultipart;
import com.zeitheron.hammercore.utils.base.Cast;
import net.minecraft.block.SoundType;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.*;
import net.minecraft.item.*;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.play.server.SPacketSoundEffect;
import net.minecraft.util.*;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

import java.util.List;

/**
 * ItemBlock for {@link BlockMultipartProvider}.
 */
public class ItemBlockMultipartProvider extends Item
{
	public final IMultipartProvider provider;
	
	public ItemBlockMultipartProvider(IMultipartProvider provider)
	{
		this.provider = provider;
	}
	
	/**
	 * Parameter-less version that casts this item into a provider
	 */
	public ItemBlockMultipartProvider()
	{
		this.provider = (IMultipartProvider) this;
	}
	
	@Override
	public EnumActionResult onItemUse(EntityPlayer player, World worldIn, BlockPos pos, EnumHand hand, EnumFacing facing, float hitX, float hitY, float hitZ)
	{
		if(Cast.cast(worldIn.getTileEntity(pos), TileMultipart.class) == null && !worldIn.getBlockState(pos).getBlock().isReplaceable(worldIn, pos))
			pos = pos.offset(facing);
		
		if(Cast.cast(worldIn.getTileEntity(pos), TileMultipart.class) == null && !worldIn.getBlockState(pos).getBlock().isReplaceable(worldIn, pos))
			return EnumActionResult.FAIL;
		TileMultipart tmp = MultipartAPI.getOrPlaceMultipart(worldIn, pos);
		
		ItemStack itemstack = player.getHeldItem(hand);
		
		if(tmp != null && (!itemstack.hasTagCompound() || !itemstack.getTagCompound().hasKey("LastPlaced")))
		{
			MultipartSignature s = provider.createSignature(tmp.getNextSignatureIndex(), itemstack, player, worldIn, pos, facing, hitX, hitY, hitZ);
			
			if(!tmp.canPlace(s) || !provider.canPlaceInto(tmp, itemstack, player, worldIn, pos, facing, hitX, hitY, hitZ))
			{
				pos = pos.offset(facing);
				if(Cast.cast(worldIn.getTileEntity(pos), TileMultipart.class) == null && !worldIn.getBlockState(pos).getBlock().isReplaceable(worldIn, pos))
					return EnumActionResult.FAIL;
				tmp = MultipartAPI.getOrPlaceMultipart(worldIn, pos);
			}
			
			if(provider.canPlaceInto(tmp, itemstack, player, worldIn, pos, facing, hitX, hitY, hitZ) && tmp.addMultipart(s))
			{
				SoundType soundtype = s.getSoundType(player);
				
				try
				{
					float br = (soundtype.getVolume() + 1.0F * soundtype.getVolume() + 1.0F) * 512F;
					List<EntityPlayerMP> ps = worldIn.getMinecraftServer().getPlayerList().getPlayers();
					for(EntityPlayerMP p : ps)
					{
						if(p.world.provider.getDimension() != worldIn.provider.getDimension())
							continue;
						if(p.getDistanceSq(pos) > br)
							continue;
						p.connection.sendPacket(new SPacketSoundEffect(soundtype.getPlaceSound(), SoundCategory.BLOCKS, pos.getX(), pos.getY(), pos.getZ(), (soundtype.getVolume() + 1F) / 2F, soundtype.getPitch()));
					}
				} catch(Throwable err)
				{
				}
				
				NBTTagCompound nbt = itemstack.getTagCompound();
				if(nbt == null)
					itemstack.setTagCompound(nbt = new NBTTagCompound());
				
				nbt.setInteger("LastPlaced", 1);
				
				if(!worldIn.isRemote && !player.capabilities.isCreativeMode)
					itemstack.shrink(1);
				return EnumActionResult.SUCCESS;
			}
		}
		
		return EnumActionResult.FAIL;
	}
	
	@Override
	public void onUpdate(ItemStack stack, World worldIn, Entity entityIn, int itemSlot, boolean isSelected)
	{
		if(stack.hasTagCompound())
		{
			if(stack.getTagCompound().hasKey("LastPlaced"))
				stack.getTagCompound().removeTag("LastPlaced");
			if(stack.getTagCompound().isEmpty())
				stack.setTagCompound(null);
		}
	}
}