package org.zeith.hammerlib.core.test.machine;

import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.player.Inventory;
import org.zeith.hammerlib.abstractions.props.KeyMap;
import org.zeith.hammerlib.client.flowgui.GuiObject;
import org.zeith.hammerlib.client.flowgui.objects.GuiRootObject;
import org.zeith.hammerlib.client.flowgui.reader.FlowguiRegistry;
import org.zeith.hammerlib.client.flowgui.reader.XmlFlowgui;
import org.zeith.hammerlib.client.render.texture.GuiTexture;
import org.zeith.hammerlib.client.screen.IAdvancedGui;
import org.zeith.hammerlib.client.screen.ScreenWTFMojang;
import org.zeith.hammerlib.proxy.HLConstants;

@XmlFlowgui("test_machine")
@IAdvancedGui.ApplyToJEI
public class ScreenTestMachine
		extends ScreenWTFMojang<ContainerTestMachine>
		implements IAdvancedGui<ScreenTestMachine>
{
	public static final GuiTexture TEXTURE = GuiTexture.of(HLConstants.id("textures/gui/test_machine.png"));
	
	public TileTestMachine tile;
	
	public GuiRootObject root;
	
	public ScreenTestMachine(ContainerTestMachine container, Inventory inv, Component label)
	{
		super(container, inv, label);
		this.tile = container.tile;
		setSize(176, 166);
	}
	
	@Override
	protected void init()
	{
		super.init();
		root = FlowguiRegistry.readRoot(HLConstants.id("test_machine"), KeyMap.createHash(), width, height);
		if(root != null) addRenderableWidget(root);
	}
	
	@Override
	protected void containerTick()
	{
		if(root != null) root.sendUpdate();
		menu.containerTick();
		super.containerTick();
	}
	
	@Override
	protected void renderBackground(GuiGraphics gfx, float partialTime, int mouseX, int mouseY)
	{
		var tex = TEXTURE.with(gfx);
		
//		tex.blitSegment(leftPos, topPos, 0, 0, imageWidth, imageHeight, 256, 256);
		
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