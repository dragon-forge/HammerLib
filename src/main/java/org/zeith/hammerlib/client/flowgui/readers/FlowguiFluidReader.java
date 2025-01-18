package org.zeith.hammerlib.client.flowgui.readers;

import com.google.common.base.MoreObjects;
import com.google.common.base.Suppliers;
import lombok.SneakyThrows;
import net.minecraft.ChatFormatting;
import net.minecraft.core.component.DataComponentPatch;
import net.minecraft.core.component.DataComponents;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.Style;
import net.minecraft.util.Mth;
import net.neoforged.neoforge.fluids.FluidStack;
import org.zeith.hammerlib.abstractions.props.KeyMap;
import org.zeith.hammerlib.annotations.ide.*;
import org.zeith.hammerlib.api.data.IDataNode;
import org.zeith.hammerlib.client.flowgui.objects.GuiFluidObject;
import org.zeith.hammerlib.client.flowgui.reader.*;
import org.zeith.hammerlib.client.utils.FluidTextureType;
import org.zeith.hammerlib.event.listeners.TagsUpdateListener;
import org.zeith.hammerlib.proxy.HLConstants;
import org.zeith.hammerlib.util.java.Suppliers2;
import org.zeith.hammerlib.util.mcf.CodecHelper;
import org.zeith.hammerlib.util.mcf.Resources;

import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicReference;
import java.util.function.Supplier;

import static org.zeith.hammerlib.client.flowgui.reader.ComDrivers.*;

@Namespace(HLConstants.MOD_ID)
@FlowguiReader("fluid")
public class FlowguiFluidReader
		extends GuiReader<GuiFluidObject>
{
	@AllowJS
	@AllowedValues(AllowedValues.RESOURCE_LOCATION)
	public static final @Required("minecraft:water") String KEY_FLUID = "fluid";
	
	@AllowJS
	@AllowedValues({ })
	public static final String KEY_FLUID_STACK = "fluid-stack";
	
	@AllowJS
	@AllowedValues(AllowedValues.POSITIVE_INTEGERS)
	public static final @Required("1000") String KEY_COUNT = "count";
	
	@AllowJS
	@AllowedValues(AllowedValues.POSITIVE_INTEGERS)
	public static final @Required("1000") String KEY_CAPACITY = "capacity";
	
	@AllowJS
	@Suggestions({ "true", "false" })
	@AllowedValues(AllowedValues.BOOLEAN)
	public static final String KEY_HOVERABLE = "hoverable";
	
	@AllowJS
	@Suggestions({ "true", "false" })
	@AllowedValues(AllowedValues.BOOLEAN)
	public static final String KEY_OFFER_INGREDIENT = "offer-ingredient";
	
	@AllowJS
	@AllowedValues("^still|flow|flowing$")
	@Suggestions({ "still", "flowing" })
	public static final String KEY_FLUID_TEXTURE = "fluid-texture";
	
	@AllowJS
	@Suggestions({ "true", "false" })
	@AllowedValues(AllowedValues.BOOLEAN)
	public static final String KEY_SHOW_CAPACITY = "show-capacity";
	
	@AllowJS
	public static final String KEY_NBT = "nbt";
	
	@SneakyThrows
	@Override
	protected GuiFluidObject readObject(KeyMap map, String name, IDataNode node)
	{
		AtomicReference<GuiFluidObject> self = new AtomicReference<>();
		var ctx = getDriverContext(map, node, self);
		
		Supplier<FluidStack> finalFactory;
		boolean dynCount;
		
		if(node.keys().contains(KEY_FLUID_STACK))
		{
			finalFactory = Suppliers2.map(ComDrivers.readObject(ctx, KEY_FLUID_STACK, FluidStack.class), f ->
					{
						if(f == null) f = FluidStack.EMPTY;
						return f;
					}
			);
			dynCount = true;
		} else
		{var itemIdFactory = ComDrivers.readString(ctx, KEY_FLUID);
		var tagFactory = ComDrivers.readString(ctx, KEY_NBT);
		
		boolean constant = ComDrivers.isConstant(itemIdFactory) && ComDrivers.isConstant(tagFactory);
		
		Supplier<FluidStack> primaryFactory = () ->
		{
			var fluid = new FluidStack(
					BuiltInRegistries.FLUID.getValue(Resources.location(itemIdFactory.get())),
					1
			);
			
			var tag = tagFactory.get();
			if(tag != null && !tag.isBlank())
			{
				CodecHelper.parseRegistryJson(TagsUpdateListener.getRegistryAccess(), DataComponentPatch.CODEC, tag)
						.ifLeft(fluid::applyComponents)
						.ifRight(err ->
								fluid.set(DataComponents.CUSTOM_NAME, Component.literal(err).setStyle(Style.EMPTY.withColor(ChatFormatting.RED)))
						);
				}
				return fluid;
			};
			
			var intermFactory = constant ? Suppliers.memoize(primaryFactory::get) : primaryFactory;
			
			AtomicInteger count = new AtomicInteger(1);
			finalFactory = () ->
			{
				var stack = intermFactory.get();
				stack.setAmount(count.get());
				return stack;
			};
			dynCount = driveInt(ctx, KEY_COUNT, 1, false, count::set);
		}
		
		GuiFluidObject o = new GuiFluidObject(name, finalFactory);
		self.set(o);
		
		driveInt(ctx, KEY_CAPACITY, 1, dynCount, o::capacity);
		driveBool(ctx, KEY_HOVERABLE, false, false, o::hoverable);
		driveBool(ctx, KEY_OFFER_INGREDIENT, false, false, o::provideIngredient);
		driveString(ctx, KEY_FLUID_TEXTURE, "still", false, t -> o.textureType(MoreObjects.firstNonNull(FluidTextureType.fromString(t), FluidTextureType.STILL)));
		driveBool(ctx, KEY_SHOW_CAPACITY, false, false, o::showCapacity);
		
		return o;
	}
}