package com.pengu.hammercore.web.webhooks.discord;

import com.pengu.hammercore.json.io.IgnoreSerialization;
import com.pengu.hammercore.json.io.Jsonable;
import com.pengu.hammercore.json.io.SerializedName;
import com.pengu.hammercore.web.HttpRequest;

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
		return HttpRequest.post(msg.webhook).acceptJson().contentType("application/json").header("User-Agent", "Mozilla/5.0 (X11; U; Linux i686) Gecko/20071127 Firefox/2.0.0.11").send(msg.serialize()).body();
	}
}