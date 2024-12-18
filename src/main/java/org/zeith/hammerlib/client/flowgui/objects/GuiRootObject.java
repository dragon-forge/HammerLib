package org.zeith.hammerlib.client.flowgui.objects;

import com.mojang.blaze3d.vertex.PoseStack;
import it.unimi.dsi.fastutil.floats.FloatConsumer;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.Renderable;
import net.minecraft.client.gui.components.events.GuiEventListener;
import net.minecraft.client.gui.narration.NarratableEntry;
import net.minecraft.client.gui.narration.NarrationElementOutput;
import net.minecraft.client.renderer.Rect2i;
import org.zeith.hammerlib.client.flowgui.*;
import org.zeith.hammerlib.client.flowgui.util.GuiObjectHelper;
import org.zeith.hammerlib.client.flowgui.util.ScrollData;
import org.zeith.hammerlib.util.math.Point;

public class GuiRootObject
		extends GuiObject
		implements GuiEventListener, Renderable, NarratableEntry
{
	protected boolean focused;
	protected Runnable tickHandler = () ->
	{
	};
	protected FloatConsumer preRenderHandler = f ->
	{
	};
	
	/**
	 * Enable this to see AABBs of all components when rendering as widget.
	 */
	public boolean debugBoundaries;
	public int debugBoundaryColor = 0xFFFF6666;
	
	public GuiRootObject()
	{
		super("$root");
	}
	
	public GuiRootObject onTick(Runnable task)
	{
		var prev = this.tickHandler;
		this.tickHandler = () ->
		{
			prev.run();
			task.run();
		};
		return this;
	}
	
	public GuiRootObject onPreRender(FloatConsumer task)
	{
		this.preRenderHandler = this.preRenderHandler.andThen(task);
		return this;
	}
	
	public final GuiRootObject add(GuiObject o)
	{
		addChild(o);
		return this;
	}
	
	@Override
	protected void update()
	{
		tickHandler.run();
	}
	
	@Override
	protected void render(Graphics gfx, MousePos pos)
	{
		preRenderHandler.accept(gfx.partialTime());
	}
	
	/**
	 * Create a default pose of this scene.
	 * If you have to change the default pose for mouse inputs and other functionality, override this method.
	 */
	public PoseStack myPose()
	{
		return new PoseStack();
	}
	
	@Override
	public boolean mouseClicked(double mouseX, double mouseY, int button)
	{
		return sendMouseClick(myPose(), new Point(mouseX, mouseY), button);
	}
	
	@Override
	public boolean mouseDragged(double mouseX, double mouseY, int button, double dragX, double dragY)
	{
		return sendMouseDrag(myPose(), new Point(mouseX, mouseY), button, new Point(dragX, dragY));
	}
	
	@Override
	public boolean mouseReleased(double mouseX, double mouseY, int button)
	{
		return sendMouseRelease(myPose(), new Point(mouseX, mouseY), button);
	}
	
	@Override
	public boolean mouseScrolled(double pMouseX, double pMouseY, double pScrollX, double pScrollY)
	{
		return sendMouseScroll(myPose(), new Point(pMouseX, pMouseY), new ScrollData(pScrollX, pScrollY));
	}
	
	@Override
	public void mouseMoved(double mouseX, double mouseY)
	{
		sendMouseMove(myPose(), new Point(mouseX, mouseY));
	}
	
	@Override
	public boolean keyPressed(int pKeyCode, int pScanCode, int pModifiers)
	{
		return sendKeyPress(pKeyCode, pScanCode, pModifiers);
	}
	
	@Override
	public boolean keyReleased(int pKeyCode, int pScanCode, int pModifiers)
	{
		return sendKeyRelease(pKeyCode, pScanCode, pModifiers);
	}
	
	@Override
	public boolean charTyped(char pCodePoint, int pModifiers)
	{
		return sendCharType(pCodePoint, pModifiers);
	}
	
	@Override
	public void setFocused(boolean pFocused)
	{
		focused = pFocused;
	}
	
	@Override
	public boolean isFocused()
	{
		return focused;
	}
	
	@Override
	public void render(GuiGraphics gfx, int mouseX, int mouseY, float partialTick)
	{
		Minecraft mc = Minecraft.getInstance();
		float x = (float) (mc.mouseHandler.xpos() * mc.getWindow().getGuiScaledWidth() / mc.getWindow().getScreenWidth());
		float y = (float) (mc.mouseHandler.ypos() * mc.getWindow().getGuiScaledHeight() / mc.getWindow().getScreenHeight());
		
		var g = Graphics.builder()
				.gfx(gfx)
				.game(Minecraft.getInstance())
				.partialTime(partialTick)
				.debugBounds(debugBoundaries)
				.build();
		
		renderObject(g, new Point(x, y));
		drawDebugOverlay(g);
	}
	
	public void drawDebugOverlay(Graphics g)
	{
		// Root object can display the boundaries after resetting its pose to zero.
		if(g.debugBounds())
		{
			var color = debugBoundaryColor;
			for(Rect2i rect : GuiObjectHelper.getAllAreas(this))
				g.renderOutline(rect.getX(), rect.getY(), rect.getWidth(), rect.getHeight(), color);
		}
	}
	
	@Override
	public void updateNarration(NarrationElementOutput output)
	{
	}
	
	@Override
	public NarrationPriority narrationPriority()
	{
		return NarrationPriority.NONE;
	}
	
	@Override
	public boolean isMouseOver(double pMouseX, double pMouseY)
	{
		return sendMouseClick(myPose(), new Point(pMouseX, pMouseY), SIMULATED_MOUSE_BUTTON);
	}
}