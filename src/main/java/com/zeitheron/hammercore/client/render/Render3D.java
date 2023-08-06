package com.zeitheron.hammercore.client.render;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import com.zeitheron.hammercore.client.adapter.ChatMessageAdapter;
import org.lwjgl.opengl.GL11;

import com.zeitheron.hammercore.HammerCore;
import com.zeitheron.hammercore.HammerCore.HCAuthor;
import com.zeitheron.hammercore.ServerHCClientPlayerData;
import com.zeitheron.hammercore.client.HCClientOptions;
import com.zeitheron.hammercore.client.particle.api.ParticleList;
import com.zeitheron.hammercore.client.render.player.PlayerRenderingManager;
import com.zeitheron.hammercore.client.render.world.PositionRenderer;
import com.zeitheron.hammercore.client.utils.RenderUtil;
import com.zeitheron.hammercore.event.client.ClientLoadedInEvent;
import com.zeitheron.hammercore.net.HCNet;
import com.zeitheron.hammercore.net.internal.opts.PacketCHCOpts;
import com.zeitheron.hammercore.proxy.RenderProxy_Client;
import com.zeitheron.hammercore.utils.color.Color;
import com.zeitheron.hammercore.utils.color.ColorHelper;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.ChatLine;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.gui.GuiNewChat;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.GlStateManager.DestFactor;
import net.minecraft.client.renderer.GlStateManager.SourceFactor;
import net.minecraft.client.renderer.RenderGlobal;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Vec3d;
import net.minecraft.util.text.TextComponentTranslation;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.util.text.event.ClickEvent;
import net.minecraft.util.text.event.HoverEvent;
import net.minecraftforge.client.event.RenderGameOverlayEvent;
import net.minecraftforge.client.event.RenderGameOverlayEvent.ElementType;
import net.minecraftforge.client.event.RenderPlayerEvent;
import net.minecraftforge.client.event.RenderWorldLastEvent;
import net.minecraftforge.fml.common.Loader;
import net.minecraftforge.fml.common.ModContainer;
import net.minecraftforge.fml.common.eventhandler.EventPriority;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent;
import net.minecraftforge.fml.relauncher.Side;

public class Render3D
{
	private static final HCAuthor[] AUTHORS = HammerCore.getHCAuthors();
	public static int ticks = 0;
	
	private static final List<PositionRenderer> renders = new ArrayList<>();
	private static final List<PositionRenderer> renderQueue = new ArrayList<>();
	private static int sneakTime = 0;
	public static int sneakTicks = 0;
	public static final Set<String> loadedPlayers = new HashSet<>();
	
	public static void registerPositionRender(PositionRenderer render)
	{
		if(!renders.contains(render) && !renderQueue.contains(render))
			renderQueue.add(render);
	}
	
	static final int alpha = 0x33 << 24;
	
