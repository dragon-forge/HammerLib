package com.zeitheron.hammercore.client.render.tesr;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import org.lwjgl.opengl.GL11;

import com.zeitheron.hammercore.client.render.item.ItemRenderingHandler;
import com.zeitheron.hammercore.client.render.item.IItemRender;
import com.zeitheron.hammercore.client.utils.DestroyStageTexture;
import com.zeitheron.hammercore.client.utils.RenderBlocks;
import com.zeitheron.hammercore.tile.TileSyncable;

import net.minecraft.block.Block;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.BufferBuilder;
import net.minecraft.client.renderer.tileentity.TileEntitySpecialRenderer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.util.math.RayTraceResult.Type;
import net.minecraftforge.fml.client.registry.ClientRegistry;

public abstract class TESR<T extends TileEntity> extends TileEntitySpecialRenderer<T> implements IItemRender
{
	/** This is safe to use while rendering */
	protected float destroyProgress;
	protected Minecraft mc = Minecraft.getMinecraft();
	
	public void bindTo(Class<? extends T> tileClass)
	{
		ClientRegistry.bindTileEntitySpecialRenderer(tileClass, this);
	}
	
	public void bindTo(Item item)
	{
		ItemRenderingHandler.INSTANCE.bindItemRender(item, this);
	}
	
	public void bindTo(Block block)
	{
		bindTo(Item.getItemFromBlock(block));
	}
	
	public void bindTo(Class<? extends T> tileClass, Block block)
	{
		bindTo(tileClass);
		bindTo(block);
	}
	
	protected void translateFromOrientation(double x, double y, double z, EnumFacing facing)
	{
		int orientation = facing.ordinal();
		
		if(orientation == 0)
		{
			GL11.glTranslated(x + .5, y + 1, z + .5);
			GL11.glRotated(180, 1, 0, 0);
		} else if(orientation == 1)
		{
			GL11.glTranslated(x + .5, y, z + .5);
		} else if(orientation == 2)
		{
			GL11.glTranslated(x + .5, y + .5, z + 1);
			GL11.glRotatef(-90, 1, 0, 0);
		} else if(orientation == 3)
		{
			GL11.glTranslated(x + .5, y + .5, z);
			GL11.glRotatef(90, 1, 0, 0);
		} else if(orientation == 4)
		{
			GL11.glTranslated(x + 1, y + .5, z + .5);
			GL11.glRotatef(90, 0, 0, 1);
		} else if(orientation == 5)
		{
			GL11.glTranslated(x, y + .5, z + .5);
			GL11.glRotatef(-90, 0, 0, 1);
		}
	}
	
	@Override
	public final void render(T te, double x, double y, double z, float partialTicks, int destroyStage, float alpha)
	{
		try
		{
			ResourceLocation destroy = null;
			RayTraceResult over = mc.objectMouseOver;
			destroyProgress = 0;
			if(over != null && over.typeOfHit == Type.BLOCK && over.getBlockPos().equals(te.getPos()))
			{
				float progress = destroyProgress = Minecraft.getMinecraft().playerController.curBlockDamageMP;
				if(progress > 0F)
					destroy = DestroyStageTexture.getByProgress(progress);
			}
			
			renderBase(te, null, x, y, z, destroy, alpha);
			renderTileEntityAt(te, x, y, z, partialTicks, destroy, alpha);
		} catch(Throwable err)
		{
			// Failed to render TESR? WE DO CARE ABOUT YOUR LIFE, NO CRASHES!
			err.printStackTrace();
		}
	}
	
	@Override
	public final void renderTileEntityFast(T te, double x, double y, double z, float partialTicks, int destroyStage, float partial, BufferBuilder buffer)
	{
		ResourceLocation destroy = null;
		RayTraceResult over = mc.objectMouseOver;
		if(over != null && over.typeOfHit == Type.BLOCK && over.getBlockPos().equals(te.getPos()))
		{
			float progress = Minecraft.getMinecraft().playerController.curBlockDamageMP;
			if(progress > 0F)
				destroy = DestroyStageTexture.getByProgress(progress);
		}
		renderTileEntityFast(te, x, y, z, partialTicks, destroy, buffer);
	}
	
	/**
	 * Return true for Hammer Core to use NBT rendering instead of TileEntity
	 * rendering.
	 **/
	public boolean canRenderFromNbt()
	{
		return false;
	}
	
	public void renderFromNBT(@Nonnull NBTTagCompound nbt, double x, double y, double z, float partialTicks, @Nullable ResourceLocation destroyStage)
	{
	}
	
	public void renderFromNBTFast(@Nonnull NBTTagCompound nbt, double x, double y, double z, float partialTicks, @Nullable ResourceLocation destroyStage, BufferBuilder buffer)
	{
	}
	
	public void renderTileEntityAt(@Nonnull T te, double x, double y, double z, float partialTicks, @Nullable ResourceLocation destroyStage, float alpha)
	{
		if(canRenderFromNbt())
			renderFromNBT(getNBTFromTile(te), x, y, z, partialTicks, destroyStage);
	}
	
	public void renderTileEntityFast(@Nonnull T te, double x, double y, double z, float partialTicks, @Nullable ResourceLocation destroyStage, BufferBuilder buffer)
	{
		if(canRenderFromNbt())
			renderFromNBTFast(getNBTFromTile(te), x, y, z, partialTicks, destroyStage, buffer);
	}
	
	/**
	 * useful implementation for rendering an item stack if it can handle
	 * rendering from NBT
	 */
	@Override
	public void renderItem(ItemStack item)
	{
		renderBase(null, item, 0, 0, 0, null, 1);
		
		if(canRenderFromNbt())
		{
			NBTTagCompound nbt = getNBTFromItemStack(item);
			if(nbt != null)
				renderFromNBT(nbt, 0D, 0D, 0D, 0F, null);
		}
	}
	
	/**
	 * Shared method that is executed on rendering both: item AND tile
	 */
	public void renderBase(@Nullable T tile, @Nullable ItemStack stack, double x, double y, double z, @Nullable ResourceLocation destroyStage, float alpha)
	{
		
	}
	
	public static NBTTagCompound getNBTFromTile(TileEntity tile)
	{
		if(tile instanceof TileSyncable)
		{
			NBTTagCompound nbt = new NBTTagCompound();
			((TileSyncable) tile).writeNBT(nbt);
			return nbt;
		}
		return tile.serializeNBT();
	}
	
	/**
	 * Used when you Ctrl+Select a block and it stores it's NBT data inside of
	 * an ItemStack
	 * 
	 * @return NBT of an ItemStack, or null if there are no tags
	 */
	@Nullable
	public static NBTTagCompound getNBTFromItemStack(ItemStack stack)
	{
		NBTTagCompound tags = stack.getTagCompound();
		if(tags != null)
			tags = tags.getCompoundTag("BlockEntityTag");
		if(tags != null && !tags.isEmpty())
			return tags.getCompoundTag("Tags");
		return null;
	}
	
	protected int getBrightnessForRB(T te, RenderBlocks rb)
	{
		return te != null ? rb.setLighting(te.getWorld(), te.getPos()) : rb.setLighting(Minecraft.getMinecraft().world, Minecraft.getMinecraft().player.getPosition());
	}
}