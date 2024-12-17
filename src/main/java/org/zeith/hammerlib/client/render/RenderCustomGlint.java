package org.zeith.hammerlib.client.render;

import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.*;
import net.minecraft.client.renderer.*;
import net.minecraft.client.renderer.RenderStateShard.*;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.TriState;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.client.event.RegisterRenderBuffersEvent;
import net.neoforged.neoforge.client.event.RegisterShadersEvent;
import org.zeith.hammerlib.HammerLib;
import org.zeith.hammerlib.proxy.HLConstants;

import java.util.function.BiFunction;
import java.util.function.Function;

import static net.minecraft.client.renderer.RenderStateShard.*;

public class RenderCustomGlint
{
	public static final ResourceLocation ENCHANTED_GLINT_ENTITY = HLConstants.id("textures/misc/enchanted_glint_entity.png");
	public static final ResourceLocation ENCHANTED_GLINT_ITEM = HLConstants.id("textures/misc/enchanted_glint_item.png");
	
	protected static final ShaderStateShard RENDERTYPE_ARMOR_GLINT_SHADER = new ShaderStateShard(GlintShaders.ARMOR_GLINT_SHADER);
	protected static final ShaderStateShard RENDERTYPE_ARMOR_ENTITY_GLINT_SHADER = new ShaderStateShard(GlintShaders.ARMOR_ENTITY_GLINT_SHADER);
	protected static final ShaderStateShard RENDERTYPE_GLINT_TRANSLUCENT_SHADER = new ShaderStateShard(GlintShaders.GLINT_TRANSLUCENT_SHADER);
	protected static final ShaderStateShard RENDERTYPE_GLINT_SHADER = new ShaderStateShard(GlintShaders.GLINT_SHADER);
	protected static final ShaderStateShard RENDERTYPE_GLINT_DIRECT_SHADER = new ShaderStateShard(GlintShaders.GLINT_DIRECT_SHADER);
	protected static final ShaderStateShard RENDERTYPE_ENTITY_GLINT_SHADER = new ShaderStateShard(GlintShaders.ENTITY_GLINT_SHADER);
	protected static final ShaderStateShard RENDERTYPE_ENTITY_GLINT_DIRECT_SHADER = new ShaderStateShard(GlintShaders.ENTITY_GLINT_DIRECT_SHADER);
	
	private static final RenderType ARMOR_GLINT = RenderType.create(HLConstants.MOD_ID + ":armor_glint", DefaultVertexFormat.POSITION_TEX_COLOR, VertexFormat.Mode.QUADS, 256, false, false, RenderType.CompositeState.builder()
			.setShaderState(RENDERTYPE_ARMOR_GLINT_SHADER)
			.setTextureState(new RenderStateShard.TextureStateShard(ENCHANTED_GLINT_ENTITY, TriState.TRUE, false))
			.setWriteMaskState(COLOR_WRITE)
			.setCullState(NO_CULL)
			.setDepthTestState(EQUAL_DEPTH_TEST)
			.setTransparencyState(GLINT_TRANSPARENCY)
			.setTexturingState(GLINT_TEXTURING)
			.setLayeringState(VIEW_OFFSET_Z_LAYERING)
			.createCompositeState(false)
	);
	
	private static final RenderType ARMOR_ENTITY_GLINT = RenderType.create(HLConstants.MOD_ID + ":armor_entity_glint", DefaultVertexFormat.POSITION_TEX_COLOR, VertexFormat.Mode.QUADS, 256, false, false, RenderType.CompositeState.builder()
			.setShaderState(RENDERTYPE_ARMOR_ENTITY_GLINT_SHADER)
			.setTextureState(new RenderStateShard.TextureStateShard(ENCHANTED_GLINT_ENTITY, TriState.TRUE, false))
			.setWriteMaskState(COLOR_WRITE)
			.setCullState(NO_CULL)
			.setDepthTestState(EQUAL_DEPTH_TEST)
			.setTransparencyState(GLINT_TRANSPARENCY)
			.setTexturingState(ENTITY_GLINT_TEXTURING)
			.setLayeringState(VIEW_OFFSET_Z_LAYERING)
			.createCompositeState(false)
	);
	
	private static final RenderType GLINT_TRANSLUCENT = RenderType.create(HLConstants.MOD_ID + ":glint_translucent", DefaultVertexFormat.POSITION_TEX_COLOR, VertexFormat.Mode.QUADS, 256, false, false, RenderType.CompositeState.builder()
			.setShaderState(RENDERTYPE_GLINT_TRANSLUCENT_SHADER)
			.setTextureState(new RenderStateShard.TextureStateShard(ENCHANTED_GLINT_ITEM, TriState.TRUE, false))
			.setWriteMaskState(COLOR_WRITE)
			.setCullState(NO_CULL)
			.setDepthTestState(EQUAL_DEPTH_TEST)
			.setTransparencyState(GLINT_TRANSPARENCY)
			.setTexturingState(GLINT_TEXTURING)
			.setOutputState(ITEM_ENTITY_TARGET)
			.createCompositeState(false)
	);
	
