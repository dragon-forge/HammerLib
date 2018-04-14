package com.pengu.hammercore.core.gui;

import java.io.IOException;
import java.util.Arrays;

import com.pengu.hammercore.client.gui.GuiFluidTank;
import com.pengu.hammercore.client.gui.GuiTankTexture;
import com.pengu.hammercore.client.utils.RenderUtil;
import com.pengu.hammercore.color.Color;
import com.pengu.hammercore.core.blocks.tesseract.TileTesseract;
import com.pengu.hammercore.core.blocks.tesseract.TransferMode;
import com.pengu.hammercore.net.HCNetwork;
import com.pengu.hammercore.net.pkt.PacketSetProperty;
import com.pengu.hammercore.net.pkt.PacketSyncSyncableTile;

import net.minecraft.client.audio.PositionedSoundRecord;
import net.minecraft.client.gui.GuiTextField;
import net.minecraft.init.SoundEvents;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.text.TextFormatting;
import net.minecraftforge.fluids.FluidRegistry;
import net.minecraftforge.fluids.FluidTank;

public class GuiTesseract extends GuiCentered
{
	public final ResourceLocation gui = new ResourceLocation("hammercore", "textures/gui/tesseract.png");
	
	public final TileTesseract tile;
	public String lastTileFreq;
	
	public GuiTextField freq;
	
	public GuiTesseract(TileTesseract tile)
	{
		this.tile = tile;
		
		xSize = 176;
		ySize = 76;
	}
	
	@Override
	public void initGui()
	{
		super.initGui();
		
		String text = getTileText();
		if(freq != null)
			text = freq.getText();
		
		freq = new GuiTextField(0, fontRenderer, (int) guiLeft + 23, (int) guiTop + 7, 130, 14);
		freq.setMaxStringLength(36);
		freq.setText(text);
	}
	
	private String getTileText()
	{
		String s = tile.frequency.get();
		if(s == null)
			return "";
		return s;
	}
	
	@Override
	protected void drawGuiContainerBackgroundLayer(float partialTicks, int mouseX, int mouseY)
	{
		FluidTank ft = new FluidTank(FluidRegistry.WATER, 5000 + (int) (5000 * Math.sin(Math.toRadians(mc.world.getTotalWorldTime() + partialTicks))), 10000);
		GuiFluidTank gft = new GuiFluidTank(16, 16, 16, 58, ft);
		
		gft.render(mouseX, mouseY);
		
		int guiLeft = (int) this.guiLeft;
		int guiTop = (int) this.guiTop;
		
		mouseX -= guiLeft;
		mouseY -= guiTop;
		
		if(lastTileFreq == null)
			lastTileFreq = getTileText();
		if(!lastTileFreq.equals(getTileText()))
		{
			lastTileFreq = tile.frequency.get();
			freq.setText(lastTileFreq);
		}
		
		mc.getTextureManager().bindTexture(gui);
		RenderUtil.drawTexturedModalRect(guiLeft, guiTop, 0, 0, xSize, ySize);
		
		boolean pblic = !tile.isPrivate.get();
		boolean hover = mouseX >= 78 && mouseY >= 44 && mouseX < 98 && mouseY < 64;
		RenderUtil.drawTexturedModalRect(guiLeft + 78, guiTop + 44, 206 + (pblic ? 20 : 0), 72 + (hover ? 20 : 0), 20, 20);
		
		hover = mouseX >= 23 && mouseY >= 24 && mouseX < 43 && mouseY < 44;
		RenderUtil.drawTexturedModalRect(guiLeft + 23, guiTop + 24, 206, 32 + (hover ? 20 : 0), 20, 20);
		
		hover = mouseX >= 133 && mouseY >= 24 && mouseX < 153 && mouseY < 44;
		RenderUtil.drawTexturedModalRect(guiLeft + 133, guiTop + 24, 226, 32 + (hover ? 20 : 0), 20, 20);
		
		int page = tile.ioPage.get() == null ? 0 : tile.ioPage.get();
		int startI = page * 5;
		int endI = Math.min(startI + 5, TileTesseract.getAllowedCapabilities().size());
		
		for(int i = startI; i < Math.min(endI, 5); ++i)
		{
			mc.getTextureManager().bindTexture(gui);
			boolean active = tile.getMode(TileTesseract.getAllowedCapabilities().get(i)).active();
			
			int j = i - startI;
			
			Color.glColourRGB(active ? 0xBBFFBB : 0xFFBBBB);
			
			int x = 10 + j * 30;
			hover = mouseX >= x && mouseY >= -19 && mouseX < x + 30 && mouseY < 1;
			RenderUtil.drawTexturedModalRect(guiLeft + x, guiTop - 19, 176, hover ? 23 : 0, 30, 22);
			
			Color.glColourRGB(0xFFFFFF);
			
			itemRender.renderItemAndEffectIntoGUI(TileTesseract.getCapIcon(TileTesseract.getAllowedCapabilities().get(i)), guiLeft + x + 7, guiTop - 15);
		}
		
		freq.drawTextBox();
		
		for(int i = startI; i < Math.min(endI, 5); ++i)
		{
			int j = i - startI;
			int x = 10 + j * 30;
			
			hover = mouseX >= x && mouseY >= -19 && mouseX < x + 30 && mouseY < 1;
			
			if(hover)
				drawHoveringText(Arrays.asList(TileTesseract.getCapName(TileTesseract.getAllowedCapabilities().get(i)), tile.getMode(TileTesseract.getAllowedCapabilities().get(i)).active() ? TextFormatting.GREEN + "Active" : TextFormatting.RED + "Inactive"), mouseX + guiLeft, mouseY + guiTop);
		}
	}
	
