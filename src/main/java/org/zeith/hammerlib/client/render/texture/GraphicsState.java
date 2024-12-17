package org.zeith.hammerlib.client.render.texture;

import lombok.Getter;
import lombok.Setter;
import net.minecraft.util.ARGB;

@Getter
@Setter
public class GraphicsState
{
	public float zLevel = 0F;
	public int color = ARGB.white(1F);
}