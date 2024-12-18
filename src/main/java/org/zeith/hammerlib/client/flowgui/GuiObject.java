package org.zeith.hammerlib.client.flowgui;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.math.Axis;
import lombok.extern.slf4j.Slf4j;
import net.minecraft.world.phys.Vec3;
import org.jetbrains.annotations.Nullable;
import org.joml.Matrix4f;
import org.joml.Vector3f;
import org.zeith.hammerlib.client.flowgui.objects.GuiRootObject;
import org.zeith.hammerlib.client.flowgui.util.ScrollData;
import org.zeith.hammerlib.util.math.Point;
import org.zeith.hammerlib.util.java.Cast;
import org.zeith.hammerlib.util.java.DirectStorage;

import java.util.*;
import java.util.function.*;

@Slf4j
public class GuiObject
{
	public static final int SIMULATED_MOUSE_BUTTON = -25902376;
	
	private static final Vec3 ONE = new Vec3(1, 1, 1);
	//<editor-fold desc="Object relationship">
	private GuiObject parent;
	private final List<GuiObject> children = new ArrayList<>();
	private final Map<String, GuiObject> byName = new HashMap<>();
	private final String originalName;
	private String name;
	//</editor-fold>
	
	private final String simpleName = getClass().getSimpleName();
	
	protected boolean enabled = true, visible = true;
	protected float zOffset;
	protected Point pos = Point.ZERO, pivot = Point.ZERO;
	protected Vec3 scale = ONE;
	protected float width, height;
	protected float rotation;
	
	public final DirectStorage<Point> elementPosition = DirectStorage.create(p -> pos(p.x(), p.y()), () -> pos);
	public final DirectStorage<Point> elementPivot = DirectStorage.create(p -> pivot(p.x(), p.y()), () -> pivot);
	public final DirectStorage<Vec3> elementScale = DirectStorage.create(p -> scale = p, () -> scale);
	public final DirectStorage<Float> elementRotation = DirectStorage.create(this::rotation, () -> rotation);
	public final DirectStorage<Float> elementWidth = DirectStorage.create(p -> width = p, () -> width);
	public final DirectStorage<Float> elementHeight = DirectStorage.create(p -> height = p, () -> height);
	
	public GuiObject(String name)
	{
		if(name.contains("/")) throw new IllegalArgumentException("GuiObject's path can not have '/' character.");
		this.originalName = this.name = name;
	}
	
	public @Nullable <T extends GuiObject> T findByName(String path, Class<T> expectType)
	{
		return Cast.cast(findByName(path), expectType);
	}
	
	public @Nullable <T extends GuiObject> T findByNameIgnoreCase(String path, Class<T> expectType)
	{
		return Cast.cast(findByNameIgnoreCase(path), expectType);
	}
	
	public @Nullable <T extends GuiObject> T findByPath(String path, Class<T> expectType)
	{
		return Cast.cast(findByPath(path), expectType);
	}
	
	public @Nullable GuiObject findByName(String path)
	{
		List<GuiObject> allChildren = new ArrayList<>();
		allChildren.add(this);
		for(int i = 0; i < allChildren.size(); i++)
		{
			GuiObject c = allChildren.get(i);
			if(c.getName().equals(path)) return c;
			for(GuiObject c2 : c.getChildren()) allChildren.add(c2);
		}
		return null;
	}
	
	public @Nullable GuiObject findByNameIgnoreCase(String path)
	{
		List<GuiObject> allChildren = new ArrayList<>();
		allChildren.add(this);
		for(int i = 0; i < allChildren.size(); i++)
		{
			GuiObject c = allChildren.get(i);
			if(c.getName().equalsIgnoreCase(path)) return c;
			for(GuiObject c2 : c.getChildren()) allChildren.add(c2);
		}
		return null;
	}
	
	public @Nullable GuiObject findByPath(String path)
	{
		if(path.isBlank()) return this;
		
		String name;
		int nextSlash = path.indexOf('/');
		if(nextSlash >= 0) name = path.substring(0, nextSlash);
		else return byName.get(path);
		
		GuiObject o = byName.get(name);
		return o != null ? o.findByPath(path.substring(nextSlash + 1)) : null;
	}
	
	public final Iterable<GuiObject> getChildren()
	{
		return children;
	}
	
	public GuiObject getChild(String name)
	{
		return byName.get(name);
	}
	
