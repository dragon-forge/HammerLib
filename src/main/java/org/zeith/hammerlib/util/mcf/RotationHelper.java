package org.zeith.hammerlib.util.mcf;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.level.block.Rotation;

import java.util.Random;
import java.util.function.BiFunction;

public class RotationHelper
{
	private static final PivotRotation[] ROTATION_MAP = new PivotRotation[6];
	private static final Direction[] DIRECTIONS = Direction.values();
	
	private static final Direction[][][] ROTATION_MATRIX = {
			{ // DOWN
					{ // AROUND X
							Direction.DOWN,
							Direction.NORTH,
							Direction.UP,
							Direction.SOUTH
					},
					{ // AROUND Y, NOTHING CHANGES HERE
							Direction.DOWN
					},
					{ // AROUND Z
							Direction.DOWN,
							Direction.EAST,
							Direction.UP,
							Direction.WEST
					}
			},
			{ // UP
					{ // AROUND X
							Direction.UP,
							Direction.SOUTH,
							Direction.DOWN,
							Direction.NORTH
					},
					{ // AROUND Y, NOTHING CHANGES HERE
							Direction.UP
					},
					{ // AROUND Z
							Direction.UP,
							Direction.WEST,
							Direction.DOWN,
							Direction.EAST
					}
			},
			{ // NORTH
					{ // AROUND X
							Direction.NORTH,
							Direction.UP,
							Direction.SOUTH,
							Direction.DOWN
					},
					{ // AROUND Y
							Direction.NORTH,
							Direction.EAST,
							Direction.SOUTH,
							Direction.WEST
					},
					{ // AROUND Z, NOTHING CHANGES HERE
							Direction.NORTH
					}
			},
			{ // SOUTH
					{ // AROUND X
							Direction.SOUTH,
							Direction.DOWN,
							Direction.NORTH,
							Direction.UP
					},
					{ // AROUND Y
							Direction.SOUTH,
							Direction.WEST,
							Direction.NORTH,
							Direction.EAST
					},
					{ // AROUND Z, NOTHING CHANGES HERE
							Direction.SOUTH
					}
			},
			{ // WEST
					{ // AROUND X, NOTHING CHANGES HERE
							Direction.WEST,
					},
					{ // AROUND Y
							Direction.WEST,
							Direction.NORTH,
							Direction.EAST,
							Direction.SOUTH
					},
					{ // AROUND Z
							Direction.WEST,
							Direction.DOWN,
							Direction.EAST,
							Direction.UP
					}
			},
			{ // EAST
					{ // AROUND X, NOTHING CHANGES HERE
							Direction.EAST,
					},
					{ // AROUND Y
							Direction.EAST,
							Direction.SOUTH,
							Direction.WEST,
							Direction.NORTH
					},
					{ // AROUND Z
							Direction.EAST,
							Direction.UP,
							Direction.WEST,
							Direction.DOWN
					}
			}
	};
	
	public static Direction rotate(Direction dir, Direction.Axis axis, Direction.AxisDirection axisDir, Rotation rotate)
	{
		Direction[][] mat = ROTATION_MATRIX[dir.ordinal()];
		Direction[] rot = mat[axis.ordinal()];
		int nextIdx = rotate.ordinal() * axisDir.getStep();
		while(nextIdx < 0) nextIdx += rot.length;
		while(nextIdx >= rot.length) nextIdx -= rot.length;
		return rot[nextIdx];
	}
	
	public static PivotRotation getRotationFromHorizontal(Direction horizontal)
	{
		return ROTATION_MAP[horizontal.ordinal()];
	}
	
	public static BlockPos rotateAroundPivot(BlockPos pivot, Direction horizontal, BlockPos relative)
	{
		return ROTATION_MAP[horizontal.ordinal()].transform(pivot, relative);
	}
	
	public enum PivotRotation
	{
		Y_0(Direction.NORTH, (pivot, pos) -> pos.immutable()),
		Y_90(Direction.WEST, (pivot, pos) ->
				new BlockPos(pivot.getX() + (pos.getZ() - pivot.getZ()), pos.getY(), pivot.getZ() - (pos.getX() - pivot.getX()))
		),
		Y_180(Direction.SOUTH, (pivot, pos) ->
				new BlockPos(pivot.getX() - (pos.getX() - pivot.getX()), pos.getY(), pivot.getZ() - (pos.getZ() - pivot.getZ()))
		),
		Y_270(Direction.EAST, (pivot, pos) ->
				new BlockPos(pivot.getX() - (pos.getZ() - pivot.getZ()), pos.getY(), pivot.getZ() + (pos.getX() - pivot.getX()))
		);
		
		private static final PivotRotation[] VALUES = values();
		
		public static PivotRotation rand(Random rng)
		{
			return VALUES[rng.nextInt(VALUES.length)];
		}
		
		final Direction face;
		final BiFunction<BlockPos, BlockPos, BlockPos> transformer;
		
		PivotRotation(Direction face, BiFunction<BlockPos, BlockPos, BlockPos> transformer)
		{
			this.face = face;
			this.transformer = transformer;
			ROTATION_MAP[face.ordinal()] = this;
		}
		
		public BlockPos transform(BlockPos pivot, BlockPos pos)
		{
			return transformer.apply(pivot, pos);
		}
		
		public Direction toHorizontal()
		{
			return face;
		}
	}
}