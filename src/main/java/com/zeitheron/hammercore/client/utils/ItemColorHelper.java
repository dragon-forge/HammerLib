package com.zeitheron.hammercore.client.utils;

import com.zeitheron.hammercore.api.items.IRenderAwareItem;
import com.zeitheron.hammercore.client.render.item.IItemRender;
import com.zeitheron.hammercore.client.render.item.ItemRenderingHandler;
import com.zeitheron.hammercore.client.render.shader.GlShaderStack;
import com.zeitheron.hammercore.internal.items.ICustomEnchantColorItem;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.block.model.IBakedModel;
import net.minecraft.client.renderer.block.model.ItemCameraTransforms;
import net.minecraft.client.renderer.block.model.ItemCameraTransforms.TransformType;
import net.minecraft.client.renderer.texture.TextureManager;
import net.minecraft.client.renderer.texture.TextureMap;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraftforge.common.util.Constants.NBT;

import java.util.*;

public class ItemColorHelper
{
	public static final int DEFAULT_GLINT_COLOR = 0xFF8040CC;
	public static final Map<Item, List<IEnchantmentColorManager>> managers = new HashMap<>();
	
	static ItemStack target;
	
	public static void addManager(IEnchantmentColorManager mgr, Iterable<Item> its)
	{
		for(Item it : its)
			addManager(it, mgr);
	}
	
	public static void addManager(IEnchantmentColorManager mgr, Item... its)
	{
		for(Item it : its)
			addManager(it, mgr);
	}
	
	public static void addManager(Item it, IEnchantmentColorManager mgr)
	{
		List<IEnchantmentColorManager> mgrs = managers.get(it);
		if(mgrs == null)
			managers.put(it, mgrs = new ArrayList<>());
		mgrs.add(mgr);
	}
	
	public static void renderItemModelIntoGUIPre(ItemStack stack, int x, int y, IBakedModel bakedmodel)
	{
		try(GlShaderStack.IShaderStack glsl = GlShaderStack.glsPushShader())
		{
			IRenderAwareItem bruh;
			if((bruh = IRenderAwareItem.get(stack)) != null) bruh.preRenderInGUI(stack, x, y, bakedmodel);
			
			TextureManager txmgr = Minecraft.getMinecraft().getTextureManager();
			GlStateManager.pushMatrix();
			txmgr.bindTexture(TextureMap.LOCATION_BLOCKS_TEXTURE);
			txmgr.getTexture(TextureMap.LOCATION_BLOCKS_TEXTURE).setBlurMipmap(false, false);
			GlStateManager.enableRescaleNormal();
			GlStateManager.enableAlpha();
			GlStateManager.alphaFunc(516, 0.1F);
			GlStateManager.enableBlend();
			GlStateManager.blendFunc(GlStateManager.SourceFactor.SRC_ALPHA, GlStateManager.DestFactor.ONE_MINUS_SRC_ALPHA);
			GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);
			GlStateManager.translate(x, y, 450);
			
			if(!stack.isEmpty())
				for(IItemRender render : ItemRenderingHandler.INSTANCE.getRenderHooks(stack.getItem()))
					if(render != null)
					{
						GlStateManager.pushMatrix();
						GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);
						render.renderItem(stack, bakedmodel, TransformType.GUI);
						GlStateManager.popMatrix();
					}
			
			GlStateManager.disableAlpha();
			GlStateManager.disableLighting();
			GlStateManager.popMatrix();
			txmgr.bindTexture(TextureMap.LOCATION_BLOCKS_TEXTURE);
			txmgr.getTexture(TextureMap.LOCATION_BLOCKS_TEXTURE).restoreLastBlurMipmap();
		}
	}
	
	public static void renderItemModelIntoGUIPost(ItemStack stack, int x, int y, IBakedModel bakedmodel)
	{
		IRenderAwareItem bruh;
		if((bruh = IRenderAwareItem.get(stack)) != null) bruh.postRenderInGUI(stack, x, y, bakedmodel);
		GlShaderStack.glsPopShader();
	}
	
	public static void renderItemModel(ItemStack stack, IBakedModel bakedmodel, ItemCameraTransforms.TransformType transform)
	{
		if(!stack.isEmpty())
			for(IItemRender render : ItemRenderingHandler.INSTANCE.getRenderHooks(stack.getItem()))
				if(render != null)
				{
					GlStateManager.pushMatrix();
					GlStateManager.translate(-0.5F, -0.5F, -0.5F);
					GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);
					GlStateManager.enableRescaleNormal();
					render.renderItem(stack, bakedmodel, transform);
					GlStateManager.popMatrix();
				}
	}
	
	public static void setTargetStackAndHandleRender(ItemStack stack)
	{
		target = stack;
		
		if(!stack.isEmpty())
			for(IItemRender render : ItemRenderingHandler.INSTANCE.getRenderHooks(stack.getItem()))
				if(render != null)
				{
					GlStateManager.pushMatrix();
					GlStateManager.translate(-0.5F, -0.5F, -0.5F);
					GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);
					GlStateManager.enableRescaleNormal();
					render.renderItem(stack);
					GlStateManager.popMatrix();
				}
	}
	
	public static int getCustomColor(int prev)
	{
		if(!(target.getItem() instanceof ICustomEnchantColorItem))
		{
			try
			{
				Class<?> ColorRunes = Class.forName("vazkii.quark.misc.feature.ColorRunes");
				ColorRunes.getMethod("setTargetStack", ItemStack.class).invoke(null, target);
				int col = (int) ColorRunes.getDeclaredMethod("getColor", int.class).invoke(null, DEFAULT_GLINT_COLOR);
				if(col != DEFAULT_GLINT_COLOR)
					return col;
			} catch(Throwable err)
			{
			}
		}
		
		return getColorFromStack(target, prev);
	}
	
	public static int getColorFromStack(ItemStack stack, int prev)
	{
		if(stack.isEmpty())
			return prev;
		int retColor = prev;
		boolean truncate = true;
		
		if(stack.getItem() instanceof ICustomEnchantColorItem)
		{
			int color = ((ICustomEnchantColorItem) stack.getItem()).getEnchantEffectColor(stack);
			truncate = ((ICustomEnchantColorItem) stack.getItem()).shouldTruncateColorBrightness(stack);
			retColor = 0xFF000000 | color;
		}
		
		List<IEnchantmentColorManager> mgrs = managers.get(stack.getItem());
		if(mgrs != null)
		{
			int s = mgrs.size();
			if(s > 0)
				for(int i = 0; i < s; ++i)
				{
					IEnchantmentColorManager m = mgrs.get(i);
					if(m != null && m.applies(stack))
						retColor = m.apply(stack, retColor);
				}
		}
		
		if(stack.hasTagCompound())
		{
			NBTTagCompound nbt = stack.getTagCompound();
			if(nbt.hasKey("HCCustomEnch", NBT.TAG_STRING))
				try
				{
					retColor = Integer.parseInt(nbt.getString("HCCustomEnch"), 16);
				} catch(Throwable err)
				{
				}
		}
		
		if(truncate)
		{
			int r = retColor >> 16 & 0xFF;
			int g = retColor >> 8 & 0xFF;
			int b = retColor & 0xFF;
			int t = r + g + b;
			if(t > 396)
			{
				float mul = 396.0F / t;
				r = (int) (r * mul);
				g = (int) (g * mul);
				b = (int) (b * mul);
				retColor = -16777216 + (r << 16) + (g << 8) + b;
			}
		}
		
		return retColor;
	}
}