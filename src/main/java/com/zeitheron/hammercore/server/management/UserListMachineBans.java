package com.zeitheron.hammercore.server.management;

import java.io.File;
import java.net.SocketAddress;
import java.util.UUID;

import com.google.gson.JsonObject;

import net.minecraft.server.management.UserList;
import net.minecraft.server.management.UserListEntry;
import net.minecraft.server.management.UserListIPBansEntry;

public class UserListMachineBans extends UserList<String, UserListMachineBansEntry>
{
	public UserListMachineBans(File bansFile)
	{
		super(bansFile);
	}
	
	@Override
	protected UserListEntry<String> createEntry(JsonObject entryData)
	{
		return new UserListIPBansEntry(entryData);
	}
	
	public boolean isBanned(UUID hardware)
	{
		return this.hasEntry(hardware.toString());
	}
	
	public UserListMachineBansEntry getBanEntry(UUID hardware)
	{
		return (UserListMachineBansEntry) this.getEntry(hardware.toString());
	}
}