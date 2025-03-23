package org.zeith.hammerlib.mixins.client.gui;

import com.mojang.blaze3d.systems.RenderSystem;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.ObjectSelectionList;
import net.minecraftforge.client.gui.ModListScreen;
import net.minecraftforge.client.gui.widget.ModListWidget;
import net.minecraftforge.fml.javafmlmod.FMLModContainer;
import net.minecraftforge.forgespi.language.IModInfo;
import org.spongepowered.asm.mixin.*;
import org.spongepowered.asm.mixin.injection.*;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.zeith.hammerlib.core.adapter.FingerprintCheckAdapter;

import static org.zeith.hammerlib.core.adapter.FingerprintCheckAdapter.*;

@Mixin(ModListWidget.ModEntry.class)
public abstract class ModListEntryMixin
		extends ObjectSelectionList.Entry<ModListWidget.ModEntry>
{
	@Shadow
	@Final
	ModListWidget this$0;
	
	@Unique
	FMLModContainer hl$fmlModContainer;
	
	@Inject(
			method = "<init>",
			at = @At("TAIL")
	)
	private void HammerLib_postInit(ModListWidget this$0, IModInfo info, ModListScreen parent, CallbackInfo ci)
	{
		hl$fmlModContainer = FingerprintCheckAdapter.getFMLMod(info.getModId());
	}
	
	@Inject(
			method = "render",
			at = @At(value = "INVOKE", target = "Lnet/minecraftforge/fml/VersionChecker$Status;shouldDraw()Z")
	)
	private void HammerLib_renderSecure(GuiGraphics guiGraphics, int entryIdx, int top, int left, int entryWidth, int entryHeight, int mouseX, int mouseY, boolean isMouseOver, float partialTick, CallbackInfo ci)
	{
		var res = FingerprintCheckAdapter.getCheckResult(hl$fmlModContainer);
		if(!res.isIconVisible()) return;
		RenderSystem.setShaderColor(1, 1, 1, 1);
		guiGraphics.pose().pushPose();
		guiGraphics.blit(SECURE_JAR_CHECK_ICONS,
				this.this$0.getLeft() + this.this$0.getWidth() - 15,
				top + entryHeight / 4 + 9,
				0,
				res.getY(),
				8, 8,
				SECURE_JAR_CHECK_ICONS_SIZE[0], SECURE_JAR_CHECK_ICONS_SIZE[1]
		);
		guiGraphics.pose().popPose();
	}
}