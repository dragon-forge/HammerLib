package com.zeitheron.hammercore.utils;

import net.minecraft.client.renderer.BufferBuilder;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.util.Rotation;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.Vec3d;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import java.util.*;
import java.util.function.Function;
import java.util.stream.Stream;
import java.util.stream.StreamSupport;

public class AABBUtils
{
	public static boolean almostEqual(double a, double b)
	{
		return a - b > -1.0E-4 && a - b < 1.0E-4;
	}

	public static boolean almostEquals(double a, double b, double precision)
	{
		return a - b > -precision && a - b < precision;
	}

	public static Vec3d randomPosWithin(AxisAlignedBB aabb, Random rng)
	{
		double x = aabb.minX + (aabb.maxX - aabb.minX) * rng.nextDouble();
		double y = aabb.minY + (aabb.maxY - aabb.minY) * rng.nextDouble();
		double z = aabb.minZ + (aabb.maxZ - aabb.minZ) * rng.nextDouble();
		return new Vec3d(x, y, z);
	}

	public static AxisAlignedBB rotate(AxisAlignedBB aabb, Rotation rotationIn)
	{
		switch(rotationIn)
		{
			case NONE:
			default:
				return aabb;
			case CLOCKWISE_90:
				return new AxisAlignedBB(-aabb.minX, aabb.minY, aabb.minZ, -aabb.maxX, aabb.maxY, aabb.maxZ);
			case CLOCKWISE_180:
				return new AxisAlignedBB(-aabb.minX, aabb.minY, -aabb.minZ, -aabb.maxX, aabb.maxY, -aabb.maxZ);
			case COUNTERCLOCKWISE_90:
				return new AxisAlignedBB(aabb.minX, aabb.minY, -aabb.minZ, aabb.maxX, aabb.maxY, -aabb.maxZ);
		}
	}

	public static AxisAlignedBB normalize(AxisAlignedBB aabb)
	{
		return aabb.offset(-aabb.minX, -aabb.minY, -aabb.minZ);
	}

	public static AxisAlignedBB partial(AxisAlignedBB prev, AxisAlignedBB cur, float partialTicks)
	{
		double x1 = prev.minX + (cur.minX - prev.minX) * partialTicks;
		double y1 = prev.minY + (cur.minY - prev.minY) * partialTicks;
		double z1 = prev.minZ + (cur.minZ - prev.minZ) * partialTicks;

		double x2 = prev.maxX + (cur.maxX - prev.maxX) * partialTicks;
		double y2 = prev.maxY + (cur.maxY - prev.maxY) * partialTicks;
		double z2 = prev.maxZ + (cur.maxZ - prev.maxZ) * partialTicks;

		return new AxisAlignedBB(x1, y1, z1, x2, y2, z2);
	}

	public static AxisAlignedBB extrudeGravity(AxisAlignedBB aabb, float ySpeed)
	{
		float ay = Math.abs(ySpeed);
		return aabb.grow(-ay * (aabb.maxX - aabb.minX) / 2, ay * (aabb.maxY - aabb.minY) / 2, -ay * (aabb.maxZ - aabb.minZ) / 2).offset(0, -ySpeed * (aabb.maxY - aabb.minY) / 2, 0);
	}

