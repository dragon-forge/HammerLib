package com.zeitheron.hammercore.client.particle.def;

import java.util.ArrayList;
import java.util.Random;

import org.lwjgl.opengl.GL11;

import com.zeitheron.hammercore.client.particle.api.SimpleParticle;
import com.zeitheron.hammercore.utils.math.MathHelper;
import com.zeitheron.hammercore.utils.math.vec.Vector3;

import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.BufferBuilder;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;

public class ParticleSlowZap extends SimpleParticle
{
	float ampl;
	BlockPos pos;
	
	public ParticleSlowZap(World par1World, int maxTicks, float ampl, double x, double y, double z, double tx, double ty, double tz, float red, float green, float blue)
	{
		super(par1World, x, y, z);
		particleRed = red;
		particleGreen = green;
		particleBlue = blue;
		this.ampl = ampl;
		setSize(.02F, .02F);
		motionX = 0;
		motionY = 0;
		motionZ = 0;
		pos = new BlockPos(x, y, z);
		tX = (tx - x);
		tY = (ty - y);
		tZ = (tz - z);
		particleMaxAge = maxTicks;
		Vec3d vs = new Vec3d(0, 0, 0);
		Vec3d ve = new Vec3d(tX, tY, tZ);
		length = (float) (ve.length() * Math.PI + maxTicks / 3F * Math.PI);
		dr = (float) (rand.nextInt(50) * Math.PI);
		seed = rand.nextLong();
	}
	
	@Override
	public void setMaxAge(int age)
	{
		particleMaxAge = age;
	}
	
	private double tX = 0.0D;
	private double tY = 0.0D;
	private double tZ = 0.0D;
	private static final ResourceLocation DEFAULT_BEAM_TEXTURE = new ResourceLocation("hammercore", "textures/misc/beaml.png");
	public ResourceLocation beamTexture = DEFAULT_BEAM_TEXTURE;
	
	@Deprecated
	public void setTexture(ResourceLocation beam)
	{
		beamTexture = beam;
	}
	
	ArrayList<Vec3d> points = new ArrayList();
	ArrayList<Float> pointsWidth = new ArrayList();
	float dr = 0.0F;
	long seed = 0L;
	
	Random rr = new Random(seed);
	
	@Override
	public void onUpdate()
	{
		rr.setSeed(seed);
		
		prevPosX = posX;
		prevPosY = posY;
		prevPosZ = posZ;
		if(particleAge++ >= particleMaxAge)
			setExpired();
		
		points.clear();
		pointsWidth.clear();
		Vec3d vs = new Vec3d(0.0D, 0.0D, 0.0D);
		Vec3d ve = new Vec3d(tX, tY, tZ);
		int steps = (int) length;
		int curSteps = Math.round((particleAge / (float) particleMaxAge) * length);
		points.add(vs);
		pointsWidth.add(1F);
		int i = 0;
		for(int a = 1; a < steps - 1; a++)
		{
			float dist = a * (length / steps) + dr;
			double dx = tX / steps * a + MathHelper.sin(dist / 4.0F) * ampl;
			double dy = tY / steps * a + MathHelper.sin(dist / 3.0F) * ampl;
			double dz = tZ / steps * a + MathHelper.sin(dist / 2.0F) * ampl;
			dx += (rr.nextFloat() - rr.nextFloat()) * 0.1F;
			dy += (rr.nextFloat() - rr.nextFloat()) * 0.1F;
			dz += (rr.nextFloat() - rr.nextFloat()) * 0.1F;
			Vec3d vp = new Vec3d(dx, dy, dz);
			points.add(vp);
			pointsWidth.add(1F);
			++i;
			if(i > curSteps)
				break;
		}
	}
	
	public void setRGB(float r, float g, float b)
	{
		particleRed = r;
		particleGreen = g;
		particleBlue = b;
	}
	
