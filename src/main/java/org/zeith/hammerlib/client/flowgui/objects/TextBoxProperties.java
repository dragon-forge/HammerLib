package org.zeith.hammerlib.client.flowgui.objects;

import net.minecraft.client.gui.components.EditBox;
import net.minecraft.network.chat.Component;
import net.minecraft.util.FormattedCharSequence;

import java.util.function.*;

interface TextBoxProperties<T>
{
	default T canLoseFocus(boolean canLoseFocus)
	{
		return initializer(c -> c.setCanLoseFocus(canLoseFocus));
	}
	
	default T bordered(boolean bordered)
	{
		return initializer(c -> c.setBordered(bordered));
	}
	
	default T editable(boolean editable)
	{
		return initializer(c -> c.setEditable(editable));
	}
	
	default T textShadow(boolean textShadow)
	{
		return initializer(c -> c.setTextShadow(textShadow));
	}
	
	default T responder(Consumer<String> responder)
	{
		return initializer(c -> c.setResponder(responder));
	}
	
	default T filter(Predicate<String> validator)
	{
		return initializer(c -> c.setFilter(validator));
	}
	
	default T suggestion(String suggestion)
	{
		return initializer(c -> c.setSuggestion(suggestion));
	}
	
	default T hint(Component hint)
	{
		return initializer(c -> c.setHint(hint));
	}
	
	default T formatter(BiFunction<String, Integer, FormattedCharSequence> textFormatter)
	{
		return initializer(c -> c.setFormatter(textFormatter));
	}
	
	default T textColor(int textColor)
	{
		return initializer(c -> c.setTextColor(textColor));
	}
	
	default T textColorUneditable(int textColor)
	{
		return initializer(c -> c.setTextColorUneditable(textColor));
	}
	
	default T maxLength(int length)
	{
		return initializer(c -> c.setMaxLength(length));
	}
	
	default T value(String value)
	{
		return initializer(c -> c.setValue(value));
	}
	
	T initializer(Consumer<EditBox> initializer);
}
