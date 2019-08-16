package com.zeitheron.hammercore.specials.Zeitheron;

import java.util.HashMap;
import java.util.Map;

import org.lwjgl.opengl.GL11;

import com.zeitheron.hammercore.ServerHCClientPlayerData;
import com.zeitheron.hammercore.client.HCClientOptions;
import com.zeitheron.hammercore.client.color.PlayerInterpolator;
import com.zeitheron.hammercore.client.render.player.IPlayerModel;
import com.zeitheron.hammercore.client.utils.RenderUtil;
import com.zeitheron.hammercore.client.utils.UtilsFX;
import com.zeitheron.hammercore.client.utils.RenderUtil.PlayerRenderUtil;
import com.zeitheron.hammercore.utils.color.ColorHelper;
import com.zeitheron.hammercore.utils.color.Rainbow;

import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.AbstractClientPlayer;
import net.minecraft.client.model.ModelBase;
import net.minecraft.client.model.ModelDragon;
import net.minecraft.client.model.ModelRenderer;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.entity.RenderPlayer;
import net.minecraft.client.renderer.entity.layers.LayerRenderer;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.math.MathHelper;
import net.minecraftforge.client.event.RenderPlayerEvent;
import net.minecraftforge.common.util.Constants.NBT;
import net.minecraftforge.fml.relauncher.Side;

public class ZeitheronRenderer implements IPlayerModel
{
	private static final String URL_WINGS = "https://gitlab.com/Zeitheron/HammerCore/raw/1.12.2/ZeitheronModel.png";
	private static final String URL_EYES = "https://gitlab.com/Zeitheron/HammerCore/raw/1.12.2/ZeitheronEyes.png";
	private static final String URL_EYES2 = "https://gitlab.com/Zeitheron/HammerCore/raw/1.12.2/ZeitheronEyes2.png";
	
	private final ModelDragon dragon = new ModelDragon(0F);
	private ModelRenderer //
	wing = new ModelRenderer(dragon, "wing"), //
	        spine = new ModelRenderer(dragon, "neck"), //
	        spike = new ModelRenderer(dragon, "neck");
	
	{
		spine.addBox("box", -5.0F, -5.0F, -5.0F, 10, 10, 10);
		spine.addBox("scale", -1.0F, -9.0F, -3.0F, 2, 4, 6);
		
		wing.setRotationPoint(-12.0F, 5.0F, 2.0F);
		wing.addBox("skin", -56.0F, 0.0F, 2.0F, 56, 0, 56);
		
		spike.addBox("scale", -1.0F, -9.0F, -3.0F, 2, 4, 6);
	}
	
	@Override
	public void render(RenderPlayerEvent event)
	{
		EntityPlayer player = event.getEntityPlayer();
		RenderPlayer render = event.getRenderer();
		
		if(player.isSpectator())
			return;
		
		if(!LayerScale.SCALES.containsKey(render))
		{
			LayerScale value = new LayerScale(render, this);
			render.addLayer(value);
			LayerScale.SCALES.put(render, value);
		}
	}
	
