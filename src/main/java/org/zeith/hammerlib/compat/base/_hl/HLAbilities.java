package org.zeith.hammerlib.compat.base._hl;

import org.zeith.hammerlib.compat.base.sided.SidedAbility;

public class HLAbilities
{
	public static final SidedAbility<BloomAbilityBase.ClientBloomAbilityBase, BloomAbilityBase> BLOOM = new SidedAbility<>(BloomAbilityBase.class, () -> BloomAbilityBase.ClientBloomAbilityBase.class);
}