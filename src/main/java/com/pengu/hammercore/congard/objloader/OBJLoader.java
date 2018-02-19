package com.pengu.hammercore.congard.objloader;

import java.io.BufferedReader;
import java.io.IOException;
import java.util.ArrayList;

import com.pengu.hammercore.congard.objloader.Model.VerticesDescriptor;

public class OBJLoader
{
	private Model model;
	public ArrayList<float[]> vertexsets = new ArrayList<>();
	public ArrayList<float[]> vertexsetsnorms = new ArrayList<>();
	public ArrayList<float[]> vertexsetstexs = new ArrayList<>();
	public ArrayList<int[]> faces = new ArrayList<>();
	public ArrayList<int[]> facestexs = new ArrayList<>();
	public ArrayList<int[]> facesnorms = new ArrayList<>();
	
	// [EN] Used when creating lists with texture coordinates
	// For example, in JOGL the origin in the lower left corner, and in the
	// OpenGL ES in the upper left
	int texA = 0;
	// [EN] Number of vertices for the texture
	int TEX_MODE = LoaderConstants.TEX_VERTEX_3D;
	
	public OBJLoader(Model model)
	{
		this.model = model;
	}
	
	// >>
	public void load(BufferedReader br) throws IOException
	{
		String line;
		int lineCounter = 0;
		while((line = br.readLine()) != null)
		{
			String[] splitedLine = line.split(" ");
			lineCounter++;
			switch(splitedLine[0])
			{
			case LoaderConstants.OBJ.VERTEX:
				float[] vertex = new float[3];
				vertex[0] = Float.parseFloat(splitedLine[1]); // x
				vertex[1] = Float.parseFloat(splitedLine[2]); // y
				vertex[2] = Float.parseFloat(splitedLine[3]); // z
				
				// points
				// max min x
				if(vertex[0] > model.maxX)
					model.maxX = vertex[0];
				else if(vertex[0] < model.minX)
					model.minX = vertex[0];
				// max min y
				if(vertex[1] > model.maxY)
					model.maxY = vertex[1];
				else if(vertex[1] < model.minY)
					model.minY = vertex[1];
				// max min z
				if(vertex[2] > model.maxZ)
					model.maxZ = vertex[2];
				else if(vertex[2] < model.minZ)
					model.minZ = vertex[2];
				
				vertexsets.add(vertex);
			break;
			case LoaderConstants.OBJ.NORMAL:
				float[] normal = new float[3];
				normal[0] = Float.parseFloat(splitedLine[1]); // x
				normal[1] = Float.parseFloat(splitedLine[2]); // y
				normal[2] = Float.parseFloat(splitedLine[3]); // z
				vertexsetsnorms.add(normal);
			break;
			case LoaderConstants.OBJ.TEXCOORD:
				if(TEX_MODE == LoaderConstants.TEX_VERTEX_2D)
				{
					float[] texCoord = new float[2];
					texCoord[0] = Float.parseFloat(splitedLine[1]); // x
					texCoord[1] = Math.abs(texA - Float.parseFloat(splitedLine[2])); // y
					vertexsetstexs.add(texCoord);
				} else
				{
					float[] texCoord = new float[3];
					texCoord[0] = Float.parseFloat(splitedLine[1]); // x
					texCoord[1] = Math.abs(texA - Float.parseFloat(splitedLine[2])); // y
					texCoord[2] = Float.parseFloat(splitedLine[3]); // w
					vertexsetstexs.add(texCoord);
				}
			break;
			case LoaderConstants.OBJ.FACE:
				int[] fv = new int[splitedLine.length - 1];
				int[] ft = new int[splitedLine.length - 1];
				int[] fn = new int[splitedLine.length - 1];
				for(int i = 1; i < splitedLine.length; i++)
				{
					String[] pos = splitedLine[i].split("/");
					if(pos.length == 1)
						fv[i - 1] = Integer.parseInt(pos[0]); // vertex
					else if(pos.length == 2)
					{
						fv[i - 1] = Integer.parseInt(pos[0]); // vertex
						ft[i - 1] = Integer.parseInt(pos[1]); // texture
						                                      // coordinate
					} else if(pos.length == 3)
					{
						fv[i - 1] = Integer.parseInt(pos[0]); // vertex
						if(!pos[1].equals(""))
							ft[i - 1] = Integer.parseInt(pos[1]); // texture
							                                      // coordinate
						fn[i - 1] = Integer.parseInt(pos[2]); // normal
					}
				}
				faces.add(fv);
				facestexs.add(ft);
				facesnorms.add(fn);
			break;
			case LoaderConstants.OBJ.COMMENT:
			
			break;
			case LoaderConstants.OBJ.OBJECT:
				System.out.println("Loading " + splitedLine[1]);
			break;
			default:
				System.out.println("Unsupported command " + splitedLine[0] + " on line " + lineCounter);
			break;
			}
		}
	}
	// <<
	
