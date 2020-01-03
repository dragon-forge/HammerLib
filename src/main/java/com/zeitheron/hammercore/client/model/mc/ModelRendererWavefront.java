package com.zeitheron.hammercore.client.model.mc;

import com.google.common.collect.ImmutableMap;
import com.zeitheron.hammercore.HammerCore;
import com.zeitheron.hammercore.client.utils.GLList;
import com.zeitheron.hammercore.client.utils.UtilsFX;
import com.zeitheron.hammercore.client.utils.rendering.WavefrontLoader;
import net.minecraft.client.model.ModelBase;
import net.minecraft.client.model.ModelRenderer;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.client.renderer.texture.TextureMap;
import net.minecraft.util.ResourceLocation;

import javax.annotation.Nullable;
import java.util.Map;
import java.util.UUID;

public class ModelRendererWavefront
		extends ModelRenderer
{
	private boolean compiled;
	private GLList list;
	private final Map<String, Object> renderProperties;
	public final WavefrontLoader.WavefrontMeshProvider meshProvider;
	public float scale = 0;
	private final ResourceLocation texture;

	UUID renderedUUID;

	public ModelRendererWavefront(ModelBase model, WavefrontLoader.WavefrontMeshProvider meshProvider)
	{
		this(model, meshProvider, (ResourceLocation) null);
	}

	public ModelRendererWavefront(ModelBase model, WavefrontLoader.WavefrontMeshProvider meshProvider, ResourceLocation texture)
	{
		this(model, meshProvider, texture, null);
	}

	public ModelRendererWavefront(ModelBase model, WavefrontLoader.WavefrontMeshProvider meshProvider, TextureAtlasSprite sprite)
	{
		this(model, meshProvider, sprite != null ? TextureMap.LOCATION_BLOCKS_TEXTURE : null, sprite != null ? ImmutableMap.<String, Object> builder().put("uvTransformer", WavefrontLoader.transformUVToSprite(sprite)).build() : null);
	}

	public ModelRendererWavefront(ModelBase model, WavefrontLoader.WavefrontMeshProvider meshProvider, @Nullable ResourceLocation texture, @Nullable Map<String, Object> renderProperties)
	{
		super(model);
		this.meshProvider = meshProvider;
		this.renderProperties = renderProperties;
		this.texture = texture;
	}

	@Override
	public void render(float scale)
	{
		if(!this.isHidden && this.showModel)
		{
			compileDisplayList(scale);

			GlStateManager.translate(this.offsetX, this.offsetY, this.offsetZ);

			if(this.rotateAngleX == 0.0F && this.rotateAngleY == 0.0F && this.rotateAngleZ == 0.0F)
			{
				if(this.rotationPointX == 0.0F && this.rotationPointY == 0.0F && this.rotationPointZ == 0.0F)
					render();
				else
				{
					GlStateManager.translate(this.rotationPointX * scale, this.rotationPointY * scale, this.rotationPointZ * scale);
					render();
					GlStateManager.translate(-this.rotationPointX * scale, -this.rotationPointY * scale, -this.rotationPointZ * scale);
				}
			} else
			{
				GlStateManager.pushMatrix();
				GlStateManager.translate(this.rotationPointX * scale, this.rotationPointY * scale, this.rotationPointZ * scale);
				if(this.rotateAngleZ != 0.0F) GlStateManager.rotate(this.rotateAngleZ * 57.295776F, 0.0F, 0.0F, 1.0F);
				if(this.rotateAngleY != 0.0F) GlStateManager.rotate(this.rotateAngleY * 57.295776F, 0.0F, 1.0F, 0.0F);
				if(this.rotateAngleX != 0.0F) GlStateManager.rotate(this.rotateAngleX * 57.295776F, 1.0F, 0.0F, 0.0F);
				render();
				GlStateManager.popMatrix();
			}

			GlStateManager.translate(-this.offsetX, -this.offsetY, -this.offsetZ);
		}
	}

	public void render()
	{
		if(texture != null)
			UtilsFX.bindTexture(texture);
		list.render();
	}

	private void compileDisplayList(float scale)
	{
		if(compiled)
		{
			WavefrontLoader.WavefrontMesh mesh;
			if(renderedUUID != null && (mesh = meshProvider.getMesh()) != null && !renderedUUID.equals(mesh.getUniqueId()))
				compiled = false;
			else return;
		}

		if(this.scale == 0) this.scale = scale;

		if(meshProvider.getMesh() == null)
		{
			compiled = true;
			HammerCore.LOG.error("Wavefront Mesh could not be parsed!!! The model is broken!");
			return;
		}

		if(list == null)
			this.list = new GLList();

		list.store(meshProvider.withSurprocessor(props ->
		{
			GlStateManager.pushMatrix();
			GlStateManager.scale(this.scale, this.scale, this.scale);
			GlStateManager.rotate(180, -1, 0, 1);
		}, props -> GlStateManager.popMatrix()), renderProperties);

		compiled = true;
		renderedUUID = meshProvider.getMesh().getUniqueId();
	}

	@Override
	public void renderWithRotation(float scale)
	{
		if(!this.isHidden && this.showModel)
		{
			if(!this.compiled)
				this.compileDisplayList(scale);
			GlStateManager.pushMatrix();
			GlStateManager.translate(this.rotationPointX * scale, this.rotationPointY * scale, this.rotationPointZ * scale);
			if(this.rotateAngleY != 0.0F) GlStateManager.rotate(this.rotateAngleY * 57.295776F, 0.0F, 1.0F, 0.0F);
			if(this.rotateAngleX != 0.0F) GlStateManager.rotate(this.rotateAngleX * 57.295776F, 1.0F, 0.0F, 0.0F);
			if(this.rotateAngleZ != 0.0F) GlStateManager.rotate(this.rotateAngleZ * 57.295776F, 0.0F, 0.0F, 1.0F);
			list.render();
			GlStateManager.popMatrix();
		}
	}

	@Override
	public void postRender(float scale)
	{
		if(!this.isHidden && this.showModel)
		{
			if(this.rotateAngleX == 0.0F && this.rotateAngleY == 0.0F && this.rotateAngleZ == 0.0F)
			{
				if(this.rotationPointX != 0.0F || this.rotationPointY != 0.0F || this.rotationPointZ != 0.0F)
					GlStateManager.translate(this.rotationPointX * scale, this.rotationPointY * scale, this.rotationPointZ * scale);
			} else
			{
				GlStateManager.translate(this.rotationPointX * scale, this.rotationPointY * scale, this.rotationPointZ * scale);
				if(this.rotateAngleZ != 0.0F) GlStateManager.rotate(this.rotateAngleZ * 57.295776F, 0.0F, 0.0F, 1.0F);
				if(this.rotateAngleY != 0.0F) GlStateManager.rotate(this.rotateAngleY * 57.295776F, 0.0F, 1.0F, 0.0F);
				if(this.rotateAngleX != 0.0F) GlStateManager.rotate(this.rotateAngleX * 57.295776F, 1.0F, 0.0F, 0.0F);
			}
		}
	}

	public void dispose()
	{
		list.delete();
	}

	@Override
	protected void finalize() throws Throwable
	{
		dispose();
		super.finalize();
	}
}