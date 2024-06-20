package org.zeith.hammerlib.net.packets;

import net.minecraft.client.Minecraft;
import net.minecraft.core.registries.Registries;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.chat.Component;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.api.distmarker.OnlyIn;
import org.zeith.hammerlib.client.render.item.Stack2ImageRenderer;
import org.zeith.hammerlib.net.IPacket;
import org.zeith.hammerlib.net.PacketContext;
import org.zeith.hammerlib.util.mcf.Resources;

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
		switch(mode)
		{
			case 0 -> Stack2ImageRenderer.renderItem(Component.literal("Main hand"), Minecraft.getInstance().player.getMainHandItem(), size);
			case 1 -> Stack2ImageRenderer.renderMod(data, size);
			case 2 -> Stack2ImageRenderer.renderAll(size);
			case 3 -> ctx.registryAccess().registry(Registries.CREATIVE_MODE_TAB).map(r -> r.get(Resources.location(data))).ifPresent(tab ->
					Stack2ImageRenderer.renderTab(Resources.location(data), tab, size)
			);
		}
	}
}