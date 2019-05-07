package com.zeitheron.hammercore.client.gui;

import com.zeitheron.hammercore.client.gui.impl.container.SlotScaled;

import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.inventory.Container;
import net.minecraft.inventory.Slot;
import net.minecraft.item.ItemStack;
import net.minecraft.util.text.TextFormatting;

public class GuiWTFMojang<T extends Container> extends GuiContainer
{
	protected boolean fixMojang = true;
	
	public GuiWTFMojang(T inventorySlotsIn)
	{
		super(inventorySlotsIn);
	}
	
	public T getContainer()
	{
		return (T) inventorySlots;
	}
	
	@Override
	public void drawScreen(int mouseX, int mouseY, float partialTicks)
	{
		if(fixMojang)
		{
			drawDefaultBackground();
			GlStateManager.color(1F, 1F, 1F, 1F);
		}
		super.drawScreen(mouseX, mouseY, partialTicks);
		if(fixMojang)
			renderHoveredToolTip(mouseX, mouseY);
	}
	
	@Override
	public boolean isMouseOverSlot(Slot slotIn, int mouseX, int mouseY)
	{
		if(slotIn instanceof SlotScaled)
			return isPointInRegion(slotIn.xPos, slotIn.yPos, ((SlotScaled) slotIn).getWidth(), ((SlotScaled) slotIn).getHeight(), mouseX, mouseY);
		return super.isMouseOverSlot(slotIn, mouseX, mouseY);
	}
	
	@Override
	public void drawSlot(Slot slotIn)
	{
		if(slotIn instanceof SlotScaled)
			drawScaledSlot((SlotScaled) slotIn);
		else
			super.drawSlot(slotIn);
	}
	
	public void drawScaledSlot(SlotScaled slotIn)
	{
		int w = slotIn.getWidth();
		int h = slotIn.getHeight();
		
		int i = slotIn.xPos;
		int j = slotIn.yPos;
		ItemStack itemstack = slotIn.getStack();
		boolean flag = false;
		boolean flag1 = slotIn == this.clickedSlot && !this.draggedStack.isEmpty() && !this.isRightMouseClick;
		ItemStack itemstack1 = this.mc.player.inventory.getItemStack();
		String s = null;
		
		if(slotIn == this.clickedSlot && !this.draggedStack.isEmpty() && this.isRightMouseClick && !itemstack.isEmpty())
		{
			itemstack = itemstack.copy();
			itemstack.setCount(itemstack.getCount() / 2);
		} else if(this.dragSplitting && this.dragSplittingSlots.contains(slotIn) && !itemstack1.isEmpty())
		{
			if(this.dragSplittingSlots.size() == 1)
			{
				return;
			}
			
			if(Container.canAddItemToSlot(slotIn, itemstack1, true) && this.inventorySlots.canDragIntoSlot(slotIn))
			{
				itemstack = itemstack1.copy();
				flag = true;
				Container.computeStackSize(this.dragSplittingSlots, this.dragSplittingLimit, itemstack, slotIn.getStack().isEmpty() ? 0 : slotIn.getStack().getCount());
				int k = Math.min(itemstack.getMaxStackSize(), slotIn.getItemStackLimit(itemstack));
				
				if(itemstack.getCount() > k)
				{
					s = TextFormatting.YELLOW.toString() + k;
					itemstack.setCount(k);
				}
			} else
			{
				this.dragSplittingSlots.remove(slotIn);
				this.updateDragSplitting();
			}
		}
		
		this.zLevel = 100.0F;
		this.itemRender.zLevel = 100.0F;
		
		if(itemstack.isEmpty() && slotIn.isEnabled())
		{
			TextureAtlasSprite textureatlassprite = slotIn.getBackgroundSprite();
			
			if(textureatlassprite != null)
			{
				GlStateManager.disableLighting();
				this.mc.getTextureManager().bindTexture(slotIn.getBackgroundLocation());
				this.drawTexturedModalRect(i, j, textureatlassprite, w, h);
				GlStateManager.enableLighting();
				flag1 = true;
			}
		}
		
		if(!flag1)
		{
			if(flag)
				drawRect(i, j, i + w, j + h, -2130706433);
			GlStateManager.enableDepth();
			GlStateManager.pushMatrix();
			GlStateManager.scale(w / 16D, h / 16D, w / 16D);
			this.itemRender.renderItemAndEffectIntoGUI(this.mc.player, itemstack, i, j);
			this.itemRender.renderItemOverlayIntoGUI(this.fontRenderer, itemstack, i, j, s);
			GlStateManager.popMatrix();
		}
		
		this.itemRender.zLevel = 0.0F;
		this.zLevel = 0.0F;
	}
	
	@Override
	protected void drawGuiContainerBackgroundLayer(float partialTicks, int mouseX, int mouseY)
	{
	}
}