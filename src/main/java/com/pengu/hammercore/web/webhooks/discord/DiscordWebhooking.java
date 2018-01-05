package com.pengu.hammercore.web.webhooks.discord;

import com.pengu.hammercore.json.io.Jsonable;
import com.pengu.hammercore.json.io.SerializedName;

/**
 * Used for constant message sending with same data
 */
public class DiscordWebhooking implements Jsonable
{
	public String avatar;
	public String webhook;
	public String username;
	
	@SerializedName("tts")
	public boolean textToSpeech;
	
	public DiscordWebhooking setAvatar(String avatar)
	{
		this.avatar = avatar;
		return this;
	}
	
	public DiscordWebhooking setUsername(String username)
	{
		this.username = username;
		return this;
	}
	
	public DiscordWebhooking setWebhook(String webhook)
	{
		this.webhook = webhook;
		return this;
	}
	
	public DiscordWebhooking enableTTS()
	{
		textToSpeech = true;
		return this;
	}
	
	public DiscordWebhooking disableTTS()
	{
		textToSpeech = false;
		return this;
	}
	
	public boolean sendMessage(String message)
	{
		return DiscordMessage.sendMessage(new DiscordMessage(webhook, username, message, textToSpeech, avatar)).isEmpty();
	}
}