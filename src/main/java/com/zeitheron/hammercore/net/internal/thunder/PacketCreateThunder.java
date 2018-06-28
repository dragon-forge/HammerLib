package com.zeitheron.hammercore.net.internal.thunder;

import com.zeitheron.hammercore.client.particle.def.thunder.ThunderHelper;
import com.zeitheron.hammercore.net.IPacket;
import com.zeitheron.hammercore.net.PacketContext;

import net.minecraft.client.Minecraft;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.math.Vec3d;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class PacketCreateThunder implements IPacket
{
	public Vec3d start, end;
	public Thunder base;
	public Thunder.Layer core, aura;
	
	static
	{
		IPacket.handle(PacketCreateThunder.class, PacketCreateThunder::new);
	}
	
	@Override
	public void writeToNBT(NBTTagCompound nbt)
	{
		nbt.setTag("b", base.serializeNBT());
		nbt.setTag("c", core.serializeNBT());
		nbt.setTag("a", aura.serializeNBT());
		Helper.setVec3d(nbt, "s", start);
		Helper.setVec3d(nbt, "e", end);
	}
	
	@Override
	public void readFromNBT(NBTTagCompound nbt)
	{
		aura = Thunder.Layer.deserializeNBT(nbt.getCompoundTag("a"));
		base = Thunder.deserializeNBT(nbt.getCompoundTag("b"));
		core = Thunder.Layer.deserializeNBT(nbt.getCompoundTag("c"));
		start = Helper.getVec3d(nbt, "s");
		end = Helper.getVec3d(nbt, "e");
	}
	
	@Override
	@SideOnly(Side.CLIENT)
	public IPacket executeOnClient(PacketContext net)
	{
		ThunderHelper.thunder(Minecraft.getMinecraft().world, start, end, base, core, aura, Thunder.Fractal.DEFAULT_FRACTAL);
		return null;
	}
}