	public static void cut(AxisAlignedBB box, AxisAlignedBB knife, List<AxisAlignedBB> intoList)
	{
		if(!box.intersects(knife))
		{
			intoList.add(box);
			return;
		}

		AxisAlignedBB inter = box.intersect(knife);

		// When cutting does not happen on lowest Y of the box, add floor
		if(!almostEqual(box.minY, inter.minY))
			intoList.add(new AxisAlignedBB(box.minX, box.minY, box.minZ, box.maxX, inter.minY, box.maxZ));

		// When cutting does not happen on highest Y of the box, add ceiling
		if(!almostEqual(box.maxY, inter.maxY))
			intoList.add(new AxisAlignedBB(box.minX, inter.maxY, box.minZ, box.maxX, box.maxY, box.maxZ));

		// Positive X and Z boxes
		{
			boolean ea1, ea2;

			if(ea1 = !almostEqual(box.maxX, inter.maxX))
			{
				intoList.add(new AxisAlignedBB(inter.maxX, inter.minY, box.minZ, box.maxX, inter.maxY, inter.maxZ));
			}

			if(ea2 = !almostEqual(box.maxZ, inter.maxZ))
			{
				intoList.add(new AxisAlignedBB(box.minX, inter.minY, inter.maxZ, inter.maxX, inter.maxY, box.maxZ));
			}

			if(ea1 && ea2)
				intoList.add(new AxisAlignedBB(inter.maxX, inter.minY, inter.maxZ, box.maxX, inter.maxY, box.maxZ));
		}

		// Negative X and Z boxes
		{
			boolean ea1, ea2;

			if(ea1 = !almostEqual(box.minZ, inter.minZ))
			{
				intoList.add(new AxisAlignedBB(inter.minX, inter.minY, box.minZ, inter.maxX, inter.maxY, inter.minZ));
			}

			if(ea2 = !almostEqual(box.minX, inter.minX))
			{
				intoList.add(new AxisAlignedBB(box.minX, inter.minY, inter.minZ, inter.minX, inter.maxY, inter.maxZ));
			}

			if(ea1 || ea2)
				intoList.add(new AxisAlignedBB(box.minX, inter.minY, box.minZ, inter.minX, inter.maxY, inter.minZ));
		}
	}

	public static Vec3d getCenter(AxisAlignedBB aabb)
	{
		return new Vec3d(aabb.minX + (aabb.maxX - aabb.minX) * 0.5D, aabb.minY + (aabb.maxY - aabb.minY) * 0.5D, aabb.minZ + (aabb.maxZ - aabb.minZ) * 0.5D);
	}

	public interface ILinable
	{
		/**
		 * Returns a list of lines to render.
		 * In case of {@link com.zeitheron.hammercore.utils.math.VoxelShape}s, return is clean and deduped from similar lines.
		 */
		List<Line> asLines();
	}

