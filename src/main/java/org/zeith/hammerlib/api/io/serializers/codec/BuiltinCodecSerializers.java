package org.zeith.hammerlib.api.io.serializers.codec;

import com.google.common.base.Suppliers;
import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.core.GlobalPos;
import net.minecraft.world.level.ChunkPos;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import net.neoforged.neoforge.fluids.FluidStack;
import org.zeith.hammerlib.util.java.Cast;
import org.zeith.hammerlib.util.mcf.fluid.FluidIngredient;
import org.zeith.hammerlib.util.mcf.fluid.FluidIngredientStack;

public interface BuiltinCodecSerializers
{
	@CodecSerializer
	ICodecSerializer<BlockState> BLOCK_STATE_CODEC = ICodecSerializer.of(BlockState.class, BlockState.CODEC, Suppliers.memoize(Blocks.AIR::defaultBlockState));
	
	@CodecSerializer
	ICodecSerializer<FluidStack> FLUID_STACK_CODEC = ICodecSerializer.of(FluidStack.class, FluidStack.CODEC, Cast.constant(FluidStack.EMPTY));
	
	@CodecSerializer
	ICodecSerializer<GlobalPos> GLOBAL_POS_CODEC = ICodecSerializer.of(GlobalPos.class, GlobalPos.CODEC);
	
	@CodecSerializer
	ICodecSerializer<FluidIngredient> FLUID_INGREDIENT_CODEC = ICodecSerializer.of(FluidIngredient.class, FluidIngredient.CODEC.codec(), Cast.constant(FluidIngredient.EMPTY));
	
	@CodecSerializer
	ICodecSerializer<FluidIngredientStack> FLUID_INGREDIENT_STACK_CODEC = ICodecSerializer.of(FluidIngredientStack.class, FluidIngredientStack.CODEC, Cast.constant(FluidIngredientStack.EMPTY));
	
	@CodecSerializer
	ICodecSerializer<ChunkPos> CHUNK_POS_CODEC = ICodecSerializer.of(ChunkPos.class, RecordCodecBuilder.create(inst ->
			inst.group(
					Codec.INT.fieldOf("x").forGetter(c -> c.x),
					Codec.INT.fieldOf("z").forGetter(c -> c.z)
			).apply(inst, ChunkPos::new)
	));
}