package org.zeith.hammerlib.api.multipart;

import net.minecraftforge.registries.ForgeRegistryEntry;

public class MultipartBlock
		extends ForgeRegistryEntry<MultipartBlock>
{
	public MultipartState createNewState()
	{
		return new MultipartState(this);
	}
}