	/**
	 * Appends a child to [this] object, positioning it relative to this object.
	 */
	public final GuiObject addChild(GuiObject child)
	{
		if(child == this)
		{
			log.warn("Attempted to add {} into itself.", this);
			return this;
		}
		
		if(anyMatchesWithinTree(this, o -> o == child))
		{
			log.warn("Attempted to add {} into {} but it is already in the vertical hierarchy.", child, this);
			return this;
		}
		
		// Remove from previous parent as this object will be our new parent (consider it as refactor)
		child.remove();
		
		child.parent = this;
		children.add(child);
		
		if(byName.containsKey(child.getName()))
		{
			String pv = child.toString();
			child.setName(child.originalName);
			log.warn("Attempted to add {} into {} but the name is already bound. Renaming to {}.", pv, this, child.getName());
			return this;
		}
		
		byName.put(child.getName(), child);
		
		return this;
	}
	
	public final GuiObject removeChild(String name)
	{
		GuiObject go = byName.remove(name);
		if(go == null) return null;
		children.remove(go);
		go.parent = null;
		go.name = go.originalName;
		return go;
	}
	
	public final void remove()
	{
		if(parent != null)
			parent.removeChild(getName());
	}
	
	public final String getName()
	{
		return name;
	}
	
	public final void setName(String name)
	{
		if(parent != null)
		{
			int j = 0;
			
			String on = name;
			while(parent.byName.containsKey(on)) on = name + " (" + (++j) + ")";
			name = on;
			
			parent.byName.put(name, this);
			
			// only remove previous value if it is current
			if(parent.byName.get(this.name) == this)
				parent.byName.remove(this.name);
		}
		
		this.name = name;
	}
	
	public GuiObject setEnabled(boolean enabled)
	{
		this.enabled = enabled;
		return this;
	}
	
	public GuiObject setVisible(boolean visible)
	{
		this.visible = visible;
		return this;
	}
	
	public GuiObject centered(float width, float height)
	{
		pos((width - this.width) / 2, (height - this.height) / 2);
		return this;
	}
	
	public GuiObject centered(int width, int height)
	{
		pos((int) ((width - this.width) / 2), (int) ((height - this.height) / 2));
		return this;
	}
	
	public GuiObject centeredX(float width)
	{
		pos((width - this.width) / 2, pos.y());
		return this;
	}
	
	public GuiObject centeredX(int width)
	{
		pos((int) ((width - this.width) / 2), pos.y());
		return this;
	}
	
	public GuiObject centeredY(float height)
	{
		pos(pos.x(), (height - this.height) / 2);
		return this;
	}
	
	public GuiObject centeredY(int height)
	{
		pos(pos.x(), (int) ((height - this.height) / 2));
		return this;
	}
	
	public GuiObject pos(float x, float y)
	{
		this.pos = new Point(x, y);
		return this;
	}
	
	public GuiObject offset(float x, float y)
	{
		this.pos = this.pos.offset(x, y);
		return this;
	}
	
	public GuiObject size(float width, float height)
	{
		this.width = width;
		this.height = height;
		return this;
	}
	
	public GuiObject pivot(float x, float y)
	{
		this.pivot = new Point(x, y);
		return this;
	}
	
	public GuiObject pivotAtCenter()
	{
		return pivot(getUnscaledWidth() / 2, getUnscaledHeight() / 2);
	}
	
	public GuiObject rotation(float rotation)
	{
		this.rotation = rotation;
		return this;
	}
	
	public float getUnscaledWidth()
	{
		return width;
	}
	
	public float getUnscaledHeight()
	{
		return height;
	}
	
	public float getScaledWidth()
	{
		return (float) (getUnscaledWidth() * scale.x);
	}
	
	public float getScaledHeight()
	{
		return (float) (getUnscaledHeight() * scale.y);
	}
	
	public GuiObject usePos(Consumer<DirectStorage<Point>> handler)
	{
		handler.accept(elementPosition);
		return this;
	}
	
	public GuiObject usePivot(Consumer<DirectStorage<Point>> handler)
	{
		handler.accept(elementPivot);
		return this;
	}
	
	public GuiObject useScale(Consumer<DirectStorage<Vec3>> handler)
	{
		handler.accept(elementScale);
		return this;
	}
	
	public GuiObject useRotation(Consumer<DirectStorage<Float>> handler)
	{
		handler.accept(elementRotation);
		return this;
	}
	
	protected void update()
	{
	}
	
	protected void render(Graphics gfx, MousePos pos)
	{
	}
	
	protected boolean onMouseClicked(Point globalMousePos, MousePos pos, int button, boolean fake)
	{
		return false;
	}
	
	protected boolean onMouseDragged(Point globalMousePos, MousePos pos, int button, MousePos dragPos)
	{
		return false;
	}
	
	protected boolean onMouseReleased(Point globalMousePos, MousePos pos, int button)
	{
		return false;
	}
	
