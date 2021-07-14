package org.zeith.hammerlib.core.test.machine;

import net.minecraft.block.*;
import net.minecraft.block.material.Material;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.inventory.InventoryHelper;
import net.minecraft.item.BlockItemUseContext;
import net.minecraft.item.ItemGroup;
import net.minecraft.state.StateContainer;
import net.minecraft.state.properties.BlockStateProperties;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.ActionResultType;
import net.minecraft.util.Direction;
import net.minecraft.util.Hand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.BlockRayTraceResult;
import net.minecraft.world.IBlockReader;
import net.minecraft.world.World;
import net.minecraftforge.common.ToolType;
import org.zeith.hammerlib.annotations.OnlyIf;
import org.zeith.hammerlib.annotations.RegistryName;
import org.zeith.hammerlib.annotations.SimplyRegister;
import org.zeith.hammerlib.api.blocks.IItemGroupBlock;
import org.zeith.hammerlib.api.forge.ContainerAPI;
import org.zeith.hammerlib.core.test.TestPreferences;
import org.zeith.hammerlib.util.java.Cast;

import javax.annotation.Nullable;

@SimplyRegister
public class BlockTestMachine
		extends ContainerBlock
		implements IItemGroupBlock
{
	@OnlyIf(owner = TestPreferences.class, member = "enableTestMachine")
	@RegistryName("test_machine")
	public static final BlockTestMachine TEST_MACHINE = new BlockTestMachine();

	public BlockTestMachine()
	{
		super(Block.Properties
				.of(Material.METAL)
				.sound(SoundType.METAL)
				.harvestTool(ToolType.PICKAXE)
				.harvestLevel(1)
				.strength(1F)
		);
	}

	@Override
	public void onRemove(BlockState prevState, World world, BlockPos pos, BlockState newState, boolean flag64)
	{
		if(!prevState.is(newState.getBlock()))
		{
			TileEntity tileentity = world.getBlockEntity(pos);
			if(tileentity instanceof TileTestMachine)
			{
				InventoryHelper.dropContents(world, pos, ((TileTestMachine) tileentity).inventory);
				world.updateNeighbourForOutputSignal(pos, this);
			}

			super.onRemove(prevState, world, pos, newState, flag64);
		}
	}

	@Override
	public BlockRenderType getRenderShape(BlockState state)
	{
		return BlockRenderType.MODEL;
	}

	@Override
	protected void createBlockStateDefinition(StateContainer.Builder<Block, BlockState> builder)
	{
		builder.add(BlockStateProperties.ENABLED);
		builder.add(BlockStateProperties.HORIZONTAL_FACING);
	}

	@Override
	public ActionResultType use(BlockState state, World world, BlockPos pos, PlayerEntity player, Hand hand, BlockRayTraceResult ray)
	{
		ContainerAPI.openContainerTile(player, Cast.cast(world.getBlockEntity(pos), TileTestMachine.class));
		return ActionResultType.SUCCESS;
	}

	@Nullable
	@Override
	public TileEntity newBlockEntity(IBlockReader p_196283_1_)
	{
		return new TileTestMachine();
	}

	@Nullable
	@Override
	public BlockState getStateForPlacement(BlockItemUseContext ctx)
	{
		return defaultBlockState()
				.setValue(BlockStateProperties.ENABLED, false)
				.setValue(BlockStateProperties.HORIZONTAL_FACING, ctx.getPlayer() != null
						? ctx.getPlayer().getDirection().getOpposite()
						: Direction.NORTH);
	}

	@Override
	public ItemGroup getItemGroup()
	{
		return ItemGroup.TAB_REDSTONE;
	}
}