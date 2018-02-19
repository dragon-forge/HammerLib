package com.pengu.hammercore.client.witty;

import java.util.ArrayList;
import java.util.List;

import com.pengu.hammercore.annotations.MCFBus;

import net.minecraft.client.gui.GuiMainMenu;
import net.minecraftforge.client.event.GuiOpenEvent;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.common.Loader;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

@MCFBus
public class SplashTextHelper
{
	public static iWittyComment splash;
	private static String lastSplashText;
	
	/** Gets the edited splash text */
	public static String handle(String sp)
	{
		if(splash != null)
			return splash.get();
		return sp;
	}
	
	@SubscribeEvent
	public void guiOpen(GuiOpenEvent e)
	{
		if(e.getGui() instanceof GuiMainMenu)
		{
			WittyCommentChooseEvent w = new WittyCommentChooseEvent();
			MinecraftForge.EVENT_BUS.post(w);
			splash = w.getWittyComment();
		}
	}
	
	@SubscribeEvent
	public void addSplashes(WittyCommentChooseEvent e)
	{
		if(!e.isCustomWittyComment() && e.rand.nextFloat() < .2F)
		{
			List<String> sp = new ArrayList<>();
			
			sp.add("Now with mods!");
			sp.add(":thonking:");
			
			if(!Loader.isModLoaded("botania"))
				sp.add("Also try mod Botania!");
			
			if(!Loader.isModLoaded("jei"))
				sp.add("Also try mod Just Enough Items!");
			
			if(!Loader.isModLoaded("waila"))
				sp.add("Also try mod WAILA!");
			
			if(!Loader.isModLoaded("solarfluxreborn"))
				sp.add("Also try mod Solar Flux Reborn!");
			
			e.setWittyComment(iWittyComment.ofStatic(sp.get(e.rand.nextInt(sp.size()))));
		}
	}
}