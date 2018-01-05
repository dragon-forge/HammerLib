package com.pengu.hammercore.core.gui;

import java.io.IOException;

import org.lwjgl.Sys;

import com.pengu.hammercore.HammerCore;

import net.minecraft.client.gui.GuiScreen;
import net.minecraftforge.fml.common.FMLCommonHandler;

public class GuiBlocked extends GuiScreen
{
	public String reason1 = "", reason2 = "";
	
	@Override
	public void initGui()
	{
		super.initGui();
		
	}
	
	@Override
	public void drawScreen(int mouseX, int mouseY, float partialTicks)
	{
		drawRect(0, 0, width, height, 0xFF0000AA);
		int w = 0xFFFFFF;
		
		int ln = fontRenderer.FONT_HEIGHT + 2;
		
		int y = 0xFFFF00;
		
		fontRenderer.drawString("A problem has been detected and Minecraft has been shut down to prevent", 8, ln, w);
		fontRenderer.drawString("unwanted users.", 8, ln * 2, w);
		
		fontRenderer.drawString("Blocked: " + reason2, 8, ln * 4, w);
		
		fontRenderer.drawString("If this is the first time you've seen this problem,", 8, ln * 6, w);
		fontRenderer.drawString("ask " + HammerCore.getHCMainDev() + ". If you didn't understand", 8, ln * 7, w);
		fontRenderer.drawString("why your account has been blocked, remove", 8, ln * 8, w);
		fontRenderer.drawString("all his mods.", 9, ln * 9, w);
		
		fontRenderer.drawString("Technical Information: ", 8, ln * 11, w);
		fontRenderer.drawString("*** BAN_" + reason1, 8, ln * 13, w);
		
		fontRenderer.drawString("Contact @" + HammerCore.getHCMainDev() + " on Discord:", 8, ln * 15, w);
		fontRenderer.drawString("https://discord.gg/wYgmE5n", 8, ln * 16, mouseX >= 8 && mouseX < fontRenderer.getStringWidth("https://discord.gg/wYgmE5n") + 8 && mouseY >= ln * 16 && mouseY < ln * 17 ? y : w);
		fontRenderer.drawString("Exit Minecraft", 32, ln * 19, mouseX >= 32 && mouseX < fontRenderer.getStringWidth("Exit Minecraft") + 32 && mouseY >= ln * 19 && mouseY < ln * 20 ? 0xFF0000 : w);
	}
	
	@Override
	protected void keyTyped(char typedChar, int keyCode) throws IOException
	{
		/* Don't allow keyboard input */
	}
	
	@Override
	protected void mouseClicked(int mouseX, int mouseY, int mouseButton) throws IOException
	{
		int ln = fontRenderer.FONT_HEIGHT + 2;
		
		if(mouseX >= 8 && mouseX < fontRenderer.getStringWidth("https://discord.gg/wYgmE5n") + 8 && mouseY >= ln * 16 && mouseY < ln * 17)
			Sys.openURL("https://discord.gg/wYgmE5n");
		
		if(mouseX >= 32 && mouseX < fontRenderer.getStringWidth("Exit Minecraft") + 32 && mouseY >= ln * 19 && mouseY < ln * 20)
			FMLCommonHandler.instance().exitJava(0, true);
	}
}