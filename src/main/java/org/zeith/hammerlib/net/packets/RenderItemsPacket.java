package org.zeith.hammerlib.net.packets;

import net.minecraft.client.Minecraft;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.chat.Component;
import net.neoforged.api.distmarker.*;
import org.zeith.hammerlib.client.render.item.Stack2ImageRenderer;
import org.zeith.hammerlib.net.*;

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
	public void write(FriendlyByteBuf buf)
	{
		buf.writeInt(mode);
		buf.writeInt(size);
		buf.writeUtf(data);
	}
	
	@Override
	public void read(FriendlyByteBuf buf)
	{
		mode = buf.readInt();
		size = buf.readInt();
		data = buf.readUtf();
	}
	
	@Override
	@OnlyIn(Dist.CLIENT)
	public void clientExecute(PacketContext ctx)
	{
		if(mode == 0)
			Stack2ImageRenderer.renderItem(Component.literal("Main hand"), Minecraft.getInstance().player.getMainHandItem(), size);
		if(mode == 1)
			Stack2ImageRenderer.renderMod(data, size);
		if(mode == 2)
			Stack2ImageRenderer.renderAll(size);
	}
}