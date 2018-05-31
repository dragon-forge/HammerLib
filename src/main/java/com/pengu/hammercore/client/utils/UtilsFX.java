package com.pengu.hammercore.client.utils;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.lwjgl.opengl.GL11;

import com.pengu.hammercore.client.texture.TexLocUploader;
import com.pengu.hammercore.client.texture.TextureUtils;
import com.pengu.hammercore.common.utils.QuadHelper;
import com.pengu.hammercore.utils.ColorHelper;
import com.pengu.hammercore.vec.Vector3;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.renderer.ActiveRenderInfo;
import net.minecraft.client.renderer.BufferBuilder;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.RenderItem;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.Vec3d;

public class UtilsFX
{
	private static final ResourceLocation PARTICLE_TEXTURES = new ResourceLocation("textures/particle/particles.png");
	
	private static Map<String, ResourceLocation> textures = new HashMap<>();
	
	public static void bindTextureURL(String url)
	{
		String withoutHTTP = url.substring(url.indexOf("://") + 3);
		String protocol = url.substring(0, url.indexOf("://"));
		ResourceLocation loca = new ResourceLocation("url_" + protocol, withoutHTTP);
		if(!TexLocUploader.cleanup.contains(loca))
		{
			final String lpa = loca.toString();
			TexLocUploader.upload(loca, url);
			TexLocUploader.cleanupAfterLogoff(loca, () -> textures.remove(lpa));
			textures.put(lpa, loca);
		}
		bindTexture(loca);
	}
	
	public static void bindTexture(String tex)
	{
		if(tex.startsWith("http"))
			bindTextureURL(tex);
		else
			bindTexture("hammercore", tex);
	}
	
	public static void bindTexture(ResourceLocation loca)
	{
		bindTexture(loca.getResourceDomain(), loca.getResourcePath());
	}
	
	public static void bindTexture(String dom, String tex)
	{
		if(textures.containsKey(dom + ":" + tex))
		{
			Minecraft.getMinecraft().getTextureManager().bindTexture(textures.get(dom + ":" + tex));
			return;
		}
		ResourceLocation value = new ResourceLocation(dom, tex);
		textures.put(dom + ":" + tex, value);
		Minecraft.getMinecraft().getTextureManager().bindTexture(value);
	}
	
	public static void drawTexturedQuadFull(int par1, int par2, double zLevel)
	{
		Tessellator var9 = Tessellator.getInstance();
		BufferBuilder b = var9.getBuffer();
		b.begin(GL11.GL_QUADS, DefaultVertexFormats.POSITION_TEX);
		b.pos(par1 + 0, par2 + 16, zLevel).tex(0.0, 1.0).endVertex();
		b.pos(par1 + 16, par2 + 16, zLevel).tex(1.0, 1.0).endVertex();
		b.pos(par1 + 16, par2 + 0, zLevel).tex(1.0, 0.0).endVertex();
		b.pos(par1 + 0, par2 + 0, zLevel).tex(0.0, 0.0).endVertex();
		var9.draw();
	}
	
