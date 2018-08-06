package com.zeitheron.hammercore.client.witty;

import java.util.Random;

import net.minecraftforge.fml.common.eventhandler.Event;

public class WittyCommentChooseEvent extends Event
{
	public final Random rand = new Random();
	private IWittyComment witty;
	
	public WittyCommentChooseEvent()
	{
	}
	
	/**
	 * @return If we didn't apply any custom comments yet.
	 */
	public boolean isCustomWittyComment()
	{
		return getWittyComment() != null;
	}
	
	public IWittyComment getWittyComment()
	{
		return witty;
	}
	
	public void setWittyComment(IWittyComment witty)
	{
		this.witty = witty;
	}
}