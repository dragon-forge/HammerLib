package com.zeitheron.hammercore.client.gui.impl;

import java.io.IOException;
import java.util.function.Supplier;

import com.zeitheron.hammercore.client.HCClientOptions;

import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.GuiCustomizeSkin;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.resources.I18n;
import net.minecraft.entity.player.EnumPlayerModelParts;

public class GuiCustomizeSkinHC extends GuiCustomizeSkin
{
	public static Supplier<? extends GuiScreen> customization;
	
	public GuiButton renderSpecial;
	public GuiButton overrideCape;
	public GuiButton skinType;
	public GuiButton cbtn;
	
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
		
		skinType = addButton(new GuiButton(64_001, this.width / 2 - 155 + i % 2 * 160, this.height / 6 + 24 * (i >> 1), 150, 20, ""));
		skinType.displayString = I18n.format("options.modelPart.hammercore:skinType") + ": " + I18n.format("options.hammercore:skintype." + HCClientOptions.options.skinType);
		
		done.y += 24;
		
		if(customization != null)
		{
			++i;
			
			cbtn = addButton(new GuiButton(64_001, this.width / 2 - 155 + i % 2 * 160, this.height / 6 + 24 * (i >> 1), 150, 20, ""));
			cbtn.displayString = "Customizations...";
			
			done.y += 24;
		}
	}
	
	@Override
	protected void actionPerformed(GuiButton button) throws IOException
	{
		HCClientOptions c = HCClientOptions.getOptions();
		
		if(button == renderSpecial)
		{
			c.renderSpecial = !c.renderSpecial;
			renderSpecial.displayString = I18n.format("options.modelPart.hammercore:renderSpecial") + ": " + I18n.format("options.o" + (c.renderSpecial ? "n" : "ff"));
			
			c.saveAndSendToServer();
		} else if(button == overrideCape)
		{
			c.overrideCape = !c.overrideCape;
			overrideCape.displayString = I18n.format("options.modelPart.hammercore:overrideCape") + ": " + I18n.format("options.o" + (c.overrideCape ? "n" : "ff"));
			
			c.saveAndSendToServer();
		} else if(button == skinType)
		{
			c.skinType = (c.skinType + 1) % 3;
			skinType.displayString = I18n.format("options.modelPart.hammercore:skinType") + ": " + I18n.format("options.hammercore:skintype." + HCClientOptions.options.skinType);
			
			c.saveAndSendToServer();
		} else if(cbtn != null && button == cbtn)
		{
			mc.displayGuiScreen(customization.get());
		} else
			super.actionPerformed(button);
	}
}