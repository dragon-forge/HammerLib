package com.pengu.hammercore.api.dynlight;

import java.lang.reflect.Field;
import java.lang.reflect.ParameterizedType;
import java.util.Iterator;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.ConcurrentSkipListSet;

import com.pengu.hammercore.HammerCore;
import com.pengu.hammercore.proxy.ParticleProxy_Client;

import net.minecraft.block.state.IBlockState;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.RenderGlobal;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.EnumSkyBlock;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraft.world.WorldServer;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent.Phase;

public class DynamicLightGetter
{
	private static IBlockAccess lastWorld;
	private static ConcurrentLinkedQueue<DynLightContainer> lastList;
	private static ConcurrentHashMap<World, ConcurrentLinkedQueue<DynLightContainer>> worldLightsMap = new ConcurrentHashMap<>();
	
	public static int getLightValue(IBlockState blockState, IBlockAccess world, BlockPos pos)
	{
		int vanillaValue = blockState.getLightValue(world, pos);
		
		if(world instanceof WorldServer)
		{
			return vanillaValue;
		}
		
		if(!world.equals(lastWorld) || lastList == null)
		{
			lastWorld = world;
			lastList = worldLightsMap.get(world);
			hackRenderGlobalConcurrently();
		}
		
		int dynamicValue = 0;
		if(lastList != null && !lastList.isEmpty())
		{
			for(DynLightContainer light : lastList)
			{
				if(light.getX() == pos.getX())
				{
					if(light.getY() == pos.getY())
					{
						if(light.getZ() == pos.getZ())
						{
							dynamicValue = Math.max(dynamicValue, light.getLightSource().getLightLevel());
						}
					}
				}
			}
		}
		return Math.max(vanillaValue, dynamicValue);
	}
	
	private static void hackRenderGlobalConcurrently()
	{
		try
		{
			for(Field f : RenderGlobal.class.getDeclaredFields())
			{
				if(Set.class.isAssignableFrom(f.getType()))
				{
					ParameterizedType fieldType = (ParameterizedType) f.getGenericType();
					if(BlockPos.class.equals(fieldType.getActualTypeArguments()[0]))
					{
						f.setAccessible(true);
						Set<BlockPos> setLightUpdates = (Set<BlockPos>) f.get(Minecraft.getMinecraft().renderGlobal);
						if(setLightUpdates instanceof ConcurrentSkipListSet)
							return;
						ConcurrentSkipListSet<BlockPos> cs = new ConcurrentSkipListSet<>(setLightUpdates);
						f.set(Minecraft.getMinecraft().renderGlobal, cs);
						return;
					}
				}
			}
		} catch(Exception e)
		{
			e.printStackTrace();
		}
	}
	
	@SubscribeEvent
	public void onTick(TickEvent.ClientTickEvent tick)
	{
		if(HammerCore.particleProxy instanceof ParticleProxy_Client)
			((ParticleProxy_Client) HammerCore.particleProxy).clientTick(tick);
		
		Minecraft mc = Minecraft.getMinecraft();
		if(tick.phase == Phase.END && mc.world != null)
		{
			ConcurrentLinkedQueue<DynLightContainer> worldLights = worldLightsMap.get(mc.world);
			
			if(worldLights != null)
			{
				Iterator<DynLightContainer> iter = worldLights.iterator();
				while(iter.hasNext())
				{
					DynLightContainer tickedLightContainer = iter.next();
					if(!tickedLightContainer.update())
					{
						iter.remove();
						mc.world.checkLightFor(EnumSkyBlock.BLOCK, new BlockPos(tickedLightContainer.getX(), tickedLightContainer.getY(), tickedLightContainer.getZ()));
					}
				}
			}
		}
	}
	
	public static void addLightSource(iDynlightSrc lightToAdd)
	{
		if(lightToAdd.getSrcInfo() != null)
		{
			if(lightToAdd.getSrcInfo().isAlive())
			{
				DynLightContainer newLightContainer = new DynLightContainer(lightToAdd);
				ConcurrentLinkedQueue<DynLightContainer> lightList = worldLightsMap.get(lightToAdd.getSrcInfo().getWorld());
				if(lightList != null)
				{
					if(!lightList.contains(newLightContainer))
						lightList.add(newLightContainer);
				} else
				{
					lightList = new ConcurrentLinkedQueue<>();
					lightList.add(newLightContainer);
					worldLightsMap.put(lightToAdd.getSrcInfo().getWorld(), lightList);
				}
			}
			// else
			// {
			// System.err.println("Cannot add Dynamic Light: Attachment Entity
			// is dead!");
			// }
		}
		// else
		// {
		// System.err.println("Cannot add Dynamic Light: Attachment Entity is
		// null!");
		// }
	}
	
	public static void removeLightSource(iDynlightSrc lightToRemove)
	{
		if(lightToRemove != null && lightToRemove.getSrcInfo() != null)
		{
			World world = lightToRemove.getSrcInfo().getWorld();
			if(world != null)
			{
				DynLightContainer iterContainer = null;
				ConcurrentLinkedQueue<DynLightContainer> lightList = worldLightsMap.get(world);
				if(lightList != null)
				{
					Iterator<DynLightContainer> iter = lightList.iterator();
					while(iter.hasNext())
					{
						iterContainer = iter.next();
						if(iterContainer.getLightSource().equals(lightToRemove))
						{
							iter.remove();
							break;
						}
					}
					
					if(iterContainer != null)
					{
						world.checkLightFor(EnumSkyBlock.BLOCK, new BlockPos(iterContainer.getX(), iterContainer.getY(), iterContainer.getZ()));
					}
				}
			}
		}
	}
}