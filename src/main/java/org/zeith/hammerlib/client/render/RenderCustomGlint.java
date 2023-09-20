package org.zeith.hammerlib.client.render;

import com.mojang.blaze3d.platform.GlStateManager;
import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.*;
import com.mojang.math.*;
import net.minecraft.Util;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.*;
import net.minecraft.client.renderer.RenderStateShard.ShaderStateShard;
import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.RegisterShadersEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import org.zeith.hammerlib.proxy.HLConstants;

import javax.annotation.Nullable;
import java.io.IOException;

public class RenderCustomGlint
{
	public static final ResourceLocation ENCHANTED_GLINT_ENTITY = HLConstants.id("textures/misc/enchanted_glint_entity.png");
	public static final ResourceLocation ENCHANTED_GLINT_ITEM = HLConstants.id("textures/misc/enchanted_glint_item.png");
	
	protected static final ShaderStateShard RENDERTYPE_ARMOR_GLINT_SHADER = new ShaderStateShard(GlintShaders::getArmorGlintShader);
	protected static final ShaderStateShard RENDERTYPE_ARMOR_ENTITY_GLINT_SHADER = new ShaderStateShard(GlintShaders::getArmorEntityGlintShader);
	protected static final ShaderStateShard RENDERTYPE_GLINT_TRANSLUCENT_SHADER = new ShaderStateShard(GlintShaders::getGlintTranslucentShader);
	protected static final ShaderStateShard RENDERTYPE_GLINT_SHADER = new ShaderStateShard(GlintShaders::getGlintShader);
	protected static final ShaderStateShard RENDERTYPE_GLINT_DIRECT_SHADER = new ShaderStateShard(GlintShaders::getGlintDirectShader);
	protected static final ShaderStateShard RENDERTYPE_ENTITY_GLINT_SHADER = new ShaderStateShard(GlintShaders::getEntityGlintShader);
	protected static final ShaderStateShard RENDERTYPE_ENTITY_GLINT_DIRECT_SHADER = new ShaderStateShard(GlintShaders::getEntityGlintDirectShader);
	
	protected static final RenderStateShard.WriteMaskStateShard COLOR_WRITE = new RenderStateShard.WriteMaskStateShard(true, false);
	protected static final RenderStateShard.CullStateShard NO_CULL = new RenderStateShard.CullStateShard(false);
	protected static final RenderStateShard.DepthTestStateShard EQUAL_DEPTH_TEST = new RenderStateShard.DepthTestStateShard("==", 514);
	
	protected static final RenderStateShard.TransparencyStateShard GLINT_TRANSPARENCY = new RenderStateShard.TransparencyStateShard("glint_transparency", () ->
	{
		RenderSystem.enableBlend();
		RenderSystem.blendFuncSeparate(GlStateManager.SourceFactor.SRC_COLOR, GlStateManager.DestFactor.ONE, GlStateManager.SourceFactor.ZERO, GlStateManager.DestFactor.ONE);
	}, () ->
	{
		RenderSystem.disableBlend();
		RenderSystem.defaultBlendFunc();
	});
	
	protected static final RenderStateShard.LayeringStateShard VIEW_OFFSET_Z_LAYERING = new RenderStateShard.LayeringStateShard("view_offset_z_layering", () ->
	{
		PoseStack posestack = RenderSystem.getModelViewStack();
		posestack.pushPose();
		posestack.scale(0.99975586F, 0.99975586F, 0.99975586F);
		RenderSystem.applyModelViewMatrix();
	}, () ->
	{
		PoseStack posestack = RenderSystem.getModelViewStack();
		posestack.popPose();
		RenderSystem.applyModelViewMatrix();
	});
	
	protected static final RenderStateShard.TexturingStateShard GLINT_TEXTURING = new RenderStateShard.TexturingStateShard("glint_texturing", () ->
	{
		setupGlintTexturing(8.0F);
	}, () ->
	{
		RenderSystem.resetTextureMatrix();
	});
	protected static final RenderStateShard.TexturingStateShard ENTITY_GLINT_TEXTURING = new RenderStateShard.TexturingStateShard("entity_glint_texturing", () ->
	{
		setupGlintTexturing(0.16F);
	}, () ->
	{
		RenderSystem.resetTextureMatrix();
	});
	
	protected static final RenderStateShard.OutputStateShard ITEM_ENTITY_TARGET = new RenderStateShard.OutputStateShard("item_entity_target", () ->
	{
		if(Minecraft.useShaderTransparency())
		{
			Minecraft.getInstance().levelRenderer.getItemEntityTarget().bindWrite(false);
		}
		
	}, () ->
	{
		if(Minecraft.useShaderTransparency())
		{
			Minecraft.getInstance().getMainRenderTarget().bindWrite(false);
		}
	});
	
