package com.zeitheron.hammercore.bookAPI.fancy;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import org.lwjgl.input.Mouse;
import org.lwjgl.opengl.GL11;

import com.zeitheron.hammercore.bookAPI.fancy.ManualEntry.eEntryShape;
import com.zeitheron.hammercore.client.utils.RenderUtil;
import com.zeitheron.hammercore.client.utils.UtilsFX;
import com.zeitheron.hammercore.client.utils.texture.gui.theme.GuiTheme;
import com.zeitheron.hammercore.utils.InventoryUtils;
import com.zeitheron.hammercore.utils.color.ColorHelper;

import net.minecraft.client.Minecraft;
import net.minecraft.client.audio.PositionedSoundRecord;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.renderer.BufferBuilder;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.RenderHelper;
import net.minecraft.client.renderer.RenderItem;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.init.SoundEvents;
import net.minecraft.util.math.MathHelper;

public class GuiHammerManual extends GuiScreen
{
	private static int guiMapTop;
	private static int guiMapLeft;
	private static int guiMapBottom;
	private static int guiMapRight;
	protected int paneWidth = 256;
	protected int paneHeight = 230;
	protected int mouseX = 0;
	protected int mouseY = 0;
	protected double field_74117_m;
	protected double field_74115_n;
	protected double guiMapX;
	protected double guiMapY;
	protected double field_74124_q;
	protected double field_74123_r;
	private int isMouseButtonDown = 0;
	public static int lastX = -5;
	public static int lastY = -6;
	private GuiButton button;
	public final List<ManualEntry> entries = HammerManual.listEntries();
	public static ArrayList highlightedItem = new ArrayList();
	private static String selectedCategory = null;
	private FontRenderer galFontRenderer;
	private ManualEntry currentHighlight = null;
	private String player = "";
	long popuptime = 0L;
	String popupmessage = "";
	
	public GuiHammerManual()
	{
		short var2 = 141;
		short var3 = 141;
		this.field_74117_m = this.guiMapX = this.field_74124_q = lastX * 24 - var2 / 2 - 12;
		this.field_74115_n = this.guiMapY = this.field_74123_r = lastY * 24 - var3 / 2;
		this.player = Minecraft.getMinecraft().player.getName();
		this.updateManual();
		this.galFontRenderer = Minecraft.getMinecraft().standardGalacticFontRenderer;
	}
	
	public GuiHammerManual(double x, double y)
	{
		this.field_74117_m = this.guiMapX = this.field_74124_q = x;
		this.field_74115_n = this.guiMapY = this.field_74123_r = y;
		this.player = Minecraft.getMinecraft().player.getName();
		this.updateManual();
		this.galFontRenderer = Minecraft.getMinecraft().standardGalacticFontRenderer;
	}
	
	public void updateManual()
	{
		if(this.mc == null)
			this.mc = Minecraft.getMinecraft();
		
		this.entries.clear();
		if(selectedCategory == null)
		{
			Set<String> col = ManualCategories.manualCategories.keySet();
			selectedCategory = col.iterator().next();
		}
		
		Collection<ManualEntry> col1 = ManualCategories.getCategory(selectedCategory).entries.values();
		Iterator<ManualEntry> i$ = col1.iterator();
		
		while(i$.hasNext())
		{
			ManualEntry res = i$.next();
			entries.add(res);
		}
		
		guiMapTop = ManualCategories.getCategory(selectedCategory).minDisplayColumn * 24 - 85;
		guiMapLeft = ManualCategories.getCategory(selectedCategory).minDisplayRow * 24 - 112;
		guiMapBottom = ManualCategories.getCategory(selectedCategory).maxDisplayColumn * 24 - 112;
		guiMapRight = ManualCategories.getCategory(selectedCategory).maxDisplayRow * 24 - 61;
	}
	
	@Override
	public void onGuiClosed()
	{
		short var2 = 141;
		short var3 = 141;
		lastX = (int) ((this.guiMapX + var2 / 2D + 12.0D) / 24.0D);
		lastY = (int) ((this.guiMapY + var3 / 2D) / 24.0D);
		super.onGuiClosed();
	}
	
	@Override
	public void initGui()
	{
	}
	
