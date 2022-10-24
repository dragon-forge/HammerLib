package org.zeith.hammerlib.core.test.machine;

import com.google.gson.*;
import net.minecraft.core.NonNullList;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.MinecraftServer;
import net.minecraft.tags.ItemTags;
import net.minecraft.util.GsonHelper;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.item.crafting.ShapedRecipe;
import net.minecraft.world.level.ItemLike;
import net.minecraftforge.common.Tags;
import net.minecraftforge.common.crafting.conditions.ICondition;
import org.zeith.hammerlib.HammerLib;
import org.zeith.hammerlib.annotations.Setup;
import org.zeith.hammerlib.api.crafting.*;
import org.zeith.hammerlib.api.crafting.building.*;
import org.zeith.hammerlib.api.crafting.impl.*;
import org.zeith.hammerlib.api.crafting.itf.IFileDecoder;
import org.zeith.hammerlib.core.RecipeHelper;
import org.zeith.hammerlib.event.recipe.ReloadRecipeRegistryEvent;
import org.zeith.hammerlib.util.mcf.itf.INetworkable;
import org.zeith.hammerlib.util.mcf.itf.IRecipeRegistrationEvent;

import java.util.Optional;

public class RecipeTestMachine
		extends BaseNameableRecipe
{
	public static final NamespacedRecipeRegistry<RecipeTestMachine> REGISTRY = RecipeRegistryFactory
			.namespacedBuilder(RecipeTestMachine.class)
			.registryId(new ResourceLocation("hammerlib", "test_machine"))
			.recipeBuilderFactory(TestMachineRecipeBuilder::new)
			.customRecipes(CustomTestMachineRecipeGenerator::new)
			.onClientRecipeReceive(clientRecipe ->
			{
				// TODO: Add to JEI runtime or other things that you care about.
			})
			.build();
	
	public final IItemIngredient<?> in1, in2;
	public final ItemStackResult output;
	public final int time;
	
	public RecipeTestMachine(ResourceLocation id, int time, ItemStack output, Object a, Object b)
	{
		this(id, time, new ItemStackResult(output), new MCIngredient(RecipeHelper.fromComponent(a)), new MCIngredient(RecipeHelper.fromComponent(b)));
	}
	
	public RecipeTestMachine(ResourceLocation id, int time, ItemStack output, IItemIngredient<?> a, IItemIngredient<?> b)
	{
		this(id, time, new ItemStackResult(output), a, b);
	}
	
	public RecipeTestMachine(ResourceLocation id, int time, ItemStackResult output, IItemIngredient<?> a, IItemIngredient<?> b)
	{
		super(id, output, NonNullList.of(a, b));
		this.time = time;
		this.output = output;
		this.in1 = a;
		this.in2 = b;
	}
	
	public int getTime()
	{
		return time;
	}
	
	public ItemStack getRecipeOutput(TileTestMachine executor)
	{
		return output.getOutput(executor);
	}
	
	@Setup
	public static void setup()
	{
		HammerLib.LOG.info("Setup Test Machine recipes!");
//		HammerLib.EVENT_BUS.addGenericListener(RecipeTestMachine.class, RecipeTestMachine::addTestMachineRecipes);
	}
	
	public static void addTestMachineRecipes(ReloadRecipeRegistryEvent.AddRecipes<RecipeTestMachine> evt)
	{
		if(!evt.is(RecipeTestMachine.REGISTRY)) return;
		
		var f = evt.<TestMachineRecipeBuilder> builderFactory();
		
		f.get()
				.result(Items.CHEST)
				.top(new TagIngredient(ItemTags.LOGS).quantify(2))
				.bottom(new TagIngredient(Tags.Items.INGOTS_IRON))
				.time(100)
				.register();
	}
	
	/**
	 * This is an example builder to use conjunction with recipe registration event.
	 * This builder is defined in {@link RecipeRegistryFactory.NamespacedBuilder#recipeBuilderFactory(IRecipeBuilderFactory)} and is also required to be specified during recipe registration.
	 */
	public static class TestMachineRecipeBuilder
			extends GeneralRecipeBuilder<RecipeTestMachine, TestMachineRecipeBuilder>
	{
		protected IItemIngredient<?> inputA, inputB;
		protected ItemStack result = ItemStack.EMPTY;
		protected int time = 100;
		
		public TestMachineRecipeBuilder(IRecipeRegistrationEvent<RecipeTestMachine> registrar)
		{
			super(registrar);
		}
		
		public TestMachineRecipeBuilder time(int time)
		{
			this.time = time;
			return this;
		}
		
		public TestMachineRecipeBuilder result(ItemStack stack)
		{
			this.result = stack;
			return this;
		}
		
		public TestMachineRecipeBuilder result(ItemLike provider)
		{
			this.result = new ItemStack(provider);
			return this;
		}
		
		public TestMachineRecipeBuilder result(ItemLike provider, int count)
		{
			this.result = new ItemStack(provider, count);
			return this;
		}
		
		public TestMachineRecipeBuilder top(IItemIngredient<?> in)
		{
			this.inputA = in;
			return this;
		}
		
		public TestMachineRecipeBuilder bottom(IItemIngredient<?> in)
		{
			this.inputB = in;
			return this;
		}
		
		@Override
		protected ResourceLocation generateId()
		{
			return registrar.nextId(result.getItem());
		}
		
		@Override
		protected void validate() throws IllegalStateException
		{
			if(inputA == null)
				throw new IllegalStateException(getClass().getSimpleName() + " does not have a defined input A!");
			if(inputB == null)
				throw new IllegalStateException(getClass().getSimpleName() + " does not have a defined input B!");
			if(result.isEmpty())
				throw new IllegalStateException(getClass().getSimpleName() + " does not have a defined output!");
		}
		
		@Override
		protected RecipeTestMachine createRecipe() throws IllegalStateException
		{
			return new RecipeTestMachine(getIdentifier(), time, result, inputA, inputB);
		}
	}
	
	/**
	 * This is an example of how modders should make custom json recipe readers.
	 */
	public static class CustomTestMachineRecipeGenerator
			extends CustomRecipeGenerator<RecipeTestMachine, GsonFileDecoder, JsonElement>
			implements INetworkable<RecipeTestMachine>
	{
		public CustomTestMachineRecipeGenerator(ResourceLocation registryPath)
		{
			super(registryPath, IFileDecoder::gson);
		}
		
		@Override
		public Optional<RecipeTestMachine> decodeRecipe(ResourceLocation recipeId, JsonElement jsonElement, MinecraftServer server, ICondition.IContext context)
		{
			try
			{
				if(jsonElement.isJsonObject() && !net.minecraftforge.common.crafting.CraftingHelper.processConditions(jsonElement.getAsJsonObject(), "conditions", context))
				{
					HammerLib.LOG.debug("Skipping loading recipe {} as it's conditions were not met", recipeId);
					return Optional.empty();
				}
				
				var recipe = fromJson(recipeId, GsonHelper.convertToJsonObject(jsonElement, "top element"), context);
				
				return Optional.of(recipe);
			} catch(IllegalArgumentException | JsonParseException jsonparseexception)
			{
				HammerLib.LOG.error("Parsing error loading recipe {}", recipeId, jsonparseexception);
			}
			
			return Optional.empty();
		}
		
		public static RecipeTestMachine fromJson(ResourceLocation recipeId, JsonObject root, ICondition.IContext context)
		{
			if(!root.has("top"))
				throw new JsonSyntaxException("Missing top, expected to find an Ingredient.");
			if(!root.has("bottom"))
				throw new JsonSyntaxException("Missing top, expected to find an Ingredient.");
			int time = GsonHelper.getAsInt(root, "time", 100);
			var result = ShapedRecipe.itemStackFromJson(GsonHelper.getAsJsonObject(root, "result"));
			var a = Ingredient.fromJson(root.get("top"));
			var b = Ingredient.fromJson(root.get("bottom"));
			return new RecipeTestMachine(recipeId, time, result, a, b);
		}
		
		@Override
		public Optional<JsonElement> createTemplate()
		{
			var $ = new JsonObject();
			$.addProperty("time", 100);
			$.add("result", itemStackTemplate());
			$.add("top", ingredientTemplate());
			$.add("bottom", ingredientTemplate());
			return Optional.of($);
		}
		
		@Override
		public Optional<INetworkable<RecipeTestMachine>> getSerializer()
		{
			return Optional.of(this);
		}
		
		@Override
		public void toNetwork(FriendlyByteBuf buf, RecipeTestMachine obj)
		{
			buf.writeResourceLocation(obj.getRecipeName());
			
			buf.writeInt(obj.time);
			buf.writeItemStack(obj.output.getBaseOutput(), false);
			
			buf.writeByte(obj.in1.getCount());
			obj.in1.asIngredient().toNetwork(buf);
			
			buf.writeByte(obj.in2.getCount());
			obj.in2.asIngredient().toNetwork(buf);
		}
		
		@Override
		public RecipeTestMachine fromNetwork(FriendlyByteBuf buf)
		{
			var id = buf.readResourceLocation();
			
			int time = buf.readInt();
			var result = buf.readItem();
			
			int in1C = buf.readByte();
			var in1 = new MCIngredient(Ingredient.fromNetwork(buf)).quantify(in1C);
			
			int in2C = buf.readByte();
			var in2 = new MCIngredient(Ingredient.fromNetwork(buf)).quantify(in2C);
			
			return new RecipeTestMachine(id, time, result, in1, in2);
		}
	}
}