	protected boolean onMouseScrolled(Point globalMousePos, MousePos pos, ScrollData delta)
	{
		return false;
	}
	
	protected void onMouseMoved(Point globalMousePos, MousePos pos)
	{
	}
	
	protected boolean onKeyPressed(int keyCode, int scanCode, int modifiers)
	{
		return false;
	}
	
	protected boolean onKeyReleased(int keyCode, int scanCode, int modifiers)
	{
		return false;
	}
	
	protected boolean onCharTyped(char codePoint, int modifiers)
	{
		return false;
	}
	
	public final void sendUpdate()
	{
		if(!enabled) return;
		if(visible) update();
		for(GuiObject child : children) child.sendUpdate();
	}
	
	public final <T> void runForTree(PoseStack pose, Class<T> filter, BiConsumer<T, PoseStack> handler)
	{
		runForTree(pose, (obj, ps) ->
				{
					T t = Cast.cast(obj, filter);
					if(t != null) handler.accept(t, ps);
				}
		);
	}
	
	public final <T, R> Optional<R> findInTree(PoseStack pose, Class<T> filter, BiFunction<T, PoseStack, Optional<R>> handler)
	{
		return findInTree(pose, (obj, ps) ->
				{
					T t = Cast.cast(obj, filter);
					if(t != null) return handler.apply(t, ps);
					return Optional.empty();
				}
		);
	}
	
	public final void runForTree(PoseStack pose, BiConsumer<GuiObject, PoseStack> handler)
	{
		if(!enabled) return;
		pose.pushPose();
		transform(pose);
		if(visible) handler.accept(this, pose);
		for(GuiObject child : children)
			child.runForTree(pose, handler);
		pose.popPose();
	}
	
	public final <R> Optional<R> findInTree(PoseStack pose, BiFunction<GuiObject, PoseStack, Optional<R>> handler)
	{
		if(!enabled) return Optional.empty();
		pose.pushPose();
		transform(pose);
		if(visible)
		{
			var o = handler.apply(this, pose);
			if(o.isPresent())
			{
				pose.popPose();
				return o;
			}
		}
		for(GuiObject child : children)
		{
			var o = child.findInTree(pose, handler);
			if(o.isPresent())
			{
				pose.popPose();
				return o;
			}
		}
		pose.popPose();
		return Optional.empty();
	}
	
	public final void renderObject(Graphics g, Point globalMousePos)
	{
		if(!enabled) return;
		PoseStack ps = g.gfx().pose();
		ps.pushPose();
		transform(ps);
		
		if(visible)
		{
			Vector3f v = untransform(ps).transformPosition(globalMousePos.x(), globalMousePos.y(), 0, new Vector3f());
			render(g, new MousePos(globalMousePos, v.x, v.y));
		}
		
		for(GuiObject child : children)
			child.renderObject(g, globalMousePos);
		
		ps.popPose();
	}
	
	public boolean isFakeMouseButton(int button)
	{
		return SIMULATED_MOUSE_BUTTON == button;
	}
	
	public final boolean sendMouseClick(PoseStack ps, Point globalMousePos, int button)
	{
		if(!enabled) return false;
		ps.pushPose();
		transform(ps);
		
		if(visible)
		{
			Vector3f v = untransform(ps).transformPosition(globalMousePos.x(), globalMousePos.y(), 0, new Vector3f());
			if(onMouseClicked(globalMousePos, new MousePos(globalMousePos, v.x, v.y), button, isFakeMouseButton(button)))
				return true;
		}
		
		for(GuiObject child : children)
			if(child.sendMouseClick(ps, globalMousePos, button))
				return true;
		
		ps.popPose();
		return false;
	}
	
	public final boolean sendMouseDrag(PoseStack ps, Point globalMousePos, int button, Point globalDragPos)
	{
		if(!enabled) return false;
		ps.pushPose();
		transform(ps);
		
		if(visible)
		{
			Matrix4f ivm = untransform(ps);
			Vector3f v = ivm.transformPosition(globalMousePos.x(), globalMousePos.y(), 0, new Vector3f());
			Vector3f d = ivm.transformPosition(globalDragPos.x(), globalDragPos.y(), 0, new Vector3f());
			if(onMouseDragged(globalMousePos, new MousePos(globalMousePos, v.x, v.y), button, new MousePos(globalDragPos, d.x, d.y)))
				return true;
		}
		
		for(GuiObject child : children)
			if(child.sendMouseDrag(ps, globalMousePos, button, globalDragPos))
				return true;
		
		ps.popPose();
		return false;
	}
	
