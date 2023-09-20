package org.zeith.hammerlib.client.render;

import com.mojang.blaze3d.vertex.VertexConsumer;
import org.apache.logging.log4j.*;
import org.zeith.hammerlib.util.colors.ColorHelper;

public class TintingVertexConsumer
		implements VertexConsumer
{
	protected final VertexConsumer parent;
	
	protected float a, r, g, b;
	
	public TintingVertexConsumer(VertexConsumer parent, int rgba)
	{
		this.parent = parent;
		r = ColorHelper.getRed(rgba);
		g = ColorHelper.getGreen(rgba);
		b = ColorHelper.getBlue(rgba);
		a = ColorHelper.getAlpha(rgba);
	}
	
	public TintingVertexConsumer(VertexConsumer parent, float r, float g, float b, float a)
	{
		this.parent = parent;
		this.r = r;
		this.g = g;
		this.b = b;
		this.a = a;
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
	
	public static class Logging
			extends TintingVertexConsumer
	{
		public static Logger LOG = LogManager.getLogger("TintingVertexConsumer");
		
		public Logging(VertexConsumer parent, int rgba)
		{
			super(parent, rgba);
		}
		
		public Logging(VertexConsumer parent, float r, float g, float b, float a)
		{
			super(parent, r, g, b, a);
		}
		
		@Override
		public VertexConsumer vertex(double pX, double pY, double pZ)
		{
			LOG.info("vertex({}, {}, {})", pX, pY, pZ);
			return super.vertex(pX, pY, pZ);
		}
		
		@Override
		public VertexConsumer color(int pRed, int pGreen, int pBlue, int pAlpha)
		{
			LOG.info("color({}, {}, {}, {})", pRed, pGreen, pBlue, pAlpha);
			return super.color(pRed, pGreen, pBlue, pAlpha);
		}
		
		@Override
		public VertexConsumer uv(float pU, float pV)
		{
			LOG.info("uv({}, {})", pU, pV);
			return super.uv(pU, pV);
		}
		
		@Override
		public VertexConsumer overlayCoords(int pU, int pV)
		{
			LOG.info("overlayCoords({}, {})", pU, pV);
			return super.overlayCoords(pU, pV);
		}
		
		@Override
		public VertexConsumer uv2(int pU, int pV)
		{
			LOG.info("uv2({}, {})", pU, pV);
			return super.uv2(pU, pV);
		}
		
		@Override
		public VertexConsumer normal(float pX, float pY, float pZ)
		{
			LOG.info("normal({}, {}, {})", pX, pY, pZ);
			return super.normal(pX, pY, pZ);
		}
		
		@Override
		public void endVertex()
		{
			LOG.info("endVertex()");
			super.endVertex();
		}
		
		@Override
		public void defaultColor(int pDefaultR, int pDefaultG, int pDefaultB, int pDefaultA)
		{
			LOG.info("defaultColor({}, {}, {}, {})", pDefaultR, pDefaultG, pDefaultB, pDefaultA);
			super.defaultColor(pDefaultR, pDefaultG, pDefaultB, pDefaultA);
		}
	}
}
