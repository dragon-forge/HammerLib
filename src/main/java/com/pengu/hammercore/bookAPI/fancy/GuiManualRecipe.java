package com.pengu.hammercore.bookAPI.fancy;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;

import org.lwjgl.input.Mouse;
import org.lwjgl.opengl.GL11;

import com.pengu.hammercore.bookAPI.fancy.HCFontRenderer.iTooltipContext;
import com.pengu.hammercore.client.texture.gui.theme.GuiTheme;
import com.pengu.hammercore.client.utils.UtilsFX;
import com.pengu.hammercore.common.InterItemStack;
import com.pengu.hammercore.common.utils.InventoryUtils;
import com.pengu.hammercore.utils.ColorHelper;

import net.minecraft.client.Minecraft;
import net.minecraft.client.audio.PositionedSoundRecord;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.renderer.BufferBuilder;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.RenderHelper;
import net.minecraft.client.renderer.RenderItem;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.client.resources.I18n;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.client.util.ITooltipFlag.TooltipFlags;
import net.minecraft.init.SoundEvents;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.FurnaceRecipes;
import net.minecraft.item.crafting.IRecipe;
import net.minecraft.item.crafting.Ingredient;
import net.minecraft.item.crafting.ShapedRecipes;
import net.minecraft.item.crafting.ShapelessRecipes;
import net.minecraft.util.NonNullList;
import net.minecraft.util.math.MathHelper;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import net.minecraftforge.oredict.ShapedOreRecipe;
import net.minecraftforge.oredict.ShapelessOreRecipe;

@SideOnly(value = Side.CLIENT)
public class GuiManualRecipe extends GuiScreen implements iTooltipContext
{
	public static RenderItem itemRenderer = Minecraft.getMinecraft().getRenderItem();
	public static LinkedList<Object[]> history = new LinkedList();
	public int paneWidth = 256;
	public int paneHeight = 181;
	public double guiMapX;
	public double guiMapY;
	public int mouseX = 0;
	public int mouseY = 0;
	public GuiButton button;
	public ManualEntry entry;
	public ManualPage[] pages = null;
	public int page = 0;
	public int maxPages = 0;
	public HCFontRenderer fr = null;
	public static ConcurrentHashMap<Integer, ItemStack> cache = new ConcurrentHashMap();
	String tex1 = "textures/gui/gui_manual_page.png";
	String tex2 = "textures/gui/gui_manual_overlay.png";
	String tex3 = "textures/misc/infuser_symbol.png";
	public Object[] tooltip = null;
	public long lastCycle = 0;
	public ArrayList<List> reference = new ArrayList();
	public int cycle = -1;
	public int mx, my;
	
	public static synchronized void putToCache(int key, ItemStack stack)
	{
		cache.put(key, stack);
	}
	
	public static synchronized ItemStack getFromCache(int key)
	{
		return cache.get(key);
	}
	
	public GuiManualRecipe(ManualEntry research, int page, double x, double y)
	{
		this.entry = research;
		this.guiMapX = x;
		this.guiMapY = y;
		this.mc = Minecraft.getMinecraft();
		this.pages = research.getPages();
		List<ManualPage> p1 = Arrays.asList(this.pages);
		ArrayList<ManualPage> p2 = new ArrayList<ManualPage>();
		for(ManualPage pp : p1)
			p2.add(pp);
		this.pages = p2.toArray(new ManualPage[0]);
		this.maxPages = this.pages.length;
		this.fr = new HCFontRenderer(this.mc.gameSettings, HCFontRenderer.FONT_NORMAL, this.mc.renderEngine, true);
		if(page % 2 == 1)
			--page;
		this.page = page;
	}

	@Override
	public void initGui()
	{
		
	}

	@Override
	protected void keyTyped(char par1, int par2) throws IOException
	{
		if(par2 == this.mc.gameSettings.keyBindInventory.getKeyCode() || par2 == 1)
		{
			history.clear();
			this.mc.displayGuiScreen(new GuiHammerManual(this.guiMapX, this.guiMapY));
		} else
			super.keyTyped(par1, par2);
	}
	
	@Override
	public void drawScreen(int par1, int par2, float par3)
	{
		mx = par1;
		my = par2;
		
		try
		{
			this.drawDefaultBackground();
			GlStateManager.enableBlend();
			this.renderManualPage(par1, par2, par3);
			int sw = (this.width - this.paneWidth) / 2;
			int sh = (this.height - this.paneHeight) / 2;
			if(!history.isEmpty())
			{
				int mx = par1 - (sw + 118);
				int my = par2 - (sh + 189);
				if(mx >= 0 && my >= 0 && mx < 20 && my < 12)
					UtilsFX.drawCustomTooltip(this, itemRenderer, fontRenderer, Arrays.asList(I18n.format("recipe.return")), par1 - 4, par2, 15);
			}
		} catch(Throwable err)
		{
			err.printStackTrace();
		}
	}
	
