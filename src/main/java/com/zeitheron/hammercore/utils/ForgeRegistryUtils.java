package com.zeitheron.hammercore.utils;

import java.lang.reflect.Field;
import java.util.Collection;
import java.util.Map;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.google.common.collect.BiMap;
import com.google.common.collect.Multimap;

import net.minecraft.block.Block;
import net.minecraft.item.Item;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.IntIdentityHashBiMap;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.registry.RegistryNamespaced;
import net.minecraft.util.registry.RegistrySimple;
import net.minecraftforge.registries.ForgeRegistry;
import net.minecraftforge.registries.GameData;
import net.minecraftforge.registries.IForgeRegistry;
import net.minecraftforge.registries.IForgeRegistryEntry;

/**
 * I would call it hacks class instead, btw.
 */
public class ForgeRegistryUtils
{
	public static final Logger LOGGER = LogManager.getLogger();
	
	public static <V extends IForgeRegistryEntry<V>> V deleteEntry(IForgeRegistry<V> registry, ResourceLocation path)
	{
		// Check if it is a default forge registry
		if(!ForgeRegistry.class.isAssignableFrom(registry.getClass()))
			return null;
		
		try
		{
			BiMap<Integer, V> ids = null;
			BiMap<ResourceLocation, V> names = null;
			Multimap<ResourceLocation, V> overrides = null;
			BiMap<V, ?> ownersReverse = null;
			Map<ResourceLocation, ResourceLocation> aliases = null;
			Map<ResourceLocation, ?> slaves = null;
			
			Field f = ForgeRegistry.class.getDeclaredField("ids");
			f.setAccessible(true);
			ids = (BiMap<Integer, V>) f.get(registry);
			
			f = ForgeRegistry.class.getDeclaredField("aliases");
			f.setAccessible(true);
			aliases = (Map<ResourceLocation, ResourceLocation>) f.get(registry);
			
			f = ForgeRegistry.class.getDeclaredField("names");
			f.setAccessible(true);
			names = (BiMap<ResourceLocation, V>) f.get(registry);
			
			f = ForgeRegistry.class.getDeclaredField("overrides");
			f.setAccessible(true);
			overrides = (Multimap<ResourceLocation, V>) f.get(registry);
			
			f = ForgeRegistry.class.getDeclaredField("owners");
			f.setAccessible(true);
			ownersReverse = ((BiMap<?, V>) f.get(registry)).inverse();
			
			f = ForgeRegistry.class.getDeclaredField("slaves");
			f.setAccessible(true);
			slaves = (Map<ResourceLocation, ?>) f.get(registry);
			
			V prev = names.remove(path);
			
			ResourceLocation alias = aliases.remove(path);
			Collection<V> ovs = overrides.removeAll(path);
			Object overrideOwner = ownersReverse.remove(prev);
			int id = ids.inverse().remove(prev);
			
			LOGGER.info("Removed entry \"" + path + "\" from registry of " + registry.getRegistrySuperType().getName() + ". Info: Alias - \"" + alias + "\"; Overrides removed: " + ovs.size() + "; ID: " + id + "; OverrideOwner: " + overrideOwner + "; Registry Slaves: " + slaves.keySet());
			
			if(prev instanceof Block)
			{
				GameData.getBlockItemMap().remove(prev);
				LOGGER.info("Removed Block-to-Item mapping for Block " + prev.getRegistryName());
			} else if(prev instanceof Item)
			{
				GameData.getBlockItemMap().inverse().remove(prev);
				LOGGER.info("Removed Block-to-Item mapping for Block " + prev.getRegistryName());
			}
			
			return prev;
		} catch(Throwable err)
		{
			err.printStackTrace();
		}
		
		return null;
	}
	
	public static RegistryNamespaced<ResourceLocation, Class<? extends TileEntity>> getTileEntityRegistry()
	{
		RegistryNamespaced<ResourceLocation, Class<? extends TileEntity>> registry = null;
		
		Field f = TileEntity.class.getDeclaredFields()[1];
		f.setAccessible(true);
		try
		{
			registry = (RegistryNamespaced<ResourceLocation, Class<? extends TileEntity>>) f.get(null);
		} catch(IllegalArgumentException | IllegalAccessException e)
		{
			e.printStackTrace();
		}
		
		return registry;
	}
	
	public static void overrideTileEntity(String id, Class<? extends TileEntity> type)
	{
		RegistryNamespaced<ResourceLocation, Class<? extends TileEntity>> registry = getTileEntityRegistry();
		removeKey(registry, new ResourceLocation(id));
		registry.putObject(new ResourceLocation(id), type);
	}
	
	public static <K, V> V removeKey(RegistryNamespaced<K, V> registry, K key)
	{
		V val = registry.getObject(key);
		
		IntIdentityHashBiMap<V> ids = null;
		Map<V, K> inverseObjectRegistry = null;
		Map<K, V> registryObjects = null;
		
		try
		{
			Field f = RegistryNamespaced.class.getDeclaredFields()[0];
			f.setAccessible(true);
			ids = (IntIdentityHashBiMap<V>) f.get(registry);
			{
				final IntIdentityHashBiMap<V> idsf = ids;
				
				IntIdentityHashBiMap<V> ids2 = new IntIdentityHashBiMap<V>(256);
				
				ids.forEach(va ->
				{
					if(va != val) // Skip the value that we return
						ids2.put(va, idsf.getId(va));
				});
				
				FinalFieldHelper.setFinalField(f, registry, ids2);
			}
			
			f = RegistryNamespaced.class.getDeclaredFields()[1];
			f.setAccessible(true);
			inverseObjectRegistry = (Map<V, K>) f.get(registry);
			inverseObjectRegistry.remove(val);
			
			f = RegistrySimple.class.getDeclaredFields()[1];
			f.setAccessible(true);
			registryObjects = (Map<K, V>) f.get(registry);
			registryObjects.remove(key);
			
			// Reset Object[] values
			f = RegistrySimple.class.getDeclaredFields()[2];
			f.setAccessible(true);
			f.set(registry, null);
			
			LOGGER.warn("WARNING: Removed \"" + val + "\" from registry \"" + registry + "\"");
			
			return val;
		} catch(Throwable err)
		{
			err.printStackTrace();
		}
		
		return null;
	}
}