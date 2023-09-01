package org.zeith.hammerlib.core.init;

import org.zeith.hammerlib.abstractions.sources.*;
import org.zeith.hammerlib.annotations.*;

@SimplyRegister
public interface SourceTypesHL
{
	@RegistryName("entity")
	EntitySourceType ENTITY_TYPE = new EntitySourceType();
	
	@RegistryName("tile")
	TileSourceType TILE_TYPE = new TileSourceType();
}