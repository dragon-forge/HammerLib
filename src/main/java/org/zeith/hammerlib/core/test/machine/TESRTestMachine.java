package org.zeith.hammerlib.core.test.machine;

import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.util.Mth;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import org.zeith.hammerlib.client.render.tile.IBESR;
import org.zeith.hammerlib.client.utils.RenderUtils;

@OnlyIn(Dist.CLIENT)
public class TESRTestMachine
		implements IBESR<TileTestMachine>
{
	@Override
	public void render(TileTestMachine tile, float partial, PoseStack matrix, MultiBufferSource buf, int lighting, int overlay)
	{
		matrix.translate(0.5, 1, 0.5);
		
		BlockState s = tile.getLevel().getBlockState(tile.getBlockPos());
		if(s.getBlock() == BlockTestMachine.TEST_MACHINE)
		{
			boolean e = s.getValue(BlockStateProperties.ENABLED);
			
			int mp = tile.maxProgress.getInt();
			float p = Math.min(mp, tile.progress.getInt());
			float progress = mp > 0 ? p / mp : 0F;
			
			if(e)
				RenderUtils.renderLightRayEffects(buf, matrix,
						Mth.hsvToRgb(progress / 3F, 1, 1) | 0xFF << 24,
						Mth.getSeed(tile.getBlockPos()), progress, 5, progress, 32);
		}
	}
}