	protected void renderManualPage(int mouseX, int mouseY, float partialTime)
	{
		Mouse.getDWheel();
		
		sx = 0;
		sy = 0;
		
		int sw = (width - paneWidth) / 2;
		int sh = (height - paneHeight) / 2;
		float var10 = (width - paneWidth * 1.3F) / 2F;
		float var11 = (height - paneHeight * 1.3F) / 2F;
		int rgb = GuiTheme.CURRENT_THEME.bodyColor;
		GL11.glColor4f(ColorHelper.getRed(rgb), ColorHelper.getGreen(rgb), ColorHelper.getBlue(rgb), 1F);
		UtilsFX.bindTexture(this.tex1);
		GL11.glPushMatrix();
		GL11.glTranslatef(var10, var11, 0F);
		GL11.glEnable(3042);
		GL11.glScalef(1.3F, 1.3F, 1);
		drawTexturedModalRect(0, 0, 0, 0, paneWidth, paneHeight);
		GL11.glPopMatrix();
		reference.clear();
		tooltip = null;
		int current = 0;
		
		sx += var10;
		sy += var11;
		
		for(int a = 0; a < pages.length; ++a)
		{
			if((current == page || current == page + 1) && current < maxPages)
				drawPage(pages[a], current % 2, sw, sh, mouseX, mouseY);
			if(++current > page + 1)
				break;
		}
		
		if(tooltip != null)
			UtilsFX.drawCustomTooltip(this, itemRenderer, fontRenderer, (List) this.tooltip[0], (Integer) this.tooltip[1], (Integer) this.tooltip[2], (Integer) this.tooltip[3]);
		
		GL11.glColor4f(1, 1, 1, 1);
		
		GlStateManager.enableBlend();
		
		UtilsFX.bindTexture(tex1);
		int mx1 = mouseX - (sw + 261);
		int my1 = mouseY - (sh + 189);
		int mx2 = mouseX - (sw - 17);
		int my2 = mouseY - (sh + 189);
		float bob = MathHelper.sin((mc.player.ticksExisted / 3F)) * .2F + .1F;
		
		if(!history.isEmpty())
		{
			GL11.glEnable(3042);
			drawTexturedModalRectScaled(sw + 118, sh + 189, 38, 202, 20, 12, bob);
		}
		
		if(page > 0)
		{
			GL11.glEnable(3042);
			drawTexturedModalRectScaled(sw - 16, sh + 190, 0, 184, 12, 8, bob);
		}
		
		if(page < maxPages - 2)
		{
			GL11.glEnable(3042);
			drawTexturedModalRectScaled(sw + 262, sh + 190, 12, 184, 12, 8, bob);
		}
	}
	
	public void drawCustomTooltip(List var4, int par2, int par3, int subTipColor)
	{
		this.tooltip = new Object[] { var4, par2, par3, subTipColor };
	}
	
	public void drawPage(ManualPage pageParm, int side, int x, int y, int mx, int my)
	{
		GL11.glPushAttrib(1048575);
		
		if(lastCycle < System.currentTimeMillis())
		{
			++cycle;
			lastCycle = System.currentTimeMillis() + 1000;
		}
		
		if(page == 0 && side == 0)
		{
			drawTexturedModalRect(x + 4, y - 13, 24, 184, 96, 4);
			drawTexturedModalRect(x + 4, y + 4, 24, 184, 96, 4);
			int offset = fontRenderer.getStringWidth(entry.getName());
			if(offset <= 130)
				fontRenderer.drawString(entry.getName(), x + 52 - offset / 2, y - 6, GuiTheme.CURRENT_THEME.textColor);
			else
			{
				float vv = 130F / offset;
				GL11.glPushMatrix();
				GL11.glTranslatef((float) ((float) (x + 52) - (float) (offset / 2) * vv), (float) ((float) y - 6.0f * vv), (float) 0.0f);
				GL11.glScalef(vv, vv, vv);
				fontRenderer.drawString(entry.getName(), 0, 0, GuiTheme.CURRENT_THEME.textColor);
				GL11.glPopMatrix();
			}
			y += 25;
		}
		
		GlStateManager.enableBlend();
		
		if(pageParm.type == ManualPage.PageType.TEXT || pageParm.type == ManualPage.PageType.TEXT_CONCEALED)
			drawTextPage(side, x, y - 10, pageParm.getTranslatedText());
		else if(pageParm.type == ManualPage.PageType.NORMAL_CRAFTING)
			drawCraftingPage(side, x - 4, y - 8, mx, my, pageParm);
		else if(pageParm.type == ManualPage.PageType.SMELTING)
			drawSmeltingPage(side, x - 4, y - 8, mx, my, pageParm);
		else if(pageParm.type == ManualPage.PageType.COMPOUND_CRAFTING)
			drawCompoundCraftingPage(side, x - 4, y - 8, mx, my, pageParm);
		else if(pageParm.type.getRender() != null)
			pageParm.type.getRender().render(pageParm, side, x - 4, y - 8, mx, my, this);
		GL11.glAlphaFunc(516, .1F);
		GL11.glPopAttrib();
	}
	
