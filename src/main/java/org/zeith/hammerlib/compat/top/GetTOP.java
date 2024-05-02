package org.zeith.hammerlib.compat.top;

import it.unimi.dsi.fastutil.objects.ObjectArrayList;
import lombok.Getter;
import mcjty.theoneprobe.api.*;
import mcjty.theoneprobe.apiimpl.ProbeInfo;
import mcjty.theoneprobe.apiimpl.elements.ElementItemStack;
import mcjty.theoneprobe.apiimpl.elements.ElementText;
import mcjty.theoneprobe.apiimpl.styles.ItemStyle;
import mcjty.theoneprobe.apiimpl.styles.LayoutStyle;
import mcjty.theoneprobe.rendering.RenderHelper;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.Nameable;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.api.distmarker.OnlyIn;
import net.neoforged.fml.ModList;
import org.zeith.hammerlib.HammerLib;
import org.zeith.hammerlib.proxy.HLConstants;
import org.zeith.hammerlib.tiles.tooltip.EnumTooltipEngine;
import org.zeith.hammerlib.tiles.tooltip.ITooltipTile;
import org.zeith.hammerlib.util.java.Cast;

import java.util.List;
import java.util.function.Function;

public class GetTOP
		implements IProbeInfoProvider, IBlockDisplayOverride, Function<ITheOneProbe, Void>
{
	protected final ResourceLocation id = HLConstants.id("root");
	
	@Override
	public Void apply(ITheOneProbe top)
	{
		HammerLib.LOG.info("TheOneProbe API hooked!");
		top.registerProvider(this);
		top.registerBlockDisplayOverride(this);
		top.registerElementFactory(create(Panel.ELEMENT_ID, Panel::new));
		return null;
	}
	
	@Override
	public ResourceLocation getID()
	{
		return id;
	}
	
	@Override
	public void addProbeInfo(ProbeMode mode, IProbeInfo info, Player player, Level world, BlockState state, IProbeHitData hitData)
	{
		ITooltipTile tile = Cast.cast(world.getBlockEntity(hitData.getPos()), ITooltipTile.class);
		if(tile != null && tile.isEngineSupported(EnumTooltipEngine.THEONEPROBE))
			tile.addTooltip(new TOPTooltipConsumer(info, hitData), player);
	}
	
	@Override
	public boolean overrideStandardInfo(ProbeMode mode, IProbeInfo info, Player player, Level world, BlockState state, IProbeHitData hitData)
	{
		ITooltipTile tile = Cast.cast(world.getBlockEntity(hitData.getPos()), ITooltipTile.class);
		if(tile != null && tile.hasItemIconOverride())
		{
			Panel header = new Panel(ILayoutStyleBuilder.spacing(2), Panel.Type.HORIZONTAL);
			header.element(new ElementItemStack(tile.getItemIconOverride(), new ItemStyle().height(20).width(20)));
			
			Panel right = new Panel(ILayoutStyleBuilder.create().spacing(3), Panel.Type.VERTICAL);
			Component name = tile instanceof Nameable n ? n.getName() : state.getBlock().getName();
			right.element(new ElementText(name).setLegacy());
			right.element(new ElementText(CompoundText.create().style(TextStyleClass.MODNAME)
					.text(ModList.get().getModContainerById(BuiltInRegistries.BLOCK.getKey(state.getBlock()).getNamespace()).get().getModInfo().getDisplayName())
					.get()).setLegacy());
			
			header.element(right);
			
			info.element(header);
			return true;
		}
		return false;
	}
	
	private static IElementFactory create(final ResourceLocation id, final Function<RegistryFriendlyByteBuf, IElement> factory)
	{
		return new IElementFactory()
		{
			@Override
			public IElement createElement(RegistryFriendlyByteBuf buf)
			{
				return factory.apply(buf);
			}
			
			@Override
			public ResourceLocation getId()
			{
				return id;
			}
		};
	}
	
	//<editor-fold desc="Taken from IC2">
	public interface ILayoutStyleBuilder
	{
		static ILayoutStyle create()
		{
			return new LayoutStyle();
		}
		
		static ILayoutStyle border(Color color)
		{
			return (new LayoutStyle()).borderColor(color);
		}
		
		static ILayoutStyle border(Integer color)
		{
			return (new LayoutStyle()).borderColor(color);
		}
		
		static ILayoutStyle spacing(int spacing)
		{
			return (new LayoutStyle()).spacing(spacing);
		}
		
		static ILayoutStyle aligned(ElementAlignment align)
		{
			return (new LayoutStyle()).alignment(align);
		}
		
		static ILayoutStyle padding(int padding)
		{
			return (new LayoutStyle()).padding(padding);
		}
		
		static ILayoutStyle padding(int xPadding, int yPadding)
		{
			return (new LayoutStyle()).hPadding(xPadding).vPadding(yPadding);
		}
		
		static ILayoutStyle padding(int top, int bottom, int left, int right)
		{
			return (new LayoutStyle()).topPadding(top).bottomPadding(bottom).leftPadding(left).rightPadding(right);
		}
	}
	
	public static class Panel
			implements IElement
	{
		public static final ResourceLocation ELEMENT_ID = HammerLib.id("panel");
		
		@Getter
		protected List<IElement> children;
		protected ElementAlignment horizontal;
		protected ElementAlignment vertical;
		@Getter
		protected ILayoutStyle layout;
		protected Integer borderColor;
		protected int spacing;
		
		public Panel(Panel.Type type)
		{
			this(ILayoutStyleBuilder.create(), type);
		}
		
		public Panel(ILayoutStyle layout, Panel.Type type)
		{
			this(layout, type == Panel.Type.VERTICAL);
		}
		
		public Panel(boolean vertical)
		{
			this(ILayoutStyleBuilder.create(), vertical);
		}
		
		public Panel(ILayoutStyle layout, boolean vertical)
		{
			this.children = new ObjectArrayList<>();
			
			if(vertical) this.vertical = layout.getAlignment();
			else this.horizontal = layout.getAlignment();
			
			this.layout = layout;
			this.borderColor = layout.getBorderColor();
			this.spacing = layout.getSpacing();
		}
		
		public Panel(RegistryFriendlyByteBuf buffer)
		{
			this.children = new ObjectArrayList<>();
			
			if(buffer.readBoolean()) this.vertical = buffer.readEnum(ElementAlignment.class);
			else this.horizontal = buffer.readEnum(ElementAlignment.class);
			
			if(buffer.readBoolean())
				this.borderColor = buffer.readInt();
			
			this.spacing = buffer.readInt();
			this.layout = ILayoutStyleBuilder.create().topPadding(buffer.readInt()).bottomPadding(buffer.readInt()).leftPadding(buffer.readInt()).rightPadding(buffer.readInt());
			this.children.addAll(ProbeInfo.createElements(buffer));
		}
		
		@Override
		public void toBytes(RegistryFriendlyByteBuf buffer)
		{
			buffer.writeBoolean(this.vertical != null);
			
			buffer.writeEnum(this.vertical != null ? this.vertical : this.horizontal);
			
			buffer.writeBoolean(this.borderColor != null);
			if(this.borderColor != null) buffer.writeInt(this.borderColor);
			
			buffer.writeInt(this.spacing).writeInt(this.layout.getTopPadding()).writeInt(this.layout.getBottomPadding()).writeInt(this.layout.getLeftPadding()).writeInt(this.layout.getRightPadding());
			ProbeInfo.writeElements(this.children, buffer);
		}
		
		@Override
		@OnlyIn(Dist.CLIENT)
		public void render(GuiGraphics matrix, int x, int y)
		{
			int totWidth;
			
			if(this.borderColor != null)
			{
				totWidth = this.getWidth();
				int h = this.getHeight();
				RenderHelper.drawHorizontalLine(matrix, x, y, x + totWidth - 1, this.borderColor);
				RenderHelper.drawHorizontalLine(matrix, x, y + h - 1, x + totWidth - 1, this.borderColor);
				RenderHelper.drawVerticalLine(matrix, x, y, y + h - 1, this.borderColor);
				RenderHelper.drawVerticalLine(matrix, x + totWidth - 1, y, y + h, this.borderColor);
				x += 3;
				y += 3;
			}
			
			if(this.horizontal != null)
			{
				x += this.layout.getLeftPadding();
				totWidth = this.getHeight() - this.getYPadding();
				
				for(IElement child : this.children)
				{
					int w = child.getHeight();
					int cx = y;
					
					switch(this.horizontal)
					{
						default ->
						{
						}
						case ALIGN_CENTER -> cx = y + (totWidth - w) / 2;
						case ALIGN_BOTTOMRIGHT -> cx = y + totWidth - w;
					}
					
					child.render(matrix, x, cx + this.layout.getTopPadding());
					x += child.getWidth() + this.spacing;
				}
				
				return;
			}
			
			if(this.vertical != null)
			{
				y += this.layout.getTopPadding();
				totWidth = this.getWidth() - this.getXPadding();
				
				for(IElement child : this.children)
				{
					int w = child.getWidth();
					int cx = x;
					
					switch(this.vertical)
					{
						default ->
						{
						}
						case ALIGN_CENTER -> cx = x + (totWidth - w) / 2;
						case ALIGN_BOTTOMRIGHT -> cx = x + totWidth - w;
					}
					
					child.render(matrix, cx + this.layout.getLeftPadding(), y);
					y += child.getHeight() + this.spacing;
				}
			}
		}
		
		@Override
		public int getHeight()
		{
			int h = 0;
			
			if(this.vertical != null)
			{
				for(IElement child : this.children) h += child.getHeight();
				return h + this.spacing * (this.children.size() - 1) + this.getBorderSpacing() + this.getYPadding();
			}
			
			for(IElement child : this.children) h = Math.max(child.getHeight(), h);
			return h + this.getBorderSpacing() + this.getYPadding();
		}
		
		@Override
		public int getWidth()
		{
			int w = 0;
			
			if(this.horizontal != null)
			{
				for(IElement child : this.children) w += child.getWidth();
				return w + this.spacing * (this.children.size() - 1) + this.getBorderSpacing() + this.getXPadding();
			}
			
			for(IElement child : this.children) w = Math.max(child.getWidth(), w);
			return w + this.getBorderSpacing() + this.getXPadding();
		}
		
		protected int getYPadding()
		{
			return this.layout.getBottomPadding() + this.layout.getTopPadding();
		}
		
		protected int getXPadding()
		{
			return this.layout.getLeftPadding() + this.layout.getRightPadding();
		}
		
		protected int getBorderSpacing()
		{
			return this.borderColor == null ? 0 : 6;
		}
		
		@Override
		public ResourceLocation getID()
		{
			return ELEMENT_ID;
		}
		
		public List<IElement> getElements()
		{
			return this.getChildren();
		}
		
		public Panel element(IElement element)
		{
			this.children.add(element);
			return this;
		}
		
		public enum Type
		{
			VERTICAL,
			HORIZONTAL;
		}
	}
	//</editor-fold>
}