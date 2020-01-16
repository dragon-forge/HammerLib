package com.zeitheron.hammercore.client.utils.rendering;

import com.zeitheron.hammercore.api.events.ResourceManagerReloadEvent;
import com.zeitheron.hammercore.utils.base.Cast;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.BufferBuilder;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.client.resources.IResource;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.client.resource.VanillaResourceType;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import org.lwjgl.opengl.GL11;
import org.lwjgl.util.vector.Vector2f;
import org.lwjgl.util.vector.Vector3f;

import javax.annotation.Nullable;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.*;
import java.util.function.Function;
import java.util.function.Supplier;

import static org.lwjgl.opengl.GL11.*;

@SideOnly(Side.CLIENT)
@Mod.EventBusSubscriber(Side.CLIENT)
public class WavefrontLoader
{
	public static IVertexConsumer VC_OPENGL = (pos, tex, normal) ->
	{
		GL11.glNormal3f(normal.getX(), normal.getY(), normal.getZ());
		GL11.glTexCoord2f(tex.getX(), tex.getY());
		GL11.glVertex3f(pos.getX(), pos.getY(), pos.getZ());
	};

	public static IVertexConsumer VC_OPENGL_FLIP_UV = (pos, tex, normal) ->
	{
		GL11.glNormal3f(normal.getX(), normal.getY(), normal.getZ());
		GL11.glTexCoord2f(tex.getX(), 1 - tex.getY());
		GL11.glVertex3f(pos.getX(), pos.getY(), pos.getZ());
	};

	public static final Map<ResourceLocation, WavefrontMeshProvider> MESH_PROVIDERS = new HashMap<>();
	public static final Map<ResourceLocation, WavefrontMesh> LOADED_MESHES = new HashMap<>();

	/**
	 * This method provides OBJ mesh dynamically, through the minecraft resource pipeline.
	 * This allows models to dynamically reload alongside with resources.
	 */
	public static WavefrontMeshProvider getMeshProvider(ResourceLocation path)
	{
		return MESH_PROVIDERS.computeIfAbsent(path, rl0 -> new WavefrontMeshProvider(() -> LOADED_MESHES.computeIfAbsent(path, rl ->
		{
			try(IResource res = Minecraft.getMinecraft().getResourceManager().getResource(path))
			{
				if(res == null)
					throw new FileNotFoundException("assets/" + path.getNamespace() + "/" + path.getPath());
				else return loadModel(res.getInputStream(), 1 / 16F);
			} catch(IOException ioe)
			{
				ioe.printStackTrace();
			}
			return null;
		})));
	}

	public static Function<Vector2f, Vector2f> transformUVToSprite(TextureAtlasSprite sprite)
	{
		return uv -> new Vector2f(sprite.getInterpolatedU(uv.x * 16F), sprite.getInterpolatedV(uv.y * 16F));
	}

	@SubscribeEvent
	public static void reloadResources(ResourceManagerReloadEvent e)
	{
		if(e.isType(VanillaResourceType.MODELS))
			LOADED_MESHES.clear();
	}

	/**
	 * @param stream the stream to be loaded
	 * @return the loaded <code>WavefrontMesh</code>
	 */
	public static WavefrontMesh loadModel(InputStream stream, float scale)
	{
		return loadModel(new Scanner(stream), scale);
	}

