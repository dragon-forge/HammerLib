package com.zeitheron.hammercore.client.utils;

import com.zeitheron.hammercore.api.RequiredDeps;
import com.zeitheron.hammercore.cfg.HammerCoreConfigs;
import com.zeitheron.hammercore.client.*;
import com.zeitheron.hammercore.client.gui.impl.*;
import com.zeitheron.hammercore.client.gui.impl.smooth.*;
import com.zeitheron.hammercore.lib.zlib.utils.IndexedMap;
import com.zeitheron.hammercore.tile.TileSyncable;
import com.zeitheron.hammercore.utils.base.Cast;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.*;
import net.minecraft.client.gui.inventory.*;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.util.math.RayTraceResult.Type;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.world.World;
import net.minecraftforge.client.event.*;
import net.minecraftforge.client.event.RenderGameOverlayEvent.ElementType;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.relauncher.*;

import java.lang.reflect.Field;
import java.util.*;

@SideOnly(Side.CLIENT)
public class RenderGui
{
	public boolean renderF3;
	
	@SubscribeEvent
	public void addF3Info(RenderGameOverlayEvent.Pre event)
	{
		if(event.getType() == ElementType.DEBUG)
			renderF3 = true;
	}
	
	private final IndexedMap<String, Object> f3Right = new IndexedMap<>();
	
	@SubscribeEvent
	public void addF3Info(RenderGameOverlayEvent.Text f3)
	{
		Minecraft mc = Minecraft.getMinecraft();
		RayTraceResult omon = mc.objectMouseOver;
		World world = mc.world;
		
		if(renderF3)
		{
			List<String> tip = f3.getLeft();
			tip.add(TextFormatting.GOLD + "[HammerCore]" + TextFormatting.RESET + " Approx. Ping: " + HammerCoreClient.ping + " ms.");
			
			tip = f3.getRight();
			if(world != null && omon != null && omon.typeOfHit == Type.BLOCK)
			{
				TileSyncable ts = Cast.cast(world.getTileEntity(omon.getBlockPos()), TileSyncable.class);
				if(ts != null)
				{
					String f3r = ts.getF3Registry();
					if(f3r != null)
					{
						int reg = tip.indexOf(world.getBlockState(omon.getBlockPos()).getBlock().getRegistryName().toString());
						if(reg != -1)
							tip.set(reg, f3r);
					}
					
					f3Right.clear();
					ts.addProperties(f3Right, omon);
					List<String> keys = f3Right.getKeys();
					for(int i = 0; i < keys.size(); ++i)
					{
						String key = keys.get(i);
						Object val = f3Right.get(key);
						String str = "";
						
						if(val instanceof Boolean)
							str = ((Boolean) val ? TextFormatting.GREEN : TextFormatting.RED) + Objects.toString(val) + TextFormatting.RESET;
						else
							str = Objects.toString(val);
						
						tip.add(key.toLowerCase() + ": " + str);
					}
				}
			}
			renderF3 = false;
		}
	}
	
	@SubscribeEvent
	public void openGui(GuiOpenEvent evt)
	{
		GuiScreen gui = evt.getGui();
		final GuiScreen fgui = gui;
		
		if(!HCClientOptions.getOptions().checkAuthority())
		{
			evt.setGui(new GuiConfirmAuthority());
			return;
		}
		
		if(gui instanceof GuiCustomizeSkin)
		{
			Field parentScreen = GuiCustomizeSkin.class.getDeclaredFields()[0];
			parentScreen.setAccessible(true);
			try
			{
				gui = (GuiScreen) parentScreen.get(gui);
				gui = new GuiCustomizeSkinHC(gui);
			} catch(Throwable er)
			{
			}
		}
		
		if(gui instanceof GuiMainMenu && !RequiredDeps.allDepsResolved())
			gui = new GuiMissingApis();
		
		smooth:
		{
			// @since 1.5.1
			if(!HammerCoreConfigs.client_smoothVanillaGuis)
				break smooth;
			
			if(gui instanceof GuiFurnace)
			{
				GuiFurnace g = (GuiFurnace) gui;
				gui = new GuiFurnaceSmooth(g.playerInventory, g.tileFurnace);
			}
			
			if(gui instanceof GuiBrewingStand)
			{
				GuiBrewingStand g = (GuiBrewingStand) gui;
				gui = new GuiBrewingStandSmooth(g.playerInventory, g.tileBrewingStand);
			}
			
			break smooth;
		}
		
		if(fgui != gui)
			evt.setGui(gui);
	}
}