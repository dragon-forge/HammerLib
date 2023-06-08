package org.zeith.hammerlib.tiles.tooltip.own;

import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import org.zeith.hammerlib.tiles.tooltip.ProgressBar;
import org.zeith.hammerlib.tiles.tooltip.own.inf.*;

import javax.annotation.Nullable;

public interface ITooltip
{
	/**
	 * Appends an element to the left end of the line.
	 *
	 * @param info
	 * 		- the object that's going to be rendered.
	 */
	ITooltip add(IRenderableInfo info);
	
	default ITooltip addText(Component component)
	{
		return add(new TooltipInfoText(component));
	}
	
	default ITooltip addSpacing(float width, float height)
	{
		return add(new TooltipInfoSpacing(width, height));
	}
	
	default ITooltip addTexture(ResourceLocation texture, float width, float height, int rgba)
	{
		return add(new TooltipInfoTexture(texture, width, height, rgba));
	}
	
	default ITooltip addTexture(ResourceLocation texture, float width, float height)
	{
		return add(new TooltipInfoTexture(texture, width, height, 0xFFFFFFFF));
	}
	
	default ITooltip addStack(ItemStack stack, float width, float height)
	{
		return add(new TooltipInfoStack(stack, width, height));
	}
	
	default ITooltip addProgressBar(ProgressBar bar)
	{
		return add(new TooltipInfoProgressBar(bar));
	}
	
	/**
	 * Finalizes the current line and begins to store new one.
	 */
	ITooltip newLine();
	
	@Nullable
	Level getWorld();
	
	@Nullable
	BlockPos getPos();
	
	@Nullable
	Entity getEntity();
	
	Player getPlayer();
	
	/**
	 * Gets the total width of the tooltip. It's calculated using
	 * max{width<sub>ln 1</sub>; width<sub>ln 2</sub>; ...; width<sub>ln
	 * n</sub>}
	 *
	 * @return the width of the tooltip
	 */
	float getWidth();
	
	/**
	 * Gets the total height of the tooltip. It's calculated using height<sub>ln
	 * 1</sub> + height<sub>ln 2</sub> + ... + height<sub>ln n</sub>
	 *
	 * @return the height of the tooltip
	 */
	float getHeight();
	
	/**
	 * Renders current tooltip that was stored.
	 *
	 * @param x
	 * 		- the X offset of the tooltip
	 * @param y
	 * 		- the Y offset of the tooltip
	 * @param partialTime
	 * 		- the Minecraft's partial tick time. Passed in GUIs, render
	 * 		events, TESRs.
	 */
	@OnlyIn(Dist.CLIENT)
	void render(GuiGraphics pose, float x, float y, float partialTime);
	
	/**
	 * Clears current tooltip and resets width, height, x and y of renderer
	 */
	void reset();
}