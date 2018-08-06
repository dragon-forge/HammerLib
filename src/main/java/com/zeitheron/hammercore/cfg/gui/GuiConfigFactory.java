package com.zeitheron.hammercore.cfg.gui;

import java.util.Set;

import com.zeitheron.hammercore.HammerCore;
import com.zeitheron.hammercore.cfg.HammerCoreConfigs;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiScreen;
import net.minecraftforge.fml.client.IModGuiFactory;

/**
 * An interal class.
 */
public class GuiConfigFactory implements IModGuiFactory
{
	@Override
	public void initialize(Minecraft minecraftInstance)
	{
		HammerCore.LOG.info("Created Hammer Core Gui Config Factory!");
	}
	
	@Override
	public Set<RuntimeOptionCategoryElement> runtimeGuiCategories()
	{
		return null;
	}
	
	@Override
	public boolean hasConfigGui()
	{
		return true;
	}
	
	@Override
	public GuiScreen createConfigGui(GuiScreen parentScreen)
	{
		return new HCConfigGui(parentScreen, HammerCoreConfigs.cfg, "hammercore");
	}
}