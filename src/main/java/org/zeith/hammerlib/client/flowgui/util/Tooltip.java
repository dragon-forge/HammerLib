package org.zeith.hammerlib.client.flowgui.util;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.screens.inventory.tooltip.ClientTooltipPositioner;
import net.minecraft.client.gui.screens.inventory.tooltip.DefaultTooltipPositioner;
import net.minecraft.network.chat.Component;
import net.minecraft.util.FormattedCharSequence;
import net.minecraft.world.inventory.tooltip.TooltipComponent;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.fluids.FluidStack;
import org.zeith.hammerlib.client.flowgui.Graphics;
import org.zeith.hammerlib.util.mcf.fluid.FluidHelper;

import javax.tools.Tool;
import java.util.List;
import java.util.Optional;
import java.util.function.Supplier;

public interface Tooltip
{
	void render(Graphics graphics, Font font, int x, int y);
	
	static Tooltip ofItem(ItemStack stack)
	{
		return new ItemTooltip(stack);
	}
	
	static Tooltip ofItem(Supplier<ItemStack> stack)
	{
		return new ItemTooltip(stack);
	}
	
	static Tooltip ofFluid(FluidStack stack, boolean showCapacity, int capacity)
	{
		return new FluidTooltip(stack, showCapacity, capacity);
	}
	
	static Tooltip ofFluid(Supplier<FluidStack> stack, boolean showCapacity, int capacity)
	{
		return new FluidTooltip(stack, showCapacity, capacity);
	}
	
	static Tooltip ofComponent(Component... text)
	{
		return new TextComponentTooltip(text);
	}
	
	static Tooltip ofComponent(List<Component> text)
	{
		return new TextComponentTooltip(text);
	}
	
	static Tooltip ofComponent(TooltipComponent component, Component... text)
	{
		return new TextComponentTooltip(Optional.ofNullable(component), text);
	}
	
	static Tooltip ofComponent(TooltipComponent component, List<Component> text)
	{
		return new TextComponentTooltip(text, Optional.ofNullable(component));
	}
	
	static Tooltip ofSequence(FormattedCharSequence... text)
	{
		return new CharSequenceTooltip(List.of(text));
	}
	
	static Tooltip ofSequence(List<FormattedCharSequence> text)
	{
		return new CharSequenceTooltip(text);
	}
	
	static Tooltip ofSequence(ClientTooltipPositioner positioner, FormattedCharSequence... text)
	{
		return new CharSequenceTooltip(List.of(text), positioner);
	}
	
	static Tooltip ofSequence(ClientTooltipPositioner positioner, List<FormattedCharSequence> text)
	{
		return new CharSequenceTooltip(text, positioner);
	}
	
	record TextComponentTooltip(List<Component> tooltip, Optional<TooltipComponent> component)
			implements Tooltip
	{
		public TextComponentTooltip(List<Component> tooltip)
		{
			this(tooltip, Optional.empty());
		}
		
		public TextComponentTooltip(Optional<TooltipComponent> component, Component... tooltip)
		{
			this(List.of(tooltip), component);
		}
		
		public TextComponentTooltip(Component... tooltip)
		{
			this(List.of(tooltip));
		}
		
		@Override
		public void render(Graphics g, Font font, int x, int y)
		{
			g.renderTooltip(font, tooltip, component, x, y);
		}
	}
	
	record CharSequenceTooltip(List<FormattedCharSequence> tooltip, ClientTooltipPositioner positioner)
			implements Tooltip
	{
		public CharSequenceTooltip(List<FormattedCharSequence> tooltip)
		{
			this(tooltip, DefaultTooltipPositioner.INSTANCE);
		}
		
		@Override
		public void render(Graphics g, Font font, int x, int y)
		{
			g.gfx().renderTooltip(font, tooltip, positioner, x, y);
		}
	}
	
	record ItemTooltip(Supplier<ItemStack> stack)
			implements Tooltip
	{
		public ItemTooltip(ItemStack stack)
		{
			this(stack::copy);
		}
		
		@Override
		public void render(Graphics g, Font font, int x, int y)
		{
			g.gfx().renderTooltip(font, stack.get(), x, y);
		}
	}
	
	record FluidTooltip(Supplier<FluidStack> stack, boolean showCapacity, int capacity)
			implements Tooltip
	{
		public FluidTooltip(FluidStack stack, boolean showCapacity, int capacity)
		{
			this(stack::copy, showCapacity, capacity);
		}
		
		@Override
		public void render(Graphics g, Font font, int x, int y)
		{
			boolean ait = Minecraft.getInstance().options.advancedItemTooltips;
			var tt = FluidHelper.getFluidTooltip(stack.get(), showCapacity, capacity, ait);
			var com = FluidHelper.getFluidTooltipComponent(stack.get(), showCapacity, capacity, ait);
			g.gfx().renderTooltip(font, tt, com, x, y);
		}
	}
}