	private void drawHead(AbstractClientPlayer player, float limbSwing, float limbSwingAmount, float partialTicks, float ageInTicks, float netHeadYaw, float headPitch, float scale)
	{
		double deg = 60;
		
		if(player.capabilities.isFlying)
			deg = 60 - (Math.sin((player.ticksExisted + partialTicks) / 2) + 1) * 10;
		else if(player.isSneaking())
			deg = 50 + (Math.sin((player.ticksExisted + partialTicks) / 12) + 1) * .5F;
		else
			deg = 50 + (Math.sin((player.ticksExisted + partialTicks) / 8) + 1);
		
		RenderPlayer rp = Minecraft.getMinecraft().getRenderManager().getSkinMap().get("default");
		
		if(UtilsFX.bindTextureURL(URL_WINGS))
		{
			ModelBase.copyModelAngles(rp.getMainModel().bipedHead, spike);
			
			float f = player.prevRotationYawHead + (player.rotationYawHead - player.prevRotationYawHead) * partialTicks - (player.prevRenderYawOffset + (player.renderYawOffset - player.prevRenderYawOffset) * partialTicks);
			float f1 = player.prevRotationPitch + (player.rotationPitch - player.prevRotationPitch) * partialTicks;
			
			GlStateManager.pushMatrix();
			
			for(int i = 0; i < 2; ++i)
			{
				GlStateManager.pushMatrix();
				GlStateManager.translate(0, player.isSneaking() ? .2 : 0, 0);
				GlStateManager.rotate(f, 0.0F, 1.0F, 0.0F);
				GlStateManager.rotate(f1, 1.0F, 0.0F, 0.0F);
				GlStateManager.translate(.18F * (i * 2 - 1), 0, 0);
				GlStateManager.translate(0, -.32F, 0);
				GlStateManager.rotate(-f1, 1.0F, 0.0F, 0.0F);
				GlStateManager.rotate(-f, 0.0F, 1.0F, 0.0F);
				float f2 = 0.03F;
				GlStateManager.scale(f2, f2, f2);
				spike.render(1F);
				GlStateManager.popMatrix();
			}
			
			GlStateManager.popMatrix();
			
			f = player.prevRotationYaw + (player.rotationYaw - player.prevRotationYaw) * partialTicks - (player.prevRenderYawOffset + (player.renderYawOffset - player.prevRenderYawOffset) * partialTicks);
			f1 = player.prevRotationPitch + (player.rotationPitch - player.prevRotationPitch) * partialTicks;
			
			GL11.glPushMatrix();
			
			GlStateManager.translate(0, player.isSneaking() ? .15 : 0, 0);
			
			GL11.glTranslated(.5, 1, .5);
			GL11.glRotated(180, 1, 0, 0);
			GL11.glRotated(180, 0, 1, 0);
			GL11.glTranslated(.5, .15, -.5);
			
			GL11.glPushMatrix();
			
			if(player.isSneaking())
			{
				GL11.glTranslated(0, 0, .5);
				GL11.glRotated(30, -1, 0, 0);
			}
			
			GL11.glPushMatrix();
			GL11.glTranslated(0, 0, .05);
			GL11.glRotated(deg, 0, 1, 0);
			GL11.glRotatef(270, 0, 0, 1);
			GL11.glTranslated(0, -.075, 0);
			wing.render(.0128F);
			GL11.glPopMatrix();
			
			GL11.glPushMatrix();
			GL11.glTranslated(0, 0, .05);
			GL11.glRotated(-deg, 0, 1, 0);
			GL11.glRotatef(270, 0, 0, 1);
			GL11.glTranslated(0, -.053, 0);
			wing.render(.0128F);
			GL11.glPopMatrix();
			
			GL11.glPopMatrix();
			
			//
			
			GL11.glPushMatrix();
			if(player.isSneaking())
			{
				GL11.glTranslated(0, 0, .45);
				GL11.glRotated(30, -1, 0, 0);
			}
			
			float tailDeg = (float) (Math.cos(Math.toRadians(System.currentTimeMillis() % 36000L / 10L)) * 1);
			
			GL11.glTranslated(0, .24, 0);
			GL11.glRotated(180, 1, 0, 0);
			for(int i = 0; i < 10; ++i)
			{
				float calc = (float) Math.sin((player.ticksExisted + partialTicks - i) / 10);
				float displace = (float) Math.sin((player.ticksExisted + partialTicks - i) / 10);
				GL11.glTranslated(displace / 200F, calc / 200F, -.09);
				GL11.glRotated(9 + tailDeg, 1, 0, 0);
				spine.render(.01F);
			}
			GL11.glPopMatrix();
			
			//
			
			ModelBase.copyModelAngles(spine, spike);
			
			//
			
			GL11.glPushMatrix();
			if(player.isSneaking())
			{
				GL11.glTranslated(0, 0, .45);
				GL11.glRotated(30, -1, 0, 0);
			}
			GL11.glTranslated(0, .45, .35);
			for(int i = 0; i < 2; ++i)
			{
				GL11.glPushMatrix();
				GL11.glScaled(.03, .03, .03);
				GL11.glRotated(90, 1, 0, 0);
				spike.render(1F);
				GL11.glPopMatrix();
				GL11.glTranslated(0, .25, 0);
			}
			GL11.glPopMatrix();
			
			GL11.glPopMatrix();
		}
		
		// ModelBase.copyModelAngles(rp.getMainModel().bipedHead, spike);
		//
		// for(int i = 0; i < 2; ++i)
		// {
		// f = player.prevRotationYaw + (player.rotationYaw -
		// player.prevRotationYaw) * partialTicks - (player.prevRenderYawOffset
		// + (player.renderYawOffset - player.prevRenderYawOffset) *
		// partialTicks);
		// f1 = player.prevRotationPitch + (player.rotationPitch -
		// player.prevRotationPitch) * partialTicks;
		// GlStateManager.pushMatrix();
		// GlStateManager.translate(0, player.isSneaking() ? .2 : 0, 0);
		// GlStateManager.rotate(f, 0.0F, 1.0F, 0.0F);
		// GlStateManager.rotate(f1, 1.0F, 0.0F, 0.0F);
		// GlStateManager.translate(0, .135F * (float) (i * 2 - 1) + .02F, 0);
		// GlStateManager.translate(0, 0, .2F);
		// GlStateManager.rotate(-f1, 1.0F, 0.0F, 0.0F);
		// GlStateManager.rotate(-f, 0.0F, 1.0F, 0.0F);
		//
		// float f2 = 0.03F;
		// GlStateManager.scale(f2, f2 * 1.3, f2);
		// spike.render(1F);
		// GlStateManager.popMatrix();
		// }
	}
	
