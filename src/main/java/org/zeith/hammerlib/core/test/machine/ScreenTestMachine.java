package org.zeith.hammerlib.core.test.machine;

import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.player.Inventory;
import org.zeith.hammerlib.client.render.texture.GuiTexture;
import org.zeith.hammerlib.client.screen.IAdvancedGui;
import org.zeith.hammerlib.client.screen.ScreenWTFMojang;
import org.zeith.hammerlib.proxy.HLConstants;

@IAdvancedGui.ApplyToJEI
public class ScreenTestMachine
		extends ScreenWTFMojang<ContainerTestMachine>
		implements IAdvancedGui<ScreenTestMachine>
{
	public static final GuiTexture TEXTURE = GuiTexture.of(HLConstants.id("textures/gui/test_machine.png"));
	
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
	protected void renderBackground(GuiGraphics gfx, float partialTime, int mouseX, int mouseY)
	{
		var tex = TEXTURE.with(gfx);
		
		tex.blitSegment(leftPos, topPos, 0, 0, imageWidth, imageHeight, 256, 256);
		
		float maxProgress = 200F;
		int mp = tile.maxProgress.getInt();
		if(mp > 0) maxProgress = mp;
		
		float prog = tile.progress.getInt() / maxProgress;
		
		tex.blitSegment(leftPos + 80, topPos + 35,
				imageWidth, 14,
				22 * prog, 16
		);
	}
}