package org.zeith.hammerlib.core.test.machine;

import com.mojang.serialization.MapCodec;
import net.minecraft.core.*;
import net.minecraft.world.*;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.*;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.*;
import net.minecraft.world.level.block.entity.*;
import net.minecraft.world.level.block.state.*;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.storage.loot.LootParams;
import net.minecraft.world.phys.BlockHitResult;
import org.zeith.hammerlib.annotations.*;
import org.zeith.hammerlib.api.forge.*;
import org.zeith.hammerlib.core.adapter.*;
import org.zeith.hammerlib.proxy.HLConstants;
import org.zeith.hammerlib.util.java.Cast;

import javax.annotation.Nullable;
import java.util.List;

@SimplyRegister
public class BlockTestMachine
		extends BaseEntityBlock
{
	@RegistryName("test_machine")
	public static final BlockTestMachine TEST_MACHINE = new BlockTestMachine(Block.Properties
			.of()
			.requiresCorrectToolForDrops()
			.sound(SoundType.METAL)
			.strength(1.5F));
	
	public BlockTestMachine(Block.Properties properties)
	{
		super(properties);
		
		BlockHarvestAdapter.bindTool(BlockHarvestAdapter.MineableType.PICKAXE, Tiers.IRON, this);
		CreativeTabAdapter.bindTab(this, HLConstants.HL_TAB);
	}
	
	@Override
	public List<ItemStack> getDrops(BlockState p_60537_, LootParams.Builder p_60538_)
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
	
	public static final MapCodec<BlockTestMachine> CODEC = simpleCodec(BlockTestMachine::new);
	
	@Override
	protected MapCodec<? extends BaseEntityBlock> codec()
	{
		return CODEC;
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
}