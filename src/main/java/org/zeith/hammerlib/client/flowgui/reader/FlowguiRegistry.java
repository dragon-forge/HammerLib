package org.zeith.hammerlib.client.flowgui.reader;

import com.google.common.base.Suppliers;
import lombok.extern.slf4j.Slf4j;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.resources.FileToIdConverter;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.packs.resources.ResourceManager;
import net.minecraft.util.RandomSource;
import net.minecraft.util.profiling.ProfilerFiller;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.api.distmarker.OnlyIn;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.objectweb.asm.Type;
import org.xml.sax.SAXException;
import org.zeith.hammerlib.HammerLib;
import org.zeith.hammerlib.abstractions.props.Key;
import org.zeith.hammerlib.abstractions.props.KeyMap;
import org.zeith.hammerlib.api.data.IDataNode;
import org.zeith.hammerlib.client.flowgui.GuiObject;
import org.zeith.hammerlib.client.flowgui.data.FlowQuery;
import org.zeith.hammerlib.client.flowgui.objects.GuiRootObject;
import org.zeith.hammerlib.core.js.JsFactory;
import org.zeith.hammerlib.proxy.HLConstants;
import org.zeith.hammerlib.util.data.XmlHelper;
import org.zeith.hammerlib.util.java.Cast;
import org.zeith.hammerlib.util.java.Hashers;
import org.zeith.hammerlib.util.java.itf.FloatSupplier;
import org.zeith.hammerlib.util.mcf.Resources;
import org.zeith.hammerlib.util.mcf.ScanDataHelper;

import javax.xml.parsers.ParserConfigurationException;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.*;
import java.util.function.Supplier;
import java.util.stream.Collectors;

@Slf4j
@OnlyIn(Dist.CLIENT)
public class FlowguiRegistry
{
	public static final FileToIdConverter FILE_TO_ID_CONVERTER = new FileToIdConverter("flowgui", ".xml");
	private static final Map<ResourceLocation, GuiReader<?>> REGISTRY = new HashMap<>();
	
	private static ResourceManager resources;
	private static final Map<Type, ResourceLocation> PRELOADED = new HashMap<>();
	private static final Set<ResourceLocation> MISSING = new HashSet<>();
	private static final Map<ResourceLocation, Supplier<IDataNode>> GUI_DATA = new HashMap<>();
	
	//<editor-fold desc="Required keys">
	public static final Key<FlowQuery> QUERY = Key.of(HLConstants.id("query"), FlowQuery.class);
	public static final Key<ResourceLocation> ROOT_ID = Key.of(HLConstants.id("root_id"), ResourceLocation.class);
	public static final Key<GuiRootObject> PREVIOUS_ROOT = Key.of(HLConstants.id("previous_root"), GuiRootObject.class);
	//</editor-fold>
	
	public static final Key<Boolean> IS_CACHING_JS = Key.of(HLConstants.id("caching_js"), Boolean.class);
	
	//<editor-fold desc="Automatically populated keys">
	public static final Key<GuiRootObject> GUI_ROOT = Key.of(HLConstants.id("gui_root"), GuiRootObject.class);
	public static final Key<Stack<ResourceLocation>> LOAD_STACK = Key.of(HLConstants.id("load_stack"), Cast.cast(Stack.class));
	public static final Key<JsContext> JS_CONTEXT = Key.of(HLConstants.id("js_context"), JsContext.class);
	public static final Key<RandomSource> NAMEGEN_RANDOM = Key.of(HLConstants.id("namegen_random"), RandomSource.class);
	//</editor-fold>
	
	public static boolean DISABLE_CACHE = Boolean.parseBoolean(Objects.toString(System.getProperty("hammerlib.flowgui.nocache")));
	private static final JsContext GLOBAL = new JsContext();
	
	@NotNull
	public static GuiRootObject readRoot(KeyMap context)
	{
		var location = context.opt(ROOT_ID).orElseThrow();
		var gui = context.opt(QUERY).orElseThrow().gui;
		context.put(LOAD_STACK, new Stack<>());
		context.put(JS_CONTEXT, GLOBAL);
		context.computeIfAbsent(IS_CACHING_JS, Cast.constant(false));
		context.put(NAMEGEN_RANDOM, RandomSource.create(Hashers.hashCodeL(location.getNamespace(), location.getPath())));
		GuiRootObject root = readRoot(location, context, gui != null ? () -> gui.width : Cast.constantF(1920), gui != null ? () -> gui.height : Cast.constantF(1080));
		root.finishBuilding();
		return root;
	}
	