	public void drawCompoundCraftingPage(int side, int x, int y, int mx, int my, ManualPage page)
	{
		List r = (List) page.getRecipe();
		if(r != null)
		{
			int j;
			int px;
			int k;
			int py;
			int dx = (Integer) r.get(0);
			int dy = (Integer) r.get(1);
			int dz = (Integer) r.get(2);
			int xoff = 64 - (dx * 16 + dz * 16) / 2;
			int yoff = (-dy) * 25;
			List items = (List) r.get(3);
			GL11.glPushMatrix();
			int start = side * 152;
			String text = I18n.format("recipe.type.construct");
			int offset = fontRenderer.getStringWidth(text);
			fontRenderer.drawString(text, x + start + 56 - offset / 2, y, 5263440);
			int mposx = mx;
			int mposy = my;
			UtilsFX.bindTexture(tex2);
			GL11.glColor4f(1, 1, 1, 1);
			GL11.glDisable(2896);
			GL11.glPushMatrix();
			float sz = 0.0f;
			if(dy > 3)
			{
				sz = (float) (dy - 3) * 0.2f;
				GL11.glTranslatef((float) ((float) (x + start) + (float) xoff * (1.0f + sz)), (float) ((float) (y + 108) + (float) yoff * (1.0f - sz)), (float) 0.0f);
				GL11.glScalef((float) (1.0f - sz), (float) (1.0f - sz), (float) (1.0f - sz));
			} else
				GL11.glTranslatef((float) (x + start + xoff), (float) (y + 108 + yoff), (float) 0.0f);
			GL11.glPushMatrix();
			GL11.glColor4f((float) 1.0f, (float) 1.0f, (float) 1.0f, (float) 0.5f);
			GL11.glEnable(3042);
			GL11.glTranslatef((float) (-8 - xoff), (float) (-119 + Math.max(3 - dx, 3 - dz) * 8 + dx * 4 + dz * 4 + dy * 50), (float) 0.0f);
			GL11.glScalef((float) 2.0f, (float) 2.0f, (float) 1.0f);
			this.drawTexturedModalRect(0, 0, 0, 72, 64, 44);
			GL11.glPopMatrix();
			int count = 0;
			GlStateManager.disableLighting();
			for(j = 0; j < dy; ++j)
			{
				for(k = dz - 1; k >= 0; --k)
				{
					for(int i = dx - 1; i >= 0; --i)
					{
						px = i * 16 + k * 16;
						py = (-i) * 8 + k * 8 + j * 50;
						GL11.glPushMatrix();
						GL11.glColor4f(1.0f, 1.0f, 1.0f, 1.0f);
						RenderHelper.enableGUIStandardItemLighting();
						GL11.glEnable(2884);
						GL11.glTranslatef(0.0f, 0.0f, 60 - j * 10);
						if(items.get(count) != null)
							itemRenderer.renderItemAndEffectIntoGUI(InventoryUtils.cycleItemStack(items.get(count)), px, py);
						RenderHelper.enableGUIStandardItemLighting();
						GL11.glPopMatrix();
						++count;
					}
				}
			}
			GL11.glEnable(2896);
			GL11.glPopMatrix();
			count = 0;
			for(j = 0; j < dy; ++j)
			{
				for(k = dz - 1; k >= 0; --k)
				{
					for(int i = dx - 1; i >= 0; --i)
					{
						px = (int) ((float) (x + start) + (float) xoff * (1.0f + sz) + (float) (i * 16) * (1.0f - sz) + (float) (k * 16) * (1.0f - sz));
						py = (int) ((float) (y + 108) + (float) yoff * (1.0f - sz) - (float) (i * 8) * (1.0f - sz) + (float) (k * 8) * (1.0f - sz) + (float) (j * 50) * (1.0f - sz));
						if(items.get(count) != null && mposx >= px && mposy >= py && (float) mposx < (float) px + 16.0f * (1.0f - sz) && (float) mposy < (float) py + 16.0f * (1.0f - sz))
						{
							ItemStack stack = InventoryUtils.cycleItemStack(items.get(count));
							List addtext = stack.getTooltip(mc.player, mc.gameSettings.advancedItemTooltips ? TooltipFlags.ADVANCED : TooltipFlags.NORMAL);
							Object[] ref = findRecipeReference(stack);
							if(ref != null && (!entry.key.equals(ref[0]) || (side % 2 == 0 && ((Integer) ref[1]) != this.page) || (side % 2 == 1 && ((Integer) ref[1]) != this.page)))
							{
								addtext.add("\u00a78\u00a7o" + I18n.format("recipe.clickthrough"));
								reference.add(Arrays.asList(mx, my, ref[0], ref[1]));
							}
							drawCustomTooltip(addtext, mx, my, 11);
						}
						++count;
					}
				}
			}
			GL11.glPopMatrix();
		}
	}
	
