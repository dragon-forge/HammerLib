package com.pengu.hammercore.client;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.pengu.hammercore.client.render.item.ItemRenderingHandler;
import com.pengu.hammercore.client.render.item.iItemRender;
import com.pengu.hammercore.client.utils.iEnchantmentColorManager;
import com.pengu.hammercore.common.items.iCustomEnchantColorItem;

import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraftforge.common.util.Constants.NBT;

/**
 * This class was generated 2017-09-20:22:45:55
 * 
 * @author APengu
 */
public class ItemColorHelper
{
	public static final int DEFAULT_GLINT_COLOR = -8372020;
	public static final Map<Item, List<iEnchantmentColorManager>> managers = new HashMap<>();
	
	static ItemStack target;
	
	public static void addManager(iEnchantmentColorManager mgr, Iterable<Item> its)
	{
		for(Item it : its)
			addManager(it, mgr);
	}
	
	public static void addManager(iEnchantmentColorManager mgr, Item... its)
	{
		for(Item it : its)
			addManager(it, mgr);
	}
	
	public static void addManager(Item it, iEnchantmentColorManager mgr)
	{
		List<iEnchantmentColorManager> mgrs = managers.get(it);
		if(mgrs == null)
			managers.put(it, mgrs = new ArrayList<>());
		mgrs.add(mgr);
	}
	
	public static void setTargetStackAndHandleRender(ItemStack stack)
	{
		target = stack;
		
		if(!stack.isEmpty())
		{
			iItemRender render = ItemRenderingHandler.INSTANCE.getRender(stack.getItem());
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
	}
	
	public static int getCustomColor()
	{
		if(!(target.getItem() instanceof iCustomEnchantColorItem))
		{
			try
			{
				Class<?> ColorRunes = Class.forName("vazkii.quark.misc.feature.ColorRunes");
				ColorRunes.getMethod("setTargetStack", ItemStack.class).invoke(null, target);
				int col = (int) ColorRunes.getMethod("getColor").invoke(null, DEFAULT_GLINT_COLOR);
				if(col != DEFAULT_GLINT_COLOR)
					return col;
			} catch(Throwable err)
			{
			}
		}
		
		return getColorFromStack(target);
	}
	
	public static int getColorFromStack(ItemStack stack)
	{
		if(stack.isEmpty())
			return DEFAULT_GLINT_COLOR;
		int retColor = DEFAULT_GLINT_COLOR;
		boolean truncate = true;
		
		if(stack.getItem() instanceof iCustomEnchantColorItem)
		{
			int color = ((iCustomEnchantColorItem) stack.getItem()).getEnchantEffectColor(stack);
			truncate = ((iCustomEnchantColorItem) stack.getItem()).shouldTruncateColorBrightness(stack);
			retColor = 0xFF000000 | color;
		}
		
		List<iEnchantmentColorManager> mgrs = managers.get(stack.getItem());
		if(mgrs != null)
		{
			int s = mgrs.size();
			if(s > 0)
				for(int i = 0; i < s; ++i)
				{
					iEnchantmentColorManager m = mgrs.get(i);
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