	public static class Line
			implements ILinable
	{
		public static final double SPATIAL_PRECISION = 0.00001;

		public final Vec3d a, b;

		public Line(Vec3d a, Vec3d b)
		{
			this.a = a;
			this.b = b;
		}

		@Override
		public boolean equals(Object o)
		{
			if(this == o) return true;
			if(o == null || getClass() != o.getClass()) return false;
			Line line = (Line) o;
			return (almostEquals(a, line.a, SPATIAL_PRECISION) && almostEquals(b, line.b, SPATIAL_PRECISION))
					|| (almostEquals(a, line.b, SPATIAL_PRECISION) && almostEquals(b, line.a, SPATIAL_PRECISION));
		}

		@Override
		public int hashCode()
		{
			return Objects.hash(a, b);
		}

		@Override
		public String toString()
		{
			return "Line{" +
					"start=" + a +
					", end=" + b +
					'}';
		}

		public static boolean almostEquals(Vec3d a, Vec3d b, double precision)
		{
			return AABBUtils.almostEquals(a.x, b.x, precision) && AABBUtils.almostEquals(a.y, b.y, precision) && AABBUtils.almostEquals(a.z, b.z, precision);
		}

		public Line offset(double x, double y, double z)
		{
			return new Line(a.add(x, y, z), b.add(x, y, z));
		}

		public Vec3d center()
		{
			return new Vec3d((a.x + b.x) / 2, (a.y + b.y) / 2, (a.z + b.z) / 2);
		}

		public Line expandFrom(Vec3d center, double expand)
		{
			Vec3d newA = a.subtract(center).normalize().scale(expand);
			Vec3d newB = b.subtract(center).normalize().scale(expand);
			return new Line(a.add(newA), b.add(newB));
		}

		public static Stream<Line> of(AxisAlignedBB aabb)
		{
			Stream.Builder<Line> lines = Stream.builder();
			{
				// define all points
				Vec3d p1 = new Vec3d(aabb.minX, aabb.minY, aabb.minZ);
				Vec3d p2 = new Vec3d(aabb.minX, aabb.minY, aabb.maxZ);
				Vec3d p3 = new Vec3d(aabb.minX, aabb.maxY, aabb.minZ);
				Vec3d p4 = new Vec3d(aabb.minX, aabb.maxY, aabb.maxZ);
				Vec3d p5 = new Vec3d(aabb.maxX, aabb.minY, aabb.minZ);
				Vec3d p6 = new Vec3d(aabb.maxX, aabb.minY, aabb.maxZ);
				Vec3d p7 = new Vec3d(aabb.maxX, aabb.maxY, aabb.minZ);
				Vec3d p8 = new Vec3d(aabb.maxX, aabb.maxY, aabb.maxZ);

				// create lines between all points of this cube
				lines.add(new Line(p1, p2));
				lines.add(new Line(p1, p3));
				lines.add(new Line(p1, p5));
				lines.add(new Line(p2, p4));
				lines.add(new Line(p2, p6));
				lines.add(new Line(p3, p4));
				lines.add(new Line(p3, p7));
				lines.add(new Line(p5, p6));
				lines.add(new Line(p5, p7));
				lines.add(new Line(p8, p4));
				lines.add(new Line(p8, p6));
				lines.add(new Line(p8, p7));
			}
			return lines.build();
		}

		public static List<Line> of(AxisAlignedBB... aabbs)
		{
			return of(Arrays.asList(aabbs));
		}

		public static <T> List<Line> of(Function<T, AxisAlignedBB> aabb, T... aabbs)
		{
			return of(aabb, Arrays.asList(aabbs));
		}

		public static <T> List<Line> of(Function<T, AxisAlignedBB> aabb, Iterable<T> aabbs)
		{
			return dedupe(StreamSupport.stream(aabbs.spliterator(), false).map(aabb).flatMap(Line::of));
		}

		public static List<Line> of(Iterable<AxisAlignedBB> aabbs)
		{
			return dedupe(StreamSupport.stream(aabbs.spliterator(), false).flatMap(Line::of));
		}

		public static List<Line> dedupe(Iterable<Line> aabbs)
		{
			return dedupe(StreamSupport.stream(aabbs.spliterator(), false));
		}

		public static List<Line> dedupe(Stream<Line> aabbs)
		{
			List<Line> lines = new ArrayList<>(), dna = new ArrayList<>();
			StreamSupport.stream(aabbs.spliterator(), false)
					.filter(ln -> !dna.contains(ln))
					.forEach(ln ->
					{
						if(!lines.contains(ln))
							lines.add(ln);
						else
						{
							dna.add(ln);
							lines.remove(ln);
						}
					});
			return lines;
		}

		public static List<Line> grow(List<Line> lines, double expand)
		{
			Vec3d center = getCenter(lines);
			lines = new ArrayList<>(lines);
			for(int i = 0; i < lines.size(); i++)
			{
				Line origin = lines.get(i);
				lines.set(i, origin.expandFrom(center, expand));
			}
			return lines;
		}

		public static Vec3d getCenter(List<Line> lines)
		{
			if(lines.isEmpty()) return null;
			double x = 0, y = 0, z = 0;
			for(Line ln : lines)
			{
				Vec3d c = ln.center();
				x += c.x;
				y += c.y;
				z += c.z;
			}
			return new Vec3d(x / lines.size(), y / lines.size(), z / lines.size());
		}

		@Override
		public List<Line> asLines()
		{
			return Collections.singletonList(this);
		}

		@SideOnly(Side.CLIENT)
		public static class LineRenderer
		{
			@SideOnly(Side.CLIENT)
			public static void drawBoundingBox(Stream<Line> lines, float red, float green, float blue, float alpha)
			{
				Tessellator tessellator = Tessellator.getInstance();
				BufferBuilder bufferbuilder = tessellator.getBuffer();
				bufferbuilder.begin(3, DefaultVertexFormats.POSITION_COLOR);
				drawBoundingBox(bufferbuilder, lines, red, green, blue, alpha);
				tessellator.draw();
			}

			@SideOnly(Side.CLIENT)
			public static void drawBoundingBox(BufferBuilder buffer, Stream<Line> lines, float red, float green, float blue, float alpha)
			{
				lines.forEach(ln ->
				{
					buffer.pos(ln.a.x, ln.a.y, ln.a.z).color(red, green, blue, 0.0F).endVertex();
					buffer.pos(ln.a.x, ln.a.y, ln.a.z).color(red, green, blue, alpha).endVertex();
					buffer.pos(ln.b.x, ln.b.y, ln.b.z).color(red, green, blue, alpha).endVertex();
				});
			}
		}
	}
}