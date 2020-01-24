package com.zeitheron.hammercore.client.particle.api;

import static net.minecraft.client.renderer.tileentity.TileEntityRendererDispatcher.staticPlayerX;
import static net.minecraft.client.renderer.tileentity.TileEntityRendererDispatcher.staticPlayerY;
import static net.minecraft.client.renderer.tileentity.TileEntityRendererDispatcher.staticPlayerZ;

import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import com.zeitheron.hammercore.client.particle.FXGroupRenderer;
import com.zeitheron.hammercore.client.particle.ParticleGrouped;

import net.minecraft.client.Minecraft;
import net.minecraft.client.particle.Particle;
import net.minecraft.client.renderer.ActiveRenderInfo;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraftforge.client.event.RenderWorldLastEvent;

public class ParticleList
{
	private static final Set<IParticleGetter> getters = new HashSet<>();
	
	private static final Set<Particle> vanillaParticleSet = new HashSet<>();
	private static final List<Particle> vanillaParticleList = new ArrayList<>();
	
	private static final List<IRenderedParticle> renderedParticleList = new ArrayList<>();
	
	private static final List<ParticleGrouped> groupedParticleList = new ArrayList<>();
	
	static
	{
		addGetter(new DefaultParticleGetter());
	}
	
	public static void addGetter(IParticleGetter getter)
	{
		getters.add(getter);
	}
	
	public static List<Particle> getParticlesWithinAABB(AxisAlignedBB bounds)
	{
		List<Particle> particles = new ArrayList<>();
		List<Particle> ps = getParticlesList();
		for(int i = 0; i < ps.size(); ++i)
		{
			Particle p = ps.get(i);
			AxisAlignedBB pbb = p.getBoundingBox();
			if(pbb != null && pbb.intersects(bounds))
				particles.add(p);
		}
		return particles;
	}
	
	public static void refreshParticles()
	{
		vanillaParticleSet.clear();
		vanillaParticleList.clear();
		renderedParticleList.clear();
		
		if(Minecraft.getMinecraft().player == null || Minecraft.getMinecraft().world == null)
			return;
		
		for(IParticleGetter getter : getters)
			getter.addParticles(vanillaParticleSet);
		
		vanillaParticleList.addAll(vanillaParticleSet);
		for(int i = 0; i < vanillaParticleList.size(); ++i)
		{
			Particle p = vanillaParticleList.get(i);
			if(p instanceof IRenderedParticle)
				renderedParticleList.add((IRenderedParticle) p);
		}
	}
	
	public static void renderExtendedParticles(RenderWorldLastEvent evt)
	{
		EntityPlayer entityIn = Minecraft.getMinecraft().player;
		ActiveRenderInfo.updateRenderInfo(entityIn, Minecraft.getMinecraft().gameSettings.thirdPersonView == 2);
		
		FXGroupRenderer.INSTANCE.render(evt);
		
		for(int i = 0; i < renderedParticleList.size(); ++i)
		{
			Particle p = (Particle) renderedParticleList.get(i);
			IRenderedParticle rp = renderedParticleList.get(i);
			
			if(rp != null && rp.isRendered())
			{
				rp.setRendered();
				
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
				
				rp.doRenderParticle(p.posX - staticPlayerX, p.posY - staticPlayerY, p.posZ - staticPlayerZ, partialTicks, rotationX, rotationZ, rotationYZ, rotationXY, rotationXZ);
			}
		}
	}
	
	public static Set<Particle> getParticlesSet()
	{
		return vanillaParticleSet;
	}
	
	public static List<Particle> getParticlesList()
	{
		return vanillaParticleList;
	}
	
	public static List<IRenderedParticle> getRenderedParticleList()
	{
		return renderedParticleList;
	}
	
	private static class DefaultParticleGetter implements IParticleGetter
	{
		@Override
		public void addParticles(Set<Particle> particlesSet)
		{
			try
			{
				ArrayDeque<Particle>[][] particles = Minecraft.getMinecraft().effectRenderer.fxLayers;
				for(ArrayDeque<Particle>[] layer : particles)
					for(ArrayDeque<Particle> deque : layer)
						for(Particle p : deque)
							if(p instanceof ParticleGrouped)
							{
								// Move grouped particles to custom render engine
								FXGroupRenderer.INSTANCE.addParticle((ParticleGrouped) p);
								deque.remove(p);
							}
							else
								particlesSet.add(p);
			} catch(Throwable err)
			{
			}
		}
	}
}