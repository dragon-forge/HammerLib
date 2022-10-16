package org.zeith.hammerlib.client.model;

import com.mojang.datafixers.util.Pair;
import com.mojang.math.Vector3f;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.block.model.*;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.client.resources.model.*;
import net.minecraft.core.Direction;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.RandomSource;
import net.minecraft.world.inventory.InventoryMenu;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.client.model.data.ModelData;
import net.minecraftforge.client.model.geometry.*;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.zeith.hammerlib.proxy.HLConstants;

import java.util.*;
import java.util.function.Function;

@LoadUnbakedGeometry(path = "block/test")
public class TestModel
		implements IUnbakedGeometry<TestModel>
{
	public static final Material TEST_MATERIAL = new Material(InventoryMenu.BLOCK_ATLAS, new ResourceLocation(HLConstants.MOD_ID, "block/machine_down"));
	
	@Override
	public BakedModel bake(IGeometryBakingContext context, ModelBakery bakery, Function<Material, TextureAtlasSprite> spriteGetter, ModelState modelState, ItemOverrides overrides, ResourceLocation modelLocation)
	{
		Minecraft.getInstance().getConnection();
		return new TestBakedModel(spriteGetter, modelState, modelLocation);
	}
	
	@Override
	public Collection<Material> getMaterials(IGeometryBakingContext context, Function<ResourceLocation, UnbakedModel> modelGetter, Set<Pair<String, String>> missingTextureErrors)
	{
		return Set.of(
				TEST_MATERIAL
		);
	}
	
	public static class TestBakedModel
			implements IBakedModel
	{
		private TextureAtlasSprite testSprite;
		Function<Material, TextureAtlasSprite> spriteGetter;
		ModelState modelState;
		ResourceLocation modelLocation;
		
		public TestBakedModel(Function<Material, TextureAtlasSprite> spriteGetter, ModelState modelState, ResourceLocation modelLocation)
		{
			testSprite = spriteGetter.apply(TEST_MATERIAL);
			
			this.spriteGetter = spriteGetter;
			this.modelState = modelState;
			this.modelLocation = modelLocation;
		}
		
		@Override
		public @NotNull List<BakedQuad> getQuads(@Nullable BlockState state, @Nullable Direction side, @NotNull RandomSource rand, @NotNull ModelData data, @Nullable RenderType renderType)
		{
			return UnbakedGeometryHelper.bakeElements(List.of(
					new BlockElement(new Vector3f(0, 0, 0), new Vector3f(16, 16, 16), Map.of(
							
							Direction.UP, new BlockElementFace(Direction.UP, 0, TEST_MATERIAL.texture().toString(), new BlockFaceUV(new float[] {
									0,
									0,
									16,
									16
							}, 0)),
							
							Direction.DOWN, new BlockElementFace(Direction.DOWN, 0, TEST_MATERIAL.texture().toString(), new BlockFaceUV(new float[] {
									0,
									0,
									16,
									16
							}, 0)),
							
							Direction.NORTH, new BlockElementFace(Direction.NORTH, 0, TEST_MATERIAL.texture().toString(), new BlockFaceUV(new float[] {
									0,
									0,
									16,
									16
							}, 0)),
							
							Direction.EAST, new BlockElementFace(Direction.EAST, 0, TEST_MATERIAL.texture().toString(), new BlockFaceUV(new float[] {
									0,
									0,
									16,
									16
							}, 0)),
							
							Direction.SOUTH, new BlockElementFace(Direction.SOUTH, 0, TEST_MATERIAL.texture().toString(), new BlockFaceUV(new float[] {
									0,
									0,
									16,
									16
							}, 0)),
							
							Direction.WEST, new BlockElementFace(Direction.WEST, 0, TEST_MATERIAL.texture().toString(), new BlockFaceUV(new float[] {
									0,
									0,
									16,
									16
							}, 0))
							
					), null, true)
			), spriteGetter, modelState, modelLocation);
		}
		
		@Override
		public boolean useAmbientOcclusion()
		{
			return false;
		}
		
		@Override
		public boolean isGui3d()
		{
			return false;
		}
		
		@Override
		public boolean usesBlockLight()
		{
			return true;
		}
		
		@Override
		public boolean isCustomRenderer()
		{
			return false;
		}
		
		@Override
		public TextureAtlasSprite getParticleIcon()
		{
			return testSprite;
		}
	}
}