	/**
	 * @param sc the <code>WavefrontMesh</code> to be loaded
	 * @param scale the scale of the newly loaded model
	 * @return the loaded <code>WavefrontMesh</code>
	 */
	public static WavefrontMesh loadModel(Scanner sc, float scale)
	{
		WavefrontMesh model = new WavefrontMesh();
		while(sc.hasNextLine())
		{
			String ln = sc.nextLine();
			if(ln == null || ln.equals("") || ln.startsWith("#"))
			{
			} else
			{
				String[] split = ln.split(" ");
				switch(split[0])
				{
					case "v":
						model.getVertices().add(new Vector3f(
								Float.parseFloat(split[1]) * scale,
								Float.parseFloat(split[2]) * scale,
								Float.parseFloat(split[3]) * scale
						));
						break;
					case "vn":
						model.getNormals().add(new Vector3f(
								Float.parseFloat(split[1]),
								Float.parseFloat(split[2]),
								Float.parseFloat(split[3])
						));
						break;
					case "vt":
						model.getTextureCoordinates().add(new Vector2f(
								Float.parseFloat(split[1]),
								Float.parseFloat(split[2])
						));
						break;
					case "f":
						model.getFaces().add(new Face(
								new int[]{
										Integer.parseInt(split[1].split("/")[0]),
										Integer.parseInt(split[2].split("/")[0]),
										Integer.parseInt(split[3].split("/")[0])
								},
								new int[]{
										Integer.parseInt(split[1].split("/")[1]),
										Integer.parseInt(split[2].split("/")[1]),
										Integer.parseInt(split[3].split("/")[1])
								},
								new int[]{
										Integer.parseInt(split[1].split("/")[2]),
										Integer.parseInt(split[2].split("/")[2]),
										Integer.parseInt(split[3].split("/")[2])
								}
						));
						break;
					case "s":
						model.setSmoothShadingEnabled(!ln.contains("off"));
						break;
					default:
						break;
				}
			}
		}
		sc.close();
		return model;
	}

	public static class WavefrontMeshProvider
			implements ISimpleRenderable
	{
		final Supplier<WavefrontMesh> meshSupplier;

		public WavefrontMeshProvider(Supplier<WavefrontMesh> meshSupplier)
		{
			this.meshSupplier = meshSupplier;
		}

		public WavefrontMesh getMesh()
		{
			return meshSupplier.get();
		}

		@Override
		public void render(@Nullable Map<String, Object> properties)
		{
			WavefrontMesh m = getMesh();
			if(m != null)
				m.render(properties);
		}
	}

