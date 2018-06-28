package com.zeitheron.hammercore.client.gui.impl.book;

import java.io.IOException;

import org.lwjgl.opengl.GL11;

import com.zeitheron.hammercore.bookAPI.Book;
import com.zeitheron.hammercore.bookAPI.BookCategory;
import com.zeitheron.hammercore.client.gui.GuiCentered;
import com.zeitheron.hammercore.client.utils.GLRenderState;
import com.zeitheron.hammercore.client.utils.RenderUtil;
import com.zeitheron.hammercore.utils.color.Color;

import net.minecraft.client.audio.PositionedSoundRecord;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.RenderHelper;
import net.minecraft.init.SoundEvents;

public class GuiBook extends GuiCentered
{
	public final Book book;
	
	public GuiBook(Book book)
	{
		this.book = book;
		xSize = book.width;
		ySize = book.height;
	}
	
	@Override
	protected void drawGuiContainerBackgroundLayer(float partialTicks, int mouseX, int mouseY)
	{
		GLRenderState.BLEND.on();
		mc.getTextureManager().bindTexture(book.customBackground);
		RenderUtil.drawTexturedModalRect(guiLeft, guiTop, 0, 0, xSize, ySize);
		
		int FONT_HEIGHT = fontRenderer.FONT_HEIGHT;
		
		int y = 0;
		for(BookCategory cat : book.categories)
		{
			if(cat.isHidden())
				continue;
			fontRenderer.drawString(cat.getTitle(), (int) guiLeft + 12 + (!cat.getIcon().isEmpty() ? FONT_HEIGHT : 0), (int) guiTop + 14 + y, 0, false);
			if(!cat.getIcon().isEmpty())
			{
				GL11.glPushMatrix();
				GlStateManager.disableLighting();
				RenderHelper.enableGUIStandardItemLighting();
				GL11.glTranslated(guiLeft + 11, guiTop + 13 + y, 0);
				GL11.glScaled(1 / 16D * FONT_HEIGHT, 1 / 16D * FONT_HEIGHT, 1);
				itemRender.renderItemAndEffectIntoGUI(cat.getIcon(), 0, 0);
				GlStateManager.disableLighting();
				GL11.glPopMatrix();
			}
			y += fontRenderer.FONT_HEIGHT + 4;
		}
		
		y = 0;
		for(BookCategory cat : book.categories)
		{
			if(cat.isHidden())
				continue;
			if(mouseX >= guiLeft + 10 && mouseY >= guiTop + 12 + y && mouseX < guiLeft + 124 && mouseY < guiTop + 14 + y + fontRenderer.FONT_HEIGHT)
			{
				GL11.glColor4f(1, 1, 1, 1);
				Color.glColourRGB(cat.getHoverColor());
				mc.getTextureManager().bindTexture(book.customBackground);
				RenderUtil.drawTexturedModalRect(guiLeft + 10, guiTop + 12 + y, 146, 0, 110, 11);
				GL11.glColor4f(1, 1, 1, 1);
			}
			
			y += fontRenderer.FONT_HEIGHT + 4;
		}
		GLRenderState.BLEND.off();
	}
	
	@Override
	protected void mouseClicked(int mouseX, int mouseY, int mouseButton) throws IOException
	{
		super.mouseClicked(mouseX, mouseY, mouseButton);
		
		int y = 0;
		if(mouseButton == 0)
			for(BookCategory cat : book.categories)
			{
				if(cat.isHidden())
					continue;
				
				if(mouseX >= guiLeft + 10 && mouseY >= guiTop + 12 + y && mouseX < guiLeft + 124 && mouseY < guiTop + 14 + y + fontRenderer.FONT_HEIGHT)
				{
					if(cat.isDisabled())
					{
						mc.getSoundHandler().playSound(PositionedSoundRecord.getMasterRecord(SoundEvents.UI_BUTTON_CLICK, .6F));
					} else
					{
						mc.displayGuiScreen(new GuiBookCategory(this, cat));
						mc.getSoundHandler().playSound(PositionedSoundRecord.getMasterRecord(SoundEvents.UI_BUTTON_CLICK, 1));
					}
					break;
				}
				
				y += fontRenderer.FONT_HEIGHT + 4;
			}
	}
}