	@SubscribeEvent
	public void clientReady(ClientLoadedInEvent e)
	{
		HammerCore.invalidCertificates.keySet().stream().forEach(mod ->
		{
			ModContainer mc = Loader.instance().getIndexedModList().get(mod);
			if(mc != null)
			{
				TextComponentTranslation tct = new TextComponentTranslation("chat.hammercore:newversion.clickdwn");
				tct.getStyle().setColor(TextFormatting.BLUE);
				tct.getStyle().setUnderlined(true);
				tct.getStyle().setHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT, new TextComponentTranslation("chat.hammercore:newversion.clickdwn.detail")));
				tct.getStyle().setClickEvent(new ClickEvent(ClickEvent.Action.OPEN_URL, HammerCore.invalidCertificates.get(mod)));
				Minecraft.getMinecraft().ingameGUI.getChatGUI().printChatMessage(new TextComponentTranslation("chat.hammercore:corrupt", TextFormatting.AQUA + mc.getName() + TextFormatting.RESET).appendText(" ").appendSibling(tct));
			}
		});
		
		while(!ChatMessageAdapter.messages.isEmpty())
			Minecraft.getMinecraft().ingameGUI.getChatGUI().printChatMessage(ChatMessageAdapter.messages.remove(0));
	}
	
	@SubscribeEvent(priority = EventPriority.LOWEST)
	public void renderWorld(RenderWorldLastEvent evt)
	{
		while(!renderQueue.isEmpty())
			renders.add(renderQueue.remove(0));
		
		if(RenderProxy_Client.needsClConfigSync)
		{
			RenderProxy_Client.needsClConfigSync = false;
			HCNet.INSTANCE.sendToServer(new PacketCHCOpts().setPlayer(Minecraft.getMinecraft().player).setOpts(HCClientOptions.getOptions()));
		}
		
		for(int i = 0; i < renders.size(); ++i)
		{
			PositionRenderer render = renders.get(i);
			if(render.isDead())
				renders.remove(i);
			else if(render.canRender(Minecraft.getMinecraft().player))
				render.render(Minecraft.getMinecraft().player, render.calcX(), render.calcY(), render.calcZ());
		}
		
		ParticleList.renderExtendedParticles(evt);
	}
	
	@SubscribeEvent
	public void renderSpecial(RenderPlayerEvent.Post e)
	{
		EntityPlayer player = e.getEntityPlayer();
		PlayerRenderingManager.get(player.getGameProfile().getName()).render(e);
	}
	
	public static void renderFilledBlockOverlay(AxisAlignedBB aabb, Vec3d pos, float partialTicks, int argb)
	{
		EntityPlayer player = Minecraft.getMinecraft().player;
		
		GL11.glPushMatrix();
		GL11.glTranslated(pos.x, pos.y, pos.z);
		GlStateManager.enableBlend();
		GlStateManager.tryBlendFuncSeparate(GlStateManager.SourceFactor.SRC_ALPHA, GlStateManager.DestFactor.ONE_MINUS_SRC_ALPHA, GlStateManager.SourceFactor.ONE, GlStateManager.DestFactor.ZERO);
		GlStateManager.disableTexture2D();
		GlStateManager.depthMask(false);
		
		double d0 = player.lastTickPosX + (player.posX - player.lastTickPosX) * partialTicks;
		double d1 = player.lastTickPosY + (player.posY - player.lastTickPosY) * partialTicks;
		double d2 = player.lastTickPosZ + (player.posZ - player.lastTickPosZ) * partialTicks;
		RenderGlobal.renderFilledBox(aabb.offset(-d0, -d1, -d2), ColorHelper.getRed(argb), ColorHelper.getGreen(argb), ColorHelper.getBlue(argb), ColorHelper.getAlpha(argb));
		
		GlStateManager.depthMask(true);
		GlStateManager.enableTexture2D();
		GlStateManager.disableBlend();
		GL11.glPopMatrix();
	}
	
	public static void renderFilledBlockOverlay(AxisAlignedBB aabb, BlockPos pos, float partialTicks, int argb)
	{
		EntityPlayer player = Minecraft.getMinecraft().player;
		
		GL11.glPushMatrix();
		GL11.glTranslated(pos.getX(), pos.getY(), pos.getZ());
		GlStateManager.enableBlend();
		GlStateManager.tryBlendFuncSeparate(GlStateManager.SourceFactor.SRC_ALPHA, GlStateManager.DestFactor.ONE_MINUS_SRC_ALPHA, GlStateManager.SourceFactor.ONE, GlStateManager.DestFactor.ZERO);
		GlStateManager.disableTexture2D();
		GlStateManager.depthMask(false);
		
		double d0 = player.lastTickPosX + (player.posX - player.lastTickPosX) * partialTicks;
		double d1 = player.lastTickPosY + (player.posY - player.lastTickPosY) * partialTicks;
		double d2 = player.lastTickPosZ + (player.posZ - player.lastTickPosZ) * partialTicks;
		RenderGlobal.renderFilledBox(aabb.offset(-d0, -d1, -d2), ColorHelper.getRed(argb), ColorHelper.getGreen(argb), ColorHelper.getBlue(argb), ColorHelper.getAlpha(argb));
		
		GlStateManager.depthMask(true);
		GlStateManager.enableTexture2D();
		GlStateManager.disableBlend();
		GL11.glPopMatrix();
	}
	
	public static int chatX = 0;
	public static int chatY = 0;
	
	@SubscribeEvent
	public void cpe(RenderGameOverlayEvent.Chat event)
	{
		chatX = event.getPosX();
		chatY = event.getPosY();
	}
	
	@SubscribeEvent(priority = EventPriority.HIGHEST)
	public void onTick(TickEvent.ClientTickEvent event)
	{
		if(event.side == Side.CLIENT && event.phase == TickEvent.Phase.START)
		{
			ticks++;
			HammerCore.client_ticks = ticks;
			
			if(Minecraft.getMinecraft().player != null)
				if(Minecraft.getMinecraft().player.isSneaking())
				{
					if(sneakTime < 10)
						++sneakTime;
					if(sneakTicks < 20)
						++sneakTicks;
				} else
				{
					if(sneakTime > 0)
						--sneakTime;
					if(sneakTicks > 0)
						--sneakTicks;
				}
			else
			{
				sneakTime = 0;
				sneakTicks = 0;
			}
		}
	}
	
	@SubscribeEvent
	public void rne(RenderGameOverlayEvent.Post event)
	{
		if(event.getType() == ElementType.CHAT)
			try
			{
				GuiNewChat c = Minecraft.getMinecraft().ingameGUI.getChatGUI();
				List<ChatLine> chatLines = c.drawnChatLines;
				int updateCounter = Minecraft.getMinecraft().ingameGUI.updateCounter;
				
				for(int i = 0; c.getChatOpen() && i < chatLines.size() || !c.getChatOpen() && i < chatLines.size() && i < 10; i++)
				{
					ChatLine l = chatLines.get(i);
					String s = l.getChatComponent().getUnformattedText();
					for(int j = 0; j < s.length(); j++)
					{
						for(HCAuthor au : AUTHORS)
							if((j < s.length() - au.getDisplayName().length() && s.substring(j, j + au.getDisplayName().length()).equals(au.getDisplayName())) | (j < s.length() - au.getUsername().length() && s.substring(j, j + au.getUsername().length()).equals(au.getUsername())))
							{
								HCClientOptions opts = ServerHCClientPlayerData.DATAS.get(Side.CLIENT).opts(au.getUsername());
								NBTTagCompound data = opts == null ? null : opts.getCustomData();
								
								if(data != null && data.hasKey("SUsername") && !data.getBoolean("SUsername"))
									continue;
								
								String before = s.substring(0, j);
								float f = Minecraft.getMinecraft().gameSettings.chatOpacity * .9F + .1F;
								int j1 = updateCounter - l.getUpdatedCounter();
								if(j1 < 200 || c.getChatOpen())
								{
									double d0 = j1 / 200.0D;
									d0 = 1.0D - d0;
									d0 = d0 * 10.0D;
									d0 = MathHelper.clamp(d0, 0, 1);
									d0 = d0 * d0;
									int l1 = (int) (255D * d0);
									
									if(c.getChatOpen())
										l1 = 255;
									
									l1 = (int) (l1 * f);
									if((20 * l1) / 255 > 3)
									{
										GlStateManager.enableAlpha();
										GlStateManager.enableBlend();
										GlStateManager.blendFunc(SourceFactor.SRC_ALPHA, DestFactor.ONE);
										int dfunc = GL11.glGetInteger(GL11.GL_DEPTH_FUNC);
										GlStateManager.depthFunc(GL11.GL_LEQUAL);
										int func = GL11.glGetInteger(GL11.GL_ALPHA_TEST_FUNC);
										float ref = GL11.glGetFloat(GL11.GL_ALPHA_TEST_REF);
										GlStateManager.alphaFunc(GL11.GL_ALWAYS, 0);
										GlStateManager.depthMask(false);
										
										GL11.glTranslated(.25, 0, 0);
										drawTextGlowingAuraTransparent(Minecraft.getMinecraft().fontRenderer, au.getUsername(), chatX + 2 + Minecraft.getMinecraft().fontRenderer.getStringWidth(before), chatY - (Minecraft.getMinecraft().fontRenderer.FONT_HEIGHT) * i, l1, au.getColor().get());
										GL11.glTranslated(-.25, 0, 0);
										
										GlStateManager.depthMask(true);
										GlStateManager.alphaFunc(func, ref);
										GlStateManager.depthFunc(dfunc);
										GlStateManager.blendFunc(SourceFactor.SRC_ALPHA, DestFactor.ONE_MINUS_SRC_ALPHA);
										GlStateManager.disableBlend();
										GlStateManager.disableAlpha();
									}
								}
							}
					}
				}
				
			} catch(Throwable err)
			{
				
			}
	}
	
	public static void drawTextGlowingAuraTransparent(FontRenderer font, String s, int x, int y, int a, int rgb)
	{
		GlStateManager.blendFunc(SourceFactor.SRC_ALPHA, DestFactor.ONE);
		
		int r = (int) (ColorHelper.getRed(rgb) * 255F);
		int g = (int) (ColorHelper.getGreen(rgb) * 255F);
		int b = (int) (ColorHelper.getBlue(rgb) * 255F);
		
		font.drawString(s, x, y, Color.packARGB(r, g, b, a));
		RenderUtil.drawTextRGBA(font, s, x - 1, y, r, g, b, (40 * a) / 255);
		RenderUtil.drawTextRGBA(font, s, x + 1, y, r, g, b, (40 * a) / 255);
		RenderUtil.drawTextRGBA(font, s, x, y - 1, r, g, b, (40 * a) / 255);
		RenderUtil.drawTextRGBA(font, s, x, y + 1, r, g, b, (40 * a) / 255);
		RenderUtil.drawTextRGBA(font, s, x - 2, y, r, g, b, (20 * a) / 255);
		RenderUtil.drawTextRGBA(font, s, x + 2, y, r, g, b, (20 * a) / 255);
		RenderUtil.drawTextRGBA(font, s, x, y - 2, r, g, b, (20 * a) / 255);
		RenderUtil.drawTextRGBA(font, s, x, y + 2, r, g, b, (20 * a) / 255);
		RenderUtil.drawTextRGBA(font, s, x - 1, y + 1, r, g, b, (20 * a) / 255);
		RenderUtil.drawTextRGBA(font, s, x + 1, y - 1, r, g, b, (20 * a) / 255);
		RenderUtil.drawTextRGBA(font, s, x - 1, y - 1, r, g, b, (20 * a) / 255);
		RenderUtil.drawTextRGBA(font, s, x + 1, y + 1, r, g, b, (20 * a) / 255);
		GlStateManager.blendFunc(SourceFactor.SRC_ALPHA, DestFactor.ONE_MINUS_SRC_ALPHA);
	}
}