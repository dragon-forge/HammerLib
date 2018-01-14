package com.pengu.hammercore.client;

import java.io.FileOutputStream;
import java.io.IOException;

import com.pengu.hammercore.client.texture.gui.theme.GuiTheme;
import com.pengu.hammercore.json.JSONObject;
import com.pengu.hammercore.json.io.IgnoreSerialization;
import com.pengu.hammercore.json.io.Jsonable;

public class HCClientOptions implements Jsonable
{
	public static HCClientOptions options = new HCClientOptions();
	
	@IgnoreSerialization
	private JSONObject data;
	
	private String Theme;
	
	public void setDefaults()
	{
		setTheme("Vanilla");
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