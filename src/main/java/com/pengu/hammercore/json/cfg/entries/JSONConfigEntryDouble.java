package com.pengu.hammercore.json.cfg.entries;

import com.pengu.hammercore.json.cfg.iJSONConfigEntry;
import com.pengu.hammercore.json.io.Jsonable;
import com.pengu.hammercore.json.io.SerializedName;

public class JSONConfigEntryDouble implements iJSONConfigEntry, Jsonable
{
	@SerializedName("Description")
	public String desc;
	
	@SerializedName("Name")
	public String key;
	
	@SerializedName("Value")
	public double value;
	
	@Override
	public String getName()
	{
		return key;
	}
	
	@Override
	public Object getValue()
	{
		return value;
	}
	
	@Override
	public String getComment()
	{
		return desc;
	}
	
	@Override
	public void setName(String name)
	{
		key = name;
	}
	
	@Override
	public void setComment(String comment)
	{
		desc = comment;
	}
	
	@Override
	public void setValue(Object value)
	{
		this.value = ((Number) value).doubleValue();
	}
	
	@Override
	public boolean equals(Object obj)
	{
		return equalsdef(obj);
	}
}