	public static class LayerScale implements LayerRenderer<AbstractClientPlayer>
	{
		private static Map<RenderPlayer, LayerScale> SCALES = new HashMap<>();
		
		private final RenderPlayer playerRenderer;
		private final ZeitheronRenderer player;
		
		public LayerScale(RenderPlayer playerRenderer, ZeitheronRenderer player)
		{
			this.playerRenderer = playerRenderer;
			this.player = player;
		}
		
		@Override
		public void doRenderLayer(AbstractClientPlayer entitylivingbaseIn, float limbSwing, float limbSwingAmount, float partialTicks, float ageInTicks, float netHeadYaw, float headPitch, float scale)
		{
			if("Zeitheron".equals(entitylivingbaseIn.getName()) && entitylivingbaseIn.hasSkin() && !entitylivingbaseIn.isInvisible())
			{
				HCClientOptions clopts = ServerHCClientPlayerData.DATAS.get(Side.CLIENT).getOptionsForPlayer(entitylivingbaseIn);
				
				if(clopts.renderSpecial)
					player.drawHead(entitylivingbaseIn, limbSwing, limbSwingAmount, partialTicks, ageInTicks, netHeadYaw, headPitch, scale);
				
				int eyes = clopts.getCustomData().getInteger("RenderEyes");
				
				if(eyes != 2)
				{
					NBTTagCompound interp = null;
					if(clopts.customData != null && clopts.customData.hasKey("EyeColor", NBT.TAG_COMPOUND))
						interp = clopts.customData.getCompoundTag("EyeColor");
					int rendered = interp != null && interp.hasKey("RainbowCycle", NBT.TAG_INT) ? Rainbow.doIt(0, interp.getInteger("RainbowCycle") * 50) : PlayerInterpolator.getRendered(entitylivingbaseIn, interp);
					
					GlStateManager.pushMatrix();
					
					if(UtilsFX.bindTextureURL(eyes == 0 ? URL_EYES2 : URL_EYES))
					{
						ColorHelper.glColor1i(rendered);
						
						GlStateManager.disableLighting();
						GlStateManager.pushMatrix();
						GlStateManager.translate(0, entitylivingbaseIn.isSneaking() ? -.75 : 0, 0);
						GlStateManager.scale(1.01F, 1.01F, 1.01F);
						playerRenderer.getMainModel().bipedHead.render(1F);
						GlStateManager.popMatrix();
						GlStateManager.enableLighting();
						
						GlStateManager.color(1F, 1F, 1F);
					}
					
					GlStateManager.popMatrix();
				}
			}
		}
		
		@Override
		public boolean shouldCombineTextures()
		{
			return false;
		}
	}
}