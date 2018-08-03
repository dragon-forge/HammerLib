package com.zeitheron.hammercore.net.internal;

import com.zeitheron.hammercore.net.IPacket;
import com.zeitheron.hammercore.net.MainThreaded;
import com.zeitheron.hammercore.net.PacketContext;

import net.minecraft.client.Minecraft;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraft.world.biome.Biome;
import net.minecraft.world.chunk.Chunk;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

@MainThreaded
public class PacketSetBiome implements IPacket
{
	int x, z, id;
	byte biome;
	
	static
	{
		IPacket.handle(PacketSetBiome.class, PacketSetBiome::new);
	}
	
	public PacketSetBiome(int x, int z, int id, byte biome)
	{
		this.x = x;
		this.z = z;
		this.id = id;
		this.biome = biome;
	}
	
	public PacketSetBiome()
	{
	}
	
	@Override
	public void writeToNBT(NBTTagCompound nbt)
	{
		nbt.setIntArray("d", new int[] { x, z, id, biome });
	}
	
	@Override
	public void readFromNBT(NBTTagCompound nbt)
	{
		int[] d = nbt.getIntArray("d");
		x = d[0];
		z = d[1];
		id = d[2];
		biome = (byte) d[3];
	}
	
	@Override
	@SideOnly(Side.CLIENT)
	public IPacket executeOnClient(PacketContext net)
	{
		BlockPos pos = new BlockPos(x, 0, z);
		World world = Minecraft.getMinecraft().world;
		
		if(world == null)
			return null;
		
		Chunk c = world.getChunk(pos);
		
		if(c == null)
			return null;
		
		byte[] blockBiomeArray = c.getBiomeArray();
		blockBiomeArray[id] = biome;
		c.setBiomeArray(blockBiomeArray);
		
		return null;
	}
}