package com.pengu.hammercore.core.modbrowser;

import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.imageio.ImageIO;

import org.lwjgl.opengl.GL11;

import com.pengu.hammercore.client.UV;
import com.pengu.hammercore.client.utils.GLImageManager;
import com.pengu.hammercore.common.utils.DynamicObject;
import com.pengu.hammercore.common.utils.IOUtils;
import com.pengu.hammercore.common.utils.MD5;
import com.pengu.hammercore.json.JSONArray;
import com.pengu.hammercore.json.JSONException;
import com.pengu.hammercore.json.JSONObject;
import com.pengu.hammercore.json.JSONTokener;

import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.texture.TextureMap;
import net.minecraft.util.math.Vec3i;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.common.Loader;
import net.minecraftforge.fml.common.ModContainer;

public class ModBrowser
{
	public static final String URL_BASE = "https://raw.githubusercontent.com/APengu/HammerCore/mod-list/", SHEETS = URL_BASE + "sheets/";
	
	private static final Map<String, Integer> glImages = new HashMap<>();
	
	private Map<String, Version> versions = new HashMap<>();
	public final List<Mod> mods = new ArrayList<>();
	public final Map<String, LoadedMod> loadedMods = new HashMap<>();
	
	public ModBrowser(DynamicObject<String> status) throws IOException
	{
		status.set("Checking mod list...");
		
		for(LoadedMod mod : collectMods(status))
			loadedMods.put(mod.modid, mod);
		
		status.set("Connecting to github...");
		
		InputStream input = new URL(URL_BASE + "version.types").openConnection().getInputStream();
		status.set("Downloading version list...");
		
		String jsonstr = new String(IOUtils.pipeOut(input), "UTF-8");
		status.set("Parsing version list...");
		
		try
		{
			JSONObject object = (JSONObject) new JSONTokener(jsonstr).nextValue();
			
			for(String key : object.keySet())
			{
				JSONObject version = object.getJSONObject(key);
				JSONArray uv = version.getJSONArray("icon");
				JSONObject translations = version.getJSONObject("translations");
				
				Version ver = new Version();
				ver.id = key;
				ver.iconId = version.getString("sheet");
				ver.iconPos = new Vec3i(uv.getInt(0), uv.getInt(1), 0);
				
				for(String lang : translations.keySet())
				{
					String val = translations.getString(lang);
					ver.translations.put(lang.toLowerCase(), val);
					status.set(lang + "=" + val);
				}
				
				versions.put(key, ver);
			}
		} catch(JSONException e)
		{
			e.printStackTrace();
		}
		
		status.set("Downloading mod list for " + MinecraftForge.MC_VERSION + "...");
		
		jsonstr = new String(IOUtils.downloadData(URL_BASE + "mods-" + MinecraftForge.MC_VERSION + ".prop"));
		
		status.set("Parsing mod list...");
		
		try
		{
			JSONObject object = (JSONObject) new JSONTokener(jsonstr).nextValue();
			
			for(String modname : object.keySet())
			{
				JSONObject modjson = object.getJSONObject(modname);
				
				Mod mod = new Mod();
				mod.modName = modname;
				mod.modid = modjson.getString("modid");
				mod.iconUrl = modjson.getString("logo");
				mod.description = modjson.getString("tooltip");
				
				mod.authors = Arrays.toString(modjson.getJSONArray("authors").values());
				mod.authors = mod.authors.substring(1, mod.authors.length() - 1);
				
				JSONObject versions = modjson.getJSONObject("versions");
				
				for(String vertype : versions.keySet())
				{
					JSONObject version = versions.getJSONObject(vertype);
					Version ver = this.versions.get(vertype);
					mod.supportedVersions.put(ver, version.getString("version"));
					mod.fileVersions.put(version.getString("version"), version.getString("download"));
					mod.fileSizes.put(version.getString("version"), version.getLong("size"));
				}
				
				mods.add(mod);
			}
		} catch(Throwable err)
		{
		}
		
		status.set("Loading icons...");
		
		for(String key : versions.keySet())
		{
			Version ver = versions.get(key);
			status.set("Loading icon for version " + ver.id + "...");
			ver.loadIcon();
		}
		
		for(Mod mod : mods)
		{
			status.set("Loading icon for mod " + mod.modName + "...");
			mod.loadIcon();
		}
		
		for(int i = 0; i < 8; ++i)
			try
			{
				Mod[] sorted = null;
				Arrays.sort(sorted = mods.toArray(new Mod[0]), new Comparator<Mod>()
				{
					@Override
					public int compare(Mod o1, Mod o2)
					{
						return o1.modName.compareTo(o2.modName);
					}
				});
				mods.clear();
				mods.addAll(Arrays.asList(sorted));
				
				break;
			} catch(Throwable err)
			{
			}
		
		status.set("Done!");
	}
	
