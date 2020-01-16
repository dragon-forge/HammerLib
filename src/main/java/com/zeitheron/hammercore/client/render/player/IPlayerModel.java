package com.zeitheron.hammercore.client.render.player;

import net.minecraftforge.client.event.RenderPlayerEvent;

public interface IPlayerModel
{
	default IPlayerModel and(IPlayerModel r)
	{
		if(r == IPlayerModel.this)
			return this;

		return player ->
		{
			IPlayerModel.this.render(player);
			r.render(player);
		};
	}

	void render(RenderPlayerEvent player);
}