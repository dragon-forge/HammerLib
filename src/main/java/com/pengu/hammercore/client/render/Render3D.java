package com.pengu.hammercore.client.render;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.lwjgl.opengl.ARBShaderObjects;
import org.lwjgl.opengl.GL11;

import com.mojang.authlib.minecraft.MinecraftProfileTexture;
import com.pengu.hammercore.HammerCore;
import com.pengu.hammercore.HammerCore.HCAuthor;
import com.pengu.hammercore.MultiHitboxGetter;
import com.pengu.hammercore.client.particle.api.ParticleList;
import com.pengu.hammercore.client.render.player.PlayerRenderingManager;
import com.pengu.hammercore.client.render.shader.ShaderProgram;
import com.pengu.hammercore.client.render.shader.impl.ShaderEnderField;
import com.pengu.hammercore.client.render.vertex.SimpleBlockRendering;
import com.pengu.hammercore.client.render.world.PositionRenderer;
import com.pengu.hammercore.client.utils.RenderBlocks;
import com.pengu.hammercore.client.utils.RenderUtil;
import com.pengu.hammercore.client.utils.UtilsFX;
import com.pengu.hammercore.color.Color;
import com.pengu.hammercore.common.iWrenchable;
import com.pengu.hammercore.common.items.ItemIWrench;
import com.pengu.hammercore.raytracer.RayTracer;
import com.pengu.hammercore.utils.ColorHelper;
import com.pengu.hammercore.vec.Cuboid6;

