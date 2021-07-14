package org.zeith.hammerlib.core.test.machine;

import com.mojang.blaze3d.matrix.MatrixStack;
import net.minecraft.client.renderer.IRenderTypeBuffer;
import net.minecraft.client.renderer.tileentity.TileEntityRenderer;
import net.minecraft.client.renderer.tileentity.TileEntityRendererDispatcher;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

@OnlyIn(Dist.CLIENT)
public class TESRTestMachine
		extends TileEntityRenderer<TileTestMachine>
{
	public TESRTestMachine(TileEntityRendererDispatcher dispatcher)
	{
		super(dispatcher);
	}

	@Override
	public void render(TileTestMachine tile, float partialTicks, MatrixStack matrix, IRenderTypeBuffer buf, int light, int overlayTexture)
	{
//		int mp = tile.maxProgress.getInt();
//		int p = tile.progress.getInt();
//		float progress = mp > 0 ? p / (float) mp : 0F;
//		RenderUtils.renderLightRayEffects(buf, matrix, 0xFF66FF55, MathHelper.getSeed(tile.getBlockPos()), progress, 5, 2F, 32);
	}
}