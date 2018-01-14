package com.pengu.hammercore.client.particle.def.lightbolt;

import org.lwjgl.opengl.GL11;

import com.pengu.hammercore.client.particle.api.SimpleParticle;
import com.pengu.hammercore.client.particle.def.lightbolt.LightningBoltWrapper.Segment;
import com.pengu.hammercore.client.utils.UtilsFX;
import com.pengu.hammercore.proxy.ParticleProxy_Client;
import com.pengu.hammercore.utils.ColorHelper;

import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.BufferBuilder;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.entity.Entity;
import net.minecraft.world.World;

public class LightningBolt extends SimpleParticle
{
	private LightningBoltWrapper main;
	
	private int func1 = 1, func2 = 771;
	public int bcol, dcol;
	
	public LightningBolt(World world, final LBVector3 jammervec, final LBVector3 targetvec, final long seed)
	{
		super(world, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0);
		func1 = 0;
		main = new LightningBoltWrapper(world, jammervec, targetvec, seed);
		setupFromMain();
	}
	
	public LightningBolt(World world, final Entity detonator, final Entity target, final long seed)
	{
		super(world, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0);
		func1 = 0;
		main = new LightningBoltWrapper(world, detonator, target, seed);
		setupFromMain();
	}
	
	public LightningBolt(final World world, final Entity detonator, final Entity target, final long seed, final int speed)
	{
		super(world, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0);
		func1 = 0;
		main = new LightningBoltWrapper(world, detonator, target, seed, speed);
		setupFromMain();
	}
	
	public LightningBolt(final World world, final double x1, final double y1, final double z1, final double x, final double y, final double z, final long seed, final int duration, final float multi)
	{
		super(world, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0);
		func1 = 0;
		main = new LightningBoltWrapper(world, x1, y1, z1, x, y, z, seed, duration, multi, 1);
		setupFromMain();
	}
	
	public LightningBolt(final World world, final double x1, final double y1, final double z1, final double x, final double y, final double z, final long seed, final int duration, final float multi, final int speed)
	{
		super(world, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0);
		func1 = 0;
		main = new LightningBoltWrapper(world, x1, y1, z1, x, y, z, seed, duration, multi, speed);
		setupFromMain();
	}
	
	private void setupFromMain()
	{
		particleMaxAge = main.particleMaxAge;
		setPosition(main.start.x, main.start.y, main.start.z);
	}
	
	public void defaultFractal()
	{
		main.defaultFractal();
	}
	
	public void fractal(final int splits, final float amount, final float splitchance, final float splitlength, final float splitangle)
	{
		main.fractal(splits, amount, splitchance, splitlength, splitangle);
	}
	
	public void finalizeBolt()
	{
		main.finalizeBolt();
		ParticleProxy_Client.queueParticleSpawn(this);
	}
	
	public void setBlendFunc(final int funcb, final int funcd)
	{
		func1 = funcb;
		func1 = funcd;
	}
	
	public void setMultiplier(final float m)
	{
		main.multiplier = m;
	}
	
	@Override
	public void onUpdate()
	{
		main.onUpdate();
		if(main.particleAge >= main.particleMaxAge)
			setExpired();
	}
	
	private static LBVector3 getRelativeViewVector(LBVector3 pos)
	{
		Entity r = Minecraft.getMinecraft().getRenderViewEntity();
		return new LBVector3(r.posX - pos.x, r.posY - pos.y, r.posZ - pos.z);
	}
	
	private void renderBolt(Tessellator tessellator, final float partialframe, final float cosyaw, final float cospitch, final float sinyaw, final float cossinpitch, final int pass)
	{
		BufferBuilder b = tessellator.getBuffer();
		
		final LBVector3 playervec = new LBVector3(sinyaw * -cospitch, -cossinpitch / cosyaw, cosyaw * cospitch);
		final float boltage = (main.particleAge >= 0) ? (main.particleAge / main.particleMaxAge) : 0.0f;
		
		float mainalpha = 1;
		
		if(pass == 0)
			mainalpha = (1 - boltage) * .4F;
		else
			mainalpha = 1 - boltage * .5F;
		
		final int renderlength = (int) ((main.particleAge + partialframe + (int) (main.length * 3.0f)) / (int) (main.length * 3.0f) * main.numsegments0);
		
		for(Segment rendersegment : main.segments)
			if(rendersegment.segmentno <= renderlength)
			{
				final float width = 0.03f * (getRelativeViewVector(rendersegment.startpoint.point).length() / 5F + 1F) * (1F + rendersegment.light) * .5F;
				final LBVector3 diff1 = LBVector3.crossProduct(playervec, rendersegment.prevdiff).scale(width / rendersegment.sinprev);
				final LBVector3 diff2 = LBVector3.crossProduct(playervec, rendersegment.nextdiff).scale(width / rendersegment.sinnext);
				final LBVector3 startvec = rendersegment.startpoint.point;
				final LBVector3 endvec = rendersegment.endpoint.point;
				final float rx1 = (float) (startvec.x - LightningBolt.interpPosX);
				final float ry1 = (float) (startvec.y - LightningBolt.interpPosY);
				final float rz1 = (float) (startvec.z - LightningBolt.interpPosZ);
				final float rx2 = (float) (endvec.x - LightningBolt.interpPosX);
				final float ry2 = (float) (endvec.y - LightningBolt.interpPosY);
				final float rz2 = (float) (endvec.z - LightningBolt.interpPosZ);
				
				b.pos(rx2 - diff2.x, ry2 - diff2.y, rz2 - diff2.z).tex(.5, 0).color(particleRed, particleGreen, particleBlue, mainalpha * rendersegment.light).endVertex();
				b.pos(rx1 - diff1.x, ry1 - diff1.y, rz1 - diff1.z).tex(.5, 0).color(particleRed, particleGreen, particleBlue, mainalpha * rendersegment.light).endVertex();
				b.pos(rx1 + diff1.x, ry1 + diff1.y, rz1 + diff1.z).tex(.5, 1).color(particleRed, particleGreen, particleBlue, mainalpha * rendersegment.light).endVertex();
				b.pos(rx2 + diff2.x, ry2 + diff2.y, rz2 + diff2.z).tex(.5, 1).color(particleRed, particleGreen, particleBlue, mainalpha * rendersegment.light).endVertex();
			}
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
		GL11.glEnable(3042);
		
		GL11.glBlendFunc(770, func1);
		particleRed = ColorHelper.getRed(bcol);
		particleGreen = ColorHelper.getRed(bcol);
		particleBlue = ColorHelper.getRed(bcol);
		UtilsFX.bindTexture("textures/misc/p_large.png");
		tessellator.getBuffer().begin(GL11.GL_QUADS, DefaultVertexFormats.POSITION_TEX_COLOR);
		renderBolt(tessellator, partialframe, cosyaw, cospitch, sinyaw, cossinpitch, 0);
		tessellator.draw();
		GL11.glBlendFunc(770, func2);
		particleRed = ColorHelper.getRed(dcol);
		particleGreen = ColorHelper.getRed(dcol);
		particleBlue = ColorHelper.getRed(dcol);
		UtilsFX.bindTexture("textures/misc/p_small.png");
		tessellator.getBuffer().begin(GL11.GL_QUADS, DefaultVertexFormats.POSITION_TEX_COLOR);
		renderBolt(tessellator, partialframe, cosyaw, cospitch, sinyaw, cossinpitch, 1);
		tessellator.draw();
		GL11.glDepthMask(true);
		GL11.glPopMatrix();
	}
	
	public int getRenderPass()
	{
		return 2;
	}
}