package com.zeitheron.hammercore.client.utils.gl.shading;

import com.zeitheron.hammercore.HammerCore;
import com.zeitheron.hammercore.api.events.ResourceManagerReloadEvent;
import com.zeitheron.hammercore.client.render.shader.GlShaderStack;
import com.zeitheron.hammercore.client.utils.gl.GLBuffer;
import com.zeitheron.hammercore.utils.base.EvtBus;
import com.zeitheron.hammercore.utils.java.Once;
import it.unimi.dsi.fastutil.ints.*;
import it.unimi.dsi.fastutil.objects.*;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.toasts.*;
import net.minecraft.client.renderer.*;
import net.minecraft.client.resources.IResourceManager;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.text.*;
import net.minecraftforge.client.resource.VanillaResourceType;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent;
import net.minecraftforge.fml.relauncher.*;
import org.lwjgl.BufferUtils;
import org.lwjgl.opengl.*;

import javax.annotation.Nullable;
import java.nio.ByteBuffer;
import java.nio.charset.StandardCharsets;
import java.util.*;
import java.util.function.Consumer;

/**
 * Represents a constant type for shader program to be used in any desired way.
 * You may add variables to it to make constants a variable in glsl.
 * This program reloads alongside minecraft shader reload event, too.
 */
@SideOnly(Side.CLIENT)
@Mod.EventBusSubscriber(Side.CLIENT)
public class VariableShaderProgram
{
	public static boolean hasInitialized;
	private static final List<VariableShaderProgram> PROGRAMS = new ArrayList<>();
	private static final Map<ResourceLocation, VariableShaderProgram> PROGRAM_REGISTRY = new HashMap<>();
	
	protected final Int2ObjectMap<ShaderSource> sourcesStatic = new Int2ObjectOpenHashMap<>();
	protected final List<Consumer<IShaderLinker>> linkers = new ArrayList<>();
	protected final Int2ObjectMap<ShaderSource> sources = new Int2ObjectOpenHashMap<>();
	protected final List<ShaderVar<?>> variables = new ArrayList<>();
	protected final Object2IntMap<String> uniformCache = new Object2IntOpenHashMap<>();
	protected final List<Consumer<VariableShaderProgram>> onBind = new ArrayList<>();
	protected final List<Consumer<VariableShaderProgram>> onCompilationFailed = new ArrayList<>();
	protected Integer program;
	protected ResourceLocation id;
	protected boolean doGLLog = true, hasCompiled, compilationFailed;
	protected final List<Throwable> compilationErrors = new ArrayList<>();
	
	protected boolean silentMissingUniforms;
	
	public final List<String> uniformNames = new ArrayList<>();
	
	protected final Int2FloatMap uniformValues1 = new Int2FloatOpenHashMap();
	
	{
		uniformValues1.defaultReturnValue(Float.NaN);
	}
	
	public VariableShaderProgram id(ResourceLocation id)
	{
		if(this.id == null)
		{
			if(PROGRAM_REGISTRY.containsKey(id)) throw new RuntimeException("Duplicate shader pipeline id: " + id);
			this.id = id;
			PROGRAM_REGISTRY.put(id, this);
		} else
			throw new RuntimeException("ID already assigned to shader pipe: " + this.id + " (tried to override to " + id + ")");
		return this;
	}
	
	/**
	 * Note: if any errors occur, logging will still happen!
	 */
	public VariableShaderProgram doGLLog(boolean flag)
	{
		this.doGLLog = flag;
		return this;
	}
	
	public VariableShaderProgram silentMissingUniforms(boolean silentMissingUniforms)
	{
		this.silentMissingUniforms = silentMissingUniforms;
		return this;
	}
	
	public VariableShaderProgram subscribe4Events()
	{
		if(!PROGRAMS.contains(this)) PROGRAMS.add(this);
		return this;
	}
	
	public VariableShaderProgram addVariable(ShaderVar<?> var)
	{
		var.setProgram(this);
		variables.add(var);
		return this;
	}
	
	public VariableShaderProgram onBind(Consumer<VariableShaderProgram> onBind)
	{
		this.onBind.add(onBind);
		return this;
	}
	
	public VariableShaderProgram dynamicLinker(Consumer<IShaderLinker> linker)
	{
		linkers.add(linker);
		return this;
	}
	