	public void drawCraftingPage(int side, int x, int y, int mx, int my, ManualPage pageParm)
	{
		String text;
		int offset;
		IRecipe recipe = null;
		Object tr = null;
		Object r = pageParm.getRecipe();
		
		if(r instanceof Object[])
		{
			try
			{
				tr = ((Object[]) r)[this.cycle];
			} catch(Exception e)
			{
				this.cycle = 0;
				tr = ((Object[]) r)[this.cycle];
			}
		} else
			tr = r;
		
		if(tr instanceof ShapedRecipes)
			recipe = (ShapedRecipes) tr;
		else if(tr instanceof ShapelessRecipes)
			recipe = (ShapelessRecipes) tr;
		else if(tr instanceof ShapedOreRecipe)
			recipe = (ShapedOreRecipe) tr;
		else if(tr instanceof ShapelessOreRecipe)
			recipe = (ShapelessOreRecipe) tr;
		if(recipe == null)
			return;
		
		GL11.glPushMatrix();
		int start = side * 152;
		GL11.glPushMatrix();
		GL11.glEnable((int) 3042);
		GL11.glTranslatef((float) (x + start), (float) y, (float) 0.0f);
		GL11.glScalef((float) 2.0f, (float) 2.0f, (float) 1.0f);
		GlStateManager.enableAlpha();
		GlStateManager.enableBlend();
		UtilsFX.bindTexture(this.tex2);
		GL11.glColor4f(1, 1, 1, 1);
		
		this.drawTexturedModalRect(2, 32, 60, 15, 52, 52);
		this.drawTexturedModalRect(20, 12, 20, 3, 16, 16);
		
		GL11.glPopMatrix();
		int mposx = mx;
		int mposy = my;
		GL11.glPushMatrix();
		GL11.glTranslated(0.0, 0.0, 100.0);
		GL11.glColor4f(1.0f, 1.0f, 1.0f, 1.0f);
		RenderHelper.enableGUIStandardItemLighting();
		GL11.glEnable(2884);
		itemRenderer.renderItemAndEffectIntoGUI(InventoryUtils.cycleItemStack(recipe.getRecipeOutput()), x + 48 + start, y + 32);
		itemRenderer.renderItemOverlayIntoGUI(mc.fontRenderer, InventoryUtils.cycleItemStack(recipe.getRecipeOutput()), x + 48 + start, y + 32, null);
		RenderHelper.enableGUIStandardItemLighting();
		GL11.glEnable(2896);
		GL11.glPopMatrix();
		if(mposx >= x + 48 + start && mposy >= y + 32 && mposx < x + 48 + start + 16 && mposy < y + 32 + 16)
			this.drawCustomTooltip(InventoryUtils.cycleItemStack(recipe.getRecipeOutput()).getTooltip(mc.player, mc.gameSettings.advancedItemTooltips ? ITooltipFlag.TooltipFlags.ADVANCED : ITooltipFlag.TooltipFlags.NORMAL), mx, my, 11);
		
		NonNullList<Ingredient> items = recipe.getIngredients();
		
		if(recipe != null && (recipe instanceof ShapedRecipes || recipe instanceof ShapedOreRecipe))
		{
			int i;
			int j;
			text = I18n.format("recipe.type.workbench");
			offset = this.fontRenderer.getStringWidth(text);
			this.fontRenderer.drawString(text, x + start + 56 - offset / 2, y, GuiTheme.CURRENT_THEME.textColor);
			int rw = 0;
			int rh = 0;
			
			if(recipe instanceof ShapedRecipes)
			{
				rw = ((ShapedRecipes) recipe).getWidth();
				rh = ((ShapedRecipes) recipe).getHeight();
				items = recipe.getIngredients();
			} else
			{
				rw = ((ShapedOreRecipe) recipe).getWidth();
				rh = ((ShapedOreRecipe) recipe).getHeight();
				items = ((ShapedOreRecipe) recipe).getIngredients();
			}
			for(i = 0; i < rw && i < 3; ++i)
			{
				for(j = 0; j < rh && j < 3; ++j)
				{
					ItemStack stack = InventoryUtils.cycleItemStack(items.get(i + j * rw));
					if(InterItemStack.isStackNull(stack))
						continue;
					GL11.glPushMatrix();
					GL11.glColor4f(1, 1, 1, 1);
					RenderHelper.enableGUIStandardItemLighting();
					GL11.glEnable(2884);
					GL11.glTranslated(0, 0, 100);
					itemRenderer.renderItemAndEffectIntoGUI(stack, x + start + 16 + i * 32, y + 76 + j * 32);
					itemRenderer.renderItemOverlayIntoGUI(mc.fontRenderer, stack.copy().splitStack(1), x + start + 16 + i * 32, y + 76 + j * 32, null);
					RenderHelper.enableGUIStandardItemLighting();
					GL11.glEnable(2896);
					GL11.glPopMatrix();
				}
			}
			
			for(i = 0; i < rw && i < 3; ++i)
			{
				for(j = 0; j < rh && j < 3; ++j)
				{
					ItemStack item = InventoryUtils.cycleItemStack(items.get(i + j * rw));
					
					if(InterItemStack.isStackNull(item) || mposx < x + 16 + start + i * 32 || mposy < y + 76 + j * 32 || mposx >= x + 16 + start + i * 32 + 16 || mposy >= y + 76 + j * 32 + 16)
						continue;
					
					List addtext = item.getTooltip(mc.player, mc.gameSettings.advancedItemTooltips ? TooltipFlags.ADVANCED : TooltipFlags.NORMAL);
					
					Object[] ref = this.findRecipeReference(item);
					
					if(ref != null && (!entry.key.equals(ref[0]) || (side % 2 == 0 && ((Integer) ref[1]) != page) || (side % 2 == 1 && ((Integer) ref[1]) != page)))
					{
						addtext.add("\u00a78\u00a7o" + I18n.format("recipe.clickthrough"));
						reference.add(Arrays.asList(mx, my, ref[0], ref[1]));
					}
					
					drawCustomTooltip(addtext, mx, my, 11);
				}
			}
		}
		
		if(recipe != null && (recipe instanceof ShapelessRecipes || recipe instanceof ShapelessOreRecipe))
		{
			int i;
			text = I18n.format("recipe.type.workbenchshapeless");
			offset = fontRenderer.getStringWidth(text);
			fontRenderer.drawString(text, x + start + 56 - offset / 2, y, GuiTheme.CURRENT_THEME.textColor);
			for(i = 0; i < items.size() && i < 9; ++i)
			{
				if(items.get(i) == null)
					continue;
				GL11.glPushMatrix();
				GL11.glColor4f(1, 1, 1, 1);
				RenderHelper.enableGUIStandardItemLighting();
				GL11.glEnable(2884);
				GL11.glTranslated(0, 0, 100);
				itemRenderer.renderItemAndEffectIntoGUI(InventoryUtils.cycleItemStack(items.get(i)), x + start + 16 + i % 3 * 32, y + 76 + i / 3 * 32);
				itemRenderer.renderItemOverlayIntoGUI(mc.fontRenderer, InventoryUtils.cycleItemStack(items.get(i)).copy().splitStack(1), x + start + 16 + i % 3 * 32, y + 76 + i / 3 * 32, null);
				RenderHelper.enableGUIStandardItemLighting();
				GL11.glEnable(2896);
				GL11.glPopMatrix();
			}
			
			for(i = 0; i < items.size() && i < 9; ++i)
			{
				if(items.get(i) == null || mposx < x + 16 + start + i % 3 * 32 || mposy < y + 76 + i / 3 * 32 || mposx >= x + 16 + start + i % 3 * 32 + 16 || mposy >= y + 76 + i / 3 * 32 + 16)
					continue;
				
				List addtext = InventoryUtils.cycleItemStack(items.get(i)).getTooltip(mc.player, mc.gameSettings.advancedItemTooltips ? TooltipFlags.ADVANCED : TooltipFlags.NORMAL);
				Object[] ref = findRecipeReference(InventoryUtils.cycleItemStack(items.get(i)));
				if(ref != null && (!entry.key.equals(ref[0]) || (side % 2 == 0 && ((Integer) ref[1]) != page) || (side % 2 == 1 && ((Integer) ref[1]) != page)))
				{
					addtext.add("\u00a78\u00a7o" + I18n.format("recipe.clickthrough"));
					reference.add(Arrays.asList(mx, my, ref[0], ref[1]));
				}
				drawCustomTooltip(addtext, mx, my, 11);
			}
		}
		GL11.glPopMatrix();
	}
	
