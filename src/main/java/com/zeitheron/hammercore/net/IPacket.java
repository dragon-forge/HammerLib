package com.zeitheron.hammercore.net;

import com.zeitheron.hammercore.lib.zlib.utils.Threading;
import com.zeitheron.hammercore.utils.NPEUtils;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.PacketBuffer;
import net.minecraft.util.math.Vec3d;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import java.io.IOException;
import java.util.function.Supplier;

/**
 * New experimental network system that will be defaulted in MC 1.13.
 * <p>
 * Client -> Server packets may not be larger than 32 KB (32768 bytes) (for safeguard, don't use more than 30 000 bytes)
 * Server -> Client packets may not be larger than ~254 MB (267366480 bytes) (for safeguard, don't use more than 267364000 bytes)
 */
public interface IPacket
{
	default void read(PacketBuffer buf)
			throws IOException
	{
		readFromNBT(buf.readCompoundTag());
	}
	
	default void write(PacketBuffer buf)
	{
		NBTTagCompound tag = new NBTTagCompound();
		writeToNBT(tag);
		buf.writeCompoundTag(tag);
	}
	
	/**
	 * Reads this packet from NBT.
	 *
	 * @param nbt
	 * 		The NBT tag that was read from network.
	 */
	default void readFromNBT(NBTTagCompound nbt)
	{
	}
	
	/**
	 * Writes this packet to NBT. Keep everything as short as possible to make
	 * the network usage as low as possible.
	 *
	 * @param nbt
	 * 		The NBT tag that will be written to network.
	 */
	default void writeToNBT(NBTTagCompound nbt)
	{
	}
	
	/**
	 * Executes this packet on client side.
	 *
	 * @param net
	 * 		A context of this packet. Mostly useless in this case.
	 *
	 * @return Reply packet, or null, if none.
	 */
	@SideOnly(Side.CLIENT)
	default IPacket executeOnClient(PacketContext net)
	{
		return execute(Side.CLIENT, net);
	}
	
	/**
	 * Executes this packet on client side.
	 *
	 * @param net
	 * 		A context of this packet. Mostly useless in this case.
	 */
	@SideOnly(Side.CLIENT)
	default void executeOnClient2(PacketContext net)
	{
		net.withReply(executeOnClient(net));
	}
	
	/**
	 * Executes this packet on server side.
	 *
	 * @param net
	 * 		A context of this packet. Contains sender as an
	 *        {@link EntityPlayerMP}
	 *
	 * @return Reply packet, or null, if none.
	 */
	default IPacket executeOnServer(PacketContext net)
	{
		return execute(Side.SERVER, net);
	}
	
	/**
	 * Executes this packet on server side.
	 *
	 * @param net
	 * 		A context of this packet. Contains sender as an
	 *        {@link EntityPlayerMP}
	 */
	default void executeOnServer2(PacketContext net)
	{
		net.withReply(executeOnServer(net));
	}
	
	default IPacket execute(Side side, PacketContext net)
	{
		return null;
	}
	
	/**
	 * @return Should this packet execute from main thread, or from network thread?
	 */
	default boolean executeOnMainThread()
	{
		return Threading.isMainThreaded(getClass());
	}
	
	/**
	 * Use in static { } body to add handler to this packet.
	 *
	 * @param <T>
	 * 		The packet subclass
	 * @param t
	 * 		The packet class
	 * @param nev
	 * 		The supplier of new instances for this packet.
	 */
	static <T extends IPacket> void handle(Class<T> t, Supplier<T> nev)
	{
		HCNet.INSTANCE.handle(t, nev);
	}
	
	final class Helper
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