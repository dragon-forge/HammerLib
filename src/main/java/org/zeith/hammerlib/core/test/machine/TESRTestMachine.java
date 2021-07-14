package org.zeith.hammerlib.core.test.machine;

import com.mojang.blaze3d.matrix.MatrixStack;
import net.minecraft.client.renderer.IRenderTypeBuffer;
import net.minecraft.client.renderer.tileentity.TileEntityRendererDispatcher;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import org.zeith.hammerlib.client.render.tile.ITESR;

@OnlyIn(Dist.CLIENT)
public class TESRTestMachine
		implements ITESR<TileTestMachine>
{
	@Override
	public void render(TileTestMachine tile, float partial, MatrixStack matrix, IRenderTypeBuffer buf, int lighting, int overlay, TileEntityRendererDispatcher renderer)
	{
//		int mp = tile.maxProgress.getInt();
//		int p = tile.progress.getInt();
//		float progress = mp > 0 ? p / (float) mp : 0F;
//		RenderUtils.renderLightRayEffects(buf, matrix, 0xFF66FF55, MathHelper.getSeed(tile.getBlockPos()), progress, 5, 2F, 32);
	}
}