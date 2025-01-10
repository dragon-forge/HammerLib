package org.zeith.hammerlib.compat.jei;

import lombok.AllArgsConstructor;

import java.util.function.Consumer;

@AllArgsConstructor
public enum JeiKeyRole
{
	RECIPES(ing -> IJeiPluginHL.get().showRecipes(ing)),
	USES(ing -> IJeiPluginHL.get().showUses(ing));
	
	private Consumer<Object> sendToJei;
	
	public void sendToJei(Object ingredient)
	{
		sendToJei.accept(ingredient);
	}
}
