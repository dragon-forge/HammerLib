package org.zeith.hammerlib.client.flowgui.readers;

import com.google.common.base.Suppliers;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import lombok.SneakyThrows;
import net.minecraft.ChatFormatting;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.nbt.TagParser;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.Style;
import net.minecraft.world.item.ItemStack;
import org.zeith.hammerlib.abstractions.props.KeyMap;
import org.zeith.hammerlib.annotations.ide.*;
import org.zeith.hammerlib.api.data.IDataNode;
import org.zeith.hammerlib.client.flowgui.objects.GuiItemObject;
import org.zeith.hammerlib.client.flowgui.reader.*;
import org.zeith.hammerlib.proxy.HLConstants;
import org.zeith.hammerlib.util.mcf.Resources;

import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicReference;
import java.util.function.Supplier;

import static org.zeith.hammerlib.client.flowgui.reader.ComDrivers.*;

@Namespace(HLConstants.MOD_ID)
@FlowguiReader("item")
public class FlowguiItemReader
		extends GuiReader<GuiItemObject>
{
	@AllowJS
	@AllowedValues(AllowedValues.RESOURCE_LOCATION)
	@FileReference(
			regex = { "^(?<modid>[a-z0-9_.-]+):(?<path>[a-z0-9_./-]+)$", "^(?<path>[a-z0-9_./-]+)$" },
			value = { "assets/%modid%/models/item/%path%.json", "assets/minecraft/models/item/%path%.json" }
	)
	public static final @Required("minecraft:diamond") String KEY_ITEM = "item";
	
	@AllowJS
	@AllowedValues(AllowedValues.POSITIVE_INTEGERS)
	public static final String KEY_COUNT = "count";
	
	@AllowJS
	@Suggestions({ "true", "false" })
	@AllowedValues(AllowedValues.BOOLEAN)
	public static final @Default("false") String KEY_HOVERABLE = "hoverable";
	
	@AllowJS
	@Suggestions({ "true", "false" })
	@AllowedValues(AllowedValues.BOOLEAN)
	public static final @Default("true") String KEY_DECORATED = "decorated";
	
	@AllowJS
	@Suggestions({ "true", "false" })
	@AllowedValues(AllowedValues.BOOLEAN)
	public static final String KEY_OFFER_INGREDIENT = "offer-ingredient";
	
	@AllowJS
	public static final String KEY_NBT = "nbt";
	
	@SneakyThrows
	@Override
	protected GuiItemObject readObject(KeyMap map, String name, IDataNode node)
	{
		AtomicReference<GuiItemObject> self = new AtomicReference<>();
		var ctx = getDriverContext(map, node, self);
		
		var itemIdFactory = ComDrivers.readString(ctx, KEY_ITEM);
		var tagFactory = ComDrivers.readString(ctx, KEY_NBT);
		
		boolean constant = ComDrivers.isConstant(itemIdFactory) && ComDrivers.isConstant(tagFactory);
		
		Supplier<ItemStack> primaryFactory = () ->
		{
			var stack = BuiltInRegistries.ITEM
					.get(Resources.location(itemIdFactory.get()))
					.getDefaultInstance();
			
			var tag = tagFactory.get();
			if(tag != null && !tag.isBlank())
			{
				try
				{
					stack.setTag(TagParser.parseTag(tag));
				} catch(CommandSyntaxException e)
				{
					stack.setHoverName(Component.literal(e.toString()).setStyle(Style.EMPTY.withColor(ChatFormatting.RED)));
				}
			}
			return stack;
		};
		
		var intermFactory = constant ? Suppliers.memoize(primaryFactory::get) : primaryFactory;
		
		AtomicInteger count = new AtomicInteger(1);
		Supplier<ItemStack> finalFactory = () ->
		{
			var stack = intermFactory.get();
			stack.setCount(count.get());
			return stack;
		};
		
		GuiItemObject o = new GuiItemObject(name, finalFactory);
		self.set(o);
		
		driveInt(ctx, KEY_COUNT, 1, false, count::set);
		driveBool(ctx, KEY_HOVERABLE, false, false, o::hoverable);
		driveBool(ctx, KEY_DECORATED, true, false, o::decorated);
		driveBool(ctx, KEY_OFFER_INGREDIENT, false, false, o::provideIngredient);
		
		return o;
	}
}