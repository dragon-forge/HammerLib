package com.zeitheron.hammercore.client.particle;

import static net.minecraft.client.renderer.tileentity.TileEntityRendererDispatcher.staticPlayerX;
import static net.minecraft.client.renderer.tileentity.TileEntityRendererDispatcher.staticPlayerY;
import static net.minecraft.client.renderer.tileentity.TileEntityRendererDispatcher.staticPlayerZ;

import java.util.ArrayList;
import java.util.List;

import org.lwjgl.opengl.GL11;

import net.minecraft.client.Minecraft;
import net.minecraft.client.particle.Particle;
import net.minecraft.client.renderer.ActiveRenderInfo;
import net.minecraft.client.renderer.BufferBuilder;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.client.event.RenderWorldLastEvent;

public class FXGroup
{
	public final List<ParticleGrouped> groupedParticles = new ArrayList<>();
	public final ResourceLocation texture;
	public final FXGroupRenderer renderer;
	
	public FXGroup(FXGroupRenderer renderer, ResourceLocation texture)
	{
		this.texture = texture;
		this.renderer = renderer;
	}
	
	public void update()
	{
		for(int i = 0; i < groupedParticles.size(); ++i)
		{
			ParticleGrouped p = groupedParticles.get(i);
			if(!p.isAlive())
			{
				groupedParticles.remove(i);
				continue;
			}
			p.onUpdate();
		}
	}
	
	public void render(RenderWorldLastEvent evt)
	{
		EntityPlayer entityIn = Minecraft.getMinecraft().player;
		
		BufferBuilder buffer = Tessellator.getInstance().getBuffer();
		buffer.begin(GL11.GL_QUADS, DefaultVertexFormats.PARTICLE_POSITION_TEX_COLOR_LMAP);
		
		for(int i = 0; i < groupedParticles.size(); ++i)
		{
			ParticleGrouped p = groupedParticles.get(i);
			if(!p.isAlive())
			{
				groupedParticles.remove(i);
				continue;
			}
			
			if(!p.getTexture().equals(texture))
			{
				renderer.addParticle(p);
				groupedParticles.remove(i);
				continue;
			}
			
			float rotationX = ActiveRenderInfo.getRotationX();
			float rotationZ = ActiveRenderInfo.getRotationXZ();
			float rotationYZ = ActiveRenderInfo.getRotationZ();
			float rotationXY = ActiveRenderInfo.getRotationYZ();
			float rotationXZ = ActiveRenderInfo.getRotationXY();
			float partialTicks = evt.getPartialTicks();
			Particle.interpPosX = entityIn.lastTickPosX + (entityIn.posX - entityIn.lastTickPosX) * partialTicks;
			Particle.interpPosY = entityIn.lastTickPosY + (entityIn.posY - entityIn.lastTickPosY) * partialTicks;
			Particle.interpPosZ = entityIn.lastTickPosZ + (entityIn.posZ - entityIn.lastTickPosZ) * partialTicks;
			Particle.cameraViewDir = entityIn.getLook(partialTicks);
			GlStateManager.enableBlend();
			GlStateManager.blendFunc(GlStateManager.SourceFactor.SRC_ALPHA, GlStateManager.DestFactor.ONE_MINUS_SRC_ALPHA);
			GlStateManager.alphaFunc(516, 0.003921569F);
			p.doRenderParticle(buffer, p.posX - staticPlayerX, p.posY - staticPlayerY, p.posZ - staticPlayerZ, partialTicks, rotationX, rotationZ, rotationYZ, rotationXY, rotationXZ);
		}
		
		Tessellator.getInstance().draw();
	}
	
	public boolean isAlive()
	{
		return !groupedParticles.isEmpty();
	}
}