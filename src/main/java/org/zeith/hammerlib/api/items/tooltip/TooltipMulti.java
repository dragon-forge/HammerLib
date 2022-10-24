package org.zeith.hammerlib.api.items.tooltip;

import net.minecraft.world.inventory.tooltip.TooltipComponent;

import java.util.List;
import java.util.Optional;
import java.util.stream.Stream;

public record TooltipMulti(List<TooltipComponent> children)
		implements TooltipComponent
{
	public TooltipMulti(TooltipComponent... children)
	{
		this(Stream.of(children).flatMap(TooltipMulti::unwrap).toList());
	}
	
	public static Optional<TooltipComponent> create(Stream<TooltipComponent> stream)
	{
		var comp = stream.flatMap(TooltipMulti::unwrap).toList();
		return comp.isEmpty() ? Optional.empty() : Optional.of(comp.size() == 1 ? comp.get(0) : new TooltipMulti(comp));
	}
	
	public static Stream<TooltipComponent> unwrap(TooltipComponent comp)
	{
		return comp instanceof TooltipMulti multi ? multi.children.stream() : Stream.of(comp);
	}
}