package org.zeith.hammerlib.compat.shimmer;

import net.minecraft.client.renderer.RenderType;
import net.minecraft.resources.ResourceLocation;
import org.zeith.hammerlib.compat.base.Ability;
import org.zeith.hammerlib.compat.base.BaseCompat;
import org.zeith.hammerlib.compat.base._hl.BaseHLCompat;
import org.zeith.hammerlib.compat.base._hl.BloomAbilityBase;

import java.util.Optional;
import java.util.function.Supplier;

@BaseCompat.LoadCompat(
		modid = "shimmer",
		compatType = BaseHLCompat.class
)
public class ShimmerCompat
		extends BaseHLCompat
{
	public final BloomAbilityBase bloom = new BloomAbilityBase()
	{
		@Override
		protected Supplier<Supplier<ClientBloomAbilityBase>> forClient()
		{
			return () -> () -> new ClientBloomAbilityBase()
			{
				@Override
				public RenderType emissiveTranslucentArmor(ResourceLocation resourceLocation)
				{
					return RenderType.entityTranslucentEmissive(resourceLocation);
				}
			};
		}
	};
	
	@Override
	public <R> Optional<R> getAbility(Ability<R> ability)
	{
		return ability.findIn(
				bloom
		);
	}
}