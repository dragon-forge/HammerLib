package org.zeith.hammerlib.mixins.client.render;

import net.minecraft.client.renderer.entity.state.PlayerRenderState;
import net.minecraft.resources.ResourceLocation;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.Mixin;
import org.zeith.hammerlib.api.client.IEmissivePlayerInfo;
import org.zeith.hammerlib.api.client.IEmissivePlayerState;

@Mixin(PlayerRenderState.class)
public class PlayerRenderStateMixin
		implements IEmissivePlayerState
{
	public IEmissivePlayerInfo hl$emissiveInfo;
	
	@Override
	public IEmissivePlayerInfo getEmissivePlayerInfo()
	{
		return hl$emissiveInfo;
	}
	
	@Override
	public void setEmissivePlayerInfo(IEmissivePlayerInfo info)
	{
		hl$emissiveInfo = info;
	}
}