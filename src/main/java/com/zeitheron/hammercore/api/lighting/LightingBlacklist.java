package com.zeitheron.hammercore.api.lighting;

import java.util.ArrayList;
import java.util.List;

import net.minecraft.entity.Entity;
import net.minecraft.entity.effect.EntityLightningBolt;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.tileentity.TileEntityEndGateway;
import net.minecraft.tileentity.TileEntityEndPortal;

public class LightingBlacklist
{
	private static final List<Class<? extends TileEntity>> TILE_BLACKLIST = new ArrayList<>();
	private static final List<Class<? extends Entity>> ENTITIES_BLACKLIST = new ArrayList<>();
	
	static
	{
		registerShadedTile(TileEntityEndPortal.class);
		registerShadedTile(TileEntityEndGateway.class);
		
		registerShadedEntity(EntityLightningBolt.class);
	}
	
	public static void registerShadedEntity(Class<? extends Entity> blacklist)
	{
		if(!ENTITIES_BLACKLIST.contains(blacklist))
			ENTITIES_BLACKLIST.add(blacklist);
	}
	
	public static void registerShadedTile(Class<? extends TileEntity> blacklist)
	{
		if(!TILE_BLACKLIST.contains(blacklist))
			TILE_BLACKLIST.add(blacklist);
	}
	
	public static List<Class<? extends TileEntity>> getTileBlacklist()
	{
		return TILE_BLACKLIST;
	}
	
	public static List<Class<? extends Entity>> getEntitiesBlacklist()
	{
		return ENTITIES_BLACKLIST;
	}
	
	public static boolean blocksShader(TileEntity tile)
	{
		if(tile == null)
			return false;
		Class<? extends TileEntity> tt = tile.getClass();
		return TILE_BLACKLIST.stream().filter(c -> c.isAssignableFrom(tt)).findAny().isPresent();
	}
	
	public static boolean blocksShader(Entity entity)
	{
		if(entity == null)
			return false;
		Class<? extends Entity> tt = entity.getClass();
		return ENTITIES_BLACKLIST.stream().filter(c -> c.isAssignableFrom(tt)).findAny().isPresent();
	}
}