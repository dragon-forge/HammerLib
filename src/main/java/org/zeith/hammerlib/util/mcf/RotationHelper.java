package org.zeith.hammerlib.util.mcf;

import net.minecraft.core.Direction;
import net.minecraft.world.level.block.Rotation;

public class RotationHelper
{
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
}