package com.zeitheron.hammercore.client.utils;

import java.lang.reflect.Field;
import java.util.List;

import com.zeitheron.hammercore.api.RequiredDeps;
import com.zeitheron.hammercore.cfg.HammerCoreConfigs;
import com.zeitheron.hammercore.client.HCClientOptions;
import com.zeitheron.hammercore.client.HammerCoreClient;
import com.zeitheron.hammercore.client.gui.impl.GuiConfirmAuthority;
import com.zeitheron.hammercore.client.gui.impl.GuiCustomizeSkinHC;
import com.zeitheron.hammercore.client.gui.impl.GuiMissingApis;
import com.zeitheron.hammercore.client.gui.impl.smooth.GuiBrewingStandSmooth;
import com.zeitheron.hammercore.client.gui.impl.smooth.GuiFurnaceSmooth;
import com.zeitheron.hammercore.lib.zlib.utils.IndexedMap;
import com.zeitheron.hammercore.tile.TileSyncable;
import com.zeitheron.hammercore.utils.WorldUtil;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiCustomizeSkin;
import net.minecraft.client.gui.GuiMainMenu;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.gui.inventory.GuiBrewingStand;
import net.minecraft.client.gui.inventory.GuiFurnace;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.util.math.RayTraceResult.Type;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.world.World;
import net.minecraftforge.client.event.GuiOpenEvent;
import net.minecraftforge.client.event.RenderGameOverlayEvent;
import net.minecraftforge.client.event.RenderGameOverlayEvent.ElementType;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

@SideOnly(Side.CLIENT)
public class RenderGui
{
	private static final UV hammer = new UV(new ResourceLocation("hammercore", "textures/hammer.png"), 0, 0, 256, 256);
	private static final ResourceLocation main_menu_widgets = new ResourceLocation("hammercore", "textures/gui/main_menu_widgets.png");
	private double modListHoverTip = 0;
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
		RayTraceResult omon = Minecraft.getMinecraft().objectMouseOver;
		World world = Minecraft.getMinecraft().world;
		
		if(renderF3)
		{
			List<String> tip = f3.getLeft();
			tip.add(TextFormatting.GOLD + "[HammerCore]" + TextFormatting.RESET + " Approx. Ping: " + HammerCoreClient.ping + " ms.");
			
			tip = f3.getRight();
			if(world != null && omon != null && omon.typeOfHit == Type.BLOCK)
			{
				TileSyncable ts = WorldUtil.cast(world.getTileEntity(omon.getBlockPos()), TileSyncable.class);
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
							str = (val == Boolean.TRUE ? TextFormatting.GREEN : TextFormatting.RED) + (val + "") + TextFormatting.RESET;
						else
							str = val + "";
						
						tip.add(key.toLowerCase() + ": " + str);
					}
				}
			}
			renderF3 = false;
		}
	}
	
	private Thread mmDwnT;
	
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