	@Override
	protected void keyTyped(char par1, int par2) throws IOException
	{
		if(par2 == mc.gameSettings.keyBindInventory.getKeyCode())
		{
			highlightedItem.clear();
			mc.displayGuiScreen(null);
			mc.setIngameFocus();
		} else
		{
			if(par2 == 1)
				highlightedItem.clear();
			
			super.keyTyped(par1, par2);
		}
	}
	
	@Override
	public void updateScreen()
	{
		this.field_74117_m = this.guiMapX;
		this.field_74115_n = this.guiMapY;
		double var1 = this.field_74124_q - this.guiMapX;
		double var3 = this.field_74123_r - this.guiMapY;
		if(var1 * var1 + var3 * var3 < 4.0D)
		{
			this.guiMapX += var1;
			this.guiMapY += var3;
		} else
		{
			this.guiMapX += var1 * 0.85D;
			this.guiMapY += var3 * 0.85D;
		}
	}
	
	protected void renderManual(int par1, int par2, float par3)
	{
		float scale = MathHelper.clamp(ManualScale.get(), .3F, 1.5F);
		int sz = (int) (24 * scale);
		
		int rgb = GuiTheme.CURRENT_THEME.bodyColor;
		
		long t = System.nanoTime() / 50000000L;
		int var4 = MathHelper.floor(this.field_74117_m + (this.guiMapX - this.field_74117_m) * par3);
		int var5 = MathHelper.floor(this.field_74115_n + (this.guiMapY - this.field_74115_n) * par3);
		if(var4 < guiMapTop)
			var4 = guiMapTop;
		
		if(var5 < guiMapLeft)
			var5 = guiMapLeft;
		
		if(var4 > guiMapBottom)
			var4 = guiMapBottom;
		
		if(var5 > guiMapRight)
			var5 = guiMapRight;
		
		int var8 = (this.width - this.paneWidth) / 2;
		int var9 = (this.height - this.paneHeight) / 2;
		int var10 = var8 + 16;
		int var11 = var9 + 17;
		
		zLevel = 0.0F;
		GL11.glDepthFunc(518);
		GL11.glPushMatrix();
		GL11.glTranslatef(0.0F, 0.0F, -200.0F);
		GL11.glEnable(3553);
		RenderHelper.enableGUIStandardItemLighting();
		GL11.glDisable(2896);
		GL11.glEnable('\u803a');
		GL11.glEnable(2903);
		GL11.glPushMatrix();
		GL11.glScalef(2.0F, 2.0F, 1.0F);
		int vx = (int) ((float) (var4 - guiMapTop) / (float) Math.abs(guiMapTop - guiMapBottom) * 288.0F);
		int vy = (int) ((float) (var5 - guiMapLeft) / (float) Math.abs(guiMapLeft - guiMapRight) * 316.0F);
		GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
		Minecraft.getMinecraft().renderEngine.bindTexture(ManualCategories.getCategory(selectedCategory).background);
		RenderUtil.drawTexturedModalRect(var10 / 2D, var11 / 2D, vx / 2D, vy / 2D, 112, 98);
		GL11.glScalef(0.5F, 0.5F, 1.0F);
		GL11.glPopMatrix();
		GL11.glEnable(2929);
		GL11.glDepthFunc(515);
		int var24;
		int var26;
		int var27;
		int var42;
		
		GL11.glPushMatrix();
		
		for(int var22 = 0; var22 < entries.size(); ++var22)
		{
			ManualEntry entry = entries.get(var22);
			int var25;
			ManualEntry parent;
			if(entry.parents != null && entry.parents.length > 0)
			{
				for(var42 = 0; var42 < entry.parents.length; ++var42)
				{
					if(entry.parents[var42] != null && ManualCategories.getEntry(entry.parents[var42]).category.equals(selectedCategory))
					{
						parent = ManualCategories.getEntry(entry.parents[var42]);
						
						var24 = entry.displayColumn * sz - var4 + sz / 2 + var10;
						var25 = entry.displayRow * sz - var5 + sz / 2 + var11;
						var26 = parent.displayColumn * sz - var4 + sz / 2 + var10;
						var27 = parent.displayRow * sz - var5 + sz / 2 + var11;
						boolean var30 = Math.sin(Minecraft.getSystemTime() % 600L / 600.0D * 3.141592653589793D * 2.0D) > 0.6D ? true : true;
						
						int altcol = entry.getColor();
						drawLine(var24, var25, var26, var27, 0.1F, 0.1F, 0.1F, ColorHelper.getRed(altcol), ColorHelper.getGreen(altcol), ColorHelper.getBlue(altcol), par3, false, 3);
					}
				}
			}
			
			if(entry.siblings != null && entry.siblings.length > 0)
			{
				for(var42 = 0; var42 < entry.siblings.length; ++var42)
				{
					if(entry.siblings[var42] != null && ManualCategories.getEntry(entry.siblings[var42]).category.equals(selectedCategory))
					{
						parent = ManualCategories.getEntry(entry.siblings[var42]);
						if(parent.parents == null || parent.parents != null && !Arrays.asList(parent.parents).contains(entry.key))
						{
							var24 = entry.displayColumn * sz - var4 + sz / 2 + var10;
							var25 = entry.displayRow * sz - var5 + sz / 2 + var11;
							var26 = parent.displayColumn * sz - var4 + sz / 2 + var10;
							var27 = parent.displayRow * sz - var5 + sz / 2 + var11;
							
							int altcol = entry.getColor();
							drawLine(var24, var25, var26, var27, 0.1F, 0.1F, 0.2F, ColorHelper.getRed(altcol), ColorHelper.getGreen(altcol), ColorHelper.getBlue(altcol), par3, false, 3);
						}
					}
				}
			}
		}
		
		currentHighlight = null;
		RenderItem var43 = Minecraft.getMinecraft().getRenderItem();
		GL11.glEnable('\u803a');
		GL11.glEnable(2903);
		boolean renderWithColor = true;
		int var44;
		
		for(var24 = 0; var24 < this.entries.size(); ++var24)
		{
			ManualEntry var45 = this.entries.get(var24);
			var26 = var45.displayColumn * sz - var4;
			var27 = var45.displayRow * sz - var5;
			
			if(var26 >= -24 * sz && var27 >= -24 * sz && var26 <= 224 && var27 <= 196)
			{
				var42 = var10 + var26;
				var44 = var11 + var27;
				float var47;
				
				int col = var45.getColor();
				if(col == 0xFFFFFF)
					col = rgb;
				
				float r = ColorHelper.getRed(col), g = ColorHelper.getGreen(col), b = ColorHelper.getBlue(col);
				
				GL11.glColor4f(r, g, b, 1F);
				
				UtilsFX.bindTexture("textures/gui/gui_manual.png");
				GL11.glEnable(2884);
				GL11.glEnable(3042);
				GL11.glBlendFunc(770, 771);
				
				GL11.glPushMatrix();
				
				GL11.glTranslated(var42 - 1.5, var44 - 1.5, .5);
				GL11.glScaled(scale, scale, 1);
				GL11.glTranslated(-.5, -.5, -.5);
				
				eEntryShape shape = var45.getShape();
				
				GL11.glColor4f(r, g, b, 1F);
				if(shape == eEntryShape.ROUND)
					this.drawTexturedModalRect(0, 0, 54, 230, 26, 26);
				else if(shape == eEntryShape.HEX)
					this.drawTexturedModalRect(0, 0, 80, 230, 26, 26);
				else
					this.drawTexturedModalRect(0, 0, 0, 230, 26, 26);
				
				if(var45.isSpecial())
					this.drawTexturedModalRect(0, 0, 26, 230, 26, 26);
				
				GL11.glColor4f(1, 1, 1, 1);
				
				GL11.glPopMatrix();
				
				GL11.glDisable(3042);
				if(highlightedItem.contains(var45.key))
				{
					GL11.glPushMatrix();
					GL11.glEnable(3042);
					GL11.glBlendFunc(770, 771);
					GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
					this.mc.renderEngine.bindTexture(com.zeitheron.hammercore.client.utils.UtilsFX.getMCParticleTexture());
					int var48 = (int) (t % 16L) * 16;
					GL11.glTranslatef(var42 - 5, var44 - 5, 0.0F);
					RenderUtil.drawTexturedModalRect(0, 0, var48, 80, 16, 16, 0.0D);
					GL11.glDisable(3042);
					GL11.glPopMatrix();
				}
				
				if(var45.icon_item != null)
				{
					GL11.glPushMatrix();
					
					GL11.glTranslated(var42 - 1.5, var44 - 1.5, .5);
					GL11.glScaled(scale, scale, 1);
					GL11.glTranslated(-.5, -.5, -.5);
					GL11.glTranslated(5, 5, 0);
					
					GL11.glEnable(3042);
					GL11.glBlendFunc(770, 771);
					RenderHelper.enableGUIStandardItemLighting();
					GL11.glDisable(2896);
					GL11.glEnable('\u803a');
					GL11.glEnable(2903);
					GL11.glEnable(2896);
					var43.renderItemAndEffectIntoGUI(InventoryUtils.cycleItemStack(var45.icon_item), 0, 0);
					GL11.glDisable(2896);
					GL11.glDepthMask(true);
					GL11.glEnable(2929);
					GL11.glDisable(3042);
					GL11.glPopMatrix();
				} else if(var45.icon_resource != null)
				{
					GL11.glPushMatrix();
					
					GL11.glTranslated(var42 - 1.5, var44 - 1.5, .5);
					GL11.glScaled(scale, scale, 1);
					GL11.glTranslated(-.5, -.5, -.5);
					GL11.glTranslated(5, 5, 0);
					
					GL11.glEnable(3042);
					GL11.glBlendFunc(770, 771);
					mc.renderEngine.bindTexture(var45.icon_resource);
					if(!renderWithColor)
						GL11.glColor4f(0.2F, 0.2F, 0.2F, 1.0F);
					UtilsFX.drawTexturedQuadFull(0, 0, this.zLevel);
					GL11.glPopMatrix();
				}
				
				if(par1 >= var10 && par2 >= var11 && par1 < var10 + 224 && par2 < var11 + 196 && par1 >= var42 && par1 <= var42 + sz && par2 >= var44 && par2 <= var44 + sz)
					this.currentHighlight = var45;
				
				GlStateManager.color(1, 1, 1, 1);
				RenderHelper.enableGUIStandardItemLighting();
			}
		}
		
		GL11.glPopMatrix();
		GL11.glDisable(2929);
		GL11.glEnable(3042);
		GL11.glBlendFunc(770, 771);
		GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
		Set var46 = ManualCategories.manualCategories.keySet();
		int var49 = 0;
		boolean var50 = false;
		Iterator var34 = var46.iterator();
		
		int warp;
		while(var34.hasNext())
		{
			Object var99 = var34.next();
			ManualCategory fr = ManualCategories.getCategory((String) var99);
			
			if(fr.isVisibleTo(mc.player))
			{
				GL11.glPushMatrix();
				if(var49 == 9)
				{
					var49 = 0;
					var50 = true;
				}
				
				int var39 = !var50 ? 0 : 264;
				byte primary = 0;
				warp = var50 ? 14 : 0;
				if(!selectedCategory.equals(var99))
				{
					primary = 24;
					warp = var50 ? 6 : 8;
				}
				
				UtilsFX.bindTexture("textures/gui/gui_manual.png");
				
				GL11.glColor4f(ColorHelper.getRed(rgb), ColorHelper.getGreen(rgb), ColorHelper.getBlue(rgb), 1F);
				if(var50)
					this.drawTexturedModalRectReversed(var8 + var39 - 8, var9 + 2 + var49 * 24, 176 + primary, 232, 24, 24);
				else
					this.drawTexturedModalRect(var8 - 24 + var39, var9 + 2 + var49 * 24, 152 + primary, 232, 24, 24);
				
				if(highlightedItem.contains(var99))
				{
					GL11.glPushMatrix();
					this.mc.renderEngine.bindTexture(com.zeitheron.hammercore.client.utils.UtilsFX.getMCParticleTexture());
					GL11.glColor4f(1, 1, 1, 1);
					int ws = (int) (16L * (t % 16L));
					RenderUtil.drawTexturedModalRect(var8 - 27 + warp + var39, var9 - 2 + var49 * 24, ws, 80, 16, 16, -90.0D);
					GL11.glPopMatrix();
				}
				
				GL11.glPushMatrix();
				this.mc.renderEngine.bindTexture(fr.icon);
				GL11.glColor4f(1, 1, 1, 1);
				UtilsFX.drawTexturedQuadFull(var8 - 19 + warp + var39, var9 + 6 + var49 * 24, -80.0D);
				GL11.glPopMatrix();
				
				if(!selectedCategory.equals(var99))
				{
					UtilsFX.bindTexture("textures/gui/gui_manual.png");
					GL11.glColor4f(ColorHelper.getRed(rgb), ColorHelper.getGreen(rgb), ColorHelper.getBlue(rgb), 1F);
					if(var50)
						drawTexturedModalRectReversed(var8 + var39 - 8, var9 + 2 + var49 * 24, 224, 232, 24, 24);
					else
						drawTexturedModalRect(var8 - 24 + var39, var9 + 2 + var49 * 24, 200, 232, 24, 24);
				}
				
				GL11.glPopMatrix();
				++var49;
			}
		}
		
		UtilsFX.bindTexture("textures/gui/gui_manual.png");
		GL11.glColor4f(ColorHelper.getRed(rgb), ColorHelper.getGreen(rgb), ColorHelper.getBlue(rgb), 1F);
		drawTexturedModalRect(var8, var9, 0, 0, this.paneWidth, this.paneHeight);
		GL11.glPopMatrix();
		this.zLevel = 0.0F;
		GL11.glDepthFunc(515);
		GL11.glDisable(2929);
		GL11.glEnable(3553);
		
		super.drawScreen(par1, par2, par3);
		
		if(this.currentHighlight != null)
		{
			String var51 = this.currentHighlight.getName();
			var26 = par1 + 6;
			var27 = par2 - 4;
			int var52 = 0;
			FontRenderer var53 = this.fontRenderer;
			
			var42 = (int) Math.max(var53.getStringWidth(var51), var53.getStringWidth(this.currentHighlight.getText()) / 1.9F);
			var44 = var53.getWordWrappedHeight(var51, var42) + 5;
			
			this.drawGradientRect(var26 - 3, var27 - 3, var26 + var42 + 3, var27 + var44 + 6 + var52, -1073741824, -1073741824);
			GL11.glPushMatrix();
			GL11.glTranslatef(var26, var27 + var44 - 1, 0.0F);
			GL11.glScalef(0.5F, 0.5F, 0.5F);
			this.fontRenderer.drawStringWithShadow(this.currentHighlight.getText(), 0, 0, -7302913);
			GL11.glPopMatrix();
			
			var53.drawStringWithShadow(var51, var26, var27, this.currentHighlight.isSpecial() ? -128 : -1);
		}
		
		GL11.glEnable(2929);
		GL11.glEnable(2896);
		RenderHelper.disableStandardItemLighting();
	}
	