	@NotNull
	public static GuiRootObject readRoot(ResourceLocation location, KeyMap context, FloatSupplier parentWidth, FloatSupplier parentHeight)
	{
		var stack = context.get(LOAD_STACK);
		if(stack.contains(location))
		{
			log.error("Detected infinite recursion while loading Flowgui root. Load stack: {} -X-> {}",
					stack.stream()
							.map(ResourceLocation::toString)
							.collect(Collectors.joining(" -> ")),
					location
			);
			return GuiObject.root();
		}
		stack.push(location);
		var root = readRootUnsafe(location, context, parentWidth, parentHeight);
		stack.pop();
		if(root == null) return GuiObject.root();
		return root;
	}
	
	@Nullable
	private static GuiRootObject readRootUnsafe(ResourceLocation location, KeyMap context, FloatSupplier parentWidth, FloatSupplier parentHeight)
	{
		var dataProvider = getFlowguiFile(location);
		if(dataProvider == null)
		{
			if(MISSING.add(location)) log.error("Unknown flowgui file: {}", FILE_TO_ID_CONVERTER.idToFile(location));
			return null;
		}
		var data = dataProvider.get();
		if(data == null)
		{
			if(MISSING.add(location)) log.error("Missing flowgui file: {}", FILE_TO_ID_CONVERTER.idToFile(location));
			return null;
		}
		try
		{
			return Objects.requireNonNull(RootReader.INSTANCE.read(context, data, parentWidth, parentHeight),
					"The root was not created."
			);
		} catch(Exception e)
		{
			log.error("Failed to create Flowgui instance from file {}", FILE_TO_ID_CONVERTER.idToFile(location), e);
			return null;
		}
	}
	
	@Nullable
	static GuiObject read(KeyMap context, IDataNode data, FloatSupplier parentWidth, FloatSupplier parentHeight)
	{
		var cls = data.getString(GuiReader.KEY_CLASS);
		var reader = getReader(Resources.locationOrNull(cls.toLowerCase(Locale.ROOT)));
		if(reader == null)
		{
			log.error("{} flowgui class: {}",
					Resources.locationOrNull(cls.toLowerCase(Locale.ROOT)) == null ? "Malformed" : "Unknown",
					cls
			);
			return null;
		}
		return reader.read(context, data, parentWidth, parentHeight);
	}
	
	static GuiReader<?> getReader(ResourceLocation id)
	{
		return id != null ? REGISTRY.get(id) : null;
	}
	
	public static void reload(ResourceManager resources, ProfilerFiller profiler)
	{
		FlowguiRegistry.resources = resources;
		GUI_DATA.clear();
		MISSING.clear();
		GLOBAL.clear();
		
		JsFactory.init(true);
		
		for(ResourceLocation id : PRELOADED.values())
		{
			getFlowguiFile(id);
			
			// If cache is not disabled, memoize the result from the resource!
			if(!DISABLE_CACHE)
			{
				// Instantly query the value to perform immediate preload.
				FlowguiRegistry.readRoot(
						KeyMap.createHash()
								.with(FlowguiRegistry.QUERY, new FlowQuery(null))
								.with(FlowguiRegistry.ROOT_ID, id)
								.with(FlowguiRegistry.IS_CACHING_JS, true)
				);
			}
		}
	}
	
	static Supplier<IDataNode> getFlowguiFile(ResourceLocation id)
	{
		var reg = GUI_DATA.get(id);
		if(reg != null) return reg;
		
		final var file = FILE_TO_ID_CONVERTER.idToFile(id);
		
		Supplier<IDataNode> provider = () ->
		{
			var res = resources.getResource(file).orElse(null);
			
			if(res == null)
			{
				log.error("Flowgui XML file not found: {}", file);
				return null;
			}
			
			try(var input = res.open())
			{
				var data = XmlHelper.parse(new String(input.readAllBytes(), StandardCharsets.UTF_8));
				if(data == null)
				{
					log.error("Unable to read XML from flowgui file: {}", file);
					return null;
				}
				if(!data.getMyName().equalsIgnoreCase("root"))
				{
					log.error("Malformed XML from flowgui file {}: First XML node must always be <root>", file);
					return null;
				}
				return data;
			} catch(IOException | ParserConfigurationException | SAXException e)
			{
				log.error("Failed to load flowgui file: {}", file, e);
				return null;
			}
		};
		
		// If cache is not disabled, memoize the result from the resource!
		if(!DISABLE_CACHE)
			provider = Suppliers.memoize(provider::get);
		
		GUI_DATA.put(id, provider);
		return provider;
	}
	
	public static ResourceLocation getId(Class<? extends Screen> gui)
	{
		return Objects.requireNonNull(PRELOADED.get(Type.getType(gui)), "XmlFlowgui.value() on " + gui);
	}
	
	public static void handleXml(ScanDataHelper.ModAwareAnnotationData data)
	{
		PRELOADED.put(data.clazz(), Resources.location(
						data.getOwnerMod().orElseThrow().getNamespace(),
						Objects.toString(data.getProperty("value").orElseThrow())
				)
		);
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