package com.zeitheron.hammercore.lib.objl;

import java.util.ArrayList;

public class CgrTools
{
	public static float[] convertArrayListToArray(ArrayList<Float> arr)
	{
		float[] result = new float[arr.size()];
		for(int i = 0; i < arr.size(); i++)
			result[i] = arr.get(i);
		return result;
	}
}