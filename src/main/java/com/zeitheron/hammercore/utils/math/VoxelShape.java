package com.zeitheron.hammercore.utils.math;

import com.zeitheron.hammercore.utils.AABBUtils;
import net.minecraft.util.math.AxisAlignedBB;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Stream;

/**
 * A simple collection of lines, that can be modified
 */
public class VoxelShape
		implements AABBUtils.ILinable
{
	private final List<AABBUtils.Line> lines = new ArrayList<>();

	public static VoxelShape start()
	{
		return new VoxelShape();
	}

	private VoxelShape(List<AABBUtils.Line> lns)
	{
		this.lines.addAll(lns);
	}

	private VoxelShape(Stream<AABBUtils.Line> lns)
	{
		lns.forEach(this.lines::add);
	}

	private VoxelShape()
	{
	}

	public VoxelShape add(AABBUtils.Line line)
	{
		lines.add(line);
		return this;
	}

	public VoxelShape addAll(AABBUtils.Line... lines)
	{
		for(AABBUtils.Line ln : lines) add(ln);
		return this;
	}

	public VoxelShape add(AxisAlignedBB aabb)
	{
		AABBUtils.Line.of(aabb).forEach(lines::add);
		return this;
	}

	public VoxelShape addAll(AxisAlignedBB... aabbs)
	{
		for(AxisAlignedBB aabb : aabbs) add(aabb);
		return this;
	}

	public VoxelShape add(AABBUtils.ILinable linable)
	{
		lines.addAll(linable.asLines());
		return this;
	}

	public VoxelShape addAll(AABBUtils.ILinable... linables)
	{
		for(AABBUtils.ILinable lnb : linables) add(lnb);
		return this;
	}

	public VoxelShape grow(double expand)
	{
		List<AABBUtils.Line> nl = AABBUtils.Line.grow(lines, expand);
		lines.clear();
		lines.addAll(nl);
		return this;
	}

	@Override
	public List<AABBUtils.Line> asLines()
	{
		return AABBUtils.Line.dedupe(lines);
	}

	public VoxelShape offset(double x, double y, double z)
	{
		return new VoxelShape(lines.stream().map(ln -> ln.offset(x, y, z)));
	}
}