	@Override
	public boolean doesGuiPauseGame()
	{
		return false;
	}
	
	@Override
	protected void mouseClicked(int par1, int par2, int par3) throws IOException
	{
		this.popuptime = System.currentTimeMillis() - 1L;
		int count;
		if(this.currentHighlight != null)
		{
			mc.getSoundHandler().playSound(PositionedSoundRecord.getMasterRecord(SoundEvents.UI_BUTTON_CLICK, 1));
			this.mc.displayGuiScreen(new GuiManualRecipe(this.currentHighlight, 0, this.guiMapX, this.guiMapY));
		} else
		{
			int var4 = (this.width - this.paneWidth) / 2;
			int var5 = (this.height - this.paneHeight) / 2 + 2;
			Set cats = ManualCategories.manualCategories.keySet();
			count = 0;
			boolean swop = false;
			Iterator i$ = cats.iterator();
			
			while(i$.hasNext())
			{
				Object obj = i$.next();
				ManualCategory rcl = ManualCategories.getCategory((String) obj);
				if(rcl.isVisibleTo(mc.player))
				{
					if(count == 9)
					{
						count = 0;
						swop = true;
					}
					
					int mposx = par1 - (var4 - 24 + (swop ? 280 : 0));
					int mposy = par2 - (var5 + count * 24);
					if(mposx >= 0 && mposx < 24 && mposy >= 0 && mposy < 24)
					{
						selectedCategory = (String) obj;
						this.updateManual();
						this.playButtonClick();
						ManualScale.set(1);
						break;
					}
					
					++count;
				}
			}
		}
		
		super.mouseClicked(par1, par2, par3);
	}
	
