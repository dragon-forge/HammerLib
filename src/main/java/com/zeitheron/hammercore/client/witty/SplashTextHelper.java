package com.zeitheron.hammercore.client.witty;

import java.util.ArrayList;
import java.util.List;
import java.util.function.BooleanSupplier;

import com.zeitheron.hammercore.lib.zlib.tuple.TwoTuple;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiMainMenu;
import net.minecraft.client.resources.I18n;
import net.minecraftforge.client.event.GuiOpenEvent;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.common.Loader;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

public class SplashTextHelper
{
	public static IWittyComment splash;
	private static String lastSplashText;
	
	private static final List<TwoTuple<BooleanSupplier, String>> mods = new ArrayList<>();
	
	static
	{
		addModSplashesIfOneModInstalled("hc.splash.thaumcraft", "thaumcraft", "lostthaumaturgy");
		addModSplashesIfOneModInstalled("hc.splash.draconicevolution", "draconicevolution");
		addModSplashesIfOneModInstalled("hc.splash.ae2", "appliedenergistics2");
		addModSplashesIfOneModInstalled("hc.splash.ic2", "ic2");
	}
	
	public static void addModSplashesIfOneModInstalled(String ident, String... modsOR)
	{
		mods.add(new TwoTuple<>(() ->
		{
			for(String mod : modsOR)
				if(Loader.isModLoaded(mod))
					return true;
			return false;
		}, ident));
	}
	
	public static void addModSplashesIfAllModsInstalled(String ident, String... modsAND)
	{
		mods.add(new TwoTuple<>(() ->
		{
			for(String mod : modsAND)
				if(!Loader.isModLoaded(mod))
					return false;
			return true;
		}, ident));
	}
	
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
			String birthday = DaylengthSplashes.getDaylengthSplash();
			if(birthday != null)
				splash = IWittyComment.ofStatic(birthday);
			else
			{
				WittyCommentChooseEvent w = new WittyCommentChooseEvent();
				MinecraftForge.EVENT_BUS.post(w);
				splash = w.getWittyComment();
			}
		}
	}
	
	@SubscribeEvent
	public void addSplashes(WittyCommentChooseEvent e)
	{
		List<String> sp = new ArrayList<>();
		
		addFormattedSubs("hc.splash", sp);
		
		for(TwoTuple<BooleanSupplier, String> tt : mods)
			if(tt.get1().getAsBoolean())
				addFormattedSubs(tt.get2(), sp);
			
		for(String modid : SplashModPool.modIds())
			if(!Loader.isModLoaded(modid))
				sp.add(I18n.format("hc.splash.mod", SplashModPool.getName(modid)));
			
		double log2 = Math.log(sp.size());
		log2 *= log2;
		
		if(!e.isCustomWittyComment() && e.rand.nextFloat() * log2 < .05F * sp.size())
			e.setWittyComment(IWittyComment.ofStatic(replaceVars(sp.get(e.rand.nextInt(sp.size())))));
	}
	
	private static void addFormattedSubs(String base, List<String> list)
	{
		int t = 1;
		while(true)
		{
			String raw = base + '.' + t;
			String f = I18n.format(raw);
			if(raw.equals(f))
				break;
			else
				list.add(f);
			++t;
		}
	}
	
	private String replaceVars(String str)
	{
		str = str.replaceAll("%player%", Minecraft.getMinecraft().getSession().getUsername());
		return str;
	}
}