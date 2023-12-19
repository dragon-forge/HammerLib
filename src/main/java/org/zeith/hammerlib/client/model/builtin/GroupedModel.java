package org.zeith.hammerlib.client.model.builtin;

import com.google.common.collect.Lists;
import com.google.gson.*;
import it.unimi.dsi.fastutil.ints.*;
import net.minecraft.Util;
import net.minecraft.client.renderer.block.model.*;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.client.resources.model.*;
import net.minecraft.core.Direction;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.GsonHelper;
import net.minecraft.world.inventory.InventoryMenu;
import net.neoforged.neoforge.client.RenderTypeGroup;
import net.neoforged.neoforge.client.model.geometry.*;
import org.zeith.hammerlib.client.model.IUnbakedGeometry;
import org.zeith.hammerlib.client.model.*;
import org.zeith.hammerlib.mixins.client.BlockElementFaceAccessor;
import org.zeith.hammerlib.util.java.tuples.*;

import java.util.*;
import java.util.function.Function;
import java.util.stream.*;

@LoadUnbakedGeometry(path = "grouped")
public class GroupedModel
		implements IUnbakedGeometry<GroupedModel>
{
	private static final IntList EG = IntList.of();
	protected final List<BlockElement> elements;
	protected final IntList ungrouped;
	protected final Map<String, IntList> groups = new HashMap<>();
	protected final Map<String, String> textures = new HashMap<>();
	
	public GroupedModel(JsonObject obj, JsonDeserializationContext context)
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
			for(BlockElementFace value : element.faces.values())
			{
				if(value.texture.startsWith("#"))
				{
					var ntx = textures.getOrDefault(value.texture.substring(1), "missing");
					((BlockElementFaceAccessor) value).setTexture(ntx);
				}
			}
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
	public BakedModel bake(IGeometryBakingContext context, ModelBaker baker, Function<Material, TextureAtlasSprite> spriteGetter, ModelState modelState, ItemOverrides overrides, ResourceLocation modelLocation)
	{
		try
		{
			List<BakedQuad> quads = Lists.newArrayList();
			int[][] quadOffsetsAndCounts = new int[elements.size()][];
			
			for(int i = 0; i < elements.size(); i++)
			{
				var baked = UnbakedGeometryHelper.bakeElements(List.of(elements.get(i)), spriteGetter, modelState, modelLocation);
				quadOffsetsAndCounts[i] = new int[] { quads.size(), quads.size() + baked.size() };
				quads.addAll(baked);
			}
			
			TextureAtlasSprite particle = spriteGetter.apply(new Material(InventoryMenu.BLOCK_ATLAS, new ResourceLocation(textures.getOrDefault("particle", "particle"))));
			
			var renderTypeHint = context.getRenderTypeHint();
			var renderTypes = renderTypeHint != null ? context.getRenderType(renderTypeHint) : RenderTypeGroup.EMPTY;
			
			return new GroupedBakedModel(
					quads, context.useAmbientOcclusion(), context.useBlockLight(), context.isGui3d(), particle, context.getTransforms(), overrides, renderTypes,
					this::getGroup, quadOffsetsAndCounts
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
	
	public static final Map<Direction, List<BakedQuad>> CULLED_QUADS = Util.make(new HashMap<>(), map ->
	{
		for(var d : Direction.values())
			map.put(d, List.of());
	});
	
	public static class GroupedBakedModel
			extends SimpleBakedModel
			implements IGroupedQuadModel
	{
		protected final int[][] quadOffsetsAndCounts;
		protected final Function<String, IntList> quadIndices;
		
		public GroupedBakedModel(List<BakedQuad> unculledFaces,
								 boolean hasAmbientOcclusion, boolean usesBlockLight, boolean isGui3d,
								 TextureAtlasSprite particleIcon, ItemTransforms transforms, ItemOverrides overrides,
								 RenderTypeGroup renderTypes,
								 Function<String, IntList> quadIndices, int[][] quadOffsetsAndCounts)
		{
			super(unculledFaces, CULLED_QUADS, hasAmbientOcclusion, usesBlockLight, isGui3d, particleIcon, transforms, overrides, renderTypes);
			this.quadIndices = quadIndices;
			this.quadOffsetsAndCounts = quadOffsetsAndCounts;
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
	}
}
