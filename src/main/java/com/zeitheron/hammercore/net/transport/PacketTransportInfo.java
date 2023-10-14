package com.zeitheron.hammercore.net.transport;

import com.zeitheron.hammercore.net.IPacket;
import com.zeitheron.hammercore.net.PacketContext;

import net.minecraft.nbt.NBTTagCompound;
import net.minecraftforge.fml.relauncher.Side;

public class PacketTransportInfo implements IPacket
{
	static
	{
		IPacket.handle(PacketTransportInfo.class, PacketTransportInfo::new);
	}
	
	public String id;
	public int length;
	public String clas;
	
	public PacketTransportInfo(String id, String clas, int size)
	{
		this.id = id;
		this.clas = clas;
		this.length = size;
	}
	
	public PacketTransportInfo()
	{
	}
	
	@Override
	public void writeToNBT(NBTTagCompound nbt)
	{
		nbt.setString("i", id);
		nbt.setString("c", clas);
		nbt.setInteger("l", length);
	}
	
	@Override
	public void readFromNBT(NBTTagCompound nbt)
	{
		id = nbt.getString("i");
		clas = nbt.getString("c");
		length = nbt.getInteger("l");
	}
	
	@Override
	public IPacket execute(Side side, PacketContext ctx)
	{
		try
		{
			Class<? extends ITransportAcceptor> cl = Class.forName(clas).asSubclass(ITransportAcceptor.class);
			ITransportAcceptor it = cl.getDeclaredConstructor().newInstance();
			it.setInitialContext(side, ctx);
			new TransportSession(id, cl, null, it, length);
			return new PacketRequestFurther(id, true);
		} catch(Throwable err)
		{
		}
		return new PacketRequestFurther(id, false);
	}
}