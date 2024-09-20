package org.zeith.hammerlib.client.flowgui.test;

import lombok.extern.slf4j.Slf4j;
import net.minecraft.network.chat.Component;
import net.minecraft.util.Mth;
import org.zeith.hammerlib.client.flowgui.GuiObject;
import org.zeith.hammerlib.client.flowgui.objects.*;
import org.zeith.hammerlib.proxy.HLConstants;

@Slf4j
public class TestFlowgui
{
	public static GuiRootObject assemble(int width, int height)
	{
		GuiObject slot, c1, c2, c3, c4;
		
		var g = GuiObject.root()
				.add(GuiObject.create("core")
						.image(HLConstants.id("textures/gui/test_machine.png"), 0, 0, 176, 166)
						.centered(width, height)
						.addChild(GuiObject.create("btn").button().message(Component.literal("Test Button"))
								.callback(b -> log.info("Clicked test button."))
								.build()
								.setEnabled(false)
								.size(100, 20)
								.pos(8, 18 + 50).rotation(-90).scale(0.5F)
								.addChild(GuiObject.create("label")
										.text(Component.literal("Original Value"))
										.offset(0, 20) // add to current position
								)
						)
						.addChild(slot = GuiObject.create("input")
								.image(HLConstants.id("textures/block/test_machine_front.png"), 0, 0, 16, 16, 16, 16)
								.pos(56, 17)
								.pivotAtCenter()
								.rotation(45)
								.addChild(c1 = GuiObject.create("1")
										.image(HLConstants.id("textures/block/test_machine_front.png"), 0, 0, 8, 8, 8, 8)
										.pivotAtCenter()
								)
								.addChild(c2 = GuiObject.create("2")
										.image(HLConstants.id("textures/block/test_machine_front.png"), 0, 0, 8, 8, 8, 8)
										.pivotAtCenter()
								)
								.addChild(c3 = GuiObject.create("3")
										.image(HLConstants.id("textures/block/test_machine_front.png"), 0, 0, 8, 8, 8, 8)
										.pivotAtCenter()
								)
								.addChild(c4 = GuiObject.create("4")
										.image(HLConstants.id("textures/block/test_machine_front.png"), 0, 0, 8, 8, 8, 8)
										.pivotAtCenter()
								)
						)
				)
				.onPreRender(partialTicks ->
				{
					long sys = System.currentTimeMillis();
					float dist = Mth.sin(sys % 36000L / 100F);
					float r = sys % 3600L / 10F;
					slot.rotation(r); c1.rotation(r + 45); c2.rotation(r + 45); c3.rotation(r + 45); c4.rotation(r + 45);
					c1.pos(16 + 8 * dist, 16 + 8 * dist); c2.pos(-8 - 8 * dist, 16 + 8 * dist); c3.pos(16 + 8 * dist, -8 - 8 * dist); c4.pos(-8 - 8 * dist, -8 - 8 * dist);
				});
		
		GuiButtonObject cbtn = g.findByPath("core/btn", GuiButtonObject.class);
		if(cbtn != null) cbtn.message = Component.literal("Changed Text");
		
		GuiTextObject lbl = g.findByPath("core/btn/label", GuiTextObject.class);
		if(lbl != null) lbl.setText(Component.literal("I'm under button!"));
		
		// Enable this to see AABBs of all components
		g.debugBoundaries = true;
		g.debugBoundaryColor = 0xFF669999;
		
		return g;
	}
}