	public static class WavefrontMesh
			implements ISimpleRenderable
	{
		private final UUID UNIQUE_ID = UUID.randomUUID();
		private final List<Vector3f> vertices;
		private final List<Vector2f> textureCoords;
		private final List<Vector3f> normals;
		private final List<Face> faces;
		private boolean enableSmoothShading;

		public WavefrontMesh()
		{
			this(new ArrayList<>(), new ArrayList<>(), new ArrayList<>(), new ArrayList<Face>(), true);
		}

		public WavefrontMesh(List<Vector3f> vertices, List<Vector2f> textureCoords, List<Vector3f> normals, List<Face> faces, boolean enableSmoothShading)
		{
			this.vertices = vertices;
			this.textureCoords = textureCoords;
			this.normals = normals;
			this.faces = faces;
			this.enableSmoothShading = enableSmoothShading;
		}

		public UUID getUniqueId()
		{
			return UNIQUE_ID;
		}

		public void enableStates()
		{
			if(this.isSmoothShadingEnabled()) GL11.glShadeModel(GL_SMOOTH);
			else GL11.glShadeModel(GL_FLAT);
		}

		public boolean hasTextureCoordinates()
		{
			return this.getTextureCoordinates().size() > 0;
		}

		public boolean hasNormals()
		{
			return this.getNormals().size() > 0;
		}

		public List<Vector3f> getVertices()
		{
			return this.vertices;
		}

		public List<Vector2f> getTextureCoordinates()
		{
			return this.textureCoords;
		}

		public List<Vector3f> getNormals()
		{
			return this.normals;
		}

		public List<Face> getFaces()
		{
			return this.faces;
		}

		public boolean isSmoothShadingEnabled()
		{
			return this.enableSmoothShading;
		}

		public void setSmoothShadingEnabled(boolean isSmoothShadingEnabled)
		{
			this.enableSmoothShading = isSmoothShadingEnabled;
		}

		private Function<Vector2f, Vector2f> uvTransform;

		public WavefrontMesh withUVTransformation(Function<Vector2f, Vector2f> uvTransform)
		{
			if(uvTransform == null)
				return this;
			if(this.uvTransform != null) this.uvTransform = this.uvTransform.andThen(uvTransform);
			else this.uvTransform = uvTransform;
			return this;
		}

		public void renderVertices(boolean flipUV)
		{
			Tessellator tess = Tessellator.getInstance();
			BufferBuilder builder = tess.getBuffer();
			builder.begin(GL_TRIANGLES, DefaultVertexFormats.POSITION_TEX_NORMAL);
			pipeVertices(builder, flipUV);
			tess.draw();
		}

		public void renderVerticesGL(boolean flipUV)
		{
			GL11.glMaterialf(GL_FRONT, GL_SHININESS, 120);
			GL11.glBegin(GL_TRIANGLES);
			pipeVertices(flipUV ? VC_OPENGL_FLIP_UV : VC_OPENGL);
			GL11.glEnd();
		}

		public void pipeVertices(BufferBuilder builder, boolean flipUV)
		{
			pipeVertices((pos, tex, normal) -> builder.pos(pos.getX(), pos.getY(), pos.getZ()).tex(tex.getX(), flipUV ? 1 - tex.getY() : tex.getY()).normal(normal.getX(), normal.getY(), normal.getZ()));
		}

		public void pipeVertices(IVertexConsumer consumer)
		{
			Function<Vector2f, Vector2f> uvt = uvTransform != null ? uvTransform : v -> v;
			uvTransform = null;

			for(Face face : getFaces())
			{
				consumer.consumeVertex(getVertices().get(face.getVertices()[0] - 1), uvt.apply(getTextureCoordinates().get(face.getTextureCoords()[0] - 1)), getNormals().get(face.getNormals()[0] - 1));
				consumer.consumeVertex(getVertices().get(face.getVertices()[1] - 1), uvt.apply(getTextureCoordinates().get(face.getTextureCoords()[1] - 1)), getNormals().get(face.getNormals()[1] - 1));
				consumer.consumeVertex(getVertices().get(face.getVertices()[2] - 1), uvt.apply(getTextureCoordinates().get(face.getTextureCoords()[2] - 1)), getNormals().get(face.getNormals()[2] - 1));
			}
		}

		@Override
		public void render(@Nullable Map<String, Object> properties)
		{
			if(properties != null && properties.containsKey("uvTransformer"))
				withUVTransformation(Cast.cast(properties.get("uvTransformer"), Function.class));
			Boolean bool = properties == null ? true : Cast.cast(properties.getOrDefault("flipUV", true), Boolean.class);
			renderVerticesGL(bool != null ? bool.booleanValue() : true);
		}
	}

	public static class Face
	{
		private final int[] vertexIndices;
		private final int[] normalIndices;
		private final int[] textureCoordinateIndices;

		public Face(int[] vertexIndices, int[] textureCoordinateIndices, int[] normalIndices)
		{
			this.vertexIndices = vertexIndices;
			this.normalIndices = normalIndices;
			this.textureCoordinateIndices = textureCoordinateIndices;
		}

		public boolean hasNormals()
		{
			return this.normalIndices != null;
		}

		public boolean hasTextureCoords()
		{
			return this.textureCoordinateIndices != null;
		}

		public int[] getVertices()
		{
			return this.vertexIndices;
		}

		public int[] getTextureCoords()
		{
			return this.textureCoordinateIndices;
		}

		public int[] getNormals()
		{
			return this.normalIndices;
		}

		@Override
		public String toString()
		{
			return String.format("Face[vertexIndices:%s,normalIndices:%s,textureCoordinateIndices:%s]", Arrays.toString(vertexIndices), Arrays.toString(normalIndices), Arrays.toString(textureCoordinateIndices));
		}
	}

	public interface IVertexConsumer
	{
		void consumeVertex(Vector3f pos, Vector2f tex, Vector3f normal);
	}
}