	public void drawTexturedModalRectReversed(int par1, int par2, int par3, int par4, int par5, int par6)
	{
		float f = 0.00390625F;
		float f1 = 0.00390625F;
		Tessellator tessellator = Tessellator.getInstance();
		BufferBuilder b = tessellator.getBuffer();
		b.begin(GL11.GL_QUADS, DefaultVertexFormats.POSITION_TEX);
		b.pos(par1 + 0, par2 + par6, this.zLevel).tex((par3 + 0) * f, (par4 + par6) * f1).endVertex();
		b.pos(par1 + par5, par2 + par6, this.zLevel).tex((par3 - par5) * f, (par4 + par6) * f1).endVertex();
		b.pos(par1 + par5, par2 + 0, this.zLevel).tex((par3 - par5) * f, (par4 + 0) * f1).endVertex();
		b.pos(par1 + 0, par2 + 0, this.zLevel).tex((par3 + 0) * f, (par4 + 0) * f1).endVertex();
		tessellator.draw();
	}
	
	private void playButtonClick()
	{
		mc.getSoundHandler().playSound(PositionedSoundRecord.getMasterRecord(SoundEvents.UI_BUTTON_CLICK, 1));
	}
	
	private void drawLine(int x, int y, int x2, int y2, float r, float g, float b, float rLn, float gLn, float bLn, float te, boolean wiggle, int maxAlts)
	{
		float count = Minecraft.getMinecraft().player.ticksExisted + te;
		double milli = System.currentTimeMillis() + te * 50L;
		Tessellator var12 = Tessellator.getInstance();
		GL11.glPushMatrix();
		GL11.glAlphaFunc(516, 0.003921569F);
		GL11.glDisable(3553);
		GL11.glEnable(3042);
		GL11.glBlendFunc(770, 771);
		double d3 = x - x2;
		double d4 = y - y2;
		float dist = MathHelper.sqrt(d3 * d3 + d4 * d4);
		int inc = (int) (dist / 2F);
		float dx = (float) (d3 / inc);
		float dy = (float) (d4 / inc);
		
		if(Math.abs(d3) > Math.abs(d4))
			dx *= 2.0F;
		else
			dy *= 2.0F;
		
		GL11.glLineWidth(3);
		GL11.glEnable(2848);
		GL11.glHint(3154, 4354);
		BufferBuilder bb = var12.getBuffer();
		bb.begin(3, DefaultVertexFormats.POSITION_COLOR);
		
		double sine = Math.sin(milli % 10000L / 1000D) + 1;
		
		for(int a = 0; a <= inc; ++a)
		{
			int leftover = (int) (a + count * 1.1F) % (inc / maxAlts);
			
			leftover -= Math.abs(sine);
			
			boolean alt = maxAlts > 0 && inc != 0 && Math.abs(leftover) < inc / maxAlts / 4;
			
			float r2 = r;
			float g2 = g;
			float b2 = b;
			float mx = 0.0F;
			float my = 0.0F;
			float op = 0.6F;
			
			float scale = ManualScale.get();
			
			if(wiggle)
			{
				float phase = (float) a / (float) inc;
				mx = MathHelper.sin((count + a) / 7.0F) * 5.0F * scale * (1.0F - phase);
				my = MathHelper.sin((count + a) / 5.0F) * 5.0F * scale * (1.0F - phase);
				r2 = r * (1.0F - phase);
				g2 = g * (1.0F - phase);
				b2 = b * (1.0F - phase);
				op *= phase;
			}
			
			float x0 = x - dx * a + mx;
			float y0 = y - dy * a + my;
			
			float r3 = alt ? rLn : r2;
			float g3 = alt ? gLn : g2;
			float b3 = alt ? bLn : b2;
			
			bb.pos(x0, y0, 0.0D).color(r3, g3, b3, op).endVertex();
			
			if(Math.abs(d3) > Math.abs(d4))
				dx *= 1.0F - 1.0F / (inc * 3.0F / 2.0F);
			else
				dy *= 1.0F - 1.0F / (inc * 3.0F / 2.0F);
		}
		
		var12.draw();
		GL11.glBlendFunc(770, 771);
		GL11.glDisable(2848);
		GL11.glDisable(3042);
		GL11.glDisable('\u803a');
		GL11.glEnable(3553);
		GL11.glAlphaFunc(516, 0.1F);
		GL11.glPopMatrix();
	}
	
