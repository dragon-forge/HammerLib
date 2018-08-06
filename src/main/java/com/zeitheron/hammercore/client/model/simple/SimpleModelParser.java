package com.zeitheron.hammercore.client.model.simple;

import java.nio.ByteBuffer;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.apache.commons.lang3.ArrayUtils;

import com.zeitheron.hammercore.utils.NPEUtils;
import com.zeitheron.hammercore.utils.UnmodifiableList;
import com.zeitheron.hammercore.utils.math.ExpressionEvaluator;

import net.minecraft.util.EnumFacing;

/**
 * Parser class for simple model files.
 */
public class SimpleModelParser
{
	/**
	 * Parses a string passed to this method.
	 * 
	 * @param model
	 *            A string with model to parse.
	 * @return An opnode list.
	 */
	public static UnmodifiableList<int[]> toOpcodes(String model)
	{
		int[] node = null;
		List<int[]> nodes = new ArrayList<>();
		model = model.replaceAll("\r", "");
		String[] lines = model.split("\n");
		
		for(String s : lines)
		{
			if(s.equals("{"))
			{
				if(node == null)
					node = new int[0];
				else
					throw new RuntimeException("unterminated node " + Arrays.toString(node));
			} else if(s.equals("}"))
			{
				NPEUtils.checkNotNull(node, "node");
				nodes.add(node);
				node = null;
			} else if(s.startsWith("name "))
			{
				NPEUtils.checkNotNull(node, "node");
				String name = s.substring(5);
				byte[] bt = name.getBytes();
				int[] ir = new int[bt.length + 2];
				ir[0] = ModelOpcodes.INAME;
				ir[1] = bt.length;
				for(int i = 0; i < bt.length; ++i)
					ir[i + 2] = bt[i];
				
				node = ArrayUtils.addAll(node, ir);
			} else if(s.startsWith("texture "))
			{
				NPEUtils.checkNotNull(node, "node");
				
				String dir = s.substring(8, 10);
				int op = 0;
				if(dir.equals("x-"))
					op = EnumFacing.WEST.ordinal();
				else if(dir.equals("x+"))
					op = EnumFacing.EAST.ordinal();
				else if(dir.equals("y-"))
					op = EnumFacing.DOWN.ordinal();
				else if(dir.equals("y+"))
					op = EnumFacing.UP.ordinal();
				else if(dir.equals("z-"))
					op = EnumFacing.NORTH.ordinal();
				else if(dir.equals("z+"))
					op = EnumFacing.SOUTH.ordinal();
				else
					NPEUtils.checkNotNull(null, "unknown orientation: " + dir);
				
				byte[] bt = s.substring(11).getBytes();
				int[] ir = new int[bt.length + 3];
				ir[0] = ModelOpcodes.ITEX;
				ir[1] = bt.length;
				ir[2] = op;
				for(int i = 0; i < bt.length; ++i)
					ir[i + 3] = bt[i];
				
				node = ArrayUtils.addAll(node, ir);
			} else if(s.startsWith("textures "))
			{
				NPEUtils.checkNotNull(node, "node");
				
				for(EnumFacing face : EnumFacing.VALUES)
				{
					byte[] bt = s.substring(9).getBytes();
					int[] ir = new int[bt.length + 3];
					ir[0] = ModelOpcodes.ITEX;
					ir[1] = bt.length;
					ir[2] = face.ordinal();
					for(int i = 0; i < bt.length; ++i)
						ir[i + 3] = bt[i];
					node = ArrayUtils.addAll(node, ir);
				}
			} else if(s.startsWith("disable face "))
			{
				NPEUtils.checkNotNull(node, "node");
				String dir = s.substring(13, 15);
				int op = 0;
				if(dir.equals("x-"))
					op = EnumFacing.WEST.ordinal();
				else if(dir.equals("x+"))
					op = EnumFacing.EAST.ordinal();
				else if(dir.equals("y-"))
					op = EnumFacing.DOWN.ordinal();
				else if(dir.equals("y+"))
					op = EnumFacing.UP.ordinal();
				else if(dir.equals("z-"))
					op = EnumFacing.NORTH.ordinal();
				else if(dir.equals("z+"))
					op = EnumFacing.SOUTH.ordinal();
				else
					NPEUtils.checkNotNull(null, "unknown orientation: " + dir);
				
				node = ArrayUtils.addAll(node, ModelOpcodes.DFACE, op);
			} else if(s.equals("disable faces"))
			{
				NPEUtils.checkNotNull(node, "node");
				node = ArrayUtils.add(node, ModelOpcodes.DFACES);
			} else if(s.startsWith("enable face "))
			{
				NPEUtils.checkNotNull(node, "node");
				String dir = s.substring(12, 14);
				int op = 0;
				if(dir.equals("x-"))
					op = EnumFacing.WEST.ordinal();
				else if(dir.equals("x+"))
					op = EnumFacing.EAST.ordinal();
				else if(dir.equals("y-"))
					op = EnumFacing.DOWN.ordinal();
				else if(dir.equals("y+"))
					op = EnumFacing.UP.ordinal();
				else if(dir.equals("z-"))
					op = EnumFacing.NORTH.ordinal();
				else if(dir.equals("z+"))
					op = EnumFacing.SOUTH.ordinal();
				else
					NPEUtils.checkNotNull(null, "unknown orientation: " + dir);
				
				node = ArrayUtils.addAll(node, ModelOpcodes.EFACE, op);
			} else if(s.equals("enable faces"))
			{
				NPEUtils.checkNotNull(node, "node");
				node = ArrayUtils.add(node, ModelOpcodes.EFACES);
			} else if(s.startsWith("color "))
			{
				NPEUtils.checkNotNull(node, "node");
				String[] c = s.substring(6).split(" ");
				node = ArrayUtils.addAll(node, ModelOpcodes.COLOR, Integer.parseInt(c[0]), Integer.parseInt(c[1]), Integer.parseInt(c[2]), c.length == 3 ? 255 : Integer.parseInt(c[3]));
			} else if(s.startsWith("bounds "))
			{
				NPEUtils.checkNotNull(node, "node");
				String[] bds = s.substring(7).split(" ");
				node = ArrayUtils.add(node, ModelOpcodes.BOUNDS);
				node = addDouble(node, ExpressionEvaluator.evaluateDouble(bds[0]));
				node = addDouble(node, ExpressionEvaluator.evaluateDouble(bds[1]));
				node = addDouble(node, ExpressionEvaluator.evaluateDouble(bds[2]));
				node = addDouble(node, ExpressionEvaluator.evaluateDouble(bds[3]));
				node = addDouble(node, ExpressionEvaluator.evaluateDouble(bds[4]));
				node = addDouble(node, ExpressionEvaluator.evaluateDouble(bds[5]));
			} else if(s.equals("draw"))
			{
				NPEUtils.checkNotNull(node, "node");
				node = ArrayUtils.addAll(node, ModelOpcodes.DRAW);
			}
		}
		
		return new UnmodifiableList(nodes);
	}
	
	public static final ThreadLocal<ByteBuffer> buf = ThreadLocal.withInitial(() ->
	{
		return ByteBuffer.allocate(8);
	});
	
	public static int[] addDouble(int[] array, double d)
	{
		ByteBuffer b = buf.get();
		b.position(0);
		b.putDouble(d);
		b.position(0);
		int int1 = b.getInt();
		int int2 = b.getInt();
		return ArrayUtils.addAll(array, int1, int2);
	}
	
	public static double getDouble(int[] array, int pos)
	{
		ByteBuffer b = buf.get();
		b.position(0);
		b.putInt(array[pos]);
		b.putInt(array[pos + 1]);
		b.position(0);
		return b.getDouble();
	}
}