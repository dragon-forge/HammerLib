package org.zeith.hammerlib.client.render;

import com.mojang.blaze3d.platform.GlStateManager;
import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.*;
import net.minecraft.Util;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.*;
import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.RegisterShadersEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import org.joml.Matrix4f;
import org.zeith.hammerlib.proxy.HLConstants;
import org.zeith.hammerlib.util.colors.ColorHelper;

import javax.annotation.Nullable;
import java.io.IOException;
import java.util.function.Supplier;

public class RenderCustomGlint
{
	public static final ResourceLocation ENCHANTED_GLINT_ENTITY = HLConstants.id("textures/misc/enchanted_glint_entity.png");
	public static final ResourceLocation ENCHANTED_GLINT_ITEM = HLConstants.id("textures/misc/enchanted_glint_item.png");
	
	protected static final ShaderStateShardWithColor RENDERTYPE_ARMOR_GLINT_SHADER = new ShaderStateShardWithColor(GlintShaders::getArmorGlintShader);
	protected static final ShaderStateShardWithColor RENDERTYPE_ARMOR_ENTITY_GLINT_SHADER = new ShaderStateShardWithColor(GlintShaders::getArmorEntityGlintShader);
	protected static final ShaderStateShardWithColor RENDERTYPE_GLINT_TRANSLUCENT_SHADER = new ShaderStateShardWithColor(GlintShaders::getGlintTranslucentShader);
	protected static final ShaderStateShardWithColor RENDERTYPE_GLINT_SHADER = new ShaderStateShardWithColor(GlintShaders::getGlintShader);
	protected static final ShaderStateShardWithColor RENDERTYPE_GLINT_DIRECT_SHADER = new ShaderStateShardWithColor(GlintShaders::getGlintDirectShader);
	protected static final ShaderStateShardWithColor RENDERTYPE_ENTITY_GLINT_SHADER = new ShaderStateShardWithColor(GlintShaders::getEntityGlintShader);
	protected static final ShaderStateShardWithColor RENDERTYPE_ENTITY_GLINT_DIRECT_SHADER = new ShaderStateShardWithColor(GlintShaders::getEntityGlintDirectShader);
	
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
	
