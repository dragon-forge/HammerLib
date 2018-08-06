package com.zeitheron.hammercore.client.model.mc;

import static net.minecraft.client.renderer.block.model.ItemCameraTransforms.TransformType.FIRST_PERSON_LEFT_HAND;
import static net.minecraft.client.renderer.block.model.ItemCameraTransforms.TransformType.FIRST_PERSON_RIGHT_HAND;
import static net.minecraft.client.renderer.block.model.ItemCameraTransforms.TransformType.GROUND;
import static net.minecraft.client.renderer.block.model.ItemCameraTransforms.TransformType.NONE;
import static net.minecraft.client.renderer.block.model.ItemCameraTransforms.TransformType.THIRD_PERSON_LEFT_HAND;
import static net.minecraft.client.renderer.block.model.ItemCameraTransforms.TransformType.THIRD_PERSON_RIGHT_HAND;

import java.util.Collections;
import java.util.List;

import javax.annotation.Nonnull;
import javax.vecmath.Matrix4f;

import org.apache.commons.lang3.tuple.Pair;

import com.google.common.collect.ImmutableList;

import net.minecraft.block.state.IBlockState;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.BufferBuilder;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.block.model.BakedQuad;
import net.minecraft.client.renderer.block.model.IBakedModel;
import net.minecraft.client.renderer.block.model.ItemCameraTransforms;
import net.minecraft.client.renderer.block.model.ItemCameraTransforms.TransformType;
import net.minecraft.client.renderer.block.model.ItemOverrideList;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.client.renderer.texture.TextureMap;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.client.renderer.vertex.VertexFormat;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumFacing;
import net.minecraft.world.World;

/**
 * Failed experiment, I think.
 */
public class HackyBakedModel implements IBakedModel
{
	public static final List<BakedQuad> QUADS = Collections.emptyList();
	public final ISimplifiedModel model;
	private final Pair<IBakedModel, Matrix4f> selfPair;
	private ItemCameraTransforms.TransformType currentPerspective;
	private ItemStack lastStack;
	private EntityLivingBase lastEntity;
	VertexFormat defaultVertexFormat = DefaultVertexFormats.ITEM;
	
	public HackyBakedModel(ISimplifiedModel model)
	{
		this.model = model;
		selfPair = Pair.of(this, null);
	}
	
	public HackyBakedModel(@Nonnull ISimplifiedModel renderer, @Nonnull VertexFormat defVertexFormat)
	{
		this(renderer);
		defaultVertexFormat = defVertexFormat;
	}
	
	@Override
	public List<BakedQuad> getQuads(IBlockState state, EnumFacing side, long rand)
	{
		model.handleBlockState(state, side, rand);
		
		Tessellator tessellator = Tessellator.getInstance();
		tessellator.draw();
		
		GlStateManager.pushMatrix();
		GlStateManager.translate(.5, .5, .5);
		GlStateManager.scale(-1, -1, 1);
		
		Minecraft.getMinecraft().getTextureManager().bindTexture(model.getTexture());
		model.renderModel(0);
		model.postRender();
		Minecraft.getMinecraft().getTextureManager().bindTexture(TextureMap.LOCATION_BLOCKS_TEXTURE);
		
		GlStateManager.popMatrix();
		
		// cleanup
		currentPerspective = null;
		lastStack = null;
		lastEntity = null;
		
		BufferBuilder vertexBuffer = tessellator.getBuffer();
		vertexBuffer.begin(7, defaultVertexFormat);
		
		return QUADS;
	}
	
	@Override
	public boolean isAmbientOcclusion()
	{
		return true;
	}
	
	@Override
	public boolean isGui3d()
	{
		return true;
	}
	
	@Override
	public boolean isBuiltInRenderer()
	{
		return false;
	}
	
	@Override
	public TextureAtlasSprite getParticleTexture()
	{
		return Minecraft.getMinecraft().getTextureMapBlocks().getMissingSprite();
	}
	
	@Override
	public ItemOverrideList getOverrides()
	{
		return ItemOverrideListHandler.INSTANCE_HACKY.setModelBaseWrapper(this);
	}
	
	@Override
	public ItemCameraTransforms getItemCameraTransforms()
	{
		return model.getCameraTransforms();
	}
	
	@Override
	public Pair<? extends IBakedModel, Matrix4f> handlePerspective(TransformType cameraTransformType)
	{
		currentPerspective = cameraTransformType;
		Pair<? extends IBakedModel, Matrix4f> pair = model.handlePerspective(cameraTransformType, selfPair);
		if(model.useVanillaCameraTransform())
		{
			boolean isLeft = isLeftHand(cameraTransformType);
			ItemCameraTransforms.applyTransformSide(this.getItemCameraTransforms().getTransform(cameraTransformType), isLeft);
		}
		return pair;
	}
	
	private static final class ItemOverrideListHandler extends ItemOverrideList
	{
		private static final ItemOverrideListHandler INSTANCE_HACKY = new ItemOverrideListHandler();
		
		private ItemOverrideListHandler()
		{
			super(ImmutableList.of());
		}
		
		private HackyBakedModel modelBaseWrapper;
		
		private ItemOverrideListHandler setModelBaseWrapper(HackyBakedModel model)
		{
			modelBaseWrapper = model;
			return this;
		}
		
		@Override
		public IBakedModel handleItemState(IBakedModel originalModel, ItemStack stack, World world, EntityLivingBase entity)
		{
			modelBaseWrapper.lastStack = stack;
			modelBaseWrapper.lastEntity = entity;
			modelBaseWrapper.model.handleItemState(stack, world, entity);
			return originalModel;
		}
	}
	
	public static boolean isFirstPerson(ItemCameraTransforms.TransformType type)
	{
		return type == FIRST_PERSON_LEFT_HAND || type == FIRST_PERSON_RIGHT_HAND;
	}
	
	public static boolean isThirdPerson(ItemCameraTransforms.TransformType type)
	{
		return type == THIRD_PERSON_LEFT_HAND || type == THIRD_PERSON_RIGHT_HAND;
	}
	
	public static boolean isEntityRender(ItemCameraTransforms.TransformType type)
	{
		return isFirstPerson(type) || isThirdPerson(type);
	}
	
	public static boolean isLeftHand(ItemCameraTransforms.TransformType type)
	{
		return type == FIRST_PERSON_LEFT_HAND || type == THIRD_PERSON_LEFT_HAND;
	}
	
	public static boolean isRightHand(ItemCameraTransforms.TransformType type)
	{
		return type == FIRST_PERSON_RIGHT_HAND || type == THIRD_PERSON_RIGHT_HAND;
	}
	
	public static boolean isItemRender(ItemCameraTransforms.TransformType type)
	{
		return type == null || type == GROUND || type == NONE;
	}
}