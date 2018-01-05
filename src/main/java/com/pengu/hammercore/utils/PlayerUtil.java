package com.pengu.hammercore.utils;

import java.io.InputStream;
import java.net.URL;
import java.net.URLConnection;
import java.util.UUID;

import com.pengu.hammercore.common.utils.IOUtils;
import com.pengu.hammercore.json.JSONArray;
import com.pengu.hammercore.json.JSONObject;
import com.pengu.hammercore.json.JSONTokener;

public class PlayerUtil
{
	public static UUID getUUIDFromUsername(String username)
	{
		try
		{
			URL u = new URL("https://api.mojang.com/users/profiles/minecraft/" + username);
			URLConnection conn = u.openConnection();
			InputStream in = conn.getInputStream();
			byte[] id = new byte[7];
			in.read(id);
			byte[] uuidBuf = new byte[32];
			in.read(uuidBuf);
			in.close();
			return StringHelper.untrimUUID(new String(uuidBuf));
		} catch(Throwable err)
		{
		}
		return UUID.randomUUID();
	}
	
	public static String getUsernameFromUUID(UUID uid)
	{
		try
		{
			JSONArray arr = (JSONArray) new JSONTokener(new String(IOUtils.downloadData("https://api.mojang.com/user/profiles/" + StringHelper.trimUUID(uid) + "/names"))).nextValue();
			JSONObject obj = arr.getJSONObject(arr.length() - 1);
			return obj.getString("name");
		} catch(Throwable err)
		{
		}
		return "?";
	}
}