	private static final RenderType ARMOR_GLINT = RenderType.create("armor_glint", DefaultVertexFormat.POSITION_COLOR_TEX, VertexFormat.Mode.QUADS, 256, false, false, RenderType.CompositeState.builder()
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
	
	private static final RenderType ARMOR_ENTITY_GLINT = RenderType.create("armor_entity_glint", DefaultVertexFormat.POSITION_COLOR_TEX, VertexFormat.Mode.QUADS, 256, false, false, RenderType.CompositeState.builder()
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
	
	private static final RenderType GLINT_TRANSLUCENT = RenderType.create("glint_translucent", DefaultVertexFormat.POSITION_COLOR_TEX, VertexFormat.Mode.QUADS, 256, false, false, RenderType.CompositeState.builder()
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
	
	private static final RenderType GLINT = RenderType.create("glint", DefaultVertexFormat.POSITION_COLOR_TEX, VertexFormat.Mode.QUADS, 256, false, false, RenderType.CompositeState.builder()
			.setShaderState(RENDERTYPE_GLINT_SHADER)
			.setTextureState(new RenderStateShard.TextureStateShard(ENCHANTED_GLINT_ITEM, true, false))
			.setWriteMaskState(COLOR_WRITE)
			.setCullState(NO_CULL)
			.setDepthTestState(EQUAL_DEPTH_TEST)
			.setTransparencyState(GLINT_TRANSPARENCY)
			.setTexturingState(GLINT_TEXTURING)
			.createCompositeState(false)
	);
	
	private static final RenderType GLINT_DIRECT = RenderType.create("glint_direct", DefaultVertexFormat.POSITION_COLOR_TEX, VertexFormat.Mode.QUADS, 256, false, false, RenderType.CompositeState.builder()
			.setShaderState(RENDERTYPE_GLINT_DIRECT_SHADER)
			.setTextureState(new RenderStateShard.TextureStateShard(ENCHANTED_GLINT_ITEM, true, false))
			.setWriteMaskState(COLOR_WRITE)
			.setCullState(NO_CULL)
			.setDepthTestState(EQUAL_DEPTH_TEST)
			.setTransparencyState(GLINT_TRANSPARENCY)
			.setTexturingState(GLINT_TEXTURING)
			.createCompositeState(false)
	);
	
	private static final RenderType ENTITY_GLINT = RenderType.create("entity_glint", DefaultVertexFormat.POSITION_COLOR_TEX, VertexFormat.Mode.QUADS, 256, false, false, RenderType.CompositeState.builder()
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
	
	private static final RenderType ENTITY_GLINT_DIRECT = RenderType.create("entity_glint_direct", DefaultVertexFormat.POSITION_COLOR_TEX, VertexFormat.Mode.QUADS, 256, false, false, RenderType.CompositeState.builder()
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
		long i = (long) ((double) Util.getMillis() * Minecraft.getInstance().options.glintSpeed().get() * 8.0D);
		float f = (float) (i % 110000L) / 110000.0F;
		float f1 = (float) (i % 30000L) / 30000.0F;
		Matrix4f matrix4f = (new Matrix4f()).translation(-f, f1, 0.0F);
		matrix4f.rotateZ(0.17453292F).scale(pScale / 32F);
		RenderSystem.setTextureMatrix(matrix4f);
	}
	
	public static RenderType glint(GlintType type, float r, float g, float b, float a)
	{
		type.shard.setColor(r, g, b, a);
		return type.type;
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
		ARMOR_GLINT(RENDERTYPE_ARMOR_GLINT_SHADER, RenderCustomGlint.ARMOR_GLINT),
		ARMOR_ENTITY_GLINT(RENDERTYPE_ARMOR_ENTITY_GLINT_SHADER, RenderCustomGlint.ARMOR_ENTITY_GLINT),
		GLINT_TRANSLUCENT(RENDERTYPE_GLINT_TRANSLUCENT_SHADER, RenderCustomGlint.GLINT_TRANSLUCENT),
		GLINT(RENDERTYPE_GLINT_SHADER, RenderCustomGlint.GLINT),
		GLINT_DIRECT(RENDERTYPE_GLINT_DIRECT_SHADER, RenderCustomGlint.GLINT_DIRECT),
		ENTITY_GLINT(RENDERTYPE_ENTITY_GLINT_SHADER, RenderCustomGlint.ENTITY_GLINT),
		ENTITY_GLINT_DIRECT(RENDERTYPE_ENTITY_GLINT_DIRECT_SHADER, RenderCustomGlint.ENTITY_GLINT_DIRECT);
		
		final ShaderStateShardWithColor shard;
		final RenderType type;
		
		GlintType(ShaderStateShardWithColor shard, RenderType type)
		{
			this.shard = shard;
			this.type = type;
		}
	}
	
	public static class ShaderStateShardWithColor
			extends RenderStateShard.ShaderStateShard
	{
		protected Supplier<ShaderInstance> shader;
		protected float chosenR, chosenG, chosenB, chosenA;
		
		public ShaderStateShardWithColor(Supplier<ShaderInstance> pShader)
		{
			super(pShader);
			this.shader = pShader;
		}
		
		public void setColor(float r, float g, float b, float a)
		{
			this.chosenR = r;
			this.chosenG = g;
			this.chosenB = b;
			this.chosenA = a;
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
			var pResourceProvider = e.getResourceProvider();
			e.registerShader(new ShaderInstance(pResourceProvider, HLConstants.id("rendertype_armor_glint"), DefaultVertexFormat.POSITION_COLOR_TEX), (s) ->
					armorGlintShader = s);
			e.registerShader(new ShaderInstance(pResourceProvider, HLConstants.id("rendertype_armor_entity_glint"), DefaultVertexFormat.POSITION_COLOR_TEX), (s) ->
					armorEntityGlintShader = s);
			e.registerShader(new ShaderInstance(pResourceProvider, HLConstants.id("rendertype_glint_translucent"), DefaultVertexFormat.POSITION_COLOR_TEX), (s) ->
					glintTranslucentShader = s);
			e.registerShader(new ShaderInstance(pResourceProvider, HLConstants.id("rendertype_glint"), DefaultVertexFormat.POSITION_COLOR_TEX), (s) ->
					glintShader = s);
			e.registerShader(new ShaderInstance(pResourceProvider, HLConstants.id("rendertype_glint_direct"), DefaultVertexFormat.POSITION_COLOR_TEX), (s) ->
					glintDirectShader = s);
			e.registerShader(new ShaderInstance(pResourceProvider, HLConstants.id("rendertype_entity_glint"), DefaultVertexFormat.POSITION_COLOR_TEX), (s) ->
					entityGlintShader = s);
			e.registerShader(new ShaderInstance(pResourceProvider, HLConstants.id("rendertype_entity_glint_direct"), DefaultVertexFormat.POSITION_COLOR_TEX), (s) ->
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