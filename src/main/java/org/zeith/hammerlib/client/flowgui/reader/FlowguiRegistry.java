package org.zeith.hammerlib.client.flowgui.reader;

import lombok.extern.slf4j.Slf4j;
import net.minecraft.resources.FileToIdConverter;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.packs.resources.ResourceManager;
import net.minecraft.util.profiling.ProfilerFiller;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.api.distmarker.OnlyIn;
import org.jetbrains.annotations.NotNull;
import org.xml.sax.SAXException;
import org.zeith.hammerlib.HammerLib;
import org.zeith.hammerlib.abstractions.props.KeyMap;
import org.zeith.hammerlib.api.data.IDataNode;
import org.zeith.hammerlib.client.flowgui.GuiObject;
import org.zeith.hammerlib.client.flowgui.objects.GuiRootObject;
import org.zeith.hammerlib.util.data.XmlHelper;
import org.zeith.hammerlib.util.mcf.Resources;
import org.zeith.hammerlib.util.mcf.ScanDataHelper;

import javax.xml.parsers.ParserConfigurationException;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.*;

@Slf4j
@OnlyIn(Dist.CLIENT)
public class FlowguiRegistry
{
	public static final FileToIdConverter FILE_TO_ID_CONVERTER = new FileToIdConverter("flowgui", ".xml");
	private static final Map<ResourceLocation, GuiReader<?>> REGISTRY = new HashMap<>();
	
	private static final Set<ResourceLocation> PRELOADED = new HashSet<>();
	private static final Set<ResourceLocation> MISSING = new HashSet<>();
	private static final Map<ResourceLocation, IDataNode> GUI_DATA = new HashMap<>();
	
	@NotNull
	public static GuiRootObject readRoot(ResourceLocation location, KeyMap context, float parentWidth, float parentHeight)
	{
		var data = GUI_DATA.get(location);
		if(data == null)
		{
			if(MISSING.add(location)) log.error("Missing flowgui file: {}", FILE_TO_ID_CONVERTER.idToFile(location));
			return GuiRootObject.root();
		}
		try
		{
			return Objects.requireNonNull(RootReader.INSTANCE.read(context, data, parentWidth, parentHeight),
					"The root was not created."
			);
		} catch(Exception e)
		{
			log.error("Failed to create Flowgui instance from file {}", FILE_TO_ID_CONVERTER.idToFile(location), e);
			return GuiRootObject.root();
		}
	}
	
	static GuiObject read(KeyMap context, IDataNode mess, float parentWidth, float parentHeight)
	{
		var cls = mess.getString(GuiReader.KEY_CLASS);
		var reader = getReader(Resources.locationOrNull(cls.toLowerCase(Locale.ROOT)));
		if(reader == null)
		{
			log.error("{} flowgui class: {}",
					Resources.locationOrNull(cls.toLowerCase(Locale.ROOT)) == null ? "Malformed" : "Unknown",
					cls
			);
			return null;
		}
		return reader.read(context, mess, parentWidth, parentHeight);
	}
	
	static GuiReader<?> getReader(ResourceLocation id)
	{
		return id != null ? REGISTRY.get(id) : null;
	}
	
	public static void reload(ResourceManager resources, ProfilerFiller profiler)
	{
		GUI_DATA.clear();
		MISSING.clear();
		GUI_DATA.clear();
		
		for(ResourceLocation id : PRELOADED)
		{
			var file = FILE_TO_ID_CONVERTER.idToFile(id);
			var res = resources.getResource(file).orElse(null);
			try(var input = res.open())
			{
				var data = XmlHelper.parse(new String(input.readAllBytes(), StandardCharsets.UTF_8));
				if(data == null)
				{
					log.error("Unable to read XML from flowgui file: {}", file);
					continue;
				}
				if(!data.getMyName().equalsIgnoreCase("root"))
				{
					log.error("Malformed XML from flowgui file {}: First XML node must always be <root>", file);
					continue;
				}
				GUI_DATA.put(id, data);
			} catch(IOException | ParserConfigurationException | SAXException e)
			{
				log.error("Failed to load flowgui file: {}", file, e);
				continue;
			}
		}
	}
	
	public static void handleXml(ScanDataHelper.ModAwareAnnotationData data)
	{
		PRELOADED.add(Resources.location(
				data.getOwnerMod().orElseThrow().getNamespace(),
				Objects.toString(data.getProperty("value").orElseThrow())
		));
	}
	
	public static void handleReader(ScanDataHelper.ModAwareAnnotationData data)
	{
		try
		{
			var ctor = data.getOwnerClass().asSubclass(GuiReader.class).getDeclaredConstructor();
			ctor.setAccessible(true);
			var reader = ctor.newInstance();
			
			var id = Resources.location(
					data.getOwnerMod().orElseThrow().getNamespace(),
					Objects.toString(data.getProperty("value").orElseThrow())
			);
			
			REGISTRY.put(id, reader);
		} catch(Exception e)
		{
			HammerLib.LOG.error("Failed to create FlowguiReader: {}", data.clazz());
		}
	}
}