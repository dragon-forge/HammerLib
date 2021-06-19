package com.zeitheron.hammercore.utils;

import net.minecraftforge.fml.common.versioning.ComparableVersion;

public class VersionCompareTool
{
	public final ComparableVersion version;
	
	public VersionCompareTool(String version)
	{
		this.version = new ComparableVersion(version);
	}
	
	public EnumVersionLevel compare(VersionCompareTool other)
	{
		int level = version.compareTo(other.version);
		return level > 0 ? EnumVersionLevel.NEWER : level < 0 ? EnumVersionLevel.OLDER : EnumVersionLevel.SAME;
	}
	
	public enum EnumVersionLevel
	{
		SAME, NEWER, OLDER;
	}
}