	public final boolean sendMouseRelease(PoseStack ps, Point globalMousePos, int button)
	{
		if(!enabled) return false;
		ps.pushPose();
		transform(ps);
		
		if(visible)
		{
			Vector3f v = untransform(ps).transformPosition(globalMousePos.x(), globalMousePos.y(), 0, new Vector3f());
			if(onMouseReleased(globalMousePos, new MousePos(globalMousePos, v.x, v.y), button))
				return true;
		}
		
		for(GuiObject child : children)
			if(child.sendMouseRelease(ps, globalMousePos, button))
				return true;
		
		ps.popPose();
		return false;
	}
	
	public final boolean sendMouseScroll(PoseStack ps, Point globalMousePos, ScrollData delta)
	{
		if(!enabled) return false;
		ps.pushPose();
		transform(ps);
		
		if(visible)
		{
			Vector3f v = untransform(ps).transformPosition(globalMousePos.x(), globalMousePos.y(), 0, new Vector3f());
			if(onMouseScrolled(globalMousePos, new MousePos(globalMousePos, v.x, v.y), delta))
				return true;
		}
		
		for(GuiObject child : children)
			if(child.sendMouseScroll(ps, globalMousePos, delta))
				return true;
		
		ps.popPose();
		return false;
	}
	
	public final void sendMouseMove(PoseStack ps, Point globalMousePos)
	{
		if(!enabled) return;
		ps.pushPose();
		transform(ps);
		if(visible)
		{
			Vector3f v = untransform(ps).transformPosition(globalMousePos.x(), globalMousePos.y(), 0, new Vector3f());
			onMouseMoved(globalMousePos, new MousePos(globalMousePos, v.x, v.y));
		}
		for(GuiObject child : children)
			child.sendMouseMove(ps, globalMousePos);
		ps.popPose();
	}
	
	public final boolean sendKeyPress(int keyCode, int scanCode, int modifiers)
	{
		if(!enabled) return false;
		if(visible && onKeyPressed(keyCode, scanCode, modifiers)) return true;
		for(GuiObject child : children)
			if(child.sendKeyPress(keyCode, scanCode, modifiers))
				return true;
		return false;
	}
	
	public final boolean sendKeyRelease(int keyCode, int scanCode, int modifiers)
	{
		if(!enabled) return false;
		if(visible && onKeyReleased(keyCode, scanCode, modifiers)) return true;
		for(GuiObject child : children)
			if(child.sendKeyRelease(keyCode, scanCode, modifiers))
				return true;
		return false;
	}
	
	public final boolean sendCharType(char codePoint, int modifiers)
	{
		if(!enabled) return false;
		if(visible && onCharTyped(codePoint, modifiers)) return true;
		for(GuiObject child : children)
			if(child.sendCharType(codePoint, modifiers))
				return true;
		return false;
	}
	
	public void transform(PoseStack ps)
	{
		ps.translate(pos.x(), pos.y(), zOffset);
		
		if(scale != ONE)
			ps.scale((float) scale.x, (float) scale.y, (float) scale.z);
		
		ps.translate(pivot.x(), pivot.y(), 0);
		ps.mulPose(Axis.ZP.rotationDegrees(rotation));
		ps.translate(-pivot.x(), -pivot.y(), 0);
	}
	
	@Override
	public String toString()
	{
		return simpleName + '{' + getName() + '}';
	}
	
	/**
	 * Create a root for your GUI.
	 * <p>
	 * This is the object which holds all components.
	 */
	public static GuiRootObject root()
	{
		return new GuiRootObject();
	}
	
	/**
	 * Start building a default GUI component.
	 * <p>
	 * You must add it using {@link GuiObject#addChild(GuiObject)}
	 */
	public static GuiObjectBuilder create(String s)
	{
		return GuiObjectBuilder.named(s);
	}
	
	/**
	 * Creates inverted matrix of last pose.
	 * <p>
	 * Transforming positions using this matrix will convert them from global to relative space.
	 */
	public static Matrix4f untransform(PoseStack pose)
	{
		return pose.last().pose().invert(new Matrix4f());
	}
	
	protected static boolean anyMatchesWithinTree(GuiObject root, Predicate<GuiObject> obj)
	{
		for(GuiObject child : root.children) if(anyMatchesWithinTree(child, obj)) return true;
		
		while(root != null)
		{
			if(obj.test(root)) return true;
			root = root.parent; // step up
		}
		
		return false;
	}
	
	public GuiObject scale(float scale)
	{
		this.scale = this.scale.scale(scale);
		return this;
	}
	
	public GuiObject scale(float x, float y)
	{
		return scale(x, y, x);
	}
	
	public GuiObject scale(float x, float y, float z)
	{
		this.scale = this.scale.multiply(x, y, z);
		return this;
	}
}