	public void drawSmeltingPage(int side, int x, int y, int mx, int my, ManualPage pageParm)
	{
		ItemStack in = (ItemStack) pageParm.getRecipe();
		ItemStack out = null;
		if(!InterItemStack.isStackNull(in))
			out = FurnaceRecipes.instance().getSmeltingResult(in);
		if(!InterItemStack.isStackNull(out))
		{
			GL11.glPushMatrix();
			int start = side * 152;
			String text = I18n.format("recipe.type.smelting");
			int offset = fontRenderer.getStringWidth(text);
			fontRenderer.drawString(text, x + start + 56 - offset / 2, y, GuiTheme.CURRENT_THEME.textColor);
			UtilsFX.bindTexture(this.tex2);
			GL11.glPushMatrix();
			GL11.glColor4f(1, 1, 1, 1);
			GL11.glEnable(3042);
			GL11.glTranslatef(x + start, y + 28, 0);
			GL11.glScalef(2, 2, 1);
			drawTexturedModalRect(0, 0, 0, 192, 56, 64);
			GL11.glPopMatrix();
			int mposx = mx;
			int mposy = my;
			GL11.glPushMatrix();
			GL11.glTranslated(0, 0, 100);
			GL11.glColor4f(1, 1, 1, 1);
			RenderHelper.enableGUIStandardItemLighting();
			GL11.glEnable(2884);
			itemRenderer.renderItemAndEffectIntoGUI(in, x + 48 + start, y + 64);
			itemRenderer.renderItemOverlayIntoGUI(mc.fontRenderer, in, x + 48 + start, y + 64, null);
			RenderHelper.enableGUIStandardItemLighting();
			GL11.glEnable(2896);
			GL11.glPopMatrix();
			GL11.glPushMatrix();
			GL11.glTranslated(0, 0, 100);
			GL11.glColor4f(1, 1, 1, 1);
			RenderHelper.enableGUIStandardItemLighting();
			GL11.glEnable(2884);
			itemRenderer.renderItemAndEffectIntoGUI(out, x + 48 + start, y + 144);
			itemRenderer.renderItemOverlayIntoGUI(mc.fontRenderer, out, x + 48 + start, y + 144, null);
			RenderHelper.enableGUIStandardItemLighting();
			GL11.glEnable(2896);
			GL11.glPopMatrix();
			if(mposx >= x + 48 + start && mposy >= y + 64 && mposx < x + 48 + start + 16 && mposy < y + 64 + 16)
			{
				List addtext = in.getTooltip(mc.player, mc.gameSettings.advancedItemTooltips ? TooltipFlags.ADVANCED : TooltipFlags.NORMAL);
				Object[] ref = findRecipeReference(in);
				if(ref != null && (!entry.key.equals(ref[0]) || (side % 2 == 0 && ((Integer) ref[1]) != page) || (side % 2 == 1 && ((Integer) ref[1]) != page)))
				{
					addtext.add("\u00a78\u00a7o" + I18n.format("recipe.clickthrough"));
					reference.add(Arrays.asList(mx, my, ref[0], ref[1]));
				}
				drawCustomTooltip(addtext, mx, my, 11);
			}
			if(mposx >= x + 48 + start && mposy >= y + 144 && mposx < x + 48 + start + 16 && mposy < y + 144 + 16)
				drawCustomTooltip(out.getTooltip(mc.player, mc.gameSettings.advancedItemTooltips ? TooltipFlags.ADVANCED : TooltipFlags.NORMAL), mx, my, 11);
			GL11.glPopMatrix();
		}
	}
	