	@Override
	public void drawScreen(int mx, int my, float par3)
	{
		float scale = MathHelper.clamp(ManualScale.get(), .3F, 1.5F);
		
		int var4 = (this.width - this.paneWidth) / 2;
		int var5 = (this.height - this.paneHeight) / 2;
		
		if(Mouse.isButtonDown(0))
		{
			int var6 = var4 + 8;
			int var7 = var5 + 17;
			if((this.isMouseButtonDown == 0 || this.isMouseButtonDown == 1) && mx >= var6 && mx < var6 + 224 && my >= var7 && my < var7 + 196)
			{
				if(this.isMouseButtonDown == 0)
					this.isMouseButtonDown = 1;
				else
				{
					this.guiMapX -= (double) (mx - this.mouseX) * scale;
					this.guiMapY -= (double) (my - this.mouseY) * scale;
					this.field_74124_q = this.field_74117_m = this.guiMapX;
					this.field_74123_r = this.field_74115_n = this.guiMapY;
				}
				this.mouseX = mx;
				this.mouseY = my;
			}
			if(this.field_74124_q < guiMapTop)
				this.field_74124_q = guiMapTop;
			if(this.field_74123_r < guiMapLeft)
				this.field_74123_r = guiMapLeft;
			if(this.field_74124_q > guiMapBottom)
				this.field_74124_q = guiMapBottom;
			if(this.field_74123_r > guiMapRight)
				this.field_74123_r = guiMapRight;
		} else
			this.isMouseButtonDown = 0;
		
		int dw = Mouse.getDWheel();
		
		if(dw != 0)
		{
			float ns = scale + dw / 720F;
			ManualScale.set(MathHelper.clamp(ns, .3F, 1.5F));
		}
		
		drawDefaultBackground();
		GlStateManager.enableBlend();
		
		renderManual(mx, my, par3);
		
		if(this.popuptime > System.currentTimeMillis())
		{
			int xq = var4 + 128;
			int yq = var5 + 128;
			int var41 = this.fontRenderer.getWordWrappedHeight(this.popupmessage, 150) / 2;
			this.drawGradientRect(xq - 78, yq - var41 - 3, xq + 78, yq + var41 + 3, -1073741824, -1073741824);
			this.fontRenderer.drawSplitString(this.popupmessage, xq - 75, yq - var41, 150, -7302913);
		}
		
		Set<String> cats = ManualCategories.manualCategories.keySet();
		int count = 0;
		boolean swop = false;
		for(String obj : cats)
		{
			if(count == 9)
			{
				count = 0;
				swop = true;
			}
			ManualCategory rcl = ManualCategories.getCategory(obj);
			if(!rcl.isVisibleTo(mc.player))
				continue;
			int mposx = mx - (var4 - 24 + (swop ? 280 : 0));
			int mposy = my - ((var5 + 2) + count * 24);
			if(mposx >= 0 && mposx < 24 && mposy >= 0 && mposy < 24)
				UtilsFX.drawCustomTooltip(this, itemRender, fontRenderer, Arrays.asList(ManualCategories.getCategoryName(obj)), mx, my - 16 < 0 ? (int) (my - (my - 16)) : my, 15);
			++count;
		}
		
		String cat = ManualCategories.getCategoryName(selectedCategory);
		fontRenderer.drawStringWithShadow(cat, var4 + (paneWidth - fontRenderer.getStringWidth(cat)) / 2, var5 + 5, GuiTheme.CURRENT_THEME.textColor);
	}
}