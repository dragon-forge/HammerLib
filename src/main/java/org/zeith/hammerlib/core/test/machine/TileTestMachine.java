package org.zeith.hammerlib.core.test.machine;

import net.minecraft.block.BlockState;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.inventory.container.Container;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ItemUseContext;
import net.minecraft.state.properties.BlockStateProperties;
import net.minecraft.tileentity.TileEntityType;
import net.minecraft.util.Direction;
import net.minecraft.util.ResourceLocation;
import org.zeith.api.wrench.IWrenchable;
import org.zeith.hammerlib.annotations.OnlyIf;
import org.zeith.hammerlib.annotations.RegistryName;
import org.zeith.hammerlib.annotations.SimplyRegister;
import org.zeith.hammerlib.annotations.TileRenderer;
import org.zeith.hammerlib.api.crafting.ICraftingExecutor;
import org.zeith.hammerlib.api.forge.TileAPI;
import org.zeith.hammerlib.api.inv.SimpleInventory;
import org.zeith.hammerlib.api.io.NBTSerializable;
import org.zeith.hammerlib.api.tiles.IContainerTile;
import org.zeith.hammerlib.core.test.TestPreferences;
import org.zeith.hammerlib.net.properties.PropertyInt;
import org.zeith.hammerlib.net.properties.PropertyResourceLocation;
import org.zeith.hammerlib.tiles.TileSyncableTickable;
import org.zeith.hammerlib.util.java.DirectStorage;

@SimplyRegister
public class TileTestMachine
		extends TileSyncableTickable
		implements IContainerTile, ICraftingExecutor, IWrenchable
{
	@OnlyIf(owner = TestPreferences.class, member = "enableTestMachine")
	@TileRenderer(TESRTestMachine.class)
	@RegistryName("test_machine")
	public static final TileEntityType<TileTestMachine> TEST_MACHINE = TileAPI.createType(TileTestMachine.class, BlockTestMachine.TEST_MACHINE);

	@NBTSerializable
	private int _progress;

	@NBTSerializable
	private int _maxProgress = 200;

	@NBTSerializable
	public final SimpleInventory inventory = new SimpleInventory(3);

	@NBTSerializable
	private ResourceLocation _activeRecipeId;

	public final PropertyInt progress = new PropertyInt(DirectStorage.create(i -> _progress = i, () -> _progress));
	public final PropertyInt maxProgress = new PropertyInt(DirectStorage.create(i -> _maxProgress = i, () -> _maxProgress));
	public final PropertyResourceLocation activeRecipeId = new PropertyResourceLocation(DirectStorage.create(r -> _activeRecipeId = r, () -> _activeRecipeId));

	public TileTestMachine()
	{
		super(TEST_MACHINE);
		this.inventory.isStackValid = (i, s) -> i != 2;
		this.dispatcher.registerProperty("progress", progress);
		this.dispatcher.registerProperty("max_progress", maxProgress);
		this.dispatcher.registerProperty("ar_id", activeRecipeId);
	}

	@Override
	public void update()
	{
		if(isOnServer())
		{
			RecipeTestMachine r = getActiveRecipe();

			if(isValidRecipe(r))
			{
				int p = progress.getInt() + 1;
				if(p >= r.getTime())
				{
					ItemStack result = r.getRecipeOutput(this);
					if(output(result))
					{
						inventory.getItem(0).shrink(r.in1.getCount());
						inventory.getItem(1).shrink(r.in2.getCount());

						if(!isValidRecipe(r))
						{
							activeRecipeId.set(null);
							r = null;
							setEnabledState(false);
						}

						progress.setInt(0);
					}
				} else
					progress.setInt(p);
			} else
			{
				if(_progress > 0)
				{
					progress.setInt(_progress - 1);
					if(progress.getInt() <= 0)
						setEnabledState(false);
				}

				activeRecipeId.set(null);
				r = null;
			}

			if(r == null && atTickRate(10))
			{
				RecipeTestMachine recipe = RecipeTestMachine.REGISTRY.getRecipes().stream().filter(this::isValidRecipe).findFirst().orElse(null);
				if(recipe != null)
				{
					if(recipe.time != maxProgress.getInt())
						progress.setInt(0);
					maxProgress.setInt(recipe.time);
					activeRecipeId.set(recipe.getRecipeName());
					setEnabledState(true);
				}
			}
		}
	}

	public void setEnabledState(boolean enabled)
	{
		BlockState s = level.getBlockState(worldPosition);
		if(s.getBlock() == BlockTestMachine.TEST_MACHINE)
			level.setBlockAndUpdate(worldPosition, s.setValue(BlockStateProperties.ENABLED, enabled));
	}

	public RecipeTestMachine getActiveRecipe()
	{
		return RecipeTestMachine.REGISTRY.getRecipe(_activeRecipeId);
	}

	private boolean output(ItemStack stack)
	{
		if(canStore(stack))
		{
			ItemStack s = inventory.getItem(2);
			if(s.isEmpty()) inventory.setItem(2, stack);
			else inventory.getItem(2).grow(stack.getCount());
			return true;
		}
		return false;
	}

	private boolean isValidRecipe(RecipeTestMachine recipe)
	{
		return recipe != null
				&& recipe.in1.asIngredient().test(inventory.getItem(0)) && inventory.getItem(0).getCount() >= recipe.in1.getCount()
				&& recipe.in2.asIngredient().test(inventory.getItem(1)) && inventory.getItem(1).getCount() >= recipe.in2.getCount()
				&& canStore(recipe.getRecipeOutput(this));
	}

	private boolean canStore(ItemStack result)
	{
		ItemStack stack = inventory.getItem(2);
		if(stack.isEmpty()) return true;
		if(!ItemStack.isSame(result, stack)) return false;
		return stack.getCount() + result.getCount() <= Math.min(inventory.getMaxStackSize(), result.getMaxStackSize());
	}

	@Override
	public Container openContainer(PlayerEntity player, int windowId)
	{
		return new ContainerTestMachine(player, windowId, this);
	}

	@Override
	public boolean onWrenchUsed(ItemUseContext context)
	{
		Direction d = context.getClickedFace();
		if(context.getPlayer().isShiftKeyDown()) d = d.getOpposite();

		BlockState state = level.getBlockState(worldPosition);
		if(state.getBlock() == BlockTestMachine.TEST_MACHINE)
		{
			Direction facing = state.getValue(BlockStateProperties.HORIZONTAL_FACING);
			final Direction origin = facing;

			if(d == Direction.UP)
				facing = facing.getClockWise();
			else if(d == Direction.DOWN)
				facing = facing.getCounterClockWise();
			else
				facing = d;

			if(origin != facing)
			{
				level.setBlockAndUpdate(worldPosition, state.setValue(BlockStateProperties.HORIZONTAL_FACING, facing));
			}
		}

		return true;
	}
}