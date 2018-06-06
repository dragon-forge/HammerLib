package com.zeitheron.hammercore.client.particle;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import net.minecraft.util.ResourceLocation;
import net.minecraftforge.client.event.RenderWorldLastEvent;

public class FXGroupRenderer
{
	public static final FXGroupRenderer INSTANCE = new FXGroupRenderer();
	
	final Map<ResourceLocation, FXGroup> groupMap = new HashMap<>();
	final List<FXGroup> groups = new ArrayList<>();
	
	public void addParticle(ParticleGrouped particle)
	{
		FXGroup group = groupMap.get(particle.getTexture());
		if(group == null)
			groupMap.put(particle.getTexture(), group = new FXGroup(this, particle.getTexture()));
		group.groupedParticles.add(particle);
	}
	
	public void update()
	{
		for(int i = 0; i < groups.size(); ++i)
		{
			FXGroup p = groups.get(i);
			if(!p.isAlive())
			{
				groups.remove(i);
				continue;
			}
			p.update();
		}
	}
	
	public void render(RenderWorldLastEvent evt)
	{
		for(int i = 0; i < groups.size(); ++i)
		{
			FXGroup p = groups.get(i);
			if(!p.isAlive())
			{
				groups.remove(i);
				continue;
			}
			p.render(evt);
		}
	}
}