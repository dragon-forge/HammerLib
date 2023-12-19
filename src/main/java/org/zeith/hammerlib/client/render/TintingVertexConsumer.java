package org.zeith.hammerlib.client.render;

import com.mojang.blaze3d.vertex.VertexConsumer;
import net.neoforged.neoforge.client.model.pipeline.VertexConsumerWrapper;
import org.zeith.hammerlib.util.colors.ColorHelper;
import org.zeith.hammerlib.util.java.functions.*;

public class TintingVertexConsumer
		extends VertexConsumerWrapper
{
	protected float a, r, g, b;
	
	public static boolean tintingEnabled = true;
	public static Function2<VertexConsumer, Integer, VertexConsumer> TINT1i = TintingVertexConsumer::new;
	public static Function5<VertexConsumer, Float, Float, Float, Float, VertexConsumer> TINT4f = TintingVertexConsumer::new;
	
	protected TintingVertexConsumer(VertexConsumer parent, int rgba)
	{
		super(parent);
		r = ColorHelper.getRed(rgba);
		g = ColorHelper.getGreen(rgba);
		b = ColorHelper.getBlue(rgba);
		a = ColorHelper.getAlpha(rgba);
	}
	
	protected TintingVertexConsumer(VertexConsumer parent, float r, float g, float b, float a)
	{
		super(parent);
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
	public void defaultColor(int pDefaultR, int pDefaultG, int pDefaultB, int pDefaultA)
	{
		pDefaultR *= r;
		pDefaultG *= g;
		pDefaultB *= b;
		pDefaultA *= a;
		parent.defaultColor(pDefaultR, pDefaultG, pDefaultB, pDefaultA);
	}
}