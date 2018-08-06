package com.zeitheron.hammercore.client.gui.impl;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Supplier;

import com.zeitheron.hammercore.HammerCore;
import com.zeitheron.hammercore.client.HCClientOptions;
import com.zeitheron.hammercore.client.PerUserModule.CTButton;
import com.zeitheron.hammercore.proxy.RenderProxy_Client;

import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.GuiCustomizeSkin;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.resources.I18n;
import net.minecraft.entity.player.EnumPlayerModelParts;

/**
 * An internal class.
 */
public class GuiCustomizeSkinHC extends GuiCustomizeSkin
{
	public static Supplier<? extends GuiScreen> customization;
	
	public GuiButton renderSpecial;
	public GuiButton overrideCape;
	public GuiButton skinType;
	public GuiButton cbtn;
	
	public Map<GuiButton, CTButton> customBtnMap = new HashMap<>();
	public Map<CTButton, GuiButton> customCTMap = new HashMap<>();
	
	public GuiCustomizeSkinHC(GuiScreen parentScreenIn)
	{
		super(parentScreenIn);
	}
	
	int buttons;
	GuiButton done;
	
	public GuiButton initializeAdditional(String text)
	{
		GuiButton btn = addButton(new GuiButton(64_001, this.width / 2 - 155 + buttons % 2 * 160, this.height / 6 + 24 * (buttons >> 1), 150, 20, ""));
		btn.displayString = text;
		++buttons;
		if(buttons % 2 == 1)
			done.y += 24;
		return btn;
	}
	
	@Override
	public void initGui()
	{
		super.initGui();
		
		done = buttonList.get(buttonList.size() - 1);
		
		buttons = EnumPlayerModelParts.values().length + 1;
		
		renderSpecial = initializeAdditional(I18n.format("options.modelPart.hammercore:renderSpecial") + ": " + I18n.format("options.o" + (HCClientOptions.options.renderSpecial ? "n" : "ff")));
		overrideCape = initializeAdditional(I18n.format("options.modelPart.hammercore:overrideCape") + ": " + I18n.format("options.o" + (HCClientOptions.options.overrideCape ? "n" : "ff")));
		skinType = initializeAdditional(I18n.format("options.modelPart.hammercore:skinType") + ": " + I18n.format("options.hammercore:skintype." + HCClientOptions.options.skinType));
		if(customization != null)
			cbtn = initializeAdditional("Customizations...");
		
		customBtnMap.clear();
		customCTMap.clear();
		
		if(RenderProxy_Client.module != null)
			for(CTButton btn : RenderProxy_Client.module.getSkinButtons())
			{
				GuiButton k = initializeAdditional(btn.getText());
				customBtnMap.put(k, btn);
				customCTMap.put(btn, k);
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
		} else if(customBtnMap.containsKey(button))
		{
			CTButton b = customBtnMap.get(button);
			if(b == null)
				return;
			b.click();
			button.displayString = b.getText();
		} else
			super.actionPerformed(button);
	}
}