package org.zeith.hammerlib.api.crafting.itf;

import java.util.Collection;

@FunctionalInterface
public interface IRecipeReceiver<R>
{
	void onReceive(R recipe);
	
	default IRecipeReceiver<R> combine(IRecipeReceiver<R> other)
	{
		var t = this;
		return r ->
		{
			t.onReceive(r);
			other.onReceive(r);
		};
	}
	
	static <T> IRecipeReceiver<T> combine(Collection<IRecipeReceiver<T>> receivers)
	{
		return r -> receivers.forEach(rcv -> rcv.onReceive(r));
	}
}