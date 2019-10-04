package com.zeitheron.hammercore.api.inconnect;

import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.HashSet;
import java.util.Set;

import com.google.common.base.Predicates;
import com.zeitheron.hammercore.api.blocks.INoBlockstate;

import net.minecraft.block.Block;
import net.minecraft.client.resources.IResourcePack;
import net.minecraft.client.resources.data.IMetadataSection;
import net.minecraft.client.resources.data.MetadataSerializer;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.common.Loader;
import net.minecraftforge.fml.common.registry.ForgeRegistries;

public class EmptyModelPack implements IResourcePack
{
	final Set<String> domains = new HashSet<>();
	final Set<ResourceLocation> resources = new HashSet<>();
	
	{
		domains.addAll(Loader.instance().getIndexedModList().keySet());
	}
	
	public void empty(Block blk)
	{
		ResourceLocation rl = blk.getRegistryName();
		domains.add(rl.getNamespace());
		resources.add(rl = new ResourceLocation(rl.getNamespace(), "blockstates/" + rl.getPath() + ".json"));
	}
	
	public void init()
	{
		ForgeRegistries.BLOCKS.getValuesCollection().stream() //
		        .filter(Predicates.instanceOf(INoBlockstate.class)) //
		        .forEach(this::empty);
	}
	
	@Override
	public InputStream getInputStream(ResourceLocation location) throws IOException
	{
		if(resources.contains(location))
			return new ByteArrayInputStream("{\"variants\":{\"normal\":{\"model\":\"hammercore:empty\"}}}".getBytes());
		throw new FileNotFoundException(location.toString());
	}
	
	@Override
	public boolean resourceExists(ResourceLocation location)
	{
		return resources.contains(location);
	}
	
	@Override
	public Set<String> getResourceDomains()
	{
		return domains;
	}
	
	@Override
	public <T extends IMetadataSection> T getPackMetadata(MetadataSerializer metadataSerializer, String metadataSectionName) throws IOException
	{
		return null;
	}
	
	@Override
	public BufferedImage getPackImage() throws IOException
	{
		return null;
	}
	
	@Override
	public String getPackName()
	{
		return "HammerCore Connected Models";
	}
}