	public void drawTextPage(int side, int x, int y, String text)
	{
		sx += x + 8 + side * 152;
		sy += y + 59;
		
		GL11.glPushMatrix();
		RenderHelper.enableGUIStandardItemLighting();
		GL11.glEnable(3042);
		
		List tip = fr.drawSplitString(text, x - 15 + side * 152, y, 139, GuiTheme.CURRENT_THEME.textColor, this, this);
		
		if(tip != null && !tip.isEmpty())
		{
			LinkedList<String> nl = new LinkedList<>();
			for(Object o : tip)
				if((o + "").startsWith("@") && nl.isEmpty())
					continue;
				else
					nl.add(o + "");
			nl.push("");
			nl.push("");
			
			UtilsFX.drawCustomTooltip(this, itemRenderer, fontRenderer, nl, mx, my, 10);
			
			if((tip.get(0) + "").startsWith("@"))
			{
				String str = (tip.remove(0) + "").substring(1);
				int li = str.lastIndexOf(":");
				
				ManualEntry res = HammerManual.getById(str.substring(0, li));
				
				if(res != null)
				{
					int width = 0;
					for(String s : nl)
						width = Math.max(width, fontRenderer.getStringWidth(s));
					int drawY = my + (nl.size() * fontRenderer.FONT_HEIGHT) / 2 - 8;
					
					if(res.icon_item != null)
					{
						GL11.glPushMatrix();
						GL11.glTranslated(mx + width / 2 - 8, drawY - 20, 370);
						GL11.glEnable(3042);
						GL11.glBlendFunc(770, 771);
						RenderHelper.enableGUIStandardItemLighting();
						GL11.glDisable(2896);
						GL11.glEnable('\u803a');
						GL11.glEnable(2903);
						GL11.glEnable(2896);
						mc.getRenderItem().renderItemAndEffectIntoGUI(InventoryUtils.cycleItemStack(res.icon_item), 0, 0);
						GL11.glDisable(2896);
						GL11.glDepthMask(true);
						GL11.glEnable(2929);
						GL11.glPopMatrix();
						RenderHelper.enableGUIStandardItemLighting();
					} else if(res.icon_resource != null)
					{
						GL11.glPushMatrix();
						GL11.glTranslated(mx + width / 2 - 8, drawY - 20, 370);
						GL11.glEnable(3042);
						GL11.glBlendFunc(770, 771);
						GL11.glColor3f(1, 1, 1);
						mc.renderEngine.bindTexture(res.icon_resource);
						UtilsFX.drawTexturedQuadFull(0, 0, zLevel);
						GL11.glPopMatrix();
					}
					
					reference.add(Arrays.asList(mx, my, res.key, Integer.parseInt(str.substring(li + 1))));
				}
			}
		}
		
		GL11.glPopMatrix();
		
		sx -= x + 8 + side * 152;
		sy -= y + 59;
	}

