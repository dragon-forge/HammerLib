package org.zeith.hammerlib.net.packets;

import net.minecraft.client.Minecraft;
import net.minecraft.network.PacketBuffer;
import net.minecraft.util.text.StringTextComponent;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import org.zeith.hammerlib.net.IPacket;
import org.zeith.hammerlib.net.PacketContext;

public class RenderItemsPacket
		implements IPacket
{
	private int mode, size;
	private String data;

	public RenderItemsPacket()
	{
	}

	public RenderItemsPacket(int mode, int size, String data)
	{
		this.mode = mode;
		this.size = size;
		this.data = data;
	}

	@Override
	public void write(PacketBuffer buf)
	{
		buf.writeInt(mode);
		buf.writeInt(size);
		buf.writeUtf(data);
	}

	@Override
	public void read(PacketBuffer buf)
	{
		mode = buf.readInt();
		size = buf.readInt();
		data = buf.readUtf();
	}

	@Override
	@OnlyIn(Dist.CLIENT)
	public void clientExecute(PacketContext ctx)
	{
//		if(mode == 1)
//			Stack2ImageRenderer.renderMod(data, size);
//		if(mode == 2)
//			Stack2ImageRenderer.renderAll(size);

		Minecraft.getInstance().gui.getChat().addMessage(new StringTextComponent("Rendering items is not yet implemented, sorry!"));
	}
}