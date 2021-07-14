package org.zeith.hammerlib.tiles.tooltip.own;

import net.minecraft.block.Block;
import net.minecraft.client.Minecraft;
import net.minecraft.client.world.ClientWorld;
import net.minecraft.entity.Entity;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.BlockRayTraceResult;
import net.minecraft.util.math.EntityRayTraceResult;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.util.math.RayTraceResult.Type;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.client.event.RenderGameOverlayEvent;
import net.minecraftforge.client.event.RenderGameOverlayEvent.ElementType;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.eventbus.api.SubscribeEvent;

import java.util.Objects;
import java.util.UUID;

@OnlyIn(Dist.CLIENT)
public class EntityTooltipRenderEngine
{
	public BlockPos lastPos;
	public UUID lastEntity;
	public GuiTooltip lastTooltip;

	{
		MinecraftForge.EVENT_BUS.register(this);
	}

	@SubscribeEvent
	public void renderWorldLast(RenderGameOverlayEvent.Post e)
	{
		if(e.getType() == ElementType.ALL)
		{
			RayTraceResult res = Minecraft.getInstance().hitResult;
			if(res != null)
			{
				ITooltipProviderHC prov = null;

				if(res.getType() == Type.BLOCK && res instanceof BlockRayTraceResult)
					bl:
							{
								ClientWorld wc = Minecraft.getInstance().level;
								BlockPos bp = ((BlockRayTraceResult) res).getBlockPos();
								Block bl = wc.getBlockState(bp).getBlock();
								TileEntity tl = wc.getBlockEntity(bp);

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

								if(prov == null)
									break bl;

								if(diffPos || prov.isTooltipDirty())
								{
									prov.setTooltipDirty(false);
									lastTooltip = new GuiTooltip().withLocation(wc, bp).withProvider(prov);
								}
							}
				else if(res.getType() == Type.ENTITY && res instanceof EntityRayTraceResult && ((EntityRayTraceResult) res).getEntity() instanceof ITooltipProviderHC)
				{
					Entity ent;
					prov = (ITooltipProviderHC) (ent = ((EntityRayTraceResult) res).getEntity());
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
				int sw = e.getWindow().getGuiScaledWidth();
				int sh = e.getWindow().getGuiScaledHeight();

				float cx = ((float) sw) / 2F + 12;
				float cy = ((float) sh - lastTooltip.getHeight()) / 2F + 2;

				float pt = e.getPartialTicks();
				lastTooltip.render(e.getMatrixStack(), cx, cy, pt);
			}
		}
	}
}