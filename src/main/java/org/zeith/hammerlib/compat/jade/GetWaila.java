package org.zeith.hammerlib.compat.jade;

import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntity;
import org.zeith.hammerlib.proxy.HLConstants;
import snownee.jade.api.*;

@WailaPlugin
public class GetWaila
		implements IWailaPlugin
{
	public GetWaila()
	{
		HLConstants.enableHammerLibTooltipEngine = false;
	}
	
	@Override
	public void register(IWailaCommonRegistration registration)
	{
		registration.registerBlockDataProvider(new WailaBlockProviderNBT(), BlockEntity.class);
	}
	
	@Override
	public void registerClient(IWailaClientRegistration registration)
	{
		registration.registerBlockIcon(WailaBlockRenderer.INSTANCE, Block.class);
		registration.registerBlockComponent(WailaBlockRenderer.INSTANCE, Block.class);
	}
}