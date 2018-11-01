// Main file of congard OBJLoader
// Version 1.0.1
// 9 feb 2018 18:58
// dbcongard@gmail.com
// t.me/congard
// congard.pp.ua
// GitHub page: https://github.com/congard/universal-obj-loader

package com.zeitheron.hammercore.lib.objl;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class Model
{
	public static final Logger LOGGER = LogManager.getLogger();
	
	public float[] vertices, texCoords, normals;
	public VerticesDescriptor[] vd;
	public float minX, maxX, minY, maxY, minZ, maxZ;
	public int POLY_TYPE_TRIANGLES = 0, POLY_TYPE_QUADS = 1, POLY_TYPE_POLYGON = 2;
	public OBJLoader loader;
	
	public Model(InputStream obj)
	{
		loader = new OBJLoader(this);
		try
		{
			loader.load(new BufferedReader(new InputStreamReader(obj)));
		} catch(IOException e)
		{
			LOGGER.error("IOException >> Error loading model: " + e);
		} catch(Exception e)
		{
			e.printStackTrace();
			LOGGER.error("Exception >> Error loading model: " + e);
		}
	}
	
	// [EN] Clears arrays to consume less memory (can be called after the
	// Model.convertToFloatArray)
	public void cleanup()
	{
		loader.faces.clear();
		loader.facesnorms.clear();
		loader.facestexs.clear();
		loader.vertexsets.clear();
		loader.vertexsetsnorms.clear();
		loader.vertexsetstexs.clear();
	}
	
	// [EN] Creates primitive arrays for further rendering
	public void convertToFloatArrays(boolean isMakeNormalsArray, boolean isMakeTexCoordsArray)
	{
		loader.convertToArrays(isMakeNormalsArray, isMakeTexCoordsArray);
	}
	
	// >>
	public void enable(int funct)
	{
		if(funct == LoaderConstants.ONE_MINUS_TEX_COORD)
			loader.texA = 1;
		else if(funct == LoaderConstants.TEX_VERTEX_2D || funct == LoaderConstants.TEX_VERTEX_3D)
			loader.TEX_MODE = funct;
	}
	
	public void disable(int funct)
	{
		if(funct == LoaderConstants.ONE_MINUS_TEX_COORD)
			loader.texA = 0;
		else
			System.out.println("Can't disable this function");
	}
	// <<
	
	// [EN] Sets the types of polygons used for rendering
	// [EN] For example, if you do not make a check for what type it is, you can
	// predefine
	// setDefaultPolyTypes(GL2.GL_TRIANGLES, GL2.GL_QUADS, GL2.GL_POLYGON)
	public void setDefaultPolyTypes(int POLY_TYPE_TRIANGLES, int POLY_TYPE_QUADS, int POLY_TYPE_POLYGON)
	{
		this.POLY_TYPE_TRIANGLES = POLY_TYPE_TRIANGLES;
		this.POLY_TYPE_QUADS = POLY_TYPE_QUADS;
		this.POLY_TYPE_POLYGON = POLY_TYPE_POLYGON;
	}
	
	// [EN] A class that stores data about vertices and is needed when rendering
	public static class VerticesDescriptor
	{
		public static final int UNDEFINED_POLY_TYPE = -1;
		public int POLYTYPE = UNDEFINED_POLY_TYPE, START = 0, END = 0;
	}
}