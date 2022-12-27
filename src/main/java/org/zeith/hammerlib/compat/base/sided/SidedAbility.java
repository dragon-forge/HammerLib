package org.zeith.hammerlib.compat.base.sided;

import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import org.zeith.hammerlib.compat.base.Ability;

import java.util.Objects;
import java.util.function.Supplier;

public class SidedAbility<CL extends DS, DS extends SidedAbilityBase<CL>>
		extends Ability<DS>
{
	private final Supplier<Class<CL>> clientType;
	
	public SidedAbility(Class<DS> type, Supplier<Class<CL>> clientType)
	{
		super(type);
		this.clientType = clientType;
	}
	
	@OnlyIn(Dist.CLIENT)
	public Supplier<Class<CL>> clientType()
	{
		return clientType;
	}
	
	@Override
	public boolean equals(Object obj)
	{
		if(obj == this) return true;
		if(obj == null || obj.getClass() != this.getClass()) return false;
		var that = (SidedAbility<?, ?>) obj;
		return Objects.equals(this.type(), that.type());
	}
}