import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.AbstractClientPlayer;
import net.minecraft.client.gui.ChatLine;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.gui.GuiNewChat;
import net.minecraft.client.network.NetworkPlayerInfo;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.GlStateManager.DestFactor;
import net.minecraft.client.renderer.GlStateManager.SourceFactor;
import net.minecraft.client.renderer.RenderGlobal;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.util.math.RayTraceResult.Type;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;
import net.minecraftforge.client.event.RenderGameOverlayEvent;
import net.minecraftforge.client.event.RenderGameOverlayEvent.ElementType;
import net.minecraftforge.client.event.RenderLivingEvent;
import net.minecraftforge.client.event.RenderPlayerEvent;
import net.minecraftforge.client.event.RenderWorldLastEvent;
import net.minecraftforge.fml.common.ObfuscationReflectionHelper;
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
	
	@SubscribeEvent(priority = EventPriority.LOWEST)
	public void renderWorld(RenderWorldLastEvent evt)
	{
		while(!renderQueue.isEmpty())
			renders.add(renderQueue.remove(0));
		
		for(int i = 0; i < renders.size(); ++i)
		{
			PositionRenderer render = renders.get(i);
			if(render.isDead())
				renders.remove(i);
			else if(render.canRender(Minecraft.getMinecraft().player))
				render.render(Minecraft.getMinecraft().player, render.calcX(), render.calcY(), render.calcZ());
		}
		
		ItemStack mh = Minecraft.getMinecraft().player.getHeldItemMainhand();
		ItemStack oh = Minecraft.getMinecraft().player.getHeldItemOffhand();
		
		if((!mh.isEmpty() && mh.getItem() instanceof ItemIWrench) || (!oh.isEmpty() && oh.getItem() instanceof ItemIWrench))
		{
			RayTraceResult o = Minecraft.getMinecraft().objectMouseOver;
			
			if(o != null && o.getBlockPos() != null && o.typeOfHit == Type.BLOCK)
			{
				World w = Minecraft.getMinecraft().world;
				BlockPos p = o.getBlockPos();
				IBlockState s = w.getBlockState(p);
				Block b = s.getBlock();
				TileEntity tile = w.getTileEntity(p);
				boolean wrenchable = tile instanceof iWrenchable || b instanceof iWrenchable;
				
				int col = wrenchable ? 0x3322FF22 : 0x33FF2222;
				
				Cuboid6[] cbs = MultiHitboxGetter.getCuboidsAt(w, p);
				for(int i = 0; i < cbs.length; ++i)
					renderFilledBlockOverlay(cbs[i].aabb().grow(.01D), p, evt.getPartialTicks(), col);
			} else if(sneakTime > 0)
			{
				EntityPlayer player = Minecraft.getMinecraft().player;
				Vec3d headVec = RayTracer.getCorrectedHeadVec(player);
				Vec3d lookVec = player.getLook(1.0F);
				double reach = 2;
				Vec3d pos = headVec.addVector(lookVec.x * reach, lookVec.y * reach, lookVec.z * reach);
				float alpha = sneakTime / 10F;
				
				BlockPos p = new BlockPos(pos);
				
				GL11.glPushMatrix();
				GL11.glTranslated(p.getX() - player.posX + .3, p.getY() - player.posY + .3, p.getZ() - player.posZ + .3);
				GL11.glScaled(.4, .4, .4);
				
				SimpleBlockRendering sbr = RenderBlocks.getInstance().simpleRenderer;
				sbr.begin();
				sbr.drawBlock(0, 0, 0);
				
				if(ShaderEnderField.useShaders() && ShaderEnderField.endShader == null)
					ShaderEnderField.reloadShader();
				if(ShaderEnderField.useShaders() && ShaderEnderField.endShader != null)
				{
					ShaderEnderField.endShader.freeBindShader();
					ARBShaderObjects.glUniform4fARB(ShaderEnderField.endShader.getUniformLoc("color"), 0.044F, 0.036F, 0.063F, alpha);
				}
				UtilsFX.bindTexture("minecraft", "textures/entity/end_portal.png");
				Tessellator.getInstance().draw();
				if(ShaderEnderField.useShaders())
					ShaderProgram.unbindShader();
				
				GL11.glPopMatrix();
			}
		}
		
		ParticleList.renderExtendedParticles(evt);
	}
	
	@SubscribeEvent
	public void render(RenderPlayerEvent.Post e)
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
		
		double d0 = player.lastTickPosX + (player.posX - player.lastTickPosX) * (double) partialTicks;
		double d1 = player.lastTickPosY + (player.posY - player.lastTickPosY) * (double) partialTicks;
		double d2 = player.lastTickPosZ + (player.posZ - player.lastTickPosZ) * (double) partialTicks;
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
		
		double d0 = player.lastTickPosX + (player.posX - player.lastTickPosX) * (double) partialTicks;
		double d1 = player.lastTickPosY + (player.posY - player.lastTickPosY) * (double) partialTicks;
		double d2 = player.lastTickPosZ + (player.posZ - player.lastTickPosZ) * (double) partialTicks;
		RenderGlobal.renderFilledBox(aabb.offset(-d0, -d1, -d2), ColorHelper.getRed(argb), ColorHelper.getGreen(argb), ColorHelper.getBlue(argb), ColorHelper.getAlpha(argb));
		
		GlStateManager.depthMask(true);
		GlStateManager.enableTexture2D();
		GlStateManager.disableBlend();
		GL11.glPopMatrix();
	}
	
	@SubscribeEvent
	public void renderLiving(RenderLivingEvent.Pre<EntityLivingBase> e)
	{
		if(!(e.getEntity() instanceof AbstractClientPlayer))
			return;
		
		AbstractClientPlayer player = (AbstractClientPlayer) e.getEntity();
		
		// if(!loadedPlayers.contains(player.getGameProfile().getName()))
		// {
		// setPlayerTexture(player, new ResourceLocation("skins/" +
		// player.getGameProfile().getName()));
		// loadedPlayers.add(player.getGameProfile().getName());
		// }
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
								String before = s.substring(0, j);
								float f = Minecraft.getMinecraft().gameSettings.chatOpacity * .9F + .1F;
								int j1 = updateCounter - l.getUpdatedCounter();
								if(j1 < 200 || c.getChatOpen())
								{
									double d0 = (double) j1 / 200.0D;
									d0 = 1.0D - d0;
									d0 = d0 * 10.0D;
									d0 = MathHelper.clamp(d0, 0, 1);
									d0 = d0 * d0;
									int l1 = (int) (255D * d0);
									
									if(c.getChatOpen())
										l1 = 255;
									
									l1 = (int) ((float) l1 * f);
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
	
	private void setPlayerTexture(AbstractClientPlayer player, ResourceLocation texture)
	{
		NetworkPlayerInfo playerInfo = (NetworkPlayerInfo) ObfuscationReflectionHelper.getPrivateValue(AbstractClientPlayer.class, player, 0);
		if(playerInfo == null)
			return;
		Map<MinecraftProfileTexture.Type, ResourceLocation> playerTextures = (Map) ObfuscationReflectionHelper.getPrivateValue(NetworkPlayerInfo.class, playerInfo, 1);
		playerTextures.put(MinecraftProfileTexture.Type.SKIN, texture);
		if(texture == null)
			ObfuscationReflectionHelper.setPrivateValue(NetworkPlayerInfo.class, playerInfo, false, 4);
	}
}