package com.pengu.hammercore.core.gui.book;

import java.io.IOException;

import org.lwjgl.opengl.GL11;

import com.pengu.hammercore.bookAPI.BookEntry;
import com.pengu.hammercore.bookAPI.BookPage;
import com.pengu.hammercore.client.GLRenderState;
import com.pengu.hammercore.client.utils.RenderUtil;
import com.pengu.hammercore.core.gui.GuiCentered;

import net.minecraft.client.audio.PositionedSoundRecord;
import net.minecraft.init.SoundEvents;

public class GuiBookEntry extends GuiCentered
{
	public final BookEntry entry;
	public final GuiBookCategory bookGui;
	
	public int page = 0, lastPage = -1;
	
	public GuiBookEntry(GuiBookCategory bookGui, BookEntry entry)
	{
		this.entry = entry;
		this.bookGui = bookGui;
		xSize = 256;
		ySize = 206;
	}
	
	@Override
	protected void drawGuiContainerBackgroundLayer(float partialTicks, int mouseX, int mouseY)
	{
		if(page != lastPage)
		{
			lastPage = page;
			entry.getPageAt(page).prepare();
		}
		
		GLRenderState.BLEND.on();
		mc.getTextureManager().bindTexture(entry.category.book.customEntryBackground);
		RenderUtil.drawTexturedModalRect(guiLeft, guiTop, 0, 0, xSize, ySize);
		
		if(entry.getPageCount() > 0)
		{
			BookPage page = entry.getPageAt(this.page);
			GL11.glPushMatrix();
			GL11.glTranslated(guiLeft, guiTop, 0);
			page.render(mouseX, mouseY);
			GL11.glPopMatrix();
		} else
			mc.displayGuiScreen(bookGui);
		
		GLRenderState.BLEND.off();
		
		boolean hoveredOver = mouseX >= guiLeft + xSize / 2 - 9 && mouseY >= guiTop + ySize - 4 && mouseX < guiLeft + xSize / 2 + 9 && mouseY < guiTop + ySize + 5;
		
		GL11.glColor4d(1, 1, 1, 1);
		mc.getTextureManager().bindTexture(entry.category.book.customBackground);
		RenderUtil.drawTexturedModalRect(guiLeft + xSize / 2 - 9, guiTop + ySize - 4, hoveredOver ? 19 : 0, 203, 18, 9);
		
		if(page < entry.getPageCount() - 1)
		{
			hoveredOver = mouseX >= guiLeft + xSize - 18 && mouseY >= guiTop + ySize - 6 && mouseX < guiLeft + xSize && mouseY < guiTop + ySize + 4;
			
			GL11.glColor4d(1, 1, 1, 1);
			mc.getTextureManager().bindTexture(entry.category.book.customBackground);
			RenderUtil.drawTexturedModalRect(guiLeft + xSize - 18, guiTop + ySize - 6, hoveredOver ? 19 : 0, 181, 18, 10);
		}
		
		if(page > 0)
		{
			hoveredOver = mouseX >= guiLeft && mouseY >= guiTop + ySize - 6 && mouseX < guiLeft + 18 && mouseY < guiTop + ySize + 4;
			
			GL11.glColor4d(1, 1, 1, 1);
			mc.getTextureManager().bindTexture(entry.category.book.customBackground);
			RenderUtil.drawTexturedModalRect(guiLeft, guiTop + ySize - 6, hoveredOver ? 19 : 0, 192, 18, 10);
		}
	}
	
	@Override
	protected void mouseClicked(int mouseX, int mouseY, int mouseButton) throws IOException
	{
		if(mouseButton == 1 || (mouseButton == 0 && mouseX >= guiLeft + xSize / 2 - 9 && mouseY >= guiTop + ySize - 4 && mouseX < guiLeft + xSize / 2 + 9 && mouseY < guiTop + ySize + 5))
		{
			mc.displayGuiScreen(bookGui);
			mc.getSoundHandler().playSound(PositionedSoundRecord.getMasterRecord(SoundEvents.UI_BUTTON_CLICK, 1));
			return;
		}
		
		if(mouseButton == 0 && page < entry.getPageCount() - 1 && mouseX >= guiLeft + xSize - 18 && mouseY >= guiTop + ySize - 6 && mouseX < guiLeft + xSize && mouseY < guiTop + ySize + 4)
		{
			mc.getSoundHandler().playSound(PositionedSoundRecord.getMasterRecord(SoundEvents.UI_BUTTON_CLICK, 1));
			page++;
		}
		
		if(mouseButton == 0 && page > 0 && mouseX >= guiLeft && mouseY >= guiTop + ySize - 6 && mouseX < guiLeft + 18 && mouseY < guiTop + ySize + 4)
		{
			mc.getSoundHandler().playSound(PositionedSoundRecord.getMasterRecord(SoundEvents.UI_BUTTON_CLICK, 1));
			page--;
		}
		
		super.mouseClicked(mouseX, mouseY, mouseButton);
	}
	
	@Override
	protected void keyTyped(char typedChar, int keyCode) throws IOException
	{
		if(keyCode == 1)
			mc.displayGuiScreen(bookGui);
	}
}