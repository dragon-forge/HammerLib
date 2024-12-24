package org.zeith.hammerlib.client.flowgui.objects;

import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.world.inventory.Slot;
import org.joml.Matrix4f;
import org.joml.Vector3f;
import org.zeith.hammerlib.api.client.gui.IClientSlotPatch;
import org.zeith.hammerlib.client.flowgui.*;
import org.zeith.hammerlib.client.flowgui.util.ISlotLink;
import org.zeith.hammerlib.util.math.Point;

public class GuiSlotLinkObject
		extends GuiObject
		implements ISlotLink
{
	protected Matrix4f transform, untransform;
	protected Slot slot;
	
	public GuiSlotLinkObject(String name)
	{
		super(name);
		size(16, 16);
	}
	
	public GuiSlotLinkObject bindToSlot(Slot slot)
	{
		this.slot = slot;
		
		var patch = (IClientSlotPatch) slot;
		patch.setLinkedHover(this); // Actually bind
		patch.setPos(Point.ZERO);
		
		return this;
	}
	
	@Override
	protected void render(Graphics gfx, MousePos pos)
	{
		transform = new Matrix4f(gfx.gfx().pose().last().pose());
		untransform = transform.invert(new Matrix4f());
	}
	
	@Override
	public boolean isValidFor(Slot slot)
	{
		return slot == this.slot;
	}
	
	@Override
	public void patchSlotTransforms(Slot slot, PoseStack pose)
	{
		if(!enabled || !visible || transform == null)
		{
			pose.scale(Float.NaN, Float.NaN, Float.NaN);
			return;
		}
		
		pose.mulPoseMatrix(transform);
		
		// Translate back a bit to avoid tooltip being rendered below the item & count
		pose.translate(0, 0, -75 * width / 16);
		
		// since vanilla slots are 16x the size of 1px, we should correct the scale
		pose.scale(width / 16F, height / 16F, width / 16F);
	}
	
	@Override
	public boolean isMouseOver(Slot slot, double mouseX, double mouseY)
	{
		if(!enabled || !visible || transform == null) return false;
		var pos = untransform.transformPosition((float) mouseX, (float) mouseY, 0, new Vector3f());
		return pos.x >= 0 && pos.y >= 0 && pos.x < width && pos.y < height;
	}
}