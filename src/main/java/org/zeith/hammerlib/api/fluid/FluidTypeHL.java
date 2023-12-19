package org.zeith.hammerlib.api.fluid;

import net.minecraft.resources.ResourceLocation;
import net.neoforged.neoforge.client.extensions.common.IClientFluidTypeExtensions;
import net.neoforged.neoforge.fluids.FluidType;
import org.zeith.hammerlib.api.fml.IRegisterListener;
import org.zeith.hammerlib.proxy.HLConstants;

import java.util.function.Consumer;

/**
 * Simplified FluidType with auto-populated textures.
 * Extend this to avoid registering sprite every time.
 */
public class FluidTypeHL
		extends FluidType
		implements IRegisterListener
{
	protected ResourceLocation
			still = HLConstants.id("block/machine_down"),
			flow = HLConstants.id("block/machine_down");
	
	protected FluidTypeHL(FluidType.Properties properties)
	{
		super(properties);
	}
	
	@Override
	public void onPreRegistered(ResourceLocation id)
	{
		still = new ResourceLocation(id.getNamespace(), "block/" + id.getPath());
		flow = new ResourceLocation(id.getNamespace(), "block/" + id.getPath() + "_flow");
	}
	
	@Override
	public void initializeClient(Consumer<IClientFluidTypeExtensions> consumer)
	{
		consumer.accept(new IClientFluidTypeExtensions()
		{
			@Override
			public ResourceLocation getStillTexture()
			{
				return still;
			}
			
			@Override
			public ResourceLocation getFlowingTexture()
			{
				return flow;
			}
		});
	}
}