package com.zeitheron.hammercore.netv2;

import java.util.function.Supplier;

import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.INetHandler;
import net.minecraft.network.play.INetHandlerPlayClient;
import net.minecraft.network.play.INetHandlerPlayServer;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

/**
 * New experimental network system that will be defaulted in MC 1.13.
 */
public interface IV2Packet
{
	default void readFromNBT(NBTTagCompound nbt)
	{
		
	}
	
	default void writeToNBT(NBTTagCompound nbt)
	{
		
	}
	
	default IV2Packet execute(Side side, PacketContext ctx)
	{
		if(side == Side.CLIENT)
			return executeOnClient(ctx);
		return executeOnServer(ctx);
	}
	
	default boolean refractSidedToUniversal()
	{
		return true;
	}
	
	@SideOnly(Side.CLIENT)
	default IV2Packet executeOnClient(PacketContext net)
	{
		return refractSidedToUniversal() ? execute(Side.CLIENT, net) : null;
	}
	
	default IV2Packet executeOnServer(PacketContext net)
	{
		return refractSidedToUniversal() ? execute(Side.SERVER, net) : null;
	}
	
	/**
	 * Use in static { } body to add handler to this packet.
	 */
	static <T extends IV2Packet> void handle(Class<T> t, Supplier<T> nev)
	{
		HCV2Net.INSTANCE.handle(t, nev);
	}
}