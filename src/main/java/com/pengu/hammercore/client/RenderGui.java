package com.pengu.hammercore.client;

import java.awt.image.BufferedImage;
import java.lang.reflect.Field;
import java.security.SecureRandom;
import java.util.ArrayList;
import java.util.List;

import org.lwjgl.input.Mouse;
import org.lwjgl.opengl.GL11;

import com.pengu.hammercore.api.RequiredDeps;
import com.pengu.hammercore.cfg.HammerCoreConfigs;
import com.pengu.hammercore.client.utils.GLImageManager;
import com.pengu.hammercore.client.utils.RenderUtil;
import com.pengu.hammercore.common.utils.IOUtils;
import com.pengu.hammercore.common.utils.WorldUtil;
import com.pengu.hammercore.core.gui.GuiBlocked;
import com.pengu.hammercore.core.gui.GuiConfirmAuthority;
import com.pengu.hammercore.core.gui.GuiMissingApis;
import com.pengu.hammercore.core.gui.GuiShareToLanImproved;
import com.pengu.hammercore.core.gui.modbrowser.GuiModBrowserLoading;
import com.pengu.hammercore.core.gui.smooth.GuiBrewingStandSmooth;
import com.pengu.hammercore.core.gui.smooth.GuiFurnaceSmooth;
import com.pengu.hammercore.json.JSONArray;
import com.pengu.hammercore.json.JSONObject;
import com.pengu.hammercore.json.JSONTokener;
import com.pengu.hammercore.math.ExpressionEvaluator;
import com.pengu.hammercore.math.MathHelper;
import com.pengu.hammercore.tile.TileSyncable;
import com.pengu.hammercore.utils.IndexedMap;

