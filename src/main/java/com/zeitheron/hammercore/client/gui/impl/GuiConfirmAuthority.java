package com.zeitheron.hammercore.client.gui.impl;

import java.io.IOException;
import java.util.Arrays;

import com.zeitheron.hammercore.client.HCClientOptions;
import com.zeitheron.hammercore.utils.MD5;

import net.minecraft.client.gui.GuiMainMenu;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.gui.GuiTextField;

public class GuiConfirmAuthority extends GuiScreen
{
	public GuiTextField field;
	
	@Override
	public void initGui()
	{
		super.initGui();
		field = new GuiTextField(0, fontRenderer, width / 4, height / 2 - 10, width / 2, 20);
		field.setMaxStringLength(200);
	}
	
	@Override
	public void drawScreen(int mouseX, int mouseY, float partialTicks)
	{
		drawDefaultBackground();
		super.drawScreen(mouseX, mouseY, partialTicks);
		String ao = mc.getSession().getUsername();
		if(ao.endsWith("s"))
			ao += "'";
		else
			ao += "'s";
		drawCenteredString(fontRenderer, "You logged in from " + ao + " account.", width / 2, height / 2 - 10 - fontRenderer.FONT_HEIGHT * 3, 0xFFFFFF);
		drawCenteredString(fontRenderer, "Please confirm that this is really you.", width / 2, height / 2 - 10 - fontRenderer.FONT_HEIGHT * 2 + 4, 0xFFFFFF);
		
		String text = field.getText();
		if(!isShiftKeyDown())
			field.setText(passcode(text));
		field.drawTextBox();
		field.setText(text);
	}
	
	/**
	 * The fast(est) way (I hope) of making a string with text.length()
	 * asterisks
	 */
	public static String passcode(String text)
	{
		byte[] nt = new byte[text.length()];
		Arrays.fill(nt, (byte) 42);
		return new String(nt);
	}
	
	@Override
	protected void mouseClicked(int mouseX, int mouseY, int mouseButton) throws IOException
	{
		field.mouseClicked(mouseX, mouseY, mouseButton);
	}
	
	@Override
	protected void keyTyped(char typedChar, int keyCode) throws IOException
	{
		if(field.textboxKeyTyped(typedChar, keyCode))
			if(HCClientOptions.checkAuthority(field.getText()))
			{
				HCClientOptions.getOptions().authority = MD5.encrypt(field.getText());
				/* apply passcode */
				HCClientOptions.getOptions().save();
				mc.displayGuiScreen(new GuiMainMenu());
			}
	}
}