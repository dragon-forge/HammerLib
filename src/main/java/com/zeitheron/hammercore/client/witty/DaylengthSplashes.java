package com.zeitheron.hammercore.client.witty;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.TimeZone;

import javax.annotation.Nullable;

import com.zeitheron.hammercore.lib.zlib.error.JSONException;
import com.zeitheron.hammercore.lib.zlib.io.IOUtils;
import com.zeitheron.hammercore.lib.zlib.json.JSONArray;
import com.zeitheron.hammercore.lib.zlib.json.JSONObject;
import com.zeitheron.hammercore.utils.ListUtils;

public class DaylengthSplashes
{
	public static final SimpleDateFormat DATE_PARSER = new SimpleDateFormat("dd.MM");
	public static final Random SELECT_RNG = new Random();
	private static final Map<Integer, List<String>> DAYLENGTH_SPLASHES = new HashMap<>();
	
	static
	{
		try
		{
			JSONObject jobj = (JSONObject) IOUtils.downloadjsonOrLoadFromInternal("https://pastebin.com/raw/k9NJUb2P", "/assets/hammercore/io/dlsplashes.json");
			for(String key : jobj.keySet())
			{
				List<String> vals = new ArrayList<>();
				{
					Object v = jobj.opt(key);
					if(v instanceof String)
						vals.add((String) v);
					if(v instanceof JSONArray)
					{
						JSONArray ja = (JSONArray) v;
						for(int i = 0; i < ja.length(); ++i)
							vals.add(ja.getString(i));
					}
				}
				vals.forEach(v -> addBirthdaySplash(key, v));
			}
		} catch(JSONException e)
		{
		}
	}
	
	public static boolean addBirthdaySplash(String date, String splash)
	{
		try
		{
			Calendar c = new Calendar.Builder().setInstant(DATE_PARSER.parse(date)).setTimeZone(TimeZone.getDefault()).build();
			int doy = c.get(Calendar.DAY_OF_YEAR);
			List<String> pos = DAYLENGTH_SPLASHES.get(doy);
			if(pos == null)
				DAYLENGTH_SPLASHES.put(doy, pos = new ArrayList<>());
			if(!pos.contains(splash))
				pos.add(splash);
		} catch(Throwable err)
		{
			
		}
		return false;
	}
	
	@Nullable
	public static String getDaylengthSplash()
	{
		List<String> potential = DAYLENGTH_SPLASHES.get(Calendar.getInstance().get(Calendar.DAY_OF_YEAR));
		return potential != null ? ListUtils.random(potential, SELECT_RNG) : null;
	}
}