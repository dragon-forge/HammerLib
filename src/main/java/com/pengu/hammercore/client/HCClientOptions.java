package com.pengu.hammercore.client;

import java.io.FileOutputStream;
import java.io.IOException;

import com.pengu.hammercore.HammerCore;
import com.pengu.hammercore.HammerCore.HCAuthor;
import com.pengu.hammercore.client.texture.gui.theme.GuiTheme;
import com.pengu.hammercore.common.utils.MD5;
import com.pengu.hammercore.json.JSONObject;
import com.pengu.hammercore.json.io.IgnoreSerialization;
import com.pengu.hammercore.json.io.Jsonable;
import com.pengu.hammercore.json.io.SerializedName;

import net.minecraft.client.Minecraft;

public class HCClientOptions implements Jsonable
{
	public static HCClientOptions options = new HCClientOptions();
	
	@IgnoreSerialization
	private JSONObject data;
	
	@SerializedName("Authority")
	public String authority;
	
	private String Theme;
	
	public boolean checkAuthority()
	{
		for(HCAuthor au : HammerCore.getHCAuthors())
			if(Minecraft.getMinecraft().getSession().getUsername().equals(au.getUsername()))
				return au.getStore().matches(authority);
		return true;
	}
	
	public static boolean checkAuthority(String passcode)
	{
		for(HCAuthor au : HammerCore.getHCAuthors())
			if(Minecraft.getMinecraft().getSession().getUsername().equals(au.getUsername()))
				return au.getStore().matches(MD5.encrypt(passcode));
		return true;
	}
	
	public void setDefaults()
	{
		setTheme("Vanilla");
		authority = null;
	}
	
	public void load(JSONObject j)
	{
		setTheme(j.optString("Theme"), false);
		data = j;
	}
	
	public String getTheme()
	{
		return Theme;
	}
	
	public JSONObject getData()
	{
		return data;
	}
	
	public void setTheme(String theme)
	{
		setTheme(theme, true);
	}
	
	private void setTheme(String theme, boolean save)
	{
		for(GuiTheme t : GuiTheme.THEMES)
			if(t.name.equalsIgnoreCase(theme))
			{
				GuiTheme.CURRENT_THEME = t;
				this.Theme = theme;
				if(save)
					save();
				return;
			}
		GuiTheme.CURRENT_THEME = GuiTheme.THEMES.get(0);
		this.Theme = GuiTheme.CURRENT_THEME.name;
	}
	
	public static HCClientOptions getOptions()
	{
		return options;
	}
	
	public void save()
	{
		try
		{
			FileOutputStream fos = new FileOutputStream("hc_options.txt");
			fos.write(serialize().getBytes());
			fos.close();
		} catch(IOException e)
		{
		}
	}
}