package com.zeitheron.hammercore.client.utils.gl.shading;

import com.zeitheron.hammercore.HammerCore;
import com.zeitheron.hammercore.api.events.ResourceManagerReloadEvent;
import com.zeitheron.hammercore.client.render.shader.GlShaderStack;
import com.zeitheron.hammercore.client.utils.gl.GLBuffer;
import com.zeitheron.hammercore.utils.OnetimeCaller;
import com.zeitheron.hammercore.utils.base.EvtBus;
import it.unimi.dsi.fastutil.ints.*;
import it.unimi.dsi.fastutil.objects.Object2IntArrayMap;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.toasts.*;
import net.minecraft.client.renderer.*;
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
	private final Int2ObjectArrayMap<ShaderSource> sourcesStatic = new Int2ObjectArrayMap<>();
	private final List<Consumer<IShaderLinker>> linkers = new ArrayList<>();
	private final Int2ObjectArrayMap<ShaderSource> sources = new Int2ObjectArrayMap<>();
	private final List<ShaderVar> variables = new ArrayList<>();
	private final Object2IntArrayMap<String> uniformCache = new Object2IntArrayMap<>();
	private final List<Consumer<VariableShaderProgram>> onBind = new ArrayList<>();
	private final List<Consumer<VariableShaderProgram>> onCompilationFailed = new ArrayList<>();
	private Integer program;
	private ResourceLocation id;
	private boolean doGLLog = true, hasCompiled, compilationFailed;
	private final List<Throwable> compilationErrors = new ArrayList<>();

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

	public VariableShaderProgram subscribe4Events()
	{
		if(!PROGRAMS.contains(this)) PROGRAMS.add(this);
		return this;
	}

	public VariableShaderProgram addVariable(ShaderVar var)
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

	protected void createProgram()
	{
		hasCompiled = false;
		compilationFailed = false;
		compilationErrors.clear();

		try
		{
			if(program != null) OpenGlHelper.glDeleteProgram(program);
			uniformCache.clear();
			program = OpenGlHelper.glCreateProgram();
			IntList shaders = new IntArrayList();

			sources.clear();
			sources.putAll(sourcesStatic);
			for(Consumer<IShaderLinker> linker : linkers)
				linker.accept(sources::put);

			for(int key : sources.keySet())
			{
				int shader = OpenGlHelper.glCreateShader(key);
				if(shader == 0) continue;
				byte[] abyte = sources.get(key).read(variables).getBytes(StandardCharsets.UTF_8);
				ByteBuffer bytebuffer = BufferUtils.createByteBuffer(abyte.length);
				bytebuffer.put(abyte);
				bytebuffer.position(0);
				OpenGlHelper.glShaderSource(shader, bytebuffer);
				OpenGlHelper.glCompileShader(shader);
				String gl = OpenGlHelper.glGetShaderInfoLog(shader, 32768);
				if(OpenGlHelper.glGetShaderi(shader, OpenGlHelper.GL_COMPILE_STATUS) == GL11.GL_FALSE)
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
			OpenGlHelper.glLinkProgram(program);
			String s = OpenGlHelper.glGetProgramInfoLog(program, 32768);
			if(!s.isEmpty() && doGLLog) System.out.println("GL LOG: " + s.trim());
			for(int i : shaders) OpenGlHelper.glDeleteShader(i);
			hasCompiled = true;
			compilationFailed = false;
			collectUniforms();
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

	public final List<String> uniformNames = new ArrayList<>();

	public void collectUniforms()
	{
		uniformNames.clear();
		if(program == null) return;
		int ufs = OpenGlHelper.glGetProgrami(program, GL20.GL_ACTIVE_UNIFORMS);
		for(int i = 0; i < ufs; ++i)
			uniformNames.add(GL20.glGetActiveUniform(program, i, 128));
	}

	public Integer getProgramId()
	{
		return program;
	}

	public void update()
	{
		if(program != null && variables.stream().peek(ShaderVar::update).anyMatch(v -> v.hasChanged))
		{
			createProgram();
			variables.forEach(v -> v.hasChanged = false);
		}
	}

	public void onReload()
	{
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
			if(loc == -1)
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
		OpenGlHelper.glUniform1i(getUniformLocation(uniform), value);
	}

	public void setUniform(String uniform, boolean value)
	{
		if(!hasCompiled) return;
		OpenGlHelper.glUniform1i(getUniformLocation(uniform), value ? 1 : 0);
	}

	public void setUniform(String uniform, float value)
	{
		if(!hasCompiled) return;
		GL20.glUniform1f(getUniformLocation(uniform), value);
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

	private static final OnetimeCaller initShaders = OnetimeCaller.of(() -> EvtBus.post(MinecraftForge.EVENT_BUS, new InitializeShadersEvent()));

	@SubscribeEvent
	public static void reloadShaders(ResourceManagerReloadEvent e)
	{
		if(hasInitialized && e.isType(VanillaResourceType.SHADERS))
			reload();
	}

	public static void reload()
	{
		Minecraft.getMinecraft().addScheduledTask(() ->
		{
			initShaders.call();
			HammerCore.LOG.info("Reloading {} variable shader programs.", PROGRAMS.size());
			PROGRAMS.forEach(VariableShaderProgram::onReload);
		});
	}

	@SubscribeEvent
	public static void tickShader(TickEvent.ClientTickEvent e)
	{
		if(e.phase == TickEvent.Phase.START) PROGRAMS.forEach(VariableShaderProgram::update);
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