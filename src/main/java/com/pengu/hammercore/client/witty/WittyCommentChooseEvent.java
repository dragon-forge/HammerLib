package com.pengu.hammercore.client.witty;

import java.util.Random;

import net.minecraftforge.fml.common.eventhandler.Event;

public class WittyCommentChooseEvent extends Event
{
	public final Random rand = new Random();
	private iWittyComment witty;
	
	public WittyCommentChooseEvent()
	{
	}
	
	/** Checks if we didn't apply any custom comments yet. */
	public boolean isCustomWittyComment()
	{
		return getWittyComment() != null;
	}
	
	public iWittyComment getWittyComment()
	{
		return witty;
	}
	
	public void setWittyComment(iWittyComment witty)
	{
		this.witty = witty;
	}
}