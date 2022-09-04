package org.zeith.hammerlib.core.test.machine;

import net.minecraft.core.NonNullList;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.ItemTags;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.ItemLike;
import net.minecraftforge.common.Tags;
import org.zeith.hammerlib.HammerLib;
import org.zeith.hammerlib.annotations.Setup;
import org.zeith.hammerlib.api.crafting.*;
import org.zeith.hammerlib.api.crafting.building.GeneralRecipeBuilder;
import org.zeith.hammerlib.api.crafting.building.IRecipeBuilderFactory;
import org.zeith.hammerlib.api.crafting.impl.*;
import org.zeith.hammerlib.core.RecipeHelper;
import org.zeith.hammerlib.event.recipe.ReloadRecipeRegistryEvent;
import org.zeith.hammerlib.util.mcf.itf.IRecipeRegistrationEvent;

public class RecipeTestMachine
		extends BaseNameableRecipe
{
	public static final NamespacedRecipeRegistry<RecipeTestMachine> REGISTRY = RecipeRegistryFactory
			.namespacedBuilder(RecipeTestMachine.class)
			.registryId(new ResourceLocation("hammerlib", "test_machine"))
			.recipeBuilderFactory(TestMachineRecipeBuilder::new)
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
		if(evt.is(RecipeTestMachine.REGISTRY))
		{
			var f = evt.<TestMachineRecipeBuilder> builderFactory();
			
			f.get()
					.result(Items.CHEST)
					.top(new TagIngredient(ItemTags.LOGS).quantify(2))
					.bottom(new TagIngredient(Tags.Items.INGOTS_IRON))
					.time(100)
					.register();
		}
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
				throw new IllegalStateException(getClass().getSimpleName() + " does not have a defined oputput!");
		}
		
		@Override
		protected RecipeTestMachine createRecipe() throws IllegalStateException
		{
			return new RecipeTestMachine(getIdentifier(), time, result, inputA, inputB);
		}
	}
}