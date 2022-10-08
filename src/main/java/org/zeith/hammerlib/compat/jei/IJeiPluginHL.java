package org.zeith.hammerlib.compat.jei;

import java.util.Optional;

public interface IJeiPluginHL
{
	static IJeiPluginHL get()
	{
		return Container.active;
	}
	
	<T> Optional<T> getIngredientUnderMouseJEI(Class<T> type);
	
	class Container
	{
		static IJeiPluginHL active = new IJeiPluginHL()
		{
			@Override
			public <T> Optional<T> getIngredientUnderMouseJEI(Class<T> type)
			{
				return Optional.empty();
			}
		};
	}
}
