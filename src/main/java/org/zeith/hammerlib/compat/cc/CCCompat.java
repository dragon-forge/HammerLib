package org.zeith.hammerlib.compat.cc;

import dan200.computercraft.api.ComputerCraftAPI;
import dan200.computercraft.shared.ModRegistry;
import net.neoforged.fml.javafmlmod.FMLJavaModLoadingContext;
import net.neoforged.neoforge.capabilities.RegisterCapabilitiesEvent;
import org.zeith.api.blocks.redstone.RedstoneBundleCapability;
import org.zeith.hammerlib.compat.base.BaseCompat;
import org.zeith.hammerlib.compat.base._hl.BaseHLCompat;


@BaseCompat.LoadCompat(
		modid = ComputerCraftAPI.MOD_ID,
		compatType = BaseHLCompat.class
)
public class CCCompat
		extends BaseHLCompat
{
	public CCCompat()
	{
		ComputerCraftAPI.registerBundledRedstoneProvider(new HammerLibCCRedstoneProvider());
		FMLJavaModLoadingContext.get().getModEventBus().addListener(this::tileCaps);
	}
	
	private void tileCaps(RegisterCapabilitiesEvent evt)
	{
		evt.registerBlockEntity(RedstoneBundleCapability.BLOCK,
				ModRegistry.BlockEntities.COMPUTER_NORMAL.get(),
				ComputerBundleProvider::new
		);
		evt.registerBlockEntity(RedstoneBundleCapability.BLOCK,
				ModRegistry.BlockEntities.COMPUTER_ADVANCED.get(),
				ComputerBundleProvider::new
		);
		evt.registerBlockEntity(RedstoneBundleCapability.BLOCK,
				ModRegistry.BlockEntities.COMPUTER_COMMAND.get(),
				ComputerBundleProvider::new
		);
	}
}