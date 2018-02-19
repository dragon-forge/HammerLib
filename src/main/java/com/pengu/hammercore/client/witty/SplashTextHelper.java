package com.pengu.hammercore.client.witty;

import java.util.ArrayList;
import java.util.List;

import com.pengu.hammercore.annotations.MCFBus;

import net.minecraft.client.gui.GuiMainMenu;
import net.minecraft.client.resources.I18n;
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
		if(!e.isCustomWittyComment() && e.rand.nextFloat() < .05F)
		{
			List<String> sp = new ArrayList<>();
			
			int t = 1;
			while(true)
			{
				String raw = "hc.splash." + t;
				String f = I18n.format(raw);
				
				if(raw.equals(f))
					break;
				else
					sp.add(f);
			}
			
			for(String modid : SplashModPool.modIds())
				if(!Loader.isModLoaded(modid))
					sp.add(I18n.format("hc.splash.mod", SplashModPool.getName(modid)));
				
			e.setWittyComment(iWittyComment.ofStatic(sp.get(e.rand.nextInt(sp.size()))));
		}
	}
}