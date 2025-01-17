package org.zeith.hammerlib.util.mcf.shapes;

import net.minecraft.core.Direction;
import net.minecraft.world.level.block.Rotation;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.shapes.*;
import org.zeith.hammerlib.util.AABBUtils;

import java.util.List;
import java.util.stream.Stream;

public class ShapeUtils
{
	public static VoxelShape rotateNorthShape(VoxelShape shape, Direction rotationIn)
	{
		return join(shape.toAabbs()
				.stream()
				.map(aabb -> AABBUtils.rotateNorthBox(aabb, rotationIn))
				.map(Shapes::create)
		);
	}
	
	public static VoxelShape rotate(VoxelShape shape, Rotation rotationIn)
	{
		return join(shape.toAabbs()
				.stream()
				.map(aabb -> AABBUtils.rotate(aabb, rotationIn))
				.map(Shapes::create)
		);
	}
	
	public static VoxelShape join(Stream<VoxelShape> boxes)
	{
		return boxes.reduce((a, b) -> Shapes.joinUnoptimized(a, b, BooleanOp.OR))
				.map(VoxelShape::optimize)
				.orElseGet(Shapes::empty);
	}
	
	public static VoxelShape join(List<AABB> boxes)
	{
		VoxelShape ret = Shapes.empty();
		for(AABB aabb : boxes) ret = Shapes.joinUnoptimized(ret, Shapes.create(aabb), BooleanOp.OR);
		return ret.optimize();
	}
}