	public VariableShaderProgram linkGeometrySource(ShaderSource src)
	{
		return linkSource(GL32.GL_GEOMETRY_SHADER, src);
	}
	
	public VariableShaderProgram linkVertexSource(ShaderSource src)
	{
		return linkSource(OpenGlHelper.GL_VERTEX_SHADER, src);
	}
	
	public VariableShaderProgram linkFragmentSource(ShaderSource src)
	{
		return linkSource(OpenGlHelper.GL_FRAGMENT_SHADER, src);
	}
	
	public VariableShaderProgram linkSource(int type, ShaderSource src)
	{
		sourcesStatic.put(type, src);
		return this;
	}
	
	public VariableShaderProgram onCompilationFailed(Consumer<VariableShaderProgram> errorHandler)
	{
		onCompilationFailed.add(errorHandler);
		return this;
	}
	
	public void clearCache()
	{
		uniformValues1.clear();
		uniformCache.clear();
	}
	
	protected void createProgram()
	{
		hasCompiled = false;
		compilationFailed = false;
		compilationErrors.clear();
		
		try
		{
			if(program != null) OpenGlHelper.glDeleteProgram(program);
			clearCache();
			
			int program = OpenGlHelper.glCreateProgram();
			if(program == 0) throw new RuntimeException("glCreateProgram returned 0");
			this.program = program;
			IntList shaders = new IntArrayList();
			
			sources.clear();
			sources.putAll(sourcesStatic);
			for(Consumer<IShaderLinker> linker : linkers)
				linker.accept(sources::put);
			
			for(int key : sources.keySet())
			{
				int shader = OpenGlHelper.glCreateShader(key);
				if(shader == 0) throw new RuntimeException("glCreateShader returned 0 for type #" + Integer.toHexString(key));
				
				byte[] abyte = sources.get(key).read(variables).getBytes(StandardCharsets.UTF_8);
				ByteBuffer bytebuffer = BufferUtils.createByteBuffer(abyte.length);
				bytebuffer.put(abyte);
				bytebuffer.position(0);
				OpenGlHelper.glShaderSource(shader, bytebuffer);
				
				OpenGlHelper.glCompileShader(shader);
				int compileStatus = OpenGlHelper.glGetShaderi(shader, OpenGlHelper.GL_COMPILE_STATUS);
				
				int len = OpenGlHelper.glGetShaderi(shader, GL20.GL_INFO_LOG_LENGTH);
				String gl = len > 1 ? OpenGlHelper.glGetShaderInfoLog(shader, len) : "";
				
				if(compileStatus == GL11.GL_FALSE)
				{
					RuntimeException err = new RuntimeException("Failed to load shader(#" + Integer.toHexString(key) + ") source " + sources.get(key) + ":\n" + gl);
					compilationErrors.add(err);
					OpenGlHelper.glDeleteShader(shader);
					continue;
				} else if(!gl.isEmpty())
				{
					HammerCore.LOG.warn("GL log: for shader(#{}) source {}: {}", Integer.toHexString(key), sources.get(key), gl);
				}
				
				OpenGlHelper.glAttachShader(program, shader);
				shaders.add(shader);
			}
			
			HammerCore.LOG.info("Linking shader program {}", id);
			
			OpenGlHelper.glLinkProgram(program);
			int err = GL11.glGetError();
			if(err != GL11.GL_FALSE) HammerCore.LOG.info("GL error #{}", Integer.toHexString(err));
			GL11.glFinish();
			
			int len = OpenGlHelper.glGetProgrami(program, GL20.GL_INFO_LOG_LENGTH);
			String gl = len > 1 ? OpenGlHelper.glGetProgramInfoLog(program, len) : "";
			HammerCore.LOG.info("GL link log: {}", gl);
			int linkStatus = OpenGlHelper.glGetProgrami(program, OpenGlHelper.GL_LINK_STATUS);
			
			for(int i : shaders) OpenGlHelper.glDeleteShader(i);
			
			if(linkStatus == GL11.GL_FALSE)
				throw new RuntimeException("Failed to link shader(" + id + "):\n" + gl);
			
			hasCompiled = true;
			variables.forEach(v -> v.hasChanged = false); // Mark this as not changed
			compilationFailed = false;
			collectUniforms();
			
			HammerCore.LOG.info("Shader program {} has been linked.", id);
		} catch(Throwable err)
		{
			compilationErrors.add(err);
		}
		
		if(!compilationErrors.isEmpty())
		{
			if(program != null) OpenGlHelper.glDeleteProgram(program);
			program = null;
			hasCompiled = false;
			compilationFailed = true;
			compilationErrors.forEach(err -> HammerCore.LOG.error("Shader {} error:", getId(), err));
			onCompilationFailed.forEach(c -> c.accept(VariableShaderProgram.this));
		}
	}
	
