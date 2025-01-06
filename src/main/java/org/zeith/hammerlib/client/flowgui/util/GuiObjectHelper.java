package org.zeith.hammerlib.client.flowgui.util;

import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.renderer.Rect2i;
import org.joml.*;
import org.zeith.hammerlib.client.flowgui.GuiObject;
import org.zeith.hammerlib.client.flowgui.objects.GuiRootObject;
import org.zeith.hammerlib.client.screen.IAdvancedComponent;

import java.lang.Math;
import java.util.*;

public class GuiObjectHelper
{
	/**
	 * Use within your GUI that implements {@link org.zeith.hammerlib.client.screen.IAdvancedGui} to obtain an ingredient over the root object.
	 */
	public static Object getIngredientUnderMouse(GuiRootObject object, double mouseX, double mouseY)
	{
		return getIngredientUnderMouse(object, object.myPose(), mouseX, mouseY);
	}
	
	/**
	 * Use within your GUI that implements {@link org.zeith.hammerlib.client.screen.IAdvancedGui} to obtain a list of untransformed rectangles of all areas used by this root object.
	 */
	public static List<Rect2i> getAllAreas(GuiRootObject object)
	{
		return getAllAreas(object, object.myPose());
	}
	
	public static Object getIngredientUnderMouse(GuiObject object, PoseStack pose, double mouseX, double mouseY)
	{
		Vector3f tp = new Vector3f();
		return object.findInTree(pose, IAdvancedComponent.class, (com, pos) ->
				{
					GuiObject.untransform(pos).transformPosition((float) mouseX, (float) mouseY, 0, tp);
					var ing = com.getIngredientUnderMouse(tp.x, tp.y);
					if(ing != null) return Optional.of(ing);
					return Optional.empty();
				}
		).orElse(null);
	}
	
	public static List<Rect2i> getAllAreas(GuiObject object, PoseStack pose)
	{
		List<Rect2i> rectangles = new ArrayList<>();
		object.runForTree(pose, (com, pos) ->
				{
					var mat = new Matrix4f(pos.last().pose());
					
					// Add our default boundary
					com.getUnpositionedBounds()
							.stream()
							.map(r -> untransform(mat, r))
							.forEach(rectangles::add);
					
					if(com instanceof IAdvancedComponent adv)
						for(Rect2i r : adv.getExtraAreas())
						{
							r = untransform(mat, r);
							rectangles.add(r);
						}
				}
		);
		return rectangles;
	}
	
	public static Rect2i untransform(Matrix4f mat, Rect2i src)
	{
		Vector3f p1 = mat.transformPosition(src.getX(), src.getY(), 0, new Vector3f());
		Vector3f p2 = mat.transformPosition(src.getX() + src.getWidth() - 1, src.getY(), 0, new Vector3f());
		Vector3f p3 = mat.transformPosition(src.getX() + src.getWidth() - 1, src.getY() + src.getHeight() - 1, 0, new Vector3f());
		Vector3f p4 = mat.transformPosition(src.getX(), src.getY() + src.getHeight() - 1, 0, new Vector3f());
		
		int minX = (int) Math.floor(Math.min(p1.x, Math.min(p2.x, Math.min(p3.x, p4.x))));
		int minY = (int) Math.floor(Math.min(p1.y, Math.min(p2.y, Math.min(p3.y, p4.y))));
		
		int maxX = (int) Math.ceil(Math.max(p1.x, Math.max(p2.x, Math.max(p3.x, p4.x))));
		int maxY = (int) Math.ceil(Math.max(p1.y, Math.max(p2.y, Math.max(p3.y, p4.y))));
		
		return new Rect2i(minX, minY, maxX - minX + 1, maxY - minY + 1);
	}
}