	public static void drawCustomTooltip(GuiScreen gui, RenderItem itemRenderer, FontRenderer fr, List<String> var4, int par2, int par3, int subTipColor)
	{
		GL11.glDisable(32826);
		GL11.glDisable(2929);
		GlStateManager.enableBlend();
		
		if(!var4.isEmpty())
		{
			int var5 = 0;
			
			for(String var7 : var4)
				var5 = Math.max(var5, fr.getStringWidth(var7));
			
			int var15 = par2 + 12;
			int var16 = par3 - 12;
			
			int var9 = 8;
			if(var4.size() > 1)
				var9 += 2 + (var4.size() - 1) * 10;
			
			itemRenderer.zLevel = 300.0f;
			int var10 = -267386864;
			
			RenderUtil.drawGradientRect(var15 - 3, var16 - 4, var5 + 3, 1, var10, var10);
			RenderUtil.drawGradientRect(var15 - 3, var16 + var9 + 3, var5 + 3, 1, var10, var10);
			RenderUtil.drawGradientRect(var15 - 3, var16 - 3, var5 + 3, var9 + 6, var10, var10);
			RenderUtil.drawGradientRect(var15 - 4, var16 - 3, 1, var9 + 6, var10, var10);
			RenderUtil.drawGradientRect(var15 + var5, var16 - 3, 1, var9 + 6, var10, var10);
			
			int var11 = 1347420415;
			int var12 = (var11 & 16711422) >> 1 | var11 & -16777216;
			
			RenderUtil.drawGradientRect(var15 - 3, var16 - 2, 1, var9 + 5, var11, var12);
			RenderUtil.drawGradientRect(var15 + var5 - 1, var16 - 3 + 1, 1, var9 + 5, var11, var12);
			RenderUtil.drawGradientRect(var15 - 3, var16 - 3, var5 + 3, 1, var11, var11);
			RenderUtil.drawGradientRect(var15 - 2, var16 + var9 + 2, var5 + 1, 1, var12, var12);
			
			for(int var13 = 0; var13 < var4.size(); ++var13)
			{
				String var14 = var4.get(var13);
				var14 = var13 == 0 ? "\u00a7" + Integer.toHexString(subTipColor) + var14 : "\u00a77" + var14;
				fr.drawStringWithShadow(var14, var15, var16, -1);
				if(var13 == 0)
					var16 += 2;
				var16 += 10;
			}
		}
		
		itemRenderer.zLevel = 0.0f;
		GL11.glEnable(2929);
		GlStateManager.enableBlend();
	}
	
	public static ResourceLocation getMCParticleTexture()
	{
		return PARTICLE_TEXTURES;
	}
	
	public static void drawBeam(Vec3d S, Vec3d E, Vec3d P, float width, int bright)
	{
		drawBeam(S, E, P, width, bright, 1.0F, 1.0F, 1.0F, 1.0F);
	}
	
	public static void drawBeam(Vec3d S, Vec3d E, Vec3d P, float width, int bright, float r, float g, float b, float a)
	{
		Vec3d PS = Sub(S, P);
		Vec3d SE = Sub(E, S);
		
		Vec3d normal = Cross(PS, SE);
		normal = normal.normalize();
		
		Vec3d half = Mul(normal, width);
		Vec3d p1 = Add(S, half);
		Vec3d p2 = Sub(S, half);
		Vec3d p3 = Add(E, half);
		Vec3d p4 = Sub(E, half);
		
		drawQuad(Tessellator.getInstance().getBuffer(), p1, p3, p4, p2, bright, r, g, b, a);
	}
	
	public static void drawQuad(BufferBuilder buf, Vec3d p1, Vec3d p2, Vec3d p3, Vec3d p4, int bright, float r, float g, float b, float a)
	{
		int j = bright >> 16 & 0xFFFF;
		int k = bright & 0xFFFF;
		buf.pos(p1.x, p1.y, p1.z).tex(0.0D, 0.0D).lightmap(j, k).color(r, g, b, a).endVertex();
		buf.pos(p2.x, p2.y, p2.z).tex(1.0D, 0.0D).lightmap(j, k).color(r, g, b, a).endVertex();
		buf.pos(p3.x, p3.y, p3.z).tex(1.0D, 1.0D).lightmap(j, k).color(r, g, b, a).endVertex();
		buf.pos(p4.x, p4.y, p4.z).tex(0.0D, 1.0D).lightmap(j, k).color(r, g, b, a).endVertex();
	}
	
	private static Vec3d Cross(Vec3d a, Vec3d b)
	{
		double xCoord = a.y * b.z - a.z * b.y;
		double yCoord = a.z * b.x - a.x * b.z;
		double zCoord = a.x * b.y - a.y * b.x;
		return new Vec3d(xCoord, yCoord, zCoord);
	}
	
