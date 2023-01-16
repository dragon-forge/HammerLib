package org.zeith.hammerlib.core.test.machine;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.context.UseOnContext;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import org.zeith.api.wrench.IWrenchable;
import org.zeith.hammerlib.annotations.RegistryName;
import org.zeith.hammerlib.annotations.SimplyRegister;
import org.zeith.hammerlib.annotations.client.TileRenderer;
import org.zeith.hammerlib.api.crafting.ICraftingExecutor;
import org.zeith.hammerlib.api.forge.BlockAPI;
import org.zeith.hammerlib.api.inv.SimpleInventory;
import org.zeith.hammerlib.api.io.NBTSerializable;
import org.zeith.hammerlib.api.tiles.IContainerTile;
import org.zeith.hammerlib.core.RecipeHelper;
import org.zeith.hammerlib.net.properties.PropertyInt;
import org.zeith.hammerlib.net.properties.PropertyResourceLocation;
import org.zeith.hammerlib.tiles.TileSyncableTickable;
import org.zeith.hammerlib.util.java.DirectStorage;

@SimplyRegister
public class TileTestMachine
		extends TileSyncableTickable
		implements IContainerTile, ICraftingExecutor, IWrenchable
{
	@TileRenderer(TESRTestMachine.class)
	@RegistryName("test_machine")
	public static final BlockEntityType<TileTestMachine> TEST_MACHINE = BlockAPI.createBlockEntityType(TileTestMachine::new, BlockTestMachine.TEST_MACHINE);
	
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
	
	public TileTestMachine(BlockPos pos, BlockState state)
	{
		super(TEST_MACHINE, pos, state);
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
				int p = _progress + 1;
				if(p >= r.getTime())
				{
					ItemStack result = r.getRecipeOutput(this);
					if(output(result))
					{
						inventory.getItem(0).shrink(r.inputA.count());
						inventory.getItem(1).shrink(r.inputA.count());
						
						if(!isValidRecipe(r))
						{
							activeRecipeId.set(null);
							r = null;
							setEnabledState(false);
						}
						
						_progress = (0);
					}
				} else
					_progress = (p);
			} else
			{
				if(_progress > 0)
				{
					_progress = (_progress - 1);
					if(_progress <= 0)
						setEnabledState(false);
				}
				
				activeRecipeId.set(null);
				r = null;
			}
			
			if(r == null && atTickRate(10))
			{
				RecipeTestMachine recipe = RecipeHelper.getRecipes(level, RecipeTestMachine.TYPE).filter(this::isValidRecipe).findFirst().orElse(null);
				if(recipe != null)
				{
					if(recipe.time != _maxProgress)
						_progress = 0;
					_maxProgress = recipe.time;
					activeRecipeId.set(recipe.getId());
					setEnabledState(true);
				}
			}
		}
	}
	
	public void setEnabledState(boolean enabled)
	{
		BlockState s = level.getBlockState(worldPosition);
		if(s.getBlock() == BlockTestMachine.TEST_MACHINE)
		{
			level.setBlockAndUpdate(worldPosition, s = s.setValue(BlockStateProperties.ENABLED, enabled));
			setBlockState(s);
		}
	}
	
	public RecipeTestMachine getActiveRecipe()
	{
		return RecipeHelper.getRecipeMap(level, RecipeTestMachine.TYPE)
				.get(_activeRecipeId);
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
				&& recipe.inputA.test(inventory.getItem(0))
				&& recipe.inputB.test(inventory.getItem(1))
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
	public AbstractContainerMenu openContainer(Player player, int windowId)
	{
		return new ContainerTestMachine(player, windowId, this);
	}
	
	@Override
	public boolean onWrenchUsed(UseOnContext context)
	{
		if(context.getLevel().isClientSide())
			return true;
		
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
				level.setBlockAndUpdate(worldPosition, state.setValue(BlockStateProperties.HORIZONTAL_FACING, facing));
		}
		
		return true;
	}
}