	private static final RenderType GLINT = RenderType.create(HLConstants.MOD_ID + ":glint", DefaultVertexFormat.POSITION_TEX_COLOR, VertexFormat.Mode.QUADS, 256, false, false, RenderType.CompositeState.builder()
			.setShaderState(RENDERTYPE_GLINT_SHADER)
			.setTextureState(new RenderStateShard.TextureStateShard(ENCHANTED_GLINT_ITEM, TriState.TRUE, false))
			.setWriteMaskState(COLOR_WRITE)
			.setCullState(NO_CULL)
			.setDepthTestState(EQUAL_DEPTH_TEST)
			.setTransparencyState(GLINT_TRANSPARENCY)
			.setTexturingState(GLINT_TEXTURING)
			.createCompositeState(false)
	);
	
	private static final RenderType GLINT_DIRECT = RenderType.create(HLConstants.MOD_ID + ":glint_direct", DefaultVertexFormat.POSITION_TEX_COLOR, VertexFormat.Mode.QUADS, 256, false, false, RenderType.CompositeState.builder()
			.setShaderState(RENDERTYPE_GLINT_DIRECT_SHADER)
			.setTextureState(new RenderStateShard.TextureStateShard(ENCHANTED_GLINT_ITEM, TriState.TRUE, false))
			.setWriteMaskState(COLOR_WRITE)
			.setCullState(NO_CULL)
			.setDepthTestState(EQUAL_DEPTH_TEST)
			.setTransparencyState(GLINT_TRANSPARENCY)
			.setTexturingState(GLINT_TEXTURING)
			.createCompositeState(false)
	);
	
	private static final RenderType ENTITY_GLINT = RenderType.create(HLConstants.MOD_ID + ":entity_glint", DefaultVertexFormat.POSITION_TEX_COLOR, VertexFormat.Mode.QUADS, 256, false, false, RenderType.CompositeState.builder()
			.setShaderState(RENDERTYPE_ENTITY_GLINT_SHADER)
			.setTextureState(new RenderStateShard.TextureStateShard(ENCHANTED_GLINT_ENTITY, TriState.TRUE, false))
			.setWriteMaskState(COLOR_WRITE)
			.setCullState(NO_CULL)
			.setDepthTestState(EQUAL_DEPTH_TEST)
			.setTransparencyState(GLINT_TRANSPARENCY)
			.setOutputState(ITEM_ENTITY_TARGET)
			.setTexturingState(ENTITY_GLINT_TEXTURING)
			.createCompositeState(false)
	);
	
	private static final RenderType ENTITY_GLINT_DIRECT = RenderType.create(HLConstants.MOD_ID + ":entity_glint_direct", DefaultVertexFormat.POSITION_TEX_COLOR, VertexFormat.Mode.QUADS, 256, false, false, RenderType.CompositeState.builder()
			.setShaderState(RENDERTYPE_ENTITY_GLINT_DIRECT_SHADER)
			.setTextureState(new RenderStateShard.TextureStateShard(ENCHANTED_GLINT_ENTITY, TriState.TRUE, false))
			.setWriteMaskState(COLOR_WRITE)
			.setCullState(NO_CULL)
			.setDepthTestState(EQUAL_DEPTH_TEST)
			.setTransparencyState(GLINT_TRANSPARENCY)
			.setTexturingState(ENTITY_GLINT_TEXTURING)
			.createCompositeState(false)
	);
	
	public static VertexConsumer glintBuffer(MultiBufferSource src, GlintType type, float r, float g, float b, float a)
	{
		return TintingVertexConsumer.wrap(src.getBuffer(type.type), r, g, b, a);
	}
	
	public static VertexConsumer glintBuffer(MultiBufferSource src, GlintType type, int rgba)
	{
		return TintingVertexConsumer.wrap(src.getBuffer(type.type), rgba);
	}
	
	
	public static RenderType armorGlint()
	{
		return ARMOR_GLINT;
	}
	
	public static RenderType armorEntityGlint()
	{
		return ARMOR_ENTITY_GLINT;
	}
	
	public static RenderType glintTranslucent()
	{
		return GLINT_TRANSLUCENT;
	}
	
	public static RenderType glint()
	{
		return GLINT;
	}
	
	public static RenderType glintDirect()
	{
		return GLINT_DIRECT;
	}
	
	public static RenderType entityGlint()
	{
		return ENTITY_GLINT;
	}
	
	public static RenderType entityGlintDirect()
	{
		return ENTITY_GLINT_DIRECT;
	}
	
