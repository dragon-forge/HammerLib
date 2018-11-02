package com.zeitheron.hammercore.client;

import java.io.FileOutputStream;
import java.io.IOException;

import com.zeitheron.hammercore.HammerCore;
import com.zeitheron.hammercore.HammerCore.HCAuthor;
import com.zeitheron.hammercore.client.utils.texture.gui.theme.GuiTheme;
import com.zeitheron.hammercore.lib.zlib.json.JSONObject;
import com.zeitheron.hammercore.lib.zlib.json.JSONTokener;
import com.zeitheron.hammercore.lib.zlib.json.serapi.IgnoreSerialization;
import com.zeitheron.hammercore.lib.zlib.json.serapi.Jsonable;
import com.zeitheron.hammercore.lib.zlib.json.serapi.SerializationContext;
import com.zeitheron.hammercore.lib.zlib.json.serapi.SerializedName;
import com.zeitheron.hammercore.lib.zlib.utils.MD5;
import com.zeitheron.hammercore.net.HCNet;
import com.zeitheron.hammercore.net.internal.opts.PacketCHCOpts;

import net.minecraft.client.Minecraft;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.nbt.JsonToNBT;
import net.minecraft.nbt.NBTException;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

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
	
	public boolean renderSpecial = true;
	
	public boolean overrideCape;
	
	public int skinType;
	
	private String Theme;
	
	@IgnoreSerialization
	public NBTTagCompound customData = new NBTTagCompound();
	
	public NBTTagCompound getCustomData()
	{
		if(customData == null)
			customData = new NBTTagCompound();
		return customData;
	}
	
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
			if(au.getStore() != null && Minecraft.getMinecraft().getSession().getUsername().equals(au.getUsername()))
				return au.getStore().matches(MD5.encrypt(passcode));
		return true;
	}
	
	public void setDefaults()
	{
		setTheme("Vanilla");
		customData = new NBTTagCompound();
		renderSpecial = true;
		overrideCape = true;
		skinType = 0;
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
		skinType = j.optInt("SkinType");
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