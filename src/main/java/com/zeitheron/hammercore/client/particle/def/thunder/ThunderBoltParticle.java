package com.zeitheron.hammercore.client.particle.def.thunder;

import org.lwjgl.opengl.GL11;

import com.zeitheron.hammercore.client.particle.api.SimpleParticle;
import com.zeitheron.hammercore.client.particle.def.thunder.ThunderCore.Segment;
import com.zeitheron.hammercore.client.utils.UtilsFX;
import com.zeitheron.hammercore.net.internal.thunder.Thunder;
import com.zeitheron.hammercore.proxy.ParticleProxy_Client;
import com.zeitheron.hammercore.utils.color.ColorHelper;

import net.minecraft.client.Minecraft;
import net.minecraft.client.particle.Particle;
import net.minecraft.client.renderer.BufferBuilder;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.entity.Entity;
import net.minecraft.world.World;

public class ThunderBoltParticle extends SimpleParticle
{
	private ThunderCore main;
	
	public Thunder.Layer core = new Thunder.Layer(), aura = new Thunder.Layer();
	
	public ThunderBoltParticle(final World world, final double x1, final double y1, final double z1, final double x, final double y, final double z, final long seed, final int duration, final float multi)
	{
		super(world, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0);
		main = new ThunderCore(world, x1, y1, z1, x, y, z, seed, duration, multi, 1);
		setupFromMain();
	}
	
	public ThunderBoltParticle(final World world, final double x1, final double y1, final double z1, final double x, final double y, final double z, final long seed, final int duration, final float multi, final int speed)
	{
		super(world, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0);
		main = new ThunderCore(world, x1, y1, z1, x, y, z, seed, duration, multi, speed);
		setupFromMain();
	}
	
	public ThunderBoltParticle(World world, final Entity detonator, final Entity target, final long seed)
	{
		super(world, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0);
		main = new ThunderCore(world, detonator, target, seed);
		setupFromMain();
	}
	
	public ThunderBoltParticle(final World world, final Entity detonator, final Entity target, final long seed, final int speed)
	{
		super(world, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0);
		main = new ThunderCore(world, detonator, target, seed, speed);
		setupFromMain();
	}
	
	public ThunderBoltParticle(World world, final ThVector3 jammervec, final ThVector3 targetvec, final long seed)
	{
		super(world, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0);
		main = new ThunderCore(world, jammervec, targetvec, seed);
		setupFromMain();
	}
	
	public void defaultFractal()
	{
		main.defaultFractal();
	}
	
	public void defaultFractal(int splits, float baseAngle)
	{
		main.defaultFractal(splits, baseAngle);
	}
	
	@Override
	public void doRenderParticle(double x, double y, double z, float partialframe, float cosyaw, float cospitch, float sinyaw, float sinsinpitch, float cossinpitch)
	{
		Entity renderentity = Minecraft.getMinecraft().getRenderViewEntity();
		int visibleDistance = 100;
		if(!Minecraft.getMinecraft().gameSettings.fancyGraphics)
			visibleDistance = 50;
		
		Tessellator tessellator = Tessellator.getInstance();
		
		GL11.glPushMatrix();
		GL11.glDepthMask(false);
		GL11.glEnable(GL11.GL_BLEND);
		
		if(aura.active)
		{
			GL11.glBlendFunc(770, aura.blendFunc);
			int rgb = aura.color;
			particleRed = ColorHelper.getRed(rgb);
			particleGreen = ColorHelper.getGreen(rgb);
			particleBlue = ColorHelper.getBlue(rgb);
			UtilsFX.bindTexture("hammercore", "textures/misc/p_large.png");
			tessellator.getBuffer().begin(GL11.GL_QUADS, DefaultVertexFormats.POSITION_TEX_COLOR);
			renderBolt(tessellator, partialframe, cosyaw, cospitch, sinyaw, cossinpitch, 0);
			tessellator.draw();
		}
		
		if(core.active)
		{
			GL11.glBlendFunc(770, core.blendFunc);
			int rgb = core.color;
			particleRed = ColorHelper.getRed(rgb);
			particleGreen = ColorHelper.getGreen(rgb);
			particleBlue = ColorHelper.getBlue(rgb);
			UtilsFX.bindTexture("hammercore", "textures/misc/p_small.png");
			tessellator.getBuffer().begin(GL11.GL_QUADS, DefaultVertexFormats.POSITION_TEX_COLOR);
			renderBolt(tessellator, partialframe, cosyaw, cospitch, sinyaw, cossinpitch, 1);
			tessellator.draw();
		}
		
		GL11.glDepthMask(true);
		GL11.glPopMatrix();
	}
	
