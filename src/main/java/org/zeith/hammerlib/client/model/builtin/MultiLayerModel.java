package org.zeith.hammerlib.client.model.builtin;

import com.google.common.collect.Lists;
import com.google.gson.*;
import it.unimi.dsi.fastutil.ints.*;
import net.minecraft.Util;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.block.model.*;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.client.resources.model.*;
import net.minecraft.core.Direction;
import net.minecraft.util.GsonHelper;
import net.minecraft.util.RandomSource;
import net.minecraft.util.context.ContextMap;
import net.minecraft.world.level.block.state.BlockState;
import net.neoforged.neoforge.client.ChunkRenderTypeSet;
import net.neoforged.neoforge.client.RenderTypeGroup;
import net.neoforged.neoforge.client.model.NeoForgeModelProperties;
import net.neoforged.neoforge.client.model.data.ModelData;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.zeith.hammerlib.client.model.*;
import org.zeith.hammerlib.util.java.tuples.Tuple2;
import org.zeith.hammerlib.util.java.tuples.Tuples;

import java.util.*;
import java.util.function.Function;
import java.util.stream.IntStream;
import java.util.stream.Stream;

@LoadUnbakedGeometry(path = "multi_layer")
public class MultiLayerModel
		implements IUnbakedGeometry
{
	private static final IntList EG = IntList.of();
	protected final List<BlockElement> elements;
	protected final IntList ungrouped;
	protected final Map<String, IntList> groups = new HashMap<>();
	protected final Map<String, String> textures = new HashMap<>();
	
	public MultiLayerModel(JsonObject obj, JsonDeserializationContext context)
	{
		this.elements = getElements(context, obj);
		
		if(obj.has("textures"))
		{
			var arr = obj.getAsJsonObject("textures");
			for(String id : arr.keySet())
				textures.put(id, GsonHelper.getAsString(arr, id));
		}
		
		for(BlockElement element : elements)
		{
			element.faces.replaceAll((dir, value) ->
			{
				if(value.texture().startsWith("#"))
				{
					var ntx = textures.getOrDefault(value.texture().substring(1), "missing");
					return new BlockElementFace(value.cullForDirection(), value.tintIndex(), ntx, value.uv(), value.faceData(), value.parent());
				}
				return value;
			});
		}
		
		IntList ungrouped = new IntArrayList();
		for(int i = 0; i < elements.size(); i++)
			ungrouped.add(i);
		
		if(obj.has("groups"))
		{
			var arr = obj.getAsJsonArray("groups");
			for(JsonElement el : arr)
			{
				var groupJson = el.getAsJsonObject();
				parseGroup("", groupJson).forEach(g ->
						groups.computeIfAbsent(g.a(), ke -> new IntArrayList())
								.addAll(g.b())
				);
			}
		}
		
		this.ungrouped = ungrouped;
	}
	
	protected List<Tuple2<String, IntList>> parseGroup(String prefix, JsonObject group)
	{
		List<Tuple2<String, IntList>> lst = Lists.newArrayList();
		
		var name = GsonHelper.getAsString(group, "name");
		IntList thisGroup = new IntArrayList();
		
		var childrenJson = group.getAsJsonArray("children");
		for(int i = 0; i < childrenJson.size(); i++)
		{
			var elem = childrenJson.get(i);
			if(elem.isJsonPrimitive()) thisGroup.add(elem.getAsInt());
			else if(elem.isJsonObject())
				lst.addAll(parseGroup(prefix + name + "/", elem.getAsJsonObject()));
		}
		
		if(!thisGroup.isEmpty())
			lst.add(Tuples.immutable(prefix + name, thisGroup));
		
		return lst;
	}
	
	public IntList getGroup(String name)
	{
		if(name == null) return ungrouped;
		return groups.getOrDefault(name, EG);
	}
	
	@Override
	public BakedModel bake(TextureSlots slots, ModelBaker baker, ModelState modelState, boolean useAmbientOcclusion, boolean usesBlockLight, ItemTransforms itemTransforms, ContextMap additionalProperties)
	{
		var spriteGetter = baker.sprites();
		
		boolean isGui3d = true;
		
		try
		{
			List<BakedQuad> quads = Lists.newArrayList();
			Int2ObjectArrayMap<String> toGroup = new Int2ObjectArrayMap<>();
			int[][] quadOffsetsAndCounts = new int[elements.size()][];
			
			var renderTypes = additionalProperties.getOrDefault(NeoForgeModelProperties.RENDER_TYPE, RenderTypeGroup.EMPTY);
			TextureAtlasSprite particle = IUnbakedGeometry.findSprite(spriteGetter, slots, textures.getOrDefault("particle", "particle"));
			
			for(int i = 0; i < elements.size(); i++)
			{
				var baked = IUnbakedGeometry.bakeFace(elements.get(i), spriteGetter, slots, modelState);
				for(int j = quads.size(); j < quads.size() + baked.size(); j++)
				{
					String group = null;
					for(Map.Entry<String, IntList> entry : groups.entrySet())
					{
						if(entry.getValue().contains(j))
						{
							group = entry.getKey();
							break;
						}
					}
					toGroup.put(j, group);
				}
				quadOffsetsAndCounts[i] = new int[] { quads.size(), quads.size() + baked.size() };
				quads.addAll(baked);
			}
			
			return new MultiLayerBakedModel(
					quads, useAmbientOcclusion, usesBlockLight, isGui3d, particle, itemTransforms, renderTypes,
					this::getGroup, quadOffsetsAndCounts, toGroup
			);
		} catch(Throwable e)
		{
			e.printStackTrace();
			return null;
		}
	}
	
	protected List<BlockElement> getElements(JsonDeserializationContext ctx, JsonObject root)
	{
		List<BlockElement> list = Lists.newArrayList();
		if(root.has("elements"))
			for(JsonElement jsonelement : GsonHelper.getAsJsonArray(root, "elements"))
				list.add(ctx.deserialize(jsonelement, BlockElement.class));
		return list;
	}
	
	@Override
	public void resolveDependencies(Resolver resolver)
	{
	}
	
	public static class MultiLayerBakedModel
			extends SimpleBakedModel
			implements IGroupedQuadModel
	{
		protected final int[][] quadOffsetsAndCounts;
		protected final Int2ObjectArrayMap<String> toGroup;
		protected final Function<String, IntList> quadIndices;
		
		protected final List<BakedQuad> unculledFaces;
		protected final MultiLayerItemBakedModel itemModel;
		
		public static final Function<RenderType, String> RT_KEYS = Util.make(new HashMap<RenderType, String>(), map ->
				{
					map.put(RenderType.solid(), "solid");
					map.put(RenderType.cutoutMipped(), "cutout_mipped");
					map.put(RenderType.cutout(), "cutout");
					map.put(RenderType.translucent(), "translucent");
					map.put(RenderType.tripwire(), "tripwire");
				}
		)::get;
		
		public MultiLayerBakedModel(List<BakedQuad> unculledFaces,
									boolean hasAmbientOcclusion, boolean usesBlockLight, boolean isGui3d,
									TextureAtlasSprite particleIcon, ItemTransforms transforms,
									RenderTypeGroup renderTypes,
									Function<String, IntList> quadIndices, int[][] quadOffsetsAndCounts, Int2ObjectArrayMap<String> toGroup)
		{
			super(unculledFaces, GroupedModel.CULLED_QUADS, hasAmbientOcclusion, usesBlockLight, isGui3d, particleIcon, transforms, renderTypes);
			this.unculledFaces = unculledFaces;
			this.quadIndices = quadIndices;
			this.quadOffsetsAndCounts = quadOffsetsAndCounts;
			this.toGroup = toGroup;
			
			this.itemModel = new MultiLayerItemBakedModel(
					unculledFaces, GroupedModel.CULLED_QUADS, hasAmbientOcclusion, usesBlockLight, isGui3d, particleIcon, transforms, renderTypes
			);
		}
		
		@Override
		public Stream<BakedQuad> getGroupedUnculledQuads(String group)
		{
			return quadIndices.apply(group)
					.intStream()
					.mapToObj(id -> quadOffsetsAndCounts[id])
					.flatMap(id -> IntStream.range(id[0], id[1])
							.mapToObj(unculledFaces::get)
					);
		}
		
		@Override
		public @NotNull List<BakedQuad> getQuads(@Nullable BlockState state, @Nullable Direction side, @NotNull RandomSource rand, @NotNull ModelData data, @Nullable RenderType renderType)
		{
			String group = RT_KEYS.apply(renderType);
			if(group == null || side == null) return List.of();
			return getGroupedUnculledQuads(group)
					.toList();
		}
		
		@Override
		public ChunkRenderTypeSet getRenderTypes(@NotNull BlockState state, @NotNull RandomSource rand, @NotNull ModelData data)
		{
			return ChunkRenderTypeSet.all();
		}
	}
	
	public static class MultiLayerItemBakedModel
			extends SimpleBakedModel
	{
		public MultiLayerItemBakedModel(List<BakedQuad> unculled, Map<Direction, List<BakedQuad>> culled, boolean ao, boolean bl, boolean g3d, TextureAtlasSprite sprite, ItemTransforms tf, RenderTypeGroup renderTypes)
		{
			super(unculled, culled, ao, bl, g3d, sprite, tf, renderTypes);
		}
	}
}
