package com.zeitheron.hammercore.client.render.tesr;

import java.util.List;

import org.lwjgl.opengl.GL11;

import com.zeitheron.hammercore.HammerCore;
import com.zeitheron.hammercore.annotations.AtTESR;
import com.zeitheron.hammercore.api.multipart.MultipartRenderingRegistry;
import com.zeitheron.hammercore.api.multipart.MultipartSignature;
import com.zeitheron.hammercore.api.multipart.IMultipartRender;
import com.zeitheron.hammercore.internal.blocks.multipart.BlockMultipart;
import com.zeitheron.hammercore.internal.blocks.multipart.TileMultipart;
import com.zeitheron.hammercore.internal.init.BlocksHC;
import com.zeitheron.hammercore.raytracer.RayTracer;
import com.zeitheron.hammercore.utils.math.vec.Cuboid6;

import net.minecraft.client.Minecraft;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.world.World;

@AtTESR(TileMultipart.class)
public class TESRMultipart extends TESR<TileMultipart>
{
	@Override
	public void renderTileEntityAt(TileMultipart te, double x, double y, double z, float partialTicks, ResourceLocation destroyStage, float alpha)
	{
		try
		{
			List<MultipartSignature> mps = te.signatures();
			if(mps == null)
				return;
			
			BlockMultipart bmp = (BlockMultipart) BlocksHC.MULTIPART;
			World w = te.getWorld();
			EntityPlayer p = Minecraft.getMinecraft().player;
			Cuboid6 cbd = bmp.getCuboidFromRTR(te.getWorld(), bmp.collisionRayTrace(w.getBlockState(te.getPos()), w, te.getPos(), RayTracer.getCorrectedHeadVec(p), RayTracer.getEndVec(p)));
			AxisAlignedBB aabb = cbd != null ? cbd.aabb() : null;
			
			/* Moved to good ol' for loops; The fastest way :D */
			for(MultipartSignature s : mps)
			{
				IMultipartRender render = MultipartRenderingRegistry.getRender(s);
				GL11.glPushMatrix();
				if(render != null)
					render.renderMultipartAt(s, x, y, z, partialTicks, aabb != null && s.getBoundingBox() != null && aabb.equals(s.getBoundingBox()) ? destroyStage : null);
				GL11.glPopMatrix();
			}
		} catch(Throwable err)
		{
			HammerCore.LOG.error("Failed to render multipart at " + te.getPos() + ": " + err);
		} // we must ignore all issues that may arise!
	}
	
	/** False -- for future use */
	@Override
	public boolean canRenderFromNbt()
	{
		return false;
	}
}