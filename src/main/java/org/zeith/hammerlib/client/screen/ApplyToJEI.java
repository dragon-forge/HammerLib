package org.zeith.hammerlib.client.screen;

import net.minecraft.client.gui.screens.inventory.AbstractContainerScreen;

import java.lang.annotation.*;

/**
 * Annotating your {@link AbstractContainerScreen} class that implements {@link IAdvancedGui} will result in it getting wrapped for use with JEI.
 * This allows you to tell JEI that your GUI has extra areas that should not be obstructed by its panels, or add a {@link net.neoforged.neoforge.fluids.FluidStack} ingredient under fluid rendering stuff if you need to.
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
public @interface ApplyToJEI
{
}
