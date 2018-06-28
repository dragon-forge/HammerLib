package com.zeitheron.hammercore.tile.tooltip.own;

import java.util.Objects;
import java.util.UUID;

import com.zeitheron.hammercore.utils.WorldLocation;

import net.minecraft.block.Block;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.client.multiplayer.WorldClient;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.util.math.RayTraceResult.Type;
import net.minecraftforge.client.event.RenderGameOverlayEvent;
import net.minecraftforge.client.event.RenderGameOverlayEvent.ElementType;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

@SideOnly(Side.CLIENT)
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
			RayTraceResult res = Minecraft.getMinecraft().objectMouseOver;
			if(res != null)
			{
				ITooltipProviderHC prov = null;
				
				if(res.typeOfHit == Type.BLOCK)
					bl:
					{
						WorldClient wc = Minecraft.getMinecraft().world;
						BlockPos bp = res.getBlockPos();
						Block bl = wc.getBlockState(bp).getBlock();
						TileEntity tl = wc.getTileEntity(bp);
						
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
							lastTooltip = new GuiTooltip().withLocation(new WorldLocation(wc, bp)).withProvider(prov);
						}
					}
				else if(res.typeOfHit == Type.ENTITY && res.entityHit instanceof ITooltipProviderHC)
				{
					prov = (ITooltipProviderHC) res.entityHit;
					if(prov != null)
					{
						boolean diffEntity = !Objects.equals(res.entityHit.getUniqueID(), lastEntity);
						
						if(diffEntity)
						{
							lastEntity = res.entityHit.getUniqueID();
							lastTooltip = null;
						}
						
						if(diffEntity || prov.isTooltipDirty())
						{
							prov.setTooltipDirty(false);
							lastTooltip = new GuiTooltip().withEntity(res.entityHit).withProvider(prov);
						}
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
				float cx = ((float) e.getResolution().getScaledWidth_double()) / 2F + 12;
				float cy = ((float) e.getResolution().getScaledHeight_double() - lastTooltip.getHeight()) / 2F + 2;
				
				float pt = e.getPartialTicks();
				lastTooltip.render(cx, cy, pt);
			}
		}
	}
}