package org.zeith.hammerlib.compat.jei;

import com.mojang.blaze3d.platform.InputConstants;
import org.jetbrains.annotations.Nullable;

import java.util.Optional;

public interface IJeiPluginHL
{
	static IJeiPluginHL get()
	{
		return Container.active;
	}
	
	<T> Optional<T> getIngredientUnderMouseJEI(Class<T> type);
	
	@Nullable
	JeiKeyRole getRoleForKey(InputConstants.Key key);
	
	void showRecipes(Object o);
	
	void showUses(Object o);
	
	class Container
	{
		static IJeiPluginHL active = new IJeiPluginHL()
		{
			@Override
			public <T> Optional<T> getIngredientUnderMouseJEI(Class<T> type)
			{
				return Optional.empty();
			}
			
			@Override
			public @Nullable JeiKeyRole getRoleForKey(InputConstants.Key key)
			{
				return null;
			}
			
			@Override
			public void showRecipes(Object o)
			{
			}
			
			@Override
			public void showUses(Object o)
			{
			}
		};
	}
}
