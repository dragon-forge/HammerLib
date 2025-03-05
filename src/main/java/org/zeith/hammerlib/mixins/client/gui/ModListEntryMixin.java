package org.zeith.hammerlib.mixins.client.gui;

import com.mojang.blaze3d.systems.RenderSystem;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.ObjectSelectionList;
import net.minecraft.client.renderer.RenderType;
import net.neoforged.fml.ModContainer;
import net.neoforged.neoforge.client.gui.widget.ModListWidget;
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
	
	@Shadow
	@Final
	private ModContainer container;
	
	@Inject(
			method = "render",
			at = @At(value = "INVOKE", target = "Lnet/neoforged/fml/VersionChecker$Status;shouldDraw()Z")
	)
	private void HammerLib_renderSecure(GuiGraphics guiGraphics, int entryIdx, int top, int left, int entryWidth, int entryHeight, int mouseX, int mouseY, boolean isMouseOver, float partialTick, CallbackInfo ci)
	{
		var res = FingerprintCheckAdapter.getCheckResult(container);
		if(!res.isIconVisible()) return;
		RenderSystem.setShaderColor(1, 1, 1, 1);
		guiGraphics.pose().pushPose();
		guiGraphics.blit(RenderType::guiTextured, SECURE_JAR_CHECK_ICONS,
				this.this$0.getX() + this.this$0.getWidth() - 12,
				top + entryHeight / 4 + 9,
				0,
				res.getY(),
				8, 8,
				SECURE_JAR_CHECK_ICONS_SIZE[0], SECURE_JAR_CHECK_ICONS_SIZE[1]
		);
		guiGraphics.pose().popPose();
	}
}