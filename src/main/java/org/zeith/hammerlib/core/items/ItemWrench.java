package org.zeith.hammerlib.core.items;

import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.context.UseOnContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;
import org.zeith.api.wrench.IWrenchItem;
import org.zeith.hammerlib.annotations.RegistryName;
import org.zeith.hammerlib.annotations.SimplyRegister;
import org.zeith.hammerlib.core.adapter.TagAdapter;
import org.zeith.hammerlib.core.init.TagsHL;
import org.zeith.hammerlib.proxy.HLConstants;

@SimplyRegister
public class ItemWrench
		extends Item
		implements IWrenchItem
{
	@RegistryName("wrench")
	public static final ItemWrench WRENCH = new ItemWrench(new Properties().tab(HLConstants.HL_TAB).stacksTo(1));
	
	public ItemWrench(Properties props)
	{
		super(props);
		TagAdapter.bindStaticTag(TagsHL.Items.TOOLS_WRENCH, this);
	}
	
	@Override
	public void playWrenchSound(UseOnContext context)
	{
		Level world = context.getLevel();
		if(!world.isClientSide && world instanceof ServerLevel)
		{
			Vec3 e = context.getClickLocation();
			world.playSound(null, e.x, e.y, e.z, SoundEvents.TRIPWIRE_CLICK_ON, SoundSource.PLAYERS, 1.0F, 1.5F);
		}
	}
}