	@Override
	protected void mouseClicked(int par1, int par2, int par3) throws IOException
	{
		int var4 = (width - paneWidth) / 2;
		int var5 = (height - paneHeight) / 2;
		int mx = par1 - (var4 + 261);
		int my = par2 - (var5 + 189);
		
		if(page < maxPages - 2 && mx >= 0 && my >= 0 && mx < 14 && my < 10)
		{
			this.page += 2;
			this.lastCycle = 0;
			this.cycle = -1;
			mc.getSoundHandler().playSound(PositionedSoundRecord.getMasterRecord(SoundEvents.UI_BUTTON_CLICK, 1));
		}
		
		mx = par1 - (var4 - 17);
		my = par2 - (var5 + 189);
		
		if(page >= 2 && mx >= 0 && my >= 0 && mx < 14 && my < 10)
		{
			page -= 2;
			lastCycle = 0;
			cycle = -1;
			mc.getSoundHandler().playSound(PositionedSoundRecord.getMasterRecord(SoundEvents.UI_BUTTON_CLICK, 1));
		}
		
		if(!history.isEmpty())
		{
			mx = par1 - (var4 + 118);
			my = par2 - (var5 + 189);
			if(mx >= 0 && my >= 0 && mx < 20 && my < 12)
			{
				mc.getSoundHandler().playSound(PositionedSoundRecord.getMasterRecord(SoundEvents.UI_BUTTON_CLICK, 1));
				Object[] o = history.pop();
				this.mc.displayGuiScreen((GuiScreen) new GuiManualRecipe(ManualCategories.getEntry((String) o[0]), (Integer) o[1], this.guiMapX, this.guiMapY));
			}
		}
		
		if(reference.size() > 0)
		{
			for(List coords : this.reference)
			{
				if(par1 < (Integer) coords.get(0) || par2 < (Integer) coords.get(1) || par1 >= (Integer) coords.get(0) + 16 || par2 >= (Integer) coords.get(1) + 16)
					continue;
				mc.getSoundHandler().playSound(PositionedSoundRecord.getMasterRecord(SoundEvents.UI_BUTTON_CLICK, 1));
				history.push(new Object[] { this.entry.key, this.page });
				mc.displayGuiScreen((GuiScreen) new GuiManualRecipe(ManualCategories.getEntry((String) coords.get(2)), (Integer) coords.get(3), this.guiMapX, this.guiMapY));
			}
		}
		
		super.mouseClicked(par1, par2, par3);
	}