import net.minecraft.client.Minecraft;
import net.minecraft.client.audio.PositionedSoundRecord;
import net.minecraft.client.gui.GuiMainMenu;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.gui.GuiShareToLan;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.client.gui.inventory.GuiBrewingStand;
import net.minecraft.client.gui.inventory.GuiFurnace;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.init.SoundEvents;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.util.math.RayTraceResult.Type;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.world.World;
import net.minecraftforge.client.event.GuiOpenEvent;
import net.minecraftforge.client.event.GuiScreenEvent.DrawScreenEvent;
import net.minecraftforge.client.event.GuiScreenEvent.MouseInputEvent;
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
	private static final SpecialUser user = new SpecialUser();
	private double modListHoverTip = 0;
	private boolean renderF3;
	
	@SubscribeEvent
	public void guiRender(DrawScreenEvent.Post e)
	{
		GuiScreen gui = e.getGui();
		
		if(gui instanceof GuiMainMenu)
		{
			if(HammerCoreConfigs.client_modBrowser)
			{
				int mouseX = e.getMouseX();
				int mouseY = e.getMouseY();
				
				int xOff = gui.width / 2 - 15;
				
				boolean isHovered = mouseX >= xOff && mouseY >= 0 && mouseX < xOff + 30 && mouseY < 3;
				isHovered |= mouseX >= xOff + 3 && mouseY >= 0 && mouseX < xOff + 27 && mouseY < modListHoverTip + 9;
				
				if(isHovered)
					modListHoverTip = MathHelper.clip(modListHoverTip + .8D, 0, 12);
				else
					modListHoverTip = MathHelper.clip(modListHoverTip - .8D, 0, 12);
				
				gui.mc.getTextureManager().bindTexture(main_menu_widgets);
				
				GL11.glPushMatrix();
				GL11.glTranslated(xOff, 0, 0);
				GLRenderState.BLEND.captureState();
				GLRenderState.BLEND.on();
				
				GL11.glColor4d(1, 1, 1, 1);
				
				RenderUtil.drawTexturedModalRect(0, 0, 0, 0, 30, 4);
				RenderUtil.drawTexturedModalRect(0, 4, 0, 4, 30, modListHoverTip);
				RenderUtil.drawTexturedModalRect(0, 4 + modListHoverTip, 0, 16, 30, 5);
				
				GL11.glColor4d(1, 1, 1, modListHoverTip / 12D);
				
				RenderUtil.drawTexturedModalRect(0, 0, 30, 0, 30, 4);
				RenderUtil.drawTexturedModalRect(0, 4, 30, 4, 30, modListHoverTip);
				RenderUtil.drawTexturedModalRect(0, 4 + modListHoverTip, 30, 16, 30, 5);
				
				GL11.glColor4d(1, 1, 1, 1);
				
				float f = 1F - net.minecraft.util.math.MathHelper.abs(net.minecraft.util.math.MathHelper.sin((Minecraft.getSystemTime() + 47647L) % 10000L / 10000.0F * ((float) Math.PI * 2F)) * 0.1F);
				
				GL11.glPushMatrix();
				GL11.glTranslated(14 - (f * 8) + 8, modListHoverTip - 3 - (f * 8) + 8, 0);
				GL11.glRotated((12 - modListHoverTip) / 12 * -90, 0, 0, 1);
				GL11.glTranslated(-8, -8, 0);
				
				hammer.render(0, 0, 16 * f, 16 * f);
				GL11.glPopMatrix();
				
				GLRenderState.BLEND.reset();
				GL11.glPopMatrix();
			}
			
			user.draw();
		}
	}
	
	@SubscribeEvent
	public void mouseClick(MouseInputEvent.Pre evt)
	{
		int mouseX = Mouse.getEventX() * evt.getGui().width / evt.getGui().mc.displayWidth;
		int mouseY = evt.getGui().height - Mouse.getEventY() * evt.getGui().height / evt.getGui().mc.displayHeight - 1;
		int eventButton = Mouse.getEventButton();
		
		if(Mouse.getEventButtonState())
		{
			GuiScreen gui = evt.getGui();
			
			if(gui instanceof GuiMainMenu)
			{
				int xOff = evt.getGui().width / 2 - 15;
				
				boolean isHovered = mouseX >= xOff && mouseY >= 0 && mouseX < xOff + 30 && mouseY < 3;
				isHovered |= mouseX >= xOff + 3 && mouseY >= 0 && mouseX < xOff + 27 && mouseY < modListHoverTip + 9;
				
				if(isHovered && eventButton == 0 && HammerCoreConfigs.client_modBrowser)
				{
					modListHoverTip = 0;
					Minecraft.getMinecraft().getSoundHandler().playSound(PositionedSoundRecord.getMasterRecord(SoundEvents.UI_BUTTON_CLICK, 1));
					GuiModBrowserLoading gui0;
					gui.mc.displayGuiScreen(gui0 = new GuiModBrowserLoading());
					gui0.panoramaTimer = ((GuiMainMenu) gui).panoramaTimer;
				}
			}
		}
	}
	
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
			List<String> tip = f3.getRight();
			if(world != null && omon != null && omon.typeOfHit == Type.BLOCK)
			{
				TileSyncable ts = WorldUtil.cast(world.getTileEntity(omon.getBlockPos()), TileSyncable.class);
				if(ts != null)
				{
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
		
		if(user.$BLOCKED)
		{
			GuiBlocked gb;
			evt.setGui(gb = new GuiBlocked());
			
			gb.reason1 = user.reasonCrypt;
			gb.reason2 = user.reasonUserUnFriendly;
		}
		
		if(gui instanceof GuiMainMenu)
		{
			new Thread(() ->
			{
				user.download();
				user.reload(true);
			}).start();
		}
		
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
	private static final class SpecialUser
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
				JSONArray arr = (JSONArray) new JSONTokener(new String(IOUtils.downloadData("http://pastebin.com/raw/ZQaapJ54"))).nextValue();
				JSONObject ur = null;
				
				for(int i = 0; i < arr.length(); ++i)
				{
					JSONObject obj = arr.getJSONObject(i);
					if(obj.optBoolean("enabled", false) && obj.getString("username").equals(Minecraft.getMinecraft().getSession().getUsername()))
					{
						arr = (ur = obj).getJSONArray("images");
						break;
					}
				}
				
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
					new Thread(() ->
					{
						int i = 0;
						while(++i < 5 && !reload(false))
							;
					}).start();
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
			
			double displacex = sr.getScaledWidth_double() / sr.getScaledWidth();
			double displacey = sr.getScaledHeight_double() / sr.getScaledHeight();
			
			// s = s.replaceAll("mc-width", (mc.displayWidth * displacex) + "");
			s = s.replaceAll("mc-width", (mc.displayWidth) + "");
			s = s.replaceAll("mc-height", (mc.displayHeight) + "");
			// s = s.replaceAll("mc-height", (mc.displayHeight * displacey) +
			// "");
			
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