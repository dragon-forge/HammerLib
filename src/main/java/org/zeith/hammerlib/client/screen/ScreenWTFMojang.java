package org.zeith.hammerlib.client.screen;

import com.mojang.blaze3d.matrix.MatrixStack;
import net.minecraft.client.gui.screen.inventory.ContainerScreen;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.inventory.container.Container;
import net.minecraft.util.text.ITextComponent;

public abstract class ScreenWTFMojang<T extends Container>
		extends ContainerScreen<T>
{
	public ScreenWTFMojang(T container, PlayerInventory plyerInv, ITextComponent name)
	{
		super(container, plyerInv, name);
	}

	protected void setSize(int xSize, int ySize)
	{
		imageWidth = xSize;
		imageHeight = ySize;
	}

	protected abstract void renderBackground(MatrixStack matrix, float partialTime, int mouseX, int mouseY);

	@Override
	public void render(MatrixStack ms, int mouseX, int mouseY, float partialTime)
	{
		this.renderBackground(ms);
		super.render(ms, mouseX, mouseY, partialTime);
		this.renderTooltip(ms, mouseX, mouseY);
	}

	@Override
	protected void renderBg(MatrixStack matrix, float partialTime, int mouseX, int mouseY)
	{
		renderBackground(matrix, partialTime, mouseX, mouseY);
	}
}