package com.zeitheron.hammercore.server.management;

import java.util.Date;

import com.google.gson.JsonObject;

import net.minecraft.server.management.UserListEntryBan;

public class UserListMachineBansEntry extends UserListEntryBan<String>
{
	protected String value;
	
	public UserListMachineBansEntry(String valueIn)
	{
		this(valueIn, (Date) null, (String) null, (Date) null, (String) null);
	}
	
	public UserListMachineBansEntry(String valueIn, Date startDate, String banner, Date endDate, String banReason)
	{
		super(valueIn, startDate, banner, endDate, banReason);
		this.value = valueIn;
	}
	
	public UserListMachineBansEntry(JsonObject json)
	{
		super(getHardwareFromJson(json), json);
	}
	
	private static String getHardwareFromJson(JsonObject json)
	{
		return json.has("hardware") ? json.get("hardware").getAsString() : null;
	}
	
	@Override
	protected void onSerialization(JsonObject data)
	{
		if(value != null)
		{
			data.addProperty("hardware", value);
			super.onSerialization(data);
		}
	}
}