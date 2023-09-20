package org.zeith.hammerlib.client.render;

import com.mojang.blaze3d.vertex.*;
import com.mojang.math.*;
import net.minecraft.client.renderer.block.model.BakedQuad;
import net.minecraft.world.item.ItemStack;
import org.zeith.hammerlib.util.colors.ColorHelper;
import org.zeith.hammerlib.util.java.functions.*;

import java.nio.ByteBuffer;

public class TintingVertexConsumer
		implements VertexConsumer
{
	public static ItemStack hl$contextStack = ItemStack.EMPTY;
	
	protected final VertexConsumer parent;
	
	protected float a, r, g, b;
	
	public static boolean tintingEnabled = true;
	public static Function2<VertexConsumer, Integer, VertexConsumer> TINT1i = TintingVertexConsumer::new;
	public static Function5<VertexConsumer, Float, Float, Float, Float, VertexConsumer> TINT4f = TintingVertexConsumer::new;
	
	protected TintingVertexConsumer(VertexConsumer parent, int rgba)
	{
		this.parent = parent;
		r = ColorHelper.getRed(rgba);
		g = ColorHelper.getGreen(rgba);
		b = ColorHelper.getBlue(rgba);
		a = ColorHelper.getAlpha(rgba);
	}
	
	protected TintingVertexConsumer(VertexConsumer parent, float r, float g, float b, float a)
	{
		this.parent = parent;
		this.r = r;
		this.g = g;
		this.b = b;
		this.a = a;
	}
	
	public static VertexConsumer wrap(VertexConsumer parent, int rgba)
	{
		return TINT1i.apply(parent, rgba);
	}
	
	public static VertexConsumer wrap(VertexConsumer parent, float r, float g, float b, float a)
	{
		return TINT4f.apply(parent, r, g, b, a);
	}
	
	@Override
	public VertexConsumer vertex(double pX, double pY, double pZ)
	{
		parent.vertex(pX, pY, pZ);
		return this;
	}
	
	@Override
	public VertexConsumer color(int pRed, int pGreen, int pBlue, int pAlpha)
	{
		pRed *= r;
		pGreen *= g;
		pBlue *= b;
		pAlpha *= a;
		parent.color(pRed, pGreen, pBlue, pAlpha);
		return this;
	}
	
	@Override
	public VertexConsumer uv(float pU, float pV)
	{
		parent.uv(pU, pV);
		return this;
	}
	
	@Override
	public VertexConsumer overlayCoords(int pU, int pV)
	{
		parent.overlayCoords(pU, pV);
		return this;
	}
	
	@Override
	public VertexConsumer uv2(int pU, int pV)
	{
		parent.uv2(pU, pV);
		return this;
	}
	
	@Override
	public VertexConsumer normal(float pX, float pY, float pZ)
	{
		parent.normal(pX, pY, pZ);
		return this;
	}
	
	@Override
	public void endVertex()
	{
		parent.endVertex();
	}
	
	@Override
	public void defaultColor(int pDefaultR, int pDefaultG, int pDefaultB, int pDefaultA)
	{
		pDefaultR *= r;
		pDefaultG *= g;
		pDefaultB *= b;
		pDefaultA *= a;
		parent.defaultColor(pDefaultR, pDefaultG, pDefaultB, pDefaultA);
	}
	
	@Override
	public void unsetDefaultColor()
	{
		parent.unsetDefaultColor();
	}
	
	@Override
	public void vertex(float pX, float pY, float pZ, float pRed, float pGreen, float pBlue, float pAlpha, float pTexU, float pTexV, int pOverlayUV, int pLightmapUV, float pNormalX, float pNormalY, float pNormalZ)
	{
		parent.vertex(
				pX, pY, pZ,
				pRed * r, pGreen * g, pBlue * b, pAlpha * a,
				pTexU, pTexV,
				pOverlayUV, pLightmapUV,
				pNormalX, pNormalY, pNormalZ
		);
	}
	
	@Override
	public VertexConsumer color(float pRed, float pGreen, float pBlue, float pAlpha)
	{
		parent.color(pRed * r, pGreen * g, pBlue * b, pAlpha * a);
		return this;
	}
	
	@Override
	public VertexConsumer color(int pColorARGB)
	{
		pColorARGB = ColorHelper.packARGB(
				ColorHelper.getAlpha(pColorARGB) * a,
				ColorHelper.getRed(pColorARGB) * r,
				ColorHelper.getGreen(pColorARGB) * g,
				ColorHelper.getBlue(pColorARGB) * b
		);
		parent.color(pColorARGB);
		return this;
	}
	
	@Override
	public VertexConsumer uv2(int pLightmapUV)
	{
		parent.uv2(pLightmapUV);
		return this;
	}
	
	@Override
	public VertexConsumer overlayCoords(int pOverlayUV)
	{
		parent.overlayCoords(pOverlayUV);
		return this;
	}
	
	@Override
	public void putBulkData(PoseStack.Pose pPoseEntry, BakedQuad pQuad, float pRed, float pGreen, float pBlue, int pCombinedLight, int pCombinedOverlay)
	{
		parent.putBulkData(pPoseEntry, pQuad, pRed * r, pGreen * g, pBlue * b, pCombinedLight, pCombinedOverlay);
	}
	
	@Override
	public void putBulkData(PoseStack.Pose pPoseEntry, BakedQuad pQuad, float[] pColorMuls, float pRed, float pGreen, float pBlue, int[] pCombinedLights, int pCombinedOverlay, boolean pMulColor)
	{
		parent.putBulkData(pPoseEntry, pQuad, pColorMuls,
				pRed * r, pGreen * g, pBlue * b, pCombinedLights, pCombinedOverlay, pMulColor
		);
	}
	
	@Override
	public void putBulkData(PoseStack.Pose pPoseEntry, BakedQuad pQuad, float[] pColorMuls, float pRed, float pGreen, float pBlue, float alpha, int[] pCombinedLights, int pCombinedOverlay, boolean pMulColor)
	{
		parent.putBulkData(pPoseEntry, pQuad, pColorMuls,
				pRed * r, pGreen * g, pBlue * b, alpha * a, pCombinedLights, pCombinedOverlay, pMulColor
		);
	}
	
	@Override
	public VertexConsumer misc(VertexFormatElement element, int... rawData)
	{
		parent.misc(element, rawData);
		return this;
	}
	
	@Override
	public void putBulkData(PoseStack.Pose pose, BakedQuad bakedQuad, float red, float green, float blue, float alpha, int packedLight, int packedOverlay, boolean readExistingColor)
	{
		parent.putBulkData(pose, bakedQuad,
				red * r, green * g, blue * b, alpha * a, packedLight, packedOverlay, readExistingColor
		);
	}
	
	@Override
	public int applyBakedLighting(int packedLight, ByteBuffer data)
	{
		return parent.applyBakedLighting(packedLight, data);
	}
	
	@Override
	public VertexConsumer vertex(Matrix4f p_85983_, float p_85984_, float p_85985_, float p_85986_)
	{
		parent.vertex(p_85983_, p_85984_, p_85985_, p_85986_);
		return this;
	}
	
	@Override
	public VertexConsumer normal(Matrix3f p_85978_, float p_85979_, float p_85980_, float p_85981_)
	{
		parent.normal(p_85978_, p_85979_, p_85980_, p_85981_);
		return this;
	}
	
	@Override
	public void applyBakedNormals(Vector3f generated, ByteBuffer data, Matrix3f normalTransform)
	{
		parent.applyBakedNormals(generated, data, normalTransform);
	}
}