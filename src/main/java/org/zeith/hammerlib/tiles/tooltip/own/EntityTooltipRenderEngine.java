package org.zeith.hammerlib.tiles.tooltip.own;

import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.Minecraft;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.EntityHitResult;
import net.minecraft.world.phys.HitResult;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.client.gui.overlay.ForgeGui;
import net.minecraftforge.client.gui.overlay.IGuiOverlay;

import java.util.Objects;
import java.util.UUID;

@OnlyIn(Dist.CLIENT)
public class EntityTooltipRenderEngine implements IGuiOverlay
{
	public BlockPos lastPos;
	public UUID lastEntity;
	public GuiTooltip lastTooltip;
	
	@Override
	public void render(ForgeGui gui, PoseStack poseStack, float partialTick, int screenWidth, int screenHeight)
	{
		HitResult res = Minecraft.getInstance().hitResult;
		if(res != null)
		{
			ITooltipProviderHC prov = null;
			
			if(res.getType() == HitResult.Type.BLOCK && res instanceof BlockHitResult bhr)
			{
				ClientLevel wc = Minecraft.getInstance().level;
				BlockPos bp = bhr.getBlockPos();
				Block bl = wc.getBlockState(bp).getBlock();
				BlockEntity tl = wc.getBlockEntity(bp);
				
				if(tl instanceof ITooltipProviderHC)
					prov = (ITooltipProviderHC) tl;
				else if(bl instanceof ITooltipProviderHC)
					prov = (ITooltipProviderHC) bl;
				
				boolean diffPos = !Objects.equals(bp, lastPos);
				
				if(diffPos)
				{
					lastPos = bp;
					lastTooltip = null;
				}
				
				if(prov != null && (diffPos || prov.isTooltipDirty()))
				{
					prov.setTooltipDirty(false);
					lastTooltip = new GuiTooltip().withLocation(wc, bp).withProvider(prov);
				}
			} else if(res.getType() == HitResult.Type.ENTITY && res instanceof EntityHitResult ehr && ehr.getEntity() instanceof ITooltipProviderHC)
			{
				Entity ent;
				prov = (ITooltipProviderHC) (ent = ehr.getEntity());
				boolean diffEntity = !Objects.equals(ent.getUUID(), lastEntity);
				
				if(diffEntity)
				{
					lastEntity = ent.getUUID();
					lastTooltip = null;
				}
				
				if(diffEntity || prov.isTooltipDirty())
				{
					prov.setTooltipDirty(false);
					lastTooltip = new GuiTooltip().withEntity(ent).withProvider(prov);
				}
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