	public void finalizeBolt()
	{
		main.finalizeBolt();
		setupFromMain();
		ParticleProxy_Client.queueParticleSpawn(this);
	}
	
	public void fractal(final int splits, final float amount, final float splitchance, final float splitlength, final float splitangle)
	{
		main.fractal(splits, amount, splitchance, splitlength, splitangle);
	}
	
	public int getRenderPass()
	{
		return 2;
	}
	
	@Override
	public void onUpdate()
	{
		main.onUpdate();
		if(main.particleAge >= main.particleMaxAge)
			setExpired();
	}
	
	private void renderBolt(Tessellator tessellator, final float partialframe, final float cosyaw, final float cospitch, final float sinyaw, final float cossinpitch, final int pass)
	{
		BufferBuilder b = tessellator.getBuffer();
		
		final ThVector3 playervec = new ThVector3(sinyaw * -cospitch, -cossinpitch / cosyaw, cosyaw * cospitch);
		final float boltage = main.particleAge >= 0 ? main.particleAge / main.particleMaxAge : 0.0f;
		
		float mainalpha = 1;
		
		if(pass == 0)
			mainalpha = (1 - boltage) * .4F;
		else
			mainalpha = 1 - boltage * .5F;
		
		int renderlength = (int) ((main.particleAge + partialframe + (int) (main.length * 3.0f)) / (int) (main.length * 3.0f) * main.numsegments0);
		
		if(renderlength >= main.numsegments0)
		{
			float progress = 1 - (main.particleAge + partialframe) / (float) main.particleMaxAge;
			progress *= .75F;
			mainalpha *= progress;
		}
		
		for(Segment rendersegment : main.segments)
			if(rendersegment.segmentno <= renderlength)
			{
				final float width = 0.03f * (getRelativeViewVector(rendersegment.startpoint.point).length() / 5F + 1F) * (1F + rendersegment.light) * .5F;
				final ThVector3 diff1 = ThVector3.crossProduct(playervec, rendersegment.prevdiff).scale(width / rendersegment.sinprev);
				final ThVector3 diff2 = ThVector3.crossProduct(playervec, rendersegment.nextdiff).scale(width / rendersegment.sinnext);
				final ThVector3 startvec = rendersegment.startpoint.point;
				final ThVector3 endvec = rendersegment.endpoint.point;
				final float rx1 = (float) (startvec.x - Particle.interpPosX);
				final float ry1 = (float) (startvec.y - Particle.interpPosY);
				final float rz1 = (float) (startvec.z - Particle.interpPosZ);
				final float rx2 = (float) (endvec.x - Particle.interpPosX);
				final float ry2 = (float) (endvec.y - Particle.interpPosY);
				final float rz2 = (float) (endvec.z - Particle.interpPosZ);
				
				b.pos(rx2 - diff2.x, ry2 - diff2.y, rz2 - diff2.z).tex(.5, 0).color(particleRed, particleGreen, particleBlue, mainalpha * rendersegment.light).endVertex();
				b.pos(rx1 - diff1.x, ry1 - diff1.y, rz1 - diff1.z).tex(.5, 0).color(particleRed, particleGreen, particleBlue, mainalpha * rendersegment.light).endVertex();
				b.pos(rx1 + diff1.x, ry1 + diff1.y, rz1 + diff1.z).tex(.5, 1).color(particleRed, particleGreen, particleBlue, mainalpha * rendersegment.light).endVertex();
				b.pos(rx2 + diff2.x, ry2 + diff2.y, rz2 + diff2.z).tex(.5, 1).color(particleRed, particleGreen, particleBlue, mainalpha * rendersegment.light).endVertex();
			} else
				break;
	}
	
	public void setMultiplier(final float m)
	{
		main.multiplier = m;
	}
	
	private void setupFromMain()
	{
		particleMaxAge = main.particleMaxAge;
		setPosition(main.start.x, main.start.y, main.start.z);
	}
	
	private static ThVector3 getRelativeViewVector(ThVector3 pos)
	{
		Entity r = Minecraft.getMinecraft().getRenderViewEntity();
		return new ThVector3(r.posX - pos.x, r.posY - pos.y, r.posZ - pos.z);
	}
}