package org.zeith.hammerlib.client.screen;

import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.inventory.AbstractContainerMenu;
import org.zeith.hammerlib.abstractions.props.KeyMap;
import org.zeith.hammerlib.api.inv.ITickableContainer;
import org.zeith.hammerlib.client.flowgui.data.FlowQuery;
import org.zeith.hammerlib.client.flowgui.objects.GuiRootObject;
import org.zeith.hammerlib.client.flowgui.reader.FlowguiRegistry;

public class FlowguiScreen<T extends AbstractContainerMenu>
		extends ScreenWTFMojang<T>
{
	protected GuiRootObject root;
	
	public FlowguiScreen(T container, Inventory playerInv, Component name)
	{
		super(container, playerInv, name);
	}
	
	protected void populateQuery(FlowQuery q)
	{
	}
	
	protected void populateKeymap(KeyMap context)
	{
	}
	
	protected FlowQuery createQuery()
	{
		return new FlowQuery(this);
	}
	
	protected ResourceLocation getFlowId()
	{
		return FlowguiRegistry.getId(getClass());
	}
	
	protected GuiRootObject createRoot()
	{
		var q = createQuery();
		populateQuery(q);
		var keymap = KeyMap.createHash();
		populateKeymap(keymap);
		return FlowguiRegistry.readRoot(keymap.with(FlowguiRegistry.QUERY, q).with(FlowguiRegistry.ROOT_ID, getFlowId()));
	}
	
	@Override
	protected void init()
	{
		root = addRenderableWidget(createRoot());
		imageWidth = (int) root.getScaledWidth();
		imageHeight = (int) root.getScaledHeight();
		super.init();
	}
	
	@Override
	protected void containerTick()
	{
		super.containerTick();
		if(getMenu() instanceof ITickableContainer tc)
			tc.containerTick();
	}
	
	@Override
	protected void renderBackground(GuiGraphics pose, float partialTime, int mouseX, int mouseY)
	{
	}
}