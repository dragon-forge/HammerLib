package com.pengu.hammercore.client.render.player;

import net.minecraftforge.client.event.RenderPlayerEvent;

public abstract interface iPlayerModel
{
	default iPlayerModel and(iPlayerModel r)
	{
		if(r == iPlayerModel.this)
			return this;
		
		return player ->
		{
			iPlayerModel.this.render(player);
			r.render(player);
		};
	}
	
	void render(RenderPlayerEvent player);
}