	private static final RenderType ARMOR_GLINT = RenderType.create(HLConstants.MOD_ID +
			":armor_glint", DefaultVertexFormat.POSITION_COLOR_TEX, VertexFormat.Mode.QUADS, 256, false, false, RenderType.CompositeState.builder()
			.setShaderState(RENDERTYPE_ARMOR_GLINT_SHADER)
			.setTextureState(new RenderStateShard.TextureStateShard(ENCHANTED_GLINT_ENTITY, true, false))
			.setWriteMaskState(COLOR_WRITE)
			.setCullState(NO_CULL)
			.setDepthTestState(EQUAL_DEPTH_TEST)
			.setTransparencyState(GLINT_TRANSPARENCY)
			.setTexturingState(GLINT_TEXTURING)
			.setLayeringState(VIEW_OFFSET_Z_LAYERING)
			.createCompositeState(false)
	);
	
	private static final RenderType ARMOR_ENTITY_GLINT = RenderType.create(HLConstants.MOD_ID +
			":armor_entity_glint", DefaultVertexFormat.POSITION_COLOR_TEX, VertexFormat.Mode.QUADS, 256, false, false, RenderType.CompositeState.builder()
			.setShaderState(RENDERTYPE_ARMOR_ENTITY_GLINT_SHADER)
			.setTextureState(new RenderStateShard.TextureStateShard(ENCHANTED_GLINT_ENTITY, true, false))
			.setWriteMaskState(COLOR_WRITE)
			.setCullState(NO_CULL)
			.setDepthTestState(EQUAL_DEPTH_TEST)
			.setTransparencyState(GLINT_TRANSPARENCY)
			.setTexturingState(ENTITY_GLINT_TEXTURING)
			.setLayeringState(VIEW_OFFSET_Z_LAYERING)
			.createCompositeState(false)
	);
	
	private static final RenderType GLINT_TRANSLUCENT = RenderType.create(HLConstants.MOD_ID +
			":glint_translucent", DefaultVertexFormat.POSITION_COLOR_TEX, VertexFormat.Mode.QUADS, 256, false, false, RenderType.CompositeState.builder()
			.setShaderState(RENDERTYPE_GLINT_TRANSLUCENT_SHADER)
			.setTextureState(new RenderStateShard.TextureStateShard(ENCHANTED_GLINT_ITEM, true, false))
			.setWriteMaskState(COLOR_WRITE)
			.setCullState(NO_CULL)
			.setDepthTestState(EQUAL_DEPTH_TEST)
			.setTransparencyState(GLINT_TRANSPARENCY)
			.setTexturingState(GLINT_TEXTURING)
			.setOutputState(ITEM_ENTITY_TARGET)
			.createCompositeState(false)
	);
	
	private static final RenderType GLINT = RenderType.create(HLConstants.MOD_ID +
			":glint", DefaultVertexFormat.POSITION_COLOR_TEX, VertexFormat.Mode.QUADS, 256, false, false, RenderType.CompositeState.builder()
			.setShaderState(RENDERTYPE_GLINT_SHADER)
			.setTextureState(new RenderStateShard.TextureStateShard(ENCHANTED_GLINT_ITEM, true, false))
			.setWriteMaskState(COLOR_WRITE)
			.setCullState(NO_CULL)
			.setDepthTestState(EQUAL_DEPTH_TEST)
			.setTransparencyState(GLINT_TRANSPARENCY)
			.setTexturingState(GLINT_TEXTURING)
			.createCompositeState(false)
	);
	
	private static final RenderType GLINT_DIRECT = RenderType.create(HLConstants.MOD_ID +
			":glint_direct", DefaultVertexFormat.POSITION_COLOR_TEX, VertexFormat.Mode.QUADS, 256, false, false, RenderType.CompositeState.builder()
			.setShaderState(RENDERTYPE_GLINT_DIRECT_SHADER)
			.setTextureState(new RenderStateShard.TextureStateShard(ENCHANTED_GLINT_ITEM, true, false))
			.setWriteMaskState(COLOR_WRITE)
			.setCullState(NO_CULL)
			.setDepthTestState(EQUAL_DEPTH_TEST)
			.setTransparencyState(GLINT_TRANSPARENCY)
			.setTexturingState(GLINT_TEXTURING)
			.createCompositeState(false)
	);
	
	private static final RenderType ENTITY_GLINT = RenderType.create(HLConstants.MOD_ID +
			":entity_glint", DefaultVertexFormat.POSITION_COLOR_TEX, VertexFormat.Mode.QUADS, 256, false, false, RenderType.CompositeState.builder()
			.setShaderState(RENDERTYPE_ENTITY_GLINT_SHADER)
			.setTextureState(new RenderStateShard.TextureStateShard(ENCHANTED_GLINT_ENTITY, true, false))
			.setWriteMaskState(COLOR_WRITE)
			.setCullState(NO_CULL)
			.setDepthTestState(EQUAL_DEPTH_TEST)
			.setTransparencyState(GLINT_TRANSPARENCY)
			.setOutputState(ITEM_ENTITY_TARGET)
			.setTexturingState(ENTITY_GLINT_TEXTURING)
			.createCompositeState(false)
	);
	
