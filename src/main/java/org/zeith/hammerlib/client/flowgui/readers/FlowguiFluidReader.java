package org.zeith.hammerlib.client.flowgui.readers;

import com.google.common.base.MoreObjects;
import com.google.common.base.Suppliers;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import lombok.SneakyThrows;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.nbt.TagParser;
import net.minecraft.util.Mth;
import net.minecraftforge.fluids.FluidStack;
import org.zeith.hammerlib.abstractions.props.KeyMap;
import org.zeith.hammerlib.annotations.ide.*;
import org.zeith.hammerlib.api.data.IDataNode;
import org.zeith.hammerlib.client.flowgui.objects.GuiFluidObject;
import org.zeith.hammerlib.client.flowgui.reader.*;
import org.zeith.hammerlib.client.utils.FluidTextureType;
import org.zeith.hammerlib.proxy.HLConstants;
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
		
		var itemIdFactory = ComDrivers.readString(ctx, KEY_FLUID);
		var tagFactory = ComDrivers.readString(ctx, KEY_NBT);
		
		boolean constant = ComDrivers.isConstant(itemIdFactory) && ComDrivers.isConstant(tagFactory);
		
		Supplier<FluidStack> primaryFactory = () ->
		{
			var fluid = new FluidStack(BuiltInRegistries.FLUID
					.get(Resources.location(itemIdFactory.get())), 1
			);
			
			var tag = tagFactory.get();
			if(tag != null && !tag.isBlank())
			{
				try
				{
					fluid.setTag(TagParser.parseTag(tag));
				} catch(CommandSyntaxException e)
				{
				}
			}
			return fluid;
		};
		
		var intermFactory = constant ? Suppliers.memoize(primaryFactory::get) : primaryFactory;
		
		AtomicInteger count = new AtomicInteger(1);
		Supplier<FluidStack> finalFactory = () ->
		{
			var stack = intermFactory.get();
			stack.setAmount(count.get());
			return stack;
		};
		
		GuiFluidObject o = new GuiFluidObject(name, finalFactory);
		self.set(o);
		
		AtomicInteger capacity = new AtomicInteger();
		boolean dynCount = driveInt(ctx, KEY_COUNT, 1, false, count::set);
		boolean dynCapacity = driveInt(ctx, KEY_CAPACITY, 1, dynCount, cap ->
				{
					o.fill(Mth.clamp(count.get() / Math.max(1F, cap), 0F, 1F));
					capacity.set(cap);
				}
		);
		driveBool(ctx, KEY_HOVERABLE, false, false, o::hoverable);
		driveBool(ctx, KEY_OFFER_INGREDIENT, false, false, o::provideIngredient);
		driveString(ctx, KEY_FLUID_TEXTURE, "still", false, t -> o.textureType(MoreObjects.firstNonNull(FluidTextureType.fromString(t), FluidTextureType.STILL)));
		driveBool(ctx, KEY_SHOW_CAPACITY, false, dynCapacity, b -> o.capacity(b ? capacity.get() : null));
		
		return o;
	}
}