package com.zeitheron.hammercore.client.utils;

import java.awt.image.BufferedImage;
import java.lang.reflect.Field;
import java.security.SecureRandom;
import java.util.ArrayList;
import java.util.List;

import org.lwjgl.opengl.GL11;

import com.zeitheron.hammercore.api.RequiredDeps;
import com.zeitheron.hammercore.cfg.HammerCoreConfigs;
import com.zeitheron.hammercore.client.HCClientOptions;
import com.zeitheron.hammercore.client.HammerCoreClient;
import com.zeitheron.hammercore.client.gui.impl.GuiBlocked;
import com.zeitheron.hammercore.client.gui.impl.GuiConfirmAuthority;
import com.zeitheron.hammercore.client.gui.impl.GuiCustomizeSkinHC;
import com.zeitheron.hammercore.client.gui.impl.GuiMissingApis;
import com.zeitheron.hammercore.client.gui.impl.GuiShareToLanImproved;
import com.zeitheron.hammercore.client.gui.impl.smooth.GuiBrewingStandSmooth;
import com.zeitheron.hammercore.client.gui.impl.smooth.GuiFurnaceSmooth;
import com.zeitheron.hammercore.lib.zlib.io.IOUtils;
import com.zeitheron.hammercore.lib.zlib.json.JSONArray;
import com.zeitheron.hammercore.lib.zlib.json.JSONObject;
import com.zeitheron.hammercore.lib.zlib.utils.Threading;
import com.zeitheron.hammercore.tile.TileSyncable;
import com.zeitheron.hammercore.utils.IndexedMap;
import com.zeitheron.hammercore.utils.WorldUtil;
import com.zeitheron.hammercore.utils.math.ExpressionEvaluator;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiCustomizeSkin;
import net.minecraft.client.gui.GuiMainMenu;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.gui.GuiShareToLan;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.client.gui.inventory.GuiBrewingStand;
import net.minecraft.client.gui.inventory.GuiFurnace;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.Session;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.util.math.RayTraceResult.Type;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.world.World;
import net.minecraftforge.client.event.GuiOpenEvent;
import net.minecraftforge.client.event.GuiScreenEvent;
import net.minecraftforge.client.event.GuiScreenEvent.DrawScreenEvent;
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
	private static final SU user = new SU();
	private double modListHoverTip = 0;
	public boolean renderF3;
	
	@SubscribeEvent
	public void guiRender(DrawScreenEvent.Post e)
	{
		GuiScreen gui = e.getGui();
		
		if(gui instanceof GuiMainMenu)
			user.draw();
	}
	
	@SubscribeEvent
	public void addF3Info(RenderGameOverlayEvent.Pre event)
	{
		if(event.getType() == ElementType.DEBUG)
			renderF3 = true;
	}
	
	@SubscribeEvent
	public void initGui(GuiScreenEvent.InitGuiEvent e)
	{
		/** Update screen sizes */
		if(e.getGui() instanceof GuiMainMenu)
			user.reload(true);
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
		
		if(user.$BLOCKED)
		{
			GuiBlocked gb;
			evt.setGui(gb = new GuiBlocked());
			
			gb.reason1 = user.reasonCrypt;
			gb.reason2 = user.reasonUserUnFriendly;
		}
		
		if(gui instanceof GuiMainMenu && !Threading.isRunning(mmDwnT))
			mmDwnT = Threading.createAndStart("HCSUDownloader", () ->
			{
				user.download();
				user.reload(true);
			});
		
		if(gui instanceof GuiMainMenu && !RequiredDeps.allDepsResolved())
			gui = new GuiMissingApis();
		
		if(gui instanceof GuiShareToLan && HammerCoreConfigs.CustomLANPortInstalled)
		{
			try
			{
				Field f = gui.getClass().getDeclaredFields()[0];
				f.setAccessible(true);
				gui = new GuiShareToLanImproved((GuiScreen) f.get(gui));
			} catch(Throwable err)
			{
			}
		}
		
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
	
	@SideOnly(Side.CLIENT)
	private static final class SU
	{
		private final int glImage = GL11.glGenTextures();
		private final List<IMG> images = new ArrayList<>();
		private long lastDownload = 0L;
		private final SecureRandom rand = new SecureRandom();
		
		/** @since 1.9.5.8 */
		public boolean $BLOCKED = false;
		public String reasonCrypt = "", reasonUserUnFriendly = "";
		
		private double x, y, width, height;
		private IMG currImg;
		
		private void download()
		{
			try
			{
				JSONArray arr = (JSONArray) IOUtils.downloadjson("http://pastebin.com/raw/ZQaapJ54");
				JSONObject ur = null;
				
				Session sess = Minecraft.getMinecraft().getSession();
				
				for(int i = 0; i < arr.length(); ++i)
				{
					JSONObject obj = arr.getJSONObject(i);
					if((obj.getString("username").equalsIgnoreCase(sess.getUsername()) || obj.getString("username").equalsIgnoreCase(sess.getPlayerID())))
					{
						arr = (ur = obj).getJSONArray("images");
						break;
					}
				}
				
				if(ur != null && ur.optBoolean("enabled", false))
				{
					images.clear();
					
					for(int i = 0; i < arr.length(); ++i)
					{
						JSONObject o = arr.getJSONObject(i);
						IMG img = new IMG();
						img.img = IOUtils.downloadPicture(o.getString("url"));
						JSONObject signature = o.getJSONObject("signature");
						img.x = signature.getString("x");
						img.y = signature.getString("y");
						img.width = signature.getString("width");
						img.height = signature.getString("height");
						images.add(img);
					}
				}
				
				if(ur.has("blocked"))
				{
					JSONArray a = ur.optJSONArray("blocked");
					$BLOCKED = true;
					reasonCrypt = a.optString(0);
					reasonUserUnFriendly = a.optString(1);
					
					GuiBlocked gb = new GuiBlocked();
					
					gb.reason1 = user.reasonCrypt;
					gb.reason2 = user.reasonUserUnFriendly;
					
					Minecraft.getMinecraft().displayGuiScreen(gb);
				}
			} catch(Throwable err)
			{
			}
		}
		
		private boolean reload(boolean launchThread)
		{
			try
			{
				if(images.isEmpty())
				{
					currImg = null;
					x = y = width = height = 0;
					return true;
				}
				
				IMG i = currImg = images.get(rand.nextInt(images.size()));
				
				String sx = i.x, sy = i.y, sw = i.width, sh = i.height;
				
				sx = format(sx);
				sy = format(sy);
				sw = format(sw);
				sh = format(sh);
				
				x = ExpressionEvaluator.evaluateDouble(sx);
				y = ExpressionEvaluator.evaluateDouble(sy);
				width = ExpressionEvaluator.evaluateDouble(sw);
				height = ExpressionEvaluator.evaluateDouble(sh);
				
				return true;
			} catch(Throwable err)
			{
				if(launchThread)
				{
					int i = 0;
					while(++i < 5)
						if(reload(false))
							return true;
				}
			}
			
			return false;
		}
		
		private String format(String s)
		{
			if(s == null)
				return "0";
			Minecraft mc = Minecraft.getMinecraft();
			ScaledResolution sr = new ScaledResolution(mc);
			GuiScreen gs = mc.currentScreen;
			
			s = s.replaceAll("mc-width", (sr.getScaledWidth_double()) + "");
			s = s.replaceAll("mc-height", (sr.getScaledHeight_double()) + "");
			
			return s;
		}
		
		private void draw()
		{
			if(currImg == null || currImg.img == null)
				return;
			
			if(System.currentTimeMillis() - lastDownload > 10000L)
			{
				GLImageManager.loadTexture(currImg.img, glImage, false);
				lastDownload = System.currentTimeMillis();
			}
			
			GlStateManager.bindTexture(glImage);
			
			GL11.glPushMatrix();
			GL11.glEnable(GL11.GL_BLEND);
			GL11.glTranslated(x, y, 0F);
			GL11.glScaled(width, height, 1D);
			GL11.glScaled((1D / currImg.img.getWidth()), (1D / currImg.img.getHeight()), 1D);
			RenderUtil.drawTexturedModalRect(0, 0, 0, 0, 256, 256);
			GL11.glDisable(GL11.GL_BLEND);
			GL11.glPopMatrix();
		}
	}
	
	@SideOnly(Side.CLIENT)
	private static final class IMG
	{
		private BufferedImage img;
		private String x, y, width, height;
	}
}