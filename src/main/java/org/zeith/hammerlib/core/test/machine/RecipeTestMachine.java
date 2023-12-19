package org.zeith.hammerlib.core.test.machine;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import lombok.Getter;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.util.ExtraCodecs;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.*;
import net.minecraft.world.level.ItemLike;
import net.neoforged.api.distmarker.*;
import org.jetbrains.annotations.Nullable;
import org.zeith.hammerlib.abstractions.recipes.*;
import org.zeith.hammerlib.abstractions.recipes.layout.*;
import org.zeith.hammerlib.annotations.*;
import org.zeith.hammerlib.api.recipes.*;
import org.zeith.hammerlib.client.render.IGuiDrawable;
import org.zeith.hammerlib.core.adapter.recipe.RecipeBuilder;
import org.zeith.hammerlib.util.mcf.itf.IRecipeRegistrationEvent;

import java.util.function.Consumer;
import java.util.stream.Stream;

@SimplyRegister
public class RecipeTestMachine
		extends BaseRecipe<RecipeTestMachine>
{
	@RegistryName("test_machine")
	public static final TestMachineRecipeType TYPE = new TestMachineRecipeType();
	
	public final IngredientWithCount inputA, inputB;
	public final ItemStack output;
	@Getter
	public final int time;
	
	public RecipeTestMachine(String group,
							 int time, ItemStack output,
							 IngredientWithCount inputA, IngredientWithCount inputB)
	{
		super(group);
		this.vanillaResult = output;
		if(!inputA.isEmpty()) this.vanillaIngredients.addAll(inputA.applyCount());
		if(!inputB.isEmpty()) this.vanillaIngredients.addAll(inputB.applyCount());
		this.time = time;
		this.inputA = inputA;
		this.inputB = inputB;
		this.output = output;
	}
	
	@Override
	protected SerializableRecipeType<RecipeTestMachine> getRecipeType()
	{
		return TYPE;
	}
	
	public ItemStack getRecipeOutput(TileTestMachine machine)
	{
		return output.copy();
	}
	
	public static class TestMachineRecipeType
			extends SerializableRecipeType<RecipeTestMachine>
	{
		private static final Codec<RecipeTestMachine> CODEC = RecordCodecBuilder.create(
				inst -> inst.group(
						ExtraCodecs.strictOptionalField(Codec.STRING, "group", "").forGetter(RecipeTestMachine::getGroup),
						ExtraCodecs.strictOptionalField(Codec.INT, "time", 200).forGetter(RecipeTestMachine::getTime),
						CraftingRecipeCodecs.ITEMSTACK_OBJECT_CODEC.fieldOf("result").forGetter(o -> o.output),
						IngredientWithCount.CODEC.fieldOf("a").forGetter(o -> o.inputA),
						IngredientWithCount.CODEC.fieldOf("b").forGetter(o -> o.inputB)
				).apply(inst, RecipeTestMachine::new)
		);
		
		@Override
		public Codec<RecipeTestMachine> codec()
		{
			return CODEC;
		}
		
		@Override
		public void toNetwork(FriendlyByteBuf buf, RecipeTestMachine recipe)
		{
			recipe.inputA.toNetwork(buf);
			recipe.inputB.toNetwork(buf);
			buf.writeVarInt(recipe.time);
			buf.writeItemStack(recipe.output, false);
			buf.writeUtf(recipe.group);
		}
		
		@Override
		public @Nullable RecipeTestMachine fromNetwork(FriendlyByteBuf buf)
		{
			var ingrA = IngredientWithCount.fromNetwork(buf);
			var ingrB = IngredientWithCount.fromNetwork(buf);
			int time = buf.readVarInt();
			var res = buf.readItem();
			var group = buf.readUtf();
			return new RecipeTestMachine(group, time, res, ingrA, ingrB);
		}
		
		@Override
		public void initVisuals(Consumer<IRecipeVisualizer<RecipeTestMachine, ?>> viualizerConsumer)
		{
			viualizerConsumer.accept(IRecipeVisualizer.simple(VisualizedTestMachine.class,
					IRecipeVisualizer.groupBuilder()
							.title(BlockTestMachine.TEST_MACHINE.getName())
							.size(52, 36)
							.icon(IGuiDrawable.ofItem(new ItemStack(BlockTestMachine.TEST_MACHINE)))
							.catalyst(new ItemStack(BlockTestMachine.TEST_MACHINE))
							.build(),
					VisualizedTestMachine::new
			));
		}
	}
	
	public static class TestMachineRecipeBuilder
			extends RecipeBuilder<TestMachineRecipeBuilder, Recipe<?>>
	{
		protected IngredientWithCount inputA, inputB;
		protected int time = 100;
		
		public TestMachineRecipeBuilder(IRecipeRegistrationEvent<Recipe<?>> event)
		{
			super(event);
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
		
		public TestMachineRecipeBuilder top(Object in, int count)
		{
			this.inputA = parseIngredient(in, count);
			return this;
		}
		
		public TestMachineRecipeBuilder bottom(Object in, int count)
		{
			this.inputB = parseIngredient(in, count);
			return this;
		}
		
		@Override
		protected void validate()
				throws IllegalStateException
		{
			if(inputA == null)
				throw new IllegalStateException(getClass().getSimpleName() + " does not have a defined input A!");
			if(inputB == null)
				throw new IllegalStateException(getClass().getSimpleName() + " does not have a defined input B!");
			if(result.isEmpty())
				throw new IllegalStateException(getClass().getSimpleName() + " does not have a defined output!");
		}
		
		@Override
		public void register()
				throws IllegalStateException
		{
			validate();
			
			var id = getIdentifier();
			event.register(id, new RecipeTestMachine(group, time, result, inputA, inputB));
		}
	}
	
	@OnlyIn(Dist.CLIENT)
	public static class VisualizedTestMachine
			implements IVisualizedRecipe<RecipeTestMachine>
	{
		final RecipeHolder<RecipeTestMachine> recipe;
		
		public VisualizedTestMachine(RecipeHolder<RecipeTestMachine> recipe)
		{
			this.recipe = recipe;
		}
		
		@Override
		public RecipeHolder<RecipeTestMachine> getRecipe()
		{
			return recipe;
		}
		
		@Override
		public void setupLayout(IVisualizerBuilder builder)
		{
			var recipe = this.recipe.value();
			
			builder.addSlot(ISlotBuilder.SlotRole.INPUT, 0, 0)
					.addItemStacks(Stream.of(recipe.inputA.input().getItems()).peek(s -> s.setCount(recipe.inputA.count())).toList())
					.build();
			
			builder.addSlot(ISlotBuilder.SlotRole.INPUT, 0, 18)
					.addItemStacks(Stream.of(recipe.inputB.input().getItems()).peek(s -> s.setCount(recipe.inputB.count())).toList())
					.build();
			
			builder.addSlot(ISlotBuilder.SlotRole.OUTPUT, 36, 9)
					.addItemStack(recipe.output)
					.build();
		}
	}
}