	public void collectUniforms()
	{
		uniformNames.clear();
		if(program == null) return;
		int ufs = OpenGlHelper.glGetProgrami(program, GL20.GL_ACTIVE_UNIFORMS);
		for(int i = 0; i < ufs; ++i)
		{
			String name = GL20.glGetActiveUniform(program, i, 128);
			uniformCache.put(name, OpenGlHelper.glGetUniformLocation(program, name));
			uniformNames.add(name);
		}
	}
	
	public Integer getProgramId()
	{
		return program;
	}
	
	public void update()
	{
		if(program == null) return;
		
		boolean anyChanged = false;
		for(ShaderVar<?> v : variables)
		{
			v.update();
			if(v.hasChanged) anyChanged = true;
		}
		
		if(anyChanged)
			createProgram();
	}
	
	public void prepareReload(IResourceManager resources)
	{
		for(ShaderVar<?> v : variables)
			v.prepareReload(resources);
	}
	
	public void onReload(IResourceManager resources)
	{
		for(ShaderVar<?> v : variables) v.onReload(resources);
		createProgram();
	}
	
	/*
	 * Status resolvers.
	 * Useful for disabling mod features if the shader wasn't able to compile properly.
	 */
	
	public boolean hasCompiled()
	{
		return hasCompiled;
	}
	
	public boolean hasCompilationFailed()
	{
		return compilationFailed;
	}
	
	public List<Throwable> getCompilationErrors()
	{
		return compilationErrors;
	}
	
	/* End status resolvers */
	
	public final ResourceLocation getId()
	{
		return id;
	}
	
	public int getUniformLocation(String location)
	{
		if(program == null) return 0;
		if(!uniformCache.containsKey(location))
		{
			int loc = OpenGlHelper.glGetUniformLocation(program, location);
			if(loc == -1 && !silentMissingUniforms)
			{
				HammerCore.LOG.info("Attempted to access unknown uniform location {} in shader {}! This is not going to end well!", location, id);
				Thread.dumpStack();
			}
			uniformCache.put(location, loc);
		}
		return uniformCache.getInt(location);
	}
	
	public void setUniform(String uniform, int value)
	{
		if(!hasCompiled) return;
		final int loc = getUniformLocation(uniform);
		if(uniformValues1.put(loc, value) == (float) value) return;
		OpenGlHelper.glUniform1i(loc, value);
	}
	
	public void setUniform(String uniform, boolean value)
	{
		if(!hasCompiled) return;
		setUniform(uniform, value ? 1 : 0);
	}
	
	public void setUniform(String uniform, float value)
	{
		if(!hasCompiled) return;
		final int loc = getUniformLocation(uniform);
		if(uniformValues1.put(loc, value) == value) return;
		GL20.glUniform1f(loc, value);
	}
	
	public void setUniform(String uniform, int v1, int v2)
	{
		if(!hasCompiled) return;
		GL20.glUniform2i(getUniformLocation(uniform), v1, v2);
	}
	
	public void setUniform(String uniform, int v1, int v2, int v3)
	{
		if(!hasCompiled) return;
		GL20.glUniform3i(getUniformLocation(uniform), v1, v2, v3);
	}
	
	public void setUniform(String uniform, float v1, float v2)
	{
		if(!hasCompiled) return;
		GL20.glUniform2f(getUniformLocation(uniform), v1, v2);
	}
	
	public void setUniform(String uniform, float v1, float v2, float v3)
	{
		if(!hasCompiled) return;
		GL20.glUniform3f(getUniformLocation(uniform), v1, v2, v3);
	}
	
	public void setUniform(String uniform, float v1, float v2, float v3, float v4)
	{
		if(!hasCompiled) return;
		GL20.glUniform4f(getUniformLocation(uniform), v1, v2, v3, v4);
	}
	
