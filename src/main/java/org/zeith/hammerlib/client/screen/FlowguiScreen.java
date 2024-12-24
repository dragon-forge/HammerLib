package org.zeith.hammerlib.client.screen;

import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.renderer.Rect2i;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.inventory.AbstractContainerMenu;
import org.zeith.hammerlib.abstractions.props.KeyMap;
import org.zeith.hammerlib.api.inv.ITickableContainer;
import org.zeith.hammerlib.client.flowgui.data.FlowQuery;
import org.zeith.hammerlib.client.flowgui.objects.GuiRootObject;
import org.zeith.hammerlib.client.flowgui.reader.FlowguiRegistry;
import org.zeith.hammerlib.client.flowgui.util.GuiObjectHelper;

import java.util.List;
import java.util.Objects;

public class FlowguiScreen<T extends AbstractContainerMenu>
		extends ScreenWTFMojang<T>
		implements IAdvancedGui
{
	protected ResourceLocation prevFlowId;
	protected GuiRootObject root;
	
	public FlowguiScreen(T container, Inventory playerInv, Component name)
	{
		super(container, playerInv, name);
	}
	
	@Override
	public List<Rect2i> getExtraAreas()
	{
		return GuiObjectHelper.getAllAreas(root);
	}
	
	protected void populateQuery(FlowQuery q)
	{
	}
	
	protected void populateKeymap(KeyMap context)
	{
	}
	
	protected final void populateMandatoryKeymap(KeyMap keymap, ResourceLocation id)
	{
		var q = createQuery();
		populateQuery(q);
		keymap
				.with(FlowguiRegistry.QUERY, q)
				.with(FlowguiRegistry.ROOT_ID, id)
				.with(FlowguiRegistry.PREVIOUS_ROOT, root);
	}
	
	protected FlowQuery createQuery()
	{
		return new FlowQuery(this);
	}
	
	protected ResourceLocation getFlowId()
	{
		return FlowguiRegistry.getId(getClass());
	}
	
	protected GuiRootObject createRoot(ResourceLocation id)
	{
		var keymap = KeyMap.createHash();
		populateMandatoryKeymap(keymap, id);
		populateKeymap(keymap);
		return FlowguiRegistry.readRoot(keymap);
	}
	
	@Override
	protected void init()
	{
		prevFlowId = getFlowId();
		root = addRenderableWidget(createRoot(prevFlowId));
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
		
		root.sendUpdate();
		
		var newFlowId = getFlowId();
		if(!Objects.equals(newFlowId, prevFlowId))
			init(minecraft, width, height);
	}
	
	@Override
	protected void renderBackground(GuiGraphics pose, float partialTime, int mouseX, int mouseY)
	{
	}
}