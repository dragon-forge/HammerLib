package com.zeitheron.hammercore.client.journal;

import com.zeitheron.hammercore.client.utils.UtilsFX;

import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class PostAuthor
{
	public String avatar;
	public String displayName;
	
	@SideOnly(Side.CLIENT)
	public void bindTexture()
	{
		UtilsFX.bindTextureURL(avatar);
	}
}