	@Override
	public boolean doesGuiPauseGame()
	{
		return false;
	}
	
	public Object[] findRecipeReference(ItemStack item)
	{
		return ManualItemHelper.getCraftingRecipeKey(item);
	}
	
	public void drawTexturedModalRectScaled(int par1, int par2, int par3, int par4, int par5, int par6, float scale)
	{
		GL11.glPushMatrix();
		float var7 = 0.00390625f;
		float var8 = 0.00390625f;
		Tessellator var9 = Tessellator.getInstance();
		GL11.glTranslatef((float) ((float) par1 + (float) par5 / 2.0f), (float) ((float) par2 + (float) par6 / 2.0f), (float) 0.0f);
		GL11.glScalef((float) (1.0f + scale), (float) (1.0f + scale), (float) 1.0f);
		BufferBuilder b = var9.getBuffer();
		b.begin(GL11.GL_QUADS, DefaultVertexFormats.POSITION_TEX);
		b.pos((double) ((float) (-par5) / 2.0f), (double) ((float) par6 / 2.0f), (double) this.zLevel).tex((double) ((float) (par3 + 0) * var7), (double) ((float) (par4 + par6) * var8)).endVertex();
		b.pos((double) ((float) par5 / 2.0f), (double) ((float) par6 / 2.0f), (double) this.zLevel).tex((double) ((float) (par3 + par5) * var7), (double) ((float) (par4 + par6) * var8)).endVertex();
		b.pos((double) ((float) par5 / 2.0f), (double) ((float) (-par6) / 2.0f), (double) this.zLevel).tex((double) ((float) (par3 + par5) * var7), (double) ((float) (par4 + 0) * var8)).endVertex();
		b.pos((double) ((float) (-par5) / 2.0f), (double) ((float) (-par6) / 2.0f), (double) this.zLevel).tex((double) ((float) (par3 + 0) * var7), (double) ((float) (par4 + 0) * var8)).endVertex();
		var9.draw();
		GL11.glPopMatrix();
	}
	
	class C2D
	{
		int x;
		int y;
		
		C2D(int x, int y)
		{
			this.x = x;
			this.y = y;
		}
	}
	
	int sx, sy;
	
	@Override
	public int getStartX()
	{
		return sx;
	}
	
	@Override
	public int getStartY()
	{
		return sy;
	}
	
	@Override
	public int getMouseX()
	{
		return mx;
	}
	
	@Override
	public int getMouseY()
	{
		return my;
	}
	
	public void renderStack(ItemStack stack, int x, int y, int width, int height, int side, boolean addClickTooltip)
	{
		GL11.glPushMatrix();
		GL11.glTranslated(x, y, 0);
		GL11.glScaled(width / 16D, height / 16D, 1);
		GL11.glColor4f(1, 1, 1, 1);
		RenderHelper.enableGUIStandardItemLighting();
		GL11.glEnable(2884);
		itemRenderer.renderItemAndEffectIntoGUI(stack, 0, 0);
		itemRenderer.renderItemOverlayIntoGUI(mc.fontRenderer, stack, 0, 0, null);
		RenderHelper.enableGUIStandardItemLighting();
		GL11.glEnable(2896);
		GL11.glPopMatrix();
		
		if(mx >= x && my >= y && mx < x + width && my < y + height)
		{
			List addtext = stack.getTooltip(mc.player, mc.gameSettings.advancedItemTooltips ? TooltipFlags.ADVANCED : TooltipFlags.NORMAL);
			Object[] ref = findRecipeReference(stack);
			if(addClickTooltip && ref != null && (!entry.key.equals(ref[0]) || (side % 2 == 0 && ((Integer) ref[1]) != page) || (side % 2 == 1 && ((Integer) ref[1]) != page)))
			{
				addtext.add("\u00a78\u00a7o" + I18n.format("recipe.clickthrough"));
				reference.add(Arrays.asList(mx, my, ref[0], ref[1]));
			}
			drawCustomTooltip(addtext, mx, my, 11);
		}
	}
}