	public static Vec3d Sub(Vec3d a, Vec3d b)
	{
		return new Vec3d(a.x - b.x, a.y - b.y, a.z - b.z);
	}
	
	private static Vec3d Add(Vec3d a, Vec3d b)
	{
		return new Vec3d(a.x + b.x, a.y + b.y, a.z + b.z);
	}
	
	private static Vec3d Mul(Vec3d a, float f)
	{
		return new Vec3d(a.x * f, a.y * f, a.z * f);
	}
	
	public static void renderFacingStrip(double px, double py, double pz, float angle, float scale, float alpha, int frames, int strip, int frame, float partialTicks, int color)
	{
		if(Minecraft.getMinecraft().getRenderViewEntity() instanceof EntityPlayer)
		{
			Tessellator tessellator = Tessellator.getInstance();
			float arX = ActiveRenderInfo.getRotationX();
			float arZ = ActiveRenderInfo.getRotationZ();
			float arYZ = ActiveRenderInfo.getRotationYZ();
			float arXY = ActiveRenderInfo.getRotationXY();
			float arXZ = ActiveRenderInfo.getRotationXZ();
			EntityPlayer player = (EntityPlayer) Minecraft.getMinecraft().getRenderViewEntity();
			double iPX = player.prevPosX + (player.posX - player.prevPosX) * partialTicks;
			double iPY = player.prevPosY + (player.posY - player.prevPosY) * partialTicks;
			double iPZ = player.prevPosZ + (player.posZ - player.prevPosZ) * partialTicks;
			BufferBuilder buf = tessellator.getBuffer();
			buf.begin(GL11.GL_QUADS, DefaultVertexFormats.POSITION_TEX_COLOR);
			Vector3 v1 = new Vector3(-arX * scale - arYZ * scale, -arXZ * scale, -arZ * scale - arXY * scale);
			Vector3 v2 = new Vector3(-arX * scale + arYZ * scale, arXZ * scale, -arZ * scale + arXY * scale);
			Vector3 v3 = new Vector3(arX * scale + arYZ * scale, arXZ * scale, arZ * scale + arXY * scale);
			Vector3 v4 = new Vector3(arX * scale - arYZ * scale, -arXZ * scale, arZ * scale - arXY * scale);
			if(angle != 0.0F)
			{
				Vector3 f2 = new Vector3(iPX, iPY, iPZ);
				Vector3 f3 = new Vector3(px, py, pz);
				Vector3 f4 = f2.subtract(f3).normalize();
				QuadHelper.setAxis(f4, angle).rotate(v1);
				QuadHelper.setAxis(f4, angle).rotate(v2);
				QuadHelper.setAxis(f4, angle).rotate(v3);
				QuadHelper.setAxis(f4, angle).rotate(v4);
			}
			float f21 = (float) frame / (float) frames;
			float f31 = (float) (frame + 1) / (float) frames;
			float f41 = (float) strip / (float) frames;
			float f5 = (strip + 1.0F) / frames;
			buf.pos(px + v1.x, py + v1.y, pz + v1.z).tex(f31, f5).color(ColorHelper.getRed(color), ColorHelper.getGreen(color), ColorHelper.getBlue(color), alpha).endVertex();
			buf.pos(px + v2.x, py + v2.y, pz + v2.z).tex(f31, f41).color(ColorHelper.getRed(color), ColorHelper.getGreen(color), ColorHelper.getBlue(color), alpha).endVertex();
			buf.pos(px + v3.x, py + v3.y, pz + v3.z).tex(f21, f41).color(ColorHelper.getRed(color), ColorHelper.getGreen(color), ColorHelper.getBlue(color), alpha).endVertex();
			buf.pos(px + v4.x, py + v4.y, pz + v4.z).tex(f21, f5).color(ColorHelper.getRed(color), ColorHelper.getGreen(color), ColorHelper.getBlue(color), alpha).endVertex();
			tessellator.draw();
		}
	}
}