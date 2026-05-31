package com.zeitheron.hammercore.client;

import com.zeitheron.hammercore.HammerCore;
import com.zeitheron.hammercore.client.utils.texture.gui.theme.GuiTheme;
import com.zeitheron.hammercore.lib.zlib.json.*;
import com.zeitheron.hammercore.lib.zlib.json.serapi.*;
import com.zeitheron.hammercore.net.HCNet;
import com.zeitheron.hammercore.net.internal.opts.PacketCHCOpts;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.nbt.*;
import net.minecraftforge.fml.relauncher.*;

import java.io.*;

public class HCClientOptions implements Jsonable
{
	/** ClientOnly! */
	public static HCClientOptions options = new HCClientOptions();
	
	@IgnoreSerialization
	public boolean def = true;
	
	@IgnoreSerialization
	private JSONObject data;
	
	@SerializedName("Authority")
	public String authority;
	
	public boolean renderSpecial = false;
	
	public boolean overrideCape;
	
	private String Theme;
	
	@IgnoreSerialization
	public NBTTagCompound customData = new NBTTagCompound();
	
	public NBTTagCompound getCustomData()
	{
		if(customData == null)
			customData = new NBTTagCompound();
		return customData;
	}
	
	public void setDefaults()
	{
		setTheme("Vanilla");
		customData = new NBTTagCompound();
		renderSpecial = false;
		overrideCape = true;
		authority = null;
		def = true;
	}
	
	public void load(JSONObject j)
	{
		def = false;
		// Fixed theme intersection
		if(this == getOptions())
			setTheme(j.optString("Theme"), false);
		authority = j.optString("Authority", "0");
		renderSpecial = j.optBoolean("renderSpecial", true);
		overrideCape = j.optBoolean("overrideCape", true);
		try
		{
			customData = JsonToNBT.getTagFromJson(j.optString("CustomData"));
			HammerCore.renderProxy.cl_loadOpts(this, customData);
		} catch(NBTException e)
		{
			e.printStackTrace();
		}
		data = j;
	}
	
	@Override
	public SerializationContext serializationContext()
	{
		SerializationContext c = new SerializationContext();
		
		if(customData == null)
			customData = new NBTTagCompound();
		
		HammerCore.renderProxy.cl_saveOpts(this, customData);
		
		c.set("CustomData", customData != null ? customData.toString() : "{}");
		
		return c;
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
	
	public void loadFrom(HCClientOptions o)
	{
		try
		{
			JSONObject j = (JSONObject) new JSONTokener(o.serialize()).nextValue();
			load(j);
		} catch(Throwable er)
		{
		}
	}
	
	@SideOnly(Side.CLIENT)
	public void saveAndSendToServer()
	{
		save();
		EntityPlayer ep = HammerCore.renderProxy.getClientPlayer();
		if(ep != null)
			HCNet.INSTANCE.sendToServer(new PacketCHCOpts().setPlayer(ep).setOpts(this));
	}
}