	@Override
	public void doRenderParticle(double x, double y, double z, float partialTicks, float rotationX, float rotationZ, float rotationYZ, float rotationXY, float rotationXZ)
	{
		BufferBuilder wr = Tessellator.getInstance().getBuffer();
		
		GL11.glPushMatrix();
		double ePX = prevPosX + (posX - prevPosX) * partialTicks - interpPosX;
		double ePY = prevPosY + (posY - prevPosY) * partialTicks - interpPosY;
		double ePZ = prevPosZ + (posZ - prevPosZ) * partialTicks - interpPosZ;
		GL11.glTranslated(ePX, ePY, ePZ);
		
		Minecraft.getMinecraft().renderEngine.bindTexture(beamTexture);
		
		GL11.glDepthMask(false);
		GL11.glEnable(3042);
		GL11.glBlendFunc(770, 1);
		
		GL11.glDisable(2884);
		
		int i = world != null && world.isBlockLoaded(pos) ? world.getCombinedLight(pos, 0) : 220;
		int j = i >> 16 & 0xFFFF;
		int k = i & 0xFFFF;
		
		wr.begin(5, DefaultVertexFormats.PARTICLE_POSITION_TEX_COLOR_LMAP);
		
		float f9 = 0.0F;
		float f10 = 1.0F;
		
		for(int c = 0; c < points.size(); c++)
		{
			float size = 0.15F * pointsWidth.get(c).floatValue();
			
			float f13 = c / length;
			Vec3d vc = points.get(c);
			Vec3d vp = c == 0 ? (Vec3d) points.get(c) : (Vec3d) points.get(c - 1);
			Vec3d vn = c == points.size() - 1 ? (Vec3d) points.get(c) : (Vec3d) points.get(c + 1);
			Vec3d v1 = vp.subtract(vc);
			Vec3d v2 = vc.subtract(vn);
			Vec3d v = v1.add(v2).normalize();
			v = v.rotatePitch(1.5707964F);
			Vector3 vf = new Vector3(v).multiply(size);
			wr.pos(vc.x + vf.x, vc.y + vf.y, vc.z + vf.z).tex(f13, f10).color(particleRed, particleGreen, particleBlue, particleAlpha).lightmap(j, k).endVertex();
			wr.pos(vc.x - vf.x, vc.y - vf.y, vc.z - vf.z).tex(f13, f9).color(particleRed, particleGreen, particleBlue, particleAlpha).lightmap(j, k).endVertex();
		}
		
		Tessellator.getInstance().draw();
		
		wr.begin(5, DefaultVertexFormats.PARTICLE_POSITION_TEX_COLOR_LMAP);
		
		for(int c = 0; c < points.size(); c++)
		{
			float size = 0.15F * pointsWidth.get(c).floatValue();
			float f13 = c / length;
			Vec3d vc = points.get(c);
			Vec3d vp = c == 0 ? (Vec3d) points.get(c) : (Vec3d) points.get(c - 1);
			Vec3d vn = c == points.size() - 1 ? (Vec3d) points.get(c) : (Vec3d) points.get(c + 1);
			Vec3d v1 = vp.subtract(vc);
			Vec3d v2 = vc.subtract(vn);
			Vec3d v = v1.add(v2).normalize();
			v = v.rotateYaw(1.5707964F);
			Vector3 vf = new Vector3(v).multiply(size);
			wr.pos(vc.x + vf.x, vc.y + vf.y, vc.z + vf.z).tex(f13, f10).color(particleRed, particleGreen, particleBlue, particleAlpha).lightmap(j, k).endVertex();
			wr.pos(vc.x - vf.x, vc.y - vf.y, vc.z - vf.z).tex(f13, f9).color(particleRed, particleGreen, particleBlue, particleAlpha).lightmap(j, k).endVertex();
		}
		
		Tessellator.getInstance().draw();
		
		GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
		GL11.glEnable(2884);
		GL11.glBlendFunc(770, 771);
		// GL11.glDisable(3042);
		GL11.glDepthMask(true);
		
		GL11.glPopMatrix();
	}
	
	public float length = 1.0F;
}