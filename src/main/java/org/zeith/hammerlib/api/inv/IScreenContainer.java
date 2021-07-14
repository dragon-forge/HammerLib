package org.zeith.hammerlib.api.inv;

import net.minecraft.client.gui.screen.Screen;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.util.text.ITextComponent;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

public interface IScreenContainer
{
	@OnlyIn(Dist.CLIENT)
	Screen openScreen(PlayerInventory inv, ITextComponent label);
}