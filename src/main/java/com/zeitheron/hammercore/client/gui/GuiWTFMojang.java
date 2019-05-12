package com.zeitheron.hammercore.client.gui;

import com.zeitheron.hammercore.client.gui.impl.container.SlotScaled;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.GuiLabel;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.OpenGlHelper;
import net.minecraft.client.renderer.RenderHelper;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.inventory.Container;
import net.minecraft.inventory.Slot;
import net.minecraft.item.ItemStack;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.text.TextFormatting;

public abstract class GuiWTFMojang<T extends Container> extends GuiContainer
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
		{
			
			int i = this.guiLeft;
			int j = this.guiTop;
			this.drawGuiContainerBackgroundLayer(partialTicks, mouseX, mouseY);
			GlStateManager.disableRescaleNormal();
			RenderHelper.disableStandardItemLighting();
			GlStateManager.disableLighting();
			GlStateManager.disableDepth();
			{
				for(int k = 0; k < this.buttonList.size(); ++k)
					((GuiButton) this.buttonList.get(k)).drawButton(this.mc, mouseX, mouseY, partialTicks);
				for(int l = 0; l < this.labelList.size(); ++l)
					((GuiLabel) this.labelList.get(l)).drawLabel(this.mc, mouseX, mouseY);
			}
			RenderHelper.enableGUIStandardItemLighting();
			GlStateManager.pushMatrix();
			GlStateManager.translate((float) i, (float) j, 0.0F);
			GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);
			GlStateManager.enableRescaleNormal();
			this.hoveredSlot = null;
			int k = 240;
			int l = 240;
			OpenGlHelper.setLightmapTextureCoords(OpenGlHelper.lightmapTexUnit, 240.0F, 240.0F);
			GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);
			
			for(int i1 = 0; i1 < this.inventorySlots.inventorySlots.size(); ++i1)
			{
				Slot slot = this.inventorySlots.inventorySlots.get(i1);
				
				if(slot.isEnabled())
				{
					this.drawSlot(slot);
				}
				
				if(this.isMouseOverSlot(slot, mouseX, mouseY) && slot.isEnabled())
				{
					this.hoveredSlot = slot;
					GlStateManager.disableLighting();
					GlStateManager.disableDepth();
					int j1 = slot.xPos;
					int k1 = slot.yPos;
					GlStateManager.colorMask(true, true, true, false);
					if(slot instanceof SlotScaled)
						this.drawGradientRect(j1, k1, j1 + ((SlotScaled) slot).getWidth(), k1 + ((SlotScaled) slot).getHeight(), -2130706433, -2130706433);
					else
						this.drawGradientRect(j1, k1, j1 + 16, k1 + 16, -2130706433, -2130706433);
					GlStateManager.colorMask(true, true, true, true);
					GlStateManager.enableLighting();
					GlStateManager.enableDepth();
				}
			}
			
			RenderHelper.disableStandardItemLighting();
			this.drawGuiContainerForegroundLayer(mouseX, mouseY);
			RenderHelper.enableGUIStandardItemLighting();
			net.minecraftforge.common.MinecraftForge.EVENT_BUS.post(new net.minecraftforge.client.event.GuiContainerEvent.DrawForeground(this, mouseX, mouseY));
			InventoryPlayer inventoryplayer = this.mc.player.inventory;
			ItemStack itemstack = this.draggedStack.isEmpty() ? inventoryplayer.getItemStack() : this.draggedStack;
			
			if(!itemstack.isEmpty())
			{
				int j2 = 8;
				int k2 = this.draggedStack.isEmpty() ? 8 : 16;
				String s = null;
				
				if(!this.draggedStack.isEmpty() && this.isRightMouseClick)
				{
					itemstack = itemstack.copy();
					itemstack.setCount(MathHelper.ceil((float) itemstack.getCount() / 2.0F));
				} else if(this.dragSplitting && this.dragSplittingSlots.size() > 1)
				{
					itemstack = itemstack.copy();
					itemstack.setCount(this.dragSplittingRemnant);
					
					if(itemstack.isEmpty())
					{
						s = "" + TextFormatting.YELLOW + "0";
					}
				}
				
				this.drawItemStack$(itemstack, mouseX - i - 8, mouseY - j - k2, s);
			}
			
			if(!this.returningStack.isEmpty())
			{
				float f = (float) (Minecraft.getSystemTime() - this.returningStackTime) / 100.0F;
				
				if(f >= 1.0F)
				{
					f = 1.0F;
					this.returningStack = ItemStack.EMPTY;
				}
				
				int l2 = this.returningStackDestSlot.xPos - this.touchUpX;
				int i3 = this.returningStackDestSlot.yPos - this.touchUpY;
				int l1 = this.touchUpX + (int) ((float) l2 * f);
				int i2 = this.touchUpY + (int) ((float) i3 * f);
				this.drawItemStack$(this.returningStack, l1, i2, (String) null);
			}
			
			GlStateManager.popMatrix();
			GlStateManager.enableLighting();
			GlStateManager.enableDepth();
			RenderHelper.enableStandardItemLighting();
			
		}
		if(fixMojang)
			renderHoveredToolTip(mouseX, mouseY);
	}
	
	protected void drawItemStack$(ItemStack stack, int x, int y, String altText)
	{
		GlStateManager.translate(0.0F, 0.0F, 32.0F);
		this.zLevel = 200.0F;
		this.itemRender.zLevel = 200.0F;
		net.minecraft.client.gui.FontRenderer font = stack.getItem().getFontRenderer(stack);
		if(font == null)
			font = fontRenderer;
		this.itemRender.renderItemAndEffectIntoGUI(stack, x, y);
		this.itemRender.renderItemOverlayIntoGUI(font, stack, x, y - (this.draggedStack.isEmpty() ? 0 : 8), altText);
		this.zLevel = 0.0F;
		this.itemRender.zLevel = 0.0F;
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
			GlStateManager.enableDepth();
			GlStateManager.pushMatrix();
			GlStateManager.translate(i, j, 0);
			GlStateManager.scale(w / 16D, h / 16D, w / 16D);
			if(flag)
				drawRect(0, 0, 16, 16, -2130706433);
			this.itemRender.renderItemAndEffectIntoGUI(this.mc.player, itemstack, 0, 0);
			this.itemRender.renderItemOverlayIntoGUI(this.fontRenderer, itemstack, 0, 0, s);
			GlStateManager.popMatrix();
		}
		
		this.itemRender.zLevel = 0.0F;
		this.zLevel = 0.0F;
	}
}