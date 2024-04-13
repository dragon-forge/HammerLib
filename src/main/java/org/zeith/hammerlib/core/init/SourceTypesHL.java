package org.zeith.hammerlib.core.init;

import com.zeitheron.hammercore.annotations.*;
import org.zeith.hammerlib.abstractions.sources.*;

@SimplyRegister
public interface SourceTypesHL
{
	@RegistryName("entity")
	EntitySourceType ENTITY_TYPE = new EntitySourceType();
	
	@RegistryName("tile")
	TileSourceType TILE_TYPE = new TileSourceType();
}