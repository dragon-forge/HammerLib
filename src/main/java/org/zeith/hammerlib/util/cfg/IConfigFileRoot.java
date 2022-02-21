package org.zeith.hammerlib.util.cfg;

import org.zeith.hammerlib.util.cfg.entries.ConfigEntryCategory;

public interface IConfigFileRoot
{
	ConfigEntryCategory getCategory(String s);
}