	private static final RenderType ENTITY_GLINT_DIRECT = RenderType.create(HLConstants.MOD_ID +
			":entity_glint_direct", DefaultVertexFormat.POSITION_COLOR_TEX, VertexFormat.Mode.QUADS, 256, false, false, RenderType.CompositeState.builder()
			.setShaderState(RENDERTYPE_ENTITY_GLINT_DIRECT_SHADER)
			.setTextureState(new RenderStateShard.TextureStateShard(ENCHANTED_GLINT_ENTITY, true, false))
			.setWriteMaskState(COLOR_WRITE)
			.setCullState(NO_CULL)
			.setDepthTestState(EQUAL_DEPTH_TEST)
			.setTransparencyState(GLINT_TRANSPARENCY)
			.setTexturingState(ENTITY_GLINT_TEXTURING)
			.createCompositeState(false)
	);
	
	private static void setupGlintTexturing(float pScale)
	{
		long i = Util.getMillis() * 8L;
		float f = (float) (i % 110000L) / 110000.0F;
		float f1 = (float) (i % 30000L) / 30000.0F;
		Matrix4f matrix4f = Matrix4f.createTranslateMatrix(-f, f1, 0.0F);
		matrix4f.multiply(Vector3f.ZP.rotationDegrees(10.0F));
		matrix4f.multiply(Matrix4f.createScaleMatrix(pScale, pScale, pScale));
		RenderSystem.setTextureMatrix(matrix4f);
	}
	
	public static VertexConsumer glintBuffer(MultiBufferSource src, GlintType type, float r, float g, float b, float a)
	{
		return new TintingVertexConsumer(src.getBuffer(type.type), r, g, b, a);
	}
	
	public static VertexConsumer glintBuffer(MultiBufferSource src, GlintType type, int rgba)
	{
		return new TintingVertexConsumer(src.getBuffer(type.type), rgba);
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
	
	@Mod.EventBusSubscriber(bus = Mod.EventBusSubscriber.Bus.MOD, value = Dist.CLIENT)
	public static class GlintShaders
	{
		@Nullable
		private static ShaderInstance armorGlintShader;
		@Nullable
		private static ShaderInstance armorEntityGlintShader;
		@Nullable
		private static ShaderInstance glintTranslucentShader;
		@Nullable
		private static ShaderInstance glintShader;
		@Nullable
		private static ShaderInstance glintDirectShader;
		@Nullable
		private static ShaderInstance entityGlintShader;
		@Nullable
		private static ShaderInstance entityGlintDirectShader;
		
		@SubscribeEvent
		public static void registerShaders(RegisterShadersEvent e)
				throws IOException
		{
			var pResourceProvider = e.getResourceManager();
			e.registerShader(new ShaderInstance(pResourceProvider, HLConstants.id("rendertype_armor_glint")
					.toString(), DefaultVertexFormat.POSITION_COLOR_TEX), (s) ->
					armorGlintShader = s);
			e.registerShader(new ShaderInstance(pResourceProvider, HLConstants.id("rendertype_armor_entity_glint")
					.toString(), DefaultVertexFormat.POSITION_COLOR_TEX), (s) ->
					armorEntityGlintShader = s);
			e.registerShader(new ShaderInstance(pResourceProvider, HLConstants.id("rendertype_glint_translucent")
					.toString(), DefaultVertexFormat.POSITION_COLOR_TEX), (s) ->
					glintTranslucentShader = s);
			e.registerShader(new ShaderInstance(pResourceProvider, HLConstants.id("rendertype_glint")
					.toString(), DefaultVertexFormat.POSITION_COLOR_TEX), (s) ->
					glintShader = s);
			e.registerShader(new ShaderInstance(pResourceProvider, HLConstants.id("rendertype_glint_direct")
					.toString(), DefaultVertexFormat.POSITION_COLOR_TEX), (s) ->
					glintDirectShader = s);
			e.registerShader(new ShaderInstance(pResourceProvider, HLConstants.id("rendertype_entity_glint")
					.toString(), DefaultVertexFormat.POSITION_COLOR_TEX), (s) ->
					entityGlintShader = s);
			e.registerShader(new ShaderInstance(pResourceProvider, HLConstants.id("rendertype_entity_glint_direct")
					.toString(), DefaultVertexFormat.POSITION_COLOR_TEX), (s) ->
					entityGlintDirectShader = s);
		}
		
		@Nullable
		public static ShaderInstance getArmorGlintShader()
		{
			return armorGlintShader;
		}
		
		@Nullable
		public static ShaderInstance getArmorEntityGlintShader()
		{
			return armorEntityGlintShader;
		}
		
		@Nullable
		public static ShaderInstance getGlintTranslucentShader()
		{
			return glintTranslucentShader;
		}
		
		@Nullable
		public static ShaderInstance getGlintShader()
		{
			return glintShader;
		}
		
		@Nullable
		public static ShaderInstance getGlintDirectShader()
		{
			return glintDirectShader;
		}
		
		@Nullable
		public static ShaderInstance getEntityGlintShader()
		{
			return entityGlintShader;
		}
		
		@Nullable
		public static ShaderInstance getEntityGlintDirectShader()
		{
			return entityGlintDirectShader;
		}
	}
}