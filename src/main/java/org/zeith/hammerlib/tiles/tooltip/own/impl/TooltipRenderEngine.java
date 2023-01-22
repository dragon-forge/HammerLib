package org.zeith.hammerlib.tiles.tooltip.own.impl;

import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.Minecraft;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.phys.*;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.client.gui.overlay.ForgeGui;
import net.minecraftforge.client.gui.overlay.IGuiOverlay;
import org.zeith.hammerlib.tiles.tooltip.EnumTooltipEngine;
import org.zeith.hammerlib.tiles.tooltip.ITooltipTile;
import org.zeith.hammerlib.tiles.tooltip.own.ITooltipProvider;

import java.util.Objects;
import java.util.UUID;

@OnlyIn(Dist.CLIENT)
public class TooltipRenderEngine
		implements IGuiOverlay
{
	public HitResult.Type lastType;
	public BlockPos lastPos;
	public UUID lastEntity;
	public GuiTooltip lastTooltip;
	
	@Override
	public void render(ForgeGui gui, PoseStack poseStack, float partialTick, int screenWidth, int screenHeight)
	{
		var mc = Minecraft.getInstance();
		HitResult res = mc.hitResult;
		if(res != null)
		{
			if(res.getType() == HitResult.Type.BLOCK && res instanceof BlockHitResult bhr)
			{
				ClientLevel wc = mc.level;
				BlockPos bp = bhr.getBlockPos();
				Block bl = wc.getBlockState(bp).getBlock();
				BlockEntity tl = wc.getBlockEntity(bp);
				
				ITooltipProvider prov = null;
				
				if(tl instanceof ITooltipProvider t) prov = t;
				else if(bl instanceof ITooltipProvider t) prov = t;
				else if(tl instanceof ITooltipTile t && t.isEngineSupported(EnumTooltipEngine.HAMMER_LIB))
					prov = new WrappedTooltipEngine(tl, t);
				
				boolean diffPos = !Objects.equals(bp, lastPos);
				
				if(diffPos)
				{
					lastPos = bp;
					lastTooltip = null;
				}
				
				if(prov != null && (lastType != res.getType() || diffPos || (lastTooltip == null || lastTooltip.isDirty())))
				{
					prov.setTooltipDirty(false);
					lastTooltip = new GuiTooltip()
							.withPlayer(mc.player)
							.withLocation(wc, bp)
							.withProvider(prov);
				}
				
				lastType = res.getType();
			} else if(res.getType() == HitResult.Type.ENTITY && res instanceof EntityHitResult ehr && ehr.getEntity() instanceof ITooltipProvider prov)
			{
				var ent = ehr.getEntity();
				boolean diffEntity = !Objects.equals(ent.getUUID(), lastEntity);
				
				if(diffEntity)
				{
					lastEntity = ent.getUUID();
					lastTooltip = null;
				}
				
				if(diffEntity && (lastType != res.getType() || prov.isTooltipDirty() || (lastTooltip == null || lastTooltip.isDirty())))
				{
					prov.setTooltipDirty(false);
					lastTooltip = new GuiTooltip()
							.withPlayer(mc.player)
							.withEntity(ent)
							.withProvider(prov);
				}
				
				lastType = res.getType();
			} else
			{
				lastPos = null;
				lastTooltip = null;
			}
		} else
		{
			lastPos = null;
			lastTooltip = null;
		}
		
		if(lastTooltip != null)
		{
			var window = gui.getMinecraft().window;
			int sw = window.getGuiScaledWidth();
			int sh = window.getGuiScaledHeight();
			
			float cx = ((float) sw) / 2F + 12;
			float cy = ((float) sh - lastTooltip.getHeight()) / 2F + 2;
			
			lastTooltip.render(poseStack, cx, cy, partialTick);
		}
	}
}