package org.zeith.hammerlib.api.inv;

import net.minecraft.client.gui.screens.Screen;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.player.Inventory;
import net.neoforged.api.distmarker.*;

public interface IScreenContainer
{
	@OnlyIn(Dist.CLIENT)
	Screen openScreen(Inventory inv, Component label);
}