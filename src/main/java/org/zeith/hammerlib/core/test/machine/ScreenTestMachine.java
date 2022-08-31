package org.zeith.hammerlib.core.test.machine;

import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.player.Inventory;
import org.zeith.hammerlib.client.screen.ScreenWTFMojang;
import org.zeith.hammerlib.client.utils.FXUtils;
import org.zeith.hammerlib.client.utils.RenderUtils;
import org.zeith.hammerlib.proxy.HLConstants;

public class ScreenTestMachine
		extends ScreenWTFMojang<ContainerTestMachine>
{
	public TileTestMachine tile;

	public ScreenTestMachine(ContainerTestMachine container, Inventory inv, Component label)
	{
		super(container, inv, label);
		this.tile = container.tile;
		setSize(176, 166);
	}
	
	@Override
	protected void containerTick()
	{
		menu.containerTick();
		super.containerTick();
	}
	
	@Override
	protected void renderBackground(PoseStack matrix, float partialTime, int mouseX, int mouseY)
	{
		FXUtils.bindTexture(HLConstants.MOD_ID, "textures/gui/test_machine.png");
		RenderUtils.drawTexturedModalRect(leftPos, topPos, 0, 0, imageWidth, imageHeight);

		float maxProgress = 200F;
		int mp = tile.maxProgress.getInt();
		if(mp > 0) maxProgress = mp;

		RenderUtils.drawTexturedModalRect(leftPos + 80, topPos + 35, imageWidth, 14, 22 * tile.progress.getInt() / maxProgress, 16);
	}
}