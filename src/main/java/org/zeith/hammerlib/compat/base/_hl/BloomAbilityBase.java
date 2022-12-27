package org.zeith.hammerlib.compat.base._hl;

import net.minecraft.client.renderer.RenderType;
import net.minecraft.resources.ResourceLocation;
import org.zeith.hammerlib.compat.base.sided.SidedAbilityBase;

import java.util.function.Supplier;

public abstract class BloomAbilityBase
		extends SidedAbilityBase<BloomAbilityBase.ClientBloomAbilityBase>
{
	public static abstract class ClientBloomAbilityBase
			extends BloomAbilityBase
	{
		public abstract RenderType emissiveTranslucentArmor(ResourceLocation resourceLocation);
		
		@Override
		protected Supplier<Supplier<ClientBloomAbilityBase>> forClient()
		{
			return () -> () -> this;
		}
	}
}