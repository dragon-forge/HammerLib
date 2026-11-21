package org.zeith.hammerlib.util.math;

import com.google.common.collect.AbstractIterator;
import net.minecraft.core.*;
import net.minecraft.util.Mth;
import net.minecraft.world.phys.Vec3;

import java.util.Iterator;

public class GridTraversal
{
	public static Iterable<BoxHit> traversable(Vec3 startVec, Vec3 end)
	{
		return () -> traverse(startVec, end);
	}
	
	public static Iterator<BoxHit> traverse(Vec3 startVec, Vec3 end)
	{
		if(startVec == null || Double.isNaN(startVec.x) || Double.isNaN(startVec.y) || Double.isNaN(startVec.z)) return empty();
		if(end == null || Double.isNaN(end.x) || Double.isNaN(end.y) || Double.isNaN(end.z)) return empty();
		
		return new AbstractIterator<>()
		{
			Vec3 pos = startVec;
			
			final int endX = Mth.floor(end.x);
			final int endY = Mth.floor(end.y);
			final int endZ = Mth.floor(end.z);
			
			int x = Mth.floor(pos.x);
			int y = Mth.floor(pos.y);
			int z = Mth.floor(pos.z);
			
			@Override
			protected BoxHit computeNext()
			{
				if(Double.isNaN(pos.x) || Double.isNaN(pos.y) || Double.isNaN(pos.z)) return endOfData();
				if(x == endX && y == endY && z == endZ) return endOfData();
				
				boolean didXStep = true;
				boolean didYStep = true;
				boolean didZStep = true;
				
				double nextBX = 999.0D;
				double nextBY = 999.0D;
				double nextBZ = 999.0D;
				
				if(endX > x) nextBX = (double) x + 1.0D;
				else if(endX < x) nextBX = (double) x + 0.0D;
				else didXStep = false;
				
				if(endY > y) nextBY = (double) y + 1.0D;
				else if(endY < y) nextBY = (double) y + 0.0D;
				else didYStep = false;
				
				if(endZ > z) nextBZ = (double) z + 1.0D;
				else if(endZ < z) nextBZ = (double) z + 0.0D;
				else didZStep = false;
				
				double tMaxX = 999.0D;
				double tMaxY = 999.0D;
				double tMaxZ = 999.0D;
				double dX = end.x - pos.x;
				double dY = end.y - pos.y;
				double dZ = end.z - pos.z;
				
				if(didXStep) tMaxX = (nextBX - pos.x) / dX;
				if(didYStep) tMaxY = (nextBY - pos.y) / dY;
				if(didZStep) tMaxZ = (nextBZ - pos.z) / dZ;
				
				if(tMaxX == -0.0D) tMaxX = -1.0E-4D;
				if(tMaxY == -0.0D) tMaxY = -1.0E-4D;
				if(tMaxZ == -0.0D) tMaxZ = -1.0E-4D;
				
				Direction hitFace;
				
				var pos = this.pos;
				if(tMaxX < tMaxY && tMaxX < tMaxZ)
				{
					hitFace = endX > x ? Direction.WEST : Direction.EAST;
					pos = new Vec3(nextBX, pos.y + dY * tMaxX, pos.z + dZ * tMaxX);
				} else if(tMaxY < tMaxZ)
				{
					hitFace = endY > y ? Direction.DOWN : Direction.UP;
					pos = new Vec3(pos.x + dX * tMaxY, nextBY, pos.z + dZ * tMaxY);
				} else
				{
					hitFace = endZ > z ? Direction.NORTH : Direction.SOUTH;
					pos = new Vec3(pos.x + dX * tMaxZ, pos.y + dY * tMaxZ, nextBZ);
				}
				
				x = Mth.floor(pos.x) - (hitFace == Direction.EAST ? 1 : 0);
				y = Mth.floor(pos.y) - (hitFace == Direction.UP ? 1 : 0);
				z = Mth.floor(pos.z) - (hitFace == Direction.SOUTH ? 1 : 0);
				
				this.pos = pos;
				return new BoxHit(new BlockPos(x, y, z), pos, hitFace);
			}
		};
	}
	
	public static Iterator<BoxHit> empty()
	{
		return new AbstractIterator<>()
		{
			@Override
			protected BoxHit computeNext()
			{
				return endOfData();
			}
		};
	}
}
