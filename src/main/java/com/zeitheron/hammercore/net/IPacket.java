package com.zeitheron.hammercore.net;

import java.util.function.Supplier;

import com.zeitheron.hammercore.utils.NPEUtils;

import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.INetHandler;
import net.minecraft.network.play.INetHandlerPlayClient;
import net.minecraft.network.play.INetHandlerPlayServer;
import net.minecraft.util.math.Vec3d;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

/**
 * New experimental network system that will be defaulted in MC 1.13.
 */
public interface IPacket
{
	default void readFromNBT(NBTTagCompound nbt)
	{
		
	}
	
	default void writeToNBT(NBTTagCompound nbt)
	{
		
	}
	
	default IPacket execute(Side side, PacketContext ctx)
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
	default IPacket executeOnClient(PacketContext net)
	{
		return refractSidedToUniversal() ? execute(Side.CLIENT, net) : null;
	}
	
	default IPacket executeOnServer(PacketContext net)
	{
		return refractSidedToUniversal() ? execute(Side.SERVER, net) : null;
	}
	
	default boolean executeOnMainThread()
	{
		MainThreaded mt = getClass().getAnnotation(MainThreaded.class);
		if(mt == null)
			mt = getClass().getDeclaredAnnotation(MainThreaded.class);
		return mt != null && mt.value();
	}
	
	/**
	 * Use in static { } body to add handler to this packet.
	 */
	static <T extends IPacket> void handle(Class<T> t, Supplier<T> nev)
	{
		HCNet.INSTANCE.handle(t, nev);
	}
	
	public static final class Helper
	{
		private Helper()
		{
			NPEUtils.noInstancesError();
		}
		
		public static Vec3d getVec3d(NBTTagCompound nbt, String key)
		{
			return new Vec3d(nbt.getFloat(key + "X"), nbt.getFloat(key + "Y"), nbt.getFloat(key + "Z"));
		}
		
		public static NBTTagCompound setVec3d(NBTTagCompound nbt, String key, Vec3d vec)
		{
			nbt.setFloat(key + "X", (float) vec.x);
			nbt.setFloat(key + "Y", (float) vec.y);
			nbt.setFloat(key + "Z", (float) vec.z);
			return nbt;
		}
	}
}