package org.zeith.hammerlib.api.items.tooltip;

import net.minecraft.world.inventory.tooltip.TooltipComponent;

import java.util.List;
import java.util.Optional;
import java.util.stream.Stream;

public record TooltipMulti(AlignAxis axis, int padding, List<TooltipComponent> children)
		implements TooltipComponent
{
	public TooltipMulti(AlignAxis axis, int padding, TooltipComponent... children)
	{
		this(axis, padding, List.of(children));
	}
	
	public static Optional<TooltipComponent> create(AlignAxis axis, int padding, Stream<TooltipComponent> stream)
	{
		var comp = stream.toList();
		return comp.isEmpty() ? Optional.empty() : Optional.of(comp.size() == 1 ? comp.get(0) : new TooltipMulti(axis, padding, comp));
	}
}