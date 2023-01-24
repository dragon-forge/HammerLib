package org.zeith.hammerlib.core.test.machine;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.*;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Tiers;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.*;
import net.minecraft.world.level.block.entity.*;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.material.Material;
import net.minecraft.world.level.storage.loot.LootContext;
import net.minecraft.world.phys.BlockHitResult;
import org.jetbrains.annotations.NotNull;
import org.zeith.hammerlib.annotations.RegistryName;
import org.zeith.hammerlib.annotations.SimplyRegister;
import org.zeith.hammerlib.api.blocks.ICreativeTabBlock;
import org.zeith.hammerlib.api.forge.BlockAPI;
import org.zeith.hammerlib.api.forge.ContainerAPI;
import org.zeith.hammerlib.api.items.CreativeTab;
import org.zeith.hammerlib.core.adapter.BlockHarvestAdapter;
import org.zeith.hammerlib.proxy.HLConstants;
import org.zeith.hammerlib.util.java.Cast;

import javax.annotation.Nullable;
import java.util.List;

@SimplyRegister
public class BlockTestMachine
		extends BaseEntityBlock
		implements ICreativeTabBlock
{
	@RegistryName("test_machine")
	public static final BlockTestMachine TEST_MACHINE = new BlockTestMachine();
	
	public BlockTestMachine()
	{
		super(Block.Properties
				.of(Material.METAL)
				.requiresCorrectToolForDrops()
				.sound(SoundType.METAL)
				.strength(1.5F)
		);
		
		BlockHarvestAdapter.bindTool(BlockHarvestAdapter.MineableType.PICKAXE, Tiers.IRON, this);
	}
	
	@Override
	public List<ItemStack> getDrops(BlockState p_60537_, LootContext.Builder p_60538_)
	{
		return List.of(new ItemStack(this));
	}
	
	@Override
	public void onRemove(BlockState prevState, Level world, BlockPos pos, BlockState newState, boolean flag64)
	{
		if(!prevState.is(newState.getBlock()))
		{
			BlockEntity tileentity = world.getBlockEntity(pos);
			if(tileentity instanceof TileTestMachine)
			{
				Containers.dropContents(world, pos, ((TileTestMachine) tileentity).inventory);
				world.updateNeighbourForOutputSignal(pos, this);
			}
			
			super.onRemove(prevState, world, pos, newState, flag64);
		}
	}
	
	@Nullable
	@Override
	public <T extends BlockEntity> BlockEntityTicker<T> getTicker(Level level, BlockState state, BlockEntityType<T> type)
	{
		return BlockAPI.ticker(level);
	}
	
	@Override
	public RenderShape getRenderShape(BlockState p_49232_)
	{
		return RenderShape.MODEL;
	}
	
	@Override
	protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder)
	{
		builder.add(BlockStateProperties.ENABLED);
		builder.add(BlockStateProperties.HORIZONTAL_FACING);
	}
	
	@Override
	public InteractionResult use(BlockState state, Level world, BlockPos pos, Player player, InteractionHand hand, BlockHitResult ray)
	{
		ContainerAPI.openContainerTile(player, Cast.cast(world.getBlockEntity(pos), TileTestMachine.class));
		return InteractionResult.SUCCESS;
	}
	
	@Nullable
	@Override
	public BlockEntity newBlockEntity(BlockPos pos, BlockState state)
	{
		return new TileTestMachine(pos, state);
	}
	
	@Nullable
	@Override
	public BlockState getStateForPlacement(BlockPlaceContext ctx)
	{
		return defaultBlockState()
				.setValue(BlockStateProperties.ENABLED, false)
				.setValue(BlockStateProperties.HORIZONTAL_FACING, ctx.getPlayer() != null
						? ctx.getPlayer().getDirection().getOpposite()
						: Direction.NORTH);
	}
	
	@Override
	@NotNull
	public CreativeTab getCreativeTab()
	{
		return HLConstants.HL_TAB;
	}
}