	// [EN] Converts lists to float[] for further rendering
	public void convertToArrays(boolean isMakeNormalsArray, boolean isMakeTexCoordsArray)
	{
		ArrayList<Float> vertices = new ArrayList<>();
		ArrayList<Float> textures = new ArrayList<>();
		ArrayList<Float> normals = new ArrayList<>();
		ArrayList<Model.VerticesDescriptor> vertd = new ArrayList<>();
		int counter = 0;
		for(int i = 0; i < faces.size(); i++)
		{
			int[] tempfaces = faces.get(i);
			int[] tempfacesnorms = facesnorms.get(i);
			int[] tempfacestexs = facestexs.get(i);
			
			if(vertd.size() == 0)
				vertd.add(new Model.VerticesDescriptor());
			
			if(tempfaces.length == 3)
			{
				if(vertd.get(vertd.size() - 1).POLYTYPE == model.POLY_TYPE_TRIANGLES || vertd.get(vertd.size() - 1).POLYTYPE == VerticesDescriptor.UNDEFINED_POLY_TYPE)
				{
					vertd.get(vertd.size() - 1).END += 3;
					vertd.get(vertd.size() - 1).POLYTYPE = model.POLY_TYPE_TRIANGLES;
				} else
				{
					Model.VerticesDescriptor nvd = new Model.VerticesDescriptor();
					nvd.START = counter;
					nvd.END += 3;
					nvd.POLYTYPE = model.POLY_TYPE_TRIANGLES;
					vertd.add(nvd);
				}
			} else if(tempfaces.length == 4)
			{
				vertd.get(vertd.size() - 1);
				if(vertd.get(vertd.size() - 1).POLYTYPE == model.POLY_TYPE_QUADS || vertd.get(vertd.size() - 1).POLYTYPE == VerticesDescriptor.UNDEFINED_POLY_TYPE)
				{
					vertd.get(vertd.size() - 1).END += 4;
					vertd.get(vertd.size() - 1).POLYTYPE = model.POLY_TYPE_QUADS;
				} else
				{
					Model.VerticesDescriptor nvd = new Model.VerticesDescriptor();
					nvd.START = counter;
					nvd.END += 4;
					nvd.POLYTYPE = model.POLY_TYPE_QUADS;
					vertd.add(nvd);
				}
			} else
			{
				vertd.get(vertd.size() - 1);
				if(vertd.get(vertd.size() - 1).POLYTYPE == VerticesDescriptor.UNDEFINED_POLY_TYPE)
				{
					vertd.get(vertd.size() - 1).END += tempfaces.length;
					vertd.get(vertd.size() - 1).POLYTYPE = model.POLY_TYPE_POLYGON;
				} else
				{
					Model.VerticesDescriptor nvd = new Model.VerticesDescriptor();
					nvd.START = counter;
					nvd.END += tempfaces.length;
					nvd.POLYTYPE = model.POLY_TYPE_POLYGON;
					vertd.add(nvd);
				}
			}
			counter += tempfaces.length;
			
			for(int w = 0; w < tempfaces.length; w++)
			{
				if(isMakeNormalsArray)
					if(tempfacesnorms[w] != 0)
					{
						normals.add((vertexsetsnorms.get(tempfacesnorms[w] - 1))[0]);
						normals.add((vertexsetsnorms.get(tempfacesnorms[w] - 1))[1]);
						normals.add((vertexsetsnorms.get(tempfacesnorms[w] - 1))[2]);
					} else
					{
						normals.add(0f);
						normals.add(0f);
						normals.add(0f);
					}
				
				if(isMakeTexCoordsArray)
					if(tempfacestexs[w] != 0)
					{
						textures.add((vertexsetstexs.get(tempfacestexs[w] - 1))[0]);
						textures.add((vertexsetstexs.get(tempfacestexs[w] - 1))[1]);
						if(TEX_MODE == LoaderConstants.TEX_VERTEX_3D)
							textures.add((vertexsetstexs.get(tempfacestexs[w] - 1))[2]);
					} else
					{
						textures.add(0f);
						textures.add(0f);
						if(TEX_MODE == LoaderConstants.TEX_VERTEX_3D)
							textures.add(0f);
					}
				
				vertices.add((vertexsets.get(tempfaces[w] - 1))[0]);
				vertices.add((vertexsets.get(tempfaces[w] - 1))[1]);
				vertices.add((vertexsets.get(tempfaces[w] - 1))[2]);
			}
		}
		
		model.vertices = CgrTools.convertArrayListToArray(vertices);
		if(isMakeTexCoordsArray)
			model.texCoords = CgrTools.convertArrayListToArray(textures);
		if(isMakeNormalsArray)
			model.normals = CgrTools.convertArrayListToArray(normals);
		model.vd = new Model.VerticesDescriptor[vertd.size()];
		for(int i = 0; i < vertd.size(); i++)
			model.vd[i] = vertd.get(i);
		
		// cleanup
		vertd.clear();
		vertices.clear();
		textures.clear();
		normals.clear();
	}
}