	public void setBuffer(String blockName, GLBuffer buffer)
	{
		if(!hasCompiled) return;
		buffer.bindToShader(program, 0, blockName);
	}
	
	public void bindShader()
	{
		if(compilationFailed) return;
		if(program == null) createProgram();
		if(!hasCompiled) return;
		OpenGlHelper.glUseProgram(program);
		if(!onBind.isEmpty()) onBind.forEach(c -> c.accept(this));
	}
	
	public void unbindShader()
	{
		OpenGlHelper.glUseProgram(0);
	}
	
	private static final Once initShaders = Once.run(() -> EvtBus.post(MinecraftForge.EVENT_BUS, new InitializeShadersEvent()));
	
	public static void reload()
	{
		reload(Minecraft.getMinecraft().getResourceManager());
	}
	
	public static void reload(IResourceManager resources)
	{
		Minecraft.getMinecraft().addScheduledTask(() ->
		{
			initShaders.call();
			HammerCore.LOG.info("Reloading {} variable shader programs.", PROGRAMS.size());
			for(VariableShaderProgram p : PROGRAMS) p.prepareReload(resources);
			for(VariableShaderProgram p : PROGRAMS) p.onReload(resources);
		});
	}
	
	@SubscribeEvent
	public static void reloadShaders(ResourceManagerReloadEvent e)
	{
		if(hasInitialized && e.isType(VanillaResourceType.SHADERS))
			reload(e.getManager());
	}
	
	@SubscribeEvent
	public static void tickShader(TickEvent.ClientTickEvent e)
	{
		if(e.phase != TickEvent.Phase.START) return;
		for(VariableShaderProgram p : PROGRAMS) p.update();
	}
	
	public static VariableShaderProgram byId(ResourceLocation id)
	{
		return PROGRAM_REGISTRY.get(id);
	}
	
	public boolean isActive()
	{
		return hasCompiled && program != null && program.equals(GlShaderStack.glsActiveProgram());
	}
	
	public interface IShaderLinker
	{
		void link(int type, ShaderSource source);
	}
	
	public enum ToastCompilationErrorHandler
			implements Consumer<VariableShaderProgram>
	{
		INSTANCE;
		
		@Override
		public void accept(VariableShaderProgram program)
		{
			if(program.hasCompilationFailed())
			{
				int errors = program.getCompilationErrors().size();
				ShaderErrorToast toast = new ShaderErrorToast(new TextComponentString(errors + " Shader Error" + (errors > 1 ? "s" : "")), new TextComponentString(program.getId() + " failed. :<"));
				Minecraft.getMinecraft().getToastGui().add(toast);
			}
		}
	}
	
	@SideOnly(Side.CLIENT)
	public static class ShaderErrorToast
			implements IToast
	{
		private String title;
		private String subtitle;
		private long firstDrawTime;
		private boolean newDisplay;
		
		public ShaderErrorToast(ITextComponent titleComponent, @Nullable ITextComponent subtitleComponent)
		{
			this.title = titleComponent.getUnformattedText();
			this.subtitle = subtitleComponent == null ? null : subtitleComponent.getUnformattedText();
		}
		
		@Override
		public IToast.Visibility draw(GuiToast toastGui, long delta)
		{
			if(this.newDisplay)
			{
				this.firstDrawTime = delta;
				this.newDisplay = false;
			}
			
			toastGui.getMinecraft().getTextureManager().bindTexture(TEXTURE_TOASTS);
			GlStateManager.color(1.0F, 1.0F, 1.0F);
			toastGui.drawTexturedModalRect(0, 0, 0, 64, 160, 32);
			
			if(this.subtitle == null)
			{
				toastGui.getMinecraft().fontRenderer.drawString(this.title, 18, 12, -256);
			} else
			{
				toastGui.getMinecraft().fontRenderer.drawString(this.title, 18, 7, -256);
				toastGui.getMinecraft().fontRenderer.drawString(this.subtitle, 18, 18, -1);
			}
			
			return delta - this.firstDrawTime < 5000L ? IToast.Visibility.SHOW : IToast.Visibility.HIDE;
		}
		
		public void setDisplayedText(ITextComponent titleComponent, @Nullable ITextComponent subtitleComponent)
		{
			this.title = titleComponent.getUnformattedText();
			this.subtitle = subtitleComponent == null ? null : subtitleComponent.getUnformattedText();
			this.newDisplay = true;
		}
	}
}