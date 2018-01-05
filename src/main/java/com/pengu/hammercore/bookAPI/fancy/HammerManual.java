package com.pengu.hammercore.bookAPI.fancy;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Optional;

import javax.annotation.Nullable;

public class HammerManual
{
	@Nullable
	public static ManualEntry getById(String string)
	{
		Optional<ManualEntry> found = listEntries().stream().filter(ri -> ri.key.equals(string)).findAny();
		return found.isPresent() ? found.get() : null;
	}

	public static List<ManualEntry> listEntries()
	{
		List<ManualEntry> items = new ArrayList<ManualEntry>();
		ManualCategories.manualCategories.values().forEach(cl -> items.addAll(cl.entries.values()));
		return items;
	}
}