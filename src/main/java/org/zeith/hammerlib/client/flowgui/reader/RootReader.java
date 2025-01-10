package org.zeith.hammerlib.client.flowgui.reader;

import net.minecraft.client.Minecraft;
import org.zeith.hammerlib.abstractions.props.KeyMap;
import org.zeith.hammerlib.annotations.ide.AllowedValues;
import org.zeith.hammerlib.annotations.ide.Default;
import org.zeith.hammerlib.api.data.IDataNode;
import org.zeith.hammerlib.client.flowgui.objects.GuiRootObject;

import static org.zeith.hammerlib.client.flowgui.reader.FlowguiRegistry.GUI_ROOT;

final class RootReader
		extends GuiReader<GuiRootObject>
{
	static final RootReader INSTANCE = new RootReader();
	
	@AllowedValues(AllowedValues.BOOLEAN)
	public static final @Default("false") String KEY_DEBUG = "debug";
	
	@AllowedValues(AllowedValues.BOOLEAN)
	public static final @Default("true") String KEY_SMOOTH = "smooth";
	
	@Override
	protected GuiRootObject readObject(KeyMap map, String name, IDataNode attributes)
	{
		var root = GuiRootObject.root();
		root.debugBoundaries = attributes.getBoolean(KEY_DEBUG);
		map.put(GUI_ROOT, root);
		
		var query = map.get(FlowguiRegistry.QUERY);
		
		boolean smooth = attributes.getBooleanOrDefault(KEY_SMOOTH, true);
		
		root.onPreRender((partialTime, mouse) ->
		{
			if(smooth)
				partialTime = Minecraft.getInstance().getDeltaTracker().getGameTimeDeltaPartialTick(true);
			query.partialTime = partialTime;
			query.time = (query.ticks + query.partialTime) / 20D;
		});
		
		root.onTick(() ->
		{
			query.partialTime = 0;
			++query.ticks;
		});
		
		return root;
	}
}