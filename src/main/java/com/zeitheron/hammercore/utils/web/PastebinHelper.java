package com.zeitheron.hammercore.utils.web;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.UnsupportedEncodingException;
import java.net.MalformedURLException;
import java.net.URISyntaxException;
import java.net.URL;
import java.net.URLConnection;
import java.net.URLEncoder;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;

public class PastebinHelper
{
	/**
	 * Creates a new paste using default dev key
	 */
	public static String paste(String text, String title)
	{
		try
		{
			return paste(text, "0ec2eb25b6166c0c27a394ae118ad829", title);
		} catch(RuntimeException e)
		{
			return e.getMessage();
		}
	}
	
	public static String makePostSignature(Map<String, String> post)
	{
		StringBuilder builder = new StringBuilder();
		for(Entry<String, String> entry : post.entrySet())
			try
			{
				builder.append(entry.getKey()).append('=').append(URLEncoder.encode(entry.getValue(), "UTF-8")).append('&');
			} catch(UnsupportedEncodingException e)
			{
				e.printStackTrace();
			}
		builder.deleteCharAt(builder.length() - 1);
		return new String(builder);
	}
	
	public static String paste(String content, String devkey, String title)
	{
		Map<String, String> post = new HashMap<>();
		post.put("api_dev_key", devkey);
		post.put("api_option", "paste");
		post.put("api_paste_code", content);
		post.put("api_paste_private", "1");
		if(title != null)
			post.put("api_paste_name", title);
		try
		{
			String pageResponse = post("http://pastebin.com/api/api_post.php", post);
			if(pageResponse.startsWith("http"))
				return new URL(pageResponse).toURI().getPath().substring(1);
			throw new RuntimeException("Failed to generate paste: " + pageResponse);
		} catch(MalformedURLException | URISyntaxException e)
		{
			throw new RuntimeException("Failed to generate paste: " + e);
		}
	}
	
	public static String post(String link, Map<String, String> post)
	{
		try
		{
			URL url = new URL(link);
			URLConnection connection = url.openConnection();
			if(post != null)
			{
				connection.setDoOutput(true);
				OutputStreamWriter wr = new OutputStreamWriter(connection.getOutputStream());
				wr.write(makePostSignature(post));
				wr.flush();
				wr.close();
			}
			BufferedReader reader = new BufferedReader(new InputStreamReader(connection.getInputStream()));
			StringBuilder builder = new StringBuilder();
			String line;
			while((line = reader.readLine()) != null)
			{
				if(builder.length() > 0)
					builder.append('\n');
				builder.append(line);
			}
			reader.close();
			return new String(builder);
		} catch(MalformedURLException e)
		{
			throw new IllegalArgumentException("Malformed link: " + e);
		} catch(IOException e)
		{
			throw new RuntimeException("Failed to fetch contents from link: " + e);
		}
	}
}