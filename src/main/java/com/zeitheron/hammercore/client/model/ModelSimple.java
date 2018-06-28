package com.zeitheron.hammercore.client.model;

import com.zeitheron.hammercore.client.utils.UtilsFX;

import net.minecraft.client.model.ModelBase;
import net.minecraft.client.model.ModelRenderer;
import net.minecraft.util.ResourceLocation;

public class ModelSimple<T> extends ModelBase
{
	protected ResourceLocation texture;
	protected String textureURL;
	
	public ModelSimple(int texWidth, int texHeight, ResourceLocation texture)
	{
		this.textureWidth = texWidth;
		this.textureHeight = texHeight;
		this.texture = texture;
	}
	
	public ModelSimple(int texWidth, int texHeight, String textureURL)
	{
		this.textureWidth = texWidth;
		this.textureHeight = texHeight;
		this.textureURL = textureURL;
	}
	
	public void setRotateAngle(ModelRenderer modelRenderer, float x, float y, float z)
	{
		modelRenderer.rotateAngleX = x;
		modelRenderer.rotateAngleY = y;
		modelRenderer.rotateAngleZ = z;
	}
	
	public void setRotationAngles(float f, float f1, float f2, float f3, float f4, float f5)
	{
		super.setRotationAngles(f, f1, f2, f3, f4, f5, null);
	}
	
	public void bindTexture(T o)
	{
		String url = getTextureURL();
		if(url != null)
			UtilsFX.bindTextureURL(url);
		else
			UtilsFX.bindTexture(getTexture(o));
	}
	
	public ResourceLocation getTexture(T o)
	{
		return texture;
	}
	
	public String getTextureURL()
	{
		return textureURL;
	}
}