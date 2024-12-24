package org.zeith.hammerlib.core.test.machine;

import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.player.Inventory;
import org.zeith.hammerlib.client.flowgui.objects.GuiRootObject;
import org.zeith.hammerlib.client.flowgui.reader.XmlFlowgui;
import org.zeith.hammerlib.client.screen.*;

@XmlFlowgui("test_machine")
public class ScreenTestMachine
		extends FlowguiScreen<ContainerTestMachine>
{
	public TileTestMachine tile;
	
	public GuiRootObject root;
	
	public ScreenTestMachine(ContainerTestMachine container, Inventory inv, Component label)
	{
		super(container, inv, Component.empty());
		this.tile = container.tile;
	}
	
	public Component getName()
	{
		return BlockTestMachine.TEST_MACHINE.getName();
	}
	
	public void respond(String s)
	{
	}
	
	public float getProgress(float partialTime)
	{
		float maxProgress = 200F;
		int mp = tile.maxProgress.getInt();
		if(mp > 0) maxProgress = mp;
		
		if(tile.activeRecipeId.get() == null) partialTime *= -1;
		return Math.clamp((tile.uiProgress.getInt() + partialTime) / maxProgress, 0F, 1F);
	}
}