	@Override
	protected void keyTyped(char typedChar, int keyCode) throws IOException
	{
		if(!freq.textboxKeyTyped(typedChar, keyCode))
			super.keyTyped(typedChar, keyCode);
	}
	
	@Override
	protected void mouseClicked(int mouseX, int mouseY, int mouseButton) throws IOException
	{
		int guiLeft = (int) this.guiLeft;
		int guiTop = (int) this.guiTop;
		
		super.mouseClicked(mouseX, mouseY, mouseButton);
		freq.mouseClicked(mouseX, mouseY, mouseButton);
		
		mouseX -= guiLeft;
		mouseY -= guiTop;
		
		if(mouseX >= 78 && mouseY >= 44 && mouseX < 98 && mouseY < 64)
		{
			boolean prev = tile.isPrivate.get();
			tile.isPrivate.set(!tile.isPrivate.get());
			PacketSetProperty.toServer(tile, tile.isPrivate);
			mc.getSoundHandler().playSound(PositionedSoundRecord.getMasterRecord(SoundEvents.UI_BUTTON_CLICK, prev ? .6F : 1F));
		}
		
		if(mouseX >= 23 && mouseY >= 24 && mouseX < 43 && mouseY < 44)
		{
			mc.getSoundHandler().playSound(PositionedSoundRecord.getMasterRecord(SoundEvents.UI_BUTTON_CLICK, 1.1F));
			
			tile.frequency.set(freq.getText());
			PacketSetProperty.toServer(tile, tile.frequency);
		}
		
		if(mouseX >= 133 && mouseY >= 24 && mouseX < 153 && mouseY < 44)
		{
			mc.getSoundHandler().playSound(PositionedSoundRecord.getMasterRecord(SoundEvents.UI_BUTTON_CLICK, .9F));
			
			tile.frequency.set("");
			PacketSetProperty.toServer(tile, tile.frequency);
			
			lastTileFreq = getTileText();
			freq.setText(lastTileFreq);
		}
		
		int page = tile.ioPage.get() == null ? 0 : tile.ioPage.get();
		int startI = page * 5;
		int endI = Math.min(startI + 5, TileTesseract.getAllowedCapabilities().size());
		
		for(int i = startI; i < endI % 5; ++i)
		{
			int j = i - startI;
			int x = 10 + j * 30;
			
			if(mouseX >= x && mouseY >= -19 && mouseX < x + 30 && mouseY < 1)
			{
				boolean a = tile.getMode(TileTesseract.getAllowedCapabilities().get(i)).active();
				tile.setMode(TileTesseract.getAllowedCapabilities().get(i), a ? TransferMode.DECLINE : TransferMode.ALLOW);
				HCNetwork.manager.sendToServer(new PacketSyncSyncableTile(tile));
				mc.getSoundHandler().playSound(PositionedSoundRecord.getMasterRecord(SoundEvents.UI_BUTTON_CLICK, a ? .9F : 1.1F));
			}
		}
	}
}