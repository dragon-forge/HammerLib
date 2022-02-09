package org.zeith.hammerlib.tiles.tooltip.own;

import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.level.Level;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

import javax.annotation.Nullable;

public interface ITooltip
{
	/**
	 * Appends an element to the left end of the line.
	 *
	 * @param info - the object that's going to be rendered.
	 */
	void append(IRenderableInfo info);

	/**
	 * Finalizes the current line and begins to store new one.
	 */
	void newLine();

	@Nullable
	Level getWorld();

	@Nullable
	BlockPos getPos();

	@Nullable
	Entity getEntity();

	/**
	 * Gets the total width of the tooltip. It's calculated using
	 * max{width<sub>ln 1</sub>; width<sub>ln 2</sub>; ...; width<sub>ln
	 * n</sub>}
	 *
	 * @return the width of the tooltip
	 */
	int getWidth();

	/**
	 * Gets the total height of the tooltip. It's calculated using height<sub>ln
	 * 1</sub> + height<sub>ln 2</sub> + ... + height<sub>ln n</sub>
	 *
	 * @return the height of the tooltip
	 */
	int getHeight();

	/**
	 * Renders current tooltip that was stored.
	 *
	 * @param x           - the X offset of the tooltip
	 * @param y           - the Y offset of the tooltip
	 * @param partialTime - the Minecraft's partial tick time. Passed in GUIs, render
	 *                    events, TESRs.
	 */
	@OnlyIn(Dist.CLIENT)
	void render(PoseStack pose, float x, float y, float partialTime);

	/**
	 * Clears current tooltip and resets width, height, x and y of renderer
	 */
	void reset();
}