package com.zeitheron.hammercore.utils.web.webhooks.discord;

import com.zeitheron.hammercore.lib.zlib.json.serapi.IgnoreSerialization;
import com.zeitheron.hammercore.lib.zlib.json.serapi.Jsonable;
import com.zeitheron.hammercore.lib.zlib.json.serapi.SerializedName;
import com.zeitheron.hammercore.lib.zlib.web.HttpRequest;

public class DiscordMessage implements Jsonable
{
	@IgnoreSerialization
	public String webhook;
	
	public String username;
	
	@SerializedName("content")
	public String message;
	
	@SerializedName("tts")
	public boolean textToSpeech;
	
	@SerializedName("avatar_url")
	public String avatar;
	
	public DiscordMessage(String webhook, String username, String message, boolean tts, String avatar)
	{
		this.webhook = webhook;
		this.username = username;
		this.message = message;
		this.textToSpeech = tts;
		this.avatar = avatar;
	}
	
	public static String sendMessage(DiscordMessage msg)
	{
		return HttpRequest.post(msg.webhook).acceptJson().contentType("application/json").header("User-Agent", "HammerCore/@VERSION@").send(msg.serialize()).body();
	}
}