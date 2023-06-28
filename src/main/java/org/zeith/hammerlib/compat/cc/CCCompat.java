package org.zeith.hammerlib.compat.cc;

import dan200.computercraft.api.ComputerCraftAPI;
import dan200.computercraft.shared.computer.blocks.AbstractComputerBlockEntity;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.AttachCapabilitiesEvent;
import org.zeith.hammerlib.HammerLib;
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
		MinecraftForge.EVENT_BUS.addGenericListener(BlockEntity.class, this::tileCaps);
	}
	
	private void tileCaps(AttachCapabilitiesEvent<BlockEntity> evt)
	{
		var be = evt.getObject();
		if(be instanceof AbstractComputerBlockEntity sc)
			evt.addCapability(HammerLib.id("cc_bundle"), new BundledCCCapability(sc));
	}
}