	public enum GlintType
	{
		ARMOR_GLINT(RenderCustomGlint.ARMOR_GLINT),
		ARMOR_ENTITY_GLINT(RenderCustomGlint.ARMOR_ENTITY_GLINT),
		GLINT_TRANSLUCENT(RenderCustomGlint.GLINT_TRANSLUCENT),
		GLINT(RenderCustomGlint.GLINT),
		GLINT_DIRECT(RenderCustomGlint.GLINT_DIRECT),
		ENTITY_GLINT(RenderCustomGlint.ENTITY_GLINT),
		ENTITY_GLINT_DIRECT(RenderCustomGlint.ENTITY_GLINT_DIRECT);
		
		final RenderType type;
		
		GlintType(RenderType type)
		{
			this.type = type;
		}
	}
	
	public static MultiBufferSource glintTinting(MultiBufferSource src, int color)
	{
		return new ReplacingMultiBufferSource(src, GLINT_SWAPPER(color));
	}
	
	public static BiFunction<MultiBufferSource, RenderType, VertexConsumer> GLINT_SWAPPER(int color)
	{
		return (bufs, src) ->
		{
			if(src == RenderType.GLINT) return TintingVertexConsumer.wrap(bufs.getBuffer(GLINT), color);
			if(src == RenderType.GLINT_TRANSLUCENT) return TintingVertexConsumer.wrap(bufs.getBuffer(GLINT_TRANSLUCENT), color);
			if(src == RenderType.ARMOR_ENTITY_GLINT) return TintingVertexConsumer.wrap(bufs.getBuffer(ARMOR_ENTITY_GLINT), color);
			if(src == RenderType.ENTITY_GLINT) return TintingVertexConsumer.wrap(bufs.getBuffer(ENTITY_GLINT), color);
			return bufs.getBuffer(src);
		};
	}
	
	@EventBusSubscriber(bus = EventBusSubscriber.Bus.MOD, value = Dist.CLIENT)
	public static class GlintShaders
	{
		public static final ShaderProgram ARMOR_GLINT_SHADER = new ShaderProgram(
				HLConstants.id("core/rendertype_armor_glint"),
				DefaultVertexFormat.POSITION_TEX_COLOR,
				ShaderDefines.EMPTY
		);
		
		public static final ShaderProgram ARMOR_ENTITY_GLINT_SHADER = new ShaderProgram(
				HLConstants.id("core/rendertype_armor_entity_glint"),
				DefaultVertexFormat.POSITION_TEX_COLOR,
				ShaderDefines.EMPTY
		);
		
		public static final ShaderProgram GLINT_TRANSLUCENT_SHADER = new ShaderProgram(
				HLConstants.id("core/rendertype_glint_translucent"),
				DefaultVertexFormat.POSITION_TEX_COLOR,
				ShaderDefines.EMPTY
		);
		
		public static final ShaderProgram GLINT_SHADER = new ShaderProgram(
				HLConstants.id("core/rendertype_glint"),
				DefaultVertexFormat.POSITION_TEX_COLOR,
				ShaderDefines.EMPTY
		);
		
		public static final ShaderProgram GLINT_DIRECT_SHADER = new ShaderProgram(
				HLConstants.id("core/rendertype_glint_direct"),
				DefaultVertexFormat.POSITION_TEX_COLOR,
				ShaderDefines.EMPTY
		);
		
		public static final ShaderProgram ENTITY_GLINT_SHADER = new ShaderProgram(
				HLConstants.id("core/rendertype_entity_glint"),
				DefaultVertexFormat.POSITION_TEX_COLOR,
				ShaderDefines.EMPTY
		);
		
		public static final ShaderProgram ENTITY_GLINT_DIRECT_SHADER = new ShaderProgram(
				HLConstants.id("core/rendertype_entity_glint_direct"),
				DefaultVertexFormat.POSITION_TEX_COLOR,
				ShaderDefines.EMPTY
		);
		
		@SubscribeEvent
		public static void registerShaders(RegisterShadersEvent e)
		{
			e.registerShader(ARMOR_GLINT_SHADER);
			e.registerShader(ARMOR_ENTITY_GLINT_SHADER);
			e.registerShader(GLINT_TRANSLUCENT_SHADER);
			e.registerShader(GLINT_SHADER);
			e.registerShader(GLINT_DIRECT_SHADER);
			e.registerShader(ENTITY_GLINT_SHADER);
			e.registerShader(ENTITY_GLINT_DIRECT_SHADER);
			HammerLib.LOG.info("Reloaded glint shaders.");
		}
		
		@SubscribeEvent
		public static void registerBuffers(RegisterRenderBuffersEvent e)
		{
			e.registerRenderBuffer(RenderCustomGlint.armorGlint());
			e.registerRenderBuffer(RenderCustomGlint.armorEntityGlint());
			e.registerRenderBuffer(RenderCustomGlint.glint());
			e.registerRenderBuffer(RenderCustomGlint.glintDirect());
			e.registerRenderBuffer(RenderCustomGlint.glintTranslucent());
			e.registerRenderBuffer(RenderCustomGlint.entityGlint());
			e.registerRenderBuffer(RenderCustomGlint.entityGlintDirect());
		}
	}
}