	public static List<LoadedMod> collectMods(DynamicObject<String> status)
	{
		ArrayList<LoadedMod> mods = new ArrayList<>();
		for(ModContainer mc : Loader.instance().getActiveModList())
		{
			LoadedMod mod = new LoadedMod();
			mod.description = mc.getMetadata().description;
			mod.modid = mc.getModId();
			mod.modname = mc.getName();
			mod.version = mc.getVersion();
			mod.associated_file = mc.getSource();
			mods.add(mod);
		}
		return mods;
	}
	
	public static final class LoadedMod
	{
		public String version, modid, modname, description;
		public File associated_file;
	}
	
	public static final class Mod
	{
		public final Map<Version, String> supportedVersions = new HashMap<>();
		public final Map<String, String> fileVersions = new HashMap<>();
		public final Map<String, Long> fileSizes = new HashMap<>();
		
		public String modName, modid, description, authors;
		public UV icon;
		
		private String iconUrl;
		private int openglID;
		
		private void loadIcon() throws IOException
		{
			byte[] data = IOUtils.downloadData(iconUrl);
			String md5 = MD5.encrypt(data);
			
			BufferedImage img = ImageIO.read(new ByteArrayInputStream(data));
			
			Minecraft.getMinecraft().addScheduledTask(() ->
			{
				int newGLID = glImages.get(md5) != null ? glImages.get(md5).intValue() : GL11.glGenTextures();
				GLImageManager.loadTexture(img, newGLID, false);
				openglID = newGLID;
				glImages.put(md5, newGLID);
			});
			
			icon = new UV(TextureMap.LOCATION_MISSING_TEXTURE, 0, 0, 256, 256)
			{
				@Override
				public void bindTexture()
				{
					GL11.glBindTexture(GL11.GL_TEXTURE_2D, openglID);
				}
			};
		}
	}
	
	public static final class Version
	{
		private String id;
		private String iconId;
		private Vec3i iconPos;
		private final Map<String, String> translations = new HashMap<>();
		private int openglID;
		public UV icon;
		
		public String getId()
		{
			String val = translations.get(Minecraft.getMinecraft().gameSettings.language.toLowerCase());
			String valen = translations.get("en_us");
			return val != null ? val : valen != null ? valen : id;
		}
		
		private void loadIcon() throws IOException
		{
			byte[] data = IOUtils.downloadData(SHEETS + iconId);
			String md5 = MD5.encrypt(data);
			
			BufferedImage img = ImageIO.read(new ByteArrayInputStream(data));
			
			Minecraft.getMinecraft().addScheduledTask(() ->
			{
				int newGLID = glImages.get(md5) != null ? glImages.get(md5).intValue() : GL11.glGenTextures();
				GLImageManager.loadTexture(img, newGLID, false);
				openglID = newGLID;
				glImages.put(md5, newGLID);
			});
			
			icon = new UV(TextureMap.LOCATION_MISSING_TEXTURE, iconPos.getX(), iconPos.getY(), 25, 25)
			{
				@Override
				public void bindTexture()
				{
					GL11.glBindTexture(GL11.GL_TEXTURE_2D, openglID);
				}
			};
		}
	}
}