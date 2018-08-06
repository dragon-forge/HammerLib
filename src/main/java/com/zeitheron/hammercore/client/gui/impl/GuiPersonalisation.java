package com.zeitheron.hammercore.client.gui.impl;

import java.io.IOException;
import java.util.List;

import com.zeitheron.hammercore.client.HCClientOptions;
import com.zeitheron.hammercore.client.utils.texture.gui.DynGuiTex;
import com.zeitheron.hammercore.client.utils.texture.gui.GuiTexBakery;
import com.zeitheron.hammercore.client.utils.texture.gui.theme.GuiTheme;

import net.minecraft.client.audio.PositionedSoundRecord;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.renderer.RenderHelper;
import net.minecraft.init.SoundEvents;

/**
 * An internal class.
 */
public class GuiPersonalisation extends GuiScreen
{
	public int page;
	
	public DynGuiTex tex;
	
	@Override
	public void initGui()
	{
		GuiTexBakery b = GuiTexBakery.start();
		b.body(0, 0, 176, 206);
		b.body(-70, 10, 64, 20);
		b.body(-70, 32, 64, 20);
		tex = b.bake();
		super.initGui();
	}
	
	@Override
	public void drawScreen(int mouseX, int mouseY, float partialTicks)
	{
		drawDefaultBackground();
		
		int xSize = 176;
		int ySize = 206;
		
		int guiLeft = (width - xSize) / 2;
		int guiTop = (height - ySize) / 2;
		
		tex.elements.get(2).render(guiLeft, guiTop);
		
		List<GuiTheme> ts = GuiTheme.THEMES;
		int is = page * 5;
		int tp = ts.size() / 5;
		
		for(int i = is; i < Math.min(is + 5, ts.size()); ++i)
		{
			int j = i - is;
			
			GuiTheme th = ts.get(i % ts.size());
			
			GuiTexBakery b = GuiTexBakery.start();
			b.body(0, 0, 32, 32);
			b.slot(7, 7);
			DynGuiTex tex = b.bake();
			tex.theme = th;
			tex.render(guiLeft + 8, guiTop + 7 + j * 40);
			
			fontRenderer.drawString(th.name, guiLeft + 45, guiTop + 17 + j * 40, th.textShadeColor, false);
			fontRenderer.drawString(th.name, guiLeft + 44, guiTop + 16 + j * 40, th.textColor, false);
			
			if(tex.elements.get(0).intersects(mouseX - guiLeft - 8, mouseY - guiTop - 7 - j * 40, 1, 1))
			{
				drawHoveringText("Select '" + th.name + "'", mouseX, mouseY);
				RenderHelper.disableStandardItemLighting();
			}
		}
		
		if(page < tp - 1)
		{
			tex.elements.get(1).render(guiLeft, guiTop);
			fontRenderer.drawString("Next", guiLeft - 63 + 12, guiTop + 17, GuiTheme.CURRENT_THEME.getColor(8), false);
			fontRenderer.drawString("Next", guiLeft - 64 + 12, guiTop + 16, GuiTheme.CURRENT_THEME.getColor(7), false);
		}
		
		if(page > 0)
		{
			tex.elements.get(0).render(guiLeft, guiTop);
			fontRenderer.drawString("Back", guiLeft - 63 + 12, guiTop + 17 + 22, GuiTheme.CURRENT_THEME.getColor(8), false);
			fontRenderer.drawString("Back", guiLeft - 64 + 12, guiTop + 16 + 22, GuiTheme.CURRENT_THEME.getColor(7), false);
		}
		
		mouseX -= guiLeft;
		mouseY -= guiTop;
		
		if(mouseX >= -70 && mouseX < -16)
		{
			if(mouseY >= 10 && mouseY < 30 && page < tp - 1)
				drawHoveringText("Next", mouseX + guiLeft, mouseY + guiTop);
			if(mouseY >= 32 && mouseY < 52 && page > 0)
				drawHoveringText("Back", mouseX + guiLeft, mouseY + guiTop);
		}
		
		super.drawScreen(mouseX, mouseY, partialTicks);
	}
	
	@Override
	protected void mouseClicked(int mouseX, int mouseY, int mouseButton) throws IOException
	{
		int xSize = 176;
		int ySize = 206;
		
		int guiLeft = (width - xSize) / 2;
		int guiTop = (height - ySize) / 2;
		
		List<GuiTheme> ts = GuiTheme.THEMES;
		int is = page * 5;
		int tp = ts.size() / 5;
		
		for(int i = is; i < Math.min(is + 5, ts.size()); ++i)
		{
			int j = i - is;
			
			GuiTheme th = ts.get(i % ts.size());
			
			GuiTexBakery b = GuiTexBakery.start();
			b.body(0, 0, 32, 32);
			b.slot(7, 7);
			DynGuiTex tex = b.bake();
			tex.theme = th;
			
			if(tex.elements.get(0).intersects(mouseX - guiLeft - 8, mouseY - guiTop - 7 - j * 40, 1, 1))
			{
				HCClientOptions.getOptions().setTheme(th.name);
				this.tex.theme = GuiTheme.CURRENT_THEME;
				mc.getSoundHandler().playSound(PositionedSoundRecord.getMasterRecord(SoundEvents.UI_BUTTON_CLICK, 1));
				break;
			}
		}
		
		mouseX -= guiLeft;
		mouseY -= guiTop;
		
		if(mouseX >= -70 && mouseX < -16)
		{
			if(mouseY >= 32 && mouseY < 52 && page > 0)
			{
				page--;
				mc.getSoundHandler().playSound(PositionedSoundRecord.getMasterRecord(SoundEvents.UI_BUTTON_CLICK, 1));
			}
			
			if(mouseY >= 10 && mouseY < 30 && page < tp - 1)
			{
				page++;
				mc.getSoundHandler().playSound(PositionedSoundRecord.getMasterRecord(SoundEvents.UI_BUTTON_CLICK, 1));
			}
		}
		
		super.mouseClicked(mouseX, mouseY, mouseButton);
	}
}