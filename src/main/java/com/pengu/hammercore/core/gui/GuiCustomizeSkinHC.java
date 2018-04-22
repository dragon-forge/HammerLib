package com.pengu.hammercore.core.gui;

import java.io.IOException;

import com.pengu.hammercore.client.HCClientOptions;

import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.GuiCustomizeSkin;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.resources.I18n;
import net.minecraft.entity.player.EnumPlayerModelParts;

public class GuiCustomizeSkinHC extends GuiCustomizeSkin
{
	public GuiButton renderSpecial;
	public GuiButton overrideCape;
	
	public GuiCustomizeSkinHC(GuiScreen parentScreenIn)
	{
		super(parentScreenIn);
	}
	
	@Override
	public void initGui()
	{
		super.initGui();
		GuiButton done = buttonList.get(buttonList.size() - 1);
		
		int i = EnumPlayerModelParts.values().length + 1;
		
		renderSpecial = addButton(new GuiButton(64_001, this.width / 2 - 155 + i % 2 * 160, this.height / 6 + 24 * (i >> 1), 150, 20, ""));
		renderSpecial.displayString = I18n.format("options.modelPart.hammercore:renderSpecial") + ": " + I18n.format("options.o" + (HCClientOptions.options.renderSpecial ? "n" : "ff"));
		
		++i;
		
		overrideCape = addButton(new GuiButton(64_001, this.width / 2 - 155 + i % 2 * 160, this.height / 6 + 24 * (i >> 1), 150, 20, ""));
		overrideCape.displayString = I18n.format("options.modelPart.hammercore:overrideCape") + ": " + I18n.format("options.o" + (HCClientOptions.options.overrideCape ? "n" : "ff"));
		
		done.y += 24;
	}
	
	@Override
	protected void actionPerformed(GuiButton button) throws IOException
	{
		if(button == renderSpecial)
		{
			HCClientOptions.getOptions().renderSpecial = !HCClientOptions.getOptions().renderSpecial;
			renderSpecial.displayString = I18n.format("options.modelPart.hammercore:renderSpecial") + ": " + I18n.format("options.o" + (HCClientOptions.getOptions().renderSpecial ? "n" : "ff"));
			
			HCClientOptions.getOptions().saveAndSendToServer();
		} else if(button == overrideCape)
		{
			HCClientOptions.getOptions().overrideCape = !HCClientOptions.getOptions().overrideCape;
			overrideCape.displayString = I18n.format("options.modelPart.hammercore:overrideCape") + ": " + I18n.format("options.o" + (HCClientOptions.getOptions().overrideCape ? "n" : "ff"));
			
			HCClientOptions.getOptions